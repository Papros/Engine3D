package Controller;

import java.awt.image.BufferedImage;

import View.MainViewClass;

public class ViewInput {

	MainViewClass view;
	
	public ViewInput() {
		
	}
	
	public void setView(MainViewClass v) {
		view = v;
	}
	
	public void refresh() {
		view.refresh();
	}
	
	public void display(BufferedImage img) {
		view.display(img);
	}

}
