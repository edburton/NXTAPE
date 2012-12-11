package org.opendrawer.ape.processing.renderers;

import org.opendrawer.ape.darwinianneurodynamics.HomogeneousStateStreamBundle;

public class HomogeneousStateStreamBundleRenderer extends
		StateStreamBundleRenderer {

	private Renderer statesProviderRenderer = null;

	public HomogeneousStateStreamBundleRenderer(
			HomogeneousStateStreamBundle statesStreamBundle) {
		super(statesStreamBundle);
		if (statesStreamBundle.getStateProvider() != null)
			this.statesProviderRenderer = Renderer
					.makeRendererFor(statesStreamBundle.getStateProvider());
		if (this.statesProviderRenderer != null)
			addChild(this.statesProviderRenderer);
	}

	@Override
	protected float getStreamLeft() {
		if (statesProviderRenderer != null)
			return x + height;
		else
			return x;
	}

	@Override
	public void setVisibleAt(float x, float y, float width, float height) {
		super.setVisibleAt(x, y, width, height);
		if (statesProviderRenderer != null)
			statesProviderRenderer.setVisibleAt(x + Renderer.lineWidth, y
					+ Renderer.lineWidth, height - Renderer.lineWidth * 2,
					height - Renderer.lineWidth * 2);
	}
}
