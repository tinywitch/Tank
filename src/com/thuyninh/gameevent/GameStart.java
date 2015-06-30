package com.thuyninh.gameevent;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

import com.thuyninh.main.TankClient;
import com.thuyninh.ornament.Explode;

public class GameStart
{
	private static Toolkit	tk	= Toolkit.getDefaultToolkit();
	private static Image tankSelect = tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/tankR_red.gif"));
	
	public void draw(Graphics g)
	{
		// Vẽ màn hình lúc mới bật game lên
		g.drawImage(new ImageIcon(Explode.class.getClassLoader()
	    	      .getResource("com/thuyninh/images/StartGame.png")).getImage(), 0, 0,TankClient.WIDTH,TankClient.HEIGHT, null);
	}

	public void drawTankSelect(Graphics g, int x, int y)
	{
		// Vẽ xe tăng lúc đang chọn
		g.drawImage(tankSelect, x, y, null);
	}
	
	
}
