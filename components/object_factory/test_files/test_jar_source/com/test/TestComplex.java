package com.test;

public class TestComplex {
	private final String represent;
	public TestComplex(int a, String b) {
		represent = "Complex" + a + b.toString();
	}
	public String toStrong() {
		return represent;
	}
}