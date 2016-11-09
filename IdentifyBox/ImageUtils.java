/*
*	File: ImageUtils.java
*	Utility class for image resizing
*	This class is meant to keep the aspect ratio when resizing images. 
* 
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;


public class ImageUtils{

	public double getScaleFactor(int iMasterSize, int iTargetSize) {

	    double dScale = 1;
	    if (iMasterSize > iTargetSize) {
	        dScale = (double) iTargetSize / (double) iMasterSize;
	    } else {
	        dScale = (double) iTargetSize / (double) iMasterSize;
	    }
	    return dScale;

	}

	public ArrayList<Integer> getScaledDimensions(BufferedImage image, Dimension targetSize) {

		int oldImgWidth = image.getWidth();
		int oldImgHeight = image.getHeight();

    	double dScaleWidth = getScaleFactor(oldImgWidth, targetSize.width);
    	double dScaleHeight = getScaleFactor(oldImgHeight, targetSize.height);

    	double dScale = Math.max(dScaleHeight, dScaleWidth);

    	int newImgwidth = (int)(dScale * oldImgWidth);
    	int newImgHeight = (int)(dScale * oldImgHeight);

    	return new ArrayList<Integer>(Arrays.asList(newImgwidth, newImgHeight));
	}

	public BufferedImage resize(BufferedImage image, int width, int height) {
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}

}