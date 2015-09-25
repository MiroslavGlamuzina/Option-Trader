package indicators;

import java.util.ArrayList;

import Nexus.OptionTrader;

public class SMA extends OptionTrader
{

	public static ArrayList<Double> run(ArrayList<Double> input,int period, int ALLIGATOR_INDEX)
	{
		ArrayList<Double> SMAy = new ArrayList<>();
		// for(int i =0 ; i < test.size(); i++){
		for (int i = test.size() - 1; i >= 0; i--)
		{
			if (test.size() - i < period)
			{
				SMAy.add(test.get(i));
			} else
			{
				double temp = 0;
				for (int p = period - 1; p >= 0; p--)
				{
					temp += test.get(i + p);
				}
				temp = temp / (period);
				SMAy.add(temp);
				if (i == 1 || i == 2)
				{
					ALLIGATOR[3] = temp;
				}
				if (i == 0)
				{
					ALLIGATOR[ALLIGATOR_INDEX] = temp;
				}
				
				
//				if (test.size() - i < period)
//				{
//					SMAy[i] = (int) ((-(test.get(i) - min) / range) * (300) + (bounds.height * 2 + bounds.height));
//				} else
//				{
//					double temp = 0;
//					for (int p = period - 1; p >= 0; p--)
//					{
//						temp += test.get(i + p);
//					}
//					temp = temp / (period);
//					SMAy[i] = (int) ((-(temp - min) / range) * (300) + (bounds.height * 2 + bounds.height));
//					if (i == 1 || i == 2)
//					{
//						ALLIGATOR[3] = temp;
//					}
//					if (i == 0)
//					{
//						ALLIGATOR[ALLIGATOR_INDEX] = temp;
//					}
			}
			// visual shift can go here!
		}
//		SMAx = reverseArray(SMAx);
//		// SMAy = reverseArray(SMAy);
//
//		if (color.toLowerCase().equals("red"))
//		{
//			g2.setColor(Color.RED);
//		}
//		if (color.toLowerCase().equals("green"))
//		{
//			g2.setColor(Color.GREEN);
//		}
//		if (color.toLowerCase().equals("blue"))
//		{
//			g2.setColor(Color.BLUE);
//		}
//		g2.setStroke(new BasicStroke(1));
//		g2.drawPolyline(SMAx, SMAy, test.size());
		return SMAy;
	}
}
