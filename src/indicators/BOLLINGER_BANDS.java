package indicators;

import java.util.ArrayList;

import Nexus.OptionTrader;

public class BOLLINGER_BANDS extends OptionTrader
{
	public static ArrayList<Double> BB_Middle = new ArrayList<>();
	public static ArrayList<Double> BB_Lower = new ArrayList<>();
	public static ArrayList<Double> BB_Upper = new ArrayList<>();

	public static void run(ArrayList<Double> y, int period)
	{
		BB_Middle = new ArrayList<>();
		BB_Lower = new ArrayList<>();
		BB_Upper = new ArrayList<>();
		for (int i = y.size() - 1; i >= 0; i--)
		{
			double period_SMA = 0;
			double period_SD = 0;

			if (y.size() - i <= period)
			{
				BB_Middle.add(y.get(i));
				BB_Lower.add(y.get(i));
				BB_Upper.add(y.get(i));
			} else
			{
				// get the period SMA -- AVERAGE
				 for (int z = 0; z < period; z++)
				 {
				 period_SMA += y.get(i + z);
				
				 }
				 period_SMA = period_SMA / period;
				//
				// // get the period SD
				// // determine each ticks deviation
				 for (int z = 0; z < period; z++)
				 {
				 period_SD += (period_SMA - y.get(i + z))
				 * (period_SMA - y.get(i + z));
				 }
				period_SD = Math.sqrt(period_SD);

				// add values to the bollinger bands arraylists
				BB_Middle.add(period_SMA);
				BB_Upper.add((period_SMA) + (period_SD * 2));
				BB_Lower.add((period_SMA) - (period_SD * 2));

				// reset the temporay values
				period_SD = 0;
				period_SMA = 0;
				period_SD = 0;
				// System.out.println(BB_Lower.size());
			}

		}
		OptionTrader.BOLLINGER_BANDS[0] = BB_Upper.get(BB_Upper.size()-1);
		OptionTrader.BOLLINGER_BANDS[1] = BB_Middle.get(BB_Middle.size()-1);
		OptionTrader.BOLLINGER_BANDS[2] = BB_Lower.get(BB_Lower.size()-1);

	}

}
