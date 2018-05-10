package com.metricaauditoria.commands.nullvalues;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

import com.metricaauditoria.errors.ErrorException;
import com.metricaauditoria.extractor.Extractor;
import com.metricaauditoria.extractor.ExtractorFactory;
import com.metricaauditoria.extractor.Value;
import com.metricaauditoria.input.Input;
import com.metricaauditoria.output.Output;

public class NullValues {

	public static void main(String[] args) {
		
		Input input = new Input(args);
		
		try {
			Output.INSTANCE.setDebugMode(input.isDebugEnabled());
		
			String selector = input.getString(0);
			String reNull = input.getString(1);
			String filename = input.getString(2);
			
			Extractor extractor = ExtractorFactory.create(filename, selector);
			calculate(extractor.values(), reNull);

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
		System.out.println("\tnullvalues <selector> <filename>");
	}
	
	private static void calculate(Value [] result, String reNull) {
		
		Pattern re = Pattern.compile(reNull);
		
		long nullValues = Arrays.stream(result)
				.map(Value::asString)
				.filter(re.asPredicate())
				.count();
		
		Output.INSTANCE.info("Null values: " + nullValues);
		Output.INSTANCE.info("Correct:     " + (result.length - nullValues));
		Output.INSTANCE.info("TOTAL:       " + result.length);
	}

}
