package snake;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class Game {

    private final ArrayDeque<int[]> snake = new ArrayDeque<>();
    private final Apple apple = new Apple();
    int[] appleLocation;
    boolean surviving = false;
    boolean alive = true;
    private PathFinder pathFinder;
    private int length;
    private Direction direction = Cfg.DEFAULT_DIRECTION; //For first launch
    private ArrayDeque<int[]> path;

    public Game() {
        reset();
    }

    private void reset() {
        pathFinder = new PathFinder();
        System.out.println("Resetting Game");
        snake.clear();
        snake.addFirst(Cfg.START_SQUARE);
        appleLocation = apple.getApple(snake); //This might cause errors
        length = Cfg.DEFAULT_LENGTH;
        surviving = false;
        if(Cfg.AI) updatePath();

    }


    public void update() {

        if (surviving && Cfg.AI) updatePath();

        if (Cfg.AI) direction = new Direction(Util.subtract(path.pollFirst(), snake.getFirst()));

        travel();
        shorten(length);
    }

    private void travel() {
        int[] newHead = Util.add(snake.getFirst(), direction.getDir());
        if (checkCollision(newHead)) {

            reset();
            return;
        }
        snake.addFirst(newHead);

        if (checkAppleCollected(newHead)) {
            if (alive = !(Arrays.equals(appleLocation, new int[]{-1, -1}))) updatePath();
        }
    }

    public boolean checkAppleCollected(int[] newHead) {

        boolean collected;
        if (collected = Arrays.equals(newHead, appleLocation)) {

            length += Cfg.GROWTH_RATE;
            appleLocation = apple.getApple(snake);

        }
        return collected;
    }


    private void shorten(int length) {

        for (int i = 0, size = snake.size(); i < size - length; i++) {
            snake.removeLast();
        }
    }

    public boolean checkCollision(int[] head) {
        ArrayList<int[]> snakeList = new ArrayList<>(snake);

        snakeList.remove(snakeList.size() - 1); // needed currently to allow snake to be directly behind tail, shitty solution for now

        if (Util.contains(snakeList, head) && Cfg.BODY_COLLISIONS) {
            System.out.println("Collision with body");
            return true;
        }

        if (Cfg.WALL_COLLISIONS && (head[0] < 0 || head[0] >= Cfg.xDiv || head[1] < 0 || head[1] >= Cfg.yDiv)) {
            System.out.println("Collision with wall");
            return true;
        }
        return false;
    }

    public void setUserDirection(Direction newDirection) {
        switch (this.direction.getName()) {
            case 'w':
                if (newDirection.getName() == 's') return;
                break;
            case 'a':
                if (newDirection.getName() == 'd') return;
                break;
            case 's':
                if (newDirection.getName() == 'w') return;
                break;
            case 'd':
                if (newDirection.getName() == 'a') return;
                break;
            default:
                System.out.println("Bad Direction Given");
        }
        this.direction = newDirection;
    }

    private void updatePath() {

        pathFinder.setGameInstance(this);

        path = pathFinder.normalSearch();

        if (surviving = (path == null)) path = pathFinder.survive();
        if (path == null) {
            System.out.println("Game found a null path");
            Cfg.paused = true;
        }

    }

    public ArrayDeque<int[]> getSnake() {
        return snake.clone();
    }

    public ArrayDeque<int[]> getPath() {
        return path.clone();
    }

    public Direction getDirection() {
        return direction;
    }
}
