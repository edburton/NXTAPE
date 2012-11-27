package org.opendrawer.nxtape;

import java.awt.Color;

import processing.core.PGraphics;

public class Renderer {
	protected float x, y, width, height;
	protected boolean visible = false;
	protected Color keyColor = null;

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
			g.rect(x + NXT_ArtificialPlasticityEcology.lineMarginWidth, y
					+ NXT_ArtificialPlasticityEcology.lineMarginWidth,
					(x + width)
							- NXT_ArtificialPlasticityEcology.lineMarginWidth,
					(y + height)
							- NXT_ArtificialPlasticityEcology.lineMarginWidth);
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void mouseClicked(int mouseX, int mouseY) {
	}

	public void mousePressed(int mouseX, int mouseY) {
	}

	public void mouseDragged(int mouseX, int mouseY) {
	}

	public void mouseReleased(int mouseX, int mouseY) {
	}

	public void setKeyColor(Color keyColor) {
		this.keyColor = keyColor;
	}
}
