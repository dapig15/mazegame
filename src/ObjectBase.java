import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class ObjectBase {

	private double x, y; // center x, center y
	private double xVel, yVel;
	static double gravity = 1, terminalVelocity = 20;
	private boolean obeysGravity = true;
	public void setObeysGravity(boolean obeysGravity) {
		this.obeysGravity = obeysGravity;
	}
	
	BufferedImage[] imgs;
	int pathToDisplay = 0;
	public void setImgs(BufferedImage[] imgPaths) {
		this.imgs = imgPaths;
	}
	int getPathToDisplay() {
		return pathToDisplay;
	}
	void setPathToDisplay(int pathToDisplay) {
		this.pathToDisplay = pathToDisplay;
	}
	public ObjectBase() {
		x = 0;
		y = 0;
		xVel = 0;
		yVel = 0;
	}
	public ObjectBase(double x, double y, double initXVel, double initYVel) {
		this.x = x;
		this.y = y;
		this.xVel = initXVel;
		this.yVel = initYVel;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getxVel() {
		return xVel;
	}

	public void setxVel(double xVel) {
		this.xVel = xVel;
	}

	public double getyVel() {
		return yVel;
	}

	public void setyVel(double yVel) {
		this.yVel = yVel;
	}
	
	public static double getGravity() {
		return gravity;
	}

	public static void setGravity(double gravity) {
		ObjectBase.gravity = gravity;
	}

	// OVERRIDE THIS
	// e.g, make object affected by gravity, etc
	void process() {
		if (obeysGravity) {
			yVel = Math.min(gravity+yVel, terminalVelocity);
		}
			
		x += xVel;
		y += yVel;
	}
	
	BufferedImage getImage() {
		return imgs[pathToDisplay];
	}
	
	private boolean hasTurnedLeft = false;
	public boolean getHasTurnedLeft() {
		return hasTurnedLeft;
	}
	public void setHasTurnedLeft(boolean hasTurnedLeft) {
		this.hasTurnedLeft = hasTurnedLeft;
	}
	void drawObject(Graphics g, double xInc, double yInc) {
		Graphics2D g2d = (Graphics2D) g.create();
		BufferedImage image = imgs[pathToDisplay];
		//g2d.rotate(Math.atan2(proj.getyVel(), proj.getxVel())-(Math.PI/2), proj.getX()-(proj.getHitboxWidth()/2)+playerXInc, proj.getY()-(proj.getHitboxHeight()/2)+playerYInc);
		g2d.drawImage(image, (int)(x+(hasTurnedLeft? image.getWidth()/2 : -image.getWidth()/2)+xInc), (int)(y-image.getHeight()/2+yInc), (hasTurnedLeft ? -image.getWidth() : image.getWidth()), image.getHeight(), null);
		g2d.dispose();
	}
}
