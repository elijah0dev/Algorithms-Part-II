public class Outcast {
    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        //Setup
        int highestDistance = -1;
        String outcast = nouns[0];

        //Determine the sum distance of a noun between itself and every other noun
        for (int i = 0; i < nouns.length; i++){
            int tempSumDistance = 0;
            for (int j = 0; j < nouns.length; j++){
                if (i == j) continue;
                tempSumDistance += wordNet.distance(nouns[i], nouns[j]);
            }
            if (tempSumDistance > highestDistance){
                highestDistance = tempSumDistance;
                outcast = nouns[i];
            }
        }

        return outcast;
    }

    // see test client below
    public static void main(String[] args) {

    }


}
