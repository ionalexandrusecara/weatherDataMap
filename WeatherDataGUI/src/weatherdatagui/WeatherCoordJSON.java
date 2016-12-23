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

public class WeatherCoordJSON {

    static JFrame mainFrame;

    public WeatherCoordJSON() {
        prepareGUI();
        showJFrameDemo();
    }

    private static void prepareGUI() {
        mainFrame = new JFrame("Weather Data");
        mainFrame.setSize(450, 300);
        mainFrame.getContentPane().setBackground(new Color(13, 100, 20));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    static JButton buttonCurrentWeather = new JButton("Current Weather");
    static JButton buttonForecast = new JButton("Forecast");

    private static void showJFrameDemo() {

        //Design of buttons
        buttonCurrentWeather.setForeground(Color.WHITE);
        buttonCurrentWeather.setFont(new Font("Tahoma", Font.BOLD, 12));
        buttonForecast.setForeground(Color.WHITE);
        buttonForecast.setFont(new Font("Tahoma", Font.BOLD, 12));
        
        buttonCurrentWeather.setFocusPainted(false);
        buttonForecast.setFocusPainted(false);

        buttonCurrentWeather.setBounds(50, 125, 150, 50);
        buttonForecast.setBounds(250, 125, 150, 50);

        mainFrame.setLayout(null);

        buttonCurrentWeather.setBorder(BorderFactory.createEmptyBorder());
        buttonCurrentWeather.setContentAreaFilled(false);
        buttonCurrentWeather.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new WeatherCoordCurrentWeather();
            }
        });

        buttonForecast.setBorder(BorderFactory.createEmptyBorder());
        buttonForecast.setContentAreaFilled(false);
        buttonForecast.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new WeatherCoordForecast();
            }
        });

        mainFrame.add(buttonCurrentWeather);
        mainFrame.add(buttonForecast);

        mainFrame.setVisible(true);
    }

}
