package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.DataProvider;
import org.opendrawer.ape.processing.DataProviderRenderer;
import org.opendrawer.ape.processing.Renderer;

import processing.core.PGraphics;

public class EyeBallRenderer extends DataProviderRenderer {
	EyeBall eyeBall;

	public EyeBallRenderer(EyeBall eyeBall) {
		super();
		this.eyeBall = eyeBall;
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		float radius = (Math.min(width, height) / 2);
		float xc = x + width / 2;
		float yc = y + height / 2;
		g.noStroke();
		g.fill(64, 64, 64);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		radius -= Renderer.lineMarginWidth;
		g.fill(16);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
	}

	@Override
	public boolean contains(float x1, float y1) {
		float radius = (Math.min(width, height) / 2) - Renderer.lineMarginWidth
				/ 2;
		float xc = x + width / 2;
		float yc = y + height / 2;
		float dx = x1 - xc;
		float dy = y1 - yc;
		return Math.sqrt(dx * dx + dy * dy) <= radius;
	}

	@Override
	public DataProvider getDataProvider() {
		return eyeBall;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY) {

	}

	@Override
	public void mousePressed(int mouseX, int mouseY) {

	}

	@Override
	public void mouseDragged(int mouseX, int mouseY) {

	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {

	}
}
