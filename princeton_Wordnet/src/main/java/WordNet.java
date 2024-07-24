import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class WordNet {
    private Map<Integer, List<String>> synsets;
    private Map<String, List<Integer>> reverseSynsets;
    private Digraph hypernyms;
    private int numVertices;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        this.synsets = new HashMap<>();
        readSynsets(synsets);
        this.hypernyms = new Digraph(numVertices);
        readHypernyms(hypernyms);
        this.sap = new SAP(this.hypernyms);

        reverseSynsets = new HashMap<>();
        initializeReverseSysets();
    }

    private void initializeReverseSysets(){
        for (Map.Entry<Integer, List<String>> entry: synsets.entrySet()){
            int synsetId = entry.getKey();
            List<String> nouns = entry.getValue();
            for (String noun: nouns){
                if (!reverseSynsets.containsKey(noun)){
                    reverseSynsets.put(noun, new ArrayList<>());
                }
                reverseSynsets.get(noun).add(synsetId);

            }
        }
    }

    private void readSynsets(String synsetsFile) {
        In scanner = new In(synsetsFile);
        while (scanner.hasNextLine()) {
            String line = scanner.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            String[] nouns = parts[1].split(" ");
            List<String> nounsList = new ArrayList<>();
            for (String noun : nouns) {
                nounsList.add(noun);
            }
            this.synsets.put(id, nounsList);
        }
        this.numVertices = synsets.size();
        scanner.close();
    }

    private void readHypernyms(String hypernymsFile) {
        In scanner = new In(hypernymsFile);
        while (scanner.hasNextLine()) {
            String line = scanner.readLine();
            String[] parts = line.split(",");
            int synsetId = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                int hypernymId = Integer.parseInt(parts[i]);
                this.hypernyms.addEdge(synsetId, hypernymId);
            }
        }
        scanner.close();

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        List<String> allNouns = new ArrayList<>();

        for (List<String> nouns : synsets.values()) {
            allNouns.addAll(nouns);
        }
        return allNouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return reverseSynsets.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();

        return sap.length(reverseSynsets.get(nounA), reverseSynsets.get(nounB));
    }

    private List<Integer> getSynsets(String noun) {
        List<Integer> synsetIds = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> entry : synsets.entrySet()) {
            if (entry.getValue().contains(noun)) {
                synsetIds.add(entry.getKey());
            }
        }
        if (synsetIds.isEmpty()) throw new IllegalArgumentException(); //might need to adjust
        return synsetIds;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();



        int ancestorId = sap.ancestor(reverseSynsets.get(nounA), reverseSynsets.get(nounB));
        return this.synsets.get(ancestorId).get(0);
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
