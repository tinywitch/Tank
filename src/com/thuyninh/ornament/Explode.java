
package com.thuyninh.ornament;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/* Class Explode để tạo ra hiệu ứng phát nổ khi xe tăng bị bắn. */
public class Explode
{
	private int				step	= 0;
	private boolean			init	= false;
	private boolean			live	= true; // có/còn nổ hay không
	private int				x, y;
	private boolean au= false;
	
	public boolean isAu()
	{
		return au;
	}

	public void setAu(boolean au)
	{
		this.au = au;
	}

	public Explode(int x, int y)
	{
		super();
		this.x = x;
		this.y = y;
	}

	private static Toolkit	tk		= Toolkit.getDefaultToolkit();
	
	//tạo ra các hình ảnh trạng thái của đợt phát nổ
	private static Image[]	imgs	= {				
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/1.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/2.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/3.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/4.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/5.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/6.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/7.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/8.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/9.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/10.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/12.png")),
			
			};
	
	public void draw(Graphics g)
	{
		if (!init)
		{
			init = true;
			for (int i = 0; i < imgs.length; i++)
			{
				g.drawImage(imgs[i], -100, -100, null);
			}
		}
		if (!live) 
			return;
		
		// nếu số thứ tự bước đã đạt max thì k vẽ hình ảnh vụ nổ nữa
		if (step == imgs.length)
		{
			live = false;
			return;
		}
		// biến step là số thứ tự bước thực hiện của vụ nổ
		// x, y là tọa độ của xe tăng ngay khi bị bắn
		g.drawImage(imgs[step], x, y, null);
		step++;
	}

	public boolean isLive()
	{
		return live;
	}
}
