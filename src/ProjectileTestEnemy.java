
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class ProjectileTestEnemy extends ProjectileAngular {

	public ProjectileTestEnemy(double x, double y, double angle, double velocity) {
		super(x, y, angle, velocity, 10, 10, 100, new boolean[] {true, false}, 5);
		try {
			setImgs(new BufferedImage[] {
					ImageIO.read(new File("arrow2.png"))
			});
		} catch (IOException e) {}
		setObeysGravity(false);
	}

	@Override
	void process() {
		super.process();
	}

}
