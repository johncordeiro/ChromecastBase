package br.com.ilhasoft.chromecast;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.libraries.cast.companionlibrary.cast.DataCastManager;

import br.com.ilhasoft.chromecast.application.ChromecastApplication;

/**
 * Created by johndalton on 08/12/14.
 */
public class DataActivity extends ActionBarActivity {


    private DataCastManager dataCastManager;
    private EditText etData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        dataCastManager = DataCastManager.getInstance();

        setupFields();
        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataCastManager.reconnectSessionIfPossible();
    }

    private void setupFields() {
        etData = (EditText) findViewById(R.id.etData);

        Button btSend = (Button) findViewById(R.id.btSend);
        btSend.setOnClickListener(onSendClickListener);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title));
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_data, menu);
        dataCastManager.addMediaRouterButton(menu, R.id.connect_media_router);

        return true;
    }

    private void sendMessage(String content) {
        try {
            dataCastManager.sendDataMessage(content, getString(R.string.message_namespace));
        } catch(Exception exception) {
            exception.printStackTrace();
            Toast.makeText(this, getString(R.string.message_error), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private View.OnClickListener onSendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!dataCastManager.isConnected()) {
                Toast.makeText(DataActivity.this, getString(R.string.connect_error), Toast.LENGTH_LONG)
                        .show();
                return;
            }

            sendMessage(etData.getText().toString());
        }
    };
}
