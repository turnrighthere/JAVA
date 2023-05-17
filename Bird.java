/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flappybirds;

//import com.sun.javafx.geom.RectBounds;
import java.awt.Rectangle;
import java.io.File;
import pkg2dgamesframework.Objects;
import pkg2dgamesframework.SoundPlayer;

public class Bird extends Objects{
    
    private float vt = 0;
    
    private boolean isFlying = false;

    private Rectangle rect;
    
    private boolean isLive = true;
    
    SoundPlayer fapSound;
    SoundPlayer themeSound;
    SoundPlayer getPointSound;
    SoundPlayer fallSound;
    SoundPlayer loseSound;
    
    
    
    public Bird(int x, int y, int w, int h){
        super(x, y, w, h);  
        rect = new Rectangle(x, y, w, h);
        fapSound = new SoundPlayer(new File("Assets/fap.wav"));
        getPointSound = new SoundPlayer(new File("Assets/getpoint.wav"));
        fallSound = new SoundPlayer(new File("Assets/fall.wav"));
        themeSound = new SoundPlayer(new File("Assets/theme.wav"));
        loseSound = new SoundPlayer(new File ("Assets/lose.wav"));
    }
    
    public void setLive(boolean b){
        
        isLive = b;
        if (!isLive) {
        // Stop the theme sound when the bird dies
        themeSound.stop();
        loseSound.play();
        }
        else {
            // Play the theme sound when the bird is created
        themeSound.playLoop();
        }
        
    }
    
    public boolean getLive(){
        return isLive;
    }
    public Rectangle getRect(){
        return rect;
    }
    
    public void setVt(float vt){
        this.vt = vt;
    }
    public void update(long deltaTime){
        
        vt+=FlappyBirds.g;
        this.setPosY(this.getPosY()+vt);
        this.rect.setLocation((int) this.getPosX(),(int) this.getPosY());
        
        if(vt < 0) isFlying = true;
        else if(vt>0)isFlying = false;
        
    }
    
    public void fly(){
        vt = -5;
        fapSound.play();
    }
    
    
    public boolean getIsFlying(){
        return isFlying;
    }
}
