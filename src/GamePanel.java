import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 400;
	static final int SCREEN_HEIGHT = 660;
	static final int UNIT_SIZE = 20;
	static int MOVE_UNIT = 1;
	static final int UNITX = SCREEN_WIDTH/UNIT_SIZE;
	static final int UNITY = SCREEN_HEIGHT/UNIT_SIZE;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static final int DELAY = 10;
	static int[] x = new int[6];
	static int[] y = new int[6];
	static int[] blastX = new int[(int)UNITY*2];
	static int[] blastY = new int[(int)UNITY*2];
	int score;
	int High_score;
	int vertical;
	int[] obstacleX = new int[(int)(GAME_UNITS/2)];
	int[] obstacleY = new int[(int)(GAME_UNITS/2)];
	int gen_obstacle = 0;
	int j = 0, k = 0;
	int Dificulty = 0;

	Clip clip;
	File[] music = new File[5];{
		music[0] = new File("audio1.wav");
		music[1] = new File("audio2.wav");
		music[2] = new File("audio3.wav");
		music[3] = new File("audio4.wav");
		music[4] = new File("crash.wav");
	}

	boolean running = false;
	boolean paused = false;
	boolean game_over = false;
	Timer timer = new Timer(DELAY,this);
	Random random;

	JButton easy, midium, hard, pause;

    GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		this.setLayout(null);
		startGame();
			//return;
	}

	public void select_difficulty()	{
		easy = new JButton("EASY");
		easy.setBounds(50, 275, 75, 75); 
        //easy.setPreferredSize(new Dimension(100,100));
		easy.addActionListener(this);
		easy.setFocusable(false);
		easy.setIcon(new ImageIcon("asteroid.png"));
		easy.setHorizontalTextPosition(JButton.CENTER);
		easy.setVerticalTextPosition(JButton.BOTTOM);
		easy.setFont(new Font("Comic Sans",Font.BOLD,15));
		easy.setIconTextGap(-10);
		easy.setForeground(Color.cyan);
		easy.setBackground(Color.lightGray);
		easy.setBorder(BorderFactory.createEtchedBorder());

		midium = new JButton("MIDIUM");
		midium.setBounds(150+12, 275, 75, 75); 
        //midium.setPreferredSize(new Dimension(100,100));
		midium.addActionListener(this);
		midium.setFocusable(false);
		midium.setIcon(new ImageIcon("asteroid.png"));
		midium.setHorizontalTextPosition(JButton.CENTER);
		midium.setVerticalTextPosition(JButton.BOTTOM);
		midium.setFont(new Font("Comic Sans",Font.BOLD,15));
		midium.setIconTextGap(-10);
		midium.setForeground(Color.orange);
		midium.setBackground(Color.lightGray);
		midium.setBorder(BorderFactory.createEtchedBorder()); 

		hard = new JButton("HARD");
		hard.setBounds(250+25, 275, 75, 75); 
        //hard.setPreferredSize(new Dimension(100,100));
		hard.addActionListener(this);
		hard.setFocusable(false);
		hard.setIcon(new ImageIcon("asteroid.png"));
		hard.setHorizontalTextPosition(JButton.CENTER);
		hard.setVerticalTextPosition(JButton.BOTTOM);
		hard.setFont(new Font("Comic Sans",Font.BOLD,15));
		hard.setIconTextGap(-10);
		hard.setForeground(Color.red);
		hard.setBackground(Color.lightGray);
		hard.setBorder(BorderFactory.createEtchedBorder());

    	this.add(easy);
		this.add(midium);
		this.add(hard);
		
	}
    public void startGame() {
		paused = false;
		Dificulty = 0;
		score = 0;
		select_difficulty();
		pause_button();
		set_positions();
		play_game_music();
		running = true;
		timer.start();
	}
	public void set_positions(){
		x[0] = SCREEN_WIDTH/2;
		x[1] = SCREEN_WIDTH/2-UNIT_SIZE;
		x[2] = SCREEN_WIDTH/2;
		x[3] = SCREEN_WIDTH/2+UNIT_SIZE;
		x[4] = SCREEN_WIDTH/2-UNIT_SIZE;
		x[5] = SCREEN_WIDTH/2+UNIT_SIZE;

		y[0] = SCREEN_HEIGHT-5*UNIT_SIZE;
		y[1] = SCREEN_HEIGHT-4*UNIT_SIZE;
		y[2] = SCREEN_HEIGHT-4*UNIT_SIZE;
		y[3] = SCREEN_HEIGHT-4*UNIT_SIZE;
		y[4] = SCREEN_HEIGHT-3*UNIT_SIZE;
		y[5] = SCREEN_HEIGHT-3*UNIT_SIZE;
		
		for(int i = 0; i<obstacleX.length; i++) obstacleX[i] = -1;
		for(int i = 0; i<obstacleY.length; i++) obstacleY[i] = -1;
		for(int i = 0; i<blastY.length; i++) blastX[i] = -2;
		for(int i = 0; i<blastY.length; i++) blastY[i] = -2;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		
	}
	public void draw(Graphics g) {
		
		if(running) {
			
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
            for(int i=0;i<SCREEN_WIDTH/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				
			}
            //g.setColor(Color.red);
			
			for(int i = 0; i<obstacleX.length; i++){
				g.setColor(new Color(random.nextInt(250),random.nextInt(250),random.nextInt(250)));
				if(obstacleX[i]!=-1){
					g.fillOval(obstacleX[i], obstacleY[i], UNIT_SIZE, UNIT_SIZE);
				}
			}

			g.setColor(new Color(0xFFFF25));
			for(int i = 0; i<blastX.length; i++){
				//g.setColor(new Color(random.nextInt(250),random.nextInt(250),random.nextInt(250)));
				if(blastX[i]!=-2){
					g.fillRect(blastX[i]+UNIT_SIZE/4, blastY[i], (int)UNIT_SIZE/2, UNIT_SIZE);
				}
			}

            g.setColor(new Color(0x1E8449));
			for(int i = 0; i<x.length; i++){
				//g.setColor(new Color(random.nextInt(250),random.nextInt(250),random.nextInt(250)));
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
			
			g.setColor(new Color(0xBB8FCE));
			g.setFont( new Font("Ink Free",Font.BOLD, 20));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+score, (SCREEN_WIDTH - metrics.stringWidth("Score: "+score))/2, g.getFont().getSize());

			
		}
		else {
			gameOver(g);
		}
		
	}
	public void newObstacle() {
		if(random.nextInt(10)<Dificulty){
			obstacleY[j] = 0;
			obstacleX[j] = random.nextInt(UNITX)*UNIT_SIZE;
			j++;
			if(j>obstacleX.length-1) j=0;
			//System.out.println(j);
		}
		gen_obstacle = 0;
	}
	public void pew_pew() {
		blastX[k] = x[0];
		blastY[k] = y[0];
		//System.out.println(blastX[k]+" "+blastY[k]+" "+k+" "+x[0]+" "+y[0]);
		k++;
		if(k>blastX.length-1) k=0;
	}
	public void checkHigh_score() {
		File Hscore = new File("H_score.txt");
        try {
            Scanner s = new Scanner(Hscore);
        	High_score = s.nextInt();
			High_score = Math.max(High_score, score);
            s.close();

        } catch (FileNotFoundException e) {e.printStackTrace();}
		try {
            FileWriter writer = new FileWriter(Hscore);
            writer.write(High_score+"");
            writer.close();
        } 
        catch (IOException e) {e.printStackTrace();}
	}
	public void move() {
		for(int i = 0; i<obstacleX.length; i++) {
			if(obstacleX[i]!=-1){
				obstacleY[i]+=MOVE_UNIT;
			}
			if(obstacleY[i] == SCREEN_HEIGHT) {
				obstacleX[i] = -1;
				score += Dificulty;
			}
		}

		for(int i = 0; i<blastX.length; i++){
			if(blastX[i]!=-2){
				blastY[i]-=MOVE_UNIT*2;
			}
			if(blastY[i] == 0) {
				blastX[i] = -2;
				//score += Dificulty;
			}
		}
		//System.out.println(blastY[0]);
	}	
	public void checkCollisions() {
		for(int i = 0; i<x.length; i++){
			for(int q = 0; q<obstacleX.length;q++){
				if(x[i]==obstacleX[q]&&(y[i]<=obstacleY[q]+UNIT_SIZE&&y[i]>=obstacleY[q])){
					running = false;
				}
			}
		}
		for(int i = 0; i<blastX.length; i++){
			for(int q = 0; q<obstacleX.length; q++){
				if(blastX[i]==obstacleX[q]&&(blastY[i]<=obstacleY[q]+UNIT_SIZE && blastY[i]>=obstacleY[q])){
					obstacleX[q] = -1;
					blastX[i] = -2;
					score = Math.max(0,score-10*Dificulty);
					//System.out.println(q);
				}
			}
		}
		if(!running){
			timer.stop();
		}
	}
	public void play_game_music() {
		int i = random.nextInt(4);
		//System.out.println(i);
		try {
            AudioInputStream ai = AudioSystem.getAudioInputStream(music[i]);
            clip = AudioSystem.getClip();
            clip.open(ai);
            clip.loop(clip.LOOP_CONTINUOUSLY);
            clip.start();
        } 
        catch (Exception e) {e.printStackTrace();}  
	}
	public void play_crash_music() {
        
        try {
            AudioInputStream ai = AudioSystem.getAudioInputStream(music[4]);
            clip = AudioSystem.getClip();
            clip.open(ai);
            clip.start();
        } 
        catch (Exception e) {e.printStackTrace();}  
	}
	public void gameOver(Graphics g) {
		pause.setIcon(new ImageIcon("replay.png"));
		clip.stop();
		clip.close();
		play_crash_music();
		checkHigh_score();
		//Score
		g.setColor(new Color(0xBB8FCE));
		g.setFont( new Font("Ink Free",Font.BOLD, 25));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		vertical = g.getFont().getSize()+20; 
		g.drawString("Your Score: "+score, (SCREEN_WIDTH - metrics1.stringWidth("Your Score: "+score))/2, vertical);
		//Heigh Score
		g.setColor(new Color(0x1F618D));
		g.setFont( new Font("Ink Free",Font.BOLD, 30));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		vertical = vertical+g.getFont().getSize()+20;
		g.drawString("Heigh Score: "+High_score, (SCREEN_WIDTH - metrics3.stringWidth("Heigh Score: "+High_score))/2, vertical);
		//Game Over text
		g.setColor(new Color(0xA93226));
		g.setFont( new Font("Ink Free",Font.BOLD, 65));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		vertical = vertical+g.getFont().getSize()+20;
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, vertical);

		
		
		
	}
	
    public void pause_button(){
		pause = new JButton();
		pause.setBounds(20, 20, 40, 40); 
		pause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!running){
					clip.stop();
					clip.close();
					pause.setIcon(new ImageIcon("pause.png"));
					startGame();
					return;
				}
				else if(paused){
					pause.setIcon(new ImageIcon("pause.png"));
					timer.start();
					clip.start();
				}else{
					pause.setIcon(new ImageIcon("play.png"));
					timer.stop();
					clip.stop();
				}paused = !paused;
			}
			
		});
		pause.setFocusable(false);
		pause.setIcon(new ImageIcon("pause.png"));
		pause.setHorizontalTextPosition(JButton.CENTER);
		pause.setVerticalTextPosition(JButton.BOTTOM);
		pause.setBackground(Color.lightGray);
		pause.setBorder(BorderFactory.createEtchedBorder());

		this.add(pause);
	}
	@Override
    public void actionPerformed(ActionEvent e) {
		if(e.getSource()==easy){Dificulty = 3;}
        if(e.getSource()==midium){Dificulty = 5;}
        if(e.getSource()==hard){Dificulty = 10;}
		if(e.getSource()==easy||e.getSource()==midium||e.getSource()==hard){
			this.remove(easy);
			this.remove(midium);
			this.remove(hard);
		}
		
        if(running && !paused) {
			if(gen_obstacle++>=20) newObstacle();
			move();
			if(Dificulty!=0)score++;
			checkCollisions();
		}
		//System.out.println(running+" "+paused);
		//System.out.println(e.getSource());
		repaint();
    }
    public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if(!paused){
				switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if(x[1]==0||x[4]==0) break;
					for(int i = 0; i<6; i++){
						x[i]=Math.max(x[i]-UNIT_SIZE, 0);
					}
					break;
				case KeyEvent.VK_RIGHT:
					if(x[3]==SCREEN_WIDTH-UNIT_SIZE||x[5]==SCREEN_WIDTH-UNIT_SIZE) break;
					for(int i = 0; i<6; i++){
						x[i]+=UNIT_SIZE;
					}
					break;
				case KeyEvent.VK_UP:
					if(y[0]==0) break;
					for(int i = 0; i<6; i++){
						y[i]-=UNIT_SIZE;
					}
					break;
				case KeyEvent.VK_DOWN:
					if(y[4]==SCREEN_HEIGHT-UNIT_SIZE||y[5]==SCREEN_HEIGHT-UNIT_SIZE) break;
					for(int i = 0; i<6; i++){
						y[i]+=UNIT_SIZE;
					}
					break;
				case KeyEvent.VK_SPACE:
					pew_pew();
					break;
				}
			}
		}
	}
    
}
