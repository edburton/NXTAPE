package org.opendrawer.ape.processing;

import org.opendrawer.ape.darwinianneurodynamics.DataProvider;

public abstract class DataProviderRenderer extends Renderer {

	public DataProviderRenderer() {

	}

	public DataProviderRenderer(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	public abstract DataProvider getDataProvider();
}
