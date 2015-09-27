package application;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import colorutil.ColorFinder;
import colorutil.ColorTolerance;
import colorutil.ColorUtil;

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
    //Colors
    private static Color debugColor = new Color(255,0,0);
    private static Color defaultBackgroundColor = new Color(100, 149, 237);
    //Input Display
    private static final boolean FILL_INPUT_DISPLAY_AREA = false;
    private static final boolean FIT_INPUT_DISPLAY_AREA = true;
    private static Rectangle inputDisplayArea;
    private static BufferedImage inputImage;
    //Taskbar Display
    private static final int TASKBAR_HEIGHT = 96;
    private static Color taskbarBackgroundColor;
    
    /* ***** P0SE FIELDS ***** */
    private static Webcam inputDevice;
    private static ColorTolerance jointColorTolerance;
    private static Color[] jointColors = {
        new Color(255, 255, 255),
        new Color(48, 114, 139),
        new Color(167, 175, 71)
    };
    
    /* ***** CONSTRUCTORS ***** */
    public P0sePanel(){
        //Get the webcam
        inputDevice = getWebcam();
        inputDevice.open();
        inputImage = inputDevice.getImage();
        
        //Input Display Area
        inputDisplayArea = new Rectangle(0, 0, 256, 256);
        
        //Taskbar
        taskbarBackgroundColor = new Color(255, 255, 255);
        
        //Default p0se values
        jointColorTolerance = new ColorTolerance(30);
        
        //Image Capture Thread
        Thread imageCaptureThread = new Thread(this);
        imageCaptureThread.start();
    }//constructor: default
    
    private Webcam getWebcam(){
        Webcam webcam = Webcam.getDefault();
        if (webcam != null) {
            System.out.println("Webcam: " + webcam.getName());
            return webcam;
        }//if: able to find a webcam 
        else {
            System.out.println("No webcam detected");
            System.exit(1);
            return null;
        }//else: print error and exit
    }//method: getWebcam
    
    /* ***** RUNNABLE ***** */
    public void run(){
    	while(true){
    		//System.out.println("Capturing image from webcam...");
    		inputImage = inputDevice.getImage();
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
        //Constants of panel size
        int panelWidth = this.getWidth();
        int panelHeight = this.getHeight();
        //Input Display Area sizing
        inputDisplayArea.width = panelWidth;
        inputDisplayArea.height = panelHeight - TASKBAR_HEIGHT;
        
        //Draw background - REMOVE: at release, or when inputImage is implemented
        g.setColor(defaultBackgroundColor);
        g.fillRect(0, 0, panelWidth, panelHeight);
        
        drawInputImage(g);
        highlightJoints(g);
        
        drawTaskbar(g);
        
        drawDebugInfo(g);
    }//method: paint
    
    private void drawInputImage(Graphics g){
        if(inputImage == null){
            return;
        }//if: input image is null
        
        if(FILL_INPUT_DISPLAY_AREA){
            g.drawImage(inputImage, 0, 0, inputDisplayArea.width, inputDisplayArea.height, null);
        }//if: image is set to fill the ENTIRE input display area
        else if(FIT_INPUT_DISPLAY_AREA){
            double widthRatio = ((double)inputDisplayArea.width) / ((double)inputImage.getWidth());
            double heightRatio = ((double)inputDisplayArea.height) / ((double)inputImage.getHeight());
            int scaledWidth = 0;
            int scaledHeight = 0;
            if(heightRatio >= widthRatio){
                scaledWidth = (int)Math.round(inputImage.getWidth() * widthRatio);
                scaledHeight = (int)Math.round(inputImage.getHeight() * widthRatio);
            }//if: the height ratio is greater than the width ratio, scale by width
            else{
                scaledWidth = (int)Math.round(inputImage.getWidth() * heightRatio);
                scaledHeight = (int)Math.round(inputImage.getHeight() * heightRatio);
            }//else: the width ratio is greater, scale by height
            int centerXOffset = (int)Math.round(inputDisplayArea.width / 2.0 - (scaledWidth / 2.0));
            int centerYOffset = (int)Math.round(inputDisplayArea.height / 2.0 - (scaledHeight / 2.0));
            g.drawImage(inputImage, centerXOffset, centerYOffset, scaledWidth, scaledHeight, null);
            //g.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        }//if: image is set to fill the ENTIRE input display area
        else{
            int centerXOffset = inputDisplayArea.width / 2 - inputImage.getWidth() / 2;
            int centerYOffset = inputDisplayArea.height / 2 - inputImage.getHeight() / 2;
            if(inputImage.getHeight() > inputDisplayArea.height){
                g.drawImage(inputImage, centerXOffset, centerYOffset, inputImage.getWidth(), inputDisplayArea.height, null);
            }//if: will overflow height
            else{
                g.drawImage(inputImage, centerXOffset, centerYOffset, null);
            }//else: will not overflow height, safe to draw as is
        }//else: image is not set to fill input display area, display normal size
    }//method: drawInputImage
    
    private void highlightJoints(Graphics g){
        for(Color jointColor : jointColors){
            Rectangle[] matchingTiles = ColorFinder.findMatchingTiles(inputImage, jointColor, jointColorTolerance);
            
            //Draw rectangles of matched tiles
            Color negativeJointColor = ColorUtil.negateColor(jointColor);
            g.setColor(negativeJointColor);
            if(FILL_INPUT_DISPLAY_AREA){
                double widthRatio = ((double)inputDisplayArea.width) / ((double)inputImage.getWidth());
                double heightRatio = ((double)inputDisplayArea.height) / ((double)inputImage.getHeight());
                for(Rectangle rect : matchingTiles){
                    int scaledX = (int)Math.round(rect.x * widthRatio);
                    int scaledY = (int)Math.round(rect.y * heightRatio);
                    int scaledWidth = (int)Math.round(rect.width * widthRatio);
                    int scaledHeight = (int)Math.round(rect.height * heightRatio);
                    g.drawRect(scaledX, scaledY, scaledWidth, scaledHeight);
                }//for: all matching tiles
            }//if: image is set to fill the input display area
            else if(FIT_INPUT_DISPLAY_AREA){
                double widthRatio = ((double)inputDisplayArea.width) / ((double)inputImage.getWidth());
                double heightRatio = ((double)inputDisplayArea.height) / ((double)inputImage.getHeight());
                if(heightRatio >= widthRatio){
                    heightRatio = widthRatio;
                }//if: the height ratio is greater than the width ratio, scale by width
                else{
                    widthRatio = heightRatio;
                }//else: the width ratio is greater, scale by height
                
                int centerXOffset = (int)Math.round(inputDisplayArea.width / 2.0 - ((inputImage.getWidth() * widthRatio) / 2.0));
                int centerYOffset = (int)Math.round(inputDisplayArea.height / 2.0 - ((inputImage.getHeight() * heightRatio) / 2.0));
                for(Rectangle rect : matchingTiles){
                    int scaledX = (int)Math.round(rect.x * widthRatio);
                    int scaledY = (int)Math.round(rect.y * heightRatio);
                    int scaledWidth = (int)Math.round(rect.width * widthRatio);
                    int scaledHeight = (int)Math.round(rect.height * heightRatio);
                    g.drawRect(scaledX + centerXOffset, scaledY + centerYOffset, scaledWidth, scaledHeight);
                    //g.drawRect(scaledX, scaledY, scaledWidth, scaledHeight);
                }//for: all matching tiles
            }//else if: image is set to fit to input display area
            else{
                //TODO: Make this inputDisplayArea handle height being too small
                int centerXOffset = inputDisplayArea.width / 2 - inputImage.getWidth() / 2;
                int centerYOffset = inputDisplayArea.height / 2 - inputImage.getHeight() / 2;
                for(Rectangle rect : matchingTiles){
                    g.drawRect(rect.x + centerXOffset, rect.y + centerYOffset, rect.width, rect.height);
                }//for: all matching tiles
            }//else: image does not fill display area
        }//for: all colors in jointColor
    }//method: highlight
    
    private void drawTaskbar(Graphics g){
        g.setColor(taskbarBackgroundColor);
        g.fillRect(0, getHeight() - TASKBAR_HEIGHT, getWidth(), TASKBAR_HEIGHT);
    }//method: drawTaskbar
    
    private void drawDebugInfo(Graphics g){
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
    }//method: drawDebugInfo
}//class: P0sePanel
