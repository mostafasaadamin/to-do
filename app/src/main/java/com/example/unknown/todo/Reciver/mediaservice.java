package com.example.unknown.todo.Reciver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.unknown.todo.R;

import java.io.IOException;

/**
 * Created by unknown on 7/6/2018.
 */

public class mediaservice extends Service {
    AudioManager myAudioManager;
    MediaPlayer mPlayer;
    Ringtone r;
    MediaPlayer mp;
    String whichone="source";
    @Override
    public void onCreate() {
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Uri myUri = null;
        mPlayer = new MediaPlayer();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        SharedPreferences sharedPreference = getApplication().getSharedPreferences("userSetting", Context.MODE_PRIVATE);
        String uri = sharedPreference.getString("uri", "noLink");
        if (!uri.equals("noLink")) {
            myUri = Uri.parse(uri);
            play_media(myUri);

        }
        else
            {
                play_default_media();
            }

        return START_STICKY;
    }
    private void play_default_media() {
        mp=MediaPlayer.create(this, R.raw.sound);
        mp.start();
        mp.setLooping(true);
        whichone="default";
    }
    @Override
    public void onDestroy()
    {
        if(whichone.equals("source")) {
            mPlayer.stop();
        }
        else {
            mp.stop();
        }
    }
    public  void play_media(Uri myUri )
    {
        try {
            mPlayer.setDataSource(getApplicationContext(), myUri);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mPlayer.start();
        mPlayer.setLooping(true);
    }
}
