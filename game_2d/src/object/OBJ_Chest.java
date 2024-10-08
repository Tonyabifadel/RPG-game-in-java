package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import game_2d.GamePanel;


public class OBJ_Chest extends Entity{
	
	public OBJ_Chest(GamePanel gp) {
		super(gp);
		name = "Chest";
		down1 = setup("/objects/chest",gp.tileSize ,gp.tileSize);
	}

}