package com.metricaauditoria.commands.functionaldependences;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import com.metricaauditoria.errors.ErrorException;
import com.metricaauditoria.extractor.Extractor;
import com.metricaauditoria.extractor.ExtractorFactory;
import com.metricaauditoria.extractor.Value;
import com.metricaauditoria.input.Input;
import com.metricaauditoria.output.Output;

public class FunctionalDependences {

	public static void main(String[] args) {
		
		Input input = new Input(args);
		
		try {
			Output.INSTANCE.setDebugMode(input.isDebugEnabled());
		
			String selectorMain = input.getString(0);
			String selectorSub = input.getString(1);
			String filename = input.getString(2);
			
			Extractor extractorMain = ExtractorFactory.create(filename, selectorMain);
			Extractor extractorSub = ExtractorFactory.create(filename, selectorSub);
			calculate(extractorMain.values(), extractorSub.values());

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
		System.out.println("\functionaldependences <selector> <filename>");
	}
	
	private static void calculate(Value [] resultMain, Value [] resultSub) {
		
		String [] valuesMain = Arrays.stream(resultMain)
				.map(Value::asString)
				.toArray(String[]::new);

		String [] valuesSub = Arrays.stream(resultMain)
				.map(Value::asString)
				.toArray(String[]::new);

		int position = 0;
		int unsatisfied = 0;
		for (String valueMain: valuesMain) {
			
			String valueSub = valuesSub[position++];
			boolean isSatisfied = true;
			int otherPosition = 0;

			for (String otherValueMain: valuesMain) {
				String otherValueSub = valuesSub[otherPosition++];
				if (otherValueMain.equals(valueMain) && !valueSub.equals(otherValueSub)) {
					isSatisfied = false;
				}
			}
			
			if (!isSatisfied) unsatisfied++;
		}
		
		Output.INSTANCE.info("Unsatisfied dependencies:  " + unsatisfied);
		Output.INSTANCE.info("Satisfied dependencies:    " + (valuesMain.length - unsatisfied));
		Output.INSTANCE.info("TOTAL:                     " + valuesMain.length);
	}

}
