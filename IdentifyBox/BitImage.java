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
import java.util.Arrays;
import java.util.List;
 import java.util.ArrayList;

class BitImage{

	private static SampleModel sampleModel;


	public static void main(String[] args) throws IOException, RuntimeException{

		// Files to play with
		String input_file = "./input/im1-c.bmp";
		String output_file_constrast = "lap.bmp";
		String outfile_file_kirschEdge = "smoothing_with_kirschEdge.bmp";

/*
		System.out.println("Reading an bitmap image" + input_file);
		System.out.println("Printing the file in [0-255] range " + input_file);
		//printPixelArray(getPixelArray(filename));
		//***Histogram process***
		System.out.println("Now applying contrast filter...");
		writeToFile(writeToImage(constrast(getPixelArray(input_file), 3, 127)), output_file_constrast);
		*/
		//***Smoothing process***

		// Just testing older laplacian kernel
		System.out.println("Now applying lap filter...");

		// prints out the first r value
		System.out.println(houghTransform(applyLapEdge(smooth(getPixelArray(input_file), 6,6))).get(0).r);


	}



	/**
	 * Private methods for testing
	 *
	 */
  // Getting pixels from an array
	private static int [][] getPixelArray(String filename) throws IOException{

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
	private static int[][] modifyPixelArray(int [][] arr){
		for (int i = 0; i < arr.length; i++){
			for (int j = 0; j < arr[i].length; j++){
				if (arr[i][j] < 100){
					arr[i][j] += 15;
				}
			}
		}

		return arr;
	}




	// Contrast Adjustment
	private static int[][] constrast(int[][] arr, int contrast, int brightness){
		for (int i = 0; i < arr.length; i++){
			for (int j = 0; j < arr[i].length; j++){
				arr[i][j] = contrast*(arr[i][j] - brightness) + brightness;
			}
		}

		return arr;
	}


	public static List<rThetaPair> returnPopularPairs(int[][] accum, int lengthofDiagOfImg){

		List<rThetaPair> bestPairs = new ArrayList<rThetaPair>();
		int threshold = 680; // test

		for(int theta = 0; theta < 181; theta ++){
			for(int r = 0 ; r < (lengthofDiagOfImg*2)+1; r++){
				if(accum[theta][r] >= threshold){
					rThetaPair newpair = new rThetaPair(r, theta);
					bestPairs.add(newpair);
				}
			}
		}

		return bestPairs;
	}

	private static List<rThetaPair> houghTransform(int[][] arr)
	{
		/* NOTICE: HoughTranform only takes in array that has edges (255), will not work otherwise */
		int lengthofDiagOfImg = (int) Math.sqrt(arr.length * arr.length + arr[0].length * arr[0].length);


		// Initialize the accumulator to 0
		int [][] accum = new int[181][(lengthofDiagOfImg*2)+1];


		// For each image point P
		for (int x = 0; x < arr.length; x++){
			for (int y = 0; y < arr[x].length; y++){

				// If p is part of an edge ...
				if (arr[x][y] == 255 ){

					//System.out.println("x: " + x + " y: " + y);
					// Use the accumulator to collect possible lines for this edge
					for(int theta = 0; theta <= 180; theta++){
						// Testing rho values. Keeping it positive by adding length of diagonal of the image
						int rho =  ((int)(x * Math.cos(Math.toRadians(theta))) +  (int)(y * Math.sin(Math.toRadians(theta)))) + lengthofDiagOfImg;
						//System.out.println(rho);
						//System.out.println(theta);
						accum[theta][rho]++;
					}

				}
			}
		}

			return returnPopularPairs(accum, lengthofDiagOfImg);
	}

	private static int[][] applyLapEdge(int[][] arr){
		int sum = 0;
		int avg;
		//"frame" refers to small segment being modified at a time
		int frameX = 3;
		int frameY = 3;
		int currentStartRow = 0;
		int currentMaxRow = 3;
		int currentStartColumn = 0;
		int currentMaxColumn = 3;
		int kernelColumn = 0;
		int kernelRow = 0;
		int [][] outputArray = new int[arr.length][arr[0].length];
		int threshold = 10;
		int [][] weightedKernel = new int[][] {
			{0, -1, 0},
			{-1, 4, -1},
			{0, -1, 0}
		};

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
						kernelColumn = j%3;
						kernelRow = i%3;
						sum += weightedKernel[kernelRow][kernelColumn]*arr[i][j];
					}
				}

				if(sum > 255){
					sum = 255;
				}else if(sum < 0){
					sum = 0;
				}

				if(sum <= threshold){
					outputArray[currentStartRow+1][currentStartColumn+1] = 255;
					sum = 0;
				}else{
					outputArray[currentStartRow+1][currentStartColumn+1] = 0;
					sum = 0;
				}

				currentStartColumn += 1;//frameX;
				currentMaxColumn += 1; //frameX;
			}
			currentStartRow += 1;//frameY;
			currentMaxRow += 1;//frameY;
			currentMaxColumn = 3;//frameX;
			currentStartColumn = 0;
			//System.out.println("adding to row");
		}
		return outputArray;
	}

	private static int[][] kirschEdge(int[][] arr) //whoops, its actually kirsch
	{
		int sum = 0;
		int tempSumN = 0;
		int tempSumS = 0;
		int tempSumW = 0;
		int tempSumE = 0;
		int tempSumNE = 0;
		int tempSumNW = 0;
		int tempSumSW = 0;
		int tempSumSE = 0;
		int threshold = 350;
		int avg;
		//"frame" refers to small segment being modified at a time
		int frameX = 3;
		int frameY = 3;
		int currentStartRow = 0;
		int currentMaxRow = 3;
		int currentStartColumn = 0;
		int currentMaxColumn = 3;
		int kernelColumn = 0;
		int kernelRow = 0;
		int [][] outputArray = new int[arr.length][arr[0].length];
		int [][]  N = new int[][] {
			{-3, -3, 5},
			{-3, 0, 5},
			{-3, -3, 5}
		};
		int [][]  NW = new int[][] {
			{-3, -5, 5},
			{-3, 0, 5},
			{-3, -3, -3}
		};
		int [][]  W = new int[][] {
			{5, 5, 5},
			{-3, 0, -3},
			{-3, -3, -3}
		};
		int [][]  SW = new int[][] {
			{5, 5, -3},
			{5, 0,-3},
			{-3, -3, -3}
		};
		int [][]  S = new int[][] {
			{5, -3, -3},
			{5, 0, -3},
			{5, -3, -3}
		};
		int [][]  SE = new int[][] {
			{-3, -3, -3},
			{5, 0, -3},
			{5, 5, -3}
		};
		int [][]  E = new int[][] {
			{-3, -3, -3},
			{-3, 0, -3},
			{5, 5, 5}
		};
		int [][]  NE = new int[][] {
			{-3, -3, -3},
			{-3, 0, 5},
			{-3, 5, 5}
		};

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
						kernelColumn = j%3;
						kernelRow = i%3;

						tempSumN += N[kernelRow][kernelColumn]*arr[i][j];
						tempSumW += W[kernelRow][kernelColumn]*arr[i][j];
						tempSumE += E[kernelRow][kernelColumn]*arr[i][j];
						tempSumS += S[kernelRow][kernelColumn]*arr[i][j];

						tempSumNE += NE[kernelRow][kernelColumn]*arr[i][j];
						tempSumNW += NW[kernelRow][kernelColumn]*arr[i][j];
						tempSumSE += SE[kernelRow][kernelColumn]*arr[i][j];
						tempSumSW += SW[kernelRow][kernelColumn]*arr[i][j];

					}
				}
				if(tempSumN > sum)
					sum = tempSumN;
				if(tempSumS > sum)
					sum = tempSumS;
				if(tempSumW > sum)
					sum = tempSumW;
				if(tempSumE > sum)
					sum = tempSumE;
				if(tempSumNE > sum)
					sum = tempSumNE;
				if(tempSumSW > sum)
					sum = tempSumSW;
				if(tempSumNW > sum)
					sum = tempSumNW;
				if(tempSumSE > sum)
					sum = tempSumSE;

				tempSumE = 0;
				tempSumN = 0;
				tempSumS = 0;
				tempSumW = 0;
				tempSumNE = 0;
				tempSumNW = 0;
				tempSumSE = 0;
				tempSumSW = 0;

				//System.out.println("sum variable is: " + sum);
/*
				if(sum > 255){
					sum = 255;
				}else if(sum < 0){
					sum = 0;
				}
*/

				if(sum <= threshold){
					outputArray[currentStartRow+1][currentStartColumn+1] = 255;
					sum = 0;
				}else{
					outputArray[currentStartRow+1][currentStartColumn+1] = 0;
					sum = 0;
				}

				currentStartColumn += 1;//frameX;
				currentMaxColumn += 1; //frameX;
			}
			currentStartRow += 1;//frameY;
			currentMaxRow += 1;//frameY;
			currentMaxColumn = 3;//frameX;
			currentStartColumn = 0;
			//System.out.println("adding to row");
		}
		return outputArray;
	}

	// Histogram Equalization
	private static int[][] histogramEqualize(int [][] arr){

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


	/*
	int [][] gaussianKernel = new int[][] {
			{1, 4, 6, 4, 1},
			{4, 16, 25, 16, 4},
			{6, 28, 49, 28, 6},
			{4, 16, 25, 16, 4},
			{1, 4, 6, 4, 1}
		};
	*/
	//Smoothing
	//Smoothing
	private static int[][] smooth(int[][] arr, int frameX, int frameY)
	{
		int sum = 0;
		int median;
		//"frame" refers to small segment being modified at a time
		int currentStartRow = 0;
		int currentMaxRow = frameY;
		int currentStartColumn = 0;
		int currentMaxColumn = frameX;
		int kernelColumn = 0;
		int kernelRow = 0;
		int[] sortArray = new int[frameX*frameY];
		int sortIndex=0;
		int [][] outputArray = new int[arr.length][arr[0].length];
		int[][] gaussianKernel = new int[][] {
			{1, 4, 6 , 4, 1},
			{3, 14, 23, 14, 3},
			{6, 23, 49, 23, 6},
			{3, 14, 23, 14, 3},
			{1, 4, 6, 4, 1}
		};
		int [][] weightedKernel = new int[][] {
			{3, 6, 3},
			{6, 20, 6},
			{3, 6, 3}
		};

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
						kernelColumn = j%frameX;
						kernelRow = i%frameY;
						//sum += weightedKernel[kernelRow][kernelColumn]*arr[i][j];
						sortArray[sortIndex] = arr[i][j];
						sortIndex++;
					}
				}
				sortIndex = 0;
				Arrays.sort(sortArray);
				sum = sortArray[(frameX*frameY)/2];
				median = sum;
				sum = 0;

				if(median > 255)
				{
					System.out.println("Correcting to 255.");
					median = 255;
				}
				else if(median < 0)
				{
					System.out.println("Correcting to 0.");
					median = 0;
				}
				outputArray[currentStartRow+1][currentStartColumn+1] = median;

				currentStartColumn += 1;
				currentMaxColumn += 1;
			}
			currentStartRow += 1;
			currentMaxRow += 1;
			currentMaxColumn = frameX;
			currentStartColumn = 0;
			//System.out.println("adding to row");
		}
		return outputArray;
	}

  // Printing pixels
	public static void printPixelArray(int [][] array2D){

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
	private static BufferedImage writeToImage(int [][] arr) throws IOException{

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

    	return image;
	}


	/**
	 *	End of private methods for testing
	 */


	public static void writeToFile(BufferedImage image, String file_to_save) throws IOException, RuntimeException{
		File file = new File(file_to_save);

    	if (file.createNewFile()){
    		System.out.println("File creation successful");
    	} else {
    		System.out.println("File already exists.");
    	}

		if (!ImageIO.write(image, "BMP", new File(file_to_save))) {
  			throw new RuntimeException("Unexpected error writing image");
		}

	}


	/**
	 * Start of public methods for GUI
	 *
	 */
	// Public methods

	public BufferedImage applySmoothing(String filename, int width, int height) throws IOException{
		return writeToImage(smooth(getPixelArray(filename), width, height));
	}

	public BufferedImage applyHistogramEqualization(String filename) throws IOException{
		return writeToImage(histogramEqualize(getPixelArray(filename)));
	}

	public BufferedImage applyLapicianEdgeDetection(String filename) throws IOException{
		System.out.println("Applied Lap Edge.");
		return writeToImage(applyLapEdge(getPixelArray(filename)));
	}

	public BufferedImage applyKirschEdgeDetection(String filename) throws IOException{
		return writeToImage(kirschEdge(getPixelArray(filename)));
	}

	public BufferedImage applyContrast(String filename, int constrast, int brightness) throws IOException{
		return writeToImage(constrast(getPixelArray(filename), constrast, brightness));
	}

	/**
	 *  End of public methods for GUI
	 */



}
