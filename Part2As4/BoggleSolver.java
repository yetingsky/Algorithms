import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
//import edu.princeton.cs.algs4.TrieST;
import java.util.ArrayList;
import java.util.HashMap;
//import edu.princeton.cs.algs4.TST;

public class BoggleSolver {
//    private TrieST<Integer> trie;
//    private TST<Integer> dict;
    private Trie<Integer> dict;
    private BoggleBoard board;
    private HashMap<String, Integer> map;
    private ArrayList<String> list;
    
    public BoggleSolver(String[] dictionary) {
        dict = new Trie<>();
//        dict = new TST<>();
//        trie = new TrieST<>();
        for (int i = 0; i < dictionary.length; i++) {
//            trie.put(dictionary[i], i);
            dict.put(dictionary[i], getScore(dictionary[i]));
        }
    }
    
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.board = board;
        this.map = new HashMap<>();
        this.list = new ArrayList<>();
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                dfs(visited, "", i, j, 0);
            }
        }
        return list;
    }
    
//    private boolean has_prefix(String prefix) {
////        for (String s: trie.keysWithPrefix(prefix)) {
//        for (String s: dict.keysWithPrefix(prefix)) {
//            return s != null;
//        }
//        return false;
//    }
    
    private void dfs(boolean[][] visted, String prefix, int i, int j, int count) {
        char c = board.getLetter(i, j);
        if (c == 'Q') {
            prefix += "QU";
            count += 2;
        }  
        else {
            prefix += c;
            count += 1;
        }
            
        visted[i][j] = true;
        boolean temp = false;
//        if (trie.contains(prefix)) {
        if (dict.contains(prefix)) {
            temp = true;
            if (count >= 3 && !map.containsKey(prefix)) {
                map.put(prefix, getScore(prefix));
                list.add(prefix);
            }      
        }
//        if (temp || has_prefix(prefix)) {
            if (temp || dict.hasKey(prefix)) {
            if (j-1 >= 0 && !visted[i][j-1])
                dfs(visted, prefix, i, j-1, count);
            if (j+1 < board.cols() && !visted[i][j+1])
                dfs(visted, prefix, i, j+1, count);
            if (i-1 >= 0) {
                if (j-1 >= 0 && !visted[i-1][j-1])
                    dfs(visted, prefix, i-1, j-1, count);
                if (!visted[i-1][j])
                    dfs(visted, prefix, i-1, j, count);
                if (j+1 < board.cols() && !visted[i-1][j+1])
                    dfs(visted, prefix, i-1, j+1, count);
            } 
            if (i+1 < board.rows()) {
                if (j-1 >= 0 && !visted[i+1][j-1])
                    dfs(visted, prefix, i+1, j-1, count);
                if (!visted[i+1][j])
                    dfs(visted, prefix, i+1, j, count);
                if (j+1 < board.cols() && !visted[i+1][j+1])
                    dfs(visted, prefix, i+1, j+1, count);
            }  
        }
        visted[i][j] = false;
    }
    
    private int getScore(String word) {
        if (word.length() <= 2)
            return 0;
        if (word.length() == 3 || word.length() == 4)
            return 1;
        else if (word.length() == 5)
            return 2;
        else if (word.length() == 6)
            return 3;
        else if (word.length() == 7)
            return 5;
        else
            return 11;
    }
    
    public int scoreOf(String word) {
        if (dict.contains(word))
            return dict.get(word);
        return 0;
//        return dict.get(word);
                      
//        if (trie.contains(word))
//            if (word.length() == 3 || word.length() == 4)
//                return 1;
//            else if (word.length() == 5)
//                return 2;
//            else if (word.length() == 6)
//                return 3;
//            else if (word.length() == 7)
//                return 5;
//            else
//                return 11;
//        return 0;   
    }

    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
//        StdOut.println(solver.scoreOf("AID"));
//        StdOut.println(solver.scoreOf("ENDS"));
//        StdOut.println(solver.scoreOf("EQUATION"));
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
