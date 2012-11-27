package org.opendrawer.nxtape;

import org.opendrawer.dawinian.neurodynamics.DataStreamBundleMap;

import processing.core.PGraphics;

public class DataStreamBundleMapRenderer extends Renderer {

	private DataStreamBundleRenderer inputDataStreamBundleRenderer;
	private DataStreamBundleRenderer outputDataStreamBundleRenderer;

	public DataStreamBundleMapRenderer(DataStreamBundleMap dataStreamBundleMap,
			float x, float y, float width, float height) {
		super();
		if (dataStreamBundleMap != null) {
			inputDataStreamBundleRenderer = new DataStreamBundleRenderer(
					dataStreamBundleMap.getInputDataStreamBundle());
			outputDataStreamBundleRenderer = new DataStreamBundleRenderer(
					dataStreamBundleMap.getOutputDataStreamBundle());
		}
		setVisibleAt(x, y, width, height);

	}

	public DataStreamBundleMapRenderer(DataStreamBundleMap dataStreamBundleMap) {
		super();
		if (dataStreamBundleMap != null) {
			inputDataStreamBundleRenderer = new DataStreamBundleRenderer(
					dataStreamBundleMap.getInputDataStreamBundle());
			outputDataStreamBundleRenderer = new DataStreamBundleRenderer(
					dataStreamBundleMap.getOutputDataStreamBundle());
		}
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);

		g.noStroke();
		g.fill(keyColor.getRed(), keyColor.getGreen(), keyColor.getBlue());
		g.rect(x, y, x + width, y + height);
		g.fill(keyColor.getRed() / 2, keyColor.getGreen() / 2,
				keyColor.getBlue() / 2);
		g.rect(x + NXT_ArtificialPlasticityEcology.lineMarginWidth, y
				+ NXT_ArtificialPlasticityEcology.lineMarginWidth, x + width
				- NXT_ArtificialPlasticityEcology.lineMarginWidth, y + height
				- NXT_ArtificialPlasticityEcology.lineMarginWidth);
		if (inputDataStreamBundleRenderer != null
				|| outputDataStreamBundleRenderer != null) {
			g.stroke(0, 128, 0);
			g.strokeWeight(NXT_ArtificialPlasticityEcology.lineWidth);
			g.line(x + width / 2, y, x + width / 2, y + height);
			if (inputDataStreamBundleRenderer != null)
				inputDataStreamBundleRenderer.draw(g);
			if (outputDataStreamBundleRenderer != null)
				outputDataStreamBundleRenderer.draw(g);
		}
	}

	protected float getStreamLeft() {
		return x;
	}

	@Override
	public void setVisibleAt(float x, float y, float width, float height) {
		super.setVisibleAt(x, y, width, height);
		inputDataStreamBundleRenderer.setVisibleAt(x, y, width / 2, height);
		outputDataStreamBundleRenderer.setVisibleAt(x + width / 2, y,
				width / 2, height);
	}
}