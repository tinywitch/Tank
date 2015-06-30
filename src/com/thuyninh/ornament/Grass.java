package com.thuyninh.ornament;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

//Grass là vị trí mà tank và đạn có thể đi qua, nhưng tank và đạn đều di chuyển dưới grass
public class Grass
{
	private int			x, y, w, h;
	public Grass(int x, int y, int w, int h)
	{
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	private static Toolkit	tk	= Toolkit.getDefaultToolkit();
	private static Image	img	= tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/Grass2.png"));

	public void draw(Graphics g)
	{
		g.drawImage(img, x, y, null);
	}

	public Rectangle getRect()
	{
		return new Rectangle(x, y, w, h);
	}
}
