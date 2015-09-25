package indicators;

import java.util.ArrayList;

import Nexus.OptionTrader;

public class TRSC extends OptionTrader
{
//trend, Seasonality, Cyclic, Randomness
	public static ArrayList<Double> run(ArrayList<Double> y)
	{
		ArrayList<Double> trend = new ArrayList<>();
		ArrayList<Double> seasonality= new ArrayList<>();
		ArrayList<Double> Cyclic = new ArrayList<>();
		ArrayList<Double> Randomness = new ArrayList<>();
		
		//trend 
		//slope
		double slope = (y.get(y.size()-1)- y.get(0))/(-y.size()) ;
		for(int i = y.size()-1; i >=0 ; i --)
		{
			if(i == y.size()-1)
			{
				trend.add(y.get(i));
			}else
			{
				trend.add(y.get(i+1)+slope);
			}
			System.out.println("TREND: "+ trend.get(trend.size()-1) +" Y: "+ y.get(i));
		}
		
		//Cyclic 
		
		
		System.out.println(slope);
		return trend;	
	}
	
	//there are 3 values inserted every second so i have to multiply the graph by three 
	public static ArrayList<Double> predict(ArrayList<Double> y, int timeToAdd)
	{
		//multiply the time to add variable by three
		timeToAdd = timeToAdd*3; 
		ArrayList<Double> trend = new ArrayList<>();
		ArrayList<Double> seasonality= new ArrayList<>();
		ArrayList<Double> Cyclic = new ArrayList<>();
		ArrayList<Double> Randomness = new ArrayList<>();
		
		//trend 
		//slope
		double slope = (y.get(y.size()-1)- y.get(0))/(-y.size()) ;
		//double slope = (y.get((int)(y.size()-1)/2)- y.get((y.size()-1)/2))/(-y.size()) ;
		for(int i = y.size()-1; i >=0 ; i --)
		{
			if(i == y.size()-1)
			{
				trend.add(y.get(i));
			}else
			{
				trend.add(y.get(i+1)+slope);
			}
			//System.out.println("TREND: "+ trend.get(trend.size()-1) +" Y: "+ y.get(i));
		}
		for(int i = 0 ; i < timeToAdd; i++)
		{
			trend.add((double)y.get((y.size()-1)) + slope);
		}
		
		//Cyclic 
		EXPERIMENTAL_INDICATOR = trend.get(trend.size()-1);
		
		System.out.println("Trend size: "+trend.size()+" Predicted Value: "+ trend.get(trend.size()-1)+" Real Value: " + test.get(test.size()-1));
		return trend;	
	}
	
	public static ArrayList<Double> practiceSin(int z)
	{
	ArrayList<Double> sinwave = new ArrayList<>();
	double frequency = 2; 
	
	for(int i = 0 ; i <1000; i++)
	{
		sinwave.add(Math.sin((2 * Math.PI * i * frequency)    /   (1000)));
	}
		
		
		return sinwave;
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
