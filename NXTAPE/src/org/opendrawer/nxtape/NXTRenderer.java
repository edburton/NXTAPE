package org.opendrawer.nxtape;

import org.opendrawer.dawinian.neurodynamics.DataProvider;

public abstract class NXTRenderer extends Renderer implements
		InteractiveRenderer {

	public NXTRenderer() {

	}

	public NXTRenderer(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	public abstract DataProvider getDataProvider();
}
