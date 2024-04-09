import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

class Player extends DrawableObject {
    private int score;
    private double speedX;
    private double speedY;
    private double forceX;
    private double forceY;
    private final double MAX_SPEED = 5.0;

    public Player(float x, float y) {
        super(x, y);
        this.score = 0;
    }

    public void act(boolean left, boolean right, boolean up, boolean down) {
        if (left) 
        {
            forceX -= 0.1;
        } else if (right) 
        {
            forceX += 0.1;
        }
        if (up) 
        {
            forceY -= 0.1;
        } else if (down) 
        {
            forceY += 0.1;
        }

        // Limit force
        forceX = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, forceX));
        forceY = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, forceY));

        // Apply acceleration
        speedX += forceX;
        speedY += forceY;

        // Apply Force
        speedX *= 0.975;
        speedY *= 0.975;
        
        // Check if a key is not pressed and force is within (-0.25, 0.25) range, then set force to 0
        if (!left && !right && -0.25 < forceX && forceX < 0.25) 
        {
            forceX = 0;
        }
        if (!up && !down && -0.25 < forceY && forceY < 0.25) 
        {
            forceY = 0;
        }

        // Update position
        x += speedX;
        y += speedY;

      }
    
    
    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }

    public void moveLeft() {
        setX(getX() - 1);
    }

    public void moveRight() {
        setX(getX() + 1);
    }

    public void moveUp() {
        setY(getY() - 1);
    }

    public void moveDown() {
        setY(getY() + 1);
    }
    
    public void setForceX(double forceX) 
    {
      this.forceX = forceX;
    }
    
    // Set force in the Y direction
    public void setForceY(double forceY) 
    {
        this.forceY = forceY;
    }
    
    
    public double getSpeedX() {
    return speedX;
}

public double getSpeedY() {
    return speedY;
}


   public double distanceTo(Mine mine) 
   {
    return Math.sqrt((mine.getX() - this.getX()) * (mine.getX() - this.getX()) + (mine.getY() - this.getY()) * (mine.getY() - this.getY()));
   }

    // Draws the player
    public void drawMe(float x, float y, GraphicsContext gc) {
        gc.setFill(Color.PURPLE);
        gc.fillOval(300 - 14, 300 - 14, 27, 27);
        gc.setFill(Color.GRAY);
        gc.fillOval(300 - 13, 300 - 13, 25, 25);
    }
}