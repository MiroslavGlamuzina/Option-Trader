package indicators;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Nexus.OptionTrader;

public class Draw extends OptionTrader
{

	public static void DrawThis(ArrayList<Double> y, Graphics2D g2,
			Color color, int type)
	{

		double min = max = 0;
		for (int i = 0; i < y.size(); i++)
		{
			if (i == 0)
			{
				max = y.get(i);
				min = y.get(i);
			}
			if (min > y.get(i))
			{
				min = y.get(i);
			}
			if (max < y.get(i))
			{
				max = y.get(i);
			}
		}
		double range = (max - min);

		if (range > 10)
		{
			min = 0;
			max = 100;
			range = 100;
		}

		int[] currentAlgorithm = new int[y.size()];
		int[] x = new int[y.size()];
		for (int i = 0; i < y.size(); i++)
		{
			double temp = (-(y.get(i) - min) / range);
			// if(temp == 0){temp = ((1- min) / range);}
			currentAlgorithm[i] = (int) (temp * (300) + (bounds.height * 2 + bounds.height));
			x[i] = i;
		}
		// x = OptionTrader.reverseArray(x);
		g2.setStroke(new BasicStroke(2));
		g2.setColor(color);
		if (type == 0)
		{
			g2.drawPolyline(x, currentAlgorithm, test.size());
		}
		if (type == 1)
		{
			g2.fillPolygon(x, currentAlgorithm, test.size());
		}

	}

	
	public static void DrawPrediction(double prediction,Graphics2D g2,
			Color color)
	{
		if(prediction > max)
		{
			max = prediction;
		}
		double range = (max - min);
		double temp = (-(prediction- min) / range);
		
		int y =  (int) (prediction * (300) + (bounds.height * 2 + bounds.height));
		g2.setColor(Color.green);
		g2.fillRect(970, y, 1000, y+50);
	}
	
	
}
// // SMAy[i] = (int) ((-(temp - min) / range) * (300) + (bounds.height * 2 +
// bounds.height));

