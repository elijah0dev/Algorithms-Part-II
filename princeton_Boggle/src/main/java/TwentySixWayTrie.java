public class TwentySixWayTrie {
    private Node root; // root of the trie

    // Node class for the trie
    private static class Node {
        private int score; // score associated with the key
        private Node[] next = new Node[26]; // array of child nodes
    }

    // Initializes an empty string symbol table.
    public TwentySixWayTrie() {
        this.root = new Node(); // initialize root
    }

    // Inserts a key-value pair into the trie
    public void put(String key, int score) {
        if (key == null) throw new IllegalArgumentException("Key argument to put() is null");
        root = put(root, key, score, 0);
    }

    // Recursive helper for put
    private Node put(Node x, String key, int score, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.score = score;
            return x;
        }
        char c = key.charAt(d);
        x.next[c - 'A'] = put(x.next[c - 'A'], key, score, d + 1);
        return x;
    }

    // Returns the value associated with the given key
    public int get(String key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        Node x = get(root, key, 0);
        if (x == null) return 0;
        return x.score;
    }

    // Recursive helper for get
    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x; // found the key
        char c = key.charAt(d); // get current character
        return get(x.next[c - 'A'], key, d + 1); // recurse for next character
    }

    // Checks if the trie contains the given key
    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != 0; // returns true if score is not 0
    }

    // Checks if the trie has a key with the given prefix
    public boolean hasKeyWithPrefix(String prefix) {
        return get(root, prefix, 0) != null; // returns true if any node exists for the prefix
    }

}