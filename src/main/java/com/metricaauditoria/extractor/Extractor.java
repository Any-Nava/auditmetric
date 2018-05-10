package com.metricaauditoria.extractor;

import java.io.IOException;

public interface Extractor {
	
	public Value [] values() throws IOException;

}
