package guilherme.tagtapmulti;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static guilherme.tagtapmulti.R.drawable.ic_help_black_24dp;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{


    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private ImageView tagTapClickable;
    private ImageView tagTapNotesClickable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tagTapImageClick();
        tagTapNotesImageClick();

        //Displaying Logo on Action Bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.tagtap_multi_icon_44);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        // that means current user will return null

        if(firebaseAuth.getCurrentUser() == null){

            //closing this activity
            finish();

            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.login_confirmation_text_view);
        buttonLogout = (Button) findViewById(R.id.logout_button);

        //displaying logged in user name
        textViewUserEmail.setText("Welcome: " + user.getEmail());

        //adding listener to button
        buttonLogout.setOnClickListener((View.OnClickListener) this);


        //Justifying TagTap Description Text using WebView
        WebView webViewTagtTap = (WebView) findViewById(R.id.tagtap_description);
        String tagTapDescription ="<font color=\"" + "#ffffff" + "\">" + "Save a URL address into a NFC Tag. From a modern way to show companies' products labels to your new bussiness card. Click on TagTap Logo and check it out!" + "</font>";
        webViewTagtTap.loadData("<p style=\"text-align: justify\">"+ tagTapDescription + "</p>", "text/html", "UTF-8");
        webViewTagtTap.setBackgroundColor(Color.TRANSPARENT);


        //Justifying TagTap Notes Description Text using WebView
        WebView webViewTagTapNotes = (WebView) findViewById(R.id.tagtap_notes_description);
        String tagTapNotesDescription = "<font color=\"" + "#ffffff" + "\">" + "Save a Text Message into a NFC Tag. Are you tired about dozens of post notes arround your workstation or on your fridge? Click on TagTap Notes Logo and feel the new experience!" + "</font>";
        webViewTagTapNotes.loadData("<p style=\"text-align: justify\">"+ tagTapNotesDescription + "</p>", "text/html", "UTF-8");
        webViewTagTapNotes.setBackgroundColor(Color.TRANSPARENT);



    }

    @Override
    public void onClick(View view) {

        // Adding Sound to the Add Sign In Button
        final MediaPlayer myMediabtnLogout = MediaPlayer.create(this, R.raw.button_sound2);

        //if logout is pressed
        if(view == buttonLogout){
            //logging out the user
            firebaseAuth.signOut();
            myMediabtnLogout.start();

            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


    //Making TagTap Image Clickable to open new activity of the app Tag&Tap
    public void tagTapImageClick(){
        tagTapClickable = (ImageView)findViewById(R.id.tagtap_image_link);
        tagTapClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tagTapNotesIntent = new Intent("guilherme.tagtapmulti.TagTapActivity");
                startActivity(tagTapNotesIntent);
            }
        });
    }


    //Making TagTap Notes Image Clickable to open new activity of the app Tag&Tap Notes
    public void tagTapNotesImageClick(){
        tagTapNotesClickable = (ImageView)findViewById(R.id.tagtapnotes_image_link);
        tagTapNotesClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tagTapNotesIntent = new Intent("guilherme.tagtapmulti.TagTapNotesActivity");
                startActivity(tagTapNotesIntent);
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
                goToUrl ( "https://github.com/guilhermefmello/TagTap_Multi");

                return true;

            case R.id.menu_info:

                //Building Alert Dialog to Show Information about TagTap Multi Application
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.info_menu_layout, null);
                dialogBuilder.setView(dialogView);


                dialogBuilder.setIcon(ic_help_black_24dp);
                dialogBuilder.setPositiveButton("OK", null);
                dialogBuilder.setTitle("Choosing an App!");
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

}