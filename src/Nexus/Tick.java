package Nexus;

import java.util.Date;


public class Tick {
	public double price;
	public Date time; 
	public String timeToBuy;
	public String CallorPut;
	public double strength;
	public  String winloss; 
	public  double endprice; 
	double bankrolltick; 
	
	public Tick(double price, Date time, String CallorPut, double strength, double bankrolltick){
		this.price = price;
		this.time = time;
		this.CallorPut = CallorPut;
		this.strength = strength;
		this.bankrolltick = bankrolltick;
	}
}
