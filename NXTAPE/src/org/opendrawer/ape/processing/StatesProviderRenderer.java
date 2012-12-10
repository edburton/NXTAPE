package org.opendrawer.ape.processing;

import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;

public abstract class StatesProviderRenderer extends Renderer {

	public StatesProviderRenderer(Object object) {
		super(object);
	}

	public abstract StatesProvider getStatesProvider();
}
