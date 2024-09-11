import javax.sound.sampled.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, FontFormatException, UnsupportedAudioFileException, LineUnavailableException {
        new Frame();

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Main.class.getResource("music.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(20f * (float) Math.log10(0.1f));
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
