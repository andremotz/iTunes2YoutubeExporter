package com.andremotz.itunesplaylistexporter.datahandling;

public class GlobalFunctions {
	/**
	 * Get the method name for a depth in call stack. <br />
	 * Utility function
	 * 
	 * @param depth
	 *            depth in the call stack (0 means current method, 1 means call
	 *            method, ...)
	 * @return method name
	 */
	// TODO make use of in all other classes
	public static String getMethodName(final int depth) {
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		return ste[2 + depth].getMethodName() + "()";

	}

}
