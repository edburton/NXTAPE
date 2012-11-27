package org.opendrawer.nxtape;

import processing.core.PGraphics;

public class Renderer {
	protected float x, y, width, height;
	protected boolean visible = false;

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
}
