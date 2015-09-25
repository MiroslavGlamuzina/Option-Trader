package indicators;

import java.util.ArrayList;

import Nexus.OptionTrader;

public class STOCHASTIC extends OptionTrader
{
public static ArrayList<Double> D =  new ArrayList<>();

	public static ArrayList<Double> run(ArrayList<Double> y, int period)
	{
		ArrayList<Double> stochastic = new ArrayList<>();
		 D = new ArrayList<>();

		double period_high = 0;
		double period_low = 0;
		double k = 0;
		int period_limit = 0;
		double d = 0;
		// for(int i =0; i < test.get(i); i++)

		for (int i = y.size() - 1; i >= 0; i--)
		{
			if (y.size() - i <= period)
			{
				if (i == y.size() - 1)
				{
					period_high = y.get(i);
					period_low = y.get(i);
				}
				if (period_high < y.get(i))
				{
					period_high = y.get(i);
				}
				if (period_low > y.get(i))
				{
					period_low = y.get(i);
				}
				stochastic.add(y.get(i));
				D.add(y.get(i));

			}
			else
			{
				k = ((y.get(i) - period_low) / (period_high - period_low));
				stochastic.add(k); // add the stochastic%
				
				//%D-value
				for(int z=0; z < period; z++)
				{
					d+= stochastic.get(stochastic.size()-1-z);
				}
				d = d/period;
				D.add(d);
			}
		}
		return stochastic;
	}
}
