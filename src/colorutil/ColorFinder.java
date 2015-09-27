package colorutil;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ColorFinder {
    private static final int NUM_TILES = 64;
    
    public static Rectangle[] findColor(BufferedImage image, Color color, ColorTolerance tolerance){
        return new Rectangle[0];
    }//method: findColor
    
    public static Rectangle[] findMatchingTiles(BufferedImage image, Color color, ColorTolerance tolerance){
        ArrayList<Rectangle> matchingTiles = new ArrayList<Rectangle>();
        //matchingTiles.add(new Rectangle(100, 100, 16, 16)); //debugging
        
        //Finding matching tiles
        int tileWidth = image.getWidth() / NUM_TILES;
        int tileHeight = image.getHeight() / NUM_TILES;
        Rectangle currentTile = new Rectangle(0, 0, tileWidth, tileHeight);
        for(int x = 0; x < NUM_TILES; x++){
            for(int y = 0; y < NUM_TILES; y++){
                Color averageTileColor = ColorUtil.averageRegionColor(image, currentTile);
                boolean matchResult = ColorUtil.matches(color, averageTileColor, tolerance);
                if(matchResult == true){
                    matchingTiles.add(currentTile);
                }//if: the average tile color matches the input color, within tolerance
                
                //Update tile position
                currentTile = new Rectangle(currentTile.x, currentTile.y + tileHeight, tileWidth, tileHeight);
            }//for: all y positions of tiles
            //Update tile position
            currentTile = new Rectangle(currentTile.x + tileWidth, 0, tileWidth, tileHeight);
        }//for: all x positions of tiles
        
        //Convert to array and return
        Rectangle[] matchingTilesArray = new Rectangle[matchingTiles.size()];
        return matchingTiles.toArray(matchingTilesArray);
    }//function: findMatchingTiles
}//class: ColorFinder
