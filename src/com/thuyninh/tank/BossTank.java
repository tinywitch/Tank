package com.thuyninh.tank;

import com.thuyninh.main.TankPanel;

public class BossTank extends Tank
{
	// Khởi tạo tank boss của địch, ally = false, boss = true
		{
			this.setAlly(false);
			this.setBoss(true);
		}

		public BossTank(int x, int y)
		{
			super(x, y);
		}

		public BossTank(int x, int y, TankPanel tankPanel)
		{
			super(x, y, tankPanel);
		}
}
