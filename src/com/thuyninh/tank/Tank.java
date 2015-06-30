
package com.thuyninh.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.thuyninh.bullet.*;
import com.thuyninh.main.*;
import com.thuyninh.ornament.*;

public class Tank
{
	private static Toolkit			tk;
	private static Image[]			imgsEnemy;
	private static Image[]			imgsAlly;
	private static Image[]			imgsBoss;
	public static Map<String, Image>	hashImgsEnemy	= new HashMap<String, Image>();
	public static Map<String, Image>	hashImgsAlly	= new HashMap<String, Image>();
	public static Map<String, Image>	hashImgsBoss	= new HashMap<String, Image>();
	private int					oldX, oldY;
	private int					step			= 0;
	private boolean				ally			= false;						// giá trị default của xe tăng là xe tăng do máy điều khiển
	private boolean				boss			= false;					// là tank thường
	public static Random			r			= new Random();
	private boolean				live			= true;						// còn sống không
	private int					life			= 100;
	private TankPanel				tp;
	private int					x, y;
	private Direction				dir			= Direction.STOP;
	private Direction				ptdir		= Direction.U;
	public boolean					bleft		= false, bright = false, bup = false, bdown = false;
	public static final int			TANK_SPEED	= 5;
	protected String				dirL, dirU, dirR, dirD;
	private boolean outOfHome = false;
	static
	{
		tk = Toolkit.getDefaultToolkit();
		
		// Khởi tạo các hình ảnh trạng thái của tank boss
		imgsBoss = new Image[]
		{ tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/bossL.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/bossU.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/bossR.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/bossD.png")), };

		hashImgsBoss.put("L", imgsBoss[0]);
		hashImgsBoss.put("U", imgsBoss[1]);
		hashImgsBoss.put("R", imgsBoss[2]);
		hashImgsBoss.put("D", imgsBoss[3]);

		// Khởi tạo các hình ảnh trạng thái của tank player
		imgsAlly = new Image[]
		{ tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/allyL.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/allyU.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/allyR.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/allyD.png")) };

		hashImgsAlly.put("L", imgsAlly[0]);
		hashImgsAlly.put("U", imgsAlly[1]);
		hashImgsAlly.put("R", imgsAlly[2]);
		hashImgsAlly.put("D", imgsAlly[3]);

		// // Khởi tạo các hình ảnh trạng thái của tank enemy thường
		imgsEnemy = new Image[]
		{ tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/enemyL.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/enemyU.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/enemyR.png")),
			tk.getImage(Explode.class.getClassLoader().getResource("com/thuyninh/images/enemyD.png")) };

		hashImgsEnemy.put("L", imgsEnemy[0]);
		hashImgsEnemy.put("U", imgsEnemy[1]);
		hashImgsEnemy.put("R", imgsEnemy[2]);
		hashImgsEnemy.put("D", imgsEnemy[3]);
	}

	public Tank(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;

	}

	public Tank(int x, int y, TankPanel tankPanel)
	{
		this(x, y);
		this.tp = tankPanel;
	}

	public void draw(Graphics g)
	{
		if (!live)
			return; // nếu xe tăng bị bắn thì k vẽ nữa
		// Vẽ các trạng thái của các loại xe tăng
		if (!ally)
		{
			if (!boss)
			{
				switch (ptdir)
				{
					case L:
						g.drawImage(hashImgsEnemy.get("L"), x, y, null);
						break;
					case U:
						g.drawImage(hashImgsEnemy.get("U"), x, y, null);
						break;
					case R:
						g.drawImage(hashImgsEnemy.get("R"), x, y, null);
						break;
					case D:
						g.drawImage(hashImgsEnemy.get("D"), x, y, null);
						break;
					default:
						break;
				}
			}
			else
			{
				switch (ptdir)
				{
					case L:
						g.drawImage(hashImgsBoss.get("L"), x, y, null);
						break;
					case U:
						g.drawImage(hashImgsBoss.get("U"), x, y, null);
						break;
					case R:
						g.drawImage(hashImgsBoss.get("R"), x, y, null);
						break;
					case D:
						g.drawImage(hashImgsBoss.get("D"), x, y, null);
						break;
					default:
						break;
				}
			}
		}
		else
		{
			switch (ptdir)
			{
				case L:
					g.drawImage(hashImgsAlly.get("L"), x, y, null);
					break;
				case U:
					g.drawImage(hashImgsAlly.get("U"), x, y, null);
					break;
				case R:
					g.drawImage(hashImgsAlly.get("R"), x, y, null);
					break;
				case D:
					g.drawImage(hashImgsAlly.get("D"), x, y, null);
					break;
				default:
					break;
			}
			outOfHome =true;
			if (this.y <60) outOfHome =false;
		}
		move(); // cập nhật tọa độ của xe tăng
	}

	// Cho xe tăng di chuyển
	private void move()
	{
		this.oldX = this.x; // Lưu lại tọa độ cũ của xe tăng
		this.oldY = this.y;
		switch (dir)
		{
			case L:
				x -= TANK_SPEED;
				break;
			case U:
				y -= TANK_SPEED;
				break;
			case R:
				x += TANK_SPEED;
				break;
			case D:
				y += TANK_SPEED;
				break;
			case STOP:
				break;
		}
		// Nếu không phải là trạng thái dừng lại thì cho xe tăng di chuyển tiếp
		if (this.dir != Direction.STOP)
		{
			ptdir = dir;
		}
		// Nếu vị trí xe tăng vượt qua khỏi map thì cho xe tăng dừng lại tạm thời
		if (x < 0)
			stay();
		if (y < 0)
			stay();
		if (x + getTankWidth() > TankClient.WIDTH -10)
			stay();
		if (y + getTankHeight() > TankClient.HEIGHT -30)
			stay();

		if (!ally)
		{
			/*
			 * Nếu không phải là xe tăng của player thì cho xe tăng đi với hướng ngẫu nhiên 
			 * Mỗi 1 hướng đi của xe tăng sẽ chỉ xác định được 1 số ngẫu nhiên 20<= x <40 bước đi.
			 * Nếu số bước của xe tăng = 0 thì cho xe tăng chuyển hướng sau đó với mỗi 1 lần di chuyển thì số bước đi lại giảm 1 đơn vị
			 */
			if (step == 0)
			{
				step = r.nextInt(20) + 20;
				Direction[] dirs = Direction.values();
				int rn = r.nextInt(dirs.length - 1);
				dir = dirs[rn];
				if (this.x >200 && this.x<500 && this.y >625)
					dir = Direction.R;
				if (this.x >500 && this.x<800 && this.y >625)
					dir = Direction.L;
				if (this.y > 60 && outOfHome == false )
					outOfHome =true;
			}
			step--;

			// xác suất bắn đạn của xe tăng là 1/40
			if ((this.x >200 && this.x<500 && this.y >650) || (this.x >500 && this.x<800 && this.y >650))
			{
				if (r.nextInt(40) > 25)
					this.fire();
			}
			else if (r.nextInt(40) > 38)
				this.fire();
			 
		}
		
	}

	private void stay()
	{
		// Đưa tọa độ của xe tăng trở về vị trí cũ
		x = oldX;
		y = oldY;
		dir = Direction.STOP; // không cho xe tăng di chuyển nữa

		/*
		 * Số bước để xe tăng đi tiếp theo hướng cũ được đưa về giá trị mặc định = 0 để sau khi dừng lại xe tăng sẽ lấy 1 giá trị hướng ngẫu nhiên khác
		 */
		step = 0;
	}

	public boolean collidesWithGate(Gate gate)
	{
		if (this.live && gate.isLive() && this.getRect().intersects(gate.getRect()))
		{
			stay();
			return true;
		}
		return false;
	}

	public boolean isBoss()
	{
		return boss;
	}

	public void setBoss(boolean boss)
	{
		this.boss = boss;
	}

	public boolean collidesWithStone(Stone stone)
	{
		// Nếu xe tăng còn sống và chạm vào đá thì cho xe tăng đứng yên k đi nữa
		if (this.live && this.getRect().intersects(stone.getRect()))
		{
			stay();
			return true;
		}
		return false;
	}

	public boolean collidesWithStone(ArrayList<Stone> stones)
	{
		for (Stone e : stones)
		{
			if (this.collidesWithStone(e))
				return true;
		}
		return false;
	}

	public boolean collidesWithSea(Sea sea)
	{
		if (this.live && this.getRect().intersects(sea.getRect()))
		{
			stay();
			return true;
		}
		return false;
	}

	public boolean collidesWithSea(ArrayList<Sea> sea)
	{
		for (Sea e : sea)
		{
			if (this.collidesWithSea(e))
				return true;
		}
		return false;
	}

	public boolean collidesWithBrick(Brick bricks)
	{
		// Nếu xe tăng còn sống và chạm vào gạch thì cho xe tăng đứng yên k đi nữa
		if (this.live && bricks.isLive() && this.getRect().intersects(bricks.getRect()))
		{
			stay();
			return true;
		}
		return false;
	}

	public boolean collidesWithBrick(ArrayList<Brick> bricks)
	{
		for (Brick e : bricks)
		{
			if (this.collidesWithBrick(e))
				return true;
		}
		return false;
	}

	public boolean returnHome()
	{
		if (this.y <70 && outOfHome == true && this.ally==false)
		{
			stay();
			return true;
		}
		return false;
	}
	
	public boolean collidesWithTank(Tank t)
	{
		/* Các trường hợp khi 2 xe tăng chạm nhau
		 * Nếu 2 xe tank cùng 1 đội thì chạm vào nhau sẽ dừng lại
		 * Nếu tank của player và tank enemy chạm vào thì dừng lại
		 * Nếu tank của player và tank boss chạm và thì cả 2 cùng biến mất và phát nổ
		 * */
		if (this != t && this.live && t.live && this.getRect().intersects(t.getRect()) )
		{
			if (this.boss != t.boss && this.ally != t.ally)
			{
				this.live=false;
				t.live=false;
				Explode e = new Explode(t.getX(), t.getY());
				tp.explodes.add(e);
			}
			else
				this.stay();
			return true;
		}
		return false;
	}

	public boolean collidesWithTank(ArrayList<Tank> tanks)
	{
		// Nếu 1 phần tử nào đó trong mảng tanks mà có vị trí cạnh phần tử tank khác thì trả về true
		for (Tank e : tanks)
		{
			if (this != e && this.collidesWithTank(e))
				return true;
		}
		return false;
	}

	protected void locateDirection()
	{
		// Hàm này xác định hướng đi cho xe tăng
		if (bleft && !bright && !bup && !bdown)
			dir = Direction.L;

		else if (!bleft && !bright && bup && !bdown)
			dir = Direction.U;

		else if (!bleft && bright && !bup && !bdown)
			dir = Direction.R;

		else if (!bleft && !bright && !bup && bdown)
			dir = Direction.D;

		else if (!bleft && !bright && !bup && !bdown)
			dir = Direction.STOP;
	}

	public Bullet fire()
	{
		// Trả về viên đạn đã được bắn đi
		return this.fire(ptdir);
	}

	public Bullet fire(Direction dir)
	{
		if (!live) // Nếu xe tăng không còn sống nữa thì không bắn được
			return null;

		// Xác định tọa độ ban đầu của viên đạn
		int x = this.x + getTankWidth() / 2 - Bullet.BULLET_SIZE / 2;
		int y = this.y + getTankHeight() / 2 - Bullet.BULLET_SIZE / 2;
		Bullet b = new Bullet(x, y, this.ally, this.boss, dir, this.tp);
		tp.bulletArrayList.add(b); // Thêm viên đạn này vào mảng các viên đạn trong TankPanel
		return b;
	}

	public Rectangle getRect()
	{
		return new Rectangle(x, y, Tank.getTankWidth(), Tank.getTankHeight());
	}

	public boolean isLive()
	{
		return live;
	}

	public void setLive(boolean live)
	{
		this.live = live;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public boolean isAlly()
	{
		return ally;
	}

	public int getLife()
	{
		return life;
	}

	public void setLife(int life)
	{
		this.life = life;
	}

	public void setColor(Color c)
	{
	}

	public void setAlly(boolean b)
	{
		ally = b;
	}

	public static int getTankWidth()
	{
		return imgsEnemy[0].getWidth(null);
	}

	public static int getTankHeight()
	{
		return imgsEnemy[0].getHeight(null);
	}

	public boolean isOutOfHome()
	{
		return outOfHome;
	}

	public void setOutOfHome(boolean outOfHome)
	{
		this.outOfHome = outOfHome;
	}

}
