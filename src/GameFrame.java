import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame(){
			
		this.add(new GamePanel());
		this.setTitle("Astro Dash");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		ImageIcon icon = new ImageIcon("asteroid.png");
		this.setIconImage(icon.getImage());
		
	}
}
