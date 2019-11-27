package com.example.myapplication.Hangman;import android.app.Service;import android.content.Intent;import android.media.MediaPlayer;import android.os.Binder;import android.os.IBinder;import android.util.Log;import android.widget.Toast;import com.example.myapplication.R;public class HangmanBackgroundMusic extends Service {  // creating a mediaplayer object  private MediaPlayer player;  @Override  public IBinder onBind(Intent intent) {    return null;  }  public void onCreate() {    super.onCreate();    player = MediaPlayer.create(this, R.raw.hangman_music);    player.setLooping(true); // Set looping    player.setVolume(100, 100);    Log.e("ONCREATE", "ONCREATE");  }  @Override  public int onStartCommand(Intent intent, int flags, int startId) {    // getting systems default ringtone    Log.e("ONSTARTCOMMAND", "ONSTARTCOMMAND");    // we have some options for service    // start sticky means service will be explicity started and stopped    player.start();    return START_REDELIVER_INTENT;  }  @Override  public void onDestroy() {    super.onDestroy();    Log.e("ONDESTOY", "ONDESTOY");    // stopping the player when service is destroyed    player.stop();    player.release();  }}