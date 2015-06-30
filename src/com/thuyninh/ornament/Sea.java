package com.thuyninh.ornament;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

// Biển là vị trí mà đạn có thể đi qua nhưng xe tăng k thể đi qua
public class Sea
{
	private int			x, y, w, h;
	public Sea(int x, int y, int w, int h)
	{
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	private static Toolkit	tk	= Toolkit.getDefaultToolkit();
	private static Image	img	= tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/Sea3.png"));

	public void draw(Graphics g)
	{
		g.drawImage(img, x, y, null);
	}

	public Rectangle getRect()
	{
		return new Rectangle(x, y, w, h);
	}
}
