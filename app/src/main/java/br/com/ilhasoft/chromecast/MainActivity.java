package br.com.ilhasoft.chromecast;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.TextTrackStyle;
import com.google.android.gms.common.images.WebImage;
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager;
import com.google.android.libraries.cast.companionlibrary.utils.Utils;
import com.google.android.libraries.cast.companionlibrary.widgets.MiniController;

import java.util.ArrayList;
import java.util.List;

import br.com.ilhasoft.chromecast.application.ChromecastApplication;

public class MainActivity extends ActionBarActivity {

    private VideoCastManager videoCastManager;

    private EditText etUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoCastManager = VideoCastManager.getInstance();

        setupFiedls();
        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoCastManager.reconnectSessionIfPossible();
        videoCastManager.incrementUiCounter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoCastManager.decrementUiCounter();
    }

    private void setupFiedls() {
        etUrl = (EditText) findViewById(R.id.etUrl);

        MiniController miniController = (MiniController) findViewById(R.id.miniController);
        videoCastManager.addMiniController(miniController);

        Button btPlay = (Button) findViewById(R.id.btPlayPause);
        btPlay.setOnClickListener(onPlayClickListener);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title));
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);
        videoCastManager.addMediaRouterButton(menu, R.id.connect_media_router);

        return true;
    }

    private View.OnClickListener onPlayClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!videoCastManager.isConnected()) {
                Toast.makeText(MainActivity.this, getString(R.string.connect_error), Toast.LENGTH_LONG)
                     .show();
                return;
            }

            String content = etUrl.getText().toString();
            MediaInfo mediaInfo = buildMediaInfo(content
                    ,"video"
                    ,"Title Example"
                    ,"Subtitle example"
                    ,"Studio Example"
                    ,"http://www.technobuffalo.com/wp-content/uploads/2013/08/cheapcast_logo.png"
                    ,"http://www.technobuffalo.com/wp-content/uploads/2013/08/cheapcast_logo.png");

            //Default player
            videoCastManager.startVideoCastControllerActivity(MainActivity.this, mediaInfo, 0, true);

            //My own player
            playOnMyActivity(mediaInfo);

        }
    };

    private void playOnMyActivity(MediaInfo mediaInfo) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("media", Utils.mediaInfoToBundle(mediaInfo));
        intent.putExtra("startPoint", 0);
        intent.putExtra("shouldStart", true);
        startActivity(intent);
    }

    private MediaInfo buildMediaInfo(String mediaUrl, String contentType, String title, String subtitle, String studio, String imgUrl, String bigImgUrl) {
//        List<MediaTrack> mediaTracks = buildMediaTracks();
//        TextTrackStyle textTrackStyle = TextTrackStyle.fromSystemSettings(this);

        MediaMetadata metadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

        metadata.putString(MediaMetadata.KEY_TITLE, title);
        metadata.putString(MediaMetadata.KEY_SUBTITLE, subtitle);
        metadata.putString(MediaMetadata.KEY_STUDIO, studio);
        metadata.addImage(new WebImage(Uri.parse(imgUrl)));
        metadata.addImage(new WebImage(Uri.parse(bigImgUrl)));

        return new MediaInfo.Builder(mediaUrl)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                            .setContentType(contentType)
//                            .setMediaTracks(mediaTracks)
//                            .setTextTrackStyle(textTrackStyle)
                .setMetadata(metadata)
                            .build();
    }

    private List<MediaTrack> buildMediaTracks() {
        MediaTrack mediaTrack = new MediaTrack.Builder(1, MediaTrack.TYPE_TEXT)
                                                .setName("English")
                                                .setSubtype(MediaTrack.SUBTYPE_SUBTITLES)
                                                .setContentId("https://dl.dropboxusercontent.com/u/33384342/sub.vtt")
                                                .setLanguage("en-US")
                                                .build();

        List<MediaTrack> mediaTracks = new ArrayList<MediaTrack>();
        mediaTracks.add(mediaTrack);
        return mediaTracks;
    }
}
