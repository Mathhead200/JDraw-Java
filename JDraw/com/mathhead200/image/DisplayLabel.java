package com.mathhead200.image;

import javax.swing.*;

import javax.swing.border.LineBorder;
import java.awt.Color;


public class DisplayLabel extends JLabel {
	private static final long serialVersionUID = -6677031760614269001L;

	private EquationImage image;

	public DisplayLabel(int w, int h) {
		super();
		image = new EquationImage(w, h);
		setSize(w, h);
		setIcon( new ImageIcon(image) );
		setBorder( new LineBorder(new Color(0x000000), 1, false) );
	}

	public DisplayLabel(EquationImage img) {
		super();
		setImage(img);
	}

	public void setImage(EquationImage img) {
		image = img;
		setSize( image.getWidth(), image.getHeight() );
		setIcon( new ImageIcon(image) );
	}

	public EquationImage getImage() {
		return image;
	}

	public void redraw() {
		setImage(image);
		validate();
	}
}
