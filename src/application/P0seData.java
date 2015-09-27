package application;

import java.awt.Color;

import colorutil.ColorTolerance;

public class P0seData {
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
}//class: P0seData
