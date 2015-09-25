package scanner;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;

import Database.Database;
import Nexus.OptionTrader;

import tools.ProgramCycle;

public class Scanner implements ProgramCycle {
	public static Robot robot;
	private static File directory;
	private static File file;
	private static Runtime rt;
	private static Process pr1, pr2, pr3;
	private static BufferedImage screencapture;
	private static ScannerList scannerList;
	public static Rectangle currentPrice = new Rectangle(675, 260, 150, 40);
	public static Rectangle TimeToBuy = new Rectangle(875, 265, 150, 40);
	public static Rectangle Bankroll = new Rectangle(1780, 90, 120, 40);
	// calendar for DB
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Calendar cal;
	private static String[] outPutValues = new String[4];

	public Scanner() {
		create();
	}

	@Override
	public void create() {
		// instantiate the robot
		try {
			robot = new Robot();
			directory = new File(
					"C:\\Users\\Miroslav\\Desktop\\Workspace\\OptionTraderMain\\OLDBUILD\\Tesseract-OCR\\")
					.getAbsoluteFile();
			rt = Runtime.getRuntime();
			scannerList = new ScannerList();
			// openURL();

		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			saveScreenCaptures();
			readFiles();
			scannerList.run(outPutValues);
			Database.insertEURUS(outPutValues);
			Database.UpdateMobileValues();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ocr();

	}

	public void saveScreenCaptures() throws FileNotFoundException {
		// save the image files
		// System.out.println("save screen capture called");
		rt.runFinalization();

		// CURRENT PRICE
		screencapture = robot.createScreenCapture(currentPrice);
		file = null;
		file = new File(
				"C:\\Users\\Miroslav\\Desktop\\Workspace\\OptionTraderMain\\OLDBUILD\\Scanner\\input\\currentprice.jpg");
		try {

			ImageIO.write(screencapture, "jpg", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TIME TO BUY
		file = null;
		screencapture = robot.createScreenCapture(TimeToBuy);
		file = new File(
				"C:\\Users\\Miroslav\\Desktop\\Workspace\\OptionTraderMain\\OLDBUILD\\Scanner\\input\\timetobuy.jpg");
		try {
			ImageIO.write(screencapture, "jpg", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// BANKROLL -- MAY NOT BE READABLE WITH FONT

		file = null;
		screencapture = robot.createScreenCapture(Bankroll);
		file = new File(
				"C:\\Users\\Miroslav\\Desktop\\Workspace\\OptionTraderMain\\OLDBUILD\\Scanner\\input\\bankroll.jpg");
		try {
			ImageIO.write(screencapture, "jpg", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void ocr() {
		// try reading file
		try {

			pr1 = rt.exec(
					"tesseract C:\\Users\\Miroslav\\Desktop\\Workspace\\OptionTraderMain\\OLDBUILD\\Scanner\\input\\bankroll.jpg C:\\Users\\Miroslav\\Desktop\\Workspace\\OptionTraderMain\\OLDBUILD\\Scanner\\output\\bankroll -1 8",
					null, directory);

			pr2 = rt.exec(
					"tesseract C:\\Users\\Miroslav\\Desktop\\Workspace\\OptionTraderMain\\OLDBUILD\\Scanner\\input\\currentprice.jpg C:\\Users\\Miroslav\\Desktop\\Workspace\\OptionTraderMain\\OLDBUILD\\Scanner\\output\\currentprice -1 8",
					null, directory);

			pr3 = rt.exec(
					"tesseract C:\\Users\\Miroslav\\Desktop\\Workspace\\OptionTraderMain\\OLDBUILD\\Scanner\\input\\timetobuy.jpg C:\\Users\\Miroslav\\Desktop\\Workspace\\OptionTraderMain\\OLDBUILD\\Scanner\\output\\timetobuy -1 8",
					null, directory);
			try {
				pr1.waitFor();
				pr2.waitFor();
				pr3.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void openURL() {
		try {
			Desktop.getDesktop().browse(
					new URI("!!!!INSERT BROKER SITE HERE!!!!!"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static FileReader reader;
	static char[] chars;

	public void readFiles() throws FileNotFoundException {
		file = new File(
				"C:\\Users\\Miroslav\\Desktop\\Workspace\\OptionTraderMain\\OLDBUILD\\Scanner\\output\\currentprice.txt"); // for
		// CURRENT PRICE
		try {
			reader = new FileReader(file);
			chars = new char[(int) file.length()];
			reader.read(chars);
			outPutValues[0] = new String(chars);
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("Scanner:183: "+outPutValues[0]);

		// TIME TO BUY
		file = new File(
				"C:\\Users\\Miroslav\\Desktop\\Workspace\\OptionTraderMain\\OLDBUILD\\Scanner\\output\\timetobuy.txt"); // ex
		try {
			reader = new FileReader(file);
			chars = new char[(int) file.length()];
			reader.read(chars);
			outPutValues[1] = new String(chars);
			reader.close();
		} catch (IOException e) {
		}
		// System.out.println("Scanner:196: "+outPutValues[1]);

		// BANK ROLL
		file = new File(
				"C:\\Users\\Miroslav\\Desktop\\Workspace\\OptionTraderMain\\OLDBUILD\\Scanner\\output\\bankroll.txt"); // ex
		try {
			reader = new FileReader(file);
			chars = new char[(int) file.length()];
			reader.read(chars);
			outPutValues[2] = new String(chars);	
			OptionTrader.BANKROLL = outPutValues[2];
			//outPutValues[2] = "";
			reader.close();
		} catch (IOException e) {
		}
		//System.out.println("Scanner:209: "+outPutValues[2]);
		cal = Calendar.getInstance();
		outPutValues[3] = dateFormat.format(cal.getTime());

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
