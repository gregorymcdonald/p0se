package colorutil;

import java.awt.Color;

public class ColorTolerance {
    /* ***** TOLERANCE FIELDS ***** */
    public int redTolerance;
    public int greenTolerance;
    public int blueTolerance;
    
    public ColorTolerance(){
        this(0, 0, 0);
    }//constructor: default
    
    public ColorTolerance(int tolerance){
        this(tolerance, tolerance, tolerance);
    }//constructor: single value specifies all tolerances
    
    public ColorTolerance(int redTolerance, int greenTolerance, int blueTolerance){
        this.redTolerance = redTolerance;
        this.greenTolerance = greenTolerance;
        this.blueTolerance = blueTolerance;
    }//constructor: all fields initiated
    
    
}//class: ColorTolerance
