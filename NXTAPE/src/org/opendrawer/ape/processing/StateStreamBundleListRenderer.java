package org.opendrawer.ape.processing;

import java.awt.Color;
import java.util.ArrayList;

import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundleGroup;

import processing.core.PGraphics;

public class StateStreamBundleListRenderer extends Renderer {
	private final ArrayList<StateStreamBundleRenderer> stateStreamBundleRenderers = new ArrayList<StateStreamBundleRenderer>();

	public StateStreamBundleListRenderer(
			StateStreamBundleGroup statesStreamBundleList, float x, float y,
			float width, float height) {
		super();
		if (statesStreamBundleList != null) {
			addStatesStreamBundles(statesStreamBundleList
					.getStatesStreamBundles());
		}
		setVisibleAt(x, y, width, height);
	}

	public StateStreamBundleListRenderer(
			StateStreamBundleGroup statesStreamBundleList) {
		super();
		if (statesStreamBundleList != null) {
			addStatesStreamBundles(statesStreamBundleList
					.getStatesStreamBundles());
		}
	}

	public StateStreamBundleListRenderer() {
		super();
	}

	public void addStatesStreamBundle(StateStreamBundle stateStreamBundle) {
		stateStreamBundleRenderers.add(new StateStreamBundleRenderer(
				stateStreamBundle));
	}

	public void addStatesStreamBundles(
			ArrayList<StateStreamBundle> stateStreamBundles) {
		for (int i = 0; i < stateStreamBundles.size(); i++)
			stateStreamBundleRenderers.add(new StateStreamBundleRenderer(
					stateStreamBundles.get(i)));
		setKeyColor(keyColor);
	}

	@Override
	public void draw(PGraphics g) {
		if (stateStreamBundleRenderers.size() == 0)
			super.draw(g);
		else
			for (int i = stateStreamBundleRenderers.size() - 1; i >= 0; i--)
				stateStreamBundleRenderers.get(i).draw(g);
	}

	protected float getStreamLeft() {
		return x;
	}

	@Override
	public void setVisibleAt(float x, float y, float width, float height) {
		super.setVisibleAt(x, y, width, height);
		if (stateStreamBundleRenderers.size() > 0) {
			float moduleWidth = ((width - ((lineMarginWidth / 2) * (stateStreamBundleRenderers
					.size() - 1))) / stateStreamBundleRenderers.size());
			float moduleX = x;
			for (int i = 0; i < stateStreamBundleRenderers.size(); i++) {
				StateStreamBundleRenderer statesStreamBundleRender = stateStreamBundleRenderers
						.get(i);
				statesStreamBundleRender.setVisibleAt(moduleX, y, moduleWidth,
						height);
				moduleX += moduleWidth + (lineMarginWidth / 2);
			}
		}
	}

	public ArrayList<StateStreamBundleRenderer> getStatesStreamBundleRenderers() {
		return stateStreamBundleRenderers;
	}

	@Override
	public void setKeyColor(Color keyColor) {
		super.setKeyColor(keyColor);
		for (int i = 0; i < stateStreamBundleRenderers.size(); i++) {
			stateStreamBundleRenderers.get(i).setKeyColor(keyColor);
		}
	}
}