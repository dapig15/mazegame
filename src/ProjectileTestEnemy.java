
public class ProjectileTestEnemy extends ProjectileAngular {

	public ProjectileTestEnemy(double x, double y, double angle, double velocity) {
		super(x, y, angle, velocity, 10, 10, 100, new boolean[] {true, false}, 5);
		setImgPaths(new String[] {
				"arrow2.png"
		});
		setObeysGravity(false);
	}

	@Override
	void process() {
		super.process();
	}

}
