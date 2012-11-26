package org.opendrawer.nxtape;

import java.awt.Color;

import org.opendrawer.dawinian.neurodynamics.DataStreamBundle;
import org.opendrawer.dawinian.neurodynamics.HomogeneousDataStreamBundle;

import processing.core.PGraphics;

public class HomogeneousDataStreamBundleRenderer extends InteractiveRenderer {
	private HomogeneousDataStreamBundle homogeneousDataStreamBundle;
	private InteractiveRenderer dataProviderRenderer;
	private InteractiveRenderer mouseFocusedRenderer;

	public HomogeneousDataStreamBundleRenderer(
			HomogeneousDataStreamBundle homogeneousDataStreamBundle,
			InteractiveRenderer dataProviderRenderer, float x, float y,
			float width, float height) {
		super(x, y, width, height);
		this.homogeneousDataStreamBundle = homogeneousDataStreamBundle;
		this.dataProviderRenderer = dataProviderRenderer;
		dataProviderRenderer.setPosition(x, y, height, height);
	}

	@Override
	public void draw(PGraphics g) {
		dataProviderRenderer.draw(g);
		g.stroke(64, 64, 64);
		g.strokeWeight(NXT_ArtificialPlasticityEcology.lineWidth);
		g.line(x + height, y + height / 2, x + width, y + height / 2);
		int nc = homogeneousDataStreamBundle.getDataProvider()
				.getChannelCount();
		for (int c = nc - 1; c >= 0; c--) {
			float xx = 0, yy = 0;
			boolean started = false;
			int dataWidth = homogeneousDataStreamBundle.getDataWidth();
			g.stroke(Color.HSBtoRGB(c / (float) nc, 1.0f, 1.0f));
			for (int i = 0; i < dataWidth; i++) {
				double v = homogeneousDataStreamBundle.read(i, c);
				if (v != Float.NaN) {
					float x1 = (x + height)
							+ ((i / (float) dataWidth) * (width - height));
					float y1 = (float) ((y + height / 2) - v * height / 2);
					if (started)
						g.line(x1, y1, xx, yy);
					started = true;
					xx = x1;
					yy = y1;
				}
			}
		}
	}

	@Override
	public void setPosition(float x, float y, float width, float height) {
		super.setPosition(x, y, width, height);
		dataProviderRenderer.setPosition(x, y, height, height);
	}

	public DataStreamBundle getDataStreamBundle() {
		return homogeneousDataStreamBundle;
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
			homogeneousDataStreamBundle.getDataProvider().setInhihited(true);
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
			homogeneousDataStreamBundle.getDataProvider().setInhihited(false);
			mouseFocusedRenderer.mouseReleased(mouseX, mouseY);
			mouseFocusedRenderer = null;
		}
	}
}
