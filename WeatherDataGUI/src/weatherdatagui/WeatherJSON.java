package weatherdatagui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class WeatherJSON {

    static JFrame mainFrame;

    public WeatherJSON() {
        prepareGUI();
        showJFrameDemo();
    }

    private static void prepareGUI() {
        mainFrame = new JFrame("Weather Data");
        mainFrame.setSize(350, 300);
        mainFrame.getContentPane().setBackground(new Color(13, 100, 20));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    static JButton buttonCity = new JButton("City");
    static JButton buttonCoord = new JButton("Coord");

    private static void showJFrameDemo() {

        //Design of buttons
        buttonCity.setForeground(Color.WHITE);
        buttonCity.setFont(new Font("Tahoma", Font.BOLD, 12));
        buttonCoord.setForeground(Color.WHITE);
        buttonCoord.setFont(new Font("Tahoma", Font.BOLD, 12));
        
        buttonCity.setFocusPainted(false);
        buttonCoord.setFocusPainted(false);

        buttonCity.setBounds(50, 125, 50, 50);
        buttonCoord.setBounds(250, 125, 50, 50);

        mainFrame.setLayout(null);

        buttonCity.setBorder(BorderFactory.createEmptyBorder());
        buttonCity.setContentAreaFilled(false);
        buttonCity.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new WeatherCityJSON();
            }
        });

        buttonCoord.setBorder(BorderFactory.createEmptyBorder());
        buttonCoord.setContentAreaFilled(false);
        buttonCoord.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new WeatherCoordJSON();
            }
        });

        mainFrame.add(buttonCity);
        mainFrame.add(buttonCoord);

        mainFrame.setVisible(true);
    }

}
