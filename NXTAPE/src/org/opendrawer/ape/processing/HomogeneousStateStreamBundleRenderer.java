package org.opendrawer.ape.processing;

import org.opendrawer.ape.darwinianneurodynamics.HomogeneousStateStreamBundle;

import processing.core.PGraphics;

public class HomogeneousStateStreamBundleRenderer extends
		StateStreamBundleRenderer {

	StatesProviderRenderer statesProviderRenderer;
	StatesProviderRenderer mouseFocusedRenderer;

	public HomogeneousStateStreamBundleRenderer(
			HomogeneousStateStreamBundle statesStreamBundle,
			StatesProviderRenderer statesProviderRenderer) {
		super(statesStreamBundle);
		this.statesProviderRenderer = statesProviderRenderer;
	}

	public HomogeneousStateStreamBundleRenderer(
			HomogeneousStateStreamBundle statesStreamBundle,
			StatesProviderRenderer statesProviderRenderer, float x, float y,
			float width, float height) {
		super(statesStreamBundle, x, y, width, height);
		this.statesProviderRenderer = statesProviderRenderer;
		setVisibleAt(x, y, width, height);
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		statesProviderRenderer.draw(g);
	}

	@Override
	protected float getStreamLeft() {
		return x + height;
	}

	@Override
	public void setVisibleAt(float x, float y, float width, float height) {
		super.setVisibleAt(x, y, width, height);
		statesProviderRenderer.setVisibleAt(x, y, height, height);
	}
}
