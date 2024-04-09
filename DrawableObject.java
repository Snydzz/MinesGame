import javafx.scene.paint.*;
import javafx.scene.canvas.*;

public abstract class DrawableObject
{
   public DrawableObject(float x, float y)
   {
      this.x = x;
      this.y = y;
   }

   //positions
   public float x;
   public float y;
   
   //takes the position of the player and calls draw me with appropriate positions
   public void draw(float playerx, float playery, GraphicsContext gc, boolean isPlayer)
   {
      //the 300,300 places the player at 300,300, if you want to change it you will have to modify it here
      if (isPlayer)
            drawMe(playerx, playery, gc);
        else
            drawMe(-playerx + 300, -playery + 300+y, gc);
      
      if(isPlayer)
         drawMe(playerx,playery,gc);
      else
         drawMe(-playerx+300+x,-playery+300+y,gc); 
   }
   
      
   //this method you implement for each object you want to draw. Act as if the thing you want to draw is at x,y.
   //NOTE: DO NOT CALL DRAWME YOURSELF. Let the the "draw" method do it for you. I take care of the math in that method for a reason.
  public abstract void drawMe(float x, float y, GraphicsContext gc);
  public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }   
    
    public double distance(DrawableObject other)
    {
         return(Math.sqrt((other.x-x)*(other.x-x) + (other.y-y)*(other.y-y)));
    }
   
   public class Mine extends DrawableObject 
   {
       public Mine(float x, float y) 
       {
         super(x, y);
       }

    @Override
    public void drawMe(float x, float y, GraphicsContext gc) 
    {
        gc.setFill(Color.RED);
        gc.fillOval(x - 6, y - 6, 12, 12);
    }
   }
}
