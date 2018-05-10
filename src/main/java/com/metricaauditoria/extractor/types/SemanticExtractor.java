package com.metricaauditoria.extractor.types;

import java.io.IOException;
import java.io.InputStream;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import com.metricaauditoria.errors.ErrorException;
import com.metricaauditoria.extractor.Extractor;
import com.metricaauditoria.extractor.Value;

public class SemanticExtractor implements Extractor {

	private Model model;
	private Node predicate;
	
	public SemanticExtractor(InputStream input, String selector, String format) throws ErrorException {

		try {
			model = ModelFactory.createDefaultModel();
	
			model.read(input, null, format);
			predicate = NodeFactory.createURI(selector);
		}
		catch (Exception e) {
			throw new ErrorException(e.getMessage());
		}
	}
	@Override
	public Value[] values() throws IOException {

		return model.getGraph().find()
				.filterKeep(triple -> triple.predicateMatches(predicate))
				.mapWith(this::extractFromTriple)
				.toList().stream()
				.toArray(Value[]::new);
	}
			
	private Value extractFromTriple(Triple triple) {
		String strValue = triple.getMatchObject().getLiteral().toString();
		return new StringValue(strValue);
	}

}
