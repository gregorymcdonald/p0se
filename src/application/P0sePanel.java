package application;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;

public class P0sePanel extends JPanel implements Runnable{
    /* Notes on Tags:
     * Tags are indicated by all-caps followed by colons in comments (e.g. "AAAAAA:")
     * They indicate some important information that could be relevant later in production
     * 
     * Currently Used Tags
     * "NOTE:" -> Used for general information or design decisions
     * "IMPORTANT:" -> Used for critical, need-to-know information
     * "MODIFY:" -> Indicates code that needs to be improved/changed. Include reason for change if not obvious
     * "REMOVE:" -> Indicates code to be removed at some point. Unless specified remove at release.
     */
    
    /* ***** JPANEL FIELDS ***** */
    private static final long serialVersionUID = -1; //serialVersionUID, used by Serializable
    
    /* ***** DISPLAY FIELDS ***** */
    private static final boolean DEBUG_MODE = true;
    private static Color debugColor = new Color(255,0,0);
    private static Color defaultBackgroundColor = new Color(100, 149, 237);
    private static BufferedImage backgroundImage;
    
    /* ***** P0SE FIELDS ***** */
    private static Webcam defaultWebcam;
    
    /* ***** CONSTRUCTORS ***** */
    public P0sePanel(){
        //Get the webcam
        defaultWebcam = Webcam.getDefault();
        if (defaultWebcam != null) {
            System.out.println("Webcam: " + defaultWebcam.getName());
            defaultWebcam.open();
            backgroundImage = defaultWebcam.getImage();
        }//if: able to find a webcam 
        else {
            System.out.println("No webcam detected");
            System.exit(1);
        }//else: print error and exit
        
        //Image Capture Thread
        Thread imageCaptureThread = new Thread(this);
        imageCaptureThread.start();
    }//constructor: default
    
    /* ***** RUNNABLE ***** */
    public void run(){
    	while(true){
    		//System.out.println("Capturing image from webcam...");
    		backgroundImage = defaultWebcam.getImage();
    		repaint();
    		delay(100);
    	}//while: forever
    }//method: run
    
    private boolean delay(int milliseconds){
    	try{
    		Thread.sleep(milliseconds);
    		return true;
    	}
    	catch(Exception e){
    		System.err.println("Delay of " + milliseconds + " ms failed.");
    	}//catch: all exceptions
    	return false;
    }//method: delay
    
    /* ******************** */
    /* ***** PAINTING ***** */
    /* ******************** */
    
    public void paint(Graphics g){
        int panelWidth = this.getWidth();
        int panelHeight = this.getHeight();
        
        //Draw background - REMOVE: at release, or when backgroundImage is implemented
        g.setColor(defaultBackgroundColor);
        g.fillRect(0, 0, panelWidth, panelHeight);
        
        if(backgroundImage != null){
            g.drawImage(backgroundImage, 0, 0, panelWidth, panelHeight, null);
        }//if: backgroundImage is not null
        
        //Draw DEBUG information
        if(DEBUG_MODE){
            //Set up initial debug values
            int debugDrawX = 5; //x-value to draw debug strings at, prevents "magic numbers"
            int debugDrawY = 15; //y-value to draw debug strings with, prevents "magic numbers"
            g.setColor(debugColor);
            
            //Debug Mode Disclaimer
            String debugModeDisclaimer = "DEBUG MODE";
            g.drawString(debugModeDisclaimer, debugDrawX, debugDrawY);
            debugDrawY += 15; //MODIFY: Replace with code to determine the height of the previous string based on font
        }//if: in debug mode
    }//method: paint
}//class: P0sePanel
