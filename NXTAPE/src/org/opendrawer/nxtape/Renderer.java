package org.opendrawer.nxtape;

import processing.core.PGraphics;

public abstract class Renderer {
	protected float x, y, width, height;

	public Renderer() {

	}

	public Renderer(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void setPosition(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public abstract void draw(PGraphics g);

}
