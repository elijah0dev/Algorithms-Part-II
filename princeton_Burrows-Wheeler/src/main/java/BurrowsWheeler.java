import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;


public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String text = BinaryStdIn.readString();
        CircularSuffixArray suffixArray = new CircularSuffixArray(text);
        int n = text.length();

        for (int i = 0; i < n; i++) {
            if (suffixArray.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }

        for (int i = 0; i < n; i++) {
            int index = suffixArray.index(i);
            BinaryStdOut.write(text.charAt((index + n - 1) % n)); 
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        int n = 0;

        int[] freq = new int[R];
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            freq[c]++;
            n++;
        }

        int[] cumulativeFreq = new int[R];
        char[] sorted = new char[n];
        for (int i = 1; i < R; i++) {
            cumulativeFreq[i] = cumulativeFreq[i - 1] + freq[i - 1];
        }

        for (int i = 0; i < R; i++) {
            for (int j = 0; j < freq[i]; j++) {
                sorted[cumulativeFreq[i]++] = (char) i;
            }
        }

        int[] next = new int[n];
        int[] currentCount = new int[R];
        for (int i = 0; i < n; i++) {
            char c = sorted[i];
            next[currentCount[c]++] = i;
        }

        // Output the original string based on the next array
        for (int pos = first, count = 0; count < n; count++) {
            BinaryStdOut.write(sorted[pos]);
            pos = next[pos];
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[1].equals("+")) inverseTransform();
    }

}
