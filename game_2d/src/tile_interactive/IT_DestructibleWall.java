package tile_interactive;

import java.awt.Color;
import java.util.Random;

import entity.Entity;
import game_2d.GamePanel;
import object.OBJ_Bronze_Coin;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

public class IT_DestructibleWall extends InteractiveTile{

	GamePanel gp;
	public IT_DestructibleWall(GamePanel gp , int col , int row) {
		super(gp , col , row);
		
		this.worldX = gp.tileSize * col;
		this.worldY = gp.tileSize * row;
		this.gp = gp;
		down1 = setup("/tiles_interactive/destructibleWall",gp.tileSize,gp.tileSize);
		destructible = true;
		life = 3;
	}
	
	
	public boolean isCorrectItem(Entity entity) {
		boolean isCorrectItem = false;
		
		if(entity.currentWeapon.type == type_pickaxe) {
			isCorrectItem = true;
		}
		
		return isCorrectItem;
	}
	
	public void playSE() {
		gp.playSE(19);
		
	}
	
	public InteractiveTile getDestroyedForm() {
		InteractiveTile tile = null;
		return tile;
	}


	public Color getParticleColor() {
		Color color = new Color(65,65,65);
		return color;
	}
	
	public int getParticleSize() {
		int size =  6; //6 pixels
		return size;
	}
	public int getParticleSpeed() {
		int speed = 1;
		return speed;
	}
	
	public int getParticleMaxLife() {
		int maxLife= 20;
		return maxLife;
	}
	
	//function to drop after breaking it
	//Add this function in the player class in function damageInteractiveTile when life == 0
	public void checkDrop() {
		
		int i = new Random().nextInt(100)+1;
		
		//set the monster drop
		if(i < 50) {
			dropItem(new OBJ_Bronze_Coin(gp));
		}
		if(i >= 50 &  i<75) {
			dropItem(new OBJ_Heart(gp));
		}
		
		if(i >= 75 &  i<100) {
			dropItem(new OBJ_ManaCrystal(gp));
		}
		
	}
}
