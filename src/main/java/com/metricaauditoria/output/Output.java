package com.metricaauditoria.output;

public enum Output {

	INSTANCE;
	
	private boolean enabledDebug = false;
	
	public void info(String message) {
		System.out.println(message);
	}
	
	public void error(String message) {
		System.err.println(message);
	}
	
	public void debug(String message) {
		if (enabledDebug) System.out.println(message);
	}

	public void setDebugMode(boolean debugEnabled) {
		this.enabledDebug = debugEnabled;
	}
}
