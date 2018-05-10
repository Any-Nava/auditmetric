package com.metricaauditoria.extractor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.metricaauditoria.errors.ErrorException;
import com.metricaauditoria.extractor.types.CSVExtractor;
import com.metricaauditoria.extractor.types.HTMLExtractor;
import com.metricaauditoria.extractor.types.SemanticExtractor;
import com.metricaauditoria.extractor.types.XMLExtractor;

public class ExtractorFactory {

	public static Extractor create(String filename, String selector) throws IOException, ErrorException {
		try (InputStream input = new FileInputStream(filename)) {
			if (filename.endsWith(".csv")) {
				return new CSVExtractor(input, selector);
			}
			else if (filename.endsWith(".html")) {
				return new HTMLExtractor(input, selector);
			}
			else if (filename.endsWith(".rdf")) {
				return new SemanticExtractor(input, selector, "RDF");
			}
			else if (filename.endsWith(".n3")) {
				return new SemanticExtractor(input, selector, "N3");
			}
			else if (filename.endsWith(".ttl")) {
				return new SemanticExtractor(input, selector, "TTL");
			}
			else if (filename.endsWith(".xml")) {
				return new XMLExtractor(input, selector);
			}
			else {
				throw new ErrorException("Invalid file format");
			}
		}
	}
}
