package Nexus;

import externalController.ExternalController;

//This class will be used for deciding whether to BUY, NOT BUY, and WHY this decision

public class Nexus {
	public static OptionSeries series;
	public static ExternalController robot; 
	//public static TA ta; //start in nexus TA
	
	public Nexus() {
		robot = new ExternalController();
		series = new OptionSeries("EuroUs");
	}

	
	
	public static void Decision(){
		//TODO main loop for decision tree
		if(true)
		{
			Call();
		}
		if(true)
		{
			Put();
		}
		
	}
	
	
	
	public static void Call() {
		// TODO make sure to log percentage of prediction, price, current price
		// and store in db for close price--write new db scheme
		robot.call();
	}

	public static void Put() {
		// TODO make sure to log percentage of prediction, price, current price
		// and store in db for close price--write new db scheme
		robot.put();
	}

}
