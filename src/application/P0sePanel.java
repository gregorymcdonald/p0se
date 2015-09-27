package application;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

import javax.swing.JPanel;

import colorutil.ColorFinder;
import colorutil.ColorTolerance;
import colorutil.ColorUtil;

import P0seRecognizer.P0seRecognizer;

import poseData.Pose;

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
    private static final boolean HIGHLIGHT_JOINT_COLOR_REGIONS = false;
    private static final boolean HIGHLIGHT_AVERAGE_COLOR_POINTS = true;
    private static final int INPUT_DELAY = 100; //in milliseconds
    private static final int RECENT_DELAY = 3000; //in milliseconds
    private static Rectangle inputDisplayArea;
    private static BufferedImage inputImage;
    //Taskbar Display
    private static final int TASKBAR_HEIGHT = 96;
    private static Color taskbarBackgroundColor;
    
    /* ***** P0SE FIELDS ***** */
    private static P0seRecognizer p0seRecognizer;
    private static Webcam inputDevice;
    private static ArrayList<P0seTimer> recentP0ses;
    
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
        P0seData.loadDefaultP0ses();
        P0seData.loadDefaultExecutables();
        p0seRecognizer = new P0seRecognizer();
        recentP0ses = new ArrayList<P0seTimer>();
        
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
    		
    		//p0seRecognizer.updateNodeLocations(inputImage);
    		//p0seRecognizer.compareCurrentPose();
    		
    		//Decrement time remaining on recent p0ses
    		updateRecentP0ses();
    		
    		Pose currentPose = p0seRecognizer.getPose(inputImage);
    		//Match with default p0ses
    		Pose[] matchingP0ses = p0seRecognizer.matchPose(currentPose, P0seData.defaultP0ses);
    		//execute matching poses
    		executeMappedStatements(matchingP0ses);
    		//add to recent p0ses
    		addToRecentP0ses(matchingP0ses);
    		
    		
    		repaint();
    		delay(INPUT_DELAY);
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
    
    private void executeMappedStatements(Pose[] p0ses){
        for(Pose p0se : p0ses){
            if(!recentlyP0sed(p0se)){
                P0seData.p0seExecutables_Windows.get(p0se).execute();
            }//if: not recently p0sed
        }//for: all p0ses
    }//method: executeMappedStatements
    
    private void updateRecentP0ses(){
    	for(int i = 0; i < recentP0ses.size(); i++){
    		P0seTimer recentP0se = recentP0ses.get(i);
    		recentP0se.tick(INPUT_DELAY);
    		if(recentP0se.timerReached()){
    			recentP0ses.remove(i);
    			i--;
    		}//if: the recent p0se's time has elapsed
    	}//for: all recent p0ses
    }//method: updateRecentP0ses
  
    private boolean recentlyP0sed(Pose p0se){
    	for(int i = 0; i < recentP0ses.size(); i++){
    		Pose recentP0se = recentP0ses.get(i).p0se;
    		if(p0se.equals(recentP0se)){
    			return true;
    		}//if: p0se is recent
    	}//for: all recent p0ses
    	return false;
    }//method: recentlyP0sed
    
    private void addToRecentP0ses(Pose[] p0ses){
    	for(int i = 0; i < p0ses.length; i++){
    		if(!recentlyP0sed(p0ses[i])){
    			P0seTimer recentP0se = new P0seTimer(p0ses[i], RECENT_DELAY);
    			recentP0ses.add(recentP0se);
    		}//if: the p0se was not recently p0sed
    	}//for: all input p0ses
    }//method: addToRecentP0ses
    
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
        //TODO: Reformat to have outer for loop, with checks interior
        for(ColorPair jointColorPair : P0seData.DEFAULT_JOINT_NODE_COLORS){
            Color jointColor = jointColorPair.color;
            ColorTolerance jointColorTolerance = jointColorPair.tolerance;
            
            //Set the graphics object to draw with the negative color
            Color negativeJointColor = ColorUtil.negateColor(jointColor);
            g.setColor(negativeJointColor);
            
            if(HIGHLIGHT_AVERAGE_COLOR_POINTS){
                Point matchingPoint = ColorFinder.findColor(inputImage, jointColor, jointColorTolerance);
                if(matchingPoint == null){
                    continue;
                }//if: no matching point was found
                
                if(FIT_INPUT_DISPLAY_AREA){
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
                    int scaledX = (int)Math.round(matchingPoint.x * widthRatio);
                    int scaledY = (int)Math.round(matchingPoint.y * heightRatio);
                    g.fillOval(scaledX + centerXOffset, scaledY + centerYOffset, 10, 10);
                }//if: fitting input to display area
            }//if: HIGHLIGHT_AVERAGE_COLOR_POINTS is true
            
            if(HIGHLIGHT_JOINT_COLOR_REGIONS){
                Rectangle[] matchingTiles = ColorFinder.findColorRegions(inputImage, jointColor, jointColorTolerance);
                if(matchingTiles.length == 0){
                    continue;
                }//if: no matching tiles found
                
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
            }//if: HIGHLIGHT_JOINT_COLOR_REGIONS is true
        }//for: all joint color pairs, in the default array
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
        
            //Draw Recent P0ses
            String recentP0sesHeader = "Recent p0ses:";
            g.drawString(recentP0sesHeader, debugDrawX, debugDrawY);
            debugDrawY += 15;
            for(P0seTimer recentP0se : recentP0ses){
            	String recentP0seName = recentP0se.p0se.getName();
                g.drawString(recentP0seName, debugDrawX, debugDrawY);
                debugDrawY += 15;
            }//for: all recent p0ses
        }//if: in debug mode
    }//method: drawDebugInfo
}//class: P0sePanel
