package edu.wm.cs.cs301.cheyennehwang.gui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

    /**
     * handles what happens if user presses back button
     * returns to default title screen
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        Toast toast = Toast.makeText(WinningActivity.this, "Return to Title", Toast.LENGTH_SHORT);
        toast.show();
        Log.v("Back Button Pressed", "Returned to Title");
        finish();

    }
}
