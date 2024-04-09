import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Random;

public class Mine extends DrawableObject {
    private double colorValue;
    private double colorIncrement = 0.01; 
    private int way = 1; 

    public Mine(float x, float y) {
        super(x, y);
        // Generate a random initial color value between 0 and 1
        Random rand = new Random();
        colorValue = rand.nextDouble();
    }

    @Override
    public void drawMe(float x, float y, GraphicsContext gc) {
        // Update color value for interpolation
        colorValue += colorIncrement * way;
        if (colorValue >= 1) {
            colorValue = 1;
            way = -1;
        }
        if (colorValue <= 0) {
            colorValue = 0;
            way = 1;
        }

        // Draw the mine with oscillating color
        Color color = Color.RED.interpolate(Color.WHITE, colorValue);
        gc.setFill(color);
        gc.fillOval(x - 5, y - 5, 10, 10);
    }
}
