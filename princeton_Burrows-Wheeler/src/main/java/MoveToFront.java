import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
public class MoveToFront{
    private static final int ASCII = 256;

    private static class Node{
        private Node next;
        private Node prev;
        private int value;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode(){
        Node first = new Node();
        Node node = first;
        for (int i = 0; i < ASCII; i++){
            node.value = i;
            if (i != ASCII - 1){
                node.next = new Node();
                node.next.prev = node;
            }
            node = node.next;
        }

        node = first;
        while (!BinaryStdIn.isEmpty()){
            char c = BinaryStdIn.readChar();
            int index = 0;
            while (node.value != c){
                node = node.next;
                index++;
            }
            BinaryStdOut.write(index, 8);
            if (node != first){
                node.prev.next = node.next;
                if (node.next != null){
                    node.next.prev = node.prev;
                }
                node.prev = null;
                node.next = first;
                first.prev = node;
                first = node;
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode(){
        Node first =  new Node();
        Node node = first;
        for (int i = 0; i < ASCII; i++){
            node.value = i;
            if (i != ASCII - 1){
                node.next = new Node();
                node.next.prev = node;
            }
            node = node.next;
        }

        node = first;

        while (!BinaryStdIn.isEmpty()){
            for (int i = 0, j = BinaryStdIn.readChar(); i != j; i++){
                node = node.next;
            }

            BinaryStdOut.write(node.value, 8);
            if (node != first){
                node.prev.next = node.next;
                if (node.next != null){
                    node.next.prev = node.prev;
                }
                node.prev = null;
                node.next = first;
                first.prev = node;
                first = node;
            }
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args){
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }

}