package tools;

import java.util.Calendar;

public class VerifyTickValue {

	public static String verifyPrice(String s) {
		s = s.replace(' ', '.');
		s.replaceAll("\\s+", " ");
		return s;
	}

	public static Calendar verifyTime() {

		return null;
	}

	public static int verifyTimeToBuy(String s) {
		int time = Integer.parseInt(VerifyTickValue.removeWhiteSpace((s
				.substring(s.length() - 4))));
		return time;
	}

	public static String removeWhiteSpace(String s) {
		s = s.replaceAll("\\s+", " ");
		return s;
	}

	
}
