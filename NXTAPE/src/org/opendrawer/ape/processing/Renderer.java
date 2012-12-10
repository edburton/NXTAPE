package org.opendrawer.ape.processing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import processing.core.PConstants;
import processing.core.PGraphics;

public class Renderer {
	protected float x, y, width, height;
	protected boolean visible = false;
	protected Color keyColor = null;
	public static float lineMarginWidth;
	public static float lineWidth;
	protected List<Renderer> children;

	public Renderer(Object object) {

	}

	public void setVisibleAt(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		visible = true;
	}

	public void addChild(Renderer child) {
		if (children == null)
			children = new ArrayList<Renderer>();
		children.add(child);
	}

	public void draw(PGraphics g) {
		if (keyColor != null) {
			g.noStroke();
			g.fill(keyColor.getRed() * .25f, keyColor.getGreen() * .25f,
					keyColor.getBlue() * .25f);
			g.beginShape(PConstants.QUADS);
			g.vertex(x - lineMarginWidth, y - lineMarginWidth);
			g.vertex(x + width - lineMarginWidth, y - lineMarginWidth);
			g.vertex(x + width, y);
			g.vertex(x, y);
			g.endShape();
			g.fill(Math.round(keyColor.getRed() * .5f),
					Math.round(keyColor.getGreen() * .5f),
					Math.round(keyColor.getBlue()) * .5f);
			g.beginShape(PConstants.QUADS);
			g.vertex(x - lineMarginWidth, y - lineMarginWidth);
			g.vertex(x - lineMarginWidth, y + height - lineMarginWidth);
			g.vertex(x, y + height);
			g.vertex(x, y);
			g.endShape();
			g.fill(Math.round(keyColor.getRed() * .125f),
					Math.round(keyColor.getGreen() * .125f),
					Math.round(keyColor.getBlue() * .125f));
			g.rect(x, y, x + width, y + height);
		}
		if (children != null)
			for (int i = 0; i < children.size(); i++)
				children.get(i).draw(g);
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

	public static Color createKeyColour(int index, int outOf, float intensity) {
		return new Color(Color.HSBtoRGB(index / (float) outOf, 1.0f, intensity));
	}
}
