
package com.thuyninh.tank;

import com.thuyninh.main.TankPanel;

public class EnemyTank extends Tank
{
	// Khởi tạo tank thường của địch, ally = false
	{
		this.setAlly(false);
		this.setBoss(false);
	}

	public EnemyTank(int x, int y)
	{
		super(x, y);
	}

	public EnemyTank(int x, int y, TankPanel tankPanel)
	{
		super(x, y, tankPanel);
	}
}
