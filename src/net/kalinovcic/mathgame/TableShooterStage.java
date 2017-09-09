package net.kalinovcic.mathgame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;

import net.kalinovcic.mathgame.art.Art;

public class TableShooterStage extends Stage
{
    public TableShooterStage(Game game)
    {
        super(game);
        this.isOpaque = true;
    }
    
    @Override
    public void update(float dt)
    {
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

        g.setColor(Color.DARK_GRAY);
        g.fillRect(waterHX, 0, sideW, game.height);
        
        Font font = Art.FONT.deriveFont(45.0f);
        Font smallFont = Art.FONT.deriveFont(32.0f);
        g.setStroke(new BasicStroke(2.0f));
        
        for (int i = 1; i <= 10; i++)
        {
            float x = waterHX + sideW / 11 * (i + 0.5f);
            GraphicsHelper.drawString(g, font, i + "", x, 50, 0.5f, Color.GRAY);

            int lineX = (int)(waterHX + sideW / 11 * i);
            g.drawLine(lineX, 10, lineX, 70 * 11 - 10);

            for (int j = 1; j <= 10; j++)
            {
                float y = 50 + j * 70;
                if (i == 1)
                {
                    GraphicsHelper.drawString(g, font, j + "", waterHX + sideW / 22f, y, 0.5f, Color.GRAY);

                    int lineY = (int)(y - 55);
                    g.drawLine(waterHX + 10, lineY, game.width - 10, lineY);
                }
                
                GraphicsHelper.drawString(g, smallFont, i * j + "", x, y - 7, 0.5f, Color.WHITE);
            }
        }
    }
}
