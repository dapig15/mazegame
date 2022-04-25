
public class ProjectileTest extends ProjectileAngular {

	public ProjectileTest(double x, double y, double angle, double velocity) {
		super(x, y, angle, velocity, 10, 10, 100, new boolean[] {false, true}, 5);
		setImgPaths(new String[] {
				"arrow.png"
		});
	}

	@Override
	void process() {
		super.process();
	}

}
