import java.awt.Polygon;

public class Platform extends ObjectBase {
	private Polygon p;
	private double friction;
	public Polygon getPolygon() {
		return p;
	}
	public void setPolygon(Polygon p) {
		this.p = p;
	}
	public double getFriction() {
		return friction;
	}
	public Platform(Polygon p, double friction) {
		super();
		this.p = p;
		this.friction = friction;
		this.setX(p.getBounds().getMinX());
		this.setY(p.getBounds().getMinY());
		setObeysGravity(false);
	}
}
