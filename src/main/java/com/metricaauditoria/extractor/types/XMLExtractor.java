package com.metricaauditoria.extractor.types;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.metricaauditoria.errors.ErrorException;
import com.metricaauditoria.extractor.Extractor;
import com.metricaauditoria.extractor.Value;

public class XMLExtractor implements Extractor {
	
	private NodeList values;
	
	public XMLExtractor(InputStream input, String selector) throws ErrorException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(input);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(selector);
			
			values = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
		}
		catch (XPathExpressionException e) {
			throw new ErrorException("Invalid XPath");
		} catch (ParserConfigurationException e) {
			throw new ErrorException("Error reading XML");
		} catch (SAXException e) {
			throw new ErrorException("Error reading XML");
		} catch (IOException e) {
			throw new ErrorException("Error reading XML");
		}
	}

	@Override
	public Value[] values() throws IOException {
		Value [] result = new Value[values.getLength()];
		int c = 0;
		for (int i = 0; i < values.getLength(); i++) {
			Node value = values.item(i);
			result[c] = new StringValue(value.getNodeValue());
		}
		
		return result;
	}

	
}
