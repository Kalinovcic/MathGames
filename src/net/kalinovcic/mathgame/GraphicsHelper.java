package net.kalinovcic.mathgame;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;

public class GraphicsHelper
{
    public static void drawOutlinedString(Graphics2D g, Font font, String string, float x, float y, float alignment)
    {
        drawOutlinedString(g, font, string, x, y, alignment, Color.WHITE);
    }
    
    public static void drawOutlinedString(Graphics2D g, Font font, String string, float x, float y, float alignment, Color color)
    {
        FontMetrics metrics = g.getFontMetrics(font);
        
        GlyphVector text = font.createGlyphVector(g.getFontRenderContext(), string);
        Shape shape = text.getOutline();

        float translateX = x - metrics.stringWidth(string) * alignment;
        float translateY = y;
        g.translate(translateX, translateY);

        g.setComposite(AlphaComposite.SrcOver.derive(0.6f));
        g.setColor(color);
        g.fill(shape);
        
        Stroke stroke = new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, null, 0);
        g.setStroke(stroke);

        g.setComposite(AlphaComposite.SrcOver.derive(1.0f));
        g.setColor(Color.DARK_GRAY);
        g.draw(shape);

        g.translate(-translateX, -translateY);
    }
    
    public static void drawString(Graphics2D g, Font font, String string, float x, float y, float alignment, Color color)
    {
        FontMetrics metrics = g.getFontMetrics(font);
        float stringX = x - metrics.stringWidth(string) * alignment;
        float stringY = y;
        
        g.setFont(font);
        g.setColor(color);
        g.drawString(string, stringX, stringY);
    }
}
