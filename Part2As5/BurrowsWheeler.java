import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;
    
    public static void encode() {
        String s = BinaryStdIn.readString();
//        String s = "ABRACADABRA!";
        int length = s.length();
        int first = 0;
        char[] output = new char[length];
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < length; i++) {
            int index = csa.index(i) - 1;
            if (index == -1) {
                index += length;
                first = i;
            }
            output[i] = s.charAt(index);
        }
//        System.out.println(first);
//        System.out.println(new String(output));
        BinaryStdOut.write(first);
        BinaryStdOut.write(new String(output));
        BinaryStdIn.close();
        BinaryStdOut.close();
        
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        
        int first = BinaryStdIn.readInt();
//        int first = 3;
        String s = BinaryStdIn.readString();
//      String s = "ARD!RCAAAABB";
      int length = s.length();
      char[] output = new char[length];
      char[] head = s.toCharArray();
      int[] next = new int[length];
      ArrayList<Integer>[] list = new ArrayList[R];
      
      for (int i = 0; i < length; i++) {
          int index = (int) s.charAt(i);
          if (list[index] == null) {
              list[index] = new ArrayList<>();
          }
          list[index].add(i);
      }
      
      Arrays.sort(head);
      char c = Character.MAX_HIGH_SURROGATE;
      int count = -1;
      for (int i = 0; i < length; i++) {
          if (c != head[i]) {
              c = head[i];
              count = 0;
          } else {
              count++;
          }
          next[i] = list[c].get(count);
//          System.out.println(next[i]);
      }
      
      for (int i = 0; i < length; i++) {
          output[i] = head[first];
          first = next[first];
      }
      
//    System.out.println(new String(output));
        BinaryStdOut.write(new String(output));
        BinaryStdIn.close();
        BinaryStdOut.close();
    }
    
    

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }   
//        encode();
//        decode();
    }

}
