package net.kalinovcic.mathgame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import net.kalinovcic.mathgame.art.Art;

public class TableShooterStage extends Stage
{
    class FallingNumber
    {
        int number;
        float x;
        float y;
        float vy;
        
        void update(float dt)
        {
            y += vy * dt;
        }
        
        void render(Graphics2D g)
        {
            Font smallFont = Art.FONT.deriveFont(game.height * (50.0f / 1080.0f));
            GraphicsHelper.drawString(g, smallFont, number + "", x, y - 7, 0.5f, Color.WHITE);
        }
    }
    
    public List<FallingNumber> fallingNumbers = new ArrayList<FallingNumber>();
    
    public TableShooterStage(Game game)
    {
        super(game);
        this.isOpaque = true;
    }
    
    private int numberSpawnCounter = 0;

    private float boatX = 150.0f;
    
    @Override
    public void update(float dt)
    {
        int numberSpawnRate = 300;
        
        numberSpawnCounter--;
        if (numberSpawnCounter <= 0)
        {
            FallingNumber number = new FallingNumber();
            number.number = game.random.nextInt(100) + 1;
            number.x = game.random.nextFloat() * (game.width * 0.6f - 160) + 80;
            number.y = 0;
            number.vy = 50;

            fallingNumbers.add(number);
            numberSpawnCounter = numberSpawnRate;
        }
        
        for (FallingNumber number : fallingNumbers)
            number.update(dt);

        if (Input.isKeyDown(KeyEvent.VK_LEFT )) boatX -= 500.0f * dt;
        if (Input.isKeyDown(KeyEvent.VK_RIGHT)) boatX += 500.0f * dt;

        float boatWidth = 200.0f;
        float width = game.width * 0.6f - boatWidth;
        if (boatX < 0) boatX = 0;
        if (boatX > width) boatX = width;
    }
    
    @Override
    public void render(Graphics2D g)
    {
        int waterLX = 0;
        int waterLY = (int)(game.height * 0.7);
        int waterHX = (int)(game.width * 0.6);
        int waterHY = game.height;
        int waterW = waterHX - waterLX;
        int sideW = game.width - waterW;

        g.setColor(new Color(0x73C2FB));
        g.fillRect(waterLX, 0, waterW, game.height);
        
        Polygon path = new Polygon();
        path.addPoint(waterHX, waterHY);
        path.addPoint(waterLX, waterHY);
        path.addPoint(waterLX, waterLY);
        for (int i = 0; i <= 50; i++)
        {
            path.addPoint((int)(waterLX + waterW * (i / 50.0f)),
                          (int)(waterLY + Math.sin(game.secondsSinceStart * 4.0f + i / 1.5f) * 10.0f));
        }
        
        g.setColor(new Color(0x0070FF));
        g.fillPolygon(path);
        
        for (FallingNumber number : fallingNumbers)
            number.render(g);
        
        

        g.setColor(Color.DARK_GRAY);
        g.fillRect(waterHX, 0, sideW, game.height);
        
        Font font = Art.FONT.deriveFont(game.height * (48.0f / 1080.0f));
        Font smallFont = Art.FONT.deriveFont(game.height * (32.0f / 1080.0f));
        g.setStroke(new BasicStroke(2.0f));
        
        float boatWidth = 200.0f;
        float boatHeight = boatWidth * Art.BOAT.getHeight() / Art.BOAT.getWidth();
        float boatY = waterLY + 10;
        
        AffineTransform boatTransform = AffineTransform.getTranslateInstance(boatX + boatWidth * 0.5, boatY);
        boatTransform.rotate(Math.sin(game.secondsSinceStart * 4.0) * 0.1);
        g.setTransform(boatTransform);
        g.drawImage(Art.BOAT, -(int)(boatWidth * 0.5), -(int)(boatHeight), (int)(boatWidth * 0.5), 0, 0, 0, Art.BOAT.getWidth(), Art.BOAT.getHeight(), null);
        g.setTransform(new AffineTransform());
        
        float columnSize = sideW / 11;
        for (int i = 1; i <= 10; i++)
        {
            float x = waterHX + columnSize * (i + 0.5f);
            GraphicsHelper.drawString(g, font, i + "", x, columnSize, 0.5f, Color.GRAY);

            int lineX = (int)(waterHX + sideW / 11 * i);
            g.drawLine(lineX, 10, lineX, (int)(columnSize * 11.2f));

            for (int j = 1; j <= 10; j++)
            {
                float y = columnSize + j * columnSize;
                if (i == 1)
                {
                    GraphicsHelper.drawString(g, font, j + "", waterHX + sideW / 22f, y, 0.5f, Color.GRAY);

                    int lineY = (int)(y - columnSize * 0.8);
                    g.drawLine(waterHX + 10, lineY, game.width - 10, lineY);
                }
                
                GraphicsHelper.drawString(g, smallFont, i * j + "", x, y - 7, 0.5f, Color.WHITE);
            }
        }
    }
}
