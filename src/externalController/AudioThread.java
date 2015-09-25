package externalController;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import us.monoid.web.BinaryResource;
import us.monoid.web.Resty;

public class AudioThread implements Runnable
{
	public static boolean running = false;
	String text = "";
	int fx = 0;
	public static final String BASE_URL = "http://translate.google.com/translate_tts?ie=UTF-8&q={0}&tl={1}&prev=input";

	public AudioThread(String text)
	{
		this.text = text;
	}

	public AudioThread(int fx)
	{
		this.fx = fx;
	}

	@Override
	public void run()
	{
		{
			running = true;

			try
			{
				if (fx == 0)
				{
					File f = new File("translate.mp3");
					String sentence = URLEncoder.encode(text, "UTF-8");
					String urlString = MessageFormat.format(BASE_URL, sentence,
							"en_uk");
					BinaryResource res = new Resty().bytes(new URI(urlString));
					res.save(f);

					FileInputStream in = new FileInputStream(f);

					Player p = new Player(in);
					p.play();
					p.close();
					f.delete();
					running = false;
				}
				if (fx != 0)
				{
					String[] interface_sounds = new String[] {
							"resources/interface1.wav",
							"resources/interface2.wav",
							"resources/interface3.wav",
							"resources/interface4.wav" };
					Random r = new Random();
					int index = r.nextInt(interface_sounds.length);
					switch (fx)
						{
						case 1:
							AudioInputStream audioIn;
							Clip clip = null;
							try
							{
								// audioIn =
								// AudioSystem.getAudioInputStream(getClass().getResource(new
								// File(interface_sounds[index])));
								// audioIn = AudioSystem
								// .getAudioInputStream(getClass().getResource(new
								// File(
								// interface_sounds[index]).toString()));
								audioIn = AudioSystem
										.getAudioInputStream(new File(
												interface_sounds[index]));
								clip = AudioSystem.getClip();
								clip.open(audioIn);

							} catch (IOException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (UnsupportedAudioFileException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (LineUnavailableException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							clip.start();
							running = false;

							break;

						default:
							break;
						}
				}

			} catch (UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JavaLayerException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
