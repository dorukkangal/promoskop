package com.mudo.test;

import java.io.PrintStream;

public class Time {

	private String name;
	private long initialTime;

	public Time(String name) {
		this.name = name;
		refreshTime();
	}

	public void miliseconds(PrintStream out) {
		out.println(name + ": " + ((System.currentTimeMillis() - initialTime)) + " miliseconds.");
	}

	public void refreshTime() {
		initialTime = System.currentTimeMillis();
	}
}
