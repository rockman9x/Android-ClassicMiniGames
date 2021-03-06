package com.example.myapplication.flappyfish.flappygameactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.backgroundmusic.BackgroundMusic;
import com.example.myapplication.flappyfish.flappygamedomain.FlappyGameManager;
import com.example.myapplication.flappyfish.flappygamemusic.FlappyBackgroundMusic;
import com.example.myapplication.flappyfish.flappygamestatus.FlappyGameStatusFacade;
import com.example.myapplication.flappyfish.flappygameview.FlappyGameViewFacade;
import com.example.myapplication.mainpage.GameMain;
import com.example.myapplication.R;

/** The result page that is shown when the game finishes. */
public class FlappyResultActivity extends AppCompatActivity implements View.OnClickListener {

  /** The status of this game which belongs to the current user. */
  private FlappyGameStatusFacade gameStatus;

  /**
   * Initializes this result page.
   *
   * @param savedInstanceState a bundle of the resources in this activity.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_flappy_result);

    // The intent that took the previous activity to this result page.
    Intent resultIntent = getIntent();
    gameStatus = resultIntent.getParcelableExtra(FlappyGameViewFacade.EXTRA_MESSAGE);
    boolean result = gameStatus.getLifeCount() == 0;
    setResultText(result);
    setPlayAgainBtn(result);
    setNextStageBtn(result);
    setBackToMainBtn();
  }

  /**
   * Set up the next stage button which when clicked, takes the player to the next stage.
   *
   * @param result whether the player has finished the current stage or ran out of lives.
   */
  private void setNextStageBtn(boolean result) {
    Button nextStageBtn = findViewById(R.id.nextStageBtn);
    nextStageBtn.setOnClickListener(this);
    if (result) {
      nextStageBtn.setVisibility(View.GONE);
    } else {
      nextStageBtn.setVisibility(View.VISIBLE);
    }
  }

  /** Initializes the textview object that displays the player's final score. */
  private void setResultText(boolean result) {
    // The text the displays the player's final score.
    TextView resultText = findViewById(R.id.result);
    resultText.setOnClickListener(this);
    String resultTextStr;
    if (result) {
      resultTextStr = "Your score is " + gameStatus.getScore();
    } else {
      resultTextStr = "Congratulation! You finished stage " + (gameStatus.getStage() - 1);
    }
    resultText.setText(resultTextStr);
  }

  /** Initializes the button that plays the game again. */
  private void setPlayAgainBtn(boolean result) {
    Button playAgainBtn = findViewById(R.id.playAgainBtn);
    playAgainBtn.setOnClickListener(this);
    if (result) {
      playAgainBtn.setVisibility(View.VISIBLE);
    } else {
      playAgainBtn.setVisibility(View.GONE);
    }
  }

  /** Initializes the button that returns to the menu of different games. */
  private void setBackToMainBtn() {
    // The button that takes the user to the menu of different games when clicked.
    Button backToMainBtn = findViewById(R.id.backToMainBtn);
    backToMainBtn.setOnClickListener(this);
  }

  /** Pauses this activity. */
  @Override
  protected void onPause() {
    super.onPause();
    if (BackgroundMusic.isApplicationSentToBackground(this)) {
      stopService(new Intent(this, FlappyBackgroundMusic.class));
    }
    FlappyGameManager gameManager = FlappyGameManager.getInstance(this);
    gameManager.saveGame(gameStatus);
  }

  /**
   * Events that takes place when any of the buttons are clicked.
   *
   * @param view the review that is responsible for event handling.
   */
  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.playAgainBtn:
        Intent playAgainIntent = new Intent(this, FlappyGameMenu.class);
        playAgainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        playAgainIntent.putExtra("user", gameStatus.getName());
        gameStatus.finishUpdate();
        startActivity(playAgainIntent);
        finish();
        break;
      case R.id.nextStageBtn:
        Intent nextStageIntent = new Intent(this, FlappyMainActivity.class);
        nextStageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        nextStageIntent.putExtra("gameStatus", gameStatus);
        startActivity(nextStageIntent);
        finish();
        break;
      case R.id.backToMainBtn:
        stopService(new Intent(this, FlappyBackgroundMusic.class));
        Intent backToMainIntent = new Intent(this, GameMain.class);
        backToMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        backToMainIntent.putExtra("user", gameStatus.getName());
        if (gameStatus.getLifeCount() == 0) {
          gameStatus.finishUpdate();
        }
        startActivity(backToMainIntent);
        finish();
        break;
    }
  }

  /** Resumes this activity and checks whether the background music should be played or not. */
  @Override
  protected void onResume() {
    super.onResume();
    if (FlappyGameMenu.isPlaying) {
      startService(new Intent(this, FlappyBackgroundMusic.class));
    }
  }
}
