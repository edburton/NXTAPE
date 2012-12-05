package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.DataProvider;
import org.opendrawer.ape.processing.DataProviderRenderer;
import org.opendrawer.ape.processing.Renderer;

import processing.core.PGraphics;

public class MuscleRenderer extends DataProviderRenderer {
	Muscle muscle;

	public MuscleRenderer(Muscle muscle) {
		super();
		this.muscle = muscle;
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		g.strokeWeight(Renderer.lineWidth);
		float radius = (Math.min(width, height) / 2);
		float xc = x + width / 2;
		float yc = y + height / 2;
		g.noStroke();
		g.fill(0, 128, 0);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		radius -= Renderer.lineMarginWidth;
		g.fill(16);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		g.stroke(255, 255, 0);
		float a = (float) ((muscle.getData()[0]) * (Math.PI));
		g.line(xc, yc, (float) (xc + Math.sin(a) * radius),
				(float) (yc + Math.cos(a) * radius));
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
	public void mouseClicked(int mouseX, int mouseY) {
		float xc = x + width / 2;
		float yc = y + height / 2;
		float dx = mouseX - xc;
		float dy = mouseY - yc;
		float a = (float) (Math.atan2(dx, dy) * (360 / (Math.PI)));
		muscle.setOutputChannel(a, 0);
	}

	@Override
	public void mousePressed(int mouseX, int mouseY) {
		mouseClicked(mouseX, mouseY);
	}

	@Override
	public void mouseDragged(int mouseX, int mouseY) {
		mouseClicked(mouseX, mouseY);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		mouseClicked(mouseX, mouseY);
	}

	@Override
	public DataProvider getDataProvider() {
		return muscle;
	}
}
