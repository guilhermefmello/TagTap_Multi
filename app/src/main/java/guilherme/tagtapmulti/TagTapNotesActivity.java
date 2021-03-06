package guilherme.tagtapmulti;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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

public class TagTapNotesActivity extends AppCompatActivity {

    NfcAdapter adapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag mytag;
    Context ctx;

    //we will use these constants later to pass the artist name and id to another activity
    public static final String NOTES = "guilherme.tagtapmulti.TagTapNotesActivity.notes";
    public static final String NOTES_ID = "guilherme.tagtapmulti.TagTapNotesActivity.notesId";
    public static final String CATEGORY = "guilherme.tagtapmulti.TagTapNotesActivity.category";

    //view objects
    EditText editTextNotes;
    Spinner spinnerCategory;
    Button buttonSaveNotes;
    ListView listViewNotes;

    //a list to store all the artist from firebase database
    List<DailyNotes> Daily_Notes;

    //our database reference object
    DatabaseReference databaseDailyNotes;

    //Custom Text Variable for Notes Panel
    TextView notesPanelText;

    //Custom Text Variable for Write Daily Notes
    TextView writeDailyNotesText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_tap_notes);

        //Displaying Logo on Action Bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.tagtap_notes_menu_48x48);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        //Styling Text using assets Custom Text for Notes Panel
        notesPanelText = (TextView) findViewById(R.id.textView4);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/font3.ttf");
        notesPanelText.setTypeface(customFont);

        //Styling Text using assets Custom Text for Write Daily Notes
        writeDailyNotesText = (TextView) findViewById(R.id.textView);
        Typeface customFont2 = Typeface.createFromAsset(getAssets(), "fonts/font3.ttf");
        writeDailyNotesText.setTypeface(customFont2);



        ctx=this;
        Button btnWrite = (Button) findViewById(R.id.button);
        // Adding Sound to the Button
        final MediaPlayer myMediabtSinglePlayer = MediaPlayer.create(this, R.raw.button_sound1);
        final TextView message = (TextView)findViewById(R.id.edit_message);
        final Spinner spinner = (Spinner)findViewById(R.id.spinnerCategory);


        // Button to write the Message and Category in the NFC TAG
        btnWrite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                myMediabtSinglePlayer.start();

                try {
                    if(mytag==null){
                        Toast.makeText(ctx, "TAG NOT DETECTED!", Toast.LENGTH_LONG ).show();
                    }else{
                        write(message.getText().toString(), spinner.getSelectedItem().toString(),mytag);
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



        //getting the reference of notes node
        databaseDailyNotes = FirebaseDatabase.getInstance().getReference("Daily_Notes");

        //getting views
        editTextNotes = (EditText) findViewById(R.id.edit_message);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        listViewNotes = (ListView) findViewById(R.id.listViewNotes);
        buttonSaveNotes = (Button) findViewById(R.id.buttonSaveNotes);

        //list to store notes
        Daily_Notes = new ArrayList<>();

        // Adding Sound to the Add Note Button
        final MediaPlayer myMediabtnAddNote = MediaPlayer.create(this, R.raw.button_sound1);

        //adding an onclicklistener to the save notes button
        buttonSaveNotes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //calling the method saveNotes()
                // the method is defined below
                // this method is actually performing the write operation
                saveNotes();

                myMediabtnAddNote.start();
            }
        });




        //attaching listener to listview
        listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //getting the selected note
                DailyNotes dailynotes = Daily_Notes.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);

                //putting note and noteId to intent
                intent.putExtra(NOTES_ID, dailynotes.getNotesId());
                intent.putExtra(NOTES, dailynotes.getNotes());
                intent.putExtra(CATEGORY, dailynotes.getCategory());

                //starting the activity with intent
                startActivity(intent);


            }
        });


        // Adding Long Click to Update and Delete Alert Dialog
        listViewNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DailyNotes dailynotes = Daily_Notes.get(i);
                showUpdateDeleteDialog(dailynotes.getNotesId(), dailynotes.getNotes());
                return true;
            }
        });


    }


    /*
    * This method is saving a new notes to the
    * Firebase Realtime Database
    */

    private void saveNotes() {
        //getting the values to save
        String notes = editTextNotes.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        //checking if the value is provided
        if (!TextUtils.isEmpty(notes)) {
            //getting a unique id using push().getKey() method
            // it will create a unique id and we will use it as the Primary Key for our Notes
            String notesId = databaseDailyNotes.push().getKey();

            //creating an DailyNotes Object
            DailyNotes dailyNotes = new DailyNotes(notesId, notes, category);

            //Saving the notes
            databaseDailyNotes.child(notesId).setValue(dailyNotes);

            //setting edittext to blank again
            editTextNotes.setText("");

            //displaying a success toast
            Toast.makeText(this, "Notes Added to Database", Toast.LENGTH_LONG).show();
        } else {

            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a Note!", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        //attaching value event listener
        databaseDailyNotes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous notes list
                Daily_Notes.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    //getting notes
                    DailyNotes dailynotes = postSnapshot.getValue(DailyNotes.class);
                    //adding notes to the list
                    Daily_Notes.add(dailynotes);
                }

                //creating adapter
                NotesList notesAdapter = new NotesList(TagTapNotesActivity.this, Daily_Notes);
                //attaching adapter to the listview
                listViewNotes.setAdapter(notesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    // Updating Notes
    private boolean updateNotes(String notesId, String notes, String category) {
        //getting the specified notes reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Daily_Notes").child(notesId);

        //updating notes
        DailyNotes dailyNotes = new DailyNotes(notesId, notes, category);
        dR.setValue(dailyNotes);
        Toast.makeText(getApplicationContext(), "Note Updated!", Toast.LENGTH_LONG).show();
        return true;
    }

    //Deleting Notes
    private boolean deleteNotes(String id) {
        //getting the specified note reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Daily_Notes").child(id);

        //removing note
        dR.removeValue();

        //getting the subject reference for the specified note
        DatabaseReference drSubjects = FirebaseDatabase.getInstance().getReference("Subjects").child(id);

        //removing all subjects
        drSubjects.removeValue();
        Toast.makeText(getApplicationContext(), "Note Deleted!", Toast.LENGTH_LONG).show();
        return true;
    }


    // Building a Alert Dialog Box
    private void showUpdateDeleteDialog(final String notesId, String notes) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextNotes = (EditText) dialogView.findViewById(R.id.editTextNotes);
        final Spinner spinnerCategory = (Spinner) dialogView.findViewById(R.id.spinnerCategory);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateNotes);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteNotes);

        dialogBuilder.setTitle(notes);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String notes = editTextNotes.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();
                if (!TextUtils.isEmpty(notes)) {
                    updateNotes(notesId, notes, category);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*
                * we will code this method to delete the note
                * */
                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteNotes(notesId);
                        b.dismiss();
                    }
                });

            }
        });


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
                goToUrl ( "https://github.com/guilhermefmello/TagTap_Notes");

                return true;

            case R.id.menu_info:

                //Building Alert Dialog to Show Information about Notes Panel
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.info_menu_notes_layout, null);
                dialogBuilder.setView(dialogView);

                dialogBuilder.setIcon(ic_help_black_24dp);
                dialogBuilder.setPositiveButton("OK", null);
                dialogBuilder.setTitle("Using the Notes Panel!");
                final AlertDialog b = dialogBuilder.create();
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
    private void write(String text, String text2, Tag tag) throws IOException, FormatException {

        NdefRecord[] records = { createRecord("Message: " + text), createRecord("Category: " + text2) };
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