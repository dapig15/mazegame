import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class ProjectileTestEnemyHoming extends ProjectileHoming {
	
	public ProjectileTestEnemyHoming(double x, double y, double angle, double velocity, Player playerTarget) {
		super(x, y, angle, velocity, 10, 10, 100, new boolean[] {true, false}, 5, 0.04);
		setObeysGravity(false);
		setTarget(playerTarget);
		try {
			setImgs(new BufferedImage[] {
					ImageIO.read(new File("arrow3.png"))
			});
		} catch (IOException e) {}
	}

	@Override
	void process() {
		setVelocity(getVelocity()+0.3);
		super.process();
	}

}
