package indicators;

import java.util.ArrayList;

import Nexus.OptionTrader;

public class MACD extends OptionTrader
{
	public static ArrayList<Double> MACD_LINE;
	public static ArrayList<Double> Signal_line;

	public static ArrayList<Double> run(ArrayList<Double> y)
	{
		ArrayList<Double> EMA1 = indicators.EMA.run(y, 12);
		ArrayList<Double> EMA2 = indicators.EMA.run(y, 26);
		MACD_LINE = new ArrayList<>();

		// calculate the MACD line
		 for (int i = 0; i < EMA1.size(); i++)
		//for (int i = EMA1.size() - 1; i >= 0; i--)

		{
			MACD_LINE.add(EMA1.get(i) - EMA2.get(i));
		}

		// calculate the signal line
		// create this overrided method
		Signal_line = indicators.EMA.run(MACD_LINE, 9);
		Signal_line = OptionTrader.reverseArray(Signal_line);
		ArrayList<Double> MACD_HISTOGRAM = new ArrayList<>();
		
		//for (int i = MACD_LINE.size() - 1; i >= 0; i--)
		for (int i = 0; i < MACD_LINE.size(); i++)

		{
			MACD_HISTOGRAM.add((MACD_LINE.get(i) - Signal_line.get(i))*(-1));
		}
		OptionTrader.MACD_HISTOGRAM = MACD_HISTOGRAM
				.get(MACD_HISTOGRAM.size() - 1);
		//System.out.println(getMax(MACD_HISTOGRAM));
		return MACD_HISTOGRAM;
	}
}
