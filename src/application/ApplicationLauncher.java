package application;

import application.P0sePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ApplicationLauncher {
    public static void main(String[] args){
        System.out.println("Entered main method of Launcher.");
        
        //JFrame Creation
        JFrame applicationFrame = new JFrame("p0se");
        applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Gets screen size
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //Does not account for Taskbar
        Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();
        
        //Creating and Adding the GamePanel
        int panelWidth = (int)Math.round(screenWidth * 0.75);
        int panelHeight = (int)Math.round(screenHeight * 0.75);
        P0sePanel p0sePanel = new P0sePanel();
        p0sePanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        applicationFrame.getContentPane().add(p0sePanel);
        applicationFrame.pack(); //Packs the JFrame, minimizing the size based on the added components
        
        //Set the Frame's starting position to be the center of the screen
        int initialX = (int)Math.round(screenWidth / 2.0 - p0sePanel.getWidth() / 2.0);
        int initialY = (int)Math.round(screenHeight / 2.0 - p0sePanel.getHeight() / 2.0);
        applicationFrame.setLocation(initialX, initialY);
        
        // Add Capture Button
        JButton capture = new JButton("Capture");
        capture.setPreferredSize(new Dimension(500, 100));
        p0sePanel.setLayout(new BorderLayout());
        p0sePanel.add(capture, BorderLayout.SOUTH);
        
        //Required to make the frame visible
        applicationFrame.setVisible(true);
    }//method: main
}//class: ApplicationLauncher
