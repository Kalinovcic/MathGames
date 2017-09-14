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
    public static class Task
    {
        public int[] numbers;
        public boolean subtract;
        public int result;
        
        public Task(int[] numbers, boolean subtract)
        {
            this.numbers = numbers;
            this.subtract = subtract;
            result = numbers[0];
            for (int i = 1; i < numbers.length; i++)
                if (subtract)
                    result -= numbers[i];
                else
                    result += numbers[i];
        }
    }
    
    public Task[] tasks;
    public Task currentTask = null;
    public int nextTaskIndex = 0;

    public String string;
    public boolean hasError = false;
    public boolean completed = false;

    public ParticleEmitter backgroundEmitter;
    
    public SumStage(Game game, Task[] tasks)
    {
        super(game);
        this.tasks = tasks;
        
        backgroundEmitter = new ParticleEmitter(game);
    }
    
    @Override
    public void update(float dt)
    {
        backgroundEmitter.spawnBackgroundParticles(dt);
        backgroundEmitter.updateParticles(dt);

        if (completed)
        {
            currentTask = null;
            completed = false;
        }
        
        if (currentTask == null)
        {
            if (nextTaskIndex >= tasks.length)
                return;
            currentTask = tasks[nextTaskIndex++];
            string = "";
        }
        
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
                string = input + string.substring(1);
            else
                string = input + string;
            
            String correct = currentTask.result + "";
            String match = correct.substring(correct.length() - string.length());
            hasError = !string.equals(match);

            completed = string.equals(correct);
        }
    }
    
    @Override
    public void render(Graphics2D g)
    {
        backgroundEmitter.renderParticles(g);
        
        if (currentTask == null)
            return;
        
        int lineCount = currentTask.numbers.length + 1;
        float fontSize = game.height / (float) lineCount * 0.7f;

        Font font = Art.FONT.deriveFont(fontSize);
        FontMetrics metrics = g.getFontMetrics(font);
        float lineHeight = metrics.getHeight() * 0.7f;

        float left = game.width * 0.15f;
        float right = game.width * 0.85f;
        
        float nextY = (game.height - lineHeight * (lineCount - 1)) * 0.5f;

        for (int i = 0; i < currentTask.numbers.length; i++)
        {
            String str = currentTask.numbers[i] + "";
            GraphicsHelper.drawOutlinedString(g, font, str, right, nextY, 1.0f);
            nextY += lineHeight;
        }

        GraphicsHelper.drawOutlinedString(g, font, currentTask.subtract ? "-" : "+", left, nextY - lineHeight, 0.0f);

        int lineY = (int)(nextY - lineHeight * 0.85f);
        g.setColor(Color.DARK_GRAY);
        g.drawLine((int) left, lineY, (int) right, (int) lineY);

        nextY += lineHeight * 0.2f;
        
        String correct = string.substring(hasError ? 1 : 0);
        float width = metrics.stringWidth(correct);
        GraphicsHelper.drawOutlinedString(g, font, correct, right, nextY, 1.0f);
        
        if (hasError)
        {
            GraphicsHelper.drawOutlinedString(g, font, string.substring(0, 1), right - width, nextY, 1.0f, Color.RED);
        }
        
        if (!completed)
        {
            float boxWidth = metrics.stringWidth("1");
            float boxPadding = boxWidth * 0.05f;
            boxWidth += 2 * boxPadding;
            
            g.setColor(new Color(1.0f, 1.0f, 1.0f, (float)(Math.sin(game.secondsSinceStart * 7) + 1) * 0.5f));
            g.draw(new Rectangle2D.Float(right - width - boxWidth + boxPadding, nextY - lineHeight, boxWidth, lineHeight + 20));
        }

        /*
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

        GraphicsHelper.drawOutlinedString(g, font, correctC, right, top + lineHeight * 2 + 32, 1.0f);*/
    }
}
