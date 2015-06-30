
package com.thuyninh.main;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

import javax.swing.JPanel;

import com.thuyninh.audioplay.enterGame;
import com.thuyninh.audioplay.explode;
import com.thuyninh.audioplay.shoot;
import com.thuyninh.audioplay.startTank;
import com.thuyninh.bullet.*;
import com.thuyninh.gameevent.GameFinish;
import com.thuyninh.gameevent.GameStart;
import com.thuyninh.ornament.*;
import com.thuyninh.tank.*;

public class TankPanel extends JPanel 
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public ArrayList<Tank>		tanks			= new ArrayList<Tank>();
	public ArrayList<Bullet>	bulletArrayList = new ArrayList<Bullet>();
	public ArrayList<Stone>		stones			= new ArrayList<Stone>();
	public ArrayList<Brick>		bricks			= new ArrayList<Brick>();
	public ArrayList<Sea>		sea				= new ArrayList<Sea>();
	public ArrayList<Grass>		grass			= new ArrayList<Grass>();
	public MyTank				myTank			= new MyTank(350, 640, true, this);
	public Gate					gate			= new Gate();
	public GameStart			gamestart		= new GameStart();
	public GameFinish 			gamefinish			= new GameFinish();
	public ArrayList<Explode>	explodes		= new ArrayList<Explode>();
	public int					initTankCount	= 3;
	public boolean				gameStart		= false;
	public boolean				gameFinish		= false;
	public boolean				play			= true;
	public int					tankWave		= 7;
	public boolean played = false;
	public int timetoStartTank =1;
	public boolean allofTanksBeingOut = true;
	public TankPanel()
	{
		setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new KeyMonitor());
		setOrnament();	
		tanks.add(myTank); // khởi tạo xe tăng của player	
		setEnemyTanks(); 	// Khởi tạo xe tăng của máy tính điều khiển
	}

	public void setNewGame()
	{
		tanks			= new ArrayList<Tank>();
		bulletArrayList	= new ArrayList<Bullet>();
		stones			= new ArrayList<Stone>();
		bricks			= new ArrayList<Brick>();
		sea				= new ArrayList<Sea>();
		grass			= new ArrayList<Grass>();
		myTank			= new MyTank(350, 640, true, this);
		gate			= new Gate();
		gamestart		= new GameStart();
		gamefinish			= new GameFinish();
		explodes		= new ArrayList<Explode>();
		tankWave		= 7;
		timetoStartTank =1;
		setOrnament();
		tanks.add(myTank);
		setEnemyTanks();
	}

	protected void gameStarting(Graphics g)
	{
		gamestart.draw(g);
		if (play)
			gamestart.drawTankSelect(g, 280, 360);
		else
			gamestart.drawTankSelect(g, 280, 440);
	}

	protected void gamePlaying(Graphics g)
	{
		
		if (timetoStartTank <120) 
		{
			g.setColor(Color.red);
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.drawString("Press Ctrl key to shoot straight", 350, 550);
		}
		if (tanks.size() <=3 && myTank.isLive() && tankWave == 1)
		{
			new startTank();
			setBossTanks();
			tankWave--;
		}

		if (myTank.isLive())
		{
			g.setColor(Color.red);
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.drawString("HP: " + myTank.getLife(), 20, 30);
			g.drawString("Tank waves remaining: " + tankWave, 20, 70);
		}
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if (gameStart == false && gameFinish == false)
			gameStarting(g);
		
		if (played == true )
		{
			if (gameStart == true && gameFinish == false)
			{
				paintTank(g);
				paintBullet(g);	
				gamePlaying(g);
			}
			paintGate(g);
			paintSea(g);
			paintStone(g);
			paintGrass(g);
			paintBrick(g);
			paintExplode(g);
		}
		if (tanks.size() == 1 && myTank.isLive() && tankWave == 0)
		{
			gameStart = false;
			gameFinish = true;
			gamefinish.drawWin(g);
			
			if (play)
				gamefinish.drawTankSelect(g, 280, 440);
			else
				gamefinish.drawTankSelect(g, 280, 520);
		}
		else if (!myTank.isLive() || !gate.isLive())
		{
			gameStart = false;
			gameFinish = true;
			if (!gate.isLive())
				gate.setLive(false);
			gamefinish.drawGameOver(g);
			
			if (play)
				gamefinish.drawTankSelect(g, 280, 440);
			else
				gamefinish.drawTankSelect(g, 280, 520);
		}
	}

	private class PaintThread implements Runnable
	{
		public void run()
		{
				while (gameStart == true && gameFinish == false)
				{
					repaint();
					timetoStartTank++;
					/* Điều kiện để xuất hiện lượt tank mới:
					 * ** Xe tăng của người chơi còn sống 
					 * ** Số lượt tank còn lại >1 
					 * ** cứ 30s 1 lần thêm tank mới hoặc số lượng tank địch chỉ còn 1
					 * ** Tất cả các tank đều không còn ở vị trí cũ nữa (tránh trùng nhau dẫn đến 2 tank cùng k thể di chuyển được
					 *  */
					if (myTank.isLive() && tankWave > 1 && (timetoStartTank % 600 ==0 || tanks.size() <= 2) && allofTanksBeingOut)
					{
						new startTank();
						setEnemyTanks();
						tankWave--;
					}
					try
					{
						Thread.sleep(50);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
		}
	}

	private class KeyMonitor extends KeyAdapter
	{
		public void keyPressed(KeyEvent e)
		{
			if (gameStart == false || gameFinish == true)
			{
				if (e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					if (play)
						play = false;
					repaint();
				}

				if (e.getKeyCode() == KeyEvent.VK_UP)
				{
					if (!play)
						play = true;
					repaint();
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if (!play)
						System.exit(0);
					else
					{
						gameStart = true;
						gameFinish = false;
						played = true;
						new enterGame();
						setNewGame();
						repaint();
						new Thread( new PaintThread()).start();
					}	
				}
			}
			else
				myTank.keyPressed(e);
		}

		public void keyReleased(KeyEvent e)
		{
			myTank.keyReleased(e);
		}
	}
	
	protected void paintTank(Graphics g)
	{
		Iterator<Tank> tita = tanks.iterator();
		while (tita.hasNext())
		{
			Tank t = tita.next();
			if (!t.isLive()) // nếu xe tăng bị chết thì xóa khỏi mảng
				tita.remove();
			else
			{
				t.collidesWithTank(tanks); // Ngăn sự đi lại lên nhau của các xe tăng											
				t.collidesWithStone(stones); // Ngăn không cho đi lên đá
				t.collidesWithBrick(bricks); // Ngăn không cho đi lên gạch
				t.collidesWithGate(gate);
				t.collidesWithSea(sea); // Ngăn k cho đi lên biển
				t.returnHome();
				t.draw(g);
			}
		}
		allofTanksBeingOut =true;
		for (int i =0; i<tanks.size();i++)
		{
			if (tanks.get(i).isOutOfHome() == false)
			{
				allofTanksBeingOut = false;
				break;
			}
				
		}
	}

	protected void paintBullet(Graphics g)
	{
		Iterator<Bullet> bita = bulletArrayList.iterator();
		while (bita.hasNext())
		{
			Bullet b = bita.next();

			if (b.isLive())
			{
				b.hitTank(tanks);
				b.hitTank(myTank);
				b.hitStone(stones);
				b.hitBrick(bricks);
				b.hitGate(gate);
				b.draw(g);
				if (b.isAu() == false) 
				{
					new shoot(); // tạo ra âm thanh khi bắn
					b.setAu(true); // âm thanh bắn chỉ đc play 1 lần/1 lần bắn
				}
			}
			else
				bita.remove();
		}
	}

	protected void paintBrick(Graphics g)
	{
		Iterator<Brick> br = bricks.iterator();
		while (br.hasNext())
		{
			Brick b = br.next();
			if (!b.isLive())
				br.remove();
			else
				b.draw(g);
		}
	}

	protected void paintExplode(Graphics g)
	{
		Iterator<Explode> eita = explodes.iterator();
		while (eita.hasNext())
		{
			Explode e = eita.next();
			if (e.isAu()== false)
			{
				new explode(); // tạo âm thanh tiếng nổ
				e.setAu(true);
			}
			if (!e.isLive())
				eita.remove();
			else
				e.draw(g);
		}
	}

	protected void paintSea(Graphics g)
	{
		for (Sea e : sea)
		{
			e.draw(g);
		}
	}

	protected void paintStone(Graphics g)
	{
		for (Stone e : stones)
		{
			e.draw(g);
		}
	}

	protected void paintGrass(Graphics g)
	{
		for (Grass e : grass)
		{
			e.draw(g);
		}
	}

	protected void paintGate(Graphics g)
	{
		if (gate.isLive())
			gate.draw(g);
	}

	
	public void setEnemyTanks()
	{
		// khởi tạo xe tăng thường do máy tính điều khiển
		tanks.add(new EnemyTank(10, 10, this));
		tanks.add(new EnemyTank(475, 10, this));
		tanks.add(new EnemyTank(920, 10, this));
	}

	public void setBossTanks()
	{
		// khởi tạo tank boss
		tanks.add(new BossTank(10, 10, this));
		tanks.add(new BossTank(475, 10, this));
		tanks.add(new BossTank(920, 10, this));
	}

	public void setOrnament()
	{
		// Đá trung tâm
		stones.add(new Stone(475, 260, 50, 50 ));

		// gạch chữ thập trung tâm
		bricks.add(new Brick(475, 60, 25, 25 ));
		bricks.add(new Brick(475, 110, 25, 25 ));
		bricks.add(new Brick(475, 160, 25, 25 ));
		bricks.add(new Brick(475, 210, 25, 25 ));

		bricks.add(new Brick(475, 310, 25, 25 ));
		bricks.add(new Brick(475, 360, 25, 25 ));
		bricks.add(new Brick(475, 410, 25, 25 ));
		bricks.add(new Brick(475, 460, 25, 25 ));

		bricks.add(new Brick(475, 85, 25, 25 ));
		bricks.add(new Brick(475, 135, 25, 25 ));
		bricks.add(new Brick(475, 185, 25, 25 ));
		bricks.add(new Brick(475, 235, 25, 25 ));

		bricks.add(new Brick(475, 335, 25, 25 ));
		bricks.add(new Brick(475, 385, 25, 25 ));
		bricks.add(new Brick(475, 435, 25, 25 ));
		bricks.add(new Brick(475, 485, 25, 25 ));

		bricks.add(new Brick(500, 60, 25, 25 ));
		bricks.add(new Brick(500, 110, 25, 25 ));
		bricks.add(new Brick(500, 160, 25, 25 ));
		bricks.add(new Brick(500, 210, 25, 25 ));

		bricks.add(new Brick(500, 310, 25, 25 ));
		bricks.add(new Brick(500, 360, 25, 25 ));
		bricks.add(new Brick(500, 410, 25, 25 ));
		bricks.add(new Brick(500, 460, 25, 25 ));

		bricks.add(new Brick(500, 85, 25, 25 ));
		bricks.add(new Brick(500, 135, 25, 25 ));
		bricks.add(new Brick(500, 185, 25, 25 ));
		bricks.add(new Brick(500, 235, 25, 25 ));

		bricks.add(new Brick(500, 335, 25, 25 ));
		bricks.add(new Brick(500, 385, 25, 25 ));
		bricks.add(new Brick(500, 435, 25, 25 ));
		bricks.add(new Brick(500, 485, 25, 25 ));

		bricks.add(new Brick(275, 460, 25, 25 ));
		bricks.add(new Brick(325, 460, 25, 25 ));
		bricks.add(new Brick(375, 460, 25, 25 ));
		bricks.add(new Brick(425, 460, 25, 25 ));
		bricks.add(new Brick(300, 460, 25, 25 ));
		bricks.add(new Brick(350, 460, 25, 25 ));
		bricks.add(new Brick(400, 460, 25, 25 ));
		bricks.add(new Brick(450, 460, 25, 25 ));
		bricks.add(new Brick(275, 485, 25, 25 ));
		bricks.add(new Brick(325, 485, 25, 25 ));
		bricks.add(new Brick(375, 485, 25, 25 ));
		bricks.add(new Brick(425, 485, 25, 25 ));
		bricks.add(new Brick(300, 485, 25, 25 ));
		bricks.add(new Brick(350, 485, 25, 25 ));
		bricks.add(new Brick(400, 485, 25, 25 ));
		bricks.add(new Brick(450, 485, 25, 25 ));

		bricks.add(new Brick(525, 260, 25, 25 ));
		bricks.add(new Brick(575, 260, 25, 25 ));
		bricks.add(new Brick(625, 260, 25, 25 ));
		bricks.add(new Brick(675, 260, 25, 25 ));
		bricks.add(new Brick(550, 260, 25, 25 ));
		bricks.add(new Brick(600, 260, 25, 25 ));
		bricks.add(new Brick(650, 260, 25, 25 ));
		bricks.add(new Brick(700, 260, 25, 25 ));
		bricks.add(new Brick(525, 285, 25, 25 ));
		bricks.add(new Brick(575, 285, 25, 25 ));
		bricks.add(new Brick(625, 285, 25, 25 ));
		bricks.add(new Brick(675, 285, 25, 25 ));
		bricks.add(new Brick(550, 285, 25, 25 ));
		bricks.add(new Brick(600, 285, 25, 25 ));
		bricks.add(new Brick(650, 285, 25, 25 ));
		bricks.add(new Brick(700, 285, 25, 25 ));

		// Dãy gạch phía trên bên trái
		bricks.add(new Brick(275, 60, 25, 25 ));
		bricks.add(new Brick(275, 110, 25, 25 ));
		bricks.add(new Brick(275, 160, 25, 25 ));
		bricks.add(new Brick(275, 210, 25, 25 ));
		bricks.add(new Brick(275, 85, 25, 25 ));
		bricks.add(new Brick(275, 135, 25, 25 ));
		bricks.add(new Brick(275, 185, 25, 25 ));
		bricks.add(new Brick(275, 235, 25, 25 ));
		bricks.add(new Brick(300, 60, 25, 25 ));
		bricks.add(new Brick(300, 110, 25, 25 ));
		bricks.add(new Brick(300, 160, 25, 25 ));
		bricks.add(new Brick(300, 210, 25, 25 ));
		bricks.add(new Brick(300, 85, 25, 25 ));
		bricks.add(new Brick(300, 135, 25, 25 ));
		bricks.add(new Brick(300, 185, 25, 25 ));
		bricks.add(new Brick(300, 235, 25, 25 ));

		// Dãy gạch phía trên bên phải
		bricks.add(new Brick(525, 60, 25, 25 ));
		bricks.add(new Brick(575, 60, 25, 25 ));
		bricks.add(new Brick(625, 60, 25, 25 ));
		bricks.add(new Brick(675, 60, 25, 25 ));
		bricks.add(new Brick(550, 60, 25, 25 ));
		bricks.add(new Brick(600, 60, 25, 25 ));
		bricks.add(new Brick(650, 60, 25, 25 ));
		bricks.add(new Brick(700, 60, 25, 25 ));
		bricks.add(new Brick(525, 85, 25, 25 ));
		bricks.add(new Brick(575, 85, 25, 25 ));
		bricks.add(new Brick(625, 85, 25, 25 ));
		bricks.add(new Brick(675, 85, 25, 25 ));
		bricks.add(new Brick(550, 85, 25, 25 ));
		bricks.add(new Brick(600, 85, 25, 25 ));
		bricks.add(new Brick(650, 85, 25, 25 ));
		bricks.add(new Brick(700, 85, 25, 25 ));

		// Dãy gạch phía dưới bên trái
		bricks.add(new Brick(275, 260, 25, 25 ));
		bricks.add(new Brick(325, 260, 25, 25 ));
		bricks.add(new Brick(375, 260, 25, 25 ));
		bricks.add(new Brick(425, 260, 25, 25 ));
		bricks.add(new Brick(300, 260, 25, 25 ));
		bricks.add(new Brick(350, 260, 25, 25 ));
		bricks.add(new Brick(400, 260, 25, 25 ));
		bricks.add(new Brick(450, 260, 25, 25 ));
		bricks.add(new Brick(275, 285, 25, 25 ));
		bricks.add(new Brick(325, 285, 25, 25 ));
		bricks.add(new Brick(375, 285, 25, 25 ));
		bricks.add(new Brick(425, 285, 25, 25 ));
		bricks.add(new Brick(300, 285, 25, 25 ));
		bricks.add(new Brick(350, 285, 25, 25 ));
		bricks.add(new Brick(400, 285, 25, 25 ));
		bricks.add(new Brick(450, 285, 25, 25 ));

		// Dãy gạch phía dưới bên phải
		bricks.add(new Brick(675, 310, 25, 25 ));
		bricks.add(new Brick(675, 360, 25, 25 ));
		bricks.add(new Brick(675, 410, 25, 25 ));
		bricks.add(new Brick(675, 460, 25, 25 ));
		bricks.add(new Brick(675, 335, 25, 25 ));
		bricks.add(new Brick(675, 385, 25, 25 ));
		bricks.add(new Brick(675, 435, 25, 25 ));
		bricks.add(new Brick(675, 485, 25, 25 ));
		bricks.add(new Brick(700, 310, 25, 25 ));
		bricks.add(new Brick(700, 360, 25, 25 ));
		bricks.add(new Brick(700, 410, 25, 25 ));
		bricks.add(new Brick(700, 460, 25, 25 ));
		bricks.add(new Brick(700, 335, 25, 25 ));
		bricks.add(new Brick(700, 385, 25, 25 ));
		bricks.add(new Brick(700, 435, 25, 25 ));
		bricks.add(new Brick(700, 485, 25, 25 ));

		// gạch bảo vệ thành
		bricks.add(new Brick(425, 650, 25, 25 ));
		bricks.add(new Brick(425, 600, 25, 25 ));
		bricks.add(new Brick(475, 600, 25, 25 ));
		bricks.add(new Brick(525, 600, 25, 25 ));
		bricks.add(new Brick(525, 650, 25, 25 ));
		bricks.add(new Brick(450, 650, 25, 25 ));
		bricks.add(new Brick(450, 600, 25, 25 ));
		bricks.add(new Brick(500, 600, 25, 25 ));
		bricks.add(new Brick(550, 600, 25, 25 ));
		bricks.add(new Brick(550, 650, 25, 25 ));
		bricks.add(new Brick(425, 675, 25, 25 ));
		bricks.add(new Brick(425, 625, 25, 25 ));
		bricks.add(new Brick(475, 625, 25, 25 ));
		bricks.add(new Brick(525, 625, 25, 25 ));
		bricks.add(new Brick(525, 675, 25, 25 ));
		bricks.add(new Brick(450, 675, 25, 25 ));
		bricks.add(new Brick(450, 625, 25, 25 ));
		bricks.add(new Brick(500, 625, 25, 25 ));
		bricks.add(new Brick(550, 625, 25, 25 ));
		bricks.add(new Brick(550, 675, 25, 25 ));

		// Đá bên trái - giữa
		stones.add(new Stone(0, 460, 50, 50 ));

		// Biển bên trái - giữa
		sea.add(new Sea(50, 460, 50, 50 ));
		sea.add(new Sea(100, 460, 50, 50 ));
		sea.add(new Sea(150, 460, 50, 50 ));
		sea.add(new Sea(200, 460, 50, 50 ));

		// Đá bên phải - giữa
		stones.add(new Stone(945, 460, 50, 50 ));

		// Biển bên phải - giữa
		sea.add(new Sea(895, 460, 50, 50 ));
		sea.add(new Sea(845, 460, 50, 50 ));
		sea.add(new Sea(795, 460, 50, 50 ));
		sea.add(new Sea(745, 460, 50, 50 ));

		// gạch chữ thập bên trái
		bricks.add(new Brick(120, 110, 25, 25 ));
		bricks.add(new Brick(120, 160, 25, 25 ));
		bricks.add(new Brick(145, 110, 25, 25 ));
		bricks.add(new Brick(145, 160, 25, 25 ));

		bricks.add(new Brick(120, 135, 25, 25 ));
		bricks.add(new Brick(120, 185, 25, 25 ));
		bricks.add(new Brick(145, 135, 25, 25 ));
		bricks.add(new Brick(145, 185, 25, 25 ));

		stones.add(new Stone(120, 210, 50, 50 ));

		bricks.add(new Brick(120, 260, 25, 25 ));
		bricks.add(new Brick(120, 285, 25, 25 ));
		bricks.add(new Brick(145, 260, 25, 25 ));
		bricks.add(new Brick(145, 285, 25, 25 ));

		bricks.add(new Brick(120, 310, 25, 25 ));
		bricks.add(new Brick(120, 335, 25, 25 ));
		bricks.add(new Brick(145, 310, 25, 25 ));
		bricks.add(new Brick(145, 335, 25, 25 ));

		bricks.add(new Brick(20, 210, 25, 25 ));
		bricks.add(new Brick(45, 210, 25, 25 ));
		bricks.add(new Brick(70, 210, 25, 25 ));
		bricks.add(new Brick(95, 210, 25, 25 ));

		bricks.add(new Brick(170, 210, 25, 25 ));
		bricks.add(new Brick(195, 210, 25, 25 ));
		bricks.add(new Brick(220, 210, 25, 25 ));
		bricks.add(new Brick(245, 210, 25, 25 ));

		bricks.add(new Brick(20, 235, 25, 25 ));
		bricks.add(new Brick(45, 235, 25, 25 ));
		bricks.add(new Brick(70, 235, 25, 25 ));
		bricks.add(new Brick(95, 235, 25, 25 ));

		bricks.add(new Brick(170, 235, 25, 25 ));
		bricks.add(new Brick(195, 235, 25, 25 ));
		bricks.add(new Brick(220, 235, 25, 25 ));
		bricks.add(new Brick(245, 235, 25, 25 ));

		// gạch chữ thập bên phải
		bricks.add(new Brick(835, 110, 25, 25 ));
		bricks.add(new Brick(835, 160, 25, 25 ));
		bricks.add(new Brick(860, 110, 25, 25 ));
		bricks.add(new Brick(860, 160, 25, 25 ));

		bricks.add(new Brick(835, 135, 25, 25 ));
		bricks.add(new Brick(835, 185, 25, 25 ));
		bricks.add(new Brick(860, 135, 25, 25 ));
		bricks.add(new Brick(860, 185, 25, 25 ));

		stones.add(new Stone(835, 210, 50, 50 ));

		bricks.add(new Brick(835, 260, 25, 25 ));
		bricks.add(new Brick(835, 285, 25, 25 ));
		bricks.add(new Brick(860, 260, 25, 25 ));
		bricks.add(new Brick(860, 285, 25, 25 ));

		bricks.add(new Brick(835, 310, 25, 25 ));
		bricks.add(new Brick(835, 335, 25, 25 ));
		bricks.add(new Brick(860, 310, 25, 25 ));
		bricks.add(new Brick(860, 335, 25, 25 ));

		bricks.add(new Brick(735, 210, 25, 25 ));
		bricks.add(new Brick(760, 210, 25, 25 ));
		bricks.add(new Brick(785, 210, 25, 25 ));
		bricks.add(new Brick(810, 210, 25, 25 ));

		bricks.add(new Brick(885, 210, 25, 25 ));
		bricks.add(new Brick(910, 210, 25, 25 ));
		bricks.add(new Brick(935, 210, 25, 25 ));
		bricks.add(new Brick(960, 210, 25, 25 ));

		bricks.add(new Brick(735, 235, 25, 25 ));
		bricks.add(new Brick(760, 235, 25, 25 ));
		bricks.add(new Brick(785, 235, 25, 25 ));
		bricks.add(new Brick(810, 235, 25, 25 ));

		bricks.add(new Brick(885, 235, 25, 25 ));
		bricks.add(new Brick(910, 235, 25, 25 ));
		bricks.add(new Brick(935, 235, 25, 25 ));
		bricks.add(new Brick(960, 235, 25, 25 ));

		// Cỏ trên bên trái
		grass.add(new Grass(100, 0, 50, 50 ));
		grass.add(new Grass(150, 0, 50, 50 ));
		grass.add(new Grass(200, 0, 50, 50 ));
		grass.add(new Grass(250, 0, 50, 50 ));
		grass.add(new Grass(300, 0, 50, 50 ));
		grass.add(new Grass(350, 0, 50, 50 ));
		grass.add(new Grass(400, 0, 50, 50 ));

		// Cỏ trên bên phải
		grass.add(new Grass(550, 0, 50, 50 ));
		grass.add(new Grass(600, 0, 50, 50 ));
		grass.add(new Grass(650, 0, 50, 50 ));
		grass.add(new Grass(700, 0, 50, 50 ));
		grass.add(new Grass(750, 0, 50, 50 ));
		grass.add(new Grass(800, 0, 50, 50 ));
		grass.add(new Grass(850, 0, 50, 50 ));

		// Cỏ dưới bên trái
		grass.add(new Grass(0, 510, 50, 50 ));
		grass.add(new Grass(50, 510, 50, 50 ));
		grass.add(new Grass(100, 510, 50, 50 ));
		grass.add(new Grass(150, 510, 50, 50 ));
		grass.add(new Grass(200, 510, 50, 50 ));

		grass.add(new Grass(945, 510, 50, 50 ));
		grass.add(new Grass(895, 510, 50, 50 ));
		grass.add(new Grass(845, 510, 50, 50 ));
		grass.add(new Grass(795, 510, 50, 50 ));
		grass.add(new Grass(745, 510, 50, 50 ));

		grass.add(new Grass(0, 560, 50, 50 ));
		grass.add(new Grass(50, 560, 50, 50 ));
		grass.add(new Grass(100, 560, 50, 50 ));
		grass.add(new Grass(150, 560, 50, 50 ));
		grass.add(new Grass(200, 560, 50, 50 ));

		grass.add(new Grass(945, 560, 50, 50 ));
		grass.add(new Grass(895, 560, 50, 50 ));
		grass.add(new Grass(845, 560, 50, 50 ));
		grass.add(new Grass(795, 560, 50, 50 ));
		grass.add(new Grass(745, 560, 50, 50 ));

		grass.add(new Grass(0, 610, 50, 50 ));
		grass.add(new Grass(50, 610, 50, 50 ));
		grass.add(new Grass(100, 610, 50, 50 ));
		grass.add(new Grass(150, 610, 50, 50 ));
		grass.add(new Grass(200, 610, 50, 50 ));

		grass.add(new Grass(945, 610, 50, 50 ));
		grass.add(new Grass(895, 610, 50, 50 ));
		grass.add(new Grass(845, 610, 50, 50 ));
		grass.add(new Grass(795, 610, 50, 50 ));
		grass.add(new Grass(745, 610, 50, 50 ));

		grass.add(new Grass(0, 660, 50, 50 ));
		grass.add(new Grass(50, 660, 50, 50 ));
		grass.add(new Grass(100, 660, 50, 50 ));
		grass.add(new Grass(150, 660, 50, 50 ));
		grass.add(new Grass(200, 660, 50, 50 ));

		grass.add(new Grass(945, 660, 50, 50 ));
		grass.add(new Grass(895, 660, 50, 50 ));
		grass.add(new Grass(845, 660, 50, 50 ));
		grass.add(new Grass(795, 660, 50, 50 ));
		grass.add(new Grass(745, 660, 50, 50 ));
	}

}
