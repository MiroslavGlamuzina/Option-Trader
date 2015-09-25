package Nexus;

import java.util.Calendar;

import externalController.ExternalController;

public class ClosePopupCheck implements Runnable
{
	static ExternalController robot;
	Calendar cal, newcal;
	public static boolean running = false;

	@Override
	public void run()
	{
		// // TODO Auto-generated method stub
		// robot = new ExternalController();
		// cal = Calendar.getInstance();
		// cal.add(Calendar.MILLISECOND, 1);
		// while (true )
		// {
		// newcal = Calendar.getInstance();
		//
		// if (newcal.getTime().getTime() - cal.getTime().getTime() > 1)
		// {
		// robot.ClosePopUp();
		// cal = Calendar.getInstance();
		// cal.add(Calendar.SECOND, 10);
		// System.out.println("Close Popup Thread HIT");
		// }
		// System.out.println("Close Popup Thread HIT");
		//
		// }
		running = true;
		try
		{
			robot = new ExternalController();
			robot.ClosePopUp();
			Thread.sleep(10000);
			running = false;
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

}
