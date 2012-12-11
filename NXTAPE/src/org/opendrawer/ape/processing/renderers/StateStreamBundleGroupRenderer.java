package org.opendrawer.ape.processing.renderers;

import java.awt.Color;
import java.util.ArrayList;

import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundleGroup;

public class StateStreamBundleGroupRenderer extends Renderer {
	// private final ArrayList<StateStreamBundleRenderer>
	// stateStreamBundleRenderers = new ArrayList<StateStreamBundleRenderer>();

	public StateStreamBundleGroupRenderer(
			StateStreamBundleGroup statesStreamBundleList) {
		if (statesStreamBundleList != null) {
			addStatesStreamBundles(statesStreamBundleList
					.getStatesStreamBundles());
		}
	}

	public void addStatesStreamBundle(StateStreamBundle stateStreamBundle) {
		Renderer renderer = Renderer.makeRendererFor(stateStreamBundle);
		addChild(Renderer.makeRendererFor(stateStreamBundle));
	}

	public void addStatesStreamBundles(
			ArrayList<StateStreamBundle> stateStreamBundles) {
		for (int i = 0; i < stateStreamBundles.size(); i++)
			addChild(Renderer.makeRendererFor(stateStreamBundles.get(i)));
		// setKeyColor(keyColor);
	}

	protected float getStreamLeft() {
		return x;
	}

	@Override
	public void setVisibleAt(float x, float y, float width, float height) {
		super.setVisibleAt(x, y, width, height);
		if (children != null)
			if (children.size() > 0) {
				float moduleWidth = ((width - ((lineMarginWidth / 2) * (children
						.size() - 1))) / children.size());
				float moduleX = x;
				for (int i = 0; i < children.size(); i++) {
					StateStreamBundleRenderer statesStreamBundleRender = (StateStreamBundleRenderer) children
							.get(i);
					statesStreamBundleRender.setVisibleAt(moduleX, y,
							moduleWidth, height);
					moduleX += moduleWidth + (lineMarginWidth / 2);
				}
			}
	}

	@Override
	public void setKeyColor(Color keyColor) {
		super.setKeyColor(keyColor);
		if (children != null)
			for (int i = 0; i < children.size(); i++) {
				children.get(i).setKeyColor(keyColor);
			}
	}
}