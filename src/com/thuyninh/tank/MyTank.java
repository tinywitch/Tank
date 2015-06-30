
package com.thuyninh.tank;

import java.awt.event.KeyEvent;

import com.thuyninh.main.TankPanel;
import com.thuyninh.tank.Tank;

public class MyTank extends Tank
{
	// Khởi tạo tank của player với ally = true
	public MyTank(int x, int y)
	{
		super(x, y);
		
	}

	public MyTank(int x, int y, boolean ally, TankPanel tankPanel)
	{
		super(x, y, tankPanel);
	}

	{
		this.setAlly(true);
	}

	// Set các nút điều hướng cho xe tăng của player
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		switch (key)
		{
			case KeyEvent.VK_LEFT:
				bleft = true;
				break;
			case KeyEvent.VK_RIGHT:
				bright = true;
				break;
			case KeyEvent.VK_UP:
				bup = true;
				break;
			case KeyEvent.VK_DOWN:
				bdown = true;
				break;
		}
		locateDirection(); // xác định hướng đi của xe tăng từ việc thu thập thông tin từ các keyevent của player
	}

	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		switch (key)
		{
			case KeyEvent.VK_CONTROL: // mỗi 1 lần ấn nút ctrl thì chỉ bắn ra được 1 viên đạn 
				fire();				// k thể giữ lâu nút ctrl nếu muốn bắn đạn ngay
				break;
				
			// Nếu không giữ/ấn các nút điều hướng thì xe tăng sẽ dừng lại
			case KeyEvent.VK_LEFT: 
				bleft = false;
				break;
			case KeyEvent.VK_RIGHT:
				bright = false;
				break;
			case KeyEvent.VK_UP:
				bup = false;
				break;
			case KeyEvent.VK_DOWN:
				bdown = false;
				break;
		}
		locateDirection();
	}

}
