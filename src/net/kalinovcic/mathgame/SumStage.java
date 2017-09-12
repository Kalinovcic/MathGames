package net.kalinovcic.mathgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import net.kalinovcic.mathgame.art.Art;

public class SumStage extends Stage
{
    public int numberA;
    public int numberB;
    public boolean correct;

    public String stringA;
    public String stringB;
    public String stringC;
    public String stringC2;
    
    public boolean hasError = false;
    public boolean completed = false;
    
    public ParticleEmitter backgroundEmitter;
    
    public SumStage(Game game, int numberA, int numberB, boolean correct)
    {
        super(game);
        this.numberA = numberA;
        this.numberB = numberB;
        this.correct = correct;

        this.stringA = "" + numberA;
        this.stringB = "" + numberB;
        this.stringC = "";
        this.stringC2 = "" + (numberA + numberB);
        
        backgroundEmitter = new ParticleEmitter(game);
    }
    
    @Override
    public void update(float dt)
    {
        boolean hasInput = false;
        char input = '?';
        
        for (int i = 0; i < 10; i++)
        {
            if (Input.isKeyPressed(KeyEvent.VK_0 + i) ||
                Input.isKeyPressed(KeyEvent.VK_NUMPAD0 + i))
            {
                hasInput = true;
                input = (char)('0' + i);
            }
        }
        
        if (hasInput && !completed)
        {
            if (hasError)
                stringC = input + stringC.substring(1);
            else
                stringC = input + stringC;
            
            String match = stringC2.substring(stringC2.length() - stringC.length());
            hasError = !stringC.equals(match);

            completed = stringC.equals(stringC2);
        }
        
        backgroundEmitter.spawnBackgroundParticles(dt);
        backgroundEmitter.updateParticles(dt);
    }
    
    @Override
    public void render(Graphics2D g)
    {
        backgroundEmitter.renderParticles(g);
        
        Font font = Art.FONT.deriveFont(300.0f);
        FontMetrics metrics = g.getFontMetrics(font);
        float lineHeight = metrics.getHeight() * 0.7f;

        float left = game.width * 0.2f;
        float right = game.width * 0.8f;
        float top = (game.height - 1.5f * lineHeight) * 0.5f;

        // "4⋅4=16";
        
        GraphicsHelper.drawOutlinedString(g, font, stringA, right, top + lineHeight * 0, 1.0f);
        GraphicsHelper.drawOutlinedString(g, font, stringB, right, top + lineHeight * 1, 1.0f);
        GraphicsHelper.drawOutlinedString(g, font, "+", left, top + lineHeight * 1, 0.0f);
        
        float lineY = top + lineHeight * 1 + 32;
        g.setColor(Color.DARK_GRAY);
        g.drawLine((int) left, (int) lineY, (int) right, (int) lineY);
        
        String correctC = stringC.substring(hasError ? 1 : 0);
        float width = metrics.stringWidth(correctC);

        if (!completed)
        {
            g.setColor(new Color(1.0f, 1.0f, 1.0f, (float)(Math.sin(game.secondsSinceStart * 7) + 1) * 0.5f));
            g.draw(new Rectangle2D.Float(right - width - 206, top + lineHeight + 35, 200, lineHeight + 20));
        }
        
        if (hasError)
        {
            GraphicsHelper.drawOutlinedString(g, font, stringC.substring(0, 1), right - width, top + lineHeight * 2 + 32, 1.0f, Color.RED);
        }

        GraphicsHelper.drawOutlinedString(g, font, correctC, right, top + lineHeight * 2 + 32, 1.0f);
    }
}
