package net.kalinovcic.mathgame;

import java.awt.Graphics2D;

public abstract class Stage
{
    public Game game;
    
    public Stage(Game game)
    {
        this.game = game;
    }
    
    public abstract void update(float dt);
    public abstract void render(Graphics2D g);
}
