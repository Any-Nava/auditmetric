package com.metricaauditoria.extractor.types;

import com.metricaauditoria.extractor.Value;
import com.metricaauditoria.output.Output;

public class StringValue implements Value {
	
	private String value;

	public StringValue(String value) {
		super();
		this.value = value;
	}

	@Override
	public Double asDouble() {
		try {
			Double v = Double.parseDouble(value);
			Output.INSTANCE.debug("Value: " + v);
			return v;
		}
		catch (NumberFormatException e) {
			Output.INSTANCE.debug("Invalid column value: " + value);
			return null;
		}
	}

	@Override
	public String asString() {
		return value;
	}

}
