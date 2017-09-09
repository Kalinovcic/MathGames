package net.kalinovcic.mathgame;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class Particle
{
    public float x;
    public float y;
    public float size = 10.0f;

    public BufferedImage image;
    public Color color;

    public float life     = 0.0f;
    public float duration = 5.0f;
    public float fadeIn   = 2.5f;
    public float fadeOut  = 2.5f;

    public float vx;
    public float vy;

    public float ax;
    public float ay;
    
    public void update(float dt)
    {
        x += vx * dt;
        y += vy * dt;
        vx += ax * dt;
        vy += ay * dt;
        life += dt;
    }
    
    public void render(Graphics2D g)
    {
        float s = size * 0.5f;
        
        float alpha = 0.3f;
        if (life < fadeIn)
            alpha *= life / fadeIn;
        else if ((duration - life) < fadeOut)
            alpha *= (duration - life) / fadeOut;

        if (image == null)
        {
            g.setColor(color);
            g.setComposite(AlphaComposite.SrcOver.derive(alpha));
            g.fill(new Ellipse2D.Float(x - s, y - s, size, size));
        }
        else
        {
            g.setComposite(AlphaComposite.SrcOver.derive(alpha));
            g.drawImage(image, (int)(x - s), (int)(y - s), (int)(x + s), (int)(y + s),
                    0, 0, image.getWidth(), image.getHeight(), null);
        }
    }
}
