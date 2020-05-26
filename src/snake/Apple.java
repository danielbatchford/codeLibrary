package snake;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

public class Apple {

    private final Random random = new Random();
    private final ArrayList<int[]> board;

    public Apple() {
        board = Util.getBoard();
    }

    public int[] getApple(ArrayDeque<int[]> snake) {

        ArrayList<int[]> posLocations = Util.removeAll(board, new ArrayList(snake)); //Can optimise this a lot

        if (posLocations.size() == 0) return new int[]{-1, -1};
        return posLocations.get(random.nextInt(posLocations.size()));

    }

}
