package Nexus;

import java.util.ArrayList;

public class OptionSeries {
	public static ArrayList<Tick> ticks;
	public static String name; // The Name of the series
	public static int maximumTickCount = Integer.MAX_VALUE;
	public static int removedTicksCount = 0;

	public OptionSeries() {
		ticks = new ArrayList<Tick>();
	}

	public OptionSeries(String string) {
		// TODO Auto-generated constructor stub
		name = string;
		ticks = new ArrayList<Tick>();
	}

	public static void removeTick(int index) {
		// TODO -- remove index tick
		ticks.remove(index);
	}

	public static void removeLastTick() {
		// TODO -- remove index tick
		ticks.remove(ticks.size() - 1);
	}

	public static void addTick(Tick myTick) {
		// TODO -- add a new tick to list
		if (ticks.size() < maximumTickCount) {
			ticks.add(myTick);
		}
		else{
			ticks.remove(0);
			ticks.add(myTick);
		}
	}

	public static Tick getTick(int index) {
		// TODO -- add a new tick to list
		return ticks.get(index);
	}

}
