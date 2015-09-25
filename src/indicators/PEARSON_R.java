package indicators;

import java.util.ArrayList;

import Nexus.OptionTrader;

public class PEARSON_R extends OptionTrader
{

	public static void run(ArrayList<Double> x, ArrayList<Double> y){
	
		//sum of X -- done
		//sum of Y -- done
		//sum of x^2 -- done
		//sum of y^2 -- done
		//sum of XY 
		//the size of the list
		
		
		//method variables
		double r2 = 0.00;
		double X = 0.00;
		double Y = 0.00;
		double X2 = 0.00;
		double Y2 = 0.00;
		double XY = 0.00;
		int maxIterator = 0;
		
		//get the largest length this will be used to prevent inconsitencies inside of the Arraylists<> sizes
		if(x.size() > y.size())
		{
			maxIterator = y.size();
		}
		else if(y.size() > x.size())
		{
			maxIterator = x.size();
		}
		else
		{
			maxIterator = y.size();
		}
		
		//BEGIN CALC formula
		for(int i =0; i < maxIterator; i ++)
		{
			//x && x2
			X +=(x.get(i));
			X2 +=(x.get(i)*x.get(i));
			
			//y && y2
			Y +=(y.get(i));
			Y2+=(y.get(i)*y.get(i));
			
			XY += (y.get(i) * x.get(i));
		}
	
		
		
		r2 = ((XY) - ((X*Y)/maxIterator))/(Math.sqrt((X2 - ((X*X)/maxIterator))) * Math.sqrt((Y2)-((Y*Y)/(maxIterator)))); 
		
		
		
		
	}
	
	
}
