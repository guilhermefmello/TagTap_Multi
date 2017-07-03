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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static guilherme.tagtapmulti.R.id.listViewSubject;

public class SubjectActivity extends AppCompatActivity {

    TextView textViewNotes, textViewRating;
    EditText editTextSubject;
    SeekBar seekBarRating;
    ListView ListViewSubject;
    Button buttonAddSubject;

    DatabaseReference databaseSubject;

    List<Subject> subjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        textViewNotes = (TextView) findViewById(R.id.textViewNotes);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        textViewRating = (TextView) findViewById(R.id.textViewRating);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarRating);
        ListViewSubject = (ListView) findViewById(listViewSubject);
        buttonAddSubject = (Button) findViewById(R.id.buttonAddSubject);

        Intent intent = getIntent();

        subjects = new ArrayList<>();

        /*
        textViewNotes.setText(intent.getStringExtra(TagTapNotesActivity.NOTES));


        seekBarRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewRating.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        */



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


    @Override
    protected void onStart() {
        super.onStart();
        databaseSubject.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                subjects.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Subject subject = postSnapshot.getValue(Subject.class);
                    subjects.add(subject);
                }
                SubjectList subjectListAdapter = new SubjectList(SubjectActivity.this, subjects);
                ListViewSubject.setAdapter(subjectListAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

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
