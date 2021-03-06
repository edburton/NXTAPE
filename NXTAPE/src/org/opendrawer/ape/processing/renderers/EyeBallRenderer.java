package org.opendrawer.ape.processing.renderers;

import org.opendrawer.ape.processing.nxt.dummy.EyeBall;

import processing.core.PGraphics;

public class EyeBallRenderer extends Renderer {
	EyeBall eyeBall;

	public EyeBallRenderer(EyeBall eyeBall) {
		this.eyeBall = eyeBall;
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		float radius = (Math.min(width, height) / 2) - Renderer.lineMarginWidth;
		float xc = x + width / 2;
		float yc = y + height / 2;
		g.noStroke();
		g.fill(255, 200, 200);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		radius -= Renderer.lineMarginWidth;
		g.fill(255, 240, 240);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);

		double ex = eyeBall.getX() / 2;
		double ey = eyeBall.getY() / 2;
		float dx = (float) (ex * width / 2);
		float dy = (float) (ey * height / 2);

		int nm = eyeBall.getMuscleCount();
		g.strokeWeight(Renderer.lineWidth);
		g.stroke(255, 200, 200);
		for (int i = 0; i < nm; i++) {
			float mx = -eyeBall.getMuscleX(i) * radius;
			float my = -eyeBall.getMuscleY(i) * radius;
			mx /= 2;
			my /= 2;
			g.line(xc + (mx * 3 + dx) / 4, yc + (my * 3 + dy) / 4, xc + mx, yc
					+ my);
		}
		g.noStroke();

		g.fill(100, 200, 250);
		float idx = dx * 0.75f;
		float idy = dy * 0.75f;
		g.ellipse(idx + xc - radius / 2, idy + yc - radius / 2, idx + xc
				+ radius / 2, idy + yc + radius / 2);
		g.fill(0, 0, 0);
		g.ellipse(dx + xc - radius / 4, dy + yc - radius / 4, dx + xc + radius
				/ 4, dy + yc + radius / 4);
	}
}
