package org.opendrawer.nxtape;

import java.awt.Color;

import processing.core.PGraphics;

public class DataStreamBundleRenderer extends InteractiveRenderer {
	private DataStreamBundle dataStreamBundle;
	private InteractiveRenderer dataProviderRenderer;
	private InteractiveRenderer mouseFocusedRenderer;

	public DataStreamBundleRenderer(DataStreamBundle dataStreamBundle,
			InteractiveRenderer dataProviderRenderer, float x, float y,
			float width, float height) {
		super(x, y, width, height);
		this.dataStreamBundle = dataStreamBundle;
		this.dataProviderRenderer = dataProviderRenderer;
		dataProviderRenderer.setPosition(x, y, height, height);
	}

	@Override
	public void draw(PGraphics g) {
		dataProviderRenderer.draw(g);
		g.stroke(64, 64, 64);
		g.strokeWeight(NXT_ArtificialPlasticityEcology.lineWidth);
		g.line(x + height, y + height / 2, x + width, y + height / 2);
		int nc = dataStreamBundle.getDataProvider().getChannelCount();
		for (int c = nc - 1; c >= 0; c--) {
			float xx = 0, yy = 0;
			boolean started = false;
			int dataWidth = dataStreamBundle.getDataWidth();
			g.stroke(Color.HSBtoRGB(c / (float) nc, 1.0f, 1.0f));
			for (int i = 0; i < dataWidth; i++) {
				double v = dataStreamBundle.read(i, c);
				if (v != Float.NaN) {
					float x1 = (x + height)
							+ ((i / (float) dataWidth) * (width - height));
					float y1 = (float) ((y + height / 2) - v * height / 2);
					if (started) {
						// g.fill(Color.HSBtoRGB(c / (double) nc, 1.0f, 1.0f));
						// g.rect(Math.min(x1, xx) - 0.0f,
						// Math.min(y1, yy) - 2.5f,
						// Math.max(x1, xx) + 0.0f,
						// Math.max(y1, yy) + 2.5f);

						g.line(x1, y1, xx, yy);
					}
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
		return dataStreamBundle;
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
			dataStreamBundle.getDataProvider().setInhihited(true);
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
			dataStreamBundle.getDataProvider().setInhihited(false);
			mouseFocusedRenderer.mouseReleased(mouseX, mouseY);
			mouseFocusedRenderer = null;
		}
	}
}