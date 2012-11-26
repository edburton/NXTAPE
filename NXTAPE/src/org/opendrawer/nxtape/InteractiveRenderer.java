package org.opendrawer.nxtape;

public interface InteractiveRenderer {

	public void mouseClicked(int mouseX, int mouseY);

	public void mousePressed(int mouseX, int mouseY);

	public void mouseDragged(int mouseX, int mouseY);

	public void mouseReleased(int mouseX, int mouseY);
}
