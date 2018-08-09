package com.example.user.nfctest;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null){

        }
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this,mPendingIntent,null,null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)
                ||intent.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED)
                ||intent.getAction().equals( NfcAdapter.ACTION_TECH_DISCOVERED)) {
            Log.d("NFCTest", "Tag Id : " + ByteArrayToHexString(intent
                    .getByteArrayExtra(NfcAdapter.EXTRA_ID)));
            Toast.makeText(this,ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)),Toast.LENGTH_LONG).show();

            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            //Log.i("(*’▽’)ノ＾—==ΞΞΞ☆", "沒事 ");
            NdefMessage[] msgs =null;
            if (rawMsgs != null) {
                Log.i("(*’▽’)ノ＾—==ΞΞΞ☆", "不是null ");
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {

                    msgs[i] = (NdefMessage) rawMsgs[i];

                }
                Log.i("(*’▽’)ノ＾—==ΞΞΞ☆",msgs[0].toString());
            } else {

                // Unknown tag type

                byte[] empty = new byte[]{};

                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);

                NdefMessage msg = new NdefMessage(new NdefRecord[]{

                        record

                });

                msgs = new NdefMessage[]{

                        msg

                };
            }

            byte[] payload = msgs[0].getRecords()[0].getPayload();
            String sm = new String(payload);
            //Log.i("(*’▽’)ノ＾—==ΞΞΞ☆",String.valueOf(payload.length));
        }
    }
    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F" };
        String out = "";
        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }
}
