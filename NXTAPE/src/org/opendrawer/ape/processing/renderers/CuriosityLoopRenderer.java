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
			g.fill(keyColor.getRed(), keyColor.getGreen(), keyColor.getBlue(),
					64);
			g.rect(x, y, x + width, y + height);
		}
	}
}
