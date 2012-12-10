package org.opendrawer.ape.processing;

import org.opendrawer.ape.darwinianneurodynamics.Ecosystem;

public class EcosystemRenderer extends Renderer {

	private final Ecosystem ecosystem;
	private float zoomToChild;

	public EcosystemRenderer(Ecosystem ecosystem) {
		super(ecosystem);
		this.ecosystem = ecosystem;
		int nTypes = 5;
		int nType = 0;

		for (int i = 0; i < ecosystem.getInputs().size(); i++) {
			HomogeneousStateStreamBundleRenderer renderer = new HomogeneousStateStreamBundleRenderer(
					ecosystem.getInputs().get(i), new Renderer(ecosystem
							.getInputs().get(i).getStateProvider()));
			addChild(renderer);
			renderer.setKeyColor(createKeyColour(nType, nTypes, 1));
		}

		nType++;

		for (int i = 0; i < ecosystem.getOutputs().size(); i++) {
			Renderer renderer = new Renderer(ecosystem.getOutputs().get(i));
			addChild(renderer);
			renderer.setKeyColor(createKeyColour(nType, nTypes, 1));
		}

		nType++;

		for (int i = 0; i < ecosystem.getReflexes().size(); i++) {
			Renderer renderer = new Renderer(ecosystem.getReflexes().get(i));
			addChild(renderer);
			renderer.setKeyColor(createKeyColour(nType, nTypes, 1));
		}

		nType++;

		for (int i = 0; i < ecosystem.getActors().size(); i++) {
			Renderer renderer = new Renderer(ecosystem.getActors().get(i));
			addChild(renderer);
			renderer.setKeyColor(createKeyColour(nType, nTypes, 1));
		}

		nType++;

		for (int i = 0; i < ecosystem.getPredictors().size(); i++) {
			Renderer renderer = new Renderer(ecosystem.getPredictors().get(i));
			addChild(renderer);
			renderer.setKeyColor(createKeyColour(nType, nTypes, 1));
		}
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
