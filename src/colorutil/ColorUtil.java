package colorutil;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class ColorUtil {
    public static boolean matches(Color arg0, Color arg1, ColorTolerance tolerance){
        int redDifference = Math.abs(arg0.getRed() - arg1.getRed());
        int greenDifference = Math.abs(arg0.getGreen() - arg1.getGreen());
        int blueDifference = Math.abs(arg0.getBlue() - arg1.getBlue());
        
        //Out-of-Bounds determiners
        boolean redOOB = redDifference > tolerance.redTolerance;
        boolean greenOOB = greenDifference > tolerance.greenTolerance;
        boolean blueOOB = blueDifference > tolerance.blueTolerance;
        
        if(redOOB || greenOOB || blueOOB){
            return false;
        }//if: any color channel was past the tolerance
        else{
            return true;
        }//else: color channels were within tolerance
    }//method: matches
    
    public static Color averageRegionColor(BufferedImage image, Rectangle region){
        if(image == null || region == null){
            return null;
        }//if: input is invalid
        
        //Initial values
        int redTotal = 0;
        int greenTotal = 0;
        int blueTotal = 0;
        int numPixels = 0;
        
        //Calculating total color channel values
        for(int x = region.x; x < region.x + region.width; x++){
            for(int y = region.y; y < region.y + region.height; y++){
            	if(x >= image.getWidth() || y >= image.getHeight()){
            		continue;
            	}//if: trying to sample color out-of-bounds
            	
                int color =  image.getRGB(x,y); 
                int red   = (color & 0x00ff0000) >> 16;
                int green = (color & 0x0000ff00) >> 8;
                int blue  =  color & 0x000000ff;
                
                redTotal += red;
                greenTotal += green;
                blueTotal += blue;
                numPixels += 1;
            }//for: all y positions in the region
        }//for: all x positions in the regions
        
        //Calculating average color
        int redAverage = redTotal / numPixels;
        int greenAverage = greenTotal / numPixels;
        int blueAverage = blueTotal / numPixels;
        Color averageColor = new Color(redAverage, greenAverage, blueAverage);
        return averageColor;
    }//method: averageRegionColor
    
    public static Color negateColor(Color color){
        if(color == null){
            return null;
        }//if: color is null
        
        int negatedRed = 255 - color.getRed();
        int negatedGreen = 255 - color.getGreen();
        int negatedBlue = 255 - color.getBlue();
        
        Color negatedColor = new Color(negatedRed, negatedGreen, negatedBlue);
        return negatedColor;
    }//function: negateColor
}//class: ColorUtil
