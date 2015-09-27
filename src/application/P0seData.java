package application;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import P0seRecognizer.Executable;
import colorutil.ColorTolerance;
import poseData.JointNode;
import poseData.Pose;

public class P0seData {
	
	/* ***** DEFAULT NODE COLORS ***** */
    //White Color: for DEBUGGING
    //private static Color whiteNodeColor = new Color(255, 255, 255);
    //private static ColorTolerance whiteNodeTolerance = new ColorTolerance(30);
    
    //Yellow Node Color
    private static Color yellowNodeColor = new Color(160, 179, 60);
    private static ColorTolerance yellowNodeTolerance = new ColorTolerance(30);
    
    private static Color blueNodeColor = new Color(18, 110, 169);
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
    	defaultP0ses = new Pose[3];
    	
    	int id = 0;
    	
    	//"Yellow over Blue" pose
    	ArrayList<Double> angles = new ArrayList<Double>();
    	angles.add(0.0);
    	angles.add(270.0);
    	Pose yellowOverBlue = new Pose(angles, id);
    	yellowOverBlue.setName("Yellow over Blue");
    	id += 1;
    	defaultP0ses[yellowOverBlue.getID()] = yellowOverBlue;
    	
    	//"John Cena" pose
    	angles = new ArrayList<Double>();
    	angles.add(0.0);
    	angles.add(0.0);
    	Pose johnCena = new Pose(angles, id);
    	johnCena.setName("John Cena");
    	id += 1;
    	defaultP0ses[johnCena.getID()] = johnCena;
    	
    	//"WMP" pose
    	angles = new ArrayList<Double>();
    	angles.add(0.0);
    	angles.add(90.0);
    	Pose winMP = new Pose(angles, id);
    	winMP.setName("Win MP");
    	id += 1;
    	defaultP0ses[winMP.getID()] = winMP;
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
    
    /* ***** P0SE - EXECUTABLE MAPPINGS ***** */
    public static HashMap<Pose, Executable> p0seExecutables_Windows;
    public static HashMap<Pose, Executable> p0seExecutables_Mac;
    
    public static Executable openChromeMac = new Executable(new String[]{"open", "-a", "Google Chrome", "--new", "--args"}, Executable.MAC_OS_HEADER);
    
    //Must be called after loadDefaultP0ses
    public static void loadDefaultExecutables(){
        p0seExecutables_Windows = new HashMap<Pose, Executable>();
        p0seExecutables_Mac = new HashMap<Pose, Executable>();
        
        Executable openChromeWindows = new Executable(new String[]{"cmd", "/c", "start", "chrome", "/new-window"}, Executable.WINDOWS_OS_HEADER);
        p0seExecutables_Windows.put(findPose("Yellow over Blue"), openChromeWindows);
        Executable openChromeMac = new Executable(new String[]{"open", "-a", "Google Chrome", "--new", "--args"}, Executable.MAC_OS_HEADER);
        p0seExecutables_Mac.put(findPose("Yellow over Blue"), openChromeMac);
        
        Executable openCenaWindows = new Executable(new String[]{"cmd", "/c", "start", "chrome", "/new-window", "https://youtu.be/3HoZNpPTRDU?t=10s"}, Executable.WINDOWS_OS_HEADER);
        p0seExecutables_Windows.put(findPose("John Cena"), openCenaWindows);
        Executable openCenaMac = new Executable(new String[]{"open", "-a", "Google Chrome", "--new", "--args", "https://youtu.be/3HoZNpPTRDU?t=10s"}, Executable.MAC_OS_HEADER);
        p0seExecutables_Mac.put(findPose("John Cena"), openCenaMac);
        
        Executable openWindowsMP = new Executable(new String[]{"cmd", "/c", "start", "C:\\Users\\Kieran\\Documents\\Secret_Track.mp3"}, Executable.WINDOWS_OS_HEADER);
        p0seExecutables_Windows.put(findPose("Win MP"), openWindowsMP);
    }//method: loadDefaultExecutables
}//class: P0seData
