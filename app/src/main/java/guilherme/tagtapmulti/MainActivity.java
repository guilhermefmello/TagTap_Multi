package guilherme.tagtapmulti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static guilherme.tagtapmulti.R.drawable.ic_help_black_24dp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    //Custom Text Variable for User Registration
    TextView userRegistrationText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Displaying Logo on Action Bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.tagtap_multi_icon_44);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        //Styling Text using assets Custom Text for User Registration
        userRegistrationText = (TextView) findViewById(R.id.user_registration_text_view);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/font3.ttf");
        userRegistrationText.setTypeface(customFont);


            //initializing firebase auth object
            firebaseAuth = FirebaseAuth.getInstance();

            //if getCurrentUser does not returns null
            if(firebaseAuth.getCurrentUser() != null){
                //that means user is already logged in
                // so close this activity
                finish();
                //and open profile activity
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));}

            //initializing views
            editTextEmail = (EditText) findViewById(R.id.enter_email_edit_text);
            editTextPassword = (EditText) findViewById(R.id.enter_password_edit_text);
            textViewSignin = (TextView) findViewById(R.id.signin_here_text_view);
            buttonSignup = (Button) findViewById(R.id.signup_button);
            progressDialog = new ProgressDialog(this);

            //attaching listener to button
            buttonSignup.setOnClickListener((View.OnClickListener) this);
            textViewSignin.setOnClickListener((View.OnClickListener) this);
    }

    private void registerUser(){

            //getting email and password from edit texts
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            //checking if email and passwords are empty
            if(TextUtils.isEmpty(email)){
                Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
                return;
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
                return;
            }

            //if the email and password are not empty
        // displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }else{
                            //display some message here
                            Toast.makeText(MainActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }



    @Override
        public void onClick(View view) {

        // Adding Sound to the Add Sign Up Button
        final MediaPlayer myMediabtnSignUp = MediaPlayer.create(this, R.raw.button_sound1);

            if(view == buttonSignup){
                registerUser();
                myMediabtnSignUp.start();

            }
            if(view == textViewSignin){
                //open login activity when user taps on the already registered textview
                startActivity(new Intent(this, LoginActivity.class));
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

                //Building Alert Dialog to Show Information about User Registration
                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.info_menu_signup_registration_layout, null);
                dialogBuilder.setView(dialogView);

                dialogBuilder.setIcon(ic_help_black_24dp);
                dialogBuilder.setPositiveButton("OK", null);
                dialogBuilder.setTitle("Welcome!");
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



}

