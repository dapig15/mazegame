import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public abstract class Projectile extends ObjectWithColliders {
	private int invincibilityFrames = 5;
	public int getInvincibilityFrames() {
		return invincibilityFrames;
	}
	public void setInvincibilityFrames(int invincibilityFrames) {
		this.invincibilityFrames = invincibilityFrames;
	}

	private double framesLeftAlive;
	public void kill() {
		framesLeftAlive = 0;
	}
	
	private int pierceLeft = 1;
	public void setPierceLeft(int i) {
		pierceLeft = i;
	}
	
	private int damage;
	public int getDamage() {
		return damage;
	}

	private boolean[] canHitType;
	public boolean canHitType(int type) {
		if (type >= canHitType.length || type < 0)
			return false;
		return (canHitType[type]);
	}

	public Projectile(double x, double y, double xVel, double yVel, int hitboxWidth, int hitboxHeight, int framesLeftAlive, boolean[] canHitType, int damage) {
		super(x, y, xVel, yVel, hitboxWidth, hitboxHeight);
		this.framesLeftAlive = framesLeftAlive;
		this.canHitType = canHitType;
		this.damage = damage;
	}
	
	// if shouldKill>=0, yes. else, no
	int shouldKill() {
		for (Platform p : Main.platforms) {
			Polygon temp = p.getPolygon();
			if (temp.contains(getTop()) || temp.contains(getBottom()) || temp.contains(getLeft()) || temp.contains(getRight())) {
				if (canHitWall) {
					this.doWorkOnHitWall();
				} else {
					doWorkOnDeath();
					return 1;
				}
			}
		}
		for (Entity e : Main.entities) {
			if (canHitType[e.getType()]) {
				Rectangle temp = e.getHitbox();
				if (temp.contains(getTop()) || temp.contains(getBottom()) || temp.contains(getLeft()) || temp.contains(getRight())) {
					if (e.getInvincibility() == 0 && e.hitByProjectile(this)) {
						pierceLeft--;
						doWorkOnHit();
						if (pierceLeft == 0) {
							doWorkOnDeath();
							return 2;
						}
					} else {
						return -1;
					}
				}
			}
		}
		framesLeftAlive--;
		if (framesLeftAlive < 0) {
			doWorkOnDeath();
			return 0;
		} else {
			return -1;
		}
	}
	
	boolean canHitWall = false;
	public void setCanHitWall(boolean canHitWall) {
		this.canHitWall = canHitWall;
	}
	void doWorkOnHitWall() {};
	void doWorkOnHit() {};
	void doWorkOnDeath() {};
	
	@Override
	void drawObject(Graphics g, double xInc, double yInc) {
		Graphics2D g2d = (Graphics2D) g.create();
			BufferedImage image = getImage();
			g2d.rotate(Math.atan2(getyVel(), getxVel())-(Math.PI/2), getX()+xInc, getY()+yInc);
			g2d.drawImage(image, (int)(getX()-image.getWidth()/2+xInc), (int)(getY()-image.getHeight()/2+yInc), null);
			g2d.dispose();
	}
}
