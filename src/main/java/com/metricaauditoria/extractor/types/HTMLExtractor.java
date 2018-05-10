package com.metricaauditoria.extractor.types;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;

import com.metricaauditoria.errors.ErrorException;
import com.metricaauditoria.extractor.Extractor;
import com.metricaauditoria.extractor.Value;

public class HTMLExtractor implements Extractor {
	
	private Elements elements;
	
	public HTMLExtractor(InputStream input, String selector) throws ErrorException {
		
		 try {
			Document doc = Jsoup.parse(input, "utf-8", "");
			elements = doc.select(selector);
		} catch (IOException e) {
			throw new ErrorException("Error al leer el documento");
		} catch (Selector.SelectorParseException e) {
			throw new ErrorException("Error en el selector: " + e.getMessage());
		}
	}

	@Override
	public Value[] values() throws IOException {
		return elements.eachText().stream()
			.map((value) -> new StringValue(value))
			.toArray(Value[]::new);
	}
	
}