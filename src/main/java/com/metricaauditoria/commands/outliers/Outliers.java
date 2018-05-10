package com.metricaauditoria.commands.outliers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import com.metricaauditoria.errors.ErrorException;
import com.metricaauditoria.extractor.Extractor;
import com.metricaauditoria.extractor.ExtractorFactory;
import com.metricaauditoria.extractor.Value;
import com.metricaauditoria.input.Input;
import com.metricaauditoria.output.Output;

public class Outliers {

	public static void main(String[] args) {
		
		Input input = new Input(args);
		
		try {
			Output.INSTANCE.setDebugMode(input.isDebugEnabled());
		
			String selector = input.getString(0);
			String filename = input.getString(1);
			
			Extractor extractor = ExtractorFactory.create(filename, selector);
			calculate(extractor.values());

		} catch (FileNotFoundException e) {
			error("File not found");
		} catch (IOException e) {
			error("I/O");
		} catch (ErrorException e) {
			error(e.getMessage());
		}
	}
	
	private static void error(String cause) {
		System.out.println("ERROR: " + cause + ".");
		System.out.println("\toutliers <selector> <filename>");
	}
	
	private static void calculate(Value [] result) {
		
		double [] values = Arrays.stream(result)
				.map(Value::asDouble)
				.filter(v -> v != null)
				.mapToDouble(Double::doubleValue)
				.toArray();
		
		Arrays.sort(values);
		
		double Q1 = values[(int)(values.length * 0.25)];
		double Q3 = values[(int)(values.length * 0.75)];
		
		long outliers = Arrays.stream(values)
				.filter(value -> 
					value < Q1 - 3*(Q3 - Q1)
					|| value > Q3 + 3*(Q3 - Q1))
				.count();
		
		Output.INSTANCE.info("Outliers: " + outliers);
		Output.INSTANCE.info("Correct:  " + (values.length - outliers));
		Output.INSTANCE.info("TOTAL:    " + values.length);
	}

}
