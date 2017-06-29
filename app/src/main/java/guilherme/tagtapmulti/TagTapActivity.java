package guilherme.tagtapmulti;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class TagTapActivity extends AppCompatActivity {

    private static final String TAG = "NFCWriteTag";
    private NfcAdapter mNfcAdapter;
    private IntentFilter[] mWriteTagFilters;
    private PendingIntent mNfcPendingIntent;
    private boolean silent=false;
    private boolean writeProtect = false;
    private Context context;

    private Button btnWrite;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_tap);

        //Displaying Logo on Action Bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.tagtap_logo_48);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        /*
         * TRYING TO FIND A TAG:
         * getting the default NFC adapter;
         * Aren't creating multiple instances of the same application (using FLAG_ACTIVITY_SINGLE_TOP);
         * Pending Intent performing action at a later time;
         * Adding Flags to modify the default behavior of this activity;
         * The IntentFilter advertising the types of intents that they can respond;
         */

        context = getApplicationContext();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);
        IntentFilter discovery=new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        // Intent filters for writing to a tag
        mWriteTagFilters = new IntentFilter[] { discovery };


        



    }
}
