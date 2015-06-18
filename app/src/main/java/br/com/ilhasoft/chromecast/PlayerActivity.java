package br.com.ilhasoft.chromecast;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager;
import com.google.android.libraries.cast.companionlibrary.cast.exceptions.NoConnectionException;
import com.google.android.libraries.cast.companionlibrary.cast.exceptions.TransientNetworkDisconnectionException;
import com.google.android.libraries.cast.companionlibrary.utils.Utils;

public class PlayerActivity extends AppCompatActivity {

    private static final String TAG = "PlayerActivity";

    private static final int PLAY_DRAWABLE = R.drawable.ic_av_play_light;
    private static final int PAUSE_DRAWABLE = R.drawable.ic_av_pause_light;

    private static final float DEFAULT_VOLUME_INCREMENT = 0.05f;

    private VideoCastManager castManager;

    private Button btPlayPause;

    private TextView tvTime;
    private SeekBar seekTime;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        castManager = VideoCastManager.getInstance();

        loadMediaFromExtras(getIntent().getExtras());
        setupViews();
        updateSeekbarDelayed();
    }

    private void updateSeekbarDelayed() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    int position = (int) castManager.getCurrentMediaPosition();
                    int duration = (int) castManager.getMediaDuration();

                    updateSeekbar(position, duration);
                    updateSeekbarDelayed();
                } catch(Exception exception) {
                    Log.e(TAG, "run ", exception);
                }
            }
        }, 1000);
    }

    private void setupViews() {
        tvTime = (TextView) findViewById(R.id.tvTime);

        btPlayPause = (Button) findViewById(R.id.btPlayPause);
        btPlayPause.setOnClickListener(onPlayPauseClickListener);

        Button btStop = (Button) findViewById(R.id.btStop);
        btStop.setOnClickListener(onStopClickListener);

        Button btMute = (Button) findViewById(R.id.btMute);
        btMute.setOnClickListener(onMuteClickListener);

        seekTime = (SeekBar) findViewById(R.id.seekTime);
        seekTime.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    private void loadMediaFromExtras(Bundle extras) {
        MediaInfo mediaInfo = Utils.bundleToMediaInfo(extras.getBundle("media"));
        boolean shouldStart = extras.getBoolean("shouldStart");
        int position = extras.getInt("position");

        try {
            if(!castManager.isRemoteMediaPlaying()) {
                castManager.loadMedia(mediaInfo, shouldStart, position);
            }
            updatePlaybackStatus();
        } catch(Exception exception) {
            Log.e(TAG, "onCreate ", exception);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (castManager.onDispatchVolumeKeyEvent(event, DEFAULT_VOLUME_INCREMENT)) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void showErrorAlert() {
        Toast.makeText(this, "Operation unavailable.", Toast.LENGTH_LONG).show();
    }

    private View.OnClickListener onPlayPauseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                castManager.togglePlayback();
                updatePlaybackStatus();
            } catch (Exception exception) {
                showErrorAlert();
            }
        }
    };

    private void updatePlaybackStatus() {
        switch(castManager.getMediaStatus().getPlayerState()) {
            case MediaStatus.PLAYER_STATE_PLAYING:
                btPlayPause.setBackgroundResource(PLAY_DRAWABLE);
                break;
            case MediaStatus.PLAYER_STATE_PAUSED:
                btPlayPause.setBackgroundResource(PAUSE_DRAWABLE);
        }
    }

    public void updateSeekbar(int position, int duration) {
        tvTime.setText(Utils.formatMillis(position));

        seekTime.setProgress(position);
        seekTime.setMax(duration);
    }

    private View.OnClickListener onStopClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                castManager.stop();
                finish();
            } catch (Exception exception) {
                showErrorAlert();
            }
        }
    };

    private View.OnClickListener onMuteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (castManager.isMute()) {
                    castManager.setMute(false);
                } else {
                    castManager.setMute(true);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            try {
                castManager.seek(seekBar.getProgress());
            } catch (Exception exception) {
                Log.e(TAG, "onStopTrackingTouch ", exception);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
    };
}
