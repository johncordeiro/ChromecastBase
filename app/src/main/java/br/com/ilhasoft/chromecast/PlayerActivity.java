package br.com.ilhasoft.chromecast;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.cast.MediaStatus;
import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.player.IVideoCastController;
import com.google.sample.castcompanionlibrary.cast.player.OnVideoCastControllerListener;
import com.google.sample.castcompanionlibrary.cast.player.VideoCastControllerFragment;
import com.google.sample.castcompanionlibrary.utils.Utils;

import br.com.ilhasoft.chromecast.application.ChromecastApplication;

public class PlayerActivity extends ActionBarActivity implements IVideoCastController {

    private static final int PLAY_DRAWABLE = R.drawable.ic_av_play_light;
    private static final int PAUSE_DRAWABLE = R.drawable.ic_av_pause_light;

    private static final float DEFAULT_VOLUME_INCREMENT = 0.05f;

    private VideoCastManager castManager;

    private ImageView background;
    private Button btPlayPause;

    private VideoCastControllerFragment videoCastControllerFragment;
    private TextView tvTime;
    private SeekBar seekTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        castManager = ChromecastApplication.getVideoCastManager(this);

        videoCastControllerFragment = VideoCastControllerFragment.newInstance(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(videoCastControllerFragment, "task").commit();

        background = (ImageView) findViewById(R.id.background);
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

    @Override
    public void setImage(Bitmap bitmap) {
        background.setImageBitmap(bitmap);
    }

    @Override
    public void setLine1(String text) {

    }

    @Override
    public void setLine2(String text) {

    }

    @Override
    public void setPlaybackStatus(int state) {
        switch(state) {
            case MediaStatus.PLAYER_STATE_PLAYING:
                btPlayPause.setBackgroundResource(PAUSE_DRAWABLE);
                break;
            case MediaStatus.PLAYER_STATE_PAUSED:
                btPlayPause.setBackgroundResource(PLAY_DRAWABLE);
        }
    }

    @Override
    public void setOnVideoCastControllerChangedListener(OnVideoCastControllerListener listener) {

    }

    @Override
    public void setStreamType(int streamType) {

    }

    @Override
    public void updateSeekbar(int position, int duration) {
        tvTime.setText(Utils.formatMillis(position));

        seekTime.setProgress(position);
        seekTime.setMax(duration);
    }

    @Override
    public void updateControllersStatus(boolean enabled) {

    }

    @Override
    public void showLoading(boolean visible) {

    }

    @Override
    public void closeActivity() {

    }

    @Override
    public void adjustControllersForLiveStream(boolean isLive) {

    }

    @Override
    public void updateClosedCaption(int status) {

    }

    private View.OnClickListener onPlayPauseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                castManager.togglePlayback();
            } catch (Exception exception) {
                showErrorAlert();
            }
        }
    };

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
            videoCastControllerFragment.onStopTrackingTouch(seekBar);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            videoCastControllerFragment.onStartTrackingTouch(seekBar);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            videoCastControllerFragment.onProgressChanged(seekBar, progress, fromUser);
        }
    };
}
