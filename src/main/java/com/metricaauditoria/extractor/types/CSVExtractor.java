package com.metricaauditoria.extractor.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.metricaauditoria.errors.ErrorException;
import com.metricaauditoria.extractor.Extractor;
import com.metricaauditoria.extractor.Value;
import com.opencsv.CSVReader;

public class CSVExtractor implements Extractor {
	
	private Value [] values;
	private int column;
	
	public CSVExtractor(InputStream input, String selector) throws ErrorException, IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(input));
		try {
			column = Integer.parseInt(selector);
			values = reader.readAll().stream()
					.map(this::parseLine)
					.toArray(Value[]::new);
		}
		catch (NumberFormatException e) {
			throw new ErrorException("Selector must be a number if filename is a CSV");
		}
		finally {
			reader.close();
		}
	}

	@Override
	public Value[] values() {
		return values;
	}
	
	private Value parseLine(String [] line) {
		return new StringValue(line[column]);

	}

}
