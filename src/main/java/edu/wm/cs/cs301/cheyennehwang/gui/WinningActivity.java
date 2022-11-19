package edu.wm.cs.cs301.cheyennehwang.gui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.cheyennehwang.R;

/**
 * Class: equivalent to State Winning
 * @author cheyennehwang
 *
 * Responsibilities: displays finish page for winning scenario
 * - naviagate back to AMazeActivity (State Title) as an optoin
 * - displays path length taken displays shortest path length possible, displays robot energy consumption (if applicable)
 *
 * Collaborators: AMazeActivity (State Title), PlayAnimationActivity (State PlayAnimation), PlayManuallyActivity (State PlayManually), UI, MazeFactory, Control
 *
 */

public class WinningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winlayout);

    }
}
