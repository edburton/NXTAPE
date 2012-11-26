package org.opendrawer.nxtape;

import org.opendrawer.dawinian.neurodynamics.DataStreamBundleMap;

import processing.core.PGraphics;

public class DataStreamBundleMapRenderer extends Renderer {

	protected DataStreamBundleMap dataStreamBundleMap;

	public DataStreamBundleMapRenderer(DataStreamBundleMap dataStreamBundleMap,
			float x, float y, float width, float height) {
		super(x, y, width, height);
		this.dataStreamBundleMap = dataStreamBundleMap;
	}

	public DataStreamBundleMapRenderer(float x, float y, float width,
			float height) {
		super(x, y, width, height);
	}

	@Override
	public void draw(PGraphics g) {

	}

	protected float getStreamLeft() {
		return x;
	}

	@Override
	public void setPosition(float x, float y, float width, float height) {
		super.setPosition(x, y, width, height);
	}
}