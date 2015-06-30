package com.thuyninh.gameevent;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import com.thuyninh.main.TankClient;
import com.thuyninh.ornament.Explode;

public class GameFinish
{
	private static Toolkit	tk	= Toolkit.getDefaultToolkit();
	private static Image tankSelect = tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/tankR_red.gif"));
	
	public void drawWin(Graphics g)
	{
		// Vẽ màn hình lúc player thắng
		g.drawImage(new ImageIcon(Explode.class.getClassLoader()
	    	      .getResource("com/thuyninh/images/Win.png")).getImage(), 0, 0,TankClient.WIDTH,TankClient.HEIGHT, null);
	}

	public void drawGameOver(Graphics g)
	{
		// Vẽ màn hình lúc player thua
		g.drawImage(new ImageIcon(Explode.class.getClassLoader()
	    	      .getResource("com/thuyninh/images/GameOver.png")).getImage(), 0, 0,TankClient.WIDTH,TankClient.HEIGHT, null);
	}
	public void drawTankSelect(Graphics g, int x, int y)
	{
		// Vẽ xe tăng lúc đang chọn 
		g.drawImage(tankSelect, x, y, null);
	}
}
