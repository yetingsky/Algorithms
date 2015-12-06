import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;
    
    private static char[] set() {
        char[] seq = new char[R];
        for (int i = 0; i < seq.length; i++) {
            seq[i] = (char) (0x0 + i);
//            System.out.println(seq[i]);
        }
        return seq;
    }
    
    private static void move(int index, char[] seq, char c) {
        if (index != 0)
            System.arraycopy(seq, 0, seq, 1, index);
        seq[0] = c;
    }
     
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] seq = set();
        
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char index = 0;
            for (int j = 0; j < seq.length; j++, index++) {
                if (c == seq[j]) break;
            }
            move(index, seq, c);
            BinaryStdOut.write(index);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
        
//        String s = "ABRACADABRA!";       
//        int[] output = new int[s.length()];
//        for (int i = 0; i < s.length(); i++) {
//            char c = s.charAt(i);
//            int index = 0;
//            for (int j = 0; j < seq.length; j++, index++) {
//                if (c == seq[j]) break;
//            }
//            output[i] = index;
//            System.out.println(output[i]);
//            move(index, seq, c);
////            if (index != 0)
////                System.arraycopy(seq, 0, seq, 1, index);
////            seq[0] = c;
//        }
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] seq = set();
        
        while (!BinaryStdIn.isEmpty()) {
            char index = BinaryStdIn.readChar();
            BinaryStdOut.write(seq[index]);
            move(index, seq, seq[index]);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
//        int[] s = new int[] {65, 66, 82, 2, 68, 1, 69, 1, 4, 4, 2, 38};
//        char[] output = new char[s.length];
//
//        for (int i = 0; i < s.length; i++) {
//            int index = s[i];
//            output[i] = seq[index];
//            move(index, seq, output[i]);
//            System.out.println(output[i]);
//        }
        
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }  
    }
}