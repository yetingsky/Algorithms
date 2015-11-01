import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.TreeMap;

public class WordNet {
    
    private Digraph net;
    private TreeMap<String, ArrayList<Integer>> map;
    private ArrayList<String> list;
    private int V;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new NullPointerException();
        
        initSynsets(synsets);
        initHypernyms(hypernyms);
     // is a DAG
        DirectedCycle cycle = new DirectedCycle(net);
        if (cycle.hasCycle())
            throw new IllegalArgumentException("have cycle");
        // is one root
        for (int i = 0, count = 0; i < V; i++) {
            if (net.outdegree(i) == 0) {
                if (count == 0) count++;
                else throw new IllegalArgumentException("too many root");
            }
        }
        // init sap
        sap = new SAP(net);
    }

    private void initSynsets(String synsets) {
        V = 0;
        list = new ArrayList<>();
        map = new TreeMap<>();
        
        In in = new In(synsets);
        String[] fields, keySet; 
        String key; 
        int val;
        ArrayList<Integer> temp;
        for (String line = in.readLine(); line != null; line = in.readLine(), V++) {
            fields = line.split(",");
            keySet = fields[1].split(" ");
            val = Integer.parseInt(fields[0]);
            for (int j = 0; j < keySet.length; j++) {
                key = keySet[j];
//                map.put(key, val);
                if (map.containsKey(key)) {
                    temp = map.get(key);
                    if (temp.add(val))
                        map.put(key, temp);
                } 
                else {
                    temp = new ArrayList<>();
                    if (temp.add(val))
                        map.put(key, temp);
                }
                    
            }
            list.add(fields[1]);
        }
    }
    
    private void initHypernyms(String hypernyms) {
        net = new Digraph(V);
        In in = new In(hypernyms);
        String[] fields;
        for (String line = in.readLine(); line != null; line = in.readLine()) {
            fields = line.split(",");
            for (int i = 1, k = Integer.parseInt(fields[0]); i < fields.length; i++)
                net.addEdge(k, Integer.parseInt(fields[i])); 
        }
    }
    
    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new NullPointerException();
        return map.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        return helper(nounA, nounB)[0];
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        return list.get(helper(nounA, nounB)[1]);
    }

    // helper func for get distance, sap
    private int[] helper(String nounA, String nounB) {
        int length = Integer.MAX_VALUE, temp = 0, ancestor = 0;
        for (int val1 : map.get(nounA)) {
            for (int val2 : map.get(nounB)) {
                temp = sap.length(val1, val2);    
                
                if (temp < length) {
                    length = temp;
                    ancestor = sap.ancestor(val1, val2);
                }
            } 
        }
        return new int[] {length, ancestor};
    }
    
    // do unit testing of this class
    public static void main(String[] args) {
        WordNet test = new WordNet(args[0], args[1]);
        
        // test for isNoun
//        System.out.println(test.isNoun("c"));
//        System.out.println(test.isNoun("g"));
        
        // test for nouns
//        for (String temp : test.nouns())
//            System.out.println(temp);
//        
        // test for distance
        System.out.println(test.distance("harakiri", "mute"));
        // test for sap
        System.out.println(test.sap("harakiri", "mute"));
        
    }

}
