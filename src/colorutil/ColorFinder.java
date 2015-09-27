package colorutil;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ColorFinder {
    private static final int TILE_SIZE_DIVISOR = 64;
    
    public static Rectangle[] findColor(BufferedImage image, Color color, ColorTolerance tolerance){
        return new Rectangle[0];
    }//method: findColor
    
    public static Rectangle[] findMatchingTiles(BufferedImage image, Color color, ColorTolerance tolerance){
        ArrayList<Rectangle> matchingTiles = new ArrayList<Rectangle>();
        //matchingTiles.add(new Rectangle(100, 100, 16, 16)); //debugging
        
        //Finding matching tiles
        int tileWidth = image.getWidth() / TILE_SIZE_DIVISOR;
        int tileHeight = image.getHeight() / TILE_SIZE_DIVISOR;
        for(int x = 0; x < image.getWidth(); x += tileWidth){
            for(int y = 0; y < image.getHeight(); y += tileHeight){
            	//Update tile position
                Rectangle currentTile = new Rectangle(x, y, tileWidth, tileHeight);
            	
                Color averageTileColor = ColorUtil.averageRegionColor(image, currentTile);
                boolean matchResult = ColorUtil.matches(color, averageTileColor, tolerance);
                if(matchResult == true){
                    matchingTiles.add(currentTile);
                }//if: the average tile color matches the input color, within tolerance
            }//for: all y positions of tiles
        }//for: all x positions of tiles
        
        //Convert to array and return
        Rectangle[] matchingTilesArray = new Rectangle[matchingTiles.size()];
        return matchingTiles.toArray(matchingTilesArray);
    }//function: findMatchingTiles
}//class: ColorFinder
