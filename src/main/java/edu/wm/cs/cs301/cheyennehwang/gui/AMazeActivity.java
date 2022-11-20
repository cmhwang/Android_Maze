package edu.wm.cs.cs301.cheyennehwang.gui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import edu.wm.cs.cs301.cheyennehwang.R;
import android.widget.Toast;

/**
 * Class: equivalent to state title
 * @author cheyennehwang
 *
 * Responsibilities: Display welcome an gather input parameters
 * - set maze complexity, room input, maze generation algorithm, replay option
 *
 * Collaborators: Generating Activity (State Generating), Control, UI, MazeFactory
 */

public class AMazeActivity extends AppCompatActivity {


    SeekBar sizeSeekBar;
    Spinner genAlgoSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        sizeSeekBar=(SeekBar)findViewById(R.id.levelSeekBar);

        //sets up and handles spinner that processes input for maze generation algo
        Spinner genAlgoSpinner = (Spinner) findViewById(R.id.genAlgoInput);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.algo_opt, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genAlgoSpinner.setAdapter(adapter);


    }



}

