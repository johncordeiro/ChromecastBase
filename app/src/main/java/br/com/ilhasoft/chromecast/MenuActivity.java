package br.com.ilhasoft.chromecast;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MenuActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btPlayMedia = (Button) findViewById(R.id.btPlayMedia);
        btPlayMedia.setOnClickListener(onPlayMediaClickListener);

        Button btSendData = (Button) findViewById(R.id.btSendData);
        btSendData.setOnClickListener(onSendDataClickListener);
    }

    private View.OnClickListener onPlayMediaClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent playIntent = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(playIntent);
            finish();
        }
    };

    private View.OnClickListener onSendDataClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent dataIntent = new Intent(MenuActivity.this, DataActivity.class);
            startActivity(dataIntent);
            finish();
        }
    };
}
