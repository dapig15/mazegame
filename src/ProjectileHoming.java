
public abstract class ProjectileHoming extends ProjectileAngular {
	
	private Entity target;
	private int homingFramesLeft = 50;
	private double homingFactor = 0.04;
	public Entity getTarget() {
		return target;
	}
	public void setTarget(Entity target) {
		this.target = target;
	}
	public int getHomingFramesLeft() {
		return homingFramesLeft;
	}
	public void setHomingFramesLeft(int homingFramesLeft) {
		this.homingFramesLeft = homingFramesLeft;
	}
	public double getHomingFactor() {
		return homingFactor;
	}
	public void setHomingFactor(double homingFactor) {
		this.homingFactor = homingFactor;
	}
	
	public ProjectileHoming(double x, double y, double angle, double velocity, int hitboxWidth, int hitboxHeight,
			int framesLeftAlive, boolean[] canHitType, int damage, double homingFactor) {
		super(x, y, angle, velocity, hitboxWidth, hitboxHeight, framesLeftAlive, canHitType, damage);
		this.homingFactor = homingFactor;
	}
	
	@Override
	void process() {
		homingFramesLeft--;
		if (homingFramesLeft > 0) {
			double x1 = getX(), y1 = getY(), x3 = target.getX(), y3 = target.getY();
			double x2 = getX()+calcAngularxVel(), y2 = getY()+calcAngularyVel();
			double ordering = (y2 - y1)*(x3 - x2) - (y3 - y2)*(x2 - x1);
			if (ordering < 0)
				setAngle(getAngle()+homingFactor);
			else
				setAngle(getAngle()-homingFactor);
			setxVel(calcAngularxVel());
			setyVel(calcAngularyVel());
		}
		super.process();
	}

}
