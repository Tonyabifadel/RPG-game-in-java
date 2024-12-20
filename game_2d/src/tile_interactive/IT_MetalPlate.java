package tile_interactive;

import game_2d.GamePanel;

public class IT_MetalPlate extends InteractiveTile{

	GamePanel gp;
	public static final String itName = "Metal Plate";
	
	public IT_MetalPlate(GamePanel gp , int col , int row) {
		super(gp , col , row);
		
		this.worldX = gp.tileSize * col;
		this.worldY = gp.tileSize * row;
		this.gp = gp;
		name = itName;
		
		down1 = setup("/tiles_interactive/metalplate",gp.tileSize,gp.tileSize);
		
		solidArea.x = 0;
		solidArea.y = 0;
		solidArea.width = 0;
		solidArea.height = 0;
		solidAreaDefaultX = 0;
		solidAreaDefaultY = 0;
		
	}
	
	

}
