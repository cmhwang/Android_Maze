package edu.wm.cs.cs301.cheyennehwang.gui;

import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.cheyennehwang.R;

/**
 * Class: equivalent to State Losing
 * @author cheyennehwang
 *
 * Responsibilities; displays finish page in losing scenario
 * - navigate back to AAMzeActivity/State Title based on input
 * - diaplay path length taken, display shortest path length possible, display robot energy used (if applicable)
 *
 * Collaborators: PlayAnimationActivity (State PlayAnimation), PlayManuallyActivity (State PlayManually), AMazeActivity (State Title), UI, MazeFactory, Control
 *
 */

public class LosingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loselayout);

    }

    /**
     * handles what happens if user presses back button
     * returns to default title screen
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        Toast toast = Toast.makeText(LosingActivity.this, "Return to Title", Toast.LENGTH_SHORT);
        toast.show();
        Log.v("Back Button Pressed", "Returned to Title");
        finish();

    }
}

