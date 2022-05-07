import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Stack;
import java.util.TimerTask;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
	static class GamePanel extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;
		// private boolean first = true;

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawString(hitShots + "/" + totalShots, 0, 20);
			int playerXInc = (this.getWidth() / 2) - (int) mainPlayer.getX();
			int playerYInc = (this.getHeight() / 2) - (int) mainPlayer.getY();
			g.translate(playerXInc, playerYInc);
			g.drawImage(bi.getSubimage(
					Math.max(0, Math.min(bi.getWidth()-getWidth(), (int)mainPlayer.getX()-getWidth()/2)),
					Math.max(0, Math.min(bi.getHeight()-getHeight(), (int)mainPlayer.getY()-getHeight()/2)),
					getWidth(), getHeight()), (int)mainPlayer.getX()-getWidth()/2, (int)mainPlayer.getY()-getHeight()/2, null);
			double xInc = ((mainPlayer.getX() < getWidth()/2) ? -(getWidth()/2-mainPlayer.getX()) : 
				(mainPlayer.getX() > bi.getWidth()-getWidth()/2) ? (mainPlayer.getX()-bi.getWidth()+getWidth()/2) : 0);
			double yInc = ((mainPlayer.getY() < getHeight()/2) ? -(getHeight()/2-mainPlayer.getY()) : 
				(mainPlayer.getY() > bi.getHeight()-getHeight()/2) ? (mainPlayer.getY()-bi.getHeight()+getHeight()/2) : 0);
			mainPlayer.updateScreenCoords((int)(getWidth()/2+xInc), (int)(getHeight()/2+yInc));
			for (Entity e : entities) {
				e.drawObject(g, xInc, yInc);
				/*if (e.getInvincibility() > 0)
					g.setColor(Color.green);
				g.drawRect((int) (e.getX() - e.getHitboxWidth() / 2 + xInc),
						(int) (e.getY() - e.getHitboxHeight() / 2 + yInc),
						e.getHitboxWidth(), e.getHitboxHeight());*/
				g.setColor(Color.red);
				g.fillRect((int) (e.getX() - e.getHitboxWidth() / 2 + xInc),
						(int) (e.getY() - e.getHitboxHeight() / 2 - 15 + yInc),
						(int) (e.getHitboxWidth()), 5);
				g.setColor(Color.green);
				g.fillRect((int) (e.getX() - e.getHitboxWidth() / 2 + xInc),
						(int) (e.getY() - e.getHitboxHeight() / 2 - 15 + yInc),
						(int) (e.getHitboxWidth() * e.getHp() / e.getMaxHp()), 5);
				g.setColor(Color.black);
			}
			for (Projectile p : projectiles) {
				p.drawObject(g, xInc, yInc);
			}
			/*
			 * if (first) { first = false; for (Platform p : platforms) { Polygon poly =
			 * p.getPolygon(); Rectangle bounds = poly.getBounds(); if
			 * (Math.abs(bounds.getCenterX()-mainPlayer.getX()) < this.getWidth()*2/3 &&
			 * Math.abs(bounds.getCenterY()-mainPlayer.getY()) < this.getHeight()*2/3) {
			 * g.fillPolygon(poly); p.drawObject(g); } } }
			 */

			/*
			 * g.drawRect((this.getWidth()/2-mainPlayer.getHitboxWidth()/2),
			 * (this.getHeight()/2-mainPlayer.getHitboxHeight()/2),
			 * mainPlayer.getHitboxWidth(), mainPlayer.getHitboxHeight()); for (Polygon poly
			 * : colliders) { poly.translate(playerXInc, playerYInc); g.fillPolygon(poly);
			 * poly.translate(-playerXInc, -playerYInc); } for (GravityProjectile proj :
			 * projectiles) { Graphics2D g2d = (Graphics2D) g.create(); try { BufferedImage
			 * image = ImageIO.read(new File("arrow.png"));
			 * g2d.rotate(Math.atan2(proj.getyVel(), proj.getxVel())-(Math.PI/2),
			 * proj.getX()-(proj.getHitboxWidth()/2)+playerXInc,
			 * proj.getY()-(proj.getHitboxHeight()/2)+playerYInc); g2d.drawImage(image,
			 * (int)(proj.getX()-(proj.getHitboxWidth()/2))+playerXInc,
			 * (int)(proj.getY()-(proj.getHitboxHeight()/2))+playerYInc, this);
			 * g2d.dispose(); } catch (IOException e) { e.printStackTrace(); } }
			 */

		}

		public GamePanel() {
			this.setPreferredSize(new Dimension(1024, 512));
			this.addKeyListener(mainPlayer.getPKA());
			this.addMouseListener(mainPlayer.getPML());
			this.setFocusable(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.repaint();
		}
	}

	static JFrame mainFrame;
	static JPanel mainPanel;
	static Player mainPlayer;

	static ArrayList<Platform> platforms = new ArrayList<>();
	static ArrayList<Entity> entities = new ArrayList<>();
	static ArrayList<Projectile> projectiles = new ArrayList<>();
	static ArrayList<Projectile> projectilestoAdd = new ArrayList<>();

	static Timer timer;

	static int hitShots = 0, totalShots = 0;

	public static BufferedImage bi;

	public static void main(String[] args) {
		mainFrame = new JFrame();
		// mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPlayer = new Player(128, 128, 0, 0, 32, 48);
		entities.add(mainPlayer);
		BossEnemy enemy = new BossEnemy(300, 128, 0, 0, 64, 96, mainPlayer);
		entities.add(enemy);
		BossEnemy enemy2 = new BossEnemy(300, 128, 0, 0, 50, 50, mainPlayer);
		entities.add(enemy2);
		BossEnemy enemy3 = new BossEnemy(300, 128, 0, 0, 50, 50, mainPlayer);
		entities.add(enemy3);
		mainPanel = new GamePanel();
		mainFrame.setContentPane(mainPanel);
		mainFrame.pack();
		mainFrame.setVisible(true);
		mainPlayer.setJPanel(mainPanel);
		/*
		 * Polygon p = new Polygon(new int[] {-10000, -10000, 10000, 10000}, new int[]
		 * {200, 1000, 1000, 200}, 4); Polygon p2 = new Polygon(new int[] {-1000, -900,
		 * -900, -1000}, new int[] {200, 200, -1000, -1000}, 4); Polygon p3 = new
		 * Polygon(new int[] {1000, 900, 900, 1000}, new int[] {200, 200, -1000, -1000},
		 * 4); Polygon p4 = new Polygon(new int[] {1000, 100, 1000}, new int[] {300,
		 * 300, 0}, 3); Polygon p5 = new Polygon(new int[] {-1000, -100, -1000}, new
		 * int[] {300, 300, 0}, 3); platforms.add(new Platform(p, 0.2));
		 * platforms.add(new Platform(p2, 0.2)); platforms.add(new Platform(p3, 0.2));
		 * platforms.add(new Platform(p4, 0.2)); platforms.add(new Platform(p5, 0.2));
		 */
		int[][] maze = generateMaze();
		/*
		 * for (int i = 0; i < maze.length; i++) { for (int j = 0; j < maze[0].length;
		 * j++) { if (maze[i][j]==1) { Polygon p = new Polygon( new int[] {i*128,
		 * i*128+128, i*128+128, i*128}, new int[] {j*128, j*128, j*128+128, j*128+128},
		 * 4); platforms.add(new PlatformStoneBrick(p)); } } }
		 */
		timer = new Timer();
		timer.schedule(new RemindTask(), 0, 20);
	}

	static class RemindTask extends TimerTask {
		public void run() {

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (mainPlayer.getHp() <= 0) {
						Thread.currentThread().interrupt();
						return;
					}

					projectilestoAdd = new ArrayList<>();
					for (Projectile proj : projectiles) {
						proj.process();
						int shouldKill = proj.shouldKill();
						if (shouldKill < 0) {
							projectilestoAdd.add(proj);
						} else if (shouldKill >= 0 && proj.canHitType(1)) {
							totalShots++;
							if (shouldKill == 2)
								hitShots++;
						}
					}
					projectiles = projectilestoAdd;

					ArrayList<Entity> newEntities = new ArrayList<Entity>();
					for (Entity entity : entities) {
						entity.process();
						if (!(entity.getHp() <= 0 && !(entity instanceof Player))) {
							newEntities.add(entity);
						}
					}
					entities = newEntities;

					/*
					 * for (Entity entity : entities) { entity.checkOtherEntities(); }
					 */

					mainFrame.repaint();
				}
			});
		}
	}

	public static int[][] generateMaze() {
		int[][] maze = new int[40][40];
		boolean[][] visited = new boolean[maze.length][maze[0].length];
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				maze[i][j] = 1;
				if (i == 0 || i == maze.length - 1 || j == 0 || j == maze[0].length - 1) {
					visited[i][j] = true;
				}
			}
		}
		Stack<int[]> positions = new Stack<>();
		positions.add(new int[] { 1, 1 });
		final int[] yincr = new int[] { -1, 0, 1, 0 };
		final int[] xincr = new int[] { 0, 1, 0, -1 };
		while (!positions.isEmpty()) {
			int[] next = positions.peek();
			// System.out.println(Arrays.toString(next));
			int y = next[0], x = next[1];
			visited[y][x] = true;
			maze[y][x] = 0;
			boolean[] open = new boolean[4];
			int vis = 0;
			for (int i = 0; i < yincr.length; i++) {
				visited[y + yincr[i]][x + xincr[i]] = true;
			}
			for (int i = 0; i < yincr.length; i++) {
				boolean newVis = false;
				if (y + yincr[i] == 0 || y + yincr[i] == maze.length - 1 || x + xincr[i] == 0
						|| x + xincr[i] == maze[0].length - 1) {
					continue;
				}
				for (int j = 0; j < yincr.length; j++) {
					if (!visited[y + yincr[i] + yincr[j]][x + xincr[i] + xincr[j]]) {
						newVis = true;
					}
				}
				if (newVis) {
					open[i] = true;
					vis++;
				}
			}
			if (vis == 0) {
				positions.pop();
			} else {int chosen = (int) (Math.random() * vis);
				/*if (open[1]) {
					positions.add(new int[] { y, x+1 });
					continue;
				}
				if (open[3]) {
					positions.add(new int[] { y, x-1 });
					continue;
				}*/
				for (int i = 0; i < open.length; i++) {
					if (open[i]) {
						if (chosen == 0) {
							positions.add(new int[] { y + yincr[i], x + xincr[i] });
							break;
						} else {
							chosen--;
						}
					}
				}
			}
		}
		maze[maze.length - 2][maze[0].length - 2] = 0;
		bi = new BufferedImage(40 * 128, 40 * 128, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		g.setColor(new Color(100, 100, 100, 255));
		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				Graphics2D g2d = (Graphics2D) g.create();
				try {
					BufferedImage image = ImageIO.read(new File("bricksbkgd.png"));
					// g2d.rotate(Math.atan2(proj.getyVel(), proj.getxVel())-(Math.PI/2),
					// proj.getX()-(proj.getHitboxWidth()/2)+playerXInc,
					// proj.getY()-(proj.getHitboxHeight()/2)+playerYInc);
					g2d.drawImage(image, i * 128, j * 128, null);
					g2d.dispose();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				if (maze[i][j] == 1) {
					PlatformStoneBrick p = new PlatformStoneBrick(
							new Polygon(new int[] { j * 128, j * 128 + 128, j * 128 + 128, j * 128 },
									new int[] { i * 128, i * 128, i * 128 + 128, i * 128 + 128 }, 4));
					platforms.add(p);
					Graphics2D g2d = (Graphics2D) g.create();
					try {
						BufferedImage image = ImageIO.read(new File("bricks.png"));
						// g2d.rotate(Math.atan2(proj.getyVel(), proj.getxVel())-(Math.PI/2),
						// proj.getX()-(proj.getHitboxWidth()/2)+playerXInc,
						// proj.getY()-(proj.getHitboxHeight()/2)+playerYInc);
						g2d.drawImage(image, j * 128, i * 128, null);
						g2d.dispose();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return maze;
	}
}
