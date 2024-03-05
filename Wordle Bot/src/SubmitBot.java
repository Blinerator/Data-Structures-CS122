import java.util.ArrayList;

//Ilya Cable
//
//Guesses are (mostly) found by using a high letter frequency model (we'll call this the "frequency" model).
//This model attempts to maximize the chances of getting a "correctly_placed" character.
//
//First, all the letters are counted, along with all the locations they occur.  E.g. with a five letter word pool, the letter 'a' may occur 500
//times, with 100 in position 1, 200 in pos. 2, etc...  We can use this to construct an array that looks like: [100, 200, 50, 50, 100].
//It's clear that the letter 'a' occurs most commonly in the second position.  This is done for each letter. Next, a string is constructed using
//the most commonly (or uncommonly, depending on user pref.) occuring letters. The algorithm attempts to put these letters in their most commonly
//occuring positions; in our previous example, 'a' would be placed at position 2, unless another letter had a higher frequency at position 2.
//This string is then compared to each word in the word pool, and the most similar word to it is used.  E.g. "attempt" might be chosen for a char
//string "alsempt".  This process is facilitated using common_word(), uncommon_word(), common_letters(), max_element_index(), and min_element_index().
//The purpose of this algorithm is to maximize the chances of getting a "correctly_placed" character, which is massively helpful in cutting down
//the word pool, more so than incorrectly placed or not in the puzzle (as far as I can tell).

//The second method of finding a guess uses entropy (unlike the letter frequency model, which only looks at the given word pool). This is the only
//part of the bot that uses entropy, and it is only implemented for one character at a time.
//Say we have a word pool with many similar words, and only one character is different between any two words: [tally, bally, rally, gally, dally].
//Instead of randomly picking a word, we pick whichever word has the most commonly occuring letter (in the english language).  So in this case we would pick "rally",
//since 'r' is the most commonly occuring character among the five differing characters (t,b,r,g,d).

//The second part of the code deals with the hint. The strategy is self-explanatory, but some clarifications are in order.  The method update_knownWords()
//is a recursive function (with a recursion limit) that removes invalid words based on the hint.  There is no concrete reason for it to be recursive; I simply wrote
//it that way and never changed it. After testing I found it misses some edge cases; remove_dupe_lett_words() takes care of these.  Edge case example: the key
//is "aunty" and the guess was "nanny", "fanny" wouldn't be removed from the word pool, even though it has too many 'n' chars.

//During normal gameplay (ignoring edge cases), the first guess is chosen using the frequency model.  The subsequent guesses vary from using a word with
//common/uncommon characters (frequency model) to using the entropy method, depending on the hints and the remaining word pool. Generally speaking, the frequency
//model will be used until the array has less than 10 elements, at which point the first element will be repetitively guessed until the key is found (this would
//be a great place to implement more entropy).  The specific "branches" the bot takes were obtained from repetitive testing, which means they are QUITE arbitrary.
//For example, if no char in the guess was part of the key, the bot will guess a word with uncommonly occuring letters. There is no empirical evidence to suggest
//this is a good strategy, but it seems to work reasonably well.

//I also wrote functionality to use any char string as a guess.  This simply uses the "common_letters()" string as the guess without finding a best-match
//word from the word pool first. I made this assuming another rule set would be added for extra credit, but it never happened.  It can be found under
//the second "________THIS IS NOT USED______________" marker.
public class SubmitBot implements WordlePlayer {
    ArrayList<String> knownWords;
    ArrayList<String> knownWords_foredit;
    int index=0;
    int guessNumber;
    String correct=null;
    String incorrectly_placed=null;
    String not_in_puzzle = null;
    String correct_raw=null;
    String incorrectly_placed_raw=null;
    String not_in_puzzle_raw = null;
    String not_in_puzzle_unique = null;
    int word_size=0;
    String guess="";
    String used_chars="";
    int knownWords_size;
    int iteration_num=0;
    char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    char[] order_of_commonality_letters = {'e','a','r','i','o','t','n','s','l','c','u','d','p','m','h','g','b','f','y','w','k','v','x','z','j','q'};
    int[][] word_letter_freq=new int[26][];
    int latch=0;
    int print_stuff;
    public SubmitBot() {
        print_stuff=0;
    }
    public void beginGame(Wordle game) {
        knownWords = game.getKnownWords();
        knownWords_foredit = (ArrayList<String>) knownWords.clone();
//        for(int i=0; i<knownWords.size(); i++){
//            knownWords_foredit.add(knownWords.get(i));
//        }
        word_size=knownWords.get(0).length();
        guessNumber = 0;
        knownWords_size=knownWords.size();
        for(int i=0; i<26; i++){
            word_letter_freq[i]=new int[word_size];//populating array with smaller arrays of size that the words are
        }
    }
    public boolean hasNextGuess() {
        if(knownWords_size==0){return false;}
        else return (guessNumber < knownWords_size);
    }
    public String nextGuess() {
        if(print_stuff==1) {
            System.out.println(correct_raw);
            System.out.println(incorrectly_placed_raw);
            System.out.println(not_in_puzzle_unique);
            System.out.println(not_in_puzzle);
        }
        if(word_size==1){//if word size is 1, just guess the first element until we run out of guesses
            guess=knownWords_foredit.get(0);
            knownWords_foredit.remove(0);
            return guess;
        }

        //__________THIS IS NOT USED__________
        int max_rand_guessNum=word_size+1;
        if(max_rand_guessNum>5){max_rand_guessNum=5;}
        //________________________________________

        if(knownWords_size==1){return knownWords.get(0);}//edge case where only 1 known word

        if(knownWords.size()>10){//if can use only words in knownWords and is big list
            if(correct_raw==null){//first guess is word composed of most common letters
                guess = common_word("HF");
            }
            else{
                //remove words based on hint
                int index = 0; //always start at index 0
                while (index < knownWords_foredit.size()){
                    iteration_num=0;
                    index=update_knownWords(index);//update_knownWords is recursive, but it has a recursion limit to prevent stack overflow.  So we need to call it multiple times.
                }
                remove_dupe_lett_words();//this fixes the edge cases mentioned in the introduction

                //the following conditions are semi-arbitrary, selected through repetitive testing.  This combination of conditions
                //were found to produce a reasonable result.
                int b = one_unknown_char();
                if(knownWords_foredit.size()==1){return knownWords_foredit.get(0);}

                else if(b != Integer.MAX_VALUE){guess = knownWords_foredit.get(maximize_word_entropy_oneChar(b));}//use entropy to find best guess among words with similar chars if applicable

                else if((not_in_puzzle.length()==word_size || correct.length()==0) && guessNumber<2){guess=uncommon_word();}//if there are no common letters in this word, it's probably a weird one!

                else if(correct.length()>word_size/2+2 && latch>2 || knownWords_foredit.size()<10){//once again, a pretty arbitrary branch
                    if(knownWords_foredit.size()==0){//check if we've run out of guesses yet.
                        guessNumber=knownWords_size+1;
                    }
                    else {
                        guess = knownWords_foredit.get(0);
                        knownWords_foredit.remove(0);
                    }
                }
                else{guess=common_word("LF");}//Use frequency method if no other branches were taken
            }
        }
        else if(knownWords.size()<10){//if we're given a small array

            if(correct_raw==null) {guess = common_word("LF");}//first guess uses frequency method

            else{//next guesses either guess the first element or use the entropy method where applicable
                int index = 0; //always start at index 0
                while (index < knownWords_foredit.size()){
                    iteration_num=0;
                    index=update_knownWords(index);
                }
                int b = one_unknown_char();
                if(b != Integer.MAX_VALUE){guess = knownWords_foredit.get(maximize_word_entropy_oneChar(b));}
                else{
                    guess = knownWords_foredit.get(0);
                    knownWords_foredit.remove(0);
                }
            }
        }

        //________THIS IS NOT USED______________
        if(1==0) {//if can use any char combination to guess.  Does not fit into the current ruleset!
            if (correct_raw == null) {
                guess = common_letters('M');
            } else {
                int index = 0;//always start at index 0
                while (index < knownWords_foredit.size()) {
                    iteration_num = 0;//this is to guarantee no stack overflow
                    index = update_knownWords(index);
                }
                if (knownWords_foredit.size() > 1 && guessNumber < max_rand_guessNum) {
                    guess = common_letters('M');
                }
                else {
                    guess = knownWords_foredit.get(0);
                    knownWords_foredit.remove(0);
                }
            }
        }
        //__________________________________________________

        if(print_stuff==1) {
            System.out.println("Common letters are: " + common_letters('M') + ".  \nUncommon letters are: " + common_letters('L') + ".");
            System.out.println("Guess number " + (guessNumber + 1) + " is: " + guess);
        }
        if(print_stuff==1 && knownWords_foredit.size()<25){System.out.println(knownWords_foredit);}

        guessNumber++;
        return guess;
    }
    public void tell(Hint h) {//update hint strings.  All tell() does is parse hint into a variety of useful forms.
        correct=h.getCorrectlyPlaced().replace("-","");
        incorrectly_placed=h.getIncorrectlyPlaced().replace("-","");
        not_in_puzzle = h.getNotInPuzzle().replace("-","");
        correct_raw=h.getCorrectlyPlaced();
        incorrectly_placed_raw=h.getIncorrectlyPlaced();
        not_in_puzzle_unique=not_in_puzzle;

        for(int i=0; i<incorrectly_placed.length(); i++){//for each char in incorrectly placed
            for(int j=0; j<not_in_puzzle_unique.length(); j++){//check if not in puzzle contains it
                if(incorrectly_placed.charAt(i)==not_in_puzzle_unique.charAt(j)){//if it is found, we delete it from not in puzzle
                    not_in_puzzle_unique=not_in_puzzle_unique.replace(String.valueOf(incorrectly_placed.charAt(i)),"-");
                }
            }
        }
        for(int i=0; i<correct.length(); i++){//check if there are duplicates between not in puzzle and correct
            for(int j=0; j<not_in_puzzle_unique.length(); j++){
                if(correct.charAt(i)==not_in_puzzle_unique.charAt(j)){
                    not_in_puzzle_unique=not_in_puzzle_unique.replace(String.valueOf(correct.charAt(i)),"-");
                }
            }
        }
        return;
    }
    public int update_knownWords(int index) {
        iteration_num++;
        if(iteration_num>50){return index;}//limits recursions
        int size = knownWords_foredit.size();

        if (index >= size) {
            return index;
        }

        //first we remove anything that doesn't match "correct"
        for (int i = 0; i < word_size; i++) {//check each word against "correct"
            if (index < knownWords_foredit.size()) {
                for (int w = 0; w < word_size; w++){
                    if (correct_raw.charAt(w) != knownWords_foredit.get(index).charAt(w) && correct_raw.charAt(w) != '-') {
                        knownWords_foredit.remove(index);
                        index = update_knownWords(index); //go again, starting at index (which is now a new value)
                        size = knownWords_foredit.size();
                        break;
                    }
                }
                if (index < knownWords_foredit.size()) {//next we remove anything that is in "not in puzzle" and is NOT in "incorrectly placed"
                    for (int j = 0; j < not_in_puzzle.length(); j++) {//for each letter in "not in p."
                        if (knownWords_foredit.get(index).contains(String.valueOf(not_in_puzzle_unique.charAt(j)))) {//if a char in word is found in not in puzzle unique
                            knownWords_foredit.remove(index);
                            index = update_knownWords(index);
                            size = knownWords_foredit.size();
                            break;
                        }
                    }
                    if (index < knownWords_foredit.size()) {
                        for (int k = 0; k < incorrectly_placed.length(); k++) {
                            if (!knownWords_foredit.get(index).contains(String.valueOf(incorrectly_placed.charAt(k)))) {//remove word if it doesn't contain chars in "incorrectly placed"
                                knownWords_foredit.remove(index);
                                index = update_knownWords(index);
                                size = knownWords_foredit.size();
                                break;
                            }
                        }
                    }
                }
            }
        }
        index++;
        size = knownWords_foredit.size();
        return index;
    }

    public void remove_dupe_lett_words(){
        //Fixes an edge case where if the key is "aunty" and the guess was "nanny",
        //"fanny" wouldn't be removed from the word pool, even though it doesn't meet the conditions set by Hint.

        //we first get a letter "budget" each word gets by counting letters in CP and IP
        int[] letter_budget = new int[26];

        for(int i=0; i<word_size; i++){//for each letter in CP & IP
            for(int j=0; j<26; j++){//compare against each letter in the alphabet
                if(correct_raw.charAt(i)==alphabet[j] || incorrectly_placed_raw.charAt(i)==alphabet[j]){letter_budget[j]++;}
            }
        }
        //now we make sure no words in the word pool violate the letter budget

        for(int i=0; i<knownWords_foredit.size(); i++){//count letters in each word
            int[] letter_num = new int[26];
            for(int w=0; w<26; w++){
                letter_num[w]=0;
            }
            for(int j=0; j<word_size; j++){
                for(int k=0; k<26; k++) {
                    if (knownWords_foredit.get(i).charAt(j) == alphabet[k]){letter_num[k]++;}
                }
            }
            //now we check if any of the letters violate the budget
            for(int k=0; k<26; k++){
                if(letter_num[k]>letter_budget[k] && not_in_puzzle.contains(String.valueOf(alphabet[k])) && letter_budget[k]!=0){
                    if(print_stuff==1){System.out.println("Dupe removed: "+knownWords_foredit.get(i)+" because it had too many of the letter: " + alphabet[k]+".  The budget for '"+ alphabet[k]+ "' is: "+letter_budget[k]+ " vs the word had: "+letter_num[k]);}
                    knownWords_foredit.remove(i);
                    break;
                }
            }
        }


    }

    public String common_word(String method) {//returns the word in knownWords that has the most *most* common chars
        if (method.equals("HF")) {
            String common_letters = common_letters('M');//we can choose if we want high frequency chars or low frequency chars
            String[] common_words = new String[knownWords.size()];//all words where common letters occur
            int[] num_of_common_letters = new int[knownWords.size()];//keeps track of how many common letters each common word has
            int index = 0;
            for (int j = 0; j < knownWords_foredit.size(); j++) {//check each word in known words to find one with most common letters
                for (int i = 0; i < word_size; i++) {//for each letter in common_letters
                    if (knownWords_foredit.get(j).contains(String.valueOf(common_letters.charAt(i)))) {//if a word contains
                        common_words[index] = knownWords_foredit.get(j);
                        num_of_common_letters[index]++;
                    }
                }
                if (num_of_common_letters[index] != 0) {
                    index++;
                } //index is only updated if we found a match
            }
            return common_words[max_element_index(num_of_common_letters)];//return the word with the most common letters in it
        }
        else{
            String common_letters = common_letters('M');
            String ret="";
            int index = 0;
            for (int j = knownWords_foredit.size()-1; j > 0; j--) {//check each word in known words to find one with most common letters
                int count=0;
                int prev_count=0;
                for (int i = 0; i < word_size; i++) {//for each letter in common_letters
                    if (knownWords_foredit.get(j).charAt(i)==common_letters.charAt(i)) {//if a word contains
                        count++;
                    }
                }
                if(count>prev_count){
                    prev_count=count;
                    ret=knownWords_foredit.get(j);
                }
            }
            return ret;//return the word with the most common letters in it
        }
    }

    public String uncommon_word(){//returns the word in knownWords that has the least common chars
        String uncommon_letters=common_letters('L');//we can choose high frequency chars or low frequency chars
        String[] uncommon_words = new String[knownWords.size()];//all words where uncommon letters occur
        int[] num_of_uncommon_letters = new int[knownWords.size()];//keeps track of how many uncommon letters each uncommon word has
        int index=0;
        for(int j=0; j<knownWords.size(); j++){//check each word in known words to find one with most uncommon letters
            for(int i=0; i<word_size; i++){//for each letter in uncommon_letters
                if(knownWords.get(j).contains(String.valueOf(uncommon_letters.charAt(i)))){//if a word contains
                    uncommon_words[index]=knownWords.get(j);
                    num_of_uncommon_letters[index]++;
                }
            }
            if(num_of_uncommon_letters[index]!=0){index++;} //index is only updated if we found a match
        }
        //System.out.println(uncommon_words[max_element_index(num_of_uncommon_letters)]);
        return uncommon_words[max_element_index(num_of_uncommon_letters)];//return the word with the most common letters in it
    }
    public String common_letters(char freq){
        //returns string of most common letters found in "knownWords"
        //there are many "for" loops in here...  A lot of room for optimization


        int[] letter_freq = new int[26];
        for(int i=0; i<knownWords_foredit.size(); i++){//for each word in known words
            String temp = knownWords_foredit.get(i);
            for(int j=0; j<temp.length(); j++){//for each letter in the word
                for(int k=0; k<26; k++){//for each alphabet letter
                    if(temp.charAt(j)==alphabet[k] && !used_chars.contains(String.valueOf(alphabet[k]))){
                        letter_freq[k]++;//k is the number in the alphabet-1; e.g. 'a' would be k=1
                        word_letter_freq[k][j]++;//update the coordinate of the letter
                    }
                }
            }
        }

        String common_letters="";
        for(int i=0; i<word_size; i++){common_letters=common_letters + "-";}

        if(freq=='M') {//use guess with most common letters
            while (common_letters.contains("-")) {
                int[] string_order = new int[word_size];//index of letter and where to put it
                int[][] letter_info=new int[word_size][word_size];//array of arrays for each letter and its information
                int[] letters=new int[word_size];
                for(int j=0; j<word_size; j++) {
                    int char_index = max_element_index(letter_freq);//find most common letter
                    int[] coords = word_letter_freq[char_index];//get its statistics

                    letters[j]=char_index;
                    letter_info[j]=coords;
                }

                for(int j=0; j<word_size; j++){string_order[j]=Integer.MAX_VALUE;}

                for(int j=0; j<word_size; j++){
                    int max_index = max_element_index(letter_info[j]);
                    if(string_order[max_index] == Integer.MAX_VALUE) {//if spot is open, put the char there
                        string_order[max_index] = letters[j];//index of letter and where to put it
                    }
                    else{
                        for(int k=0; k<word_size; k++){
                            if(string_order[k]==Integer.MAX_VALUE){
                                string_order[k]=letters[j];
                                break;
                            }
                        }
                    }
                }
                for(int k=0; k<word_size; k++){
                    common_letters=common_letters.substring(0,k) + alphabet[string_order[k]] + common_letters.substring(k+1);
                }
            }
        }
        else if(freq=='L') {//use guess with least common letters
            while (common_letters.contains("-")) {
                int[] string_order = new int[word_size];//index of letter and where to put it
                int[][] letter_info=new int[word_size][word_size];//array of arrays for each letter and its information
                int[] letters=new int[word_size];
                for(int j=0; j<word_size; j++) {
                    int char_index = min_element_index(letter_freq);//find most common letter
                    int[] coords = word_letter_freq[char_index];//get its statistics

                    letters[j]=char_index;
                    letter_info[j]=coords;
                }

                for(int j=0; j<word_size; j++){string_order[j]=Integer.MAX_VALUE;}

                for(int j=0; j<word_size; j++){
                    int min_index = min_element_index(letter_info[j]);
                    if(string_order[min_index] == Integer.MAX_VALUE) {//if spot is open, put the char there
                        string_order[min_index] = letters[j];//index of letter and where to put it
                    }
                    else{
                        for(int k=0; k<word_size; k++){
                            if(string_order[k]==Integer.MAX_VALUE){
                                string_order[k]=letters[j];
                                break;
                            }
                        }
                    }
                }
                for(int k=0; k<word_size; k++){
                    common_letters=common_letters.substring(0,k) + alphabet[string_order[k]] + common_letters.substring(k+1);
                }
            }
        }
        //System.out.println(common_letters);

        return common_letters;
    }

    public int max_element_index(int[] letter_freq){//given an array, will find the index of the biggest element
        int temp=letter_freq[0];
        int index_toreturn=0;
        for(int i=0; i<letter_freq.length; i++){//find the most frequent letter
            if(letter_freq[i]>temp){
                temp=letter_freq[i];
                index_toreturn=i; //index of largest number
            }
        }
        if(letter_freq.length==26) {
            letter_freq[index_toreturn] = 0;
        }
        return index_toreturn;
    }

    public int min_element_index(int[] letter_freq){//given an array, will find the index of the smallest element
        int temp= Integer.MAX_VALUE;
        int index_toreturn=0;
        for(int i=0; i<letter_freq.length; i++){//find the least frequent letter
            if(letter_freq[i]<temp && letter_freq[i]!=0){
                temp=letter_freq[i];
                index_toreturn=i; //index of smallest number
            }
        }
        if(letter_freq.length==26) {
            letter_freq[index_toreturn] = 0;
        }
        return index_toreturn;
    }

    public int maximize_word_entropy_oneChar(int index_to_check){//based on the most common chars in the english language, return the word that has the best char at index_to_check
        int best_word_index = 0;
        int best_letter_index=25;
        int[] letters = new int[knownWords_foredit.size()];
        //for each word in knownWords_foredit, check the specified index and find the word with the most commonly ocurring letter (maximize entropy)
        for(int i=0; i<knownWords_foredit.size(); i++){
            for(int j=0; j<26; j++){
                if(knownWords_foredit.get(i).charAt(index_to_check)==order_of_commonality_letters[j]){
                    letters[i]=j;
                }
            }
        }
        best_word_index = min_element_index(letters);
        return best_word_index;
    }

    public int one_unknown_char(){//checks if we're only missing one char from "correctly_placed"."  If so, return index of that char.  If not, return big number.  This is the condition for the entropy method.
        int count = 0;
        int index = 0;
        for(int i = 0; i<word_size; i++){
            if(correct_raw.charAt(i)=='-'){
                count++;
                index = i;
            }
        }
        if(count == 1){return index;}
        else return Integer.MAX_VALUE;
    }
}