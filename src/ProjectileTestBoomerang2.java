import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ProjectileTestBoomerang2 extends ProjectileAngular {

	public ProjectileTestBoomerang2(double x, double y, double angle, double velocity, Entity reference) {
		super(x, y, angle, velocity, 10, 10, 100, new boolean[] {false, true}, 5);
		setImgPaths(new String[] {
				"boomerang1.png"
		});
		setPierceLeft(100000);
		setObeysGravity(false);
		this.reference = reference;
		this.setCanHitWall(true);
	}
	
	Entity reference;

	@Override
	void process() {
		setVelocity(Math.max(-20, getVelocity()-1));
		if (getVelocity() < 0) {
			setCanHitWall(false);
			setAngle(Math.atan2(getY()-reference.getY(), getX()-reference.getX()));
			if (Math.abs(getY()-reference.getY())+Math.abs(getX()-reference.getX()) < 10) {
				kill();
			}
		}
		super.process();
	}
	
	private double angleInc = 0;
	@Override
	void drawObject(Graphics g, double xInc, double yInc) {
		Graphics2D g2d = (Graphics2D) g.create();
		try {
			BufferedImage image = ImageIO.read(new File(getImagePath()));
			g2d.rotate(angleInc-Math.PI/2, getX()+xInc, getY()+yInc);
			g2d.drawImage(image, (int)(getX()-image.getWidth()/2+xInc), (int)(getY()-image.getHeight()/2+yInc), null);
			g2d.dispose();
			angleInc += 0.3;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	void doWorkOnHitWall() {
		if (getVelocity() > 0) {
			setVelocity(-getVelocity());
		}
	}
	@Override
	void doWorkOnHit() {
		if (getVelocity() > 0) {
			setVelocity(-getVelocity());
		}
	}
	@Override
	void doWorkOnDeath() {
		((Player)reference).addAProjectile();
	}

}
