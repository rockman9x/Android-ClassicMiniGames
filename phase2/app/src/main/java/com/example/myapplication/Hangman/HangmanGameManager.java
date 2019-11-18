package com.example.myapplication.Hangman;

import android.app.Activity;

import com.example.myapplication.DBHandler;
import com.example.myapplication.GameManager;
import com.example.myapplication.GameStatus;

/** Game manager for this Hangman game. */
class HangmanGameManager extends GameManager {

  /** The singleton HangmanGameManager. */
  private static HangmanGameManager hangmanGameManager;

  private Activity activity;

  /** Constructs a HangmanGameManager. */
  private HangmanGameManager(Activity activity) {
    this.activity = activity;
  }

  /**
   * Getter for this HangmanGameManager instance.
   *
   * @return HangmanGameManager the instance of this HangmanGameManager.
   */
  static HangmanGameManager getInstance(Activity activity) {
    if (hangmanGameManager == null) {
      hangmanGameManager = new HangmanGameManager(activity);
    }
    return hangmanGameManager;
  }

  /**
   * Save the GameStatus for a particular user.
   *
   * @param gameStatus the new GameStatus want to get saved.
   */
  public void saveGame(GameStatus gameStatus) {
    DBHandler.getInstance(activity).saveGameStatus(gameStatus, DBHandler.Game.HANGMAN);
  }

  /**
   * Returns the gameStatus instance that this HangmanGameManager is managing.
   *
   * @param username the username of the user playing this game.
   * @return HangmanGameStat the HangmanGameStat instance of this user.
   */
  public HangmanGameStatInteractor getGameStatus(String username) {
    HangmanGameStatInteractor hangmanGameStat =
            (HangmanGameStatInteractor)
                    DBHandler.getInstance(activity)
                            .getGameStatus(username, DBHandler.Game.HANGMAN);

    if (hangmanGameStat == null) {
      hangmanGameStat = new HangmanGameStatInteractor(username);
    }

    return hangmanGameStat;
  }
}
