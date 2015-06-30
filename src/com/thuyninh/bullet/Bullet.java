
package com.thuyninh.bullet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.thuyninh.audioplay.explode;
import com.thuyninh.audioplay.shootStone;
import com.thuyninh.main.TankClient;
import com.thuyninh.main.TankPanel;
import com.thuyninh.ornament.Brick;
import com.thuyninh.ornament.Explode;
import com.thuyninh.ornament.Gate;
import com.thuyninh.ornament.Stone;
import com.thuyninh.tank.*;

/* class BULLET để lưu trạng thái của viên đạn khi được bắn ra. 
 * Gồm có 4 hình ảnh tương ứng với 4 hướng */
public class Bullet
{
	private static Toolkit				tk;
	private static Image[]				imgs;											// mảng các hình ảnh trạng thái của viên đạn

	// Ánh xạ đi từ tập các hướng của viên đạn kiểu Enum đến tập các hình ảnh biểu thị các trạng thái đó
	public static Map<String, Image>	hashImgs		= new HashMap<String, Image>();
	private boolean						ally;
	private boolean						boss;
	private boolean						live			= true;
	private int							x, y;
	private Direction					dir;											// Direction là 1 kiểu enum dùng để lưu các giá trị hướng của viên đạn bắn ra
	private TankPanel					tp;
	public static final int				BULLET_SIZE		= 10;
	public static final int				BULLET_SPEED	= 12;
	private boolean au= false;
	
	public boolean isAu()
	{
		return au;
	}

	public void setAu(boolean au)
	{
		this.au = au;
	}

	static
	{
		tk = Toolkit.getDefaultToolkit();
		imgs = new Image[]
		{ tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/bulletL.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/bulletU.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/bulletR.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/bulletD.gif")), };

		hashImgs.put("L", imgs[0]);
		hashImgs.put("U", imgs[1]);
		hashImgs.put("R", imgs[2]);
		hashImgs.put("D", imgs[3]);
	}

	public Bullet(int x, int y, Direction dir)
	{
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public Bullet(int x, int y, boolean ally, boolean boss, Direction dir, TankPanel tp)
	{
		this(x, y, dir);
		this.tp = tp;
		this.ally = ally;
		this.boss = boss;
	}

	public void draw(Graphics g)
	{
		// vẽ viên đạn theo hướng của nó khi được bắn ra
		switch (dir)
		{
		case L:
			g.drawImage(hashImgs.get("L"), x, y, null);
			break;
		case U:
			g.drawImage(hashImgs.get("U"), x, y, null);
			break;
		case R:
			g.drawImage(hashImgs.get("R"), x, y, null);
			break;
		case D:
			g.drawImage(hashImgs.get("D"), x, y, null);
			break;
			default:
				break;
		}
		// Cập nhật x và y của viên đạn
		move();
	}

	private void move()
	{
		// Cho viên đạn di chuyển theo hướng của nó
		switch (dir)
		{
		case L:
			x -= BULLET_SPEED;
			break;
		case U:
			y -= BULLET_SPEED;
			break;
		case R:
			x += BULLET_SPEED;
			break;
		case D:
			y += BULLET_SPEED;
			break;
			default:
				break;
		}
		// nếu viên đạn ra khỏi màn hình hoặc gặp 1 xe tăng nào đó thì tự động biến mất
		if (this.x < 0 || this.y < 0 || this.x > TankClient.WIDTH || this.y > TankClient.HEIGHT)
		{
			live = false;
		}

	}

	public boolean isLive()
	{
		return live;
	}

	public void setLive(boolean b)
	{
		live = b;
	}

	public Rectangle getRect()
	{
		return new Rectangle(x, y, BULLET_SIZE, BULLET_SIZE);
	}

	public boolean hitTank(Tank t)
	{
		/*
		 * Nếu: 
		 * + đạn và xe tăng vẫn còn trên bản đồ 
		 * + đạn va chạm và xe tăng 
		 * + đạn được bắn vào quân địch (có thể là tank địch bắn vào tank của player hoặc ngược lại) thì thực hiện khối lệnh sau...
		 */
		if (this.isLive() && t.isLive() && this.getRect().intersects(t.getRect()) && this.ally != t.isAlly())
		{
			if (t.isAlly()) // nếu t là tank của player
			{
				if (this.boss)
					t.setLife(t.getLife() - 50); // HP giảm 25 đơn vị
				else 
					t.setLife(t.getLife() - 25);
				if (t.getLife() <= 0) // Nếu HP k còn thì tank biến mất khỏi map
				{
					t.setLife(0);
					t.setLive(false);
				}
			}
			else
			// nếu t là tank của máy
			{
				if (!t.isBoss())
					t.setLive(false);
				else
				{
					t.setLife(t.getLife() - 50); // HP giảm 50 đơn vị
					if (t.getLife() <= 0) // Nếu HP k còn thì tank biến mất khỏi map
					{
						t.setLife(0);
						t.setLive(false);
					}
				}
			}

			this.live = false; // k hiện viên đạn nữa
			Explode e = new Explode(t.getX(), t.getY());
			tp.explodes.add(e);
			return true;
		}
		return false;
	}

	/*
	 * Hàm trả về trạng thái bị bắn hay chưa của mảng các tank: 
	 * ** Nếu trong mảng đã có 1 xe tăng bị bắn thì trả về giá trị true 
	 * ** Nếu trong mảng chưa có xe tăng nào bị bắn thì trả về false
	 */
	public boolean hitTank(ArrayList<Tank> tanks)
	{
		for (int i = 0; i < tanks.size(); i++)
		{
			if (this.hitTank(tanks.get(i)))
			{
				return true;
			}
		}
		return false;
	}

	public boolean hitGate(Gate gate)
	{
		if (this.live && this.getRect().intersects(gate.getRect()))
		{
			this.live = false;
			gate.setLive(false);
			Explode e = new Explode(gate.x, gate.y);
			tp.explodes.add(e);
			new explode();
			return true;
		}
		return false;
	}

	public boolean hitStone(Stone stone)
	{
		// Nếu viên đạn vẫn còn trên map và đạn va chạm vào đá thì đạn biến mất
		if (this.live && this.getRect().intersects(stone.getRect()))
		{
			this.live = false;
			new shootStone();
			return true;
		}
		return false;
	}

	public boolean hitStone(ArrayList<Stone> stones)
	{
		for (Stone e : stones)
		{
			if (this.hitStone(e))
				return true;
		}
		return false;
	}

	public boolean hitBrick(Brick bricks)
	{
		// Nếu viên đạn vẫn còn trên map và đạn va chạm vào gạch thì đạn và gạch biến mất
		if (this.live && bricks.isLive() && this.getRect().intersects(bricks.getRect()))
		{
			bricks.setLive(false);
			Explode e = new Explode(bricks.getX(), bricks.getY());
			tp.explodes.add(e);
			this.live = false;
			return true;
		}
		return false;
	}

	public boolean hitBrick(ArrayList<Brick> bricks)
	{
		for (int i = 0; i < bricks.size(); i++)
		{
			if (this.hitBrick(bricks.get(i)))
				return true;
		}
		return false;
	}

}
