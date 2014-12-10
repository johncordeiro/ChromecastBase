package br.com.ilhasoft.chromecast.application;

import android.app.Application;
import android.content.Context;

import com.google.sample.castcompanionlibrary.cast.DataCastManager;
import com.google.sample.castcompanionlibrary.cast.VideoCastManager;

import br.com.ilhasoft.chromecast.PlayerActivity;
import br.com.ilhasoft.chromecast.R;

/**
 * Created by johndalton on 04/12/14.
 */
public class ChromecastApplication extends Application {

    private static String MEDIA_APPLICATION_ID = "A70FE66C";
//    private static String DATA_APPLICATION_ID = "794B7BBF";
    private static String DATA_APPLICATION_ID = "C7907A34";


    private static VideoCastManager videoCastManager;
    private static DataCastManager dataCastManager;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static VideoCastManager getVideoCastManager(Context context) {
        if(videoCastManager == null) {
            videoCastManager = VideoCastManager.initialize(context, MEDIA_APPLICATION_ID, PlayerActivity.class, null);
            videoCastManager.enableFeatures(VideoCastManager.FEATURE_NOTIFICATION
                    | VideoCastManager.FEATURE_LOCKSCREEN
                    | VideoCastManager.FEATURE_WIFI_RECONNECT);
        }
        videoCastManager.setContext(context);
        return videoCastManager;
    }

    public static DataCastManager getDataCastManager(Context context) {
        if(dataCastManager == null) {
            dataCastManager = DataCastManager.initialize(context, DATA_APPLICATION_ID, null);
        }
        dataCastManager.setContext(context);
        return dataCastManager;
    }

}
