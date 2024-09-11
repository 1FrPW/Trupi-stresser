import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Panel extends JPanel {

    private final Image background;
    private int width;
    private int height;

    public Panel(int frameWidth, int frameHeight) throws IOException {
        background = ImageIO.read(getClass().getResourceAsStream("background.png"));
        this.width = frameWidth;
        this.height = frameHeight;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, width, height, this);
    }
}
