import java.awt.Polygon;

public class PlatformStoneBrick extends Platform {

	public PlatformStoneBrick(Polygon p) {
		super(p, 0.2);
		setImgPaths(new String[] {
				"bricks.png"
		});
	}
	
}
