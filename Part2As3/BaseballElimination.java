import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

public class BaseballElimination {
    private final static int WIN = 0;
    private final static int LOSS = 1;
    private final static int REMAIN = 2;
    private Map<String, Integer> map;
    private String[] mapR;
    private int N;
    private int[][] info;
    private int[][] against;
    private int remainTotal;
    
    public BaseballElimination(String filename) {
        In in = new In(filename);
        N = in.readInt();
        map = new HashMap<>();
        mapR = new String[N];
        against = new int[N][N];
        info = new int[N][3];
        for (int i = 0; !in.isEmpty(); i++) {
            String s = in.readString();
            map.put(s, i);
            mapR[i] = s;
            info[i][WIN] = in.readInt();
            info[i][LOSS] = in.readInt();
            info[i][REMAIN] = in.readInt();
            for (int j = 0; j < N; j++) {
                against[i][j] = in.readInt();
                remainTotal += against[i][j];
            }
        }
        remainTotal /= 2;
    }
    
    public int numberOfTeams() {
        return N;
    }
    
    public Iterable<String> teams() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            list.add(mapR[i]);
        }
        return list;
    }
    
    public int wins(String team) {
        check(team);
        return info[map.get(team)][WIN];
    }
    
    public int losses(String team) {
        check(team);
        return info[map.get(team)][LOSS];
    }
    
    public int remaining(String team) {
        check(team);
        return info[map.get(team)][REMAIN];
    }
    
    public int against(String team1, String team2) {
        check(team1);
        check(team2);
        return against[map.get(team1)][map.get(team2)];
    }
    
    public boolean isEliminated(String team) {
        check(team);
        // Trivial elimination
        if (trivial(team) != null)
            return true;
        // Nontrivial elimination
        FordFulkerson eli = nontrivial(team);
//        System.out.println(remainTotal);
//        System.out.println(eli.value());
        int remain = remainTotal;
        for (int i = 0; i < N; i++) {
            remain -= against[map.get(team)][i];
        }
        return remain != eli.value();
    }
    
    public Iterable<String> certificateOfElimination(String team) {
        check(team);
        String s = trivial(team);
        ArrayList<String> list = new ArrayList<>();
        if (s != null) {    
            list.add(s);
        } else {
            FordFulkerson eli = nontrivial(team);
            int remain = remainTotal;
            for (int i = 0; i < N; i++) {
                remain -= against[map.get(team)][i];
            }
            if (remain == eli.value())
                return null;
            for (int i = 0; i < N; i++) {
                if (eli.inCut(i)) {
//                        System.out.println(mapR[i]);s
                    list.add(mapR[i]);
                }
            } 
        }
        return list;
    }
    
    private String trivial(String team) {
        int index = map.get(team);
        for (int i = 0, total = info[index][WIN] + info[index][REMAIN]; i < N; i++) {
            if (total < info[i][WIN]) {
//                System.out.println(mapR[i]);
                return mapR[i];
            }
        }
        return null;
    }
    
    
    private FordFulkerson nontrivial(String team) {
        FlowNetwork network = network(team);
        FordFulkerson eli = new FordFulkerson(network, N + (N-1) * (N-2) / 2, map.get(team));
        return eli;
    }
    
    
    private FlowNetwork network(String team) {
        int size = N + 1 + (N-1) * (N-2) / 2;
        int sink = map.get(team);
        FlowNetwork network = new FlowNetwork(size);
        for (int i = 0, total = info[sink][WIN] + info[sink][REMAIN]; i < N; i++) {
            if (i != sink) {
                network.addEdge(new FlowEdge(i, sink, total - info[i][WIN]));
            }
        }
        int i = N, m = 0, n = m + 1;
        while (i < size -1) {
            if (n == N) {
                m++; 
                n = m + 1; 
                continue;
            }
            if (m == sink) {
                m++; 
                n = m + 1; 
                continue;
            }
            if (n == sink) {
                n++; 
                continue;
            }
            network.addEdge(new FlowEdge(size-1, i, against[m][n]));
            network.addEdge(new FlowEdge(i, m, Double.POSITIVE_INFINITY));
            network.addEdge(new FlowEdge(i, n++, Double.POSITIVE_INFINITY));
            i++;
        }
        return network;
    }
    
    private void check(String team) {
        if (map.get(team) == null)
            throw new IllegalArgumentException();
    }
    
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
//        for (String team: division.teams()) {
//            System.out.println(division.wins(team));
//            System.out.println(division.losses(team));
//            System.out.println(division.remaining(team));
//        }
//        System.out.println((division.against("Boston", "Detroit")));
//        System.out.println((division.against("Baltimore", "Detroit")));
//        System.out.println(division.numberOfTeams());
//        FlowNetwork network = division.network("Detroit");
//        System.out.println(network);
        
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}
