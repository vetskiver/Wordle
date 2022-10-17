import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import java.io.*;


//Handles all of the graphics/UI logic for the Wordle game
public class WordleView extends JComponent implements KeyListener, MouseListener {
	

	//*************     FUNCTIONS FOR STUDENT USE IN GameLogic.java    **************
	

	//Draws the argument letter in the cell at the specified row and column.
	//An EMPTY_CHAR argument value erases letter in the specified cell.
	public static void setCellLetter(int cellRow, int cellCol, char letter){
		ensureValidRowCol(cellRow, cellCol);
		
		if (Character.isLetter(letter)) { 
			letter = Character.toUpperCase(letter);
			cells[cellRow][cellCol].setLetter(letter);
		} else if (letter == EMPTY_CHAR) {
			cells[cellRow][cellCol].setLetter(EMPTY_CHAR);
		} else {
				System.out.println("Not a valid input:---" + letter + "---");
		}
	}
	

	
	//Sets the background color of the cell at the specified row and column as
	//the argument Color
	public static void setCellColor(int cellRow, int cellCol, Color newColor){
		ensureValidRowCol(cellRow, cellCol);
		cells[cellRow][cellCol].setColor(newColor);
	}
	
	
	//Returns the color of the specified letter on the graphical keyboard interface.
	//The argument key is the letter of the key whose color is to be returned.
	public static Color getKeyboardColor(char letter){
		if (Character.isLetter(letter)) {
		   letter = Character.toUpperCase(letter);
		   return keyboard.get(letter).getColor();
		}	
		else throw new IllegalArgumentException("Invalid Key specified: \"" + letter + "\"");

	}  
	
	
	//Sets the color of the specified key on the graphical keyboard interface.
	//The argument key is the letter of the key whose color is to be returned.
	public static void setKeyboardColor(char letter, Color newColor){
		if (Character.isLetter(letter)) {
			letter = Character.toUpperCase(letter);
			keyboard.get(letter).setColor(newColor);
		} 
		else throw new IllegalArgumentException("Invalid Key specified: \"" + letter + "\"");        
	}  
	
	
	//Called when the game is over in order to end the game.
	//Boolean argument indicates if player won (true) or lost (false)
	public static void gameOver(boolean didPlayerWin){
		if (didPlayerWin)
			isGameOver = GAMEOVER_WIN;
		else
			isGameOver = GAMEOVER_LOSE;
	}
	
	
	//Triggers "wiggle" animation (used for invalid or incompelete input) on the
	//specified row of cells.
	public static void wiggleRow(int row){
		//if there's already a row wiggling
		if (rowWiggling > -1)
			return;
		rowWiggling = row;          
	}	
	 
	
	//************************************************************************************      
	//**                                                                                **    
	//**                                                                                **    
	//**            STUDENTS DO NOT NEED TO READ ANY CODE BELOW THIS LINE               **
	//**                      (but feel free if you're curious!)                        **
	//**                                                                                **
	//**                                                                                **        
	//************************************************************************************    
	
	
	
	//*************     Constants    **************    
	//Dimensions of game window
	private static final int FRAME_WIDTH = 480;
	private static final int FRAME_HEIGHT = 780;    
	
	
	//Dimensions of the game: cell grid
	private static final int MAX_GUESSES = WordleLogic.MAX_ATTEMPTS; //number of rows
	private static final int WORD_LENGTH = WordleLogic.WORD_LENGTH; //number of cols
	
	public static final char EMPTY_CHAR = '\0';  //use to delete char
	
	
	//Dimensions of cells (square)
	private static final int CELL_SIZE = 50;
	//"Padding" space between cells
	private static final int CELL_PADDING = 10;    
	//Left and top Margins for upper-left most cell
	private static final int CELL_MARGIN_X = 95;
	private static final int CELL_MARGIN_Y = 25;
	//Thickness of outline drawn around the cell
	private static final int BORDER_THICKNESS = 2;    
	
	
	//Dimensions of keys on graphical keyboard interface
	private static final int KEYBOARD_KEY_WIDTH = 35;
	private static final int KEYBOARD_KEY_WIDTH_WIDE = 52;
	private static final int KEYBOARD_KEY_HEIGHT = 50;
	//"Padding" space between keys
	private static final int KEYBOARD_KEY_PADDING = 7;       
	//Letters for keys on the graphical keyboard interface
	private static final String LETTERS = "QWERTYUIOPASDFGHJKLZXCVBNM";
	//Number of keys per row for graphical keyboard interface
	private static final int[] KEYBOARD_KEYS_PER_ROW = {10, 9, 7};
	//Number of rows on the graphical keyboard interface
	private static final int KEYBOARD_ROWS = 3;
	//Left and top Margins for upper-left most key on keyboard
	private static final int[] KEYBOARD_MARGIN_X = {35, 55, 95};
	private static final int KEYBOARD_MARGIN_Y = MAX_GUESSES * (CELL_SIZE + CELL_PADDING) + 60;     
	
	
	//String values passed to GameLogic.keyPressed for the non-alpha keys
	private static final String ENTER_KEY_STRING = "ENTER";
	//private static final String BACKSPACE_KEY_STRING = "BACKSPACE";
	//Character drawn on the backspace key
	private static final String BACKSPACE_KEY_STRING = "⌫";
	
	
	//Governs the distance and numebr of times a row "wiggles" when animated
	private static final int[] WIGGLE_INTERVAL = {-1, 1, -1, 1, -1, 1, -1};
	private static final int[] WIGGLE_BOUND = {10, 20, 20, 20, 20, 20, 10};
	
	
	//Text to be displayed upon a game over (win/lose scenarios, lines 1 and 2)
	private static final String GAMEOVER_TEXT_WIN_L1 =    "You WIN";
	private static final String GAMEOVER_TEXT_WIN_L2 =    "Game Over!";    
	private static final String GAMEOVER_TEXT_LOSE_L1 = "Game Over!";
	private static final String GAMEOVER_TEXT_LOSE_L2 = "Your word was: "; 
	////Left and top Margins for upper-left corner of GameOver text
	private static final int GAMEOVER_TEXT_X = 150;
	private static final int GAMEOVER_TEXT_Y = KEYBOARD_MARGIN_Y + 215;    
	//Game over font colors for win/lost scenarios
	private static final Color GAMEOVER_WIN_COLOR = new Color(53, 209, 42);
	private static final Color GAMEOVER_LOSE_COLOR = new Color(189, 32, 15);    
	//Ints representing the three gameover states (game not yet over, player won, player lost)
	private static final int GAMEOVER_FALSE = -1;
	private static final int GAMEOVER_WIN = 0;
	private static final int GAMEOVER_LOSE = 1;    
	//Font attributes for the game over text
	private static final int GAMEOVER_FONT_SIZE = 35;  
	private static final Font GAMEOVER_FONT = new Font("Arial", Font.BOLD, GAMEOVER_FONT_SIZE); 
	
	
	//Text to be displayed in debug mode
	private static final String DEBUG_TEXT = "Debug Mode On, Word = ";       
	//Font attributes for the Debug text
	private static final int DEBUG_FONT_SIZE = 15;
	private static final Color DEBUG_FONT_COLOR = Color.ORANGE;    
	private static final Font DEBUG_FONT = new Font("Arial", Font.BOLD, DEBUG_FONT_SIZE); 
	////Left and top Margins for upper-left corner of debug text
	private static final int DEBUG_TEXT_X = 10;
	private static final int DEBUG_TEXT_Y = FRAME_HEIGHT-40;
	

	
	
	
	//*************     Class Variables    **************   
	
	//Instantion of a Wordle object, used to map the mouse and key listeners
	private static WordleView canvas = new WordleView();
	
	
	//2D array of all the of cell objects drawn on the screen (row x col)
	private static LetterCell[][] cells = new LetterCell[MAX_GUESSES][WORD_LENGTH];
	//Map of all the key objects on the graphical keyboard interface (keys are keyboard letters)
	private static HashMap<Character, KeyboardCell> keyboard;
	
	//Tracks whether or not the game is currently over, per the constants above
	private static int isGameOver = GAMEOVER_FALSE;
	
	//Window object containing game content
	private static JFrame window;
	
	//Tracks the secret word the user is trying to guess, generated by GameLogic
	private static String word;
	
	//Tracks whether a row is currently "wiggling" and where in the animation it is
	private static int rowWiggling = -1;
	private static int wiggleStep = 0;
	private static int wiggleCount = 0;
	
  public static void create(String secret){                
		initCells();
		initKeyboard();
		initWindow();
		
		word = secret;
		window.repaint();
	}
	
	
	
	
	//Initializes the game window
	private static void initWindow(){                
		window = new JFrame("jWordle!");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		//Wordle object needed for mouse/keyboard listeners
		//canvas = new WordleView();
		window.add(canvas);
		
		canvas.setBackground(Color.BLACK);
		window.getContentPane().setBackground(Color.BLACK);
		
		canvas.setOpaque(false);
		window.setVisible(true);
		window.setResizable(false);
		canvas.requestFocus();
		
		//Allows game to use mouse/keyPressed methods implemented in Wordle.java
		canvas.addKeyListener(canvas);
		canvas.addMouseListener(canvas);  
	}
	
	//Initializes the cells in the game windows (where the letters are typed/drawn)
	private static void initCells(){
		//Creates a 5 x 6 matrix of LetterCell objects
		for (int row = 0; row < cells.length; row++){      
			for (int col = 0; col <cells[0].length; col++){
				int x = CELL_MARGIN_X + (col * (CELL_SIZE + CELL_PADDING));
				int y = CELL_MARGIN_Y + (row * (CELL_SIZE + CELL_PADDING));
				cells[row][col] = new LetterCell(x, y);
				cells[row][col].setVisible(true);
				canvas.add(cells[row][col]);
			}
		}
	}
	
	//Initializes the "keys" drawn for the graphical keyboard interface
	private static void initKeyboard(){
		//Stores keys as KeyboardCell objects, kept in a map
		//Map keys is the letter of the respective key
		keyboard = new HashMap<Character, KeyboardCell>();
		int ct = 0;
		for (int row = 0; row < KEYBOARD_ROWS; row++){
			for (int key = 0; key < KEYBOARD_KEYS_PER_ROW[row]; key++){
				int x = KEYBOARD_MARGIN_X[row] + (key * (KEYBOARD_KEY_WIDTH + KEYBOARD_KEY_PADDING));
				int y = KEYBOARD_MARGIN_Y + (row * (KEYBOARD_KEY_HEIGHT + KEYBOARD_KEY_PADDING));
				KeyboardCell temp = new KeyboardCell(x, y, LETTERS.charAt(ct) + "");
				keyboard.put(LETTERS.charAt(ct), temp);
				ct++;
			}
		}
		
		//Create the Enter and Backspace keys separately, since they are sized differently
		//Enter
		KeyboardCell zKey = keyboard.get('Z'); //Enter goes to the left of the 'Z' key
		KeyboardCell enter = new KeyboardCell(
			(int)zKey.cell.getX() - (KEYBOARD_KEY_WIDTH_WIDE + KEYBOARD_KEY_PADDING), 
			(int)zKey.cell.getY(), ENTER_KEY_STRING, 
			KEYBOARD_KEY_WIDTH_WIDE, KEYBOARD_KEY_HEIGHT);
		keyboard.put('\n', enter);
		
		//Backspace
		KeyboardCell mKey = keyboard.get('M'); //Backspace goes to the rigth of the 'M' key
		KeyboardCell backspace = new KeyboardCell(
			(int)mKey.cell.getX() + (KEYBOARD_KEY_WIDTH + KEYBOARD_KEY_PADDING), 
			(int)mKey.cell.getY(), BACKSPACE_KEY_STRING, 
			KEYBOARD_KEY_WIDTH_WIDE, KEYBOARD_KEY_HEIGHT);
		keyboard.put(EMPTY_CHAR, backspace);
		
	}
	
	
	//Called everytime the game window is "repainted"
	public void paintComponent(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
		
		drawCells(g);
		drawKeyboard(g);
		
		//If the game is over, stop repainting
		if (isGameOver != GAMEOVER_FALSE)
			drawGameOverText(g);
		if (WordleLogic.DEBUG_MODE)
			drawDebugText(g);
		
		//if row is still wiggling, repaint again until the animation finishes
		if (rowWiggling > -1)
			repaint();
	}        
	
	
	//Paints the cells (containing the letters typed) to the window
	private void drawCells(Graphics g){
		for (int row = 0; row < cells.length; row++){      
			for (int col = 0; col <cells[0].length; col++){
				//if this row is wiggling, shift the cell either left or right slightly
				if (rowWiggling == row)
					cells[row][col].x += WIGGLE_INTERVAL[wiggleCount];
				cells[row][col].paintComponent(g);
			}
			//if this row is wiggling, update the wiggle trackers
			if (rowWiggling == row){
				wiggleStep++;
				if (wiggleStep >= WIGGLE_BOUND[wiggleCount]){
					wiggleStep = 0;
					wiggleCount++;
					if (wiggleCount >= WIGGLE_BOUND.length){
						wiggleCount = 0;
						rowWiggling = -1;
					}
				}
			}
		}    
	}
	
	
	//Paints the graphical keyboard interface to the game window
	private void drawKeyboard(Graphics g){
		//Get every value out of the keyboard map and paint it
		for (KeyboardCell key : keyboard.values()){
			key.paintComponent(g);            
		}
	}    
	
	//Paints the GameOver text to the window
	private void drawGameOverText(Graphics g){
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(GAMEOVER_FONT);
		String text1, text2;
		//Did the player win or lose?
		if (isGameOver == GAMEOVER_WIN) {
			g2d.setColor(GAMEOVER_WIN_COLOR);
			text1 = GAMEOVER_TEXT_WIN_L1;
			text2 = GAMEOVER_TEXT_WIN_L2;
		}
		else {
			g2d.setColor(GAMEOVER_LOSE_COLOR);  
			text1 = GAMEOVER_TEXT_LOSE_L1;
			text2 = GAMEOVER_TEXT_LOSE_L2 + word; 
		}    
		//Make sure we center the text using a FontMetrics object
		FontMetrics fm = g2d.getFontMetrics();
		int gameOverX = FRAME_WIDTH / 2 - fm.stringWidth(text1) /2;
		g2d.drawString(text1, gameOverX, GAMEOVER_TEXT_Y);  
		gameOverX = FRAME_WIDTH / 2 - fm.stringWidth(text2) /2;
		g2d.drawString(text2, gameOverX, GAMEOVER_TEXT_Y + fm.getHeight());      
	}
	
	//Paints the debug text to the window when running in debug mode
	private void drawDebugText(Graphics g){        
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(DEBUG_FONT);
		g2d.setColor(DEBUG_FONT_COLOR);
		//(include the secret word in the debug text)
		g2d.drawString(DEBUG_TEXT + word, DEBUG_TEXT_X, DEBUG_TEXT_Y);  
	}  
	
	
	private void keyProcess(int code) {

		if (code == KeyEvent.VK_ESCAPE) {
			System.out.println("Closing game");
			System.exit(0);
		} 
		
		//if the game is over, don't react to any key presses
		else if (isGameOver != GAMEOVER_FALSE)
			return;
		
		//deal with the enter/backspace keys
		else if (code == KeyEvent.VK_ENTER) {
			WordleLogic.checkLetters();
		}
		else if (code == KeyEvent.VK_BACK_SPACE) {
			WordleLogic.deleteLetter();
		}
		
		// check if alphabetic key
		else if (Character.isLetter(code)) {
			String keyText = KeyEvent.getKeyText(code);
			WordleLogic.inputLetter(keyText.toUpperCase().charAt(0));
		}
		
		//refresh the window in reaction to any key being pressed
		repaint();
	}
	
	//Called automatically whenever a key on the keyboard is pressed   
	public void keyPressed(KeyEvent event) {  
		//send the key event to process using its key code
		keyProcess(event.getKeyCode());
	}
	
	
	//Called automatically whenever the mouse is clicked inside the game window
	public void mousePressed(MouseEvent event) { 
		//if the game is over, don't react to any mouse presses
		if (isGameOver != GAMEOVER_FALSE)
			return;
		
		//Get the mouse click location
		int lastKnownMouseX = event.getX();
		int lastKnownMouseY = event.getY();
		
		//check if the user clicked on any of the graphical keyboard keys
		for (Character key : keyboard.keySet()){
			if (keyboard.get(key).contains(lastKnownMouseX, lastKnownMouseY)){
				
				if (key == '\n') {
					keyProcess(KeyEvent.VK_ENTER);
					return;
				} else if (key == EMPTY_CHAR) {
					keyProcess(KeyEvent.VK_BACK_SPACE);
					return;
					
				} else {
					keyProcess(KeyEvent.getExtendedKeyCodeForChar(key));
					return;
				}
			}
		}        
	}
	
	//Checks if row and column are within bounds of gameboard
	private static void ensureValidRowCol(int row, int col){        
		if (row >= MAX_GUESSES || row < 0 || col >= WORD_LENGTH || col < 0) 
			throw new IllegalArgumentException("Invalid cell specified, row: " 
				        + row + ", col: " + col);
	}
	
	
	//Abstraction of one cell on the game window (where the letters are drawn/colored)
	static class LetterCell extends JPanel{
		
		//Default cell colors
		private static final Color DEFAULT_BGCOLOR = Color.BLACK;
		private static final Color DEFAULT_OUTLINECOLOR = Color.GRAY;
		//Font attributes for letters drawn in cells
		private static final Color FONT_COLOR = Color.WHITE;
		private static final int LETTER_FONT_SIZE = 30;
		private static final Font LETTER_FONT = new Font("Arial", Font.BOLD, LETTER_FONT_SIZE); 
		
		//Cell colors
		private Color bgColor;
		private Color outlineColor;
		//Label being drawn in the cell
		private char letter;
		//Cell's location
		private int x, y;
		
		
		private LetterCell(int x, int y){
			bgColor = DEFAULT_BGCOLOR;
			outlineColor = DEFAULT_OUTLINECOLOR;
			this.x = x;
			this.y = y; 
			this.letter = EMPTY_CHAR;
		}
		
		//Changes the color of this cell 
		private void setColor(Color newColor){
			bgColor = newColor;
			outlineColor = newColor;
		}
		
		//Returns the current color of this cell
		private Color getColor(){
			return bgColor;
		}
		
		//Sets the letter drawn in this cell
		//(null = no letter drawn)
		private void setLetter(char letter){
			System.out.println("setLabel " + letter);
			this.letter = letter;
		}
		
		//Returns the letter currently being drawn in this cell
		private char getLetter(){
			return this.letter;
		}
		
		//Draws this cell to the game window
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			RectangularShape cell = new Rectangle2D.Double(this.x, this.y, 
				                                             CELL_SIZE, CELL_SIZE);
			g2d.setPaint(bgColor);
			g2d.fill(cell);
			g2d.setStroke(new BasicStroke(BORDER_THICKNESS));
			g2d.setPaint(outlineColor);
			g2d.draw(cell); 
			
			//if the cell has a letter in it, draw the letter
			if (letter != EMPTY_CHAR){
				g2d.setFont(LETTER_FONT);
				g2d.setColor(FONT_COLOR);  
				Rectangle2D textBounds = g2d.getFontMetrics().getStringBounds(letter+"", g);
				int fontX =  this.x + (CELL_SIZE / 2) - ((int)textBounds.getWidth() / 2);
				int fontY =  this.y + (CELL_SIZE / 2) + ((int)textBounds.getHeight() / 3);
        g2d.drawString(letter+"", fontX, fontY);

			}
		}          
		
	}
	
	//Abstraction of one key on the graphical keyboard interface
	//(that players can click on instead of typing)
	static class KeyboardCell extends JPanel{
		
		//Default background color for the keyboard keys
		private static final Color DEFAULT_BGCOLOR = new Color(160, 163, 168);
		//Attributes for font drawn ontop of the keyboard keys
		private static final Color FONT_COLOR = Color.WHITE;
		private static final int KEYBOARD_FONT_SIZE = 13;
		private static final Font KEYBOARD_FONT = new Font("Arial", Font.BOLD, KEYBOARD_FONT_SIZE); 
		
		//This key's current color
		private Color bgColor;
		//This key's currently displayed letter
		private String letter;
		//Rectangle representing the location and dimensions of the key
		private RectangularShape cell;
		
		//If key is using standard dimensions (ie, the non-enter/backspace keys)
		private KeyboardCell(int x, int y, String letter){
			this(x, y, letter, KEYBOARD_KEY_WIDTH, KEYBOARD_KEY_HEIGHT);
		}
		
		private KeyboardCell(int x, int y, String letter, int width, int height){
			bgColor = DEFAULT_BGCOLOR;
			this.letter = letter;
			cell = new RoundRectangle2D.Double(x, y, width, height, 5,5 );
		}
		
		//returns the current color of this key
		private Color getColor(){
			return bgColor;
		}
		
		//Changes the color of this key 
		private void setColor(Color newColor){
			bgColor = newColor;
		}      
		
		//Checks to see if the argument coordinate is located inside this
		//key (used to check mouse clicks)
		public boolean contains(int x, int y){
			return cell.contains(x, y);      
		}
		
		//Draws this key to the game window
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			//first draw the key...
			g2d.setPaint(bgColor);
			g2d.fill(cell);
			//then draw the letter ontop of it
			g2d.setFont(KEYBOARD_FONT);
			g2d.setColor(FONT_COLOR);  
			Rectangle2D textBounds = g2d.getFontMetrics().getStringBounds(letter, g);
			double fontX =  cell.getX() + (cell.getWidth() / 2.0) - (textBounds.getWidth() / 2.0);
			double fontY =  cell.getY() + (cell.getHeight() / 2.0) + (textBounds.getHeight() / 3.0);
			g2d.drawString(letter, (int)fontX, (int)fontY);
		}
	}          
	
	//These functions are required by various interfaces, but are not used
	public void mouseReleased(MouseEvent event) { }
	
	public void mouseClicked(MouseEvent event) { }
	
	public void mouseEntered(MouseEvent event) { }
	
	public void mouseExited(MouseEvent event) { }
	
	public void mouseMoved(MouseEvent event) { }
	
	public void keyReleased(KeyEvent event) { }
	
	public void keyTyped(KeyEvent event) {}    
	
}
