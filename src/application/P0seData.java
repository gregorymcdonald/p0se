package application;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import colorutil.ColorTolerance;
import poseData.JointNode;
import poseData.Pose;

public class P0seData {
	
	/* ***** DEFAULT NODE COLORS ***** */
    //White Color: for DEBUGGING
    //private static Color whiteNodeColor = new Color(255, 255, 255);
    //private static ColorTolerance whiteNodeTolerance = new ColorTolerance(30);
    
    //Yellow Node Color
    private static Color yellowNodeColor = new Color(150, 150, 30);
    private static ColorTolerance yellowNodeTolerance = new ColorTolerance(30);
    
    private static Color blueNodeColor = new Color(30, 60, 80);
    private static ColorTolerance blueNodeTolerance = new ColorTolerance(30);
    
    public static final ColorPair[] DEFAULT_JOINT_NODE_COLORS = {
        //DEBUGGING
        //new ColorPair(whiteNodeColor, whiteNodeTolerance),
        
        //REAL DEFAULTS
        new ColorPair(yellowNodeColor, yellowNodeTolerance),
        new ColorPair(blueNodeColor, blueNodeTolerance)
    };
    
    public void loadDefaultColors(){
    	//Currently no-op
    }//method: loadDefaultColors
    
    /* ***** DEFAULT POSES ***** */
    public static Pose[] defaultP0ses;
    
    public static void loadDefaultP0ses(){
    	//Initialize default HashMap
    	defaultP0ses = new Pose[1];
    	
    	int id = 0;
    	
    	//"Yellow over Blue" pose
    	ArrayList<Double> angles = new ArrayList<Double>();
    	angles.add(0.0);
    	angles.add(270.0);
    	Pose yellowOverBlue = new Pose(angles, id);
    	yellowOverBlue.setName("Yellow over Blue");
    	id += 1;
    	defaultP0ses[yellowOverBlue.getID()] = yellowOverBlue;
    }//method: loadDefaultPoses
    
    public static Pose findPose(String name){
    	//Look through default and saved Poses for a Pose called "name"
    	for(int i = 0; i < defaultP0ses.length; i++){
    		if(name.equals(defaultP0ses[i].getName())){
    			return defaultP0ses[i];
    		}//if: name found
    	}//for: all default p0ses
    	return null; //if not found
    }//method: findPose
}//class: P0seData
