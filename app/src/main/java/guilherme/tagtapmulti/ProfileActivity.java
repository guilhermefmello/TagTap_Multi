package guilherme.tagtapmulti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        textViewUserEmail.setText("Welcome "+user.getEmail());

        //adding listener to button
        buttonLogout.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View view) {
        //if logout is pressed
        if(view == buttonLogout){
            //logging out the user
            firebaseAuth.signOut();
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


}