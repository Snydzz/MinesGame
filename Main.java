import java.net.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.scene.image.*;
import java.io.*;

import java.util.*;
import java.text.*;
import java.io.*;
import java.lang.*;
import javafx.application.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import java.net.*;
import javafx.geometry.*;

public class Main extends Application {
    private FlowPane fp;
    private Canvas theCanvas = new Canvas(600, 600);
    private ArrayList<Mine> mines = new ArrayList<>();
    private Player thePlayer;
    private GraphicsContext gc;
    private Image background = new Image("stars.png");
    private Image overlay = new Image("starsoverlay.png");
    private Random rand = new Random();
    private boolean up, down, left, right;
    Player testPoint = new Player(300, 300);
    Player originPoint = new Player(300, 300);
    private double score = 0;
    private int highScore = 0; 

    int lastGridX = 0;
    int lastGridY = 0;

    @Override
    public void start(Stage stage) {
        fp = new FlowPane();
        fp.getChildren().add(theCanvas);
        gc = theCanvas.getGraphicsContext2D();

        thePlayer = new Player(300, 300);

        loadHighScore(); // Load high score at the beginning

        AnimationHandler animationHandler = new AnimationHandler();
        animationHandler.start();

        Scene scene = new Scene(fp, 600, 600);
        stage.setScene(scene);
        stage.setTitle("Project :)");
        stage.show();

        scene.setOnKeyPressed(new KeyListenerDown());
        scene.setOnKeyReleased(new KeyListenerUp());
    }

    // Draw the background
    private void drawBackground(float playerx, float playery) {
        playerx *= 0.1;
        playery *= 0.1;

        float x = playerx / 400;
        float y = playery / 400;

        int xi = (int) x;
        int yi = (int) y;

        for (int i = xi - 3; i < xi + 3; i++) {
            for (int j = yi - 3; j < yi + 3; j++) {
                gc.drawImage(background, -playerx + i * 400, -playery + j * 400);
            }
        }

        playerx *= 2f;
        playery *= 2f;

        x = playerx / 400;
        y = playery / 400;

        xi = (int) x;
        yi = (int) y;

        for (int i = xi - 3; i < xi + 3; i++) {
            for (int j = yi - 3; j < yi + 3; j++) {
                gc.drawImage(overlay, -playerx + i * 400, -playery + j * 400);
            }
        }
    }

    // When the keys are pressed down
    private class KeyListenerDown implements javafx.event.EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.A) {
                left = true;
            } else if (keyCode == KeyCode.W) {
                up = true;
            } else if (keyCode == KeyCode.S) {
                down = true;
            } else if (keyCode == KeyCode.D) {
                right = true;
            }
        }
    }

    // When the keys are not pressed
    private class KeyListenerUp implements javafx.event.EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.A) {
                left = false;
            } else if (keyCode == KeyCode.W) {
                up = false;
            } else if (keyCode == KeyCode.S) {
                down = false;
            } else if (keyCode == KeyCode.D) {
                right = false;
            }
        }
    }

    // Draw player's score
    private void drawScore() {
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.fillText("Score: " + (int) score, 15, 25);
    }

    // Update the player's score based on current position
    private void updateScore() {
        // Calculate distance from starting position (300, 300)
        double distance = Math.sqrt(Math.pow(thePlayer.getX() - 300, 2) + Math.pow(thePlayer.getY() - 300, 2));
        score = distance;

        // Update high score if necessary
        if (score > highScore) {
            highScore = (int) score; 
        }
    }

   // Load the highest Score top left
   private void loadHighScore() 
   {
      try 
      {
        File file = new File("highScore.txt");
        if (file.exists()) 
        {
            Scanner scan = new Scanner(file);
            if (scan.hasNextInt()) 
            {
                highScore = scan.nextInt(); 
                gc.setFill(Color.WHITE);
                gc.setFont(Font.font("Arial", 15));
                gc.fillText("High Score: " + highScore, 15, 42); // Display high score at top left
            }
            scan.close();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    // Save the highest score in a document
    private void saveHighScore() {
        try {
            PrintWriter write = new PrintWriter("highScore.txt");
            write.println(highScore); // Save the current high score
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class AnimationHandler extends AnimationTimer {
        private boolean gameRunning = true;

        @Override
        public void handle(long currentTimeInNanoSeconds) {
            gc.clearRect(0, 0, 600, 600);

            if (gameRunning) {
                if (left) {
                    thePlayer.moveLeft();
                    thePlayer.incrementScore();
                    thePlayer.setForceX(-0.1);
                }
                if (right) {
                    thePlayer.moveRight();
                    thePlayer.incrementScore();
                    thePlayer.setForceX(0.1);
                }
                if (down) {
                    thePlayer.moveDown();
                    thePlayer.incrementScore();
                    thePlayer.setForceY(0.1);
                }
                if (up) {
                    thePlayer.moveUp();
                    thePlayer.incrementScore();
                    thePlayer.setForceY(-0.1);
                }

                drawBackground(thePlayer.getX(), thePlayer.getY());
                thePlayer.draw(thePlayer.getX(), thePlayer.getY(), gc, true);
                updateScore();
                drawScore();
                thePlayer.act(left, right, up, down);
                saveHighScore();
                
                loadHighScore();

                grid();
                for (int i = 0; i < mines.size(); i++) {
                    Mine currentMine = mines.get(i);

                    double distance = Math.sqrt(Math.pow(thePlayer.getX() - currentMine.getX(), 2) + Math.pow(thePlayer.getY() - currentMine.getY(), 2));

                    if (distance > 800) {
                        mines.remove(i);
                        i--;
                    } else {
                        currentMine.draw(thePlayer.getX(), thePlayer.getY(), gc, false);
                    }
                }

                // Check for collisions between player and mines
                for (int i = 0; i < mines.size(); i++) {
                    Mine currentMine = mines.get(i);

                    double distance = Math.sqrt(Math.pow(thePlayer.getX() - currentMine.getX(), 2) + Math.pow(thePlayer.getY() - currentMine.getY(), 2));

                    if (distance < 20) {

                        gameRunning = false;

                        break;
                    }
                }
            } else {
                // Game stopped due to collision
                drawBackground(thePlayer.getX(), thePlayer.getY());
                drawScore();
                // Do not load high score every frame, only once at the beginning
                // loadHighScore();
                for (Mine mine : mines) {
                    mine.draw(thePlayer != null ? thePlayer.getX() : 0, thePlayer != null ? thePlayer.getY() : 0, gc, false);
                }
                // Update high score if necessary
                saveHighScore();
            }
        }
    }

    // Create the grid size
    private void grid() {
        int cgridx = ((int) thePlayer.getX()) / 100;
        int cgridy = ((int) thePlayer.getY()) / 100;

        if (cgridx != lastGridX || cgridy != lastGridY) {
            for (int i = 0; i < 9; i++) {

                int gx = (cgridx - 5 + i) * 100;
                int gy = (cgridy - 5) * 100;
                int sx = cgridx - 5 + i;
                int sy = cgridy - 5;
                testPoint.setX(gx);
                testPoint.setY(gy);
                int amount = (int) (testPoint.distance(originPoint) / 1000);
                addMines(sx, sy, amount);

            }
            for (int i = 0; i < 9; i++) {
                int gx = (cgridx - 5 + i) * 100;
                int gy = (cgridy + 5) * 100;
                int sx = cgridx - 5 + i;
                int sy = cgridy + 5;
                testPoint.setX(gx);
                testPoint.setY(gy);
                int amount = (int) (testPoint.distance(originPoint) / 1000);
                addMines(sx, sy, amount);
            }
            for (int i = 0; i < 9; i++) {
                int gx = (cgridx - 5) * 100;
                int gy = (cgridy - 5 + i) * 100;
                int sx = cgridx - 5;
                int sy = cgridy - 5 + i;

                testPoint.setX(gx);
                testPoint.setY(gy);
                int amount = (int) (testPoint.distance(originPoint) / 1000);
                addMines(sx, sy, amount);
            }
            for (int i = 0; i < 9; i++) {

                int gx = (cgridx + 5) * 100;
                int gy = (cgridy - 5 + i) * 100;
                int sx = cgridx + 5;
                int sy = cgridy - 5 + i;
                testPoint.setX(gx);
                testPoint.setY(gy);
                int amount = (int) (testPoint.distance(originPoint) / 1000);
                addMines(sx, sy, amount);
            }

            lastGridX = cgridx;
            lastGridY = cgridy;
        }
    }

    // Add mines to the JavaFx
    private void addMines(int gx, int gy, int amount_in) {
        for (int i = 0; i < amount_in; i++) {
            if (rand.nextFloat() < 0.3) {
                float xp = (rand.nextFloat() * 100) + (gx * 100);
                float yp = (rand.nextFloat() * 100) + (gy * 100);
                Mine m = new Mine(xp, yp);
                mines.add(m);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
