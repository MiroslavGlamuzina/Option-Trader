import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.ObjectInputStream.GetField;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Database.Database;
import Nexus.MasterAlgorithm;
import Nexus.OptionTrader;
import externalController.Audio;
import externalController.AudioThread;

@SuppressWarnings("serial")
public class GUI extends JFrame
{
	public static JPanel contentPane;
	static public OptionTrader op;
	static GUI frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		Audio.ttsGreeting();
		op = new OptionTrader();

		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public GUI()
	{
		setAlwaysOnTop(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setOpaque(true);
		//Image Icon 
		ImageIcon img = new ImageIcon("C:\\chart.png");
		setIconImage(img.getImage());
		
		// super.getContentPane().setBackground(new Color(.20f, .20f, .20f));
		super.setBackground(new Color(.20f, .20f, .20f));
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(-1000, 00, 1000, 915); // app-size
		setTitle("Option Trader");
		setResizable(false);
		setUndecorated(true); // later for look and feel
		setOpacity(.9f);

		// setLocation(new Point(-1000, 00));
		// mouse!
		MouseActions();
		new MyThread().start();
	}

	public void paint(Graphics g)
	{
		op.paintGUI(g, this);
		if (mouseDown)
		{
			OptionTrader.paintLegend((Graphics2D) g);
		}
	}

	class MyThread extends Thread
	{
		@Override
		public void run()
		{
			for (;;)
			{
				dragWindow();
				repaint();
				// RUN METHODS
				op.run();
			}
		}

		private void dragWindow()
		{
			if (mouseDown)
			{
				setLocation(MouseInfo.getPointerInfo().getLocation().x - initX,
						MouseInfo.getPointerInfo().getLocation().y - initY);
			}
		}
	}

	boolean mouseDown = false;
	public int initX = 0;
	public int initY = 0;

	private void MouseActions()
	{
		super.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mouseClicked(java.awt.event.MouseEvent evt)
			{
				int x = evt.getX();
				int y = evt.getY();
				// EXIT BUTTON
				// do last minute operations here
				if (x > 965 && x < 997 && y > 15 && y < 47)
				{
					// play sound effect
					Audio.fx(1);
					Audio.ttsFareWell();
					float opac = getOpacity();
					try
					{
						while (opac > .01f)
						{
							setOpacity(opac - .03f);
							opac = getOpacity();
							Thread.sleep(20);
						}

					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Database.removeMobileCommandsOffline();
					dispose();
					System.exit(0);
				}
				// MINIMIZE BUTTOn
				// 921, 15, 32, 32
				if (x > 921 && x < 953 && y > 15 && y < 47)
				{
					setExtendedState(JFrame.ICONIFIED);

					Audio.tts("Window brought to tray");
					// play sound effect
					Audio.fx(1);
				}

				// AUDIO TOGGLE 876, 15, 32, 32
				if (x > 876 && x < 908 && y > 15 && y < 47)
				{
					if (Audio.mute)
					{
						Audio.tts("un-Mute");
						Audio.mute = false;
					} else if (!Audio.mute)
					{
						Audio.tts("Mute");
						Audio.mute = true;
					}
					// play sound effect
					Audio.fx(1);
				}

				// EXPAND AND MINIMIZE 830, 15, 32, 32
				if (x > 830 && x < 864 && y > 15 && y < 47)
				{
					if (getBounds().height > 100)
					{
						OptionTrader.MINIMIZED = true;
						OptionTrader.update_MINIMIZED = true;

						setBounds(getBounds().x, getBounds().y, 1000, 62);
					} else
					{
						OptionTrader.MINIMIZED = false;
						setBounds(getBounds().x, getBounds().y, 1000, 915);

					}
					// play sound effect
					Audio.fx(1);
				}
				// FUNCTION SELECTOR -- 785, 15, 32, 32
				if (x > 785 && x < 817 && y > 15 && y < 47)
				{
					if (MasterAlgorithm.VERSION <= MasterAlgorithm.VERSION_COUNT)
						MasterAlgorithm.VERSION++;

					if (MasterAlgorithm.VERSION > MasterAlgorithm.VERSION_COUNT)
					{
						MasterAlgorithm.VERSION = 0;
					}
					Database.queryCurrentVersion(MasterAlgorithm.VERSION);
					OptionTrader.update_MINIMIZED = true;
					// play sound effect
					Audio.fx(1);
				}

				// PAUSE START BUTTON
				if (x > 423 && x < 573 && y > 15 && y < 47)
				{
					if (OptionTrader.READY)
					{
						OptionTrader.READY = false;
						Audio.ttsPause();
					} else
					{
						OptionTrader.READY = true;
						Audio.ttsResume();

					}
					System.out.println("READY: "
							+ String.valueOf(OptionTrader.READY));
					// play sound effect
					Audio.fx(1);
				}

			}

			public void mouseEntered(java.awt.event.MouseEvent evt)
			{

			}

			public void mouseExited(java.awt.event.MouseEvent evt)
			{

			}

			public void mousePressed(java.awt.event.MouseEvent evt)
			{
				// System.out.println("mousePressed");
				float x = evt.getX();
				float y = evt.getY();

				// DRAG
				if (x > 0 && x < 415 && y > 0 && y < 48)
				{
					mouseDown = true;
					initX = (int) x;
					initY = (int) y;
					OptionTrader.dragIconVisible = true;
					System.out.println("mousedown drag");
					// play sound effect
					Audio.fx(1);
				}
			}

			public void mouseReleased(java.awt.event.MouseEvent evt)
			{
				float x = evt.getX();
				float y = evt.getY();
				// DRAG
				if (x > 0 && x < 415 && y > 0 && y < 48)
				{
					mouseDown = false;
					OptionTrader.dragIconVisible = false;
					System.out.println("mousedown exit");
					OptionTrader.update_MINIMIZED = true;
					// play sound effect
					Audio.fx(1);
				}
			}
		});
	}

}