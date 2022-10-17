import java.awt.Color;
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;
import java.io.*;

public class WordleLogic {

    //Toggle DEBUG MODE On/Off
    public static final boolean DEBUG_MODE = true;
    //Toggle WARM_UP On/Off
    public static final boolean WARM_UP = false;

    private static final String FILENAME = "englishWords5.txt";
    // Number of words in the words.txt file
    private static final int WORDS_IN_FILE = 5758; // Review BJP 6.1 for  

    //Use for generating random numbers!
    private static final Random rand = new Random();

    public static final int MAX_ATTEMPTS = 6; //max number of attempts
    public static final int WORD_LENGTH = 5; //WORD_LENGTH-letter word 
    // as is 5 like wordle, could be changed

    private static final char EMPTY_CHAR = WordleView.EMPTY_CHAR; //use to delete char

    //************       Color Values       ************

    //Green (right letter in the right place)
    private static final Color CORRECT_COLOR = new Color(53, 209, 42);
    //Yellow (right letter in the wrong place)
    private static final Color WRONG_PLACE_COLOR = new Color(235, 216, 52);
    //Dark Gray (letter doesn't exist in the word)
    private static final Color WRONG_COLOR = Color.DARK_GRAY;
    //Light Gray (default keyboard key color, letter hasn't been checked yet)
    private static final Color NOT_CHECKED_COLOR = new Color(160, 163, 168);
    private static final Color DEFAULT_BGCOLOR = Color.BLACK;

    //***************************************************

    //************      Class variables     ************

    //Add them as necessary (I have some but less than 5)

    private static String secret = ""; // contains random secret word
    private static String guess = ""; // contains player's guess word, reset at each attempt
    private static int cellRow; // indicates current row number
    private static int cellCol; // indicates current column number
    private static String[] arr; // String array of all 5 letter words in text file
    private static String guessRight = ""; // contains all correctly placed letters 
    private static String guessYellow = ""; // contains all incorrectly placed letters

    //***************************************************

    //************      Class methods     ************

    // There are 6 already defined below, with 5 of them to be completed.
    // Add class helper methods as necessary. Our solution has 12 of them total.

    // Complete for 3.1.1

    public static void warmUp() {
        WordleView.setCellLetter(0, 0, 'c'); // sets char on game board
        WordleView.setCellColor(0, 0, CORRECT_COLOR); // colors char on game board
        WordleView.setKeyboardColor('c', CORRECT_COLOR);
        WordleView.getKeyboardColor('c');

        WordleView.setCellLetter(1, 2, 'o');
        WordleView.setCellColor(1, 2, WRONG_COLOR);

        WordleView.setCellLetter(3, 3, 's');
        WordleView.setKeyboardColor('s', WRONG_COLOR);
        WordleView.getKeyboardColor('s');

        WordleView.setCellLetter(5, 4, 'c');
        WordleView.setCellColor(5, 4, WRONG_PLACE_COLOR);
    }

    //This function gets called ONCE when the game is very first launched
    //before the player has the opportunity to do anything.
    //
    //Returns the chosen mystery word the user needs to guess
    public static String init() throws FileNotFoundException {
        int count = 0;
        File engWords = new File(FILENAME);
        Scanner scnr = new Scanner(engWords);
        arr = new String[WORDS_IN_FILE];

        while (scnr.hasNextLine()) {
            String line = scnr.nextLine();
            arr[count] = line;
            count++;
        }
        int randomNumber = rand.nextInt(arr.length);
        return arr[randomNumber];
    }

    //This function gets called everytime the user inputs 'Backspace'
    //pressing the physical or virtual keyboard.
    //call on Backspace input
    public static void deleteLetter() {
        if (DEBUG_MODE) {
            System.out.println("in deleteLetter()");
        }
        if (cellCol != 0) {
            cellCol--;
            guess = guess.substring(0, guess.length() - 1);
            WordleView.setCellLetter(cellRow, cellCol, EMPTY_CHAR);
        }
    }

    //This function gets called everytime the player inputs 'Enter'
    //pressing the physical or virtual keyboard.  
    public static void checkLetters() {
        char[] guessArr = new char[WORD_LENGTH]; // declares an array which contains the guess word
        char[] mysteryWordArr = new char[WORD_LENGTH]; // declares an array which contains the secret word
        mysteryWordArray(secret, mysteryWordArr); // creates an array for secret word

        if (DEBUG_MODE) {
            System.out.println("in checkLetters()");
        }
        if ((!invalidCheck(guess))) { // checks if word is valid/invalid
            guessArr = guessArr(guess, guessArr); // turns guess into array
            int match = 0;
            match = greenMatched(guessArr, mysteryWordArr, match); // obtains number of times guess array matches with secret array
            yellowMatched(guessArr, mysteryWordArr); // matches all incorrectly placed letters
            greyMatched(guessArr, mysteryWordArr); // matches all wrong letters
            if (match == 5) { // ends game if guess is equal to secret
                gameVerdict(true);
            } else if (cellRow >= MAX_ATTEMPTS - 1) {
                gameVerdict(false); // ends game if player runs out of attempts
            } else { // carries on game if neither conditions above are satisfied
                match = 0; // reset the count
                cellCol = 0; // reset the count 
                cellRow++; // goes down to next row
                cleanGuessArr(guessArr); // resets the guess array
                mysteryWordArray(secret, mysteryWordArr); // updates the secret array
                guess = ""; // resets the guess string
            }
        }
    }

    //This function gets called everytime the player types a valid letter
    //on the keyboard or clicks one of the letter keys on the 
    //graphical keyboard interface.  
    //The key pressed is passed in as a char uppercase for letter
    public static void inputLetter(char key) {
        //Some placeholder debugging code...
        System.out.println("Letter pressed!: " + key);
        /*	  	
         	if (WARM_UP) {
         		if (key == 'W') {
         			WordleView.wiggleRow(3);
         			System.out.println("A row should wiggle");  		
         		}
         	}
        */
        if ((cellCol < WORD_LENGTH) && (key != EMPTY_CHAR)) { // only "adds" in letters if cell column is not out of bounds and key is not empty char
            WordleView.setCellLetter(cellRow, cellCol, key);
            guess += key;
            cellCol++;
        }
    }

    // Helper Functions	

    // This function gets called everytime a player enters a valid word
    // on the keyboard. The function turns all matched letters to green
    // and updates the guessarray and mysterywordarray to ?
    // This function returns the amount of matches to determine if game should 
    // end/continue.
    public static int greenMatched(char[] guessArr, char[] mysteryWordArr, int match) {
        for (int i = 0; i < WORD_LENGTH; i++) { // iterates through the mysterywordarray and guessarray.
            if (mysteryWordArr[i] == '?') {
                continue;
            }
            if (guessArr[i] == Character.toUpperCase(mysteryWordArr[i])) { // if guessarray index is equal to mysterywordarray index, then turn green, and change current indexes to ?
                WordleView.setCellColor(cellRow, i, CORRECT_COLOR); // colors char on game board
                WordleView.setKeyboardColor(guessArr[i], CORRECT_COLOR); // colors char on keyboard
                guessRight += guessArr[i];
                mysteryWordArr[i] = '?';
                guessArr[i] = '?';
                match++; // counts up all matches
            }
        }
        return match;
    }

    // This function gets called everytime a player enters a valid word
    // on the keyboard. The function turns all incorrectly placed letters into 
    // yellow and updates the guessarray and mysterywordarray to ?.
    // Note if incorrectly placed letter was previously green, then keyboard stays green. 
    // This function also does not return anything, but instead updates the arrays.
    public static void yellowMatched(char[] guessArr, char[] mysteryWordArr) {
        for (int i = 0; i < WORD_LENGTH; i++) {
            for (int j = 0; j < WORD_LENGTH; j++) {
                if (mysteryWordArr[j] == '?' || guessArr[i] == '?') {
                    continue;
                }
                if ((guessArr[i] == Character.toUpperCase(mysteryWordArr[j]))) {
                    if (isIn(guessArr[i], guessRight)) {
                        WordleView.setCellColor(cellRow, i, WRONG_PLACE_COLOR);
                        WordleView.setKeyboardColor(guessArr[i], CORRECT_COLOR);
                        mysteryWordArr[j] = '?';
                        guessArr[i] = '?';
                    } else {
                        WordleView.setCellColor(cellRow, i, WRONG_PLACE_COLOR);
                        WordleView.setKeyboardColor(guessArr[i], WRONG_PLACE_COLOR);
                        guessYellow += guessArr[i];
                        mysteryWordArr[j] = '?';
                        guessArr[i] = '?';
                    }
                }
            }
        }
    }

    // This function gets called everytime a player enters a valid word
    // on the keyboard. The function turns all incorrect letters into 
    // yellow and updates the guessarray and mysterywordarray to ?.
    // grey. This function does not return anything.
    public static void greyMatched(char[] guessArr, char[] mysteryWordArr) {
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (guessArr[i] != '?') {
                if (isIn(guessArr[i], guessRight)) {
                    WordleView.setCellColor(cellRow, i, WRONG_COLOR);
                    WordleView.setKeyboardColor(guessArr[i], CORRECT_COLOR);
                } else if (isIn(guessArr[i], guessYellow)) {
                    WordleView.setCellColor(cellRow, i, WRONG_COLOR);
                    WordleView.setKeyboardColor(guessArr[i], WRONG_PLACE_COLOR);
                } else {
                    WordleView.setCellColor(cellRow, i, WRONG_COLOR);
                    WordleView.setKeyboardColor(guessArr[i], WRONG_COLOR);
                }
            }
        }
    }

    // This function checks to see if a letter was previously in a correct position
    // and returns a boolean value to determine whether keyboard color should remain 
    // or change. 
    public static boolean isIn(char key, String guessRight) {
        for (int k = 0; k < guessRight.length(); k++) { // traverses through the string guessright
            if (key == Character.toUpperCase(guessRight.charAt(k))) { // checks to see if key matches with a letter in guess right
                return true;
            }
        }
        return false;
    }

    // This function checks to see if a word entered by a player is valid/invalid
    // and it does this by returning a true or a false.
    public static boolean invalidCheck(String guess) {
        if (guess.length() != WORD_LENGTH) { // if the guess word is not equal to the word length required, return false
            WordleView.wiggleRow(cellRow);
            return true; // returns true if guess is not equal to 5
        }
        
        for (int i = 0; i < arr.length; i++) { // traverse through the array
            if (guess.equals(arr[i].toUpperCase())) {
                return false; // returns true if guess is equal to a valid word, then return false
            }
        }
        WordleView.wiggleRow(cellRow);
        return true;
    }

    // This function is called only when the game must end in winning or losing
    public static void gameVerdict(boolean verdict) {
        if (verdict == true) { // if the player wins, then displayer game won, else if the player loses, display game over
            WordleView.gameOver(true);
        } else {
            WordleView.gameOver(false);
        }
    }
    // This function is called anytime the game is continued and it resets the 
    // guess array to accommodate the player's new guess.
    public static void cleanGuessArr(char[] guessArr) {
        for (int i = 0; i < WORD_LENGTH; i++) { // traverse through the guessarray and modify each index value to empty character 
            guessArr[i] = ' '; // reset guess arr 
        }
    }

    // This function turns the guess string value to a guess Array in order to 
    // traverse through the array
    public static char[] guessArr(String guess, char[] guessArr) {
        if (!guess.isEmpty()) { // if the guess word is not empty, then traverse through the guess array and adds each letter
            for (int i = 0; i < WORD_LENGTH; i++) {
                guessArr[i] = guess.charAt(i);
            }
        }
        return guessArr;
    }

    // This function turns the secret string value to a mysteryword Array in order to 
    // traverse through the array
    public static void mysteryWordArray(String secret, char[] mysteryWordArr) {
        for (int i = 0; i < WORD_LENGTH; i++) { // traverse through the mysterywordarray and adds the secret word
            mysteryWordArr[i] = secret.charAt(i);
        }
    }

    //Initializes and launches the game logic and its GUI window
    public static void main(String[] args) throws FileNotFoundException {
        secret = init(); // generates a secret word		

        //Creates the game window
        WordleView.create(secret);

        if (WARM_UP) {
            warmUp();
        }
    }
}