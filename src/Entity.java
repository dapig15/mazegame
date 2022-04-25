import java.awt.Point;
import java.awt.Rectangle;

public abstract class Entity extends ObjectWithColliders {
	
	private int type;
	private Rectangle hitbox;
	private int invincibility;
	
	public int getType() {
		return type;
	}
	public Rectangle getHitbox() {
		return hitbox;
	}
	public int getInvincibility() {
		return invincibility;
	}
	public void setInvincibility(int invincibility) {
		this.invincibility = invincibility;
	}

	public Entity(int x, int y, int xVel, int yVel, int hitboxWidth, int hitboxHeight, int type, int maxHp) {
		super(x, y, xVel, yVel, hitboxWidth, hitboxHeight);
		this.type = type;
		this.maxHp = maxHp;
		this.hp = maxHp;
	}
	
	abstract boolean hitByProjectile(Projectile p);
	
	void process() {
		super.process();
		hitbox = new Rectangle((int)(getX()-getHitboxWidth()/2), (int)(getY()-getHitboxHeight()/2), getHitboxWidth(), getHitboxHeight());
		invincibility = Math.max(0, invincibility-1);
	}
	
	void checkOtherEntities() {
		if (hitbox == null)
			return;
		for (Entity e : Main.entities) {
			if (e == this)
				continue;
			Rectangle poly = e.getHitbox();
				if (poly.contains(getBottom())) {
					int i = -1;
					for (i = -1; poly.contains(new Point(getBottom().x, getBottom().y + i)); i--)
						;
					setyVel(0);
					setY(getY() + i + 1);
					recalculateHitbox();
				}
				if (poly.contains(getTop())) {
					int i = 1;
					for (i = 1; poly.contains(new Point(getTop().x, getTop().y + i)); i++)
						;
					setyVel(0);
					setY(getY() + i);
					recalculateHitbox();
				}
				if (poly.contains(getLeft())) {
					int i = 1;
					for (i = 1; poly.contains(new Point(getLeft().x + i, getLeft().y)); i++)
						;
					setxVel(0);
					setX(getX() + i - 1);
					recalculateHitbox();
				}
				if (poly.contains(getRight())) {
					int i = -1;
					for (i = -1; poly.contains(new Point(getRight().x + i, getRight().y)); i--)
						;
					setxVel(0);
					setX(getX() + i + 1);
					recalculateHitbox();
				}
		}
	}
	
	private int hp, maxHp;
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getMaxHp() {
		return maxHp;
	}
	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}
	
}
