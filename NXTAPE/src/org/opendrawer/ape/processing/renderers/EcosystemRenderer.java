package org.opendrawer.ape.processing.renderers;

import org.opendrawer.ape.darwinianneurodynamics.Ecosystem;

public class EcosystemRenderer extends Renderer {

	private final Ecosystem ecosystem;
	private float zoomToChild;

	public EcosystemRenderer(Ecosystem ecosystem) {
		this.ecosystem = ecosystem;
		int nTypes = 5;
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
		for (int i = 0; i < ecosystem.getCuriosityLoops().size(); i++) {
			Renderer renderer = Renderer.makeRendererFor(ecosystem
					.getCuriosityLoops().get(i));
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
		int gridWidth = 0;
		int gridHeight = 0;
		int best = Integer.MAX_VALUE;
		for (int ww = 0; ww < zones; ww++) {
			for (int hh = ww + 1; hh <= ww * 4; hh++) {
				if (hh * ww >= zones && (hh * ww) - zones <= best) {
					best = (hh * ww) - zones;
					gridWidth = ww;
					gridHeight = hh;
				}
			}
		}
		float w = ((width + margin - margin * 2) / gridWidth) - margin;
		float h = ((height + margin - margin * 2) / gridHeight) - margin;
		int c = 0;
		for (int gx = 0; gx < gridWidth; gx++) {
			for (int gy = 0; gy < gridHeight; gy++) {
				float x1 = x + margin + gx * (w + margin);
				float y1 = y + margin + gy * (h + margin);
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
