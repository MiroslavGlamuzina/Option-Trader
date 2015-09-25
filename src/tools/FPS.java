package tools;

public class FPS {
	long nextSecond = System.currentTimeMillis() + 1000;
	int frameInLastSecond = 0;
	int framesInCurrentSecond = 0;
	
	public  FPS (){
	
	}
	
	public void run(){
		 long currentTime = System.currentTimeMillis();
		    if (currentTime > nextSecond) {
		        nextSecond += 1000;
		        frameInLastSecond = framesInCurrentSecond;
		        framesInCurrentSecond = 0;
		    }
		    framesInCurrentSecond++;
		    System.out.println(framesInCurrentSecond+" FPS");
	}
}
