package tile_interactive;

import entity.Entity;
import game_2d.GamePanel;

public class IT_Trunk extends InteractiveTile{

	GamePanel gp;
	public IT_Trunk(GamePanel gp , int col , int row) {
		super(gp , col , row);
		
		this.worldX = gp.tileSize * col;
		this.worldY = gp.tileSize * row;
		this.gp = gp;
		down1 = setup("/tiles_interactive/trunk",gp.tileSize,gp.tileSize);
		
		solidArea.x = 0;
		solidArea.y = 0;
		solidArea.width = 0;
		solidArea.height = 0;
		solidAreaDefaultX = 0;
		solidAreaDefaultY = 0;
		
	}
	
	

}
