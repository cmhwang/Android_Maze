package edu.wm.cs.cs301.cheyennehwang.gui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import edu.wm.cs.cs301.cheyennehwang.R;
import edu.wm.cs.cs301.cheyennehwang.generation.Maze;
import edu.wm.cs.cs301.cheyennehwang.generation.MazeSettings;
import edu.wm.cs.cs301.cheyennehwang.gui.Robot;
import edu.wm.cs.cs301.cheyennehwang.gui.RobotDriver;

/**
 * Class: equivalent to State Play Animation
 * @author cheyenne hwang
 *
 * Responsibilities: displays maze and allows for view of player navigating maze
 * - displays and updates energy usage
 * - show/hide view features based on user input: map view
 * - allow for scale adjustment of map view
 * - allow for adjustment of animation speed, allow for pause animation
 * - diaplay robot sensor state
 * - option to go back to state title or forward to state winning or losing
 *
 * Collaborators: AMazeActivity (State Title), WinningActivity (State Winning), LosingActivity (State losing) UI, MazeFactory, Control
 *
 */

public class PlayAnimationActivity extends AppCompatActivity {

    public int playSpeed;
    public SeekBar playSpeedBar;

    public int botEnergyUsed;
    public ProgressBar botRemainEnergy;

    public int mapSizeVar;

    public boolean showMap;

    public Intent transitionToEnd;

    public int playPauseCounter = 0;

    public String robotType;
    public String robotConfiguration;

    public int stepsTaken = 0;
    public int shortestPath;

    public StatePlayingAnimated state;
    public RobotDriver driver;
    public Robot robot;


    /**
     * Sets up any ui features that need additional specifications
     * - specfically here sets up switches for view mode, play speed, play/pause button, skip to end buttons
     * Sets up listener for when user ready to move to next state based on skip buttons
     * @param savedInstanceState is what is passed around
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playanimationlayout);

        //sets up maze and drive and state
        Maze maze = MazeSettings.getSettings().getMaze();
        state = new StatePlayingAnimated();
        state.setMaze(maze);
        state.start(this, findViewById(R.id.tempMazeBckgd));
        int[] startPos = maze.getStartingPosition();
        shortestPath = maze.getDistanceToExit(startPos[0], startPos[1]);

        //creates robot, set up its sensor and type, connect the robot to the state playing object
        Intent transitionToPlay = getIntent();
        robotConfiguration = transitionToPlay.getStringExtra("sensorConfig");
        setSensorColor(robotConfiguration);
        if (robotConfiguration.equalsIgnoreCase("Premium")){
            robot = new ReliableRobot();

            ReliableSensor forwardSensor = new ReliableSensor();
            forwardSensor.setSensorDirection(Robot.Direction.FORWARD);
            robot.addDistanceSensor(forwardSensor, Robot.Direction.FORWARD);

            ReliableSensor leftSensor = new ReliableSensor();
            leftSensor.setSensorDirection(Robot.Direction.LEFT);
            robot.addDistanceSensor(leftSensor, Robot.Direction.LEFT);

            ReliableSensor rightSensor = new ReliableSensor();
            rightSensor.setSensorDirection(Robot.Direction.RIGHT);
            robot.addDistanceSensor(rightSensor, Robot.Direction.RIGHT);

            ReliableSensor backSensor = new ReliableSensor();
            backSensor.setSensorDirection(Robot.Direction.BACKWARD);
            robot.addDistanceSensor(backSensor, Robot.Direction.BACKWARD);
        } else if (robotConfiguration.equalsIgnoreCase("Mediocre")){
            robot = new UnreliableRobot();

        }

        //creates driver based on type, connects driver to state playing object, with final set method
        robotType = transitionToPlay.getStringExtra("driver");
        if (robotType.equalsIgnoreCase("Wizard")){
            driver = new Wizard();
        } else {
            driver = new WallFollower();
        }
        state.setRobotAndDriver(robot, driver);




        // sets up action listener and input handler for play/pause button
        beginPlayPause((Button) findViewById(R.id.playButton));

        // sets up action listener for maze switch
        setMazeView((SwitchCompat) findViewById(R.id.mapSwitch));

        // sets up action listener for speed scale
        checkSpeed((SeekBar) findViewById(R.id.speedBar));

        // sets up action listener for maze size scale
        checkSize((SeekBar) findViewById(R.id.mapSizeBar));



    }

    /**
     * handles what happens if user presses back button
     * returns to default title screen
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        Toast toast = Toast.makeText(PlayAnimationActivity.this, "Return to Title", Toast.LENGTH_SHORT);
        toast.show();
        Log.v("Back Button Pressed", "Returned to Title");
        finish();

    }

    /**
     * handles action listener for show map button
     * in p7 will change view based on what's clicked
     * sends log and toast messages based on what's pressed
     */
    public void setMazeView(SwitchCompat viewSwitch){
        viewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if (isChecked){
                    showMap = true;
                    Log.v("Show Map Mode", "On");
                    Toast toastMap = Toast.makeText(PlayAnimationActivity.this, "Show Map Mode On", Toast.LENGTH_SHORT);
                    toastMap.show();
                } else {
                    showMap = false;
                    Log.v("Show Map Mode", "Off");
                    Toast toastMap = Toast.makeText(PlayAnimationActivity.this, "Show Map Mode Off", Toast.LENGTH_SHORT);
                    toastMap.show();
                }
            }
        }
    );
    }

    /**
     * handles setting up the sensor status visualization
     * sends log and toast messages based on what is set
     */
    public void setSensorColor(String config){
        Button frontButton = (Button) findViewById(R.id.frontSensorStatus);
        Button leftButton = (Button) findViewById(R.id.leftSensorStatus);
        Button rightButton = (Button) findViewById(R.id.rightSensorStatus);
        Button backButton = (Button) findViewById(R.id.backSensorStatus);

        if (config.equalsIgnoreCase("Premium")){
            // nothing here cuz default background is green

        } else if (config.equalsIgnoreCase("Mediocre")){
            leftButton.setBackgroundColor(0xFFFF0000);
            rightButton.setBackgroundColor(0xFFFF0000);
        } else if (config.equalsIgnoreCase("Soso")){
            frontButton.setBackgroundColor(0xFFFF0000);
            backButton.setBackgroundColor(0xFFFF0000);
        } else {
            frontButton.setBackgroundColor(0xFFFF0000);
            backButton.setBackgroundColor(0xFFFF0000);
            leftButton.setBackgroundColor(0xFFFF0000);
            rightButton.setBackgroundColor(0xFFFF0000);
        }
        Log.v("Show Sensor Status", config);
        Toast toastConfig = Toast.makeText(PlayAnimationActivity.this, "Show Sensor Configuration" + config, Toast.LENGTH_SHORT);
        toastConfig.show();
    }



    /**
     * handles action listener for play/pause button
     * changes text based on clicked
     * sends log and toast messages based on what's pressed
     */
    public void beginPlayPause(Button animPlayButton){
        animPlayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                //sets up the text to switch on the play button
                if (playPauseCounter % 2 == 1){
                    animPlayButton.setText("PAUSE");
                    Log.v("Animation Control hit", "Play");

                    Toast toastPlay = Toast.makeText(PlayAnimationActivity.this, "Play Animation", Toast.LENGTH_SHORT);
                    toastPlay.show();
                } else {
                    animPlayButton.setText("PLAY");
                    Log.v("Animation Control hit", "Paused");

                    Toast toastPause = Toast.makeText(PlayAnimationActivity.this, "Pause Animation", Toast.LENGTH_SHORT);
                    toastPause.show();
                }
                playPauseCounter = playPauseCounter + 1;
            }

        }
        );
    }

    /**
     * called by state playing when at end
     * colllects data for final page
     */
    public void skipEnd(){
        // does the actual transition to the next stage and passes along the needed input
        // TODO: get actual input values and whether win or lose
        Log.v("Switch To End Screen", "Win Screen");
        transitionToEnd = new Intent(PlayAnimationActivity.this, WinningActivity.class);
        transitionToEnd.putExtra("energyUsage", "N/A - Manual");
        transitionToEnd.putExtra("pathTakenLength", String.valueOf(stepsTaken));
        transitionToEnd.putExtra("shortestLength", String.valueOf(shortestPath));
        Toast toast = Toast.makeText(PlayAnimationActivity.this, "Switch to Ending Screen, Path Length Taken: " + String.valueOf(stepsTaken), Toast.LENGTH_SHORT);
        toast.show();
        startActivity(transitionToEnd);
    }

    /**
     * handles action listener for animation speed seek bar
     * in p7 will change animation based on input
     * sends log and toast messages based on what's selected
     */
    public void checkSpeed(SeekBar speedScale){
        speedScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar speedScale, int i, boolean b) {
                if (speedScale.getProgress() == 0){
                    playSpeed = 0;
                } else if (speedScale.getProgress() == 1){
                    playSpeed = 1;
                } else if (speedScale.getProgress() == 2){
                    playSpeed = 2;
                } else if (speedScale.getProgress() == 3){
                    playSpeed = 3;
                } else if (speedScale.getProgress() == 4){
                    playSpeed = 4;
                } else {
                    playSpeed = 5;
                }
                Log.v("Animation Speed Set", String.valueOf(playSpeed));

                Toast toastSpeed = Toast.makeText(PlayAnimationActivity.this, "Animation Speed: " + String.valueOf(playSpeed), Toast.LENGTH_SHORT);
                toastSpeed.show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar loadProgressbar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar loadProgressBar) {

            }

        });
    }

    /**
     * handles action listener for map scale seek bar
     * in p7 will change animation based on input
     * sends log and toast messages based on what's selected
     */
    public void checkSize(SeekBar sizeScale){
        sizeScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar sizeScale, int i, boolean b) {
                if (sizeScale.getProgress() == 0){
                    mapSizeVar = 0;
                } else if (sizeScale.getProgress() == 1){
                    mapSizeVar = 1;
                } else if (sizeScale.getProgress() == 2){
                    mapSizeVar= 2;
                } else if (sizeScale.getProgress() == 3){
                    mapSizeVar = 3;
                } else if (sizeScale.getProgress() == 4){
                    mapSizeVar = 4;
                } else {
                    mapSizeVar = 5;
                }
                Log.v("Map Size Set", String.valueOf(mapSizeVar));

                Toast toastSize= Toast.makeText(PlayAnimationActivity.this, "Map Size: " + String.valueOf(mapSizeVar), Toast.LENGTH_SHORT);
                toastSize.show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar sizeScale) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar sizeScale) {

            }

        });
    }

    /**
     * helper accessor method so that the robot can be interracted with the state object
     *
     */

    public StatePlayingAnimated getState(){
        return state;
    }


}
