import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class ProjectileAngular extends Projectile {

	private double angle, velocity;
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public double getVelocity() {
		return velocity;
	}
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	public ProjectileAngular(double x, double y, double angle, double velocity, int hitboxWidth, int hitboxHeight, int framesLeftAlive, boolean[] canHitType, int damage) {
		super(x, y, velocity*Math.cos(angle), velocity*Math.sin(angle), hitboxWidth, hitboxHeight, framesLeftAlive, canHitType, damage);
		this.angle = angle;
		this.velocity = velocity;
	}
	
	double calcAngularxVel() {
		return velocity*Math.cos(angle);
	}
	double calcAngularyVel() {
		return velocity*Math.sin(angle); 
	}
	
	@Override
	void process() {
		setxVel(calcAngularxVel());
		setyVel(calcAngularyVel());
		super.process();
	}
	
	@Override
	void drawObject(Graphics g, double xInc, double yInc) {
		Graphics2D g2d = (Graphics2D) g.create();
		try {
			BufferedImage image = ImageIO.read(new File(getImagePath()));
			g2d.rotate(angle-Math.PI/2, getX()+xInc, getY()+yInc);
			g2d.drawImage(image, (int)(getX()-image.getWidth()/2+xInc), (int)(getY()-image.getHeight()/2+yInc), null);
			g2d.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
