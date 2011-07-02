package com.mscg.svn.tes;

import com.mscg.svn.tes.util.TestClass;

public class SvnTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("This is a simple svn branch test");
		System.out.println("This line came from the main trunk");
		System.out.println("This line is only in the branch");
		
		uselessMethod();
		TestClass tc = new TestClass(4);
		System.out.println(tc.getNumber());
	}

	public static void myMethod() {
	    System.out.println(45);
	}
	
	public static void uselessMethod() {
		System.out.println("This method is useless");
	}

}
