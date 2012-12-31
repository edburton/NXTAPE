package org.opendrawer.ape.processing.renderers;

import java.util.ArrayList;

import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundleGroup;

public class StateStreamBundleGroupRenderer extends Renderer {

	public StateStreamBundleGroupRenderer(
			StateStreamBundleGroup statesStreamBundleGroup) {
		if (statesStreamBundleGroup != null) {
			addStatesStreamBundles(statesStreamBundleGroup
					.getStatesStreamBundles());
		}
	}

	public void addStatesStreamBundle(StateStreamBundle stateStreamBundle) {
		addChild(Renderer.makeRendererFor(stateStreamBundle));
	}

	public void addStatesStreamBundles(
			ArrayList<StateStreamBundle> stateStreamBundles) {
		for (int i = 0; i < stateStreamBundles.size(); i++)
			addChild(Renderer.makeRendererFor(stateStreamBundles.get(i)));
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
					float xx = moduleX + lineMarginWidth * 2;
					float yy = y + lineMarginWidth * 2;
					float ww = moduleWidth - lineMarginWidth * 2;
					float hh = height - lineMarginWidth * 2;
					StateStreamBundleRenderer statesStreamBundleRender = (StateStreamBundleRenderer) children
							.get(i);
					statesStreamBundleRender.setVisibleAt(xx, yy, ww, hh);
					moduleX += moduleWidth + (lineMarginWidth / 2);
				}
			}
	}
}