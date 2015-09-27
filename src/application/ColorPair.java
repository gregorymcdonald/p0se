package application;

import java.awt.Color;

import colorutil.ColorTolerance;

public class ColorPair {
    public Color color;
    public ColorTolerance tolerance;
    
    public ColorPair(Color color, ColorTolerance tolerance){
        this.color = color;
        this.tolerance = tolerance;
    }//constructor: enumerate all fields
}//class: ColorPair
