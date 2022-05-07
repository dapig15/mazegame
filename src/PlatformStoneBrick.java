import java.awt.Polygon;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class PlatformStoneBrick extends Platform {

	public PlatformStoneBrick(Polygon p) {
		super(p, 0.2);
		try {
			setImgs(new BufferedImage[] {
					ImageIO.read(new File("bricks.png")),
			});
		} catch (IOException e) {}
	}
	
}
