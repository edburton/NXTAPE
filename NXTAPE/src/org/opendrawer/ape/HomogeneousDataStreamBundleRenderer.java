package org.opendrawer.ape;

import org.opendrawer.ape.nxt.NXTRenderer;
import org.opendrawer.dawinian.neurodynamics.HomogeneousDataStreamBundle;

import processing.core.PGraphics;

public class HomogeneousDataStreamBundleRenderer extends
		DataStreamBundleRenderer {

	NXTRenderer dataProviderRenderer;
	NXTRenderer mouseFocusedRenderer;

	public HomogeneousDataStreamBundleRenderer(
			HomogeneousDataStreamBundle dataStreamBundle,
			NXTRenderer dataProviderRenderer) {
		super(dataStreamBundle);
		this.dataProviderRenderer = dataProviderRenderer;
	}

	public HomogeneousDataStreamBundleRenderer(
			HomogeneousDataStreamBundle dataStreamBundle,
			NXTRenderer dataProviderRenderer, float x, float y, float width,
			float height) {
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

	@Override
	public void mouseClicked(int mouseX, int mouseY) {
		if (dataProviderRenderer.contains(mouseX, mouseY))
			dataProviderRenderer.mouseClicked(mouseX, mouseY);
	}

	@Override
	public void mousePressed(int mouseX, int mouseY) {
		if (dataProviderRenderer.contains(mouseX, mouseY)) {
			mouseFocusedRenderer = dataProviderRenderer;
			dataProviderRenderer.getDataProvider().setInhihited(true);
			mouseFocusedRenderer.mousePressed(mouseX, mouseY);

		}
	}

	@Override
	public void mouseDragged(int mouseX, int mouseY) {
		if (mouseFocusedRenderer != null)
			mouseFocusedRenderer.mouseDragged(mouseX, mouseY);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		if (mouseFocusedRenderer != null) {
			dataProviderRenderer.getDataProvider().setInhihited(false);
			mouseFocusedRenderer.mouseReleased(mouseX, mouseY);
			mouseFocusedRenderer = null;
		}
	}
}
