Bot and wordle emulator for the popular game 'Wordle'. This bot uses hint based elimination, strategic ordering, and single letter entropy to eliminate words from the given wordpool. It takes an average of 5 guesses to correctly produce the correct 5-letter word from a 7,815 entry wordpool. Note that the official Wordle game has a wordpool of 2,309.

- norvig3, 200, 333k: Wordpools
- Submitbot.java: The actual bot, written by me.
- WordleGame.java: A wordle emulator, also written by me. Can work with any wordpool in the format of the norvig files.
- WordlePlayer.java: Interfaces the bot with the game. Provided as part of the assignment.
