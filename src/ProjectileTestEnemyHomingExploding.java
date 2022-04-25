
public class ProjectileTestEnemyHomingExploding extends ProjectileHoming {

	public ProjectileTestEnemyHomingExploding(double x, double y, double angle, double velocity, Player playerTarget) {
		super(x, y, angle, velocity, 10, 10, 100, new boolean[] {true, false}, 5, 0.04);
		setObeysGravity(false);
		setTarget(playerTarget);
		setImgPaths(new String[] {
				"arrow3.png"
		});
	}

	@Override
	void process() {
		setVelocity(getVelocity()+0.3);
		super.process();
	}
	
	@Override void doWorkOnDeath() {
		for (double i = getAngle(); i < getAngle()+2*Math.PI; i += Math.PI/4) {
			Main.projectilestoAdd.add(new ProjectileTestEnemy((int)getX(), (int)getY(), i, 5));
		}
	}

}
