package edu.wm.cs.cs301.cheyennehwang.gui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import edu.wm.cs.cs301.cheyennehwang.R;
import edu.wm.cs.cs301.cheyennehwang.generation.Maze;
import edu.wm.cs.cs301.cheyennehwang.generation.MazeFactory;
import edu.wm.cs.cs301.cheyennehwang.generation.MazeSettings;

/**
 * Class: equivalent to State Play Manually
 * @author cheyenne hwang
 *
 * Responsibilities: displays maze and allows player to manually move through maze
 * - show/hide view features based on user input: maze view, solution view, walls view
 * - show robot energy usage and allow for map scaling
 * - naviagate around maze with user controlled navigation buttons
 * - option to go back to state title or forward to state winning
 *
 * Collaborators: AMazeActivity (State Title), WinningActivity (State Winning), UI, MazeFactory, Control
 *
 */

public class PlayManuallyActivity extends AppCompatActivity {

    public int stepsTaken = 0;
    public int shortestPath;



    public SwitchCompat mazeViewSwitch;
    public boolean showMaze;

    public SwitchCompat wallsViewSwitch;
    public boolean showWalls;

    public SwitchCompat solutionViewSwitch;
    public boolean showSolution;

    public Intent transitionToEnd;

    public StatePlaying state;

    public MediaPlayer music;




    /**
     * Sets up any ui features that need additional specifications
     * - specfically here sets up switches for map view mode, scale view size, walls view mode, solution view mode, controls, shortcut to end button
     * Sets up listener for when user ready to move to next state based on shortcut button
     * @param savedInstanceState is what is passed around
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playmanuallayout);

        //plays music from soundtrack towards middle of movie
        music = MediaPlayer.create(PlayManuallyActivity.this,R.raw.path_of_the_wind);
        music.start();

        //sets up maze and drive and state
        Maze maze = MazeSettings.getSettings().getMaze();
        state = new StatePlaying();
        state.setMaze(maze);
        state.start(this, findViewById(R.id.tempBckgd));
        int[] startPos = maze.getStartingPosition();
        shortestPath = maze.getDistanceToExit(startPos[0], startPos[1]);

        //sets up ui implementation for maze view switches
        mazeViewSwitch = (SwitchCompat) findViewById(R.id.mazewidth);
        wallsViewSwitch = (SwitchCompat) findViewById(R.id.wallSwitch);
        solutionViewSwitch = (SwitchCompat) findViewById(R.id.slnSwitch);

        //sets up listeners for control navigation buttons
        controlHandler((Button) findViewById(R.id.forwardButton), 0);
        controlHandler((Button) findViewById(R.id.leftButton), 1);
        controlHandler((Button) findViewById(R.id.rightbutton), 2);
        controlHandler((Button) findViewById(R.id.jumpButton), 3);

        //sets up listener for show Maze switch
        setMazeView((SwitchCompat) findViewById(R.id.mazewidth));

        //sets up listener for show Walls switch
        setWallsView((SwitchCompat) findViewById(R.id.wallSwitch));

        //sets up listener for show solution switch
        setSlnView((SwitchCompat) findViewById(R.id.slnSwitch));

        //sets up listener for maze size buttons
        setMazeScale((Button) findViewById(R.id.incButton), 0);
        setMazeScale((Button) findViewById(R.id.decButton), 1);



    }

    /**
     * handles action listener for navigation control button
     * increments path length taken var if its jump or forward button that's pressed
     * knows which one pressed based on dir var: 0 = f, 1 = l, 2 = r, 3 = jump
     */
    public void controlHandler(Button controlButton, int dir){
        controlButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                //increments path length and sends log based on type of button
                if (dir == 0){
                    stepsTaken = stepsTaken + 1;
                    state.handleUserInput(Constants.UserInput.UP, 0);
                    Log.v("Navigation Control Hit", "Forward");
                    Toast toastF = Toast.makeText(PlayManuallyActivity.this, "Forward Control Hit", Toast.LENGTH_SHORT);
                    toastF.show();
                } else if (dir == 1){
                    state.handleUserInput(Constants.UserInput.LEFT, 0);
                    Log.v("Navigation Control Hit", "Left");
                    Toast toastL = Toast.makeText(PlayManuallyActivity.this, "Left Turn Control Hit", Toast.LENGTH_SHORT);
                    toastL.show();
                } else if (dir == 2){
                    state.handleUserInput(Constants.UserInput.RIGHT, 0);
                    Log.v("Navigation Control Hit", "Right");
                    Toast toastR = Toast.makeText(PlayManuallyActivity.this, "Right Turn Control Hit", Toast.LENGTH_SHORT);
                    toastR.show();
                } else {
                    state.handleUserInput(Constants.UserInput.JUMP, 0);
                    stepsTaken = stepsTaken + 1;
                    Log.v("Navigation Control Hit", "Jump");
                    Toast toastJ = Toast.makeText(PlayManuallyActivity.this, "Jump Control Hit", Toast.LENGTH_SHORT);
                    toastJ.show();
                }
            }

        }
        );
    }

    /**
     * handles action listener for show maze switch
     * in p7 will change view based on what's clicked
     * sends log and toast messages based on what's pressed
     */
    public void setMazeView(SwitchCompat viewSwitch){
        viewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if (isChecked){
                    showMaze = true;
                    state.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 0);
                    Log.v("Show Maze Mode", "On");
                    Toast toastMaze = Toast.makeText(PlayManuallyActivity.this, "Show Maze Mode On", Toast.LENGTH_SHORT);
                    toastMaze.show();
                } else {
                    showMaze = false;
                    state.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 0);
                    Log.v("Show Maze Mode", "Off");
                    Toast toastMaze = Toast.makeText(PlayManuallyActivity.this, "Show Maze Mode Off", Toast.LENGTH_SHORT);
                    toastMaze.show();
                }
            }
        }
        );
    }

    /**
     * handles action listener for show solution switch
     * in p7 will change view based on what's clicked
     * sends log and toast messages based on what's pressed
     */
    public void setSlnView(SwitchCompat viewSwitch){
        viewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if (isChecked){
                    showSolution = true;
                    state.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 0);
                    Log.v("Show Solution Mode", "On");
                    Toast toastSln = Toast.makeText(PlayManuallyActivity.this, "Show Solution Mode On", Toast.LENGTH_SHORT);
                    toastSln.show();
                } else {
                    showSolution = false;
                    state.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 0);
                    Log.v("Show Solution Mode", "Off");
                    Toast toastSln = Toast.makeText(PlayManuallyActivity.this, "Show Maze Mode Off", Toast.LENGTH_SHORT);
                    toastSln.show();
                }
            }
        }
        );
    }

    /**
     * handles action listener for show walls switch
     * in p7 will change view based on what's clicked
     * sends log and toast messages based on what's pressed
     */
    public void setWallsView(SwitchCompat viewSwitch){
        viewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if (isChecked){
                    showWalls = true;
                    state.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 0);
                    Log.v("Show Walls Mode", "On");
                    Toast toastWalls = Toast.makeText(PlayManuallyActivity.this, "Show Walls Mode On", Toast.LENGTH_SHORT);
                    toastWalls.show();
                } else {
                    showWalls = false;
                    state.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 0);
                    Log.v("Show Walls Mode", "Off");
                    Toast toastWalls= Toast.makeText(PlayManuallyActivity.this, "Show Walls Mode Off", Toast.LENGTH_SHORT);
                    toastWalls.show();
                }
            }
        }
        );
    }

    /**
     * handles action listener for maze view scale seek bar
     * sends log and toast messages based on what's selected
     */
    public void setMazeScale(Button changeButton, int changeDir){
        changeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (changeDir == 0){
                    //branch for increment hit
                    state.handleUserInput(Constants.UserInput.ZOOMIN, 0);
                    Log.v("Maze Scale Changed", "Zoomed In");
                    Toast toastWalls= Toast.makeText(PlayManuallyActivity.this, "Maze Zoom In", Toast.LENGTH_SHORT);
                    toastWalls.show();
                } else {
                    state.handleUserInput(Constants.UserInput.ZOOMOUT, 0);
                    Log.v("Maze Scale Changed", "Zoomed Out");
                    Toast toastWalls= Toast.makeText(PlayManuallyActivity.this, "Maze Zoom Out", Toast.LENGTH_SHORT);
                    toastWalls.show();
                }
            }
        });
    }

    /**
     * handles what happens if user presses back button
     * returns to default title screen
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        Toast toast = Toast.makeText(PlayManuallyActivity.this, "Return to Title", Toast.LENGTH_SHORT);
        toast.show();
        Log.v("Back Button Pressed", "Returned to Title");
        stepsTaken = 0;
        state = null;
        music.stop();
        finish();

    }


    /**
     * called by state playing when at end
     * colllects data for final page
     */
    public void skipEnd(){
        // does the actual transition to the next stage and passes along the needed input
        Log.v("Switch To End Screen", "Win Screen");
        transitionToEnd = new Intent(PlayManuallyActivity.this, WinningActivity.class);
        transitionToEnd.putExtra("energyUsage", "N/A - Manual");
        transitionToEnd.putExtra("pathTakenLength", String.valueOf(stepsTaken));
        transitionToEnd.putExtra("shortestLength", String.valueOf(shortestPath));
        Toast toast = Toast.makeText(PlayManuallyActivity.this, "Switch to Ending Screen, Path Length Taken: " + String.valueOf(stepsTaken), Toast.LENGTH_SHORT);
        toast.show();
        music.stop();
        startActivity(transitionToEnd);
    }
}
