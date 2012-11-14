package org.opendrawer.nxtape;

import processing.core.PGraphics;

public interface GraphicalObject {
	public void drawAt(PGraphics g, float x, float y, float width, float height);
}
