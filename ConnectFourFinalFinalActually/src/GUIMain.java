import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
public class GUIMain implements ActionListener {
	Board C4 = new Board();
	AI2 ai;
	JFrame frame = new JFrame("MCTS Connect Four");
	JPanel gamepanel = new JPanel();
	JPanel startpanel = new JPanel();
	JPanel intermediate = new JPanel();
	JPanel endpanel = new JPanel();
	JLabel[][] boardlabels = new JLabel[7][6];
	JLabel winner = new JLabel();
	JLabel title = new JLabel("Welcome to our Connect Four AI project!");
	JLabel whofirst = new JLabel("Who plays first?");
	JButton[] buttons = new JButton[7];
	JButton easy = new JButton("Easy");
	JButton medium = new JButton("Medium");
	JButton hard = new JButton("Hard");
	JButton cpu = new JButton("AI First");
	JButton human = new JButton("Player first");
	JButton end = new JButton("Back");
	int difficulty=0;
	boolean playeris1;
	Image red = new ImageIcon(this.getClass().getResource("/Circle2.png")).getImage();
	ImageIcon im2 = new ImageIcon(red);
	Image img = new ImageIcon(this.getClass().getResource("/CircleBlack.png")).getImage();
	ImageIcon im3 = new ImageIcon(img);
	
	
	public void begin() {
		
		frame.getContentPane().setLayout(new CardLayout(0,0));
		frame.getContentPane().add(gamepanel, "r");
		frame.getContentPane().add(startpanel, "r");
		frame.add(intermediate, "r");
		frame.add(endpanel,"r");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		gamepanel.setLayout(new GridLayout(7, 6));
		startpanel.setLayout(null); // null layouts lets us position more precisely and none of the standard layouts we know fit in with our goals
		intermediate.setLayout(null);
		endpanel.setLayout(null);
		startpanel.setVisible(true);
		gamepanel.setVisible(false);
		intermediate.setVisible(false);
		endpanel.setVisible(false);
		frame.setSize(800,800);
		startpanel.add(easy);
		startpanel.add(medium);
		startpanel.add(hard);
		startpanel.add(title);
		intermediate.add(cpu);
		intermediate.add(human);
		intermediate.add(whofirst);
		endpanel.add(winner);
		endpanel.add(end);
		title.setBounds(280, 50, 500, 300);
		easy.setBounds(50, 300, 200, 200);
		medium.setBounds(300, 300, 200, 200);
		hard.setBounds(550, 300, 200, 200);
		cpu.setBounds(50, 250, 300, 300);
		human.setBounds(450, 250, 300, 300);
		whofirst.setBounds(355, 150, 500, 15);
		winner.setBounds(375,325,500,15);
		end.setBounds(150, 365, 500, 70);
		for (int y=5; y>=0; y--) {
			for (int x=0; x<7; x++) {
				boardlabels[x][y] = new JLabel();
				boardlabels[x][y].setOpaque(true);
				gamepanel.add(boardlabels[x][y]);
			}
		}
		for (int i=0; i<7; i++) {
			buttons[i] = new JButton("" + (i+1));
			buttons[i].addActionListener(this);
			gamepanel.add(buttons[i]);
		}
		easy.addActionListener(this);
		medium.addActionListener(this);
		hard.addActionListener(this);
		cpu.addActionListener(this);
		human.addActionListener(this);
		end.addActionListener(this);
		frame.setVisible(true);
		frame.setLocation(560, 140);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		JButton a = (JButton) evt.getSource();
		if (a.equals(easy) || a.equals(hard) || a.equals(medium)) {
			C4.setBoard(new int[7][6]);
			boardUpdate();
			startpanel.setVisible(false);
			intermediate.setVisible(true);
			if(a.equals(easy)) difficulty = 13500;
			if(a.equals(medium)) difficulty = 40000;
			if(a.equals(hard)) difficulty = 75000;
			return;
		}
		if (a.equals(cpu)) {
			
			playeris1 = false;
			C4.setP1(playeris1);
			ai = new AI2(playeris1);
			intermediate.setVisible(false);
			gamepanel.setVisible(true);
			AImove();
			System.out.println(playeris1);
			boardUpdate();
			return;
		}
		if (a.equals(human)) {
			playeris1 = true;
			C4.setP1(playeris1);
			ai = new AI2(playeris1);
			intermediate.setVisible(false);
			gamepanel.setVisible(true);
			System.out.println(playeris1);
			return;
		}
		if (a.equals(end)) {
			endpanel.setVisible(false);
			startpanel.setVisible(true);
		}
		else {
			int x = Integer.parseInt(a.getText())-1;
			if(C4.legalMove(x)) {
				C4.makeMove(x);
				boardUpdate();
				AImove();
				boardUpdate();
			}
		}
	}
	
	void AImove() {
		int x = ai.MCTS(difficulty, C4);
		while(!C4.legalMove(x)){
			x = ai.MCTS(difficulty, C4);
		}
		C4.makeMove(x);
	}
	
	void boardUpdate() {
		if (C4.winner() != 0) {
			gamepanel.setVisible(false);
			endpanel.setVisible(true);
			if (C4.winner() == 1) winner.setText("You won!");
			if (C4.winner() == 2) winner.setText("AI won!");
			if (C4.winner() == -1) winner.setText("A tie!");
		}
		for(int y=0; y<6; y++) {
			for(int x=0; x<7; x++) {
				if (C4.getBoard()[x][y] == 1) boardlabels[x][y].setIcon(im2);
				if (C4.getBoard()[x][y] == 2) boardlabels[x][y].setIcon(im3);
				if (C4.getBoard()[x][y] == 0) boardlabels[x][y].setIcon(null);
			}
		}
	}
}
