package org.opendrawer.nxtape;

import java.awt.Color;

public class DataStream {
	private int width;
	private float[] data;
	private int writeHead;
	private String name;
	private Color color;
	private float min;
	private float max;
	private boolean resetBounds = true;

	public DataStream(int width, String name, Color color) {
		super();
		this.width = width;
		this.name = name;
		this.color = color;
		data = new float[width];
	}

	public void write(float value) {
		data[writeHead] = value;
		writeHead++;
		if (writeHead >= width)
			writeHead = 0;
		if (!resetBounds) {
			if (value <= min)
				min = value;
			if (value >= max)
				max = value;
		} else {
			min = value;
			max = value;
			resetBounds = false;
		}
	}

	public float read(int pastPosition) {
		if (pastPosition < 0 || pastPosition > width)
			return 0;
		int index = writeHead - (1 + pastPosition);
		while (index < 0)
			index += width;
		return data[index];
	}

	public float readNormalized(int pastPosition) {
		float v = read(pastPosition);
		if (min == max)
			return 0;
		return (v - min) / (max - min);
	}

	public int getWidth() {
		return width;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}
}
