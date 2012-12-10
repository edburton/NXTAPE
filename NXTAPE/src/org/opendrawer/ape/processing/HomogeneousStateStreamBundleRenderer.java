package org.opendrawer.ape.processing;

import org.opendrawer.ape.darwinianneurodynamics.HomogeneousStateStreamBundle;

public class HomogeneousStateStreamBundleRenderer extends
		StateStreamBundleRenderer {

	Renderer statesProviderRenderer;

	public HomogeneousStateStreamBundleRenderer(
			HomogeneousStateStreamBundle statesStreamBundle,
			Renderer statesProviderRenderer) {
		super(statesStreamBundle);
		this.statesProviderRenderer = statesProviderRenderer;
		addChild(statesProviderRenderer);
	}

	@Override
	protected float getStreamLeft() {
		return x + height;
	}

	@Override
	public void setVisibleAt(float x, float y, float width, float height) {
		super.setVisibleAt(x, y, width, height);
		statesProviderRenderer.setVisibleAt(x + Renderer.lineWidth, y
				+ Renderer.lineWidth, height - Renderer.lineWidth * 2, height
				- Renderer.lineWidth * 2);
	}
}
