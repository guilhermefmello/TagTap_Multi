package guilherme.tagtapmulti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SubjectActivity extends AppCompatActivity {

    TextView textViewNotes;
    EditText editTextSubject;
    SeekBar seekBarRating;
    ListView ListViewSubject;
    Button buttonAddSubject;

    DatabaseReference databaseSubject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        textViewNotes = (TextView) findViewById(R.id.textViewNotes);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarRating);
        ListViewSubject = (ListView) findViewById(R.id.listViewSubject);
        buttonAddSubject = (Button) findViewById(R.id.buttonAddSubject);

        Intent intent = getIntent();

        String id = intent.getStringExtra(TagTapNotesActivity.NOTES_ID);
        String notes = intent.getStringExtra(TagTapNotesActivity.NOTES);

        textViewNotes.setText(notes);

        databaseSubject = FirebaseDatabase.getInstance().getReference("Subjects").child(id);

        buttonAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSubject();
            }
        });

    }

    private void saveSubject(){

        String subject = editTextSubject.getText().toString().trim();
        int priority = seekBarRating.getProgress();

        if(!TextUtils.isEmpty(subject)){
            String subjectId = databaseSubject.push().getKey();

            Subject subjects = new Subject(subjectId, subject, priority);
            databaseSubject.child(subjectId).setValue(subjects);
            Toast.makeText(this, "Subject Saved Successfully!", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, "Subject Should Not Be Empty", Toast.LENGTH_LONG).show();
        }



    }


}
