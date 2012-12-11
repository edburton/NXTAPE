package org.opendrawer.ape.processing.renderers;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

	@SuppressWarnings("unchecked")
	public static Renderer makeRendererFor(Object object) {
		try {

			Class<? extends Renderer> objectClass = (Class<? extends Renderer>) object
					.getClass();

			Class<Renderer> rendererClass = null;
			while (objectClass != null && rendererClass == null) {
				String rendererClassName = objectClass.getName() + "Renderer";
				rendererClassName = "org.opendrawer.ape.processing.renderers"
						+ rendererClassName.substring(rendererClassName
								.lastIndexOf('.'));
				try {
					rendererClass = (Class<Renderer>) Class
							.forName(rendererClassName);
				} catch (ClassNotFoundException e) {
					objectClass = (Class<Renderer>) objectClass.getSuperclass();
				}
			}
			if (rendererClass == null)
				return new Renderer();
			@SuppressWarnings("rawtypes")
			Class[] rendererArgsType = new Class[] { objectClass };
			Object[] rendererArgs = new Object[] { object };
			Constructor<Renderer> rendererConstructor = rendererClass
					.getConstructor(rendererArgsType);
			return rendererConstructor.newInstance(rendererArgs);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return new Renderer();
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
