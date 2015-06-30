
package com.thuyninh.ornament;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

// đá là vị trí mà xe tăng k thể đi đến và viên đạn do xe tăng bắn ra k thể gây ảnh hưởng đến
public class Stone
{
	private int			x, y, w, h;
	public Stone(int x, int y, int w, int h)
	{
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	private static Toolkit	tk	= Toolkit.getDefaultToolkit();
	private static Image	img	= tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/stone3.png"));

	public void draw(Graphics g)
	{
		g.drawImage(img, x, y, null);
	}

	public Rectangle getRect()
	{
		return new Rectangle(x, y, w, h);
	}

}
