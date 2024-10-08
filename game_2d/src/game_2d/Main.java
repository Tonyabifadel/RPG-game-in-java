package game_2d;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] agrs) {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("2d Adventure");
		
		
		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);

		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.pack();
		gamePanel.setupGame();
		gamePanel.StartGameThread();
			
	}

}
