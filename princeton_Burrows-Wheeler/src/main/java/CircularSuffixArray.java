import java.util.Arrays;

public class CircularSuffixArray {
    private final int[] indices;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("Argument is NULL");

        int length = s.length();
        CircularSuffix[] suffixes = new CircularSuffix[length];
        char[] loopedString = s.concat(s).toCharArray();

        for (int i = 0; i < length; i++) {
            suffixes[i] = new CircularSuffix(loopedString, i);
        }


        Arrays.sort(suffixes);
        indices = new int[length];
        for (int i = 0; i < length; i++) {
            indices[i] = suffixes[i].index;
        }
    }

    //Circular Suffix Class
    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final char[] text;
        private final int index;

        public CircularSuffix(char[] text, int index) {
            this.text = text;
            this.index = index;
        }

        //Sort suffixes
        public int compareTo(CircularSuffix other) {
            for (int i = 0; i < (text.length / 2); i++) {
                if ((text[i + index]) > (other.text[i + other.index])) return 1;
                if ((text[i + index]) < (other.text[i + other.index])) return -1;
            }

            return 0;
        }

    }


    // length of s
    public int length() {
        return indices.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= this.indices.length) throw new IllegalArgumentException();
        return this.indices[i];
    }

    // unit testing (required)
    public static void main(String[] args) {

    }

}