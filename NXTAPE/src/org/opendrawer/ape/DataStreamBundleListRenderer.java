package org.opendrawer.ape;

import java.awt.Color;
import java.util.ArrayList;

import org.opendrawer.dawinian.neurodynamics.DataStreamBundle;
import org.opendrawer.dawinian.neurodynamics.DataStreamBundleList;

import processing.core.PGraphics;

public class DataStreamBundleListRenderer extends Renderer {
	private ArrayList<DataStreamBundleRenderer> dataStreamBundleRenderers = new ArrayList<DataStreamBundleRenderer>();

	public DataStreamBundleListRenderer(
			DataStreamBundleList dataStreamBundleList, float x, float y,
			float width, float height) {
		super();
		if (dataStreamBundleList != null) {
			addDataStreamBundles(dataStreamBundleList.getDataStreamBundles());
		}
		setVisibleAt(x, y, width, height);
	}

	public DataStreamBundleListRenderer(
			DataStreamBundleList dataStreamBundleList) {
		super();
		if (dataStreamBundleList != null) {
			addDataStreamBundles(dataStreamBundleList.getDataStreamBundles());
		}
	}

	public DataStreamBundleListRenderer() {
		super();
	}

	public void addDataStreamBundle(DataStreamBundle dataStreamBundle) {
		dataStreamBundleRenderers.add(new DataStreamBundleRenderer(
				dataStreamBundle));
	}

	public void addDataStreamBundles(
			ArrayList<DataStreamBundle> dataStreamBundles) {
		for (int i = 0; i < dataStreamBundles.size(); i++)
			dataStreamBundleRenderers.add(new DataStreamBundleRenderer(
					dataStreamBundles.get(i)));
		setKeyColor(keyColor);
	}

	@Override
	public void draw(PGraphics g) {
		if (dataStreamBundleRenderers.size() == 0)
			super.draw(g);
		else
			for (int i = 0; i < dataStreamBundleRenderers.size(); i++)
				dataStreamBundleRenderers.get(i).draw(g);
	}

	protected float getStreamLeft() {
		return x;
	}

	@Override
	public void setVisibleAt(float x, float y, float width, float height) {
		super.setVisibleAt(x, y, width, height);
		if (dataStreamBundleRenderers.size() > 0) {
			float moduleWidth = (width / dataStreamBundleRenderers.size());
			float moduleX = x;
			for (int i = 0; i < dataStreamBundleRenderers.size(); i++) {
				DataStreamBundleRenderer dataStreamBundleRender = dataStreamBundleRenderers
						.get(i);
				dataStreamBundleRender.setVisibleAt(moduleX, y, moduleWidth,
						height);
				moduleX += moduleWidth;
			}
		}
	}

	public ArrayList<DataStreamBundleRenderer> getDataStreamBundleRenderers() {
		return dataStreamBundleRenderers;
	}

	@Override
	public void setKeyColor(Color keyColor) {
		super.setKeyColor(keyColor);
		for (int i = 0; i < dataStreamBundleRenderers.size(); i++) {
			dataStreamBundleRenderers.get(i).setKeyColor(keyColor);
		}
	}
}