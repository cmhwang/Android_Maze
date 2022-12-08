package edu.wm.cs.cs301.cheyennehwang.gui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

    public SeekBar playSpeedBar;

    public int botEnergyUsed;
    public ProgressBar botRemainEnergy;

    public boolean showMap;

    public Intent transitionToEnd;

    public boolean playPauseCounter = true;// key: true means playing

    public String robotType;
    public String robotConfiguration;

    public int stepsTaken = 0;
    public int shortestPath;

    public StatePlayingAnimated state;
    public RobotDriver driver;
    public Robot robot;

    //handler and thread for animation
    public Handler animHandler;
    public Runnable anim;
    public long playSpeed = 1600;



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
        int[] startPos = maze.getStartingPosition();
        shortestPath = maze.getDistanceToExit(startPos[0], startPos[1]);

        //creates robot, set up its sensor and type, connect the robot to the state playing object
        try {
            configurePieces(maze, state);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // sets up action listener and input handler for play/pause button
        beginPlayPause((Button) findViewById(R.id.playButton));

        // sets up action listener for maze switch
        setMazeView((SwitchCompat) findViewById(R.id.mapSwitch));

        // sets up action listener for speed scale
        checkSpeed((SeekBar) findViewById(R.id.speedBar));

        // sets up action listener for maze size scale buttons
        setMazeScale((Button) findViewById(R.id.incButton), 0);
        setMazeScale((Button) findViewById(R.id.decButton), 1);


        botRemainEnergy = findViewById(R.id.energyBar);

        state.start(this, findViewById(R.id.tempMazeBckgd));

        animHandler = new Handler();
        beginAnimation();

    }

    /**
     * helper method for organization: creates robot + driver objects according to specifications
     * also connects robot and driver to state and maze pieces
     * does what used to be done in control
     * also starts up the unreliable threads if applicable
     */
    public void configurePieces(Maze m, StatePlayingAnimated animState) throws InterruptedException {
        Intent transitionToPlay = getIntent();
        robotConfiguration = transitionToPlay.getStringExtra("sensorConfig");
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

            ReliableSensor forwardSensor = new ReliableSensor();
            forwardSensor.setSensorDirection(Robot.Direction.FORWARD);
            robot.addDistanceSensor(forwardSensor, Robot.Direction.FORWARD);

            ReliableSensor backSensor = new ReliableSensor();
            backSensor.setSensorDirection(Robot.Direction.BACKWARD);
            robot.addDistanceSensor(backSensor, Robot.Direction.BACKWARD);

            UnreliableSensor leftSensor = new UnreliableSensor();
            leftSensor.setSensorDirection(Robot.Direction.LEFT);
            robot.addDistanceSensor(leftSensor, Robot.Direction.LEFT);

            UnreliableSensor rightSensor = new UnreliableSensor();
            rightSensor.setSensorDirection(Robot.Direction.RIGHT);
            robot.addDistanceSensor(rightSensor, Robot.Direction.RIGHT);

            robot.getRobotSensor(Robot.Direction.LEFT).startFailureAndRepairProcess(4, 2);
            Thread.sleep(1300);
            robot.getRobotSensor(Robot.Direction.RIGHT).startFailureAndRepairProcess(4, 2);
            Log.v("Animation Set Up", " unreliable threads started");

        } else if (robotConfiguration.equalsIgnoreCase("Soso")){
            robot = new UnreliableRobot();

            UnreliableSensor forwardSensor = new UnreliableSensor();
            forwardSensor.setSensorDirection(Robot.Direction.FORWARD);
            robot.addDistanceSensor(forwardSensor, Robot.Direction.FORWARD);

            UnreliableSensor backSensor = new UnreliableSensor();
            backSensor.setSensorDirection(Robot.Direction.BACKWARD);
            robot.addDistanceSensor(backSensor, Robot.Direction.BACKWARD);

            ReliableSensor leftSensor = new ReliableSensor();
            leftSensor.setSensorDirection(Robot.Direction.LEFT);
            robot.addDistanceSensor(leftSensor, Robot.Direction.LEFT);

            ReliableSensor rightSensor = new ReliableSensor();
            rightSensor.setSensorDirection(Robot.Direction.RIGHT);
            robot.addDistanceSensor(rightSensor, Robot.Direction.RIGHT);

            robot.getRobotSensor(Robot.Direction.FORWARD).startFailureAndRepairProcess(4, 2);
            Thread.sleep(1300);
            robot.getRobotSensor(Robot.Direction.BACKWARD).startFailureAndRepairProcess(4, 2);
            Log.v("Animation Set Up", " unreliable threads started");
        } else { // branch for shaky configuration
            robot = new UnreliableRobot();

            UnreliableSensor forwardSensor = new UnreliableSensor();
            forwardSensor.setSensorDirection(Robot.Direction.FORWARD);
            robot.addDistanceSensor(forwardSensor, Robot.Direction.FORWARD);

            UnreliableSensor backSensor = new UnreliableSensor();
            backSensor.setSensorDirection(Robot.Direction.BACKWARD);
            robot.addDistanceSensor(backSensor, Robot.Direction.BACKWARD);

            UnreliableSensor leftSensor = new UnreliableSensor();
            leftSensor.setSensorDirection(Robot.Direction.LEFT);
            robot.addDistanceSensor(leftSensor, Robot.Direction.LEFT);

            UnreliableSensor rightSensor = new UnreliableSensor();
            rightSensor.setSensorDirection(Robot.Direction.RIGHT);
            robot.addDistanceSensor(rightSensor, Robot.Direction.RIGHT);

            robot.getRobotSensor(Robot.Direction.FORWARD).startFailureAndRepairProcess(4, 2);
            Thread.sleep(1300);
            robot.getRobotSensor(Robot.Direction.LEFT).startFailureAndRepairProcess(4, 2);
            Thread.sleep(1300);
            robot.getRobotSensor(Robot.Direction.RIGHT).startFailureAndRepairProcess(4, 2);
            Thread.sleep(1300);
            robot.getRobotSensor(Robot.Direction.BACKWARD).startFailureAndRepairProcess(4, 2);
            Log.v("Animation Set Up", " unreliable threads started");
        }

        //creates driver based on type, connects driver to state playing object, with final set method
        robotType = transitionToPlay.getStringExtra("driver");
        if (robotType.equalsIgnoreCase("Wizard")){
            driver = new Wizard();
        } else {
            driver = new WallFollower();
        }
        state.setRobotAndDriver(robot, driver);
        robot.setController(state);
        robot.getRobotSensor(Robot.Direction.FORWARD).setMaze(m);
        robot.getRobotSensor(Robot.Direction.LEFT).setMaze(m);
        robot.getRobotSensor(Robot.Direction.RIGHT).setMaze(m);
        robot.getRobotSensor(Robot.Direction.BACKWARD).setMaze(m);

        driver.setMaze(m);
        driver.setRobot(robot);
        Log.v("Animation Set-Up", " robot, sensors, and driver set up");
    }

    /**
     * method that runs the animation through a runnable
     * basically does what control used to do with the driver object but pauses at each step so frame can be drawn
     */

    public void beginAnimation(){
        // checks that animation runnable isn't already running, then begins running
        anim = () -> {
            try {
                boolean checker = driver.drive1Step2Exit(); // executes and also checker returns false if at the end
                setEnergyUsed(driver.getEnergyConsumption());
                if (!robotConfiguration.equalsIgnoreCase("Premium")){
                    setSensorColor(robot);
                }
                if (!checker) {
                    if (robotType.equalsIgnoreCase("wizard")) {
                        ((Wizard)driver).finalDrive2End(robot.getCurrentPosition());
                        Log.w("You Won!", " Congratulations!");
                        skipEnd(true, robot.getOdometerReading(), driver.getEnergyConsumption());
                    } else {
                        ((WallFollower)driver).finalDrive2End(robot.getCurrentPosition());
                        Log.w("You Won!", " Congratulations!");
                        skipEnd(true, robot.getOdometerReading(), driver.getEnergyConsumption());
                    }

                }
                else {
                    animHandler.postDelayed(anim, playSpeed);
                }
            } catch (Exception e) {
                Log.w("Failure", e.toString());
                skipEnd(false, robot.getOdometerReading(), robot.getBatteryLevel());
            }
        };
        //handles the pausing
        animHandler.postDelayed(anim, playSpeed);
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
     * handles action listener for show maze switch
     * in p7 will change view based on what's clicked
     * sends log and toast messages based on what's pressed
     */
    public void setMazeView(SwitchCompat viewSwitch){
        viewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if (isChecked){
                    showMap = true;
                    state.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 0);
                    Log.v("Show Maze Mode", "On");
                    Toast toastMaze = Toast.makeText(PlayAnimationActivity.this, "Show Maze Mode On", Toast.LENGTH_SHORT);
                    toastMaze.show();
                } else {
                    showMap = false;
                    state.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 0);
                    Log.v("Show Maze Mode", "Off");
                    Toast toastMaze = Toast.makeText(PlayAnimationActivity.this, "Show Maze Mode Off", Toast.LENGTH_SHORT);
                    toastMaze.show();
                }
            }
        }
        );
    }

    /**
     * handles setting up the sensor status visualization
     * sends log and toast messages based on what is set
     * r is the robot object to act on
     */
    public void setSensorColor(Robot r){
        Button frontButton = (Button) findViewById(R.id.frontSensorStatus);
        Button leftButton = (Button) findViewById(R.id.leftSensorStatus);
        Button rightButton = (Button) findViewById(R.id.rightSensorStatus);
        Button backButton = (Button) findViewById(R.id.backSensorStatus);

        if (r.getRobotSensor(Robot.Direction.FORWARD).checkRepairStatus()){
            // branch if forward sensor is down for repair
            frontButton.setBackgroundColor(0xFFFF0000);
        } else {
            // branch if forward sensor not down for repair
            frontButton.setBackgroundColor(0xFF304A31);
        }

        if (r.getRobotSensor(Robot.Direction.LEFT).checkRepairStatus()){
            // branch if forward sensor is down for repair
            leftButton.setBackgroundColor(0xFFFF0000);
        } else {
            // branch if forward sensor not down for repair
            leftButton.setBackgroundColor(0xFF304A31);
        }

        if (r.getRobotSensor(Robot.Direction.RIGHT).checkRepairStatus()){
            // branch if forward sensor is down for repair
            rightButton.setBackgroundColor(0xFFFF0000);
        } else {
            // branch if forward sensor not down for repair
            rightButton.setBackgroundColor(0xFF304A31);
        }

        if (r.getRobotSensor(Robot.Direction.BACKWARD).checkRepairStatus()){
            // branch if forward sensor is down for repair
            backButton.setBackgroundColor(0xFFFF0000);
        } else {
            // branch if forward sensor not down for repair
            backButton.setBackgroundColor(0xFF304A31);
        }

        Log.v("Show Sensor Status", "Updated");
        Toast toastConfig = Toast.makeText(PlayAnimationActivity.this, "Show Sensor Configuration", Toast.LENGTH_SHORT);
        toastConfig.show();
    }

    /**
     * handles action listener for maze view scale buttons
     * sends log and toast messages based on what's selected
     */
    public void setMazeScale(Button changeButton, int changeDir){
        changeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (changeDir == 0){
                    //branch for increment hit
                    state.handleUserInput(Constants.UserInput.ZOOMIN, 0);
                    Log.v("Maze Scale Changed", "Zoomed In");
                    Toast toastWalls= Toast.makeText(PlayAnimationActivity.this, "Maze Zoom In", Toast.LENGTH_SHORT);
                    toastWalls.show();
                } else {
                    state.handleUserInput(Constants.UserInput.ZOOMOUT, 0);
                    Log.v("Maze Scale Changed", "Zoomed Out");
                    Toast toastWalls= Toast.makeText(PlayAnimationActivity.this, "Maze Zoom Out", Toast.LENGTH_SHORT);
                    toastWalls.show();
                }
            }
        });
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
                if (!playPauseCounter){
                    animPlayButton.setText("PAUSE");
                    Log.v("Animation Control hit", "Play");
                    animHandler.postDelayed(anim, playSpeed);

                    Toast toastPlay = Toast.makeText(PlayAnimationActivity.this, "Play Animation", Toast.LENGTH_SHORT);
                    toastPlay.show();
                } else {
                    animPlayButton.setText("PLAY");
                    Log.v("Animation Control hit", "Paused");
                    animHandler.removeCallbacks(anim);

                    Toast toastPause = Toast.makeText(PlayAnimationActivity.this, "Pause Animation", Toast.LENGTH_SHORT);
                    toastPause.show();
                }
                playPauseCounter = !playPauseCounter;
            }

        }
        );
    }

    /**
     * called by state playing when at end
     * colllects data for final page
     */
    public void skipEnd(boolean win, int pathLength, float energyUsed){
        // does the actual transition to the next stage and passes along the needed input
        if (win){
            Log.v("Switch To End Screen", "Win Screen");
            transitionToEnd = new Intent(PlayAnimationActivity.this, WinningActivity.class);
            transitionToEnd.putExtra("energyUsage", String.valueOf(energyUsed));
            transitionToEnd.putExtra("pathTakenLength", String.valueOf(pathLength));
            transitionToEnd.putExtra("shortestLength", String.valueOf(shortestPath));
            Toast toast = Toast.makeText(PlayAnimationActivity.this, "Switch to Ending Screen, Path Length Taken: " + String.valueOf(pathLength), Toast.LENGTH_SHORT);
            toast.show();
            startActivity(transitionToEnd);
        } else {
            Log.v("Switch To End Screen", "Lose Screen");
            transitionToEnd = new Intent(PlayAnimationActivity.this, LosingActivity.class);
            if (energyUsed > 3500f){
                transitionToEnd.putExtra("loseReason", "Energy Used Up");
            } else {
                transitionToEnd.putExtra("loseReason", "Robot Failure");
            }

            transitionToEnd.putExtra("energyUsage", String.valueOf(energyUsed));
            transitionToEnd.putExtra("pathTakenLength", String.valueOf(pathLength));
            transitionToEnd.putExtra("shortestLength", String.valueOf(shortestPath));
            Toast toast = Toast.makeText(PlayAnimationActivity.this, "Switch to Ending Screen, Path Length Taken: " + String.valueOf(pathLength), Toast.LENGTH_SHORT);
            toast.show();
            startActivity(transitionToEnd);
        }



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
                    playSpeed = (long) 400;
                } else if (speedScale.getProgress() == 1){
                    playSpeed = (long) 800;
                } else if (speedScale.getProgress() == 2){
                    playSpeed = (long) 1200;
                } else if (speedScale.getProgress() == 3){
                    playSpeed = (long) 1600;
                } else if (speedScale.getProgress() == 4){
                    playSpeed = (long) 2000;
                } else {
                    playSpeed = (long) 2400;
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
     * helper accessor method so that the robot can be interracted with the state object
     *
     */

    public StatePlayingAnimated getState(){
        return state;
    }

    /**
     * method to update energy usage bar
     * called on in stateplaying to update
     */
    public void setEnergyUsed(float energyVal){
        int temp = (int) energyVal;
        botRemainEnergy.setProgress(temp);
    }



}
