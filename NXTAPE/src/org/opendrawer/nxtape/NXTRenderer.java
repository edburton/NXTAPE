package org.opendrawer.nxtape;

import org.opendrawer.dawinian.neurodynamics.DataProvider;

import processing.core.PGraphics;

public abstract class NXTRenderer extends InteractiveRenderer {

	public NXTRenderer() {
		// TODO Auto-generated constructor stub
	}

	public NXTRenderer(float x, float y, float width, float height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(PGraphics g) {
		// TODO Auto-generated method stub

	}

	public abstract DataProvider getDataProvider();

}
