package org.opendrawer.nxtape;

import org.opendrawer.dawinian.neurodynamics.DataProvider;

import processing.core.PGraphics;

public abstract class NXTRenderer extends InteractiveRenderer {

	public NXTRenderer() {

	}

	public NXTRenderer(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	@Override
	public abstract void draw(PGraphics g);

	public abstract DataProvider getDataProvider();

}
