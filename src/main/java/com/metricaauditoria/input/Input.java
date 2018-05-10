package com.metricaauditoria.input;

import com.metricaauditoria.errors.ErrorException;

public class Input {

	private String [] args;
	
	public Input(String [] args) {
		this.args = args;
	}
	
	public boolean isDebugEnabled() {
		if (args.length >= 1)
			return args[args.length - 1].equals("-d");
		else
			return false;
	}
	
	public String getString(int position) throws ErrorException {
		if (args.length > position)
			return args[position];
		else
			throw new ErrorException("Faltan parámetros requeridos.");
		
	}
}
