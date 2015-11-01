import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


public class Outcast {
    
    private WordNet wordnet;
    
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    
    public String outcast(String[] nouns) {
        int max = 0;
        String temp, out = null;
        for (int dis = 0, i = 0; i < nouns.length; i++, dis = 0) {
            temp = nouns[i];
            for (String noun : nouns) {
                dis += wordnet.distance(temp, noun);
            }            
            if (dis > max) {
                max = dis;
                out = temp;
            }   
        }
        return out;
    }
        
    
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

}
