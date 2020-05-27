package snake;

import java.util.Arrays;

public class Direction {

    private char name;
    private int[] dir;

    public Direction(char name) {
        this.name = name;
        switch (name) {

            case 'w':
                dir = new int[]{0, -1};
                break;
            case 'a':
                dir = new int[]{-1, 0};
                break;
            case 's':
                dir = new int[]{0, 1};
                break;
            case 'd':
                dir = new int[]{1, 0};
                break;
            default:
                System.out.println("Bad Char");
        }
    }

    public Direction(int[] dir) {
        this.dir = dir;

        if (Arrays.equals(dir, new int[]{0, -1})) {
            this.name = 'w';
        } else if (Arrays.equals(dir, new int[]{-1, 0})) {
            this.name = 'a';
        } else if (Arrays.equals(dir, new int[]{0, 1})) {
            this.name = 's';
        } else if (Arrays.equals(dir, new int[]{1, 0})) {
            this.name = 'd';
        } else {
            System.out.println("Bad Dir");
        }
    }

    public int[] getDir() {
        return dir;
    }


}
