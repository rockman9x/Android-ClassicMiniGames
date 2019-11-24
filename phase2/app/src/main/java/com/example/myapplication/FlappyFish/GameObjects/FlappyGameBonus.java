package com.example.myapplication.FlappyFish.GameObjects;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplication.FlappyFish.FlappyGameStatus;

import java.util.Random;

public class FlappyGameBonus extends FlappyGameObjects implements Parcelable {

  /** The default speed of the bonus item for easy mode. */
  private static final int BONUS_SPEED_EASY = 20;

  /** The default speed of the bonus item for hard mode. */
  private static final int BONUS_SPEED_HARD = 30;

  /** The default x coordinate of an object when it collides with the fish. */
  private static final int DEAD_POS = -100;


  private Random random = new Random();

  /**
   * Construct a new bonus object at the default starting coordinates with a velocity as default.
   */
  public FlappyGameBonus() {
    super(0, 0, BONUS_SPEED_EASY);
  }

  /**
   * Build the bonus object from Parcel.
   *
   * @param in the parcel that stores the previously saved shrimp object.
   */
  public FlappyGameBonus(Parcel in) {
    super(in);
  }

  /** Set the game difficulty level to easy by adjusting the bonus item's velocity. */
  public void setGameEasy() {
    setVelocity(BONUS_SPEED_EASY);
  }

  /** Set the game difficulty level to hard by adjusting the bonus item's velocity. */
  public void setGameHard() {
    setVelocity(BONUS_SPEED_HARD);
  }

  /** Move the bonus item according to its current velocity. */
  public void move() {
    setX(getX() - getVelocity());
  }

  /**
   * Update the bonus item according to the specified game status and ensure the bonus item does not
   * move out of the screen using canvasWidth, minY and maxY.
   *
   * @param gameStatus the game status object that tracks this bonus item.
   * @param canvasWidth the width of the canvas this bonus item is drawn on.
   * @param minY the minimum value for this bonus item's y coordinate.
   * @param maxY the maximum value for this bonus item's y coordinate.
   */
  public boolean update(FlappyGameStatus gameStatus, int canvasWidth, int minY, int maxY) {
    if (collideCheck(gameStatus)) {
//      gameStatus.updateScore();
      kill();
      return true;
    }
    validCheck(canvasWidth, minY, maxY);
    return false;
  }

  /**
   * Ensure the bonus item does not move out of the screen.
   *
   * @param minY the minimum value for this bonus item's y coordinate.
   * @param maxY the maximum value for this bonus item's y coordinate.
   */
  private void validCheck(int canvasWidth, int minY, int maxY) {
    int randomNum = random.nextInt(200);
    if (getX() < 0 && randomNum == 100) {
      setX(canvasWidth + 10);
      setY((int) Math.floor(Math.random() * (maxY - minY)) + minY);
    }
  }

  /** Set the x coordinate of the bonus item to dead position. */
  private void kill() {
    setX(DEAD_POS);
  }

  /**
   * Check whether the specified fish object collides with this bonus object.
   *
   * @param gameStatus the gameStatus object that tracks the two game objects.
   * @return Return true if obj collides with the fish object; Otherwise, return false.
   */
  private boolean collideCheck(FlappyGameStatus gameStatus) {
    FlappyGameFish fish = gameStatus.fish;
    FlappyGameBonus bonus = gameStatus.bonus;
    int fishX = fish.getX();
    int fishY = fish.getY();
    int bonusX = bonus.getX();
    int bonusY = bonus.getY();
    return fishX < bonusX
        && bonusX < (fishX + fish.getWidth())
        && (fishY - bonus.getHeight()) < bonusY
        && bonusY < (fishY + fish.getHeight());
  }

  /** Binds the FlappyGameBonus object. */
  public static final Creator<FlappyGameBonus> CREATOR =
      new Parcelable.Creator<FlappyGameBonus>() {
        @Override
        public FlappyGameBonus createFromParcel(Parcel in) {
          return new FlappyGameBonus(in);
        }

        @Override
        public FlappyGameBonus[] newArray(int size) {
          return new FlappyGameBonus[size];
        }
      };
}
