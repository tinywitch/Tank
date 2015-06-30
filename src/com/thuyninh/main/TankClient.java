
package com.thuyninh.main;

import javax.swing.JFrame;

public class TankClient extends JFrame
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public final static int	HEIGHT	= 730;
	public final static int	WIDTH	= 1000;
	public TankPanel tp = new TankPanel();
	public static void main(String[] args)
	{
		new TankClient();
	}

	public TankClient()
	{
		setTitle("World of Tanks");
		setLocation(100,0);
		setSize(WIDTH, HEIGHT);
		
		getContentPane().add(tp);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}

	
}
