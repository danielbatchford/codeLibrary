package snake;


import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Cfg {


    final static Direction DEFAULT_DIRECTION = new Direction('w');

    final static int[] RED = new int[]{181, 42, 66};
    static final int[] PURPLE = new int[]{66, 72, 116, 100};
    static final int[] GREY = new int[]{103, 101, 127, 100};
    static int[] DARKBLUE = new int[]{34, 34, 36};
    static int[] WHITE = new int[]{240, 237, 238};

    final static int[] SPEED_MAPPING = new int[]{1, 2, 4, 6, 16, 30, 60, 80, 150, 300};


    static int DEFAULT_LENGTH = 2;
    static int GROWTH_RATE = 1;
    static int[] START_SQUARE = new int[]{0, 0};

    static boolean FULLSCREEN = true;

    //non fullscreen
    static int width = 1000;
    static int height = 1000;

    static int DIV = 10;
    static int FRAMERATE = 120;

    static float BODY_SCALE = 0.9f;
    static float PATH_SCALE = 0.1f;
    static float APPLE_SCALE = 0.9f;

    static char PAUSE_KEY = 'p';

    static int xDiv;
    static int yDiv;
    static int xInc;
    static int yInc;


    static int updateInterval = 10;
    static boolean paused = false;


    public static void getProperties() throws IOException {

        final String pathString = "." + File.separator + "config.properties";

        FileInputStream stream = new FileInputStream(pathString);

        Properties p = new Properties();
        p.load(stream);
        stream.close();

        FULLSCREEN = Boolean.parseBoolean(p.getProperty("Fullscreen"));

        DEFAULT_LENGTH = Integer.parseInt(p.getProperty("StartLength"));

        if (DEFAULT_LENGTH < 2) DEFAULT_LENGTH = 2;

        GROWTH_RATE = Integer.parseInt(p.getProperty("GrowthRate"));
        if (GROWTH_RATE < 0) GROWTH_RATE = 0;

        DIV = Integer.parseInt(p.getProperty("NumberOfDivisions"));

        FRAMERATE = Integer.parseInt(p.getProperty("FrameRate"));
        if (FRAMERATE < 1) FRAMERATE = 1;


        PAUSE_KEY = p.getProperty("PauseKey").charAt(0);

        if (!FULLSCREEN) {
            width = Integer.parseInt(p.getProperty("WindowWidth"));
            height = Integer.parseInt(p.getProperty("WindowHeight"));
        } else {
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            Cfg.width = size.width;
            Cfg.height = size.height;
        }
        updateInterval = Integer.parseInt(p.getProperty("UpdateInterval"));


        generateDimensions();
        START_SQUARE = new int[]{xDiv / 2, yDiv / 2};


    }

    public static void generateDimensions() {

        if (width >= height) {
            yDiv = DIV;
            xDiv = (int) ((DIV) * (float) width / (float) height);
        } else {
            xDiv = DIV;
            yDiv = (int) ((DIV) * (float) height / (float) width);
        }

        xInc = (int) ((float) Cfg.width / (float) Cfg.xDiv);
        yInc = (int) ((float) Cfg.height / (float) Cfg.yDiv);
    }
}
