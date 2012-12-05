package org.opendrawer.ape.processing;

import org.opendrawer.ape.darwinianneurodynamics.HomogeneousDataStreamBundle;

import processing.core.PGraphics;

public class HomogeneousDataStreamBundleRenderer extends
		DataStreamBundleRenderer {

	DataProviderRenderer dataProviderRenderer;
	DataProviderRenderer mouseFocusedRenderer;

	public HomogeneousDataStreamBundleRenderer(
			HomogeneousDataStreamBundle dataStreamBundle,
			DataProviderRenderer dataProviderRenderer) {
		super(dataStreamBundle);
		this.dataProviderRenderer = dataProviderRenderer;
	}

	public HomogeneousDataStreamBundleRenderer(
			HomogeneousDataStreamBundle dataStreamBundle,
			DataProviderRenderer dataProviderRenderer, float x, float y,
			float width, float height) {
		super(dataStreamBundle, x, y, width, height);
		this.dataProviderRenderer = dataProviderRenderer;
		setVisibleAt(x, y, width, height);
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		dataProviderRenderer.draw(g);
	}

	@Override
	protected float getStreamLeft() {
		return x + height;
	}

	@Override
	public void setVisibleAt(float x, float y, float width, float height) {
		super.setVisibleAt(x, y, width, height);
		dataProviderRenderer.setVisibleAt(x, y, height, height);
	}
}
