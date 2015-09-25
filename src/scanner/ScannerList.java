package scanner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ScannerList {

	ArrayList<String> currentPrice;
	ArrayList<String> timetobuy;
	ArrayList<String> bankroll;
	ArrayList<String> dateAndTime;
	
	
	

	public ScannerList() {
		create();
	}

	public void create() {
		// TODO Auto-generated method stub
		currentPrice = new ArrayList<String>();
		timetobuy = new ArrayList<String>();
		bankroll = new ArrayList<String>();
		dateAndTime = new ArrayList<String>();
	}

	public void run(String[] outPutValues) {
		currentPrice.add(outPutValues[0]);
		timetobuy.add(outPutValues[1]);
		bankroll.add(outPutValues[2]);
		dateAndTime.add(outPutValues[3]);
		
//		System.out.print("currentPrice: " + outPutValues[0]);
//		System.out.print("timeToBuy: " + outPutValues[1]);
//		System.out.print("BankRoll: " + outPutValues[2]);
//		System.out.print("arraylistsize: " + String.valueOf(currentPrice.size()));

		saveToFile();
	}

	File file;
	char[] chars;
	FileWriter writer;
	String tempFileData="";
	final int MAXLISTSIZE = 10;
	public void saveToFile() {
		if (currentPrice.size() >= MAXLISTSIZE) {	
			for(int i = 0; i < currentPrice.size(); i++){
			 file = new File("C:\\Users\\Miroslav\\Desktop\\Workspace\\OptionTraderMain\\OLDBUILD\\Scanner\\history\\optionhistory.txt"); 
			 try {
			 writer = new FileWriter(file, true);
			 writer.write (currentPrice.get(i)+","+timetobuy.get(i)+","+bankroll.get(i)+","+dateAndTime.get(i)+System.lineSeparator());
			 writer.close();
			 } catch (IOException e) {
			 }
			}
			currentPrice.clear();
			timetobuy.clear();
			bankroll.clear();
			dateAndTime.clear();
		}
}

	public void pause() {
		// TODO Auto-generated method stub

	}

	public void resume() {
		// TODO Auto-generated method stub

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void update() {
		// TODO Auto-generated method stub

	}

}
