package snake;

import processing.core.PApplet;
import processing.core.PShape;

import java.io.IOException;
import java.util.ArrayDeque;


public class Renderer extends PApplet {

    private Game game;
    private PShape gameBg;

    public static void main(String[] args) {

        try {
            Cfg.getProperties();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot read from config file");
            System.exit(0);
        }
        PApplet.main("snake.Renderer");
    }

    public void settings() {
        if (Cfg.FULLSCREEN) fullScreen();

        size(Cfg.width, Cfg.height);
        noSmooth();

    }

    public void setup() {

        noStroke();
        rectMode(CENTER);

        frameRate(Cfg.FRAMERATE);

        fill(Cfg.RED[0], Cfg.RED[1], Cfg.RED[2]);

        game = new Game();

        surface.setTitle("Snake AI Demo - Daniel Batchford");

        gameBg = createShape();
        gameBg.beginShape();
        gameBg.fill(Cfg.DARKBLUE[0], Cfg.DARKBLUE[1], Cfg.DARKBLUE[2]);
        gameBg.noStroke();
        gameBg.vertex(0, 0);
        gameBg.vertex(Cfg.width, 0);
        gameBg.vertex(Cfg.width, Cfg.height);
        gameBg.vertex(0, Cfg.height);
        gameBg.endShape(CLOSE);

        shape(gameBg);

    }

    public void keyPressed() {
        if (key == Cfg.PAUSE_KEY) {

            Cfg.paused = !Cfg.paused;
            if (!Cfg.paused) loop();
        } else try {
            int i;
            if (key == '0') i = 9;
            else i = Integer.parseInt(String.valueOf(key)) - 1;
            Cfg.updateInterval = Cfg.SPEED_MAPPING[i];

        } catch (NumberFormatException e) {
        }

    }

    public void draw() {


        if (Cfg.paused) noLoop();

        if (frameCount % Cfg.updateInterval == 0) {
            if (!game.alive) {
                System.out.println("Game Won");
                noLoop();
                return;
            }

            game.update();


            ArrayDeque<int[]> drawSnake = game.getSnake();
            //for later path drawing

            //Drawing Path
            PShape followPath = null;


            ArrayDeque<int[]> path = game.getPath();

            followPath = createShape();
            followPath.beginShape();

            int[] pathCol = (game.surviving) ? Cfg.GREY : Cfg.PURPLE;
            followPath.stroke(pathCol[0], pathCol[1], pathCol[2], pathCol[3]);
            followPath.strokeWeight(Cfg.xInc * Cfg.PATH_SCALE);
            followPath.strokeCap(PROJECT);
            followPath.strokeJoin(MITER);
            followPath.noFill();

            int[] head = drawSnake.getFirst();
            followPath.vertex(Cfg.xInc * head[0] + Cfg.xInc / 2, Cfg.yInc * head[1] + Cfg.yInc / 2);
            for (int i = 0, max = path.size(); i < max; i++) {
                int[] cord = path.pop();
                followPath.vertex(Cfg.xInc * cord[0] + Cfg.xInc / 2, Cfg.yInc * cord[1] + Cfg.yInc / 2);
            }
            followPath.endShape();


            PShape snakePath = createShape();
            snakePath.beginShape();
            snakePath.stroke(Cfg.WHITE[0], Cfg.WHITE[1], Cfg.WHITE[2]);
            snakePath.strokeWeight(Cfg.xInc * Cfg.BODY_SCALE);
            snakePath.strokeJoin(MITER);
            snakePath.strokeCap(PROJECT);
            snakePath.noFill();

            for (int i = 0, length = drawSnake.size(); i < length; i++) {
                int[] cord = drawSnake.pop();
                snakePath.vertex(Cfg.xInc * cord[0] + Cfg.xInc / 2, Cfg.yInc * cord[1] + Cfg.yInc / 2);
            }
            snakePath.endShape();

            shape(gameBg);

            shape(followPath);
            shape(snakePath);

            int[] appleLocation = game.appleLocation;

            fill(Cfg.RED[0], Cfg.RED[1], Cfg.RED[2]);
            rect((float) ((appleLocation[0] + 0.5) * Cfg.xInc), (float) ((appleLocation[1] + 0.5) * Cfg.yInc), Cfg.xInc * Cfg.APPLE_SCALE, Cfg.yInc * Cfg.APPLE_SCALE);

        }
    }
}