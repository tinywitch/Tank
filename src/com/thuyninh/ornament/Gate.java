package com.thuyninh.ornament;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Gate
{
	// Cổng thành nếu bị bắn dù chỉ 1 lần cũng sẽ gameover
	private static Toolkit	tk	= Toolkit.getDefaultToolkit();
	private static Image	img	= tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/star.png"));
	private boolean live = true;
	public final int x=475,y=650;

	public void draw(Graphics g)
	{
		g.drawImage(img, x, y, null);
	}

	public Rectangle getRect()
	{
		return new Rectangle(x, y, 50, 50);
	}

	public boolean isLive()
	{
		return live;
	}

	public void setLive(boolean live)
	{
		this.live = live;
	}
}
