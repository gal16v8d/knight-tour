package co.com.gsdd.knighttour;

import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import lombok.extern.slf4j.Slf4j;

/**
 * Knight tour
 * 
 * @(#)Main.java
 * @author Alexander Galvis
 * @version 1.00 2008/7/19
 */
@Slf4j
public class Main extends JFrame {

	private static final String SELECT_A_VALID_POS_0_7 = "Select a valid pos (0-7)";
	private static final String BACKGROUND_PNG = "/icons/background.png";
	private static final long serialVersionUID = 666570542107756566L;
	private JLabel backgroundLabel;
	private JButton solveButton;
	private JButton backButton;
	private JButton confirmButton;
	private JTextField xInput, yInput;
	private JTextArea infoArea;
	private int x;
	private int y;

	public Main(String title) {
		super(title);
		this.setLayout(null);
		initTextFields();
		backgroundLabel = new JLabel();
		infoArea = new JTextArea();
		infoArea.setVisible(false);
		infoArea.setEditable(false);
		initMenu();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(50, 50, 50, 50);
		this.setVisible(true);
	}

	private void initTextFields() {
		xInput = new JTextField();
		xInput.setBounds(520, 10, 20, 20);
		xInput.setToolTipText("Only numbers here");
		xInput.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent event) {
				chessPosInput(event, xInput);
			}
		});
		yInput = new JTextField();
		yInput.setBounds(520, 40, 20, 20);
		yInput.setToolTipText("Only numbers here");
		yInput.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent event) {
				chessPosInput(event, yInput);
			}
		});
	}

	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menuTour = new JMenu("Tour");
		JMenu extraMenu = new JMenu("Extras");
		JMenu menuOptions = new JMenu("Options");
		JMenuItem menuExit = initMenuItem("Exit", "/icons/iconout.png", 'S', KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
		JMenuItem menuInfo = initMenuItem("Info", "/icons/iconcred.png", 'C', KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);
		JMenuItem menuKnight = initMenuItem("Knight Tour", "/icons/b_knight.gif", 'K', KeyEvent.VK_K,
				InputEvent.CTRL_DOWN_MASK);
		extraMenu.add(menuInfo);
		extraMenu.addSeparator();
		extraMenu.add(menuExit);
		menuOptions.add(menuTour);
		menuTour.add(menuKnight);
		menuBar.add(menuOptions);
		menuBar.add(extraMenu);
		this.init(BACKGROUND_PNG);
		this.setJMenuBar(menuBar);
		menuTour.setToolTipText("Select a tour");
		menuExit.addActionListener(evt -> exitOption());
		menuInfo.addActionListener(evt -> infoOption());
		menuKnight.addActionListener(evt -> initTourPanel());
	}

	private ImageIcon safeLoadOfIcon(String resource) {
		ImageIcon icon = null;
		try {
			icon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream(resource)));
		} catch (Exception e) {
			log.error("Error loading the resource icon {}", resource, e);
		}
		return icon;
	}

	private JMenuItem initMenuItem(String menuName, String resource, char shortcut, int keyCode, int modifier) {
		JMenuItem menuItem = new JMenuItem(menuName, safeLoadOfIcon(resource));
		menuItem.setMnemonic(shortcut);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifier));
		return menuItem;
	}

	private void chessPosInput(KeyEvent event, JTextField input) {
		char c = event.getKeyChar();
		if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))
				|| (input.getText().length() > 0)) {
			getToolkit().beep();
			event.consume();
		}
	}

	private void solveOption() {
		Knight knight = new Knight(x, y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		knight.setSize(400, 400);
		knight.setVisible(true);
		knight.setResizable(false);
		getContentPane().add(knight);
		getContentPane().repaint();
		Thread t = new Thread(knight);
		t.start();
	}

	private void confirmOption(JButton confirmButton) {
		getContentPane().removeAll();
		initTourPanel();
		confirmButton.setEnabled(false);
		xInput.setEditable(false);
		yInput.setEditable(false);
		solveButton.setEnabled(true);
		getContentPane().repaint();
		validatePos();
	}

	private void validatePos() {
		String getX = xInput.getText();
		String getY = yInput.getText();
		if (getX == null || getX.isEmpty() || getY == null || getY.isEmpty()) {
			posNotValidAction();
		} else {
			x = Integer.parseInt(getX);
			y = Integer.parseInt(getY);
			if (!BoardValidation.isLegalSquare(x, y)) {
				posNotValidAction();
			}
		}
	}

	private void posNotValidAction() {
		JOptionPane.showMessageDialog(null, SELECT_A_VALID_POS_0_7);
		initTourPanel();
		getContentPane().repaint();
	}

	private void backOption() {
		getContentPane().removeAll();
		init(BACKGROUND_PNG);
		getContentPane().repaint();
	}

	private void exitOption() {
		int z = JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Do you want to exit?",
				JOptionPane.YES_NO_OPTION);
		if (z == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	private void infoOption() {
		infoArea.setVisible(true);
		infoArea.setText("Knight Tour - Teoria de Redes" + "\n" + " (c)2010-20?? - Alexander Galvis" + "\n");
		JOptionPane.showMessageDialog(null, infoArea, "Knight Tour", JOptionPane.INFORMATION_MESSAGE);
	}

	public void init(String backgroundImage) {
		try {
			ImageIcon background = safeLoadOfIcon(backgroundImage);
			backgroundLabel.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
			backgroundLabel.setIcon(background);
			getContentPane().add(backgroundLabel);
		} catch (Exception e) {
			log.error("Error loading the background from {}", backgroundImage, e);
		}
	}

	public void initTourPanel() {
		getContentPane().removeAll();
		JLabel xInputLabel = new JLabel("Init pos x:");
		xInputLabel.setBounds(420, 10, 100, 20);
		JLabel yInputLabel = new JLabel("Init pos y:");
		yInputLabel.setBounds(420, 40, 100, 20);
		solveButton = new JButton("Solve");
		solveButton.setBounds(420, 70, 100, 25);
		solveButton.setEnabled(false);
		solveButton.addActionListener(evt -> solveOption());
		backButton = new JButton("Back");
		backButton.setBounds(420, 100, 100, 25);
		backButton.addActionListener(evt -> backOption());
		confirmButton = new JButton("Confirm");
		confirmButton.setBounds(420, 130, 100, 25);
		confirmButton.setEnabled(true);
		confirmButton.addActionListener(evt -> confirmOption(confirmButton));
		xInput.setEditable(true);
		yInput.setEditable(true);
		getContentPane().add(xInputLabel);
		getContentPane().add(xInput);
		getContentPane().add(yInputLabel);
		getContentPane().add(yInput);
		getContentPane().add(solveButton);
		getContentPane().add(backButton);
		getContentPane().add(confirmButton);
		getContentPane().repaint();
	}

	public static void main(String[] args) throws IOException {
		SwingUtilities.invokeLater(() -> {
			Main main = new Main("Chess Tours");
			main.setSize(620, 500);
			main.setVisible(true);
			main.setResizable(false);
			main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		});
	}

}
