
package pkg2dgamesframework;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


public class SoundPlayer {
    private boolean isPlaying;
    private Clip clip;
    private float volume = 1.0f;
    
    public SoundPlayer(File path){
        try{
            AudioInputStream ais;
            ais = AudioSystem.getAudioInputStream(path);
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels()*2,
                    baseFormat.getSampleRate(),
                    false
            );
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
            clip = AudioSystem.getClip();
            clip.open(dais);
        }catch(Exception e){}
    }
    public void play(){
        if(clip !=null){
            stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }
    public void stop(){
        clip.stop();
    }
    
    public void close(){
        clip.close();
    }

    public void playLoop() {
    if (clip != null) {
        stop();
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}

    public long getDuration() {
        if (clip != null) {
            return clip.getMicrosecondLength() / 1000;
        } else {
            return 0;
        }
    }

    public boolean getIsPlaying() {
        
        return isPlaying;
    }
    public void setVolume(float volume) {
        if (clip != null) {
            this.volume = volume;
            float gain = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            clip.setFramePosition(0);
            clip.start();
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(gain);
            }
        }
    }

}
