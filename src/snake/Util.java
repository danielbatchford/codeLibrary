package snake;

import java.util.ArrayList;
import java.util.Arrays;

public class Util {

    public static int[] subtract(int[] a, int[] b) {
        int[] c = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i] - b[i];
        }
        return c;
    }

    public static int[] add(int[] a, int[] b) {
        int[] c = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i] + b[i];
        }
        return c;
    }

    public static ArrayList<int[]> removeAll(ArrayList<int[]> subject, ArrayList<int[]> toRemove) { //This can be optimised, using true false array?
        boolean toAdd;
        ArrayList<int[]> result = new ArrayList<>();
        for (int[] a : subject) {
            toAdd = true;
            for (int[] b : toRemove) {
                if (Arrays.equals(a, b)) {
                    toAdd = false;
                }
            }
            if (toAdd) {
                result.add(a);
            }
        }
        return result;
    }

    public static ArrayList<int[]> getBoard() {
        ArrayList<int[]> board = new ArrayList<>();
        for (int x = 0; x < Cfg.xDiv; x++) {
            for (int y = 0; y < Cfg.yDiv; y++) {
                board.add(new int[]{x, y});
            }
        }
        return board;
    }

    public static int getAbsLinearDistance(int[] a, int[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
    }

    public static boolean contains(ArrayList<int[]> a, int[] b) {
        for (int[] c : a) {
            if (Arrays.equals(b, c)) return true;
        }
        return false;

    }

}
