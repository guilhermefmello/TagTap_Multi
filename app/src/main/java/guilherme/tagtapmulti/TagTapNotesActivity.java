package guilherme.tagtapmulti;

import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class TagTapNotesActivity extends AppCompatActivity {

    NfcAdapter adapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag mytag;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_tap_notes);

        //Displaying Logo on Action Bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.tagtap_notes_menu_48x48);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        ctx=this;
        Button btnWrite = (Button) findViewById(R.id.button);
        // Adding Sound to the Button
        final MediaPlayer myMediabtSinglePlayer = MediaPlayer.create(this, R.raw.button_sound1);
        final TextView message = (TextView)findViewById(R.id.edit_message);

        btnWrite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                myMediabtSinglePlayer.start();

                try {
                    if(mytag==null){
                        Toast.makeText(ctx, ctx.getString(R.string.error_detected), Toast.LENGTH_LONG ).show();
                    }else{
                        write(message.getText().toString(),mytag);
                        Toast.makeText(ctx, ctx.getString(R.string.ok_writing), Toast.LENGTH_LONG ).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(ctx, ctx.getString(R.string.error_writing), Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                } catch (FormatException e) {
                    Toast.makeText(ctx, ctx.getString(R.string.error_writing) , Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                }
            }
        });



    }
}
