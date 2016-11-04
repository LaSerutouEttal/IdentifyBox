/**
 * BitImage.java
 */

/*
Some Notes:

BufferedImage class - Allows us to operate directly with image data (retrieving/setting pixel values)
Raster - data structure that holds image data (allows us to scan image data)
SampleModel - Interface that allows for pixel extraction.
*/

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.Point;
import java.awt.image.WritableRaster;
import java.awt.image.SampleModel;
import java.io.*;
import javax.imageio.*;

public class BitImage{

	private static SampleModel sampleModel;

  // Getting pixels from an array
	private  int [][] getPixelArray(String filename) throws IOException{

    	BufferedImage image = ImageIO.read(new File(filename));
    	Raster raster = image.getData();
    	sampleModel = raster.getSampleModel();

    	int [][] array2D =  new int[image.getWidth()][image.getHeight()];

		for (int j = 0; j < image.getWidth(); j++) {
    		for (int k = 0; k < image.getHeight(); k++) {
        		array2D[j][k] = raster.getSample(j, k, 0);
    		}
		}

		return array2D;
	}


	// Modify test
	private  int[][] modifyPixelArray(int [][] arr){
		for (int i = 0; i < arr.length; i++){
			for (int j = 0; j < arr[i].length; j++){
				if (arr[i][j] < 100){
					arr[i][j] += 15;
				}
			}
		}

		return arr;
	}

	// Histogram Equalization
	private  int[][] histogramEqualize(int [][] arr){

		int[] H = new int[256];

		// Initialize the Histogram
		for(int i = 0; i < H.length; i++){
			H[i] = 0;
		}

		// Compute the Histogram
		for (int i = 0; i < arr.length; i++){
			for (int j = 0; j < arr[i].length; j++){
					H[arr[i][j]] += 1;
			}
		}

		// Compute the cumulative Histogram
		for(int i = 1; i < 256; i++){
			H[i] = H[i-1] + H[i];
		}

		// Normalize the cumulative Histogram
		for(int i = 0; i < 256; i++){
			H[i] = H[i] * 255 / (arr.length * arr[0].length);
		}

		//Modify Image
		for (int i = 0; i < arr.length; i++){
			for (int j = 0; j < arr[i].length; j++){
				arr[i][j] = H[arr[i][j]];
			}
		}

		return arr;
	}

	//Smoothing
	private  int[][] smooth(int[][] arr, int frameX, int frameY) //add dimensions of frame
	{
		int sum = 0;
		int avg;
		//"frame" refers to small segment being modified at a time
		int currentStartRow = 0;
		int currentMaxRow = frameY;
		int currentStartColumn = 0;
		int currentMaxColumn = frameX;
		
		//operate on rows
		while(currentMaxRow < arr.length)
		{
			//System.out.println("currentStartRow = " + currentStartRow);
			//operate on columns
			while(currentMaxColumn < arr[0].length)
			{
				//System.out.println("currentStartColumn = " + currentStartColumn);
				//gather average of frame 
				for(int i=currentStartRow; i<currentMaxRow; i++)
				{
					for(int j=currentStartColumn; j<currentMaxColumn; j++)
					{
						sum += arr[i][j];
					}
				}
				
				avg = sum/(frameX*frameY);
				sum = 0;
				
				//overwrite frame values with average 
				for(int k=currentStartRow; k<currentMaxRow; k++)
				{
					for(int y=currentStartColumn; y<currentMaxColumn; y++)
					{
						arr[k][y] = avg;
						//System.out.println("Setting frame " + k + "," + y + " to average value " + avg + ".");
					}
				}
				currentStartColumn += frameX;
				currentMaxColumn += frameX;
				
			}
			currentStartRow += frameY;
			currentMaxRow += frameY;
			currentMaxColumn = frameX;
			currentStartColumn = 0;
			//System.out.println("adding to row");
		}	
		return arr;
	}

  // Printing pixels
	private void printPixelArray(int [][] array2D){

    	for (int x = 0; x < array2D.length; x++)
    	{
        	for (int y = 0; y < array2D[x].length; y++)
        	{
            	System.out.print(array2D[x][y] + " ");
        	}
        	System.out.println();
    	}

	}


  // New Output Image
	private void writeToImage(int [][] arr, String filename) throws IOException, RuntimeException{

		WritableRaster raster= Raster.createWritableRaster(sampleModel, new Point(0,0));
    	for(int i=0;i<arr.length;i++)
    	{
        	for(int j=0;j<arr[i].length;j++)
        	{
            	raster.setSample(i,j,0,arr[i][j]);
        	}
    	}

    	BufferedImage image=new BufferedImage(arr.length,arr[0].length,BufferedImage.TYPE_BYTE_GRAY);
    	image.setData(raster);

    	File file = new File(filename);

    	if (file.createNewFile()){
    		System.out.println("File creation successful");
    	} else {
    		System.out.println("File already exists.");
    	}

		if (!ImageIO.write(image, "BMP", new File(filename))) {
  			throw new RuntimeException("Unexpected error writing image");
		}
	}

	public BufferedImage applySmoothing(String filename, int width, int height) throws IOException{
		int [][] arr = smooth(getPixelArray(filename), 4, 4);
		return new BufferedImage(arr.length,arr[0].length,BufferedImage.TYPE_BYTE_GRAY); 
	}

	public BufferedImage applyHistogramEqualization(String filename) throws IOException{
		int [][] arr = histogramEqualize(getPixelArray(filename));
		return new BufferedImage(arr.length,arr[0].length,BufferedImage.TYPE_BYTE_GRAY);
	}

	public void save(Filename) throws IOException, RuntimeException{

	}
}
