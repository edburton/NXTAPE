package org.opendrawer.ape.processing.renderers;

import org.opendrawer.ape.darwinianneurodynamics.HomogeneousStateStreamBundle;

public class HomogeneousStateStreamBundleRenderer extends
		StateStreamBundleRenderer {

	private Renderer statesProviderRenderer = null;

	public HomogeneousStateStreamBundleRenderer(
			HomogeneousStateStreamBundle statesStreamBundle) {
		super(statesStreamBundle);
		if (statesStreamBundle.getStatesProvider() != null)
			this.statesProviderRenderer = Renderer
					.makeRendererFor(statesStreamBundle.getStatesProvider());
		if (this.statesProviderRenderer != null)
			addChild(this.statesProviderRenderer);
	}

	@Override
	protected float getStreamRightMargin() {
		if (statesProviderRenderer != null)
			return height;
		else
			return 0;
	}

	@Override
	public void setVisibleAt(float x, float y, float width, float height) {
		super.setVisibleAt(x, y, width, height);
		if (statesProviderRenderer != null)
			statesProviderRenderer.setVisibleAt((x + width - height)
					+ Renderer.lineMarginWidth * 2, y
					+ Renderer.lineMarginWidth * 2, height
					- Renderer.lineMarginWidth * 2, height
					- Renderer.lineMarginWidth * 2);
	}
}
