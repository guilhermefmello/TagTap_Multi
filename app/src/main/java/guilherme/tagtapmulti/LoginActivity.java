package guilherme.tagtapmulti;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    //defining views
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Displaying Logo on Action Bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.tagtap_multi_icon_44);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        // means user is already logged in

        if(firebaseAuth.getCurrentUser() != null){

            //close this activity
            finish();

            //opening profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.enter_email_login_edit_text);
        editTextPassword = (EditText) findViewById(R.id.enter_password_login_edit_text);
        buttonSignIn = (Button) findViewById(R.id.signin_login_button);
        textViewSignup= (TextView) findViewById(R.id.signup_here_login_text_view);
        progressDialog = new ProgressDialog(this);

        //attaching click listener
        buttonSignIn.setOnClickListener((View.OnClickListener) this);
        textViewSignup.setOnClickListener((View.OnClickListener) this);
    }

    //method for user login
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password= editTextPassword.getText().toString().trim();

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

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }
                    }
                });
    }



    @Override
    public void onClick(View view) {

        // Adding Sound to the Add Sign In Button
        final MediaPlayer myMediabtnSignIn = MediaPlayer.create(this, R.raw.button_sound1);

        if(view == buttonSignIn){
            userLogin();
            myMediabtnSignIn.start();
        }
        if(view == textViewSignup){
            finish();
            startActivity(new Intent(this, MainActivity.class));
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

                //Building Alert Dialog to Show Information about User Login
                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.info_menu_login_layout, null);
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