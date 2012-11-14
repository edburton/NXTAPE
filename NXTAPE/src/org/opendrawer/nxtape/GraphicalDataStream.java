package org.opendrawer.nxtape;

import java.awt.Color;

import processing.core.PGraphics;

public class GraphicalDataStream extends DataStream implements GraphicalObject {

	public GraphicalDataStream(int width, GraphicalDataProvider dataProvider) {
		super(width, dataProvider);
	}

	private GraphicalDataProvider getGraphicalDataProvider() {
		return (GraphicalDataProvider) dataProvider;
	}

	@Override
	public void drawAt(PGraphics g, float x, float y, float width, float height) {
		getGraphicalDataProvider().drawAt(g, x, y, height, height);
		g.noStroke();
		int nc = dataProvider.getChannels();
		for (int c = nc - 1; c >= 0; c--) {
			float xx = 0, yy = 0;
			int dataWidth = getWidth();
			for (int i = 0; i < dataWidth; i++) {
				float v = read(i, c);
				float x1 = (x + height)
						+ ((i / (float) dataWidth) * (width - height));
				float y1 = (y + height) - v * height;
				if (i > 0) {
					g.fill(Color.HSBtoRGB(c / (float) nc, 1.0f, 1.0f));
					g.rect(Math.min(x1, xx) - 0.5f, Math.min(y1, yy) - 2.5f,
							Math.max(x1, xx) + 0.5f, Math.max(y1, yy) + 2.5f);
				}
				xx = x1;
				yy = y1;
			}
		}
	}
}
