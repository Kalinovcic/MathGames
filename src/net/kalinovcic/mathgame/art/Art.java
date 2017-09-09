package net.kalinovcic.mathgame.art;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Art
{
    public static final Font FONT = loadFont("DejaVuSans-Bold.ttf");
    
    public static final BufferedImage FIREWORK = loadImage("firework.png");
    
    public static Font loadFont(String name)
    {
        try
        {
            Font font = Font.createFont(Font.TRUETYPE_FONT, Art.class.getClassLoader().getResourceAsStream("net/kalinovcic/mathgame/art/" + name));;
            /*GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);*/
            return font;
        }
        catch(FontFormatException | IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public static BufferedImage loadImage(String name)
    {
        try
        {
            return ImageIO.read(Art.class.getClassLoader().getResource("net/kalinovcic/mathgame/art/" + name));
        }
        catch(IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
