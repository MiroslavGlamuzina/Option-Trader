package indicators;

import java.util.ArrayList;
import java.util.Random;

import Nexus.OptionTrader;

public class EMA extends OptionTrader
{
	public static ArrayList<Double> run(ArrayList<Double> y, int period)
	{
		ArrayList<Double> EMA = new ArrayList<Double>();

		double MULTIPLIER =  (2 / ((double)period - (period/1.3)));
		double EMA_PREVIOUS_DAY_TEMP = 1;
		double first_temp = 0;
		double temp = 0;

		for (int i = test.size() - 1; i >= 0; i--)
		{
			if (y.size() - i < period)
			{
				first_temp += y.get(i);
				EMA.add(y.get(i));
			} else if (y.size() - i == period)
			{
				EMA_PREVIOUS_DAY_TEMP = first_temp / (double)period;
				EMA.add(y.get(i));
			}

			else
			{
				temp = (double)(y.get(i) - EMA_PREVIOUS_DAY_TEMP) * MULTIPLIER
						+ EMA_PREVIOUS_DAY_TEMP;
				EMA.add(temp);
				EMA_PREVIOUS_DAY_TEMP = temp;

//				 System.out.println("TEMP "+MULTIPLIER);
				 if(i <= period*(1.5))
				 {
						EMA.set(i, EMA.get(EMA.size()-1));

				 }
			}

		}
		
		// for (int i = 0; i < EMA.size(); i++)
		// {
		// if (EMA.get(i) > 0)
		// {
		// System.out.println(EMA.get(i));
		// }
		// }
		return EMA;

	}

}
