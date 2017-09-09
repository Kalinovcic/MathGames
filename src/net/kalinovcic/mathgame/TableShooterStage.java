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
        boolean inTable;
        boolean hit;
        boolean red;

        void setX()
        {
            while (true)
            {
                boolean success = true;

                x = game.random.nextFloat() * (game.width * 0.6f - 160) + 80;
                
                int count = Math.min(fallingNumbers.size(), 3);
                for (int i = fallingNumbers.size() - count; i < fallingNumbers.size(); i++)
                {
                    if (Math.abs(x - fallingNumbers.get(i).x) < 100)
                        success = false;
                }
                
                if (success) break;
            }
        }
        
        void update(float dt)
        {
            y += vy * dt;
            if (y > game.height - 10) y = game.height - 10;
            if (x > game.width) setX();
        }
        
        void render(Graphics2D g)
        {
            Font smallFont = Art.FONT.deriveFont(game.height * (50.0f / 1080.0f));
            GraphicsHelper.drawString(g, smallFont, number + "", x, y - 7, 0.5f, red ? Color.RED : Color.WHITE);
        }
    }
    
    class Bullet
    {
        float x;
        float y;
        float vy;
        boolean dead;

        void update(float dt)
        {
            y += vy * dt;
            for (FallingNumber number : fallingNumbers)
            {
                if (y > number.y) continue;
                if (Math.abs(x - number.x) > (int)Math.max(Math.ceil(Math.log10(number.number)), 1) * 25) continue;
                if (Math.abs(y - number.y) > 30.0f) continue;
                
                number.hit = true;
                dead = true;
                break;
            }
            
            if (y < 0) dead = true;
        }
        
        void render(Graphics2D g)
        {
            g.setColor(Color.WHITE);
            g.fillOval((int)(x - 10), (int)(y - 10), 20, 20);
        }
    }
    
    public List<FallingNumber> fallingNumbers = new ArrayList<FallingNumber>();
    public List<FallingNumber> numbersToDrop = new ArrayList<FallingNumber>();
    public List<Bullet> bullets = new ArrayList<Bullet>();

    private boolean[] table = new boolean[100];
    
    public TableShooterStage(Game game)
    {
        super(game);
        this.isOpaque = true;
        
        for (int x = 1; x <= 10; x++)
            for (int y = 1; y <= 10; y++)
                if (x == 1 || y == 1 || x == 10 || y == 10)
                    table[(x - 1) * 10 + (y - 1)] = true;
        
        for (int i = 1; i <= 100; i++)
        {
            if (table[i - 1]) continue;

            FallingNumber number = new FallingNumber();
            number.number = i;
            number.inTable = false;
            
            for (int x = 1; x <= 10; x++)
                for (int y = 1; y <= 10; y++)
                    if (x * y == number.number)
                        number.inTable = true;
            
            numbersToDrop.add(number);
        }
        
        for (int i = 0; i < 1000; i++)
        {
            int x = game.random.nextInt(numbersToDrop.size());
            int y = game.random.nextInt(numbersToDrop.size());
            FallingNumber a = numbersToDrop.get(x);
            FallingNumber b = numbersToDrop.get(y);
            numbersToDrop.set(x, b);
            numbersToDrop.set(y, a);
        }
    }
    
    private float numberSpawnCounter = 0;

    private float boatX = 150.0f;
    private float boatRotationTime = 0;
    private boolean boatFacingRight = true;
    
    @Override
    public void update(float dt)
    {
        float numberSpawnRate = 2;
        
        numberSpawnCounter += dt;
        while (numberSpawnCounter >= numberSpawnRate)
        {
            numberSpawnCounter -= numberSpawnRate;

            if (numbersToDrop.size() != 0)
            {
                int last = numbersToDrop.size() - 1;
                FallingNumber number = numbersToDrop.get(last);
                numbersToDrop.remove(last);

                number.setX();
                number.y = 0;
                number.vy = 50;

                fallingNumbers.add(number);
            }
        }

        for (int i = 0; i < bullets.size(); i++)
        {
            Bullet bullet = bullets.get(i);
            bullet.update(dt);
            if (bullet.dead)
            {
                int last = bullets.size() - 1;
                bullets.set(i, bullets.get(last));
                bullets.remove(last);
                i--;
            }
        }

        for (int i = 0; i < fallingNumbers.size(); i++)
        {
            FallingNumber number = fallingNumbers.get(i);
            number.update(dt);
            if (number.hit)
            {
                if (number.inTable)
                {
                    number.red = true;
                    number.hit = false;
                }
                else
                {
                    int last = fallingNumbers.size() - 1;
                    fallingNumbers.set(i, fallingNumbers.get(last));
                    fallingNumbers.remove(last);
                    i--;
                }
            }
        }

        float move = 0.0f;
        if (Input.isKeyDown(KeyEvent.VK_LEFT )) move = -1.0f;
        if (Input.isKeyDown(KeyEvent.VK_RIGHT)) move =  1.0f;
        if (Input.isKeyPressed(KeyEvent.VK_SPACE))
        {
            Bullet bullet = new Bullet();
            bullet.x = boatX + 100;
            bullet.y = game.height * 0.7f - 100;
            bullet.vy = -400.0f;
            bullets.add(bullet);
        }
        
        if (move != 0.0f)
        {
            boatX += 500.0f * move * dt;
            boatRotationTime += 8.0 * dt;
            boatFacingRight = move > 0.0f;
        }
        else
        {
            boatRotationTime += 4.0 * dt;
        }

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
        
        for (Bullet bullet : bullets)
            bullet.render(g);
        
        

        g.setColor(Color.DARK_GRAY);
        g.fillRect(waterHX, 0, sideW, game.height);
        
        Font font = Art.FONT.deriveFont(game.height * (48.0f / 1080.0f));
        Font smallFont = Art.FONT.deriveFont(game.height * (32.0f / 1080.0f));
        g.setStroke(new BasicStroke(2.0f));
        
        float boatWidth = 200.0f;
        float boatHeight = boatWidth * Art.BOAT.getHeight() / Art.BOAT.getWidth();
        float boatY = waterLY + 10;
        
        AffineTransform boatTransform = AffineTransform.getTranslateInstance(boatX + boatWidth * 0.5, boatY);
        if (boatFacingRight)
            boatTransform.scale(-1, 1);
        boatTransform.rotate(Math.sin(boatRotationTime) * 0.1);
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
                
                if (table[(j - 1) * 10 + (i - 1)])
                {
                    GraphicsHelper.drawString(g, smallFont, i * j + "", x, y - 7, 0.5f, Color.WHITE);
                }
            }
        }
    }
}
