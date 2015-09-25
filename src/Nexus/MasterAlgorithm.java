package Nexus;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Random;

import Database.Database;

// I will use this class for version control aswell as holding the algorithm for the application
public class MasterAlgorithm extends OptionTrader
{
	public static int VERSION_COUNT = 6;

	public static int VERSION = 7;
	public static String[] VERSION_NAME = new String[] {
			"Combitorial attempt 1", "Alligator", "RSI",
			"Alligator Cross-over", "Perceptron", "MACD", "Bollinger Bands", "EXPERIMENTAL_INDICATOR" };
	public static int MAX_CONCURRENT_INVESTMENTS = 1;
	// perceptron
	public static boolean PERCEPTRON_LEARN = true;
	public static boolean PERCEPTRON_LEARN_BEGIN = true;
	public static double[] weights;

	// GUI ALGORITHM VALUES
	public static int VERSION_WIN_COUNT = 0;
	public static int VERSION_LOSS_COUNT = 0;
	public static int VERSION_WINLOSS_RATIO = 0;

	/**
	 * Master Algorithm init()
	 * 
	 * @VERSION_0 First Combines test 50% -- unsuccesfull
	 * @VERSION_1 Alligator 60% -- Averaged out to 50% <br/>
	 *            Cannot be used alone to be successful
	 * @VERSION_2 RSI <br/>
	 *            Shows potential, but should include multiple RSI to help with
	 *            spikes that dont follow the trend. it will call or put even if
	 *            it will obviously go downwards
	 * @STRENGTHS Not available yet
	 * @PROBLEMS Reversal patterns are not recognized enough
	 * */
	public static int init()
	{
		if (VERSION == 0)
		{
			return CombitorialAttempt_1();
		}

		// ALLIGATOR ONLY
		if (VERSION == 1)
		{
			return Alligator();
		}

		// RSI ONLY
		if (VERSION == 2)
		{
			return RSI();
		}

		// ALLIGATOR ONLY ON CROSSOVER
		if (VERSION == 3)
		{
			return Alligator_CrossOver();
		}

		// PERCEPTRON
		if (VERSION == 4)
		{
			return Perceptron_1();
		}
		// MACD
		if (VERSION == 5)
		{
			return MACD();
		}
		// BOLLINGER BANDS
		if (VERSION == 6)
		{
			return BollingerBands();
		}
		// EXPERIMENTAL INDICATOR
		if (VERSION == 7)
		{
			return EXPERIMENTAL_INDICATOR();
		}
		return 0;
	}

	private static int Perceptron_1()
	{
		// random for weights
		// master weight
		double sum = 0;
		// current input for algorithm
		double[] input = new double[] {
				Math.abs(ALLIGATOR[2] - ALLIGATOR[0]) * 100, RSI };

		if (PERCEPTRON_LEARN == true)
		{
			Random r = new Random();
			if (PERCEPTRON_LEARN_BEGIN)
			{

				weights = new double[] { (r.nextInt(1 + 1 + 1) - 1),
						(r.nextInt(1 + 1 + 1) - 1) };
				PERCEPTRON_LEARN_BEGIN = false;
			} else
			{
				weights = new double[] {
						weights[0] * r.nextInt(1 + 1 + 1) - 1 * input[0],
						weights[1] * r.nextInt(1 + 1 + 1) - 1 * input[1] };
				PERCEPTRON_LEARN = false;
			}
		}

		// DEBUG

		// System.out.println("WIEGHT1: "+weights[0]+"WIEGHT2: "+weights[1]);

		// loop through the input
		for (int i = 0; i < input.length; i++)
		{
			sum += input[i] * weights[i];
		}

		// determine call or put
		if (sum > 0)
		{
			sum = 0;
			return 1;
		} else
		{
			sum = 0;
			return -1;
		}
	}

	private static int Alligator_CrossOver()
	{
		int w_alligator = 0;

		// general Trend Line
		if (ALLIGATOR[0] > ALLIGATOR[2] && ALLIGATOR[0] > ALLIGATOR[3])
		{
			// System.out.println("!ALLIGATOR:BUY!");
			w_alligator += 1;
		}

		if (ALLIGATOR[2] > ALLIGATOR[0] && ALLIGATOR[2] < ALLIGATOR[3])
		{
			// System.out.println("!ALLIGATOR:PUT!");
			w_alligator -= 1;
		}
		System.out
				.println(Math.abs((ALLIGATOR[0] - ALLIGATOR[2]) / range) * 10000);
		return w_alligator;
	}

	private static int RSI()
	{
		int w_RSI = 0;

		if (RSI > 70)
		{
			w_RSI = -1;
		}
		if (RSI < 30)
		{
			w_RSI = 1;
		}
		// System.out.println(String.valueOf(RSI));
		OptionTrader.CONSOLE = "RSI: " + String.valueOf((int) RSI);
		return w_RSI;
	}

	private static int Alligator()
	{
		int w_alligator = 0;

		// general Trend Line
		if (ALLIGATOR[0] > ALLIGATOR[2])
		{
			// System.out.println("!ALLIGATOR:BUY!");
			w_alligator += 1;
		}

		if (ALLIGATOR[2] > ALLIGATOR[0])
		{
			// System.out.println("!ALLIGATOR:PUT!");
			w_alligator -= 1;
		}
		System.out
				.println(Math.abs((ALLIGATOR[0] - ALLIGATOR[2]) / range) * 10000);
		return w_alligator;
	}

	private static int CombitorialAttempt_1()
	{
		double CALLPUT = 0;
		double SENSITIVITY = 1;
		double w_alligator = 0;
		double w_rsi = 0;
		double w_slope = 0;

		// general Trend Line
		if (ALLIGATOR[0] > ALLIGATOR[2])
		{
			System.out.println("!ALLIGATOR:BUY!");
			w_alligator += 1;
		}

		if (ALLIGATOR[2] > ALLIGATOR[0])
		{
			System.out.println("!ALLIGATOR:PUT!");
			w_alligator -= 1;
		}

		double distance = Math.abs(ALLIGATOR[0] - ALLIGATOR[2]);
		if (distance == 0)
		{
			if (TREND_LINES[0] > 0)
			{
				w_slope += 1;
			}
			if (TREND_LINES[0] < 0)
			{
				w_slope -= 1;
			}
		}

		if (RSI > 52)
		{
			w_rsi += .15;
			if (RSI > 55)
			{
				w_rsi += .25;
			}
			if (RSI > 60)
			{
				w_rsi += .5;
			}
			if (RSI > 70)
			{
				w_rsi += 1;
			}
			if (RSI > 75)
			{
				w_rsi += 2;
			}
			if (RSI > 80)
			{
				w_rsi += 4;
			}
		}
		if (RSI < 41)
		{
			w_rsi -= .15;
			if (RSI < 50)
			{
				w_rsi -= .25;
			}
			if (RSI < 50)
			{
				w_rsi -= .5;
			}
			if (RSI < 30)
			{
				w_rsi -= 1;
			}
			if (RSI < 25)
			{
				w_rsi -= 2;
			}
			if (RSI < 20)
			{
				w_rsi -= 4;
			}
		}
		CALLPUT = SENSITIVITY * w_alligator - w_rsi + w_slope;
		System.out.println(CALLPUT);

		// if (CALLPUT > 0)
		// {
		// CALLPUT = 1;
		//
		// } else if (CALLPUT < 0)
		// {
		// CALLPUT = -1;
		//
		// } else

		if (CALLPUT == 0)
		{
			CALLPUT = 0;
		}
		INDICATOR_STRENGTH = CALLPUT;
		return (int) CALLPUT;
	}

	public static void paintCurrentAlgorithmStatistics(Graphics2D g2)
	{
		g2.setStroke(new BasicStroke(4));
		g2.setColor(Color.BLACK);
		g2.fillRect(395, 475, 600, 50);
		g2.setColor(Color.ORANGE);
		g2.drawRect(395, 475, 600, 50);
		// font color
		g2.setColor(Color.WHITE);
		// draw statistics
		g2.setFont(new Font("TimesRoman", Font.BOLD, 14));
		// BUILD NUMBER
		g2.drawString("  BUILD", 403, 497);
		g2.drawString(String.valueOf(MasterAlgorithm.VERSION), 428, 514);
		// WIN COUNT
		g2.drawString("  WINS", 503, 497);
		g2.drawString(String.valueOf(MasterAlgorithm.VERSION_WIN_COUNT), 528,
				514);
		// LOSS COUNT
		g2.drawString(" LOSSES", 603, 497);
		g2.drawString(String.valueOf(MasterAlgorithm.VERSION_LOSS_COUNT), 628,
				514);
		// WINLOSS RATION
		g2.drawString(" SUCCESS", 703, 497);
		g2.drawString(String.valueOf(VERSION_WINLOSS_RATIO) + "%", 728, 514);

		g2.drawString("VERSION NAME", 818, 497);
		g2.drawString(String.valueOf(VERSION_NAME[VERSION]), 818, 514);

	}

	public static void updateVersionStats(int version)
	{
		Database.queryCurrentVersion(version);
	}

	public static int MACD()
	{
		if (MACD_HISTOGRAM > 0)
		{
			return -1;
		} else
		{
			return 1;
		}
	}

	public static int BollingerBands()
	{
		// System.out.println("0: "+BOLLINGER_BANDS[0]);
		// System.out.println("1: "+BOLLINGER_BANDS[1]);
		// System.out.println("2: "+BOLLINGER_BANDS[2]);
		// System.out.println(Math.abs(BOLLINGER_BANDS[0]
		// -test.get(test.size()-1)) +" vs "+ Math.abs(BOLLINGER_BANDS[1]
		// -test.get(test.size()-1)));
		// System.out.println("curPrice: "+ test.get(test.size()-1));
		if (BOLLINGER_BANDS[0] < test.get(test.size() - 1)
				&& Math.abs(BOLLINGER_BANDS[0] - test.get(test.size() - 1)) < Math
						.abs(BOLLINGER_BANDS[1] - test.get(test.size() - 1))
				&& BOLLINGER_BANDS[0] > BOLLINGER_BANDS[2])
		{
			return -1;
		}
		if (BOLLINGER_BANDS[2] > test.get(test.size() - 1)
				&& Math.abs(BOLLINGER_BANDS[2] - test.get(test.size() - 1)) < Math
						.abs(BOLLINGER_BANDS[1] - test.get(test.size() - 1))
				&& BOLLINGER_BANDS[0] > BOLLINGER_BANDS[2])

		{
			return 1;
		}
		return 0;
	}

	public static int EXPERIMENTAL_INDICATOR()
	{
		if (EXPERIMENTAL_INDICATOR > test.get(test.size() - 1))
		{
			return 1;
		} else
		{
			return -1;
		}
	}

}
