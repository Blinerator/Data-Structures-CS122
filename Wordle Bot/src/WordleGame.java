import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * The puzzle game 'Wordle'!
 *
 * Basic idea:
 * the game loads in a set of 'known words' from a file or an array.
 * All 'known words' must have the same length (e.g., 5 characters)
 *
 * When the game starts, a secret word is chosen from the list of known words.
 * The player can then issue guesses.
 * Each guess returns a Hint object instance that tells:
 * - which characters in the guess are correct, and correctly located.
 * - which characters in the guess are in the secret word, but not at the location guessed
 * - which characters in the guess are not in the secret word
 *
 * If all characters in the guess are correctly located, the game is won ;)
 */
public class WordleGame {
    

    private ArrayList<String> knownWords = new ArrayList<String>();
    private String secretWord;
    private Random rnd = new Random(); // a source of random numbers

    /**
     * Create a new Wordle game, loading allowable words from a file.
     * See comments on loadWords() for the file format.
     *
     * @param file    - the file from which to load 'known words'
     * @param length  - the minimum length word
     * @param minfreq - the minimum allowed frequency
     * @param maxfreq - the maximum allowed frequency
     */
    public WordleGame(String file, int length, long minfreq, long maxfreq) throws IOException {
        // ~12K 5 letter words with min frequency of 100,000
        loadWords(file, length, minfreq, maxfreq);
    }

    /**
     * _Part 1: Implement this constructor._
     *
     * Load known words from the array that is supplied.  It's ok if the
     * array contains duplicates, but the constructor should verify
     * that all loaded strings are the same length and are lowercase.
     *
     * @param words - an array of words to load
     */
    public WordleGame(String[] words) {
        for(int i=0; i<words.length; i++){
            words[i]=words[i].toLowerCase();
                if(words[i].length()==words[0].length()){
                    knownWords.add(words[i]);
                }
            }
    }

    public int numberOfKnownWords() {
        return knownWords.size();
    }

    /**
     * _Part 2: Implement this method._
     *
     * Load words from a file. Each line of the file contains a word followed by one or more spaces followed
     * by a number whose value is higher for words that are more 'frequently occurring' than others.
     * Loaded words should have a frequency value in the range [minfreq, maxfreq]. However, at times, these
     * limits should be ignored (see comments below). Note that the words in the file may be many lengths
     * (e.g., 3-10 characters). Only words of the specified length should be loaded.
     *
     * Hint: use a Scanner instance to read in the file and look for the next String or Long value.
     * the following pattern will be useful ;)
     * Scanner scan = new Scanner(new File(filenm));
     *
     * The line above creates a Scanner from a File with the filename stored in the variable filenm
     * Use the scanner's .hasNext() method to test if there are more lines to read.
     * Use the scanner's .next() method to grab the next String token (word).
     * The scanner's .nextLong() method will grab the next token as a number (use this to read the frequency).
     *
     * For each line, you will want to call scan.next() and scan.nextLong() to read the data
     * identifying a word and its relative frequency.
     *
     * Words of the specified length should be added into the knownWords list if their frequencies
     * are the in the range specified.
     *
     * Hint: somewhere around 10-20 lines is probably appropriate here unless you have a lot of comments
     *
     * @param filenm  - the file name to load from
     * @param length  - the length of words we want to load (e.g., 5 to load 5 character words)
     * @param minfreq - the minimum allowable frequency for a loaded word
     * @param maxfreq - the maximum allowable frequenct for a loaded word; 0 indicates no maximum
     */
    public void loadWords(String filenm, int length, long minfreq, long maxfreq) throws IOException {
        Scanner scan = new Scanner(new File(filenm));
        if(maxfreq==0){maxfreq=Long.MAX_VALUE;}
        while(scan.hasNext()){
            String d=scan.next();
            long freq=scan.nextLong();
            if(d.length()==length && (freq<maxfreq && freq>minfreq)){
                knownWords.add(d);
//                System.out.println(d);
//                System.out.println(freq);
            }
        }
//        System.out.println(knownWords);
        return;
    }

    /**
     * _Part 3: Implement this method._
     *
     * Obtain a list of known words. This method creates a new copy of the known words list.
     * Here, you simply need to copy the knownWords list and return that copy.
     *
     * @return a new copy of list of known words.
     */
    public ArrayList<String> getKnownWords() {
        ArrayList<String> knownWordscopy = new ArrayList<String>();
        knownWordscopy=knownWords;
        return knownWordscopy;
    }

    /**
     * Prepare the game for playing by choosing a new secret word.
     */
    public void initGame() {
        Random r = new Random();
        secretWord = knownWords.get(r.nextInt(knownWords.size()));
        System.out.println(secretWord);
    }

    /**
     * Supply a guess and get a hint!
     *
     * Note that this implementation DOES NOT require that the guess be selected
     * from the known words. Rather, this implementation allows one to guess arbitrary
     * characters, so long as the guess is the same length as the secret word.
     *
     * @param g - the guess (a string which is the same length as the secret word)
     * @return a hint indicating the letters guessed correctly/incorrectly
     * @throws IllegalArgumentException if the guess is not the same length as the secret word
     */
    public Hint guess(String g) {
        int length = secretWord.length();
        if (length != g.length()) {
            throw new IllegalArgumentException("Wrong length guess!");
        }
        return new Hint(g, secretWord);

    }

}

/**
 * NOTE:
 *
 * We're breaking one of our initial rules about Java.
 * The truth is...more than one class CAN live in a file.
 *
 * However, only one public class can live in a file.
 * The Hint class below is not public.
 *
 * Style-wise, Hint would probably be best as a public class
 * because the intent is for it to be used widely. However,
 * that makes submission to autolab much more complicated.
 * And as a result, I have opted to keep it in the same file
 * as the Wordle class itself.
 */
class Hint {
    // why private?
    // because we want the hint to be generated by the constructor and then
    // not messed with further. So, we restrict access to these variables.
    private String correctlyPlaced;
    private String incorrectlyPlaced;
    private String notInPuzzle;
    private String guess1;

    /**
     * _Part 4: Implement this Constructor._
     *
     * Given a guess and the secret word, provide a hint.
     *
     * The Hint should follow these guidelines:
     * - the Hint should not store any knowledge of the secret word itself.
     * - correctlyPlaced should have a length equal to the length of the secret word
     * - incorrectlyPlaced should have a length equal to the length of the secret word
     * - notInPuzzle should have a length less than or equal to the length of the secret word/guess.
     *
     * A character in the guess that is both the correct letter, and in the correct location in the word
     * will appear in that location in the correctlyPlaced String. Characters in the guess that are not
     * correctly placed will appear as the char '-' in the correctlyPlaced String.
     *
     * Thus, for a guess "skate" and a secret word "score", the correctlyPlaced will be: "s---e"
     *
     * A character in the guess that is the correct letter, but not in the correct location in the word
     * will appear in the same location as the guess when placed in the incorrectedPlaced String.
     * Note that duplicate characters are a bit tricky, they must be examined for correctly placed characters
     * before incorrectly placed ones.
     *
     * Thus, for a guess "scoop" and a secret word "poofs", the correctlyPlaced String is: "--o--" and
     * incorrectlyPlaced String is: "s--op"
     * Note that the first 'o' is correctly placed, thus it must be the second 'o' that is incorrectly placed.
     * It is incorrect to say that the first 'o' is incorrectly placed, as it is incorrect to say that
     * both 'o's are incorrectly placed.
     *
     * Characters that are in the guess and are neither correctly placed nor incorrectly placed should be
     * added to the notInPuzzle String.  Thus, the length of the notInPuzzle String will never be greater
     * than the length of the secret word, or the guess.
     *
     * @param guess
     * @param secretWord
     */

    public Hint(String guess, String secretWord) {

        //This method keeps all strings as strings, instead of breaking up inputs into arrays and building char array outputs.
        //Using arrays would require many double 'for' loops, whereas keeping everything as a string makes the code easier to write and read, and removes the need for String.join()
        //to be used on each array at the end
        //The only exception to this is splitting up the input to find its length in the beginning.
        //Throughout the method, substrings are used heavily.  This was an attractive option for inserting chars as it's a simple one-line solution that's easy to read and write.

        //The second half of the code keeps track of what letters are available for 'correctlyPlaced' and 'incorrectlyPlaced'.
        //I first tried using an array to keep track of how many of each letter were present in 'secretWord', but this was confusing and required more 'for' loops
        //The final solution simply uses a string with all the available chars in it ('available_letters').  As chars are used up in 'correctlyPlaced' and 'incorrectlyPlaced',
        //they are removed from 'available_letters' until it is empty or there are no more available letters.  At this point any unused chars in 'guess' get moved into 'notInPuzzle'

        guess1=guess;
        incorrectlyPlaced=secretWord;
        correctlyPlaced=secretWord;
        notInPuzzle="";
        String available_letters=secretWord;
        String[] input=guess.split("");

        for(int i=0; i<input.length; i++){ //setting all variables to "-----" to save work later on
            incorrectlyPlaced=incorrectlyPlaced.substring(0,i) + "-" + incorrectlyPlaced.substring(i+1);
            correctlyPlaced=correctlyPlaced.substring(0,i) + "-" + correctlyPlaced.substring(i+1);
        }

        for(int i=0; i<input.length; i++){//if a char is in both 'guess' and 'secretWord', put it in incorrectly placed (for now)
            if(secretWord.contains(input[i])){
                incorrectlyPlaced=incorrectlyPlaced.substring(0,i) + input[i] + incorrectlyPlaced.substring(i+1);
            }
        }

        for(int i=0; i<input.length; i++){//setting correctlyPlaced and removing duplicates at the same index from incorrectlyPlaced
            if(input[i].charAt(0)==secretWord.charAt(i)){
                correctlyPlaced=correctlyPlaced.substring(0,i) + secretWord.charAt(i) + correctlyPlaced.substring(i+1);
                incorrectlyPlaced=incorrectlyPlaced.substring(0,i) + "-" + incorrectlyPlaced.substring(i+1);
            }
        }

        //the rest of the code deals with ensuring only the number of available letters are used across all outputs
        //The string 'available_letters' keeps track of what letters are still available to be used.  This string starts off
        //equal to 'secretWord', since that's how many available letters we start with.  Each of the following loops checks
        //'correctlyPlaced' and 'incorrectlyPlaced' and removes available letters from 'available_letters' if a letter is used up
        //any additional letters are put in 'notInPuzzle'.

        for(int i=0; i<input.length; i++){//this loop removes chars in 'correctlyPlaced' from 'available_letters'
            String testedchar=String.valueOf(correctlyPlaced.charAt(i));
            if(available_letters.contains(testedchar) && !testedchar.equals("-")){//if correctly placed used up some available letters, replace them with '-'
                available_letters=available_letters.substring(0,i) + "-" + available_letters.substring(i+1);
            }
        }

        for(int i=0; i<input.length; i++){//this loop removes chars in 'incorrectlyPlaced' from 'available_letters'
            String testedchar=String.valueOf(incorrectlyPlaced.charAt(i));
            if(available_letters.contains(testedchar) && !testedchar.equals("-")){
                int index=available_letters.indexOf(testedchar);
                available_letters=available_letters.substring(0,index) + "-" + available_letters.substring(index+1);
            }
            else if(!testedchar.equals("-")){//if a letter has already been used up, it is removed from 'incorrectlyPlaced'
                incorrectlyPlaced=incorrectlyPlaced.substring(0,i) + "-" + incorrectlyPlaced.substring(i+1);
            }
        }

        for(int i=0; i<input.length; i++){//this loop places chars without a "home" into 'notInPuzzle'
            if(correctlyPlaced.charAt(i)=='-' && incorrectlyPlaced.charAt(i)=='-'){
                notInPuzzle=notInPuzzle + guess.charAt(i);
            }
        }
    }

    public boolean isWin() {
        // true iff the '-' isn't in the correctlyPlaced String...
        return (correctlyPlaced.indexOf('-') == -1);
    }

    /**
     * Display a hint on System.out
     *
     * Given a secret word: 'state', and a guess 'scope' display:
     *
     * ---- Hint (scope) ----
     * Correctly placed  : s---e
     * Incorrectly placed: -----
     * Not in the puzzle : [cop]
     *
     * Given a secret word: 'state', and a guess 'sttae' display:
     *
     * ---- Hint (scope) ----
     * Correctly placed  : st--e
     * Incorrectly placed: --ta-
     * Not in the puzzle : []
     */
    public void write() {
        System.out.println("---- Hint (" + guess1 + ") ----");
        System.out.println("Correctly placed  : " + correctlyPlaced);
        System.out.println("Incorrectly placed: " + incorrectlyPlaced);
        System.out.println("Not in the puzzle : [" + notInPuzzle + "]");
    }

    /**
     * Note that we can return a reference to the correctlyPlaced String
     * safely since String's aren't immutable.  Thus, someone that messes
     * with the result we return won't actually impact the String
     * referenced by the Hint itself...
     *
     * @return the correctly placed portion of the hint
     */
    public String getCorrectlyPlaced() {
        return correctlyPlaced;
    }

    /**
     *
     * @return the incorrectly placed portion of the hint
     */
    public String getIncorrectlyPlaced() {
        return incorrectlyPlaced;
    }

    /**
     *
     * @return the not-in-puzzle portion of the hint
     */
    public String getNotInPuzzle() {
        return notInPuzzle;
    }
}
