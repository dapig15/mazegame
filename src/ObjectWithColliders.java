import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ObjectWithColliders extends ObjectBase {
	private int hitboxWidth = 20, hitboxHeight = 20;
	
	public int getHitboxWidth() {
		return hitboxWidth;
	}

	public void setHitboxWidth(int hitboxWidth) {
		this.hitboxWidth = hitboxWidth;
	}

	public int getHitboxHeight() {
		return hitboxHeight;
	}

	public void setHitboxHeight(int hitboxHeight) {
		this.hitboxHeight = hitboxHeight;
	}

	private Point top, right, bottom, left;
	
	public Point getTop() {
		return top;
	}

	public Point getRight() {
		return right;
	}

	public Point getBottom() {
		return bottom;
	}

	public Point getLeft() {
		return left;
	}

	public void recalculateHitbox() {
		top = new Point((int) (getX()), (int) (getY() - (hitboxHeight / 2)));
		bottom = new Point((int) (getX()), (int) (getY() + (hitboxHeight / 2)));
		left = new Point((int) (getX() - (hitboxWidth / 2)), (int) (getY()));
		right = new Point((int) (getX() + (hitboxWidth / 2)), (int) (getY()));
	}

	private boolean affectedByPlatforms = true;
	
	public ObjectWithColliders(double x, double y, double xVel, double yVel, int hitboxWidth, int hitboxHeight) {
		super(x, y, xVel, yVel);
		this.hitboxWidth = hitboxWidth;
		this.hitboxHeight = hitboxHeight;
		recalculateHitbox();
	}
	public ObjectWithColliders(double x, double y, double xVel, double yVel, int hitboxWidth, int hitboxHeight, boolean affectedByPlatforms) {
		super(x, y, xVel, yVel);
		this.hitboxWidth = hitboxWidth;
		this.hitboxHeight = hitboxHeight;
		this.affectedByPlatforms = affectedByPlatforms;
		recalculateHitbox();
	}
	
	private void updateBottom(Platform plat) {
		Polygon poly = plat.getPolygon();
		if (poly.contains(getBottom())) {
			int i = -1;
			for (i = -1; poly.contains(new Point(getBottom().x, getBottom().y + i)); i--)
				;
			setyVel(Math.min(0, getyVel()));
			setY(getY() + i + 1);
			recalculateHitbox();
			if (getxVel() < 0) {
				setxVel(Math.min(0, getxVel()+plat.getFriction()));
			} else if (getxVel() > 0) {
				setxVel(Math.max(0, getxVel()-plat.getFriction()));
			}
		}
	}
	
	private void updateTop(Platform plat) {
		Polygon poly = plat.getPolygon();
		if (poly.contains(getTop())) {
			int i = 1;
			for (i = 1; poly.contains(new Point(getTop().x, getTop().y + i)); i++)
				;
			setyVel(Math.max(0, getyVel()));
			setY(getY() + i - 1);
			recalculateHitbox();
		}
	}
	
	private void updateVertical(Platform plat) {
		if (getyVel() > 0) {
			updateBottom(plat);
			updateTop(plat);
		} else {
			updateTop(plat);
			updateBottom(plat);
		}
	}
	
	private void updateLeft(Platform plat) {
		Polygon poly = plat.getPolygon();
		if (poly.contains(getLeft())) {
			int i = 1;
			for (i = 1; poly.contains(new Point(getLeft().x + i, getLeft().y)); i++)
				;
			setxVel(Math.max(getxVel(), 0));
			setX(getX() + i - 1);
			recalculateHitbox();
		}
	}
	
	private void updateRight(Platform plat) {
		Polygon poly = plat.getPolygon();
		if (poly.contains(getRight())) {
			int i = -1;
			for (i = -1; poly.contains(new Point(getRight().x + i, getRight().y)); i--)
				;
			setxVel(Math.min(0, getxVel()));
			setX(getX() + i + 1);
			recalculateHitbox();
		}
	}
	
	private void updateHorizontal(Platform plat) {
		if (getxVel() < 0) {
			updateLeft(plat);
			updateRight(plat);
		} else {
			updateRight(plat);
			updateLeft(plat);
		}
	}
	
	@Override
	void process() {
		super.process();
		recalculateHitbox();
		if (affectedByPlatforms) {
			for (Platform plat : Main.platforms) {
				if (Math.abs(getxVel()) > Math.abs(getyVel())) {
					updateHorizontal(plat);
					updateVertical(plat);
				} else {
					updateVertical(plat);
					updateHorizontal(plat);
				}
			}
		}
	}
}
