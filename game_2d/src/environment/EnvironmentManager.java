package environment;

import java.awt.Graphics2D;

import game_2d.GamePanel;

//handles lightning rain...

public class EnvironmentManager {
	GamePanel gp;
	Lighting lighting;
	
	public EnvironmentManager(GamePanel gp) {
		this.gp = gp;
		
	}
	
	public void setup() {
		
		//change this number to make the size of the window,
		//number must be smaller than screnWidth and screenHeight
		lighting = new Lighting(gp,350);
	}
	
	public void draw(Graphics2D g2) {
		lighting.draw(g2);
	}

}
