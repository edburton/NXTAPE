package org.opendrawer.ape.processing;

import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;

public abstract class StatesProviderRenderer extends Renderer {

	public StatesProviderRenderer() {

	}

	public StatesProviderRenderer(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	public abstract StatesProvider getStatesProvider();
}
