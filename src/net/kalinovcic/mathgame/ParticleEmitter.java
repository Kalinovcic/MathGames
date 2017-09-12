package net.kalinovcic.mathgame;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ParticleEmitter
{
    class Particle
    {
        public float x;
        public float y;
        public float size = 10.0f;

        public BufferedImage image;
        public Color color;
        public float alpha = 0.3f;

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
            
            float alpha = this.alpha;
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
    
    public Game game;
    public List<Particle> particles = new ArrayList<Particle>();
    
    public ParticleEmitter(Game game)
    {
        this.game = game;
    }
    
    public void updateParticles(double dt)
    {
        for (int i = 0; i < particles.size(); i++)
        {
            Particle p = particles.get(i);
            p.update((float) dt);
            if (p.life > p.duration)
            {
                particles.remove(i);
                i--;
                /*int last = particles.size() - 1;
                particles.set(i, particles.get(last));
                particles.remove(last);
                i--;*/
            }
        }
    }
    
    public void renderParticles(Graphics2D g)
    {
        for (Particle p : particles)
            p.render(g);
    }

    public void spawnSparks(int amount, float x, float y, float minAngle, float maxAngle, float minSpeed, float maxSpeed, float size, float lifetime, float minHue, float maxHue, float alpha)
    {
        while (amount-- != 0)
        {
            double rad = Math.toRadians(minAngle + game.random.nextFloat() * (maxAngle - minAngle) + 90);
            double vel = minSpeed + game.random.nextFloat() * (maxSpeed - minSpeed);
            float hue = minHue + game.random.nextFloat() * (maxHue - minHue);
            
            Particle p = new Particle();
            p.x = x;
            p.y = y;
            p.vx = (float) (Math.cos(rad) * vel);
            p.vy = (float)-(Math.sin(rad) * vel);
            p.size = size;

            p.duration = lifetime;
            p.fadeIn = 0;
            p.fadeOut = lifetime * 0.2f;
            
            p.ay = 150;
            
            p.color = Color.getHSBColor(hue, 1.0f, 1.0f);
            p.alpha = alpha;

            particles.add(p);
        }
    }
    
    private int particleSpawnCounter = 0;
    private float backgroundParticleHue = 0;
    
    public void spawnBackgroundParticles(float dt)
    {
        backgroundParticleHue += dt * 0.02;
        
        particleSpawnCounter--;
        if (particleSpawnCounter <= 0)
        {
            particleSpawnCounter = 5;

            Particle p = new Particle();
            p.x = game.random.nextInt(game.width);
            p.y = game.random.nextInt(game.height);
            p.vx = (game.random.nextFloat() - 0.5f) * 50f;
            p.vy = (game.random.nextFloat() - 0.5f) * 50f;
            p.size = 400;
            
            float hue = backgroundParticleHue;
            p.color = Color.getHSBColor(hue + (game.random.nextFloat() - 0.5f) * 0.1f, 1.0f, 1.0f);           
            particles.add(p);
        }
    }
}
