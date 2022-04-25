import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Player extends Entity {

	public Player(int x, int y, int xVel, int yVel, int hitboxWidth, int hitboxHeight) {
		super(x, y, xVel, yVel, hitboxWidth, hitboxHeight, 0, 200);
		this.setImgPaths(new String[] {
				"player_walking_0.png",
				"player_walking_1.png",
				"player_walking_2.png",
		});
	}

	private int jumpsLeft = 0, maxJumps = 2;
	private char currentDirection = 'N';
	private PlayerKeyAdapter pka = new PlayerKeyAdapter();
	private int screenX = 0, screenY = 0;
	public void updateScreenCoords(int screenX, int screenY) {
		this.screenX = screenX;
		this.screenY = screenY;
	}
	public PlayerKeyAdapter getPKA() {
		return pka;
	}
	private PlayerMouseListener pml = new PlayerMouseListener();
	public PlayerMouseListener getPML() {
		return pml;
	}
	private int dashWindow = 0, dashCooldown = 0;
	private boolean isDashRight = true;
	class PlayerKeyAdapter extends KeyAdapter {
		@Override
		public void keyTyped(KeyEvent e) {}
		@Override
		public void keyPressed(KeyEvent e) {
	        int key = e.getKeyCode();
	        if (key == KeyEvent.VK_SPACE) {
	        	boolean againstWall = false;
	        	for (Platform plat : Main.platforms) {
	        		// TODO dont wall jum pfirst
					if (plat.getPolygon().contains(getLeft())) {
			        	setyVel(-16);
			        	setY(getY()-16);
			        	setxVel(5);
			        	againstWall = true;
			        	break;
					}
					if (plat.getPolygon().contains(getRight())) {
			        	setyVel(-16);
			        	setY(getY()-16);
			        	setxVel(-5);
			        	againstWall = true;
			        	break;
					}
	        	}
	        	if (jumpsLeft > 0 && !againstWall) {
		        	setyVel(-16);
		        	setY(getY()-16);
		        	jumpsLeft--;
	        	}
	        } else if (key == KeyEvent.VK_D) {
	        	currentDirection = 'D';
	        	setHasTurnedLeft(false);
	        	if (dashWindow > 0 && isDashRight) {
	        		System.out.println("dshf");
	        		setxVel(superMaxSpeed);
	        		dashWindow = 0;
	        		setInvincibility(8);
	        		dashCooldown = 20;
	        	}
	        } else if (key == KeyEvent.VK_A) {
	        	currentDirection = 'A';
	        	setHasTurnedLeft(true);
	        	if (dashWindow > 0 && !isDashRight) {
	        		setxVel(-superMaxSpeed);
	        		dashWindow = 0;
	        		setInvincibility(8);
	        		dashCooldown = 20;
	        	}
	        }
	    }
		@Override
		public void keyReleased(KeyEvent e) {
	        int key = e.getKeyCode();
			if (key == KeyEvent.VK_D && currentDirection == 'D') {
	        	currentDirection = 'N';
	        	if (dashCooldown == 0) {
			        dashWindow = 5;
			        isDashRight = true;
	        	}
	        } else if (key == KeyEvent.VK_A && currentDirection == 'A') {
	        	currentDirection = 'N';
	        	if (dashCooldown == 0) {
			        dashWindow = 5;
			        isDashRight = false;
	        	}
	        }
		}
	}

	static int projectileCooldown = 0;
	static boolean spawnProjectile = false;
	
	class PlayerMouseListener implements MouseListener {
		
		@Override public void mouseClicked(MouseEvent e) {}
		@Override public void mouseReleased(MouseEvent e) {
			spawnProjectile = false;
		}
		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseExited(MouseEvent e) {
			spawnProjectile = false;
		}
		@Override public void mousePressed(MouseEvent e) {
			spawnProjectile = true;
		}
		
	}
	
	private double maxSpeed = 10, superMaxSpeed = 20, slipFactor = 0.5;
	private JPanel mainPanel;
	public void setJPanel(JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}
	private int projectilesLeft = 1;
	public void addAProjectile() {
		projectilesLeft++;
	}
	
	private int frameCooldown = 25;
	
	@Override
	void process() {
		
    	if (spawnProjectile && projectileCooldown == 0 && projectilesLeft > 0) {
    		Point mousePosition = MouseInfo.getPointerInfo().getLocation();
    		SwingUtilities.convertPointFromScreen(mousePosition, mainPanel);
			double angle = Math.atan2(mousePosition.getY()-(screenY), mousePosition.getX()-(screenX));
			ProjectileTest testArrow = new ProjectileTest(getX(), getY(), angle, 20);
			if (testArrow.shouldKill() < 0)
				Main.projectiles.add(testArrow);
			projectileCooldown = 4;
			//projectilesLeft--;
    	}
    	projectileCooldown = Math.max(0, projectileCooldown-1);

		// movement
		if (currentDirection == 'D' && getxVel() < maxSpeed) {
			setxVel(Math.min(maxSpeed, getxVel()+slipFactor));
			frameCooldown--;
			if (frameCooldown <= 0) {
				setPathToDisplay((getPathToDisplay()+1)%3);
				frameCooldown = (int) (maxSpeed+1-Math.abs(getxVel()));
			}
		} else if (currentDirection == 'A' && getxVel() > -maxSpeed) {
			setxVel(Math.max(-maxSpeed, getxVel()-slipFactor));
			frameCooldown--;
			if (frameCooldown <= 0) {
				setPathToDisplay((getPathToDisplay()+1)%3);
				frameCooldown = (int) (maxSpeed+1-Math.abs(getxVel()));
			}
		} else {
			if (getxVel() > 0) {
				setxVel(Math.max(0, getxVel()-slipFactor));
			} else if (getxVel() < 0) {
				setxVel(Math.min(0, getxVel()+slipFactor));
			}
		}
		setxVel(Math.min(superMaxSpeed, Math.max(-superMaxSpeed, getxVel())));
		if (getxVel() > maxSpeed) {
			setxVel(Math.max(maxSpeed, getxVel()-slipFactor));
		}
		if (getxVel() < -maxSpeed) {
			setxVel(Math.min(-maxSpeed, getxVel()+slipFactor));
		}
		dashCooldown = Math.max(0, dashCooldown-1);
		dashWindow = Math.max(0, dashWindow-1);
		
		super.process();
		
		for (Platform plat : Main.platforms) {
			if (plat.getPolygon().contains(getBottom())) {
				jumpsLeft = maxJumps;
				break;
			}
		}
		for (Entity e : Main.entities) {
			Rectangle poly = e.getHitbox();
			if (e.getHitbox() != null && poly.contains(getBottom())) {
				jumpsLeft = maxJumps;
				break;
			}
		}
	}
	
	@Override
	boolean hitByProjectile(Projectile p) {
		setxVel(getxVel()+(p.getxVel()/3));
		setyVel(-10);
		setHp(getHp()-p.getDamage());
		setInvincibility(p.getInvincibilityFrames());
		return true;
	}
}