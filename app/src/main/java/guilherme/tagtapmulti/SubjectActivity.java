package guilherme.tagtapmulti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class SubjectActivity extends AppCompatActivity {

    TextView textViewNotes;
    EditText editTextSubject;
    SeekBar seekBarRating;
    ListView ListViewSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        textViewNotes = (TextView) findViewById(R.id.textViewNotes);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarRating);
        ListViewSubject = (ListView) findViewById(R.id.listViewSubject);

        Intent intent = getIntent();

        String id = intent.getStringExtra(TagTapNotesActivity.NOTES_ID);
        String notes = intent.getStringExtra(TagTapNotesActivity.NOTES);

        textViewNotes.setText(notes);

    }
}
