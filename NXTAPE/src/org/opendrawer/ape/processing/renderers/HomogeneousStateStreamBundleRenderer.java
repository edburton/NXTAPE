package org.opendrawer.ape.processing.renderers;

import org.opendrawer.ape.darwinianneurodynamics.HomogeneousStateStreamBundle;

public class HomogeneousStateStreamBundleRenderer extends
		StateStreamBundleRenderer {

	private final Renderer statesProviderRenderer;

	public HomogeneousStateStreamBundleRenderer(
			HomogeneousStateStreamBundle statesStreamBundle) {
		super(statesStreamBundle);
		statesProviderRenderer = Renderer.makeRendererFor(statesStreamBundle
				.getStateProvider());
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
