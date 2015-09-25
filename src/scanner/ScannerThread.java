package scanner;

public class ScannerThread implements Runnable{
	static Scanner scanner;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		scanner = new Scanner();
		while(true){
		scanner.run();
		}
	}

}
