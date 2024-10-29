import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private final TwentySixWayTrie dictionary;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dictionary = new TwentySixWayTrie();
        for (String word : dictionary) {
            if (word.length() < 3) continue;
            this.dictionary.put(word, 1);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> validWords = new HashSet<>();
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        char[] word = new char[board.cols() * board.rows()];


        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                dfs(board, validWords, visited, row, col, word, 0);
            }
        }

        return validWords;
    }

    private void dfs(BoggleBoard board, Set<String> validWords, boolean[][] visited, int i, int j, char[] word, int length) {
        if (i < 0 || j < 0 || i >= board.rows() || j >= board.cols() || visited[i][j]) return;

        if (!this.dictionary.hasKeyWithPrefix(new String(word, 0, length))) {
            return;
        }

        char currentLetter = board.getLetter(i, j);
        if (currentLetter == 'Q') {
            word[length++] = 'Q';
            word[length++] = 'U';
        } else {
            word[length++] = currentLetter;
        }


        if (length >= 3 && this.dictionary.contains(new String(word, 0, length))) {
            validWords.add(new String(word, 0, length));
        }

        visited[i][j] = true;


        dfs(board, validWords, visited, i + 1, j + 1, word, length);
        dfs(board, validWords, visited, i + 1, j - 1, word, length);
        dfs(board, validWords, visited, i - 1, j + 1, word, length);
        dfs(board, validWords, visited, i - 1, j - 1, word, length);
        dfs(board, validWords, visited, i, j + 1, word, length);
        dfs(board, validWords, visited, i, j - 1, word, length);
        dfs(board, validWords, visited, i + 1, j, word, length);
        dfs(board, validWords, visited, i - 1, j, word, length);

        visited[i][j] = false;
        if (currentLetter == 'Q') {
            length -= 2;
        } else {
            length--;
        }
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dictionary.contains(word)) return 0;

        int length = word.length();
        if (length == 3 || length == 4) return 1;
        if (length == 5) return 2;
        if (length == 6) return 3;
        if (length == 7) return 5;

        return 11;
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}