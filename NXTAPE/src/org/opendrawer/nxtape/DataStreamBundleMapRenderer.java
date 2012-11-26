package org.opendrawer.nxtape;

import org.opendrawer.dawinian.neurodynamics.DataStreamBundleMap;

import processing.core.PGraphics;

public class DataStreamBundleMapRenderer extends Renderer {

	private DataStreamBundleRenderer inputDataStreamBundleRenderer;
	private DataStreamBundleRenderer outputDataStreamBundleRenderer;

	public DataStreamBundleMapRenderer(DataStreamBundleMap dataStreamBundleMap,
			float x, float y, float width, float height) {
		super(x, y, width, height);
		inputDataStreamBundleRenderer = new DataStreamBundleRenderer(
				dataStreamBundleMap.getInputDataStreamBundle(), x, y,
				width / 2, height);
		outputDataStreamBundleRenderer = new DataStreamBundleRenderer(
				dataStreamBundleMap.getOutputDataStreamBundle(), x + width / 2,
				y, width / 2, height);
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);

		g.noStroke();
		g.fill(0, 128, 0);
		g.rect(x, y, x + width, y + height);
		g.fill(0, 32, 0);
		g.rect(x + NXT_ArtificialPlasticityEcology.lineMarginWidth, y
				+ NXT_ArtificialPlasticityEcology.lineMarginWidth, x + width
				- NXT_ArtificialPlasticityEcology.lineMarginWidth, y + height
				- NXT_ArtificialPlasticityEcology.lineMarginWidth);

		g.stroke(0, 128, 0);
		g.strokeWeight(NXT_ArtificialPlasticityEcology.lineWidth);
		g.line(x + width / 2, y, x + width / 2, y + height);

		inputDataStreamBundleRenderer.draw(g);
		outputDataStreamBundleRenderer.draw(g);
	}

	protected float getStreamLeft() {
		return x;
	}

	@Override
	public void setPosition(float x, float y, float width, float height) {
		super.setPosition(x, y, width, height);
		inputDataStreamBundleRenderer.setPosition(x, y, width / 2, height);
		outputDataStreamBundleRenderer.setPosition(x + width / 2, y, width / 2,
				height);
	}
}