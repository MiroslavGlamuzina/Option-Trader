package Nexus;

public class PaintGraphThread extends OptionTrader implements Runnable 
{
	@Override
	public void run()
	{

		while (true)
		{
			try
			{
				//OptionTrader.DrawGraph(OptionTrader.g2);
				Thread.sleep(500);

			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("hit");
		}
	}

}
