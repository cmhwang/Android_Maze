
package edu.wm.cs.cs301.cheyennehwang.generation;

/**
 * helper class to store and communicate maze settings
 *
 * @author cheyenne hwang
 *
 */

public class MazeSettings {
    public static MazeSettings settings = null;
    public static Maze maze;

    //empty constructor for acccess
    public MazeSettings() {}

    //maze accessor method
    public Maze getMaze() {
        return maze;
    }

    //maze setter method
    public void setMaze(Maze entryMaze) {
        maze = entryMaze;
    }

    //creates a new maze setting if no maze setting, or returns the current one
    public static MazeSettings getSettings() {
        if (settings == null) {
            synchronized (MazeSettings.class) {
                settings = new MazeSettings();
            }
        }
        return settings;
    }


}
