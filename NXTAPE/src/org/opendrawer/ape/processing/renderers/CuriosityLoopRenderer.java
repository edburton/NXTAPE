package org.opendrawer.ape.processing.renderers;

import org.opendrawer.ape.darwinianneurodynamics.CuriosityLoop;

import processing.core.PGraphics;

public class CuriosityLoopRenderer extends StateStreamBundleGroupListRenderer {
	CuriosityLoop curiosityLoop;

	public CuriosityLoopRenderer(CuriosityLoop curiosityLoop) {
		super(curiosityLoop);
		this.curiosityLoop = curiosityLoop;
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		if (curiosityLoop.isActive()) {
			g.noStroke();
			float t = (float) (Math
					.cos(((System.currentTimeMillis() * 2 * Math.PI) / 1000.0) + 1) / 2);
			t *= t;
			g.fill(Math.round(keyColor.getRed()),
					Math.round(keyColor.getGreen()),
					Math.round(keyColor.getBlue()), Math.round(100 * t));
			g.rect(x, y, x + width, y + height);
		}
	}
}
