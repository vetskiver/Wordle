# Wordle

## Basic Rules

The game plays as follows:

* An English five-letter word is selected at random: **the secret**.
* Players have *six* attempts to guess the secret.
* After each five-letter English word entered by the player, feedback regarding each letter is provided in the form
of a colored tile. 
Specifically, 
  * if the letter is in the secret word and in the correct spot, it is colored green
  * if the letter is in the secret word but not in the correct spot, it is colored yellow
  * if the letter is not in the secret word, it is colored gray

Note that the secret word and all of the player’s entered attempt must be actual, English dictionary words (ex: "ZXVQR"
is invalid and not an accepted attempt). Words may have duplicate letters.

## Basic Functionality

If a player word attempt is an invalid word, the row displaying it "wiggles" back and forth to indicate that there is a problem with the input. The letter themselves are not deleted or changed in any way.

## Advanced Features and Logic

### Invalid Inputs

If a player word attempt is an invalid word, the row displaying it "wiggles" back and forth to indicate that there is a problem with the input. The letter themselves are not deleted or changed in any way.

### Keyboard Coloring Priority

Coloring of virtual keyboard follows certain prioritization rules. If on the first guess a letter is in the correct position, both the letter on the monitor and keyboard are highlighted in **green**. If on the second guess that same letter is in the incorrect position, the letter on the monitor becomes **yellow** but ***stays*** **green** on the keyboard. 

### Duplicate Letter Handling

The web-based Wordle game has particular rules when evaluating words/guesses with duplicate letters. 

* For example, if the secret word is **BANAL**, and the player’s first guess is **ANNAL**, only the second ‘N’ will be denoted in **green** while the first ‘N’ will be denoted in **gray**. Also, the keyboard color for ‘N’ will be **green**. 
* If the player's second guess is **UNION**, the ‘N’ will be denoted in **yellow** since it is in the secret word **BANAL** but in an incorrect position. The second ‘N’ is in **gray** since there is no other ‘N’ in **BANAL**. Note the keyboard color would ***still*** be **green** since the player’s first guess **ANNAL** guessed a correct position for ‘N.’ 
* If a player’s third guess is **ALLOY**, the ‘A’ is in the secret word but in an incorrect position, so it is denoted in **yellow**. Note the keyboard color would ***still*** be **green** since the player’s first guess **ANNAL** guessed the correct position for ‘A.’ As there is only one ‘L’ in **BANAL** and two ‘L’s’ in **ALLOY**, the first ‘L’ takes precedence by virtue of being first. Note the keyboard color ***remains*** **green** since the player's first guess had ‘L’ at the correct position. 

<img width="258" alt="Screen Shot 2022-11-05 at 11 46 31 PM" src="https://user-images.githubusercontent.com/84925247/200172385-4d131870-2878-4ce9-b4bd-008d6793ae85.png">

<img width="635" alt="Screen Shot 2022-11-05 at 11 46 39 PM" src="https://user-images.githubusercontent.com/84925247/200172348-6c5bd921-aa07-4e20-a97c-0a46af35f1db.png">

