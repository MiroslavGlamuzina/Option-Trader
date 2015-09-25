package externalController;

import java.util.Random;

public class Audio
{
	public static AudioThread audio;
	public static boolean mute = true;

	public static void tts(String text)
	{
		audio = new AudioThread(text);
		Thread audioThread = new Thread(audio);
		audioThread.start();

	}

	public static void ttsGreeting()
	{
		if (mute)
		{
			return;
		}
		if (!audio.running)
		{
			String[] greeting = new String[] {
					"Hold on, this may take a while",
					"Setting up configurations now", "Up and running",
					"Welcome Back Sir", "Welcome Back", "Up and running",
					"Loading History" };
			Random r = new Random();
			int i = r.nextInt(greeting.length);
			audio = new AudioThread(greeting[i]);
			Thread audioThread = new Thread(audio);
			audioThread.start();
		}
	}

	public static void ttsFareWell()
	{
		if (mute)
		{
			return;
		}

		if (!audio.running)
		{
			String[] fareWell = new String[] { "Goodbye",
					"Saving Progress now", "Exiting Application" };
			Random r = new Random();
			int i = r.nextInt(fareWell.length);
			audio = new AudioThread(fareWell[i]);
			Thread audioThread = new Thread(audio);
			audioThread.start();
		}
	}

	public static void ttsProgressBar(String text)
	{
		if (mute)
		{
			return;
		}

		if (!audio.running)
		{
			String[] notifyProgress = new String[] {
					"Progress is at " + text + " percent",
					"Currently at " + text + " percent",
					"Progress is currently at " + text + " percent",
					text + " percent" };
			Random r = new Random();
			int i = r.nextInt(notifyProgress.length);
			audio = new AudioThread(notifyProgress[i]);
			Thread audioThread = new Thread(audio);
			audioThread.start();
		}
	}

	public static void ttsProgressBarComplete(String text)
	{
		if (mute)
		{
			return;
		}

		if (!audio.running)
		{
			String[] notifyProgress = new String[] { "Progress is Complete",
					"Application 100 percent up and running",
					"Database Requirements have been fullfilled" };
			Random r = new Random();
			int i = r.nextInt(notifyProgress.length);
			audio = new AudioThread(notifyProgress[i]);
			Thread audioThread = new Thread(audio);
			audioThread.start();
		}
	}

	public static void ttsPause()
	{
		if (mute)
		{
			return;
		}

		if (!audio.running)
		{
			String[] notifyProgress = new String[] { "Pausing calculations",
					"calculations are paused", "Algorithm on hold",
					"Standing By" };
			Random r = new Random();
			int i = r.nextInt(notifyProgress.length);
			audio = new AudioThread(notifyProgress[i]);
			Thread audioThread = new Thread(audio);
			audioThread.start();
		}
	}

	public static void ttsResume()
	{
		if (mute)
		{
			return;
		}

		if (!audio.running)
		{
			String[] notifyProgress = new String[] { "Resuming application",
					"Application Resumed", "Calculations are in effect",
					"Algorithms are now running" };
			Random r = new Random();
			int i = r.nextInt(notifyProgress.length);
			audio = new AudioThread(notifyProgress[i]);
			Thread audioThread = new Thread(audio);
			audioThread.start();
		}
	}

	public static void fx(int fx)
	{
		if (!audio.running)
		{
			audio = new AudioThread(fx);
			Thread audioThread = new Thread(audio);
			audioThread.start();
		}
	}

}
