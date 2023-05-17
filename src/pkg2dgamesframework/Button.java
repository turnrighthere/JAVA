package pkg2dgamesframework;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Button {

    private int x, y, width, height;
    private String text;
    private Font font;
    private Color color;
    private Color textColor;
    
    public Button(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = "";
        this.font = new Font("Arial", Font.PLAIN, 20);
        this.color = Color.decode("#5eb5e0");
        this.textColor = Color.white;
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    public void setFont(Font font){
        this.font = font;
    }
    
    public void setColor(Color color){
        this.color = color;
    }
    
    public void setTextColor(Color textColor){
        this.textColor = textColor;
    }
    
    public void paint(Graphics2D g2){
        g2.setFont(font);
        FontMetrics fontMetrics = g2.getFontMetrics();
        int textX = x + (width - fontMetrics.stringWidth(text)) / 2;
        int textY = y + (height - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();
        g2.setColor(color);
        g2.fill(new Rectangle(x, y, width, height));
        g2.setColor(textColor);
        g2.drawString(text, textX, textY);
    }
    
    public boolean contains(int x, int y){
        return (this.x <= x && x <= this.x + this.width && this.y <= y && y <= this.y + this.height);
    }
}