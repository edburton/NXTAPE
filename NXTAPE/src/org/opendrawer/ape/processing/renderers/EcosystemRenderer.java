package org.opendrawer.ape.processing.renderers;

import org.opendrawer.ape.darwinianneurodynamics.Ecosystem;

public class EcosystemRenderer extends Renderer {

	private final Ecosystem ecosystem;
	private float zoomToChild;

	public EcosystemRenderer(Ecosystem ecosystem) {
		this.ecosystem = ecosystem;
		int nTypes = 6;
		int nType = 0;

		for (int i = 0; i < ecosystem.getInputs().size(); i++) {
			Renderer renderer = Renderer.makeRendererFor(ecosystem.getInputs()
					.get(i));
			addChild(renderer);
			renderer.setKeyColor(createKeyColour(nType, nTypes));
		}

		nType++;

		for (int i = 0; i < ecosystem.getOutputs().size(); i++) {
			Renderer renderer = Renderer.makeRendererFor(ecosystem.getOutputs()
					.get(i));
			addChild(renderer);
			renderer.setKeyColor(createKeyColour(nType, nTypes));
		}

		nType++;

		for (int i = 0; i < ecosystem.getReflexes().size(); i++) {
			Renderer renderer = Renderer.makeRendererFor(ecosystem
					.getReflexes().get(i));
			addChild(renderer);
			renderer.setKeyColor(createKeyColour(nType, nTypes));
		}

		nType++;

		for (int i = 0; i < ecosystem.getActors().size(); i++) {
			Renderer renderer = Renderer.makeRendererFor(ecosystem.getActors()
					.get(i));
			addChild(renderer);
			renderer.setKeyColor(createKeyColour(nType, nTypes));
		}

		nType++;

		for (int i = 0; i < ecosystem.getPredictors().size(); i++) {
			Renderer renderer = Renderer.makeRendererFor(ecosystem
					.getPredictors().get(i));
			addChild(renderer);
			renderer.setKeyColor(createKeyColour(nType, nTypes));
		}

		nType++;

		Renderer renderer = Renderer.makeRendererFor(ecosystem.getAllErrors());
		addChild(renderer);
		renderer.setKeyColor(createKeyColour(nType, nTypes));
	}

	@Override
	public void setVisibleAt(float x, float y, float width, float height) {
		super.setVisibleAt(x, y, width, height);

		int zones = children.size();

		float margin = Renderer.lineMarginWidth * 4;
		int gridWidth = (int) Math.ceil(Math.pow(zones, 1 / 3.0d));
		int gridHeight = (int) Math.floor(Math.pow(zones, 2 / 3.0d));

		float w = ((width + margin - margin * 2) / gridWidth) - margin;
		float h = ((height + margin - margin * 2) / gridHeight) - margin;

		int c = 0;
		for (int gx = 0; gx < gridWidth; gx++) {
			for (int gy = 0; gy < gridHeight; gy++) {
				float x1 = margin + gx * (w + margin);
				float y1 = margin + gy * (h + margin);
				if (c < children.size()) {
					children.get(c).setVisibleAt(x1, y1, w, h);
				}
				c++;
			}
		}
		zoomToChild = (width / (w + margin));
	}

	public static Renderer rendererFactory(Object object) {
		return new EcosystemRenderer((Ecosystem) object);
	}

	public Ecosystem getEcosystem() {
		return ecosystem;
	}

	public float getZoomToChild() {
		return zoomToChild;
	}
}
