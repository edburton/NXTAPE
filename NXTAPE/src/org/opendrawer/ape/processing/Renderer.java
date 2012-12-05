package org.opendrawer.ape.processing;

import java.awt.Color;

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

	public boolean contains(float x1, float y1) {
		return x1 >= x && y1 >= y && x1 <= x + width && y1 <= y + height;
	}

	public void draw(PGraphics g) {
		if (keyColor != null) {
			g.noStroke();
			g.fill(keyColor.getRed(), keyColor.getGreen(), keyColor.getBlue());
			g.rect(x, y, x + width, y + height);
			g.fill(keyColor.getRed() / 4, keyColor.getGreen() / 4,
					keyColor.getBlue() / 4);
			g.rect(x + Renderer.lineMarginWidth, y + Renderer.lineMarginWidth,
					(x + width) - Renderer.lineMarginWidth, (y + height)
							- Renderer.lineMarginWidth);
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
