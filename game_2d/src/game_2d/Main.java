package game_2d;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main {
	
	public static JFrame window;
	public static void main(String[] agrs) {
		
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("2d Adventure");
		//new Main().setIcon();
		
		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);
		
		if(gamePanel.fullScreenOn == true) {
			window.setUndecorated(true);
		}

		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		
		gamePanel.setupGame();
		gamePanel.StartGameThread();
			
	}
	
	public void setIcon() {
		ImageIcon  icon  = new ImageIcon(getClass().getClassLoader().getResource("game_2d/res/player/boy_down_1.png"));
		window.setIconImage(icon.getImage());
		
	}

}
