package org.opendrawer.ape.processing;

import java.awt.Color;

import processing.core.PConstants;
import processing.core.PGraphics;

public class Renderer {
	protected float x, y, width, height;
	protected boolean visible = false;
	protected Color keyColor = null;
	public static float lineMarginWidth;
	public static float lineWidth;

	public Renderer() {

	}

	public Renderer(float x, float y, float width, float height) {
		setVisibleAt(x, y, width, height);
	}

	public void setVisibleAt(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		visible = true;
	}

	public void draw(PGraphics g) {
		if (keyColor != null) {
			g.noStroke();
			g.fill(keyColor.getRed() * .75f, keyColor.getGreen() * .75f,
					keyColor.getBlue() * .75f);
			g.beginShape(PConstants.QUADS);
			g.vertex(x - lineMarginWidth, y - lineMarginWidth);
			g.vertex(x + width - lineMarginWidth, y - lineMarginWidth);
			g.vertex(x + width, y);
			g.vertex(x, y);
			g.endShape();
			g.fill(Math.round(keyColor.getRed()),
					Math.round(keyColor.getGreen()),
					Math.round(keyColor.getBlue()));
			g.beginShape(PConstants.QUADS);
			g.vertex(x - lineMarginWidth, y - lineMarginWidth);
			g.vertex(x - lineMarginWidth, y + height - lineMarginWidth);
			g.vertex(x, y + height);
			g.vertex(x, y);
			g.endShape();
			g.fill(Math.round(keyColor.getRed() * .25f),
					Math.round(keyColor.getGreen() * .25f),
					Math.round(keyColor.getBlue() * .25f));
			g.rect(x, y, x + width, y + height);
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setKeyColor(Color keyColor) {
		this.keyColor = keyColor;
	}
}
