package indicators;

import java.util.ArrayList;

import Nexus.OptionTrader;

public class RSI extends OptionTrader
{

	public static ArrayList<Double> run(ArrayList<Double> y, int period)
	{
		ArrayList<Double> rsi = new ArrayList<>();
		double RSTEMPAverageGain = 0.000000;
		double RSTEMPAverageLoss = 0.000000;
		double RSGain = 0.000000;
		double RSLoss = 0.000000;
		double lastIndex = 0;
		for (int i = y.size() - 1; i >= 0; i--)
		{
			if (i == y.size() - 1)
			{
				lastIndex = y.get(i);
			}
			if (y.size() - i < period)
			{
				if (lastIndex < y.get(i))
				{
					RSTEMPAverageGain += y.get(i) - lastIndex;
				}
				if (lastIndex > y.get(i))
				{
					RSTEMPAverageLoss += lastIndex - y.get(i);
				}

				rsi.add(50.00);

			} else
			{
				if (y.get(i) > lastIndex)
				{
					RSGain = ((RSTEMPAverageGain) * (period - 1) + Math.abs((y
							.get(i) - lastIndex))) / period;
				}
				if (y.get(i) < lastIndex)
				{
					RSLoss = ((RSTEMPAverageLoss) * (period - 1) + Math.abs((y
							.get(i) - lastIndex))) / period;
				}

				double temp = ((100) - ((100) / (1 + (RSGain / RSLoss))));
				if (temp < 0 || Double.isNaN(temp))
				{
					rsi.add(0.00);
				} else
				{
					rsi.add(((100) - ((100) / (1 + (RSGain / RSLoss)))));
				}

				RSTEMPAverageGain = RSGain;
				RSTEMPAverageLoss = RSLoss;
			}
			lastIndex = y.get(i);
		}

		// RSI = rsi.get(0); first
		RSI = rsi.get(rsi.size() - 1); // last
//		System.out.println(rsi.get(0));
		return rsi;
	}
}
