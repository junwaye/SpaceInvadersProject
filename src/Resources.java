import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Resources {
    // to add an image to the environment:
    // 1. put the file into the res folder.
    // 2. Declare a variable before the static block.
    // 3. Initialize the variable by copying and pasting and modifying the
    //    ImageIO line.


    public static BufferedImage  blueGuy, sheild, greenGuy, purpleGuy, redGuy, ship;
    public static BufferedImage[] pics;


    static{
        try{
            sheild = ImageIO.read(new File("./res/eAok2Eg.png"));
            ship = ImageIO.read(new File("./res/Space-Invaders-Ship-PNG-Photo.png"));
            greenGuy = ImageIO.read(new File("./res/invader1.png"));


        }catch(Exception e){e.printStackTrace();}
    }
}