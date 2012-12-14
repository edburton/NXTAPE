package org.opendrawer.ape.processing.renderers;

import java.awt.Color;
import java.util.ArrayList;

import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundleGroup;
import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundleGroupList;

public class StateStreamBundleGroupListRenderer extends Renderer {

	public StateStreamBundleGroupListRenderer(
			StateStreamBundleGroupList stateStreamBundleGroupList) {
		if (stateStreamBundleGroupList != null) {
			addStatesStreamBundleGroups(stateStreamBundleGroupList
					.getStatesStreamBundleGroups());
		}
	}

	public void addStatesStreamBundleGroup(
			StateStreamBundleGroup stateStreamBundleGroup) {
		addChild(Renderer.makeRendererFor(stateStreamBundleGroup));
	}

	public void addStatesStreamBundleGroups(
			ArrayList<StateStreamBundleGroup> stateStreamBundleGroups) {
		for (int i = 0; i < stateStreamBundleGroups.size(); i++)
			addChild(Renderer.makeRendererFor(stateStreamBundleGroups.get(i)));
	}

	@Override
	public void setVisibleAt(float x, float y, float width, float height) {
		super.setVisibleAt(x, y, width, height);
		if (children != null)
			if (children.size() > 0) {
				float moduleHeight = ((height - ((lineMarginWidth / 2) * (children
						.size() - 1))) / children.size());
				float moduleY = y;
				for (int i = 0; i < children.size(); i++) {
					float xx = x + lineMarginWidth * 2;
					float yy = moduleY + lineMarginWidth * 2;
					float ww = width - lineMarginWidth * 2;
					float hh = moduleHeight - lineMarginWidth * 2;
					StateStreamBundleGroupRenderer statesStreamBundleGroupRender = (StateStreamBundleGroupRenderer) children
							.get(i);
					statesStreamBundleGroupRender.setVisibleAt(xx, yy, ww, hh);
					moduleY += moduleHeight + (lineMarginWidth / 2);
				}
			}
	}

	@Override
	public void setKeyColor(Color keyColor) {
		this.keyColor = keyColor;
		if (children != null)
			for (int i = 0; i < children.size(); i++)
				if (i % 2 == 0)
					children.get(i).setKeyColor(
							new Color(Math.round(keyColor.getBlue()), Math
									.round(keyColor.getGreen()), Math
									.round(keyColor.getRed())));
				else
					children.get(i).setKeyColor(
							new Color(Math.round(keyColor.getRed()), Math
									.round(keyColor.getBlue()), Math
									.round(keyColor.getGreen())));
	}
}
