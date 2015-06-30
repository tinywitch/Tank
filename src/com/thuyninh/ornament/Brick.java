
package com.thuyninh.ornament;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

// Class Brick để tạo ra các viên gạch, viên gạch này sẽ biến mất khi có đạn bắn vào
public class Brick
{
	private int			x, y, w, h;
	private boolean live = true;
	public Brick(int x, int y, int w, int h)
	{
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
	}

	private static Toolkit	tk	= Toolkit.getDefaultToolkit();
	private static Image	img	= tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/Brick2.png"));

	public void draw(Graphics g)
	{
		if (!live) return;	
		g.drawImage(img, x, y, null);
	}

	public Rectangle getRect()
	{
		return new Rectangle(x, y, w, h);
	}

	public boolean isLive()
	{
		return live;
	}

	public void setLive(boolean live)
	{
		this.live = live;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

}
