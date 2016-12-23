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

public class WeatherDataGUI {

    static JFrame mainFrame;

    public static void main(String[] args) {
        prepareGUI();
        showJFrameDemo();

    }

    private static void prepareGUI() {
        mainFrame = new JFrame("Weather Data");
        mainFrame.setSize(350, 300);
        mainFrame.getContentPane().setBackground(Color.ORANGE);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    static JButton buttonJSON = new JButton("JSON");

    private static void showJFrameDemo() {

        //Design of buttons
        
        

        buttonJSON.setBounds(125, 125, 50, 50);
        
        buttonJSON.setOpaque(true);
        //buttonJSON.setBorderPainted(false);
        buttonJSON.setBackground(new Color(5, 89, 182));
        buttonJSON.setForeground(Color.WHITE);
        buttonJSON.setFocusPainted(false);
        buttonJSON.setFont(new Font("Tahoma", Font.BOLD, 12));

        mainFrame.setLayout(null);

        buttonJSON.setBorder(BorderFactory.createEmptyBorder());
        buttonJSON.setContentAreaFilled(false);
        buttonJSON.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new WeatherJSON();
                mainFrame.dispose();
            }
        });

        mainFrame.add(buttonJSON);

        mainFrame.setVisible(true);
        /*piecesButton24 = new JLabel("2 w", SwingConstants.CENTER);
        piecesButton24.setBounds(0, 200, 90, 50);*/

        //mainFrame.setLayout(null);
        /*BufferedImage redTriangle, blackTriangle, redTriangleR, blackTriangleR;
        redTriangle = ImageIO.read(getClass().getResource("blackTriangle.png"));
        blackTriangle = ImageIO.read(getClass().getResource("redTriangle.png"));

        redTriangleR = ImageIO.read(getClass().getResource("blackTriangleR.png"));
        blackTriangleR = ImageIO.read(getClass().getResource("redTriangleR.png"));*/

 /*button1 = new JButton(new ImageIcon(redTriangle));
        button1.setBorder(BorderFactory.createEmptyBorder());
        button1.setContentAreaFilled(false);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button1");
            }
        });*/
        //button1.setBounds(0, 360, 100, 200);
        //mainFrame.add(piecesButton24);
        //mainFrame.setVisible(true);
    }

}
