package game_2d;

import java.awt.Color; 
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import ai.PathFinder;
import data.SaveLoad;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import tile_interactive.InteractiveTile;
import tiles.Map;
import tiles.TileManager;

public class GamePanel extends JPanel implements Runnable{
	//Screen settings
	final int originalTileSize = 16;//16x16 tile size
									//by default we use 16x16 in 2d game but 16x16 might look small on screen so we need to scale it
	final int scale = 3;
	
	public final int  tileSize = originalTileSize * scale; //48x48 tile
	public final int maxScreenCol = 20;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol;  //960 pixels
	public final int screenHeight = tileSize * maxScreenRow; //576 pixels
	
	//World Settings
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	
	//Max map is how many map we can create
	public final int maxMap = 10;
	//
	public int currentMap = 2;
	
	int screenWidth2 = screenWidth;
	int screenHeight2 = screenHeight;
	BufferedImage tempScreen;
	Graphics2D g2;

	
	public boolean fullScreenOn = false;
	int FPS = 60;
	
	//System
	public TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	Sound music = new Sound();
	Sound se = new Sound();
	
	Thread gameThread;
	public CollisionChecker cChecker   = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public EventHandler eHandler = new EventHandler(this);
	public PathFinder pFinder = new PathFinder(this);
	EnvironmentManager eManager  = new EnvironmentManager(this);
	Map map  = new Map(this);
	SaveLoad saveLoad = new SaveLoad(this);
	public EntityGenerator eGenerator = new EntityGenerator(this);
	public CutsceneManager csManager = new CutsceneManager(this);
	
	//Entity and Object
	public Player player = new Player(this,keyH);
	//the number 10 means that we can show only 10 objects at the same time
	//only 10 at 1 time
	public Entity obj[][] = new Entity[maxMap][20];
	public Entity npc[][] = new Entity[maxMap][20];
	public Entity monster[][] = new Entity[maxMap][25];
	
	public InteractiveTile iTile[][] = new InteractiveTile[maxMap][50];
	public Entity projectileList[][] = new Entity[maxMap][20];

	//public ArrayList<Entity> projectileList = new ArrayList<Entity>();
	public ArrayList<Entity> particleList = new ArrayList<>(); 
	ArrayList<Entity> entityList = new ArrayList<Entity>();
	
	//GAME STATE	
	public int gameState;
	public final int titleState = 0;
	public final int playState=1;
	public final int pauseState=2;
	public final int dialogueState =3;
	public final int characterState =4;
	public final int optionsState = 5;
	public final int gameOverState= 6;
	public final int transitionState = 7;
	public final int tradeState = 8;
	public final int sleepState = 9;
	public final int mapState = 10;
	public final int cutsceneState = 11;
	
	public boolean bossBattleOn = false;
	

	//AREA
	public int currentArea;
	public int nextArea;
	public final int outside = 50;
	public final int indoor  = 51;
	public final int dungeon = 52;
	

	public GamePanel() {
		this.setPreferredSize(new Dimension (screenWidth , screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		//focus to receive game key input 
		this.setFocusable(true);
		
	}
	
	public void setupGame() {
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
		aSetter.setInteractiveTile();
		eManager.setup();
		
		gameState = titleState;
		currentArea = outside;
		
		tempScreen = new BufferedImage(screenWidth, screenHeight , BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D) tempScreen.getGraphics();
		
		if(fullScreenOn ==true) {
			setFullScreen();
		}
	}
	
	public void setFullScreen() {
		//Get Local screen device
		GraphicsEnvironment g2  = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = g2.getDefaultScreenDevice();
		gd.setFullScreenWindow(Main.window);
	
		//get full screen width and height
		screenWidth2 = Main.window.getWidth();
		screenHeight2 = Main.window.getHeight();

	}

	public void StartGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void resetGame(boolean restart) {
		
		stopMusic();
		currentArea = outside;
		removeTempEntity();
		bossBattleOn = false;
		player.setDefaultPosition();
		player.restoreStatus();
		player.resetCounter();
		aSetter.setNPC();
		aSetter.setMonster();
		
		if(restart == true) {
			player.setDefaultValue();
			aSetter.setObject();
			aSetter.setInteractiveTile();
			eManager.lighting.resetDay();
		}
	}

	
	//here we will create the game loop
	//2 things to do:
	//this is one way to create a decent game loop
//	public void run() {
//		//this means draw the screen every 0.01666 seconds
//		double drawInterval = 1000000000/FPS;
//		double nextDrawTime = System.nanoTime()+drawInterval; 
//		
//		while(gameThread !=null) {
//			
//			// update the information of the player
//			update();
//			
//			// draw the screen with new information
//					
//			repaint();
//			try {
//			double remainingTime = nextDrawTime - System.nanoTime();
//			remainingTime = remainingTime/1000000;
//			
//			if(remainingTime <0) {
//				remainingTime=0;
//			}
//			
//				Thread.sleep((long) remainingTime );
//				nextDrawTime += drawInterval; 
//			}catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//		
//	}
//	
//	another way to create a game loop
	  
	  public void run(){
	  double drawInterval = 1000000000/FPS;
	  double delta = 0;
	  long lastTime = System.nanoTime();
	  long currentTime;
	  long timer =0;
	  long drawCount = 0;
	  
	  while(gameThread!=null){
		  
		  currentTime=System.nanoTime();
		  
		  delta += (currentTime - lastTime) /drawInterval;
		  timer += (currentTime - lastTime);
		  lastTime = currentTime;
		  
		  
		  if(delta>=1) {
			  update();
			  drawToTempScreen(); //draw everything to the bufferedImage
			  drawToScreen(); //draw the buffered
			  delta--;
			  drawCount++;
		  }
		  
		  if(timer>=1000000000) {
			  //System.out.println("FPS is: " + drawCount);
			  drawCount=0;
			  timer=0;
		  }
		 
	  
	  }
	  
	  }
	  
	  
	 
	
	
	public void update() {
		if(gameState==playState) {
			//Player
			player.update();
			
			//NPC
			for(int i =0;i< npc[1].length;i++) {
				
				if(npc[currentMap][i] != null) {
					npc[currentMap][i].update();
				}
			}
			
			for(int i = 0 ;i<monster[1].length;i++) {
				if(monster[currentMap][i]!=null) {
					if(monster[currentMap][i].alive ==true && monster[currentMap][i].dying == false) {
						monster[currentMap][i].update();
					}
					if(monster[currentMap][i].alive ==false) {
						monster[currentMap][i].checkDrop();
						monster[currentMap][i] =null;
					}
					
				}
			}
			
			for(int i = 0 ;i<projectileList[1].length ;i++) {
				if(projectileList[currentMap][i]!=null) {
					if(projectileList[currentMap][i].alive ==true) {
						projectileList[currentMap][i].update();
					}
					if(projectileList[currentMap][i].alive ==false) {
						projectileList[currentMap][i] = null;
					}
					
				}
			}
			
			for(int i = 0 ;i<particleList.size() ;i++) {
				if(particleList.get(i)!=null) {
					if(particleList.get(i).alive ==true) {
						particleList.get(i).update();
					}
					if(particleList.get(i).alive ==false) {
						particleList.remove(i);
					}
					
				}
			}
			
		}
		for(int i = 0;i<iTile[1].length;i++) {
			if(iTile[currentMap][i]!=null) {
				iTile[currentMap][i].update();
			}
			eManager.update();
		}
		
		
		if(gameState==pauseState) {
			//nothing
			
		}
		
	}
	
	public void drawToTempScreen () {
		long drawStart = 0;
		if(keyH.showDebugText ==true) {
			drawStart = System.nanoTime();
		}
		
		if(gameState == titleState) {
			ui.draw(g2);
			
		}
		
		//Map Screen
		else if(gameState == mapState) {
			map.drawFullMapScreen(g2);
		}
		else {
			
		
		
		//Tile Draw
		tileM.draw(g2);
	
		//draw interactive tile
		for(int i =0 ;i<iTile[1].length;i++) {
			if(iTile[currentMap][i]!=null) {
				iTile[currentMap][i].draw(g2);
			}
		}
		//add entity to list
		entityList.add(player);
		
		for(int i=0;i<npc[1].length;i++) {
			if(npc[currentMap][i]!=null) {
				entityList.add(npc[currentMap][i]);
			}
		}
		
		for(int i = 0;i<obj[1].length;i++) {
			if(obj[currentMap][i]!=null) {
				entityList.add(obj[currentMap][i]);
			}
		}
		
		for(int i = 0;i<monster[1].length;i++) {
			if(monster[currentMap][i]!=null) {
				entityList.add(monster[currentMap][i]);
			}
		}
		
		for(int i = 0;i<projectileList[1].length;i++) {
			if(projectileList[currentMap][i]!=null) {
				entityList.add(projectileList[currentMap][i]);
			}
		}
		
		for(int i = 0;i<particleList.size();i++) {
			if(particleList.get(i)!=null) {
				entityList.add(particleList.get(i));
			}
		}
		
		Collections.sort(entityList , new Comparator<Entity>() {
			public int compare(Entity e1 ,Entity e2) {
				int result = Integer.compare(e1.worldY,e2.worldY); 
				
				return result;
			}
		});
		
		
		//Draw Entities
		for(int i = 0;i<entityList.size();i++) {
			entityList.get(i).draw(g2);
		}
		
		//reset list after drawing
		entityList.clear();
		
		//ENV
		eManager.draw(g2);
		
		//Minimap
		map.drawMiniMap(g2);
		
		//CutScene
		csManager.draw(g2);
		
		//UI
		ui.draw(g2);
		
		}
		
		if(keyH.showDebugText ==true) {
			long drawEnd = System.nanoTime();
			long passed = drawEnd-drawStart;
			
			g2.setFont(new Font("Arial", Font.PLAIN, 20));
			g2.setColor(Color.white);
			int x = 10;
			int y = 400;
			int lineheight = 20;			
			
			g2.drawString("WorldX: "+player.worldX , x , y); y+=lineheight;
			g2.drawString("WorldY: "+player.worldY, x , y);y+=lineheight;
			g2.drawString("Col: "+ (player.worldX +  player.solidArea.x )/tileSize , x , y);y+=lineheight;
			g2.drawString("Row: "+  (player.worldY +  player.solidArea.y )/tileSize  , x , y);y+=lineheight;
			g2.drawString("Draw Time: " +passed , x , y); y+=lineheight;
			g2.drawString("God Mode: " +keyH.godModOn , x , y);	
		}
		

	}
	
	public void drawToScreen() {
		Graphics g = getGraphics();
		g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
		g.dispose();
		
	}

	public void playerMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}
	
	public void stopMusic() {
		music.stop();
	}
	
	
	public void playSE(int i) {
		se.setFile(i);
		se.play();
		
	}
	
	public void changeArea() {
		if(nextArea != currentArea) {
			stopMusic();
			
			if(nextArea == outside) {
				playerMusic(0);
			}
			
			if(nextArea == indoor) {
				playerMusic(17);
			}
			
			if(nextArea == dungeon) {
				playerMusic(18);
			}
			
			aSetter.setNPC();
		}
		
		currentArea = nextArea;
		aSetter.setMonster();
	}
	
	public void removeTempEntity() {
		for(int mapNum = 0; mapNum < maxMap; mapNum ++) {
			
			for(int i = 0; i< obj[1].length; i++) {
					if(obj[mapNum][i] !=null
					&& obj[mapNum][i].temp == true ) {
						obj[mapNum][i] =null;
						
						
					}
			}
		}
		
	}
}
