package guilherme.tagtapmulti;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static guilherme.tagtapmulti.R.drawable.ic_help_black_24dp;
import static guilherme.tagtapmulti.R.id.listViewSubject;

public class SubjectActivity extends AppCompatActivity {

    TextView textViewNotes, textViewRating;
    EditText editTextSubject;
    SeekBar seekBarRating;
    ListView ListViewSubject;
    Button buttonAddSubject;

    DatabaseReference databaseSubject;

    List<Subject> subjects;


    NfcAdapter adapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag mytag;
    Context ctx;


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

        // Setting the selected saved Message and Category into TextView
        textViewNotes.setText(intent.getStringExtra(TagTapNotesActivity.NOTES));
        textViewNotes.setText(intent.getStringExtra(TagTapNotesActivity.CATEGORY));


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





        String id = intent.getStringExtra(TagTapNotesActivity.NOTES_ID);
        String notes = intent.getStringExtra(TagTapNotesActivity.NOTES);
        String category = intent.getStringExtra(TagTapNotesActivity.CATEGORY);

        // Displaying the selected saved message into TextView
        textViewNotes.setText("Note: " + notes + "\n" + "Category: " + category);

        databaseSubject = FirebaseDatabase.getInstance().getReference("Subjects").child(id);

        // Adding Sound to the Add Subject Button
        final MediaPlayer myMediabtnAddSubject = MediaPlayer.create(this, R.raw.button_sound1);


        //Button Save Subject and Priority to DataBase
        buttonAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myMediabtnAddSubject.start();
                saveSubject();

            }
        });





        ctx=this;
        Button btnWriteTag2 = (Button) findViewById(R.id.buttonWriteTag2);
        // Adding Sound to the Button
        final MediaPlayer myMediabtSinglePlayer = MediaPlayer.create(this, R.raw.button_sound1);
        final TextView message = (TextView)findViewById(R.id.textViewNotes);
        final TextView textSubject = (TextView)findViewById(R.id.editTextSubject);
        final TextView textRating = (TextView)findViewById(R.id.textViewRating);



        // Button to write the Message and Category in the NFC TAG
        btnWriteTag2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                myMediabtSinglePlayer.start();

                try {
                    if(mytag==null){
                        Toast.makeText(ctx, "TAG NOT DETECTED!", Toast.LENGTH_LONG ).show();
                    }else{
                        write(message.getText().toString(), "Subject: " + textSubject.getText().toString(), "Priority (1-5): " + textRating.getText().toString(), mytag);
                        Toast.makeText(ctx, "TAG HAS BEEN WRITTEN!", Toast.LENGTH_LONG ).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(ctx, "ERROR DURING WRITING! MAKE SURE THAT YOUR TAG IS CLOSE", Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                } catch (FormatException e) {
                    Toast.makeText(ctx, "ERROR DURING WRITING! MAKE SURE THAT YOUR TAG IS CLOSE", Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                }
            }
        });

        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected };

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



    //Saving Subject to Database
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



    //Inflating a Menu.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tagtap_multi_menu, menu);
        return true;
    }

    //Adding a Action to the Option in the Menu Bar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item:
                goToUrl ( "https://github.com/guilhermefmello/TagTap_Multi");

                return true;

            case R.id.menu_info:

                //Building Alert Dialog to Show Information about Subjects Panel
                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.info_menu_subjects_layout, null);
                dialogBuilder.setView(dialogView);

                dialogBuilder.setIcon(ic_help_black_24dp);
                dialogBuilder.setPositiveButton("OK", null);
                dialogBuilder.setTitle("Using the Subjects Panel!");
                final android.app.AlertDialog b = dialogBuilder.create();
                b.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Getting the URL and Open the page using the browser
    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    // Writing the Message and Category into the NFC TAG
    private void write(String text, String text2, String text3, Tag tag) throws IOException, FormatException {

        NdefRecord[] records = { createRecord(text), createRecord(text2), createRecord(text3) };
        NdefMessage message = new NdefMessage(records);
        // Get an instance of Ndef for the tag.
        Ndef ndef = Ndef.get(tag);
        // Enable I/O
        ndef.connect();
        // Write the message
        ndef.writeNdefMessage(message);
        // Close the connection
        ndef.close();
    }



    // Creating NDEF Message to write into the NFC TAG
    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang       = "en";
        byte[] textBytes  = text.getBytes();
        byte[] langBytes  = lang.getBytes("US-ASCII");
        int    langLength = langBytes.length;
        int    textLength = textBytes.length;
        byte[] payload    = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1,              langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,  new byte[0], payload);

        return recordNFC;
    }

    @Override
    protected void onNewIntent(Intent intent){
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Toast.makeText(this, "TAG DETECTED!" + mytag.toString(), Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }

    private void WriteModeOn(){
        writeMode = true;
        adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }

    private void WriteModeOff(){
        writeMode = false;
        adapter.disableForegroundDispatch(this);
    }


}
