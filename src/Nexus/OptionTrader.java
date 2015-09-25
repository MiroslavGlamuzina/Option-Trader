package Nexus;

import indicators.Draw;
import indicators.SMA;
import indicators.TRSC;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import scanner.Scanner;
import scanner.ScannerThread;
import tools.FPS;
import tools.ProgramCycle;
import Database.Database;
import externalController.Audio;
import externalController.ExternalController;

public class OptionTrader implements ProgramCycle
{
	// EXTERNAL CLASSES
	public static ScannerThread scannerThread;
	public static ClosePopupCheck closePopupCheck;
	public static Thread popupThread;
	public static Scanner scanner;
	public static FPS fps;
	public static Nexus nexus;
	public static boolean setBackground = false;
	public static ExternalController robot;

	// GUI GRAPH VALUES
	public static ArrayList<Double> test;
	public static Rectangle bounds;
	public static double max;
	public static double min;
	public static double range;
	Calendar CURRENT_TIME;
	public static ArrayList<Integer> finalBuy;
	public static ArrayList<Integer> endofPeriod;
	public static ArrayList<String> timetobuy;

	// GUI GRAPH AND ANIMATION
	public static boolean dragIconVisible = false;
	float dragAnimation = 0;
	public static boolean MINIMIZED = false;
	public static boolean update_MINIMIZED = true;
	float notReadyAnimation = 0;
	int current_wins = 0;
	int current_loss = 0;
	String BankRollStartingValue = "";

	// SYSTEM READY
	public static boolean READY = false;
	public boolean readyOnce = false;

	// INDICATOR VALUES
	public static double[] TREND_LINES = new double[5];
	static double INDICATOR_STRENGTH = 0;
	public static double[] ALLIGATOR = new double[4];
	public static double RSI = 0;
	double CURRENT_PRICE = 0;
	public static int TIMETOADD = 0;
	public static double MACD_HISTOGRAM = 0;
	public static double[] BOLLINGER_BANDS = new double[3]; // 0-upper , 1 -// // middle , 2 lower
	public static double EXPERIMENTAL_INDICATOR= 0.00;

	// INDICATOR PARAMETERS
	public static int RSI_PERIOD = 3;

	// CURRENT INVESTMENTS
	public static ArrayList<Tick> investments;
	public static String BANKROLL = "";

	// CONSOLE
	public static ArrayList<String> CONSOLE_ARRAY = new ArrayList<String>();
	public static String CONSOLE = "";

	public OptionTrader()
	{
		create();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void create()
	{
		// Scanner
		scannerThread = new ScannerThread();
		Thread myThread = new Thread(scannerThread);
		myThread.start();

		scanner = new Scanner();
		fps = new FPS();
		nexus = new Nexus();
		Database.removeMobileCommands();
		closePopupCheck = new ClosePopupCheck();
		popupThread = new Thread(closePopupCheck);
		popupThread.start();

	}

	public void MyInvestments()
	{
		double CallorPut = MasterAlgorithm.init();
		if (!timetobuy.get(timetobuy.size() - 1).trim()
				.equals(new String("00:00").trim()))
		{
			if (investments.size() < MasterAlgorithm.MAX_CONCURRENT_INVESTMENTS)
			{
				if (CallorPut > 0)
				{
					double bank;
					try
					{
						bank = Double.parseDouble(BANKROLL.trim().substring(1)
								.trim().replace(" ", "").replace(".", "."));
					} catch (NumberFormatException e)
					{
						// TODO Auto-generated catch block
						bank = 0;
					}
					// robot.ClosePopUp();
					System.out.println("!BUY!");
					CURRENT_TIME = Calendar.getInstance();
					CURRENT_TIME.add(Calendar.SECOND, (TIMETOADD));
					investments.add(new Tick(CURRENT_PRICE, CURRENT_TIME
							.getTime(), "call", CallorPut, bank));
					robot.ClosePopUp();
					sleep();
					robot.call();
					update_MINIMIZED = true;

				}

				if (CallorPut < 0)
				{
					double bank;
					try
					{
						bank = Double.parseDouble(BANKROLL.trim().substring(1)
								.trim().replace(" ", "").replace(".", "."));
					} catch (NumberFormatException e)
					{
						// TODO Auto-generated catch block
						bank = 0;
					} // robot.ClosePopUp();
					System.out.println("!PUT!");
					CURRENT_TIME = Calendar.getInstance();
					CURRENT_TIME.add(Calendar.SECOND, (TIMETOADD));
					investments.add(new Tick(CURRENT_PRICE, CURRENT_TIME
							.getTime(), "put", CallorPut, bank));
					robot.ClosePopUp();
					sleep();
					robot.put();
					update_MINIMIZED = true;

				}
				// pause for double clicks

			}
		}
		CURRENT_TIME = Calendar.getInstance();

		if (timetobuy.get(timetobuy.size() - 1).trim()
				.equals(new String("00:00").trim()))
		{
			if (!ClosePopupCheck.running)
			{
				Thread popupThread = new Thread(closePopupCheck);
				popupThread.start();
			}
		}
		// Remove Completed Ticks and insert the values into the database as
		// history
		for (int i = 0; i < investments.size(); i++)
		{
			if (CURRENT_TIME.getTime().after(investments.get(i).time))
			{
				double bank;
				try
				{
					bank = Double.parseDouble(BANKROLL.trim().substring(1)
							.trim().replace(" ", "").replace(".", "."));
				} catch (NumberFormatException e)
				{
					// TODO Auto-generated catch block
					bank = 0;
				}
				if (investments.get(i).CallorPut.toLowerCase().equals(
						new String("call").toLowerCase()))
				{
					if (investments.get(i).price <= CURRENT_PRICE)
					{
						// investments.get(i).endprice = CURRENT_PRICE;
						// Database.insertTradingHistory(investments.get(i));
						CONSOLE_ARRAY.add(String.valueOf(bank
								- investments.get(i).bankrolltick));
					} else
					{
						// investments.get(i).endprice = CURRENT_PRICE;
						// Database.insertTradingHistory(investments.get(i));
						CONSOLE_ARRAY.add(String.valueOf(bank
								- investments.get(i).bankrolltick));

					}
					robot.ClosePopUp();
				}
				if (investments.get(i).CallorPut.toLowerCase().equals(
						new String("put").toLowerCase()))
				{
					if (investments.get(i).price >= CURRENT_PRICE)
					{
						// investments.get(i).endprice = CURRENT_PRICE;
						// Database.insertTradingHistory(investments.get(i));
						// CONSOLE_ARRAY.add(String.valueOf(bank
						// - investments.get(i).bankrolltick));

					} else
					{
						// investments.get(i).endprice = CURRENT_PRICE;
						// Database.insertTradingHistory(investments.get(i));
						// CONSOLE_ARRAY.add(String.valueOf(bank
						// - investments.get(i).bankrolltick));

					}

					robot.ClosePopUp();

				}

				// lose
				if (investments.get(i).bankrolltick > bank)
				{
					System.out.println("LOST MONEWY");
					current_loss++;
					MasterAlgorithm.PERCEPTRON_LEARN = true;
					investments.get(i).winloss = "LOSS";
					investments.get(i).endprice = CURRENT_PRICE;
					Database.insertTradingHistory(investments.get(i));
					CONSOLE_ARRAY.add(String.valueOf(bank
							- investments.get(i).bankrolltick));

				} else
				{
					System.out.println("WON MONEWY");
					investments.get(i).winloss = "WIN";
					current_wins++;
					investments.get(i).endprice = CURRENT_PRICE;
					Database.insertTradingHistory(investments.get(i));
					CONSOLE_ARRAY.add(String.valueOf(bank
							- investments.get(i).bankrolltick));

				}
				System.out.println("Tick " + investments.get(i).bankrolltick
						+ " BANK " + bank);
				// win -- do not need it...

				// add the win loss to the array list
				if (CONSOLE_ARRAY.size() > 10)
				{
					for (int z = 10; z < CONSOLE_ARRAY.size(); z++)
					{
						CONSOLE_ARRAY.remove(z);
					}
				}
				CONSOLE = "";
				for (int z = 0; z < CONSOLE_ARRAY.size(); z++)
				{
					CONSOLE += CONSOLE_ARRAY.get(i) + "<br/>";
				}

				MasterAlgorithm.updateVersionStats(MasterAlgorithm.VERSION);
				investments.remove(i);
			}

		}

	}

	public void paintGUI(Graphics g, ImageObserver GUI)
	{
		checkMobileUpdate();
		Graphics2D g2 = (Graphics2D) g;
		paintJFrameBackGround(g2);

		// DRAG
		// display either JPG of screenshot OR drag icon
		// SCREEN SHOTS
		if (!MINIMIZED)
		{
			if (!dragIconVisible)
			{
				dragAnimation = 0;
				g2.drawImage(
						scanner.robot.createScreenCapture(scanner.currentPrice),
						13, 13, GUI);
				g2.drawImage(
						scanner.robot.createScreenCapture(scanner.TimeToBuy),
						150, 13, GUI);
				g2.drawImage(
						scanner.robot.createScreenCapture(scanner.Bankroll),
						288, 14, GUI);
				g2.setStroke(new BasicStroke(5));
				g2.setColor(Color.ORANGE);
				g2.drawRect(10, 10, 400, 48);
			}
		}
		// DRAW ALTERNATIVE GRAPHICS ------------------------------------

		if (update_MINIMIZED)
		{
			dragAnimation = 0;
			g2.setColor(Color.BLACK);
			g2.fillRect(10, 10, 400, 48);
			g2.setColor(Color.ORANGE);
			g2.drawRect(9, 9, 401, 49);
			g2.setColor(Color.ORANGE);
			g2.drawString(String.valueOf(" WIN"), 25, 30);
			g2.drawString(String.valueOf(MasterAlgorithm.VERSION_WIN_COUNT),
					30, 45);

			g2.drawString(String.valueOf("LOSS"), 75, 30);
			g2.drawString(String.valueOf(MasterAlgorithm.VERSION_LOSS_COUNT),
					80, 45);

			g2.drawString(String.valueOf("SUCCESS"), 135, 30);
			g2.drawString(String.valueOf(MasterAlgorithm.VERSION_WINLOSS_RATIO)
					+ "%", 155, 45);

			g2.drawString(String.valueOf("IN PROGRESS"), 215, 30);
			g2.drawString(String.valueOf(investments.size()), 245, 45);

			g2.drawString(String.valueOf("NAME"), 335, 30);
			g2.drawString(
					String.valueOf(MasterAlgorithm.VERSION_NAME[MasterAlgorithm.VERSION]),
					295, 45);

			update_MINIMIZED = false;
		}
		// slow down the application because it goes too fast when the graphics
		// are not being painted
		if (MINIMIZED)
		{
			try
			{
				Thread.sleep(10);

			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (dragIconVisible)
		{
			if (dragAnimation < 1)
			{
				dragAnimation += 0.1;
				g2.setColor(Color.ORANGE);
				g2.fillRect(10, 10, 400, (int) (48 * dragAnimation));
				g2.setColor(Color.ORANGE);
				g2.setStroke(new BasicStroke(5));
				g2.drawRect(10, 10, 400, 48);
				g2.setColor(Color.WHITE);
			} else
			{
				g2.setColor(Color.WHITE);
				g2.setFont(new Font("Calabri", 30, 30));
				g2.drawString("MOVE", 175, 44);
			}
		}

		// WAIT-- NOT READY
		if (READY)
		{
			MyInvestments();
		}
		// GRAPHS
		DrawGraph(g);
		paintConsole(g2);
		paintGUIButtons(g2);
		paintWaitBar(g2);
		MasterAlgorithm.paintCurrentAlgorithmStatistics(g2);
	}

	public void paintConsole(Graphics2D g2)
	{
		g2.setStroke(new BasicStroke(4));
		g2.setColor(Color.BLACK);
		g2.fillRect(10, 65, 378, 520);
		g2.setColor(Color.ORANGE);
		g2.drawRect(10, 65, 378, 520);
		// font color
		g2.setFont(new Font("TimesRoman", Font.BOLD, 14));

		if (!READY)
		{
			g2.setColor(Color.RED);
			g2.drawString("NOT READY!", 170, 89);
			notReadyAnimation += 0.29;
			if (notReadyAnimation > 1)
			{
				notReadyAnimation = 0;
			}
			g2.setColor(Color.BLACK);
			g2.fillRect(15, 72, 368, (int) (20 * notReadyAnimation));

		} else
		{
			g2.setColor(Color.GREEN);
			g2.fillRect(15, 77, 368, 15);
			g2.setColor(Color.WHITE);
			g2.drawString("READY", 170, 89);

		}
		g2.setColor(Color.WHITE);

		for (int i = 0; i < investments.size(); i++)
		{
			if (investments.get(i).CallorPut.toLowerCase().equals(
					new String("call").toLowerCase()))
			{
				g2.setColor(Color.WHITE);
				g2.drawString(
						String.valueOf(i + 1) + "))   "
								+ investments.get(i).CallorPut + " "
								+ String.valueOf(investments.get(i).price), 30,
						310 + (i * 15));
				if (investments.get(i).price <= CURRENT_PRICE)
				{
					g2.setColor(Color.RED);
					g2.drawString(String.valueOf(CURRENT_PRICE), 300,
							310 + (i * 15));
				} else
				{
					g2.setColor(Color.GREEN);
					g2.drawString(String.valueOf(CURRENT_PRICE), 300,
							310 + (i * 15));
				}
			}
			if (investments.get(i).CallorPut.toLowerCase().equals(
					new String("put").toLowerCase()))
			{
				g2.setColor(Color.WHITE);
				g2.drawString(
						String.valueOf(i + 1) + "))   "
								+ investments.get(i).CallorPut + " "
								+ String.valueOf(investments.get(i).price), 30,
						310 + (i * 15));
				if (investments.get(i).price >= CURRENT_PRICE)
				{
					g2.setColor(Color.GREEN);
					g2.drawString(String.valueOf(CURRENT_PRICE), 300,
							310 + (i * 15));
				} else
				{
					g2.setColor(Color.RED);
					g2.drawString(String.valueOf(CURRENT_PRICE), 300,
							310 + (i * 15));
				}
			}
			// time left
			g2.setColor(Color.WHITE);
			CURRENT_TIME = Calendar.getInstance();
			g2.drawString(
					String.valueOf(-1
							* (CURRENT_TIME.getTime().getTime() - investments
									.get(i).time.getTime()) / 1000)
							+ "  Str: "
							+ String.valueOf(investments.get(i).strength), 200,
					310 + (i * 15));
		}
		// current price

		double max = 0;
		double min = 2;
		int maxindex = 0;
		int minindex = 0;
		g2.drawString(String.valueOf("ALLIGATOR"), 30, 120);
		for (int i = 0; i < ALLIGATOR.length; i++)
		{

			if (min > ALLIGATOR[i])
			{
				min = ALLIGATOR[i];
			}

			if (max < ALLIGATOR[i])
			{
				max = ALLIGATOR[i];
			}
			g2.drawString(String.valueOf(ALLIGATOR[i]), 35, 140 + (i * 15));
		}
		// MAXIMUM ALLIGATOR
		// ===========================================================

		if (max == ALLIGATOR[0])
		{
			g2.setColor(Color.RED);
			g2.drawString("SHORT " + String.valueOf(ALLIGATOR[0] + "MAX"), 30,
					220);
		}

		if (max == ALLIGATOR[1])
		{
			g2.setColor(Color.GREEN);
			g2.drawString("MED " + String.valueOf(ALLIGATOR[1] + " MAX"), 30,
					220);
		}

		if (max == ALLIGATOR[2])
		{
			g2.setColor(Color.BLUE);
			g2.drawString("LONG " + String.valueOf(ALLIGATOR[2] + " MAX"), 30,
					220);
		}

		// MINIMUM ALLIGATOR
		// ===========================================================
		if (min == ALLIGATOR[0])
		{
			g2.setColor(Color.RED);
			g2.drawString("SHORT " + String.valueOf(ALLIGATOR[0] + " MIN"), 30,
					240);
		}

		if (min == ALLIGATOR[1])
		{
			g2.setColor(Color.GREEN);
			g2.drawString("MED " + String.valueOf(ALLIGATOR[1] + " MIN"), 30,
					240);
		}

		if (min == ALLIGATOR[2])
		{
			g2.setColor(Color.BLUE);
			g2.drawString("LONG " + String.valueOf(ALLIGATOR[2] + " MIN"), 60,
					240);
		}

		// WIN/LOSS RATIO
		// -------------------------------------------------------------------------------
		g2.setColor(Color.GREEN);
		g2.drawString("WIN " + String.valueOf(current_wins), 30, 520);
		g2.drawString("LOSS " + String.valueOf(current_loss), 30, 540);
		g2.drawString("RSI " + String.valueOf(RSI), 30, 560);
		g2.drawString("Strength " + String.valueOf(INDICATOR_STRENGTH), 30, 575);

		// starting bankroll
		g2.drawString(
				"Starting BankRoll " + String.valueOf(BankRollStartingValue),
				210, 575);

		// g2.drawString(String.valueOf(ALLIGATOR[i]), 30, 120+(i*15));
		// g2.drawString(String.valueOf(ALLIGATOR[i]), 30, 120+(i*15));
		// g2.drawString(String.valueOf(ALLIGATOR[i]), 30, 120+(i*15));

	}

	public static void paintLegend(Graphics2D g2)
	{
		g2.setStroke(new BasicStroke(4));
		g2.setColor(Color.BLACK);
		g2.fillRect(395, 535, 600, 50);
		g2.setColor(Color.ORANGE);
		g2.drawRect(395, 535, 600, 50);
		// font color
		g2.setColor(Color.ORANGE);
		g2.setFont(new Font("TimesRoman", Font.BOLD, 12));

		g2.fillRect(430, 555, 15, 15);
		g2.setColor(Color.WHITE);
		g2.drawString("Current Price", 447, 567);

		// Alligator idicators
		g2.setColor(Color.BLUE);
		g2.fillRect(540, 555, 5, 15);
		g2.setColor(Color.GREEN);
		g2.fillRect(545, 555, 5, 15);
		g2.setColor(Color.RED);
		g2.fillRect(550, 555, 5, 15);
		g2.setColor(Color.WHITE);
		g2.drawString("Alligator", 560, 567);

		// Purchase Time Frame
		g2.setColor(Color.YELLOW);
		g2.fillRect(620, 555, 5, 15);
		g2.setColor(Color.WHITE);
		g2.drawString("Last Purchase", 630, 567);

		g2.setColor(Color.ORANGE);
		g2.fillRect(710, 555, 5, 15);
		g2.setColor(Color.WHITE);
		g2.drawString("Option end", 720, 567);

		g2.setStroke(new BasicStroke(.3f));
		g2.setColor(Color.BLUE);
		g2.fillRect(790, 555, 5, 15);
		g2.setColor(Color.WHITE);
		g2.drawString("Trend Lines", 800, 567);

		g2.setStroke(new BasicStroke(.3f));
		g2.setColor(Color.GREEN);
		g2.fillRect(870, 555, 5, 15);
		g2.setColor(Color.WHITE);
		g2.drawString("RSI", 880, 567);

		g2.setStroke(new BasicStroke(.3f));
		g2.setColor(Color.CYAN);
		g2.fillRect(920, 555, 5, 15);
		g2.setColor(Color.WHITE);
		g2.drawString("MACD", 930, 567);
	}

	public void paintGUIButtons(Graphics2D g2)
	{
		// pause Start
		g2.setStroke(new BasicStroke(6));
		g2.setColor(Color.BLACK);
		g2.fillRect(426, 15, 150, 32);
		if (READY)
		{
			g2.setColor(Color.GREEN);
			g2.drawRect(426, 15, 150, 32);
			g2.drawString("RUNNING", 475, 35);
		}
		if (!READY)
		{
			g2.setColor(Color.ORANGE);
			g2.drawRect(426, 15, 150, 32);
			g2.drawString("PAUSED", 475, 35);
		}

		// EXIT
		g2.setStroke(new BasicStroke(6));
		g2.setColor(Color.BLACK);
		g2.fillRect(965, 15, 32, 32);

		g2.setColor(Color.RED);
		g2.drawRect(965, 15, 32, 32);
		g2.drawString("X", 977, 36);

		// MINIMIZE
		g2.setStroke(new BasicStroke(6));
		g2.setColor(Color.BLACK);
		g2.fillRect(921, 15, 32, 32);

		g2.setColor(Color.BLUE);
		g2.setFont(new Font("Courier", Font.BOLD, 16));
		g2.drawRect(921, 15, 32, 32);
		g2.drawString("_", 933, 23);
		g2.drawString("_", 933, 26);
		g2.drawString("_", 933, 29);
		g2.drawString("_", 933, 31);

		// AUDIO TOGGLE
		g2.setStroke(new BasicStroke(6));
		g2.setColor(Color.BLACK);
		g2.fillRect(876, 15, 32, 32);
		g2.setColor(Color.GREEN);
		g2.setFont(new Font("Courier", Font.BOLD, 16));
		g2.drawRect(876, 15, 32, 32);
		g2.drawRoundRect(876, 15, 32, 32, 120, 165);
		g2.drawString("|", 887, 35);

		// AUDIO TOGGLE
		g2.setStroke(new BasicStroke(6));
		g2.setColor(Color.BLACK);
		g2.fillRect(830, 15, 32, 32);
		g2.setColor(Color.MAGENTA);
		g2.setFont(new Font("Courier", Font.BOLD, 16));
		g2.drawRect(830, 15, 32, 32);
		g2.fillPolygon(new int[] { 830, 860, 830 }, new int[] { 15, 15, 41 }, 3);

		// FUNCTION SELECTOR
		g2.setStroke(new BasicStroke(6));
		g2.setColor(Color.BLACK);
		g2.fillRect(785, 15, 32, 32);
		g2.setColor(Color.YELLOW);
		g2.setFont(new Font("Serif", Font.BOLD, 16));
		g2.drawRect(785, 15, 32, 32);
		g2.drawString("f(" + String.valueOf(MasterAlgorithm.VERSION) + ")",
				791, 36);

	}

	public void paintWaitBar(Graphics2D g2)
	{
		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, 1000, 5);

		// progress bar
		if (Database.CURRENTITTERATIONS < 1000)
		{
			if ((int) Database.CURRENTITTERATIONS * .1 == 1
					|| (int) Database.CURRENTITTERATIONS * .1 % 5 == 0
					|| (int) Database.CURRENTITTERATIONS * .1 == 98)
			{
				// System.out.println((int)Database.CURRENTITTERATIONS*.1);
				try
				{
					int percent = (int) (Database.CURRENTITTERATIONS * 0.1);
					String temp = String.valueOf(percent);
					Audio.ttsProgressBar(temp);

				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			g2.setColor(Color.CYAN);

		} else
		{
			g2.setColor(Color.GREEN);

			if (!readyOnce)
			{
				READY = true;
				readyOnce = true;

			}
		}
		g2.fillRect(0, 0,
				(int) (1000 * ((Database.CURRENTITTERATIONS * 0.001))), 5);
		// System.out.println(Database.CURRENTITTERATIONS);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 0, 1000, 5);
	}

	public void paintJFrameBackGround(Graphics2D g)
	{
		if (!setBackground)
		{
			g.setColor(new Color(0.1f, 0.1f, 0.1f));
			g.fillRect(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width,
					Toolkit.getDefaultToolkit().getScreenSize().height);
			robot = new ExternalController();
			investments = new ArrayList<Tick>();// setup investements array
			setBackground = true;
			MasterAlgorithm.updateVersionStats(MasterAlgorithm.VERSION);
			paintLegend(g);
			BankRollStartingValue = BANKROLL;
		}
	}

	// Draw indicators here!
	public void DrawGraph(Graphics g)
	{
		test = Database.getDBCurentPrice(2);
		bounds = new Rectangle(0, 600, 1000, 300);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// graph body
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 590, 1000, 320);
		// graph border
		min = max = 0;
		for (int i = 0; i < test.size(); i++)
		{
			if (i == 0)
			{
				min = test.get(i);
				max = test.get(i);
			}

			if (max < test.get(i))
			{
				max = test.get(i);
			}

			if (min > test.get(i))
			{
				min = test.get(i);
			}
		}
		range = max - min;
		// get the X and Y coords of the price ticker
		int[] x = new int[test.size()];
		int[] y = new int[test.size()];
		for (int i = 0; i < test.size(); i++)
		{
			x[i] = i;
			y[i] = (int) ((-(test.get(i) - min) / range) * (300) + (bounds.height * 2 + bounds.height));
		}

		// GLOBAL VARIABLE
		CURRENT_PRICE = test.get(test.size() - 1);

		// reverse the x-coords
		x = reverseArray(x);
		// DEBUG
		// System.out.println("MIN:"+String.valueOf(min));
		// System.out.println("RANGE:"+String.valueOf(range));
		// System.out.println("MAX"+String.valueOf(max));
		// System.out.println(test.size());

		// DRAW LINES Functions
		// --------------------------------------------------------------------------------------------------------------------------------------
		// ------------------------------------------------------------------------------------------------------------------------------------------------------------

		// PURCHASE
		// TIME-FRAME-------------------------------------------------------------------
		purchaseTimeFrame(g2);

		// CURRENT
		// PRICE---------------------------------------------------------------------------------------------------------------------------------------------------
		g2.setStroke(new BasicStroke(4));
		g2.setColor(Color.ORANGE);
		g2.drawPolyline(x, y, test.size());

		// THE PEOPLE
		// ---------------------------------------------------------------------------------------------------------------------------------------------------
		// indicators.THE_PEOPLE.PEOPLE();

		// BOLLINGER
		// BANDS(f(x))------------------------------------------------------------------------------------------------------------------------------
		indicators.BOLLINGER_BANDS.run(test, 60);
		// Draw.DrawThis(indicators.BOLLINGER_BANDS.BB_Lower, g2, Color.WHITE,
		// 0);
		// Draw.DrawThis(indicators.BOLLINGER_BANDS.BB_Middle, g2, Color.PINK,
		// 0);
		// Draw.DrawThis(indicators.BOLLINGER_BANDS.BB_Upper, g2, Color.RED, 0);

		// RSI(f(x))------------------------------------------------------------------------------------------------------------------------------
		// Draw.DrawThis(indicators.RSI.run(test, RSI_PERIOD), g2, new
		// Color(0f,1f, .0f, .4f), 1);

		// MACD(f(x))-------------------------------------------------------------------------------------------------------------------------------------------------------
		// Draw.DrawThis(indicators.MACD.run(test), g2, Color.cyan, 0);
		// indicators.MACD.run(test);
		// Draw.DrawThis(indicators.MACD.MACD_LINE, g2, Color.PINK, 0);
		// Draw.DrawThis(indicators.MACD.Signal_line, g2, Color.RED, 0);

		// STOCHASTIC(f(x))-------------------------------------------------------------------------------------------------------------------------------------------------------
		// indicators.STOCHASTIC.run(test, 14);
		// Draw.DrawThis(indicators.STOCHASTIC.run(test, 14), g2, Color.pink,
		// 0);
		// Draw.DrawThis(indicators.STOCHASTIC.D, g2, Color.ORANGE, 0);

		// EMA(f(x))----------------------------------------------------------------------------------------------------------------------------------------
		// Draw.DrawThis(indicators.EMA.run(test, 14), g2, Color.CYAN, 0);

		// SMA-----------------------------------------------------------------------------------------------------------------------------------------------------------
		// Draw.DrawThis(SMA.run(test, 75, 0), g2, Color.red, 0);
		// Draw.DrawThis(SMA.run(test, 113, 1), g2, Color.green, 0);
		// Draw.DrawThis(SMA.run(test, 150, 2), g2, Color.blue, 0);
		 Draw.DrawThis(SMA.run(test, 20, 2), g2, Color.blue, 0);

		// TRSC-----------------------------------------------------------------------------------------------------------------------------------------------------------
			//Draw.DrawThis(TRSC.run(test), g2, Color.RED, 1);
			Draw.DrawThis(TRSC.predict(test, TIMETOADD), g2, Color.RED, 1);
			Draw.DrawThis(TRSC.practiceSin(1), g2, Color.BLUE, 0);
		//	indicators.Draw.DrawPrediction(EXPERIMENTAL_INDICATOR, g2, Color.green);

			
		// TREND
		// LINES----------------------------------------------------------------------------------------------------------------------------------------------------------
		// full trend
		// g2.setStroke(new BasicStroke(.6f));
		// g2.setColor(Color.ORANGE);
		// g2.drawLine(0, (y)[y.length - 1], 1000, y[0]);
		//
		// // half trend
		// g2.drawLine(500, (y)[(int) y.length / 2], 1000, (y)[0]);
		//
		// // quater trend
		// g2.drawLine(750, (y)[(int) y.length / 4], 1000, (y)[0]);
		//
		// // tenth trend
		// g2.drawLine(900, (y)[(int) y.length / 10], 1000, (y)[0]);
		//
		// // twentieth trend
		// g2.drawLine(950, (y)[(int) y.length / 20], 1000, (y)[0]);

		TREND_LINES[0] = ((y)[(int) y.length / 20] - (y)[0]) / (1000 - 950);

		// Draw String informations
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Courier", Font.BOLD, 12));
		g2.drawString(String.valueOf(max), 20, 620);
		g2.drawString(String.valueOf(min), 20, 880);
		g2.drawString(
				"Current Price " + String.valueOf(test.get(test.size() - 1)),
				400, 620);

		// GRAPH BORDER
		g2.setColor(Color.ORANGE);
		g2.setStroke(new BasicStroke(.9f));
		g2.drawLine(0, 592, 1000, 592);
		g2.setStroke(new BasicStroke(15));
		g2.drawLine(0, 915, 1000, 915);
	}

	public void purchaseTimeFrame(Graphics2D g2)
	{
		timetobuy = Database.getDBTimeToBuy(2);

		finalBuy = new ArrayList<Integer>();
		endofPeriod = new ArrayList<Integer>();
		Collections.reverse(timetobuy);

		for (int i = 0; i < timetobuy.size(); i++)
		{
			int temp = 0;
			int endtemp = 0;
			try
			{
				temp = Integer.parseInt(timetobuy.get(i).split(":")[1].trim());
				endtemp = Integer.parseInt(timetobuy.get(i).split(":")[1]
						.trim());
			} catch (Exception e)
			{

			}

			if (temp == 0)
			{
				finalBuy.add(i);
			}
			if (endtemp == 30)
			{
				endofPeriod.add(i);
			}
		}
		try
		{
			TIMETOADD = Integer.parseInt(timetobuy.get(timetobuy.size() - 1)
					.split(":")[1].trim()) + 33;
		} catch (Exception e)
		{

		}
		g2.setStroke(new BasicStroke(.2f));
		g2.setColor(Color.YELLOW);
		for (int i = 0; i < finalBuy.size(); i++)
		{
			g2.drawLine(finalBuy.get(i), 590, finalBuy.get(i), 915);
		}

		g2.setStroke(new BasicStroke(.3f));
		g2.setColor(Color.ORANGE);
		for (int i = 0; i < endofPeriod.size(); i++)
		{
			g2.drawLine(endofPeriod.get(i), 590, endofPeriod.get(i), 915);
		}
	}

	// helper methods
	public static int[] reverseArray(int[] x)
	{

		for (int i = 0; i < x.length / 2; i++)
		{
			int temp = x[i];
			x[i] = x[x.length - i - 1];
			x[x.length - i - 1] = temp;
		}
		return x;
	}

	public static double[] reverseArray(double[] x)
	{

		for (int i = 0; i < x.length / 2; i++)
		{
			double temp = x[i];
			x[i] = x[x.length - i - 1];
			x[x.length - i - 1] = temp;
		}
		return x;
	}

	public static ArrayList<Double> reverseArray(ArrayList<Double> d)
	{
		Collections.reverse(d);
		return d;
	}

	public static double getMax(ArrayList<Double> alist)
	{
		double temp = 0;
		for (int i = 0; i < alist.size(); i++)
		{
			if (i == 0)
			{
				temp = alist.get(i);
			}
			if (temp < alist.get(i))
			{
				temp = alist.get(i);
			}
		}
		return temp;
	}

	public static double getMin(ArrayList<Double> alist)
	{
		double temp = 0;
		for (int i = 0; i < alist.size(); i++)
		{
			if (i == 0)
			{
				temp = alist.get(i);
			}
			if (temp > alist.get(i))
			{
				temp = alist.get(i);
			}
		}
		return temp;
	}

	public void sleep()
	{
		try
		{
			Thread.sleep(1200);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub

	}

	public void checkMobileUpdate()
	{
		ArrayList<Integer> dbCommands = Database.getMobileCommads();
		for (int i = 0; i < dbCommands.size(); i++)
		{
			if (dbCommands.get(i) == 1)
			{
				Audio.fx(1);
				if (READY)
				{
					READY = false;
				} else
				{
					READY = true;
				}
			}
			if (dbCommands.get(i) == 2)
			{
				Audio.fx(1);

				if (MasterAlgorithm.VERSION <= MasterAlgorithm.VERSION_COUNT)
					MasterAlgorithm.VERSION++;

				if (MasterAlgorithm.VERSION > MasterAlgorithm.VERSION_COUNT)
				{
					MasterAlgorithm.VERSION = 0;
				}
				Database.UpdateMobileValues();
				Database.queryCurrentVersion(MasterAlgorithm.VERSION);
				OptionTrader.update_MINIMIZED = true;
			}
			if (dbCommands.get(i) == 3)
			{
				Audio.fx(1);

				robot.checkURL();
				System.out.println("PRINTLN");
			}
			if (dbCommands.get(i) == 4)
			{
				Audio.fx(1);
				Database.removeMobileCommandsOffline();
				System.exit(0);
			}
		}
		Database.removeMobileCommands();
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		// fps.run();
	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub

	}

}
