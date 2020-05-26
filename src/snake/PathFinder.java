package snake;


import pathfinding.grid.GridCell;
import pathfinding.grid.NavigationGrid;
import pathfinding.grid.finders.AStarGridFinder;
import pathfinding.grid.finders.GridFinderOptions;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PathFinder { //UPDATE PATHFINDER ON NON DRAW FRAMES


    private final AStarGridFinder<GridCell> finderShort;
    private final AStarGridFinder<GridCell> finderLong;

    private final GridCell[][] cells = new GridCell[Cfg.xDiv][Cfg.yDiv];
    private List<GridCell> path = new ArrayList<>();
    private int[] head;
    private ArrayList<int[]> snakeArr;
    private int length;
    private int[] apple;


    public PathFinder() {

        GridFinderOptions optShort = new GridFinderOptions();
        optShort.isYDown = true;
        optShort.allowDiagonal = false;
        optShort.orthogonalMovementCost = 1;
        optShort.dontCrossCorners = false;
        optShort.heuristic = (from, to) -> {

            int[] f = ((GridCell) from).toIntArray();
            int[] t = ((GridCell) to).toIntArray();

            if (f[0] - t[0] == 0) return Math.abs(f[1] - t[1]);
            if (f[1] - t[1] == 0) return Math.abs(f[0] - t[0]);
            return 200 * Math.min(Math.abs(f[0] - t[0]), Math.abs(f[1] - t[1])); //fix corner cases*/ return 0;
        };

        GridFinderOptions optLong = new GridFinderOptions();
        optLong.isYDown = true;
        optLong.allowDiagonal = false;
        optLong.orthogonalMovementCost = -1;
        optLong.dontCrossCorners = false;
        optLong.heuristic = (from, to) -> {
            int[] start = ((GridCell) from).toIntArray();
            int[] end = ((GridCell) to).toIntArray();

            return (float) (-1 * Math.max(Math.abs(start[0] - end[0]), Math.abs(start[1] - end[1])));
        };

        finderShort = new AStarGridFinder<GridCell>(GridCell.class, optShort);
        finderLong = new AStarGridFinder<GridCell>(GridCell.class, optLong);


    }


    public ArrayDeque<int[]> normalSearch() {

        InitialiseCleanGrid();
        blockSnake(); //wont be needed after code below


        //Here, set walkable if offset from tail <= manhattan distance from head to tail
        for (int i = 0; i < length; i++) {
            int[] snakeBox = snakeArr.get(i);
            int distanceFromTail = length - i - 1;
            blockCell(snakeBox, Util.getAbsLinearDistance(snakeBox, head) >= distanceFromTail + 1); //Release cells from tail of snake before travel
        }
        path = finderShort.findPath(head, apple, new NavigationGrid<>(cells, true));

        if (path == null) {
            System.out.println("No path found from head to apple");
            return null;
        }

        path = new ArrayList<GridCell>(path);

        if (length + Cfg.GROWTH_RATE == Cfg.xDiv * Cfg.yDiv) {
            System.out.println("last square, returning path");
            return pathToArrayDeque(); //for last square
        }

        int moveAmount = path.size();

        ArrayList<int[]> rSnakeArr = (ArrayList<int[]>) snakeArr.clone();
        Collections.reverse(rSnakeArr);

        //"release" tail of snake, simulating moving forward.
        for (int i = 0; i < rSnakeArr.size(); i++) blockCell(rSnakeArr.get(i), i < moveAmount);

        blockPath(path, false);
        blockCell(apple, true);

        int[] target = rSnakeArr.get(Math.min(moveAmount - 1, rSnakeArr.size() - 1)); // Account for snake growing here

        List<GridCell> tPath = finderShort.findPath(apple, target, new NavigationGrid<>(cells, true));

        if (tPath == null) {
            System.out.println("No path from Apple to future tail of snake");
            return null; //No tail path found
        }

        if (tPath.size() <= Cfg.GROWTH_RATE) {
            System.out.println("Head of snake would collide with growing tail");
            return null; //Tail path will collide with growing tail
        }

        return pathToArrayDeque(); //Else, Path to apple and tail path are valid
    }


    public ArrayDeque<int[]> survive() {

        InitialiseCleanGrid();
        blockSnake();

        for (int i = length - 1; i >= 0; i -= 1) { //Only needed as failsafe, only first iteration should be needed, try testing then removing?

            int[] target = snakeArr.get(i);
            cells[target[0]][target[1]].setWalkable(true);

            if ((i < length - 1)) {
                int[] prev = snakeArr.get(i + 1);
                cells[prev[0]][prev[1]].setWalkable(false);
            }

            path = finderLong.findPath(head[0], head[1], target[0], target[1], new NavigationGrid<>(cells, true)); //use longest path
            if (path != null) {
                return pathToArrayDeque();
            }
        }
        System.out.println("No survival path (length > 0) found to any square on the grid");
        return null;

    }

    public void setGameInstance(Game gameInstance) {
        snakeArr = new ArrayList<int[]>(gameInstance.getSnake());
        length = snakeArr.size();
        apple = gameInstance.appleLocation;

        head = snakeArr.get(0);
    }

    public ArrayDeque<int[]> pathToArrayDeque() {
        if (path.size() == 0) return null;

        int length = path.size();
        ArrayDeque<int[]> dPath = new ArrayDeque<>();
        for (GridCell cell : path) {
            dPath.add(new int[]{cell.getX(), cell.getY()});
        }

        return dPath;
    }

    private void InitialiseCleanGrid() {
        for (int x = 0; x < Cfg.xDiv; x++) {
            for (int y = 0; y < Cfg.yDiv; y++) {
                cells[x][y] = new GridCell(true);
            }
        }
    }


    private void blockSnake() {
        for (int[] a : snakeArr) {
            cells[a[0]][a[1]].setWalkable(false);
        }
    }

    private void blockPath(List<GridCell> list, boolean walkable) {
        for (GridCell g : list) {
            int[] box = g.toIntArray();
            cells[box[0]][box[1]].setWalkable(walkable);
        }
    }

    private void blockCell(int[] cord, boolean walkable) {
        cells[cord[0]][cord[1]].setWalkable(walkable);
    }
}
