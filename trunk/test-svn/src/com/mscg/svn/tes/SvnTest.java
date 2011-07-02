package com.mscg.svn.tes;

public class SvnTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("This is a simple svn branch test");
		System.out.println("This line came from the main trunk");
		uselessMethod();
		TestClass tc = new TestClass(4);
		System.out.println(tc.getNumber());
	}
	
	public static void uselessMethod() {
		System.out.println("This method is useless");
	}

}
