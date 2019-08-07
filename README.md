This is a submission for Jane Street's July 2019 puzzle, found at: https://www.janestreet.com/puzzles/scroggle/. (They changed the puzzle name from Scroggle to Scraggle at some point.) The basic goal was to place a chain of four Scrabble words, such that the last letter of the first, second, and thid word began the next one, and to maximize the product of the four word's scores. Each letter may be placed at most once, and may be in multiple words. My submission tied for first overall at 824670 points.

The "scrabble.txt" file containing a list of scrabble words was taken from https://www.wordgamedictionary.com/twl06/, and is included with the project. This file contained words outside of Merriam-Webster's official list - since the puzzle required all placed words to come from Merriam-Webster's list, I removed any unofficial words I happened to encounter. (Later on, I actually got recently-updated official NASPA Word List from a friend. It's available to members only, so I haven't posted it here, but it's located at https://scrabbleplayers.org/w/NWL2018. I'm not sure if this is the same as Merriam Webster's list.) It doesn't seem like the Merriam Webster list is available anywhere online - I bet that if you had it, you might be able to come up with a higher score. The file "gitlist.txt" was found at https://gist.github.com/mrflip/1516fa9e28c421a2f71e.

The algorithm uses a depth first search to place words. It has a greedy option that returns the first sequence of words it finds, having chosen the highest value placeable word at each step. This, however, may not be the best for maximizing score: for example, it would return a sequence of scores (30, 30, 30, 30) with total 810000 instead of a sequence with (25, 34, 34, 34) with total 982600. Initially I got around this by placing with some filtering statements in "validateScores()" for the first few words to see what happened. Eventually I did modify my code to perform an exhaustive search.

Doing an exhaustive search on all words will take a *long* time (I think, by some rough estimates, three years on my machine). But, if you have a score you're trying to beat, you can set KNOWN_HIGH_SCORE in FixedSearcher to that value, set GREEDY to false, and uncomment the boolean method validateScores() in placeNextWord() to run an exhaustive pretty quickly (like 10-20 minutes) - the main difference is some logic saying that if a word is too low scoring, we won't bother placing it.

The algorithm is straightforward - try placing a word, if successful place the next (or stop when you've placed four words), otherwise remove the letters you placed and try the next word - but needs some bookkeeping to avoid, say, a failed placement for Word 3 removing a letter that was placed by Word 1. Similarly, in a word like COCARBOXYLASE, if the second C cannot connect to an A, we can't remove C from the board since it appears earlier in the word. So, the Cell class keeps track of which word filled it, and the index of its char value within that word.

The SearchTester class checks each word to see if it was even placeable on the board and to write them to a specified text file, usually called "validWords_*x*.txt", where *x* was the set cutoff score.

Change "CUTOFF_VALUE_LOW" in class ScrabbleWordList to exclude all words with scores below that.

Sample Output (the spaces in the grid are empty cells):
```
Search Result: Row: 5 Column: 5 Value: D. for start (2, 1)
ENJAMBEMENT 24
TZITZIT 25
TZITZITH 29
HYPOCYCLOID 27
Score: 469800.0
 O E U
INAZAP
JETIYO
AMOHEC
 EBALI
U I OD
```
Some other comments:

Some parts of the code are pretty hard-coded, like the CUT_OFF_VALUE mentioned above, the greedy setting, etc. Sorry!

The Grid class has a JTable reference that displays the board in real time. This was quite useful in debugging and seeing final board configs, but updating it slows down the search considerably (although it's pretty cool to watch the depth first search happen in real time). Hence, all JTable/TableModel references are commented out in Grid.

There's a ScrabbleWordComparator, which prioritizes higher scoring words for placement. It also prioritizes shorter words, assuming they'll be easier to place than longer ones. But, one imaginable drawback is that on the greedy setting we may lose out on long words with high-scoring subwords (e.g. DIAZOTIZED, 30 and AZOTIZED, 27). I did some quick checks, though, and I don't think it makes a huge difference.
