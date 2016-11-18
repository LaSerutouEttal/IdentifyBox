/*
*	File: ImageProcessGUI.java
*
<<<<<<< HEAD
=======
*	Naming conventions:
*	Buttons - (name)Btn
*	Panels - (name)Panel
*	Radio Button - (name)RdioBtn
*	Labels - (name)Label
* 	ButtonGroup - (name)BtnGrp
*   Dialog - (name)diag
*   ActionListener - (name)listner
*
*
*	Components are added in the order they appear.
* 
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.JCheckBox;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

<<<<<<< HEAD
public class ImageProcessGUI extends JFrame{

	private LoadImagePanel loadPanel;
	private ImageTransformPanel transformPanel;
	public String originalLoadedImageName = "";
=======

/**
 * 	This is the main GUI class
 */
public class ImageProcessGUI extends JFrame{

	private LoadInitialImagePanel loadPanel;
	private ImageTransformPanel transformPanel;
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4

	public ImageProcessGUI(){
		super("Image Processing Application");
		getContentPane().setLayout(new BorderLayout());

<<<<<<< HEAD
		loadPanel = new LoadImagePanel("Original Image");
		transformPanel = new ImageTransformPanel("Steps to generate output images", loadPanel);

		add(loadPanel, BorderLayout.CENTER);
		add(transformPanel, BorderLayout.LINE_END);

=======
		loadPanel = new LoadInitialImagePanel("Original Image");
		transformPanel = new ImageTransformPanel("Steps to generate output images", loadPanel);
		
		add(loadPanel, BorderLayout.CENTER);			
		add(transformPanel, BorderLayout.LINE_END);
		
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(950, 750);
		setVisible(true);
	}

	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ImageProcessGUI();
            }
        });
<<<<<<< HEAD
	}
}

class LoadImagePanel extends JPanel{

	private JLabel labelImage;
	private JLabel imageLabel;
	private JButton loadImage;
	private ImageUtils imageUtils = new ImageUtils();
	private String path = " ";

	public LoadImagePanel(String title){
=======
	} 
}


/**
 *  This acts the display for the initial image to be processed 
 */
class LoadInitialImagePanel extends JPanel{

	private JLabel imgTitleLabel, emptyLabel;
	private JButton loadImageBtn;

	private ImageUtils imageUtils = new ImageUtils();
	private String path = " ";

	public LoadInitialImagePanel(String title){
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new TitledBorder(new EtchedBorder(), title));

<<<<<<< HEAD
		labelImage = new JLabel("Image:");
		imageLabel = new JLabel();
		loadImage = new JButton("Load Image to be Compared Against");
		loadImage.addActionListener(new ActionListener(){
=======
		imgTitleLabel = new JLabel("Image:");
		emptyLabel = new JLabel();
		loadImageBtn = new JButton("Load Image to be Compared Against");
		loadImageBtn.addActionListener(new ActionListener(){
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
			public void actionPerformed(ActionEvent e){
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(new File("").getAbsolutePath(), "input"));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Bitmap Images", "bmp");
    			fc.setFileFilter(filter);
				int result = fc.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					path = file.getAbsolutePath();
					try{
						BufferedImage img = ImageIO.read(file);
						ArrayList<Integer> scaledDimens = imageUtils.getScaledDimensions(img, new Dimension(500, 500));
<<<<<<< HEAD
						imageLabel.setIcon(new ImageIcon(imageUtils.resize(img, scaledDimens.get(0), scaledDimens.get(1))));
=======
						emptyLabel.setIcon(new ImageIcon(imageUtils.resize(img, scaledDimens.get(0), scaledDimens.get(1))));
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
					} catch (IOException evt){
						evt.printStackTrace();
					}
				}
			}
		});

<<<<<<< HEAD
		add(loadImage);
		add(labelImage);
		add(imageLabel);
	}


=======
		add(loadImageBtn);
		add(imgTitleLabel);
		add(emptyLabel);
		
	}

	
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
	public String getPath(){
		return path;
	}

}


class ImageTransformPanel extends JPanel{

<<<<<<< HEAD
	private String imagePath = " ";
	private JButton loadImage;
	private JRadioButton smoothBox, contrastBox, histogramBox, kirschBox, finalBox, laplacianBox;
	private ButtonGroup actions;
	private BitImage bitimage = new BitImage();
	private BufferedImage img;
	private JButton applyButton;

	private int order = 3;
	private JLabel kernelLabel;
	private JSpinner matrixOrder;
	private JPanel extras;

	private KernelGrid kernelVals_GUI;

	public ImageTransformPanel(String titled, LoadImagePanel loadPanel){
=======
	private JButton loadImageBtn, applyOperationBtn;
	private JRadioButton smoothRdioBtn, contrastRdioBtn, histogramRdioBtn, kirschRdioBtn, lapicianRdioBtn;
	private ButtonGroup imgProcessOperations;
	private JLabel kernelLabel;
	private JSpinner matOrderJSpinner;		// Input for order
	private JPanel extrasPanel;

	private String imagePath = " ";
	private BufferedImage img;
	private int order = 3;				// Since kernel is a order by order matrix
	
	private BitImage bitImage = new BitImage();
	private KernelGrid kernelVals_GUI;

	public ImageTransformPanel(String titled, LoadInitialImagePanel loadPanel){
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new TitledBorder(new EtchedBorder(), titled));

<<<<<<< HEAD
		loadImage = new JButton("Load Image to be Processed");

		smoothBox = new JRadioButton("Apply smoothing");
		histogramBox = new JRadioButton("Apply histogram equalization");
		kirschBox = new JRadioButton("Apply kirsch edge detection");
		laplacianBox = new JRadioButton("Apply Laplacian edge detection");
		contrastBox = new JRadioButton("Apply constrast");
		applyButton = new JButton("Apply operation to image");

		actions = new ButtonGroup ();

		actions.add(smoothBox);
		actions.add(histogramBox);
		actions.add(laplacianBox);
		actions.add(kirschBox);
		actions.add(contrastBox);

		smoothBox.setEnabled(false);
		histogramBox.setEnabled(false);
		laplacianBox.setEnabled(false);
		kirschBox.setEnabled(false);
		contrastBox.setEnabled(false);


		loadImage.addActionListener(new ActionListener(){
=======

		loadImageBtn = new JButton("Load Image to be Processed");
		smoothRdioBtn = new JRadioButton("Apply smoothing");
		histogramRdioBtn = new JRadioButton("Apply histogram equalization");
		kirschRdioBtn = new JRadioButton("Apply kirsch edge detection");
		lapicianRdioBtn = new JRadioButton("Apply lapician edge detection");
		contrastRdioBtn = new JRadioButton("Apply constrast");
		applyOperationBtn = new JButton("Apply operation to image");

		extrasPanel = new JPanel();			// holds all kernel related stuff
		JPanel inputToKernelPanel = new JPanel();
		kernelLabel = new JLabel("Enter n for an n by n kernel");


		imgProcessOperations = new ButtonGroup ();
		imgProcessOperations.add(smoothRdioBtn);
		imgProcessOperations.add(histogramRdioBtn);
		imgProcessOperations.add(lapicianRdioBtn);
		imgProcessOperations.add(contrastRdioBtn);

		smoothRdioBtn.setEnabled(false);
		histogramRdioBtn.setEnabled(false);
		kirschRdioBtn.setEnabled(false);
		lapicianRdioBtn.setEnabled(false);
		contrastRdioBtn.setEnabled(false);


		loadImageBtn.addActionListener(new ActionListener(){
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
			public void actionPerformed(ActionEvent et){
				int option = JOptionPane.showConfirmDialog(null, "Do you want to use a new image(Yes to use a new image, No to use the last image loaded)?",
								"Select Image for Processing", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION){
					JFileChooser fc = new JFileChooser();
					fc.setCurrentDirectory(new File(new File("").getAbsolutePath()));
					int result = fc.showOpenDialog(null);
					if (result == JFileChooser.APPROVE_OPTION){
						File file = fc.getSelectedFile();
						setImagePath(file.getAbsolutePath());
					}
					// Loading the image from the LoadPanel
<<<<<<< HEAD
				} else if (option == JOptionPane.NO_OPTION && " ".equals(imagePath)){
=======
				} else if (option == JOptionPane.NO_OPTION && " ".equals(imagePath)){ 
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
					setImagePath(loadPanel.getPath());
					System.out.println(imagePath);
				}

<<<<<<< HEAD
				smoothBox.setEnabled(true);
				histogramBox.setEnabled(true);
				laplacianBox.setEnabled(true);
				kirschBox.setEnabled(true);
				contrastBox.setEnabled(true);
=======
				smoothRdioBtn.setEnabled(true);
				histogramRdioBtn.setEnabled(true);
				kirschRdioBtn.setEnabled(true);
				lapicianRdioBtn.setEnabled(true);
				contrastRdioBtn.setEnabled(true);
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
			}
		});

		// Will remove hardcoded values
<<<<<<< HEAD
		ActionListener act = new ActionListener(){
			@Override
   			public void actionPerformed(ActionEvent e) {
            	 if(e.getSource() == smoothBox){
            	 	try{
        				img = bitimage.applySmoothing((imagePath), 4, 4);
        			} catch(IOException ie){
        				ie.printStackTrace();
        			}
            	 }else if(e.getSource() == histogramBox){
            	 	try{
            	 		img = bitimage.applyHistogramEqualization(imagePath);
        			} catch(IOException ie){
        				ie.printStackTrace();
   					}
            	 } else if(e.getSource() == contrastBox){
            	 	try{
            	 		img = bitimage.applyContrast((imagePath), 4, 4);
        			} catch(IOException ie){
        				ie.printStackTrace();
   					}
            	 } else if(e.getSource() == kirschBox){
            	 	try{
            	 		img = bitimage.applyKirschEdgeDetection(imagePath);
        			} catch(IOException ie){
        				ie.printStackTrace();
   					}
					 	 } else if(e.getSource() == laplacianBox){
		             	 	try{
		             	 		img = bitimage.applyLapEdgeDetection(imagePath);
		         			} catch(IOException ie){
		         				ie.printStackTrace();
		    					}
		             	 }
            }
        };

		smoothBox.addActionListener(act);
		histogramBox.addActionListener(act);


		applyButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent at){
				new OutputImageModal(img, ImageTransformPanel.this);
			}
		});


		extras = new JPanel();
		extras.setLayout(new BoxLayout(extras, BoxLayout.PAGE_AXIS));
		extras.setBorder(new TitledBorder(new EtchedBorder(), "Extra configs for option selected"));

		kernelLabel = new JLabel("Enter n for an n by n kernel");


		matrixOrder = new JSpinner (new SpinnerNumberModel(
=======
		ActionListener actListner = new ActionListener(){
			@Override
   			public void actionPerformed(ActionEvent e) {
            	 if(e.getSource() == smoothRdioBtn){
            	 	try{
            	 		String options = JOptionPane.showInputDialog(null, "Enter your frameX and frameY values respectively.Example :4,5");
            	 		if (options != null){
	            	 		String[] values = options.split(",");
	            	 		int frameX = Integer.parseInt(values[0]);
	            	 		int frameY = Integer.parseInt(values[1]);
	        				img = bitImage.applySmoothing((imagePath), frameX, frameY);
	        			}
        			} catch(IOException ie){
        				ie.printStackTrace();
        			}
            	 }else if(e.getSource() == histogramRdioBtn){
            	 	try{
            	 		img = bitImage.applyHistogramEqualization(imagePath);
        			} catch(IOException ie){
        				ie.printStackTrace();
   					}
            	 } else if(e.getSource() == contrastRdioBtn){
            	 	try{
            	 		String options = JOptionPane.showInputDialog(null, "Enter your contrast and brightness values respectively.Example :4,5");
            	 		if (options != null){
	            	 		String[] values = options.split(",");
	            	 		int contrast = Integer.parseInt(values[0]);
	            	 		int brightness = Integer.parseInt(values[1]);
            	 			img = bitImage.applyContrast((imagePath), contrast, brightness);
            	 		}
        			} catch(IOException ie){
        				ie.printStackTrace();
   					}
   				 } else if(e.getSource() == kirschRdioBtn){
            	 	try{
            	 		img = bitImage.applyKirschEdgeDetection(imagePath);
        			} catch(IOException ie){
        				ie.printStackTrace();
   					}
            	 } else if(e.getSource() == lapicianRdioBtn){
            	 	try{
            	 		img = bitImage.applyLapicianEdgeDetection(imagePath);
        			} catch(IOException ie){
        				ie.printStackTrace();
   					}
            	 }
            }
        };

		smoothRdioBtn.addActionListener(actListner);
		histogramRdioBtn.addActionListener(actListner);
		kirschRdioBtn.addActionListener(actListner);
		lapicianRdioBtn.addActionListener(actListner);
		contrastRdioBtn.addActionListener(actListner);

		
		extrasPanel.setLayout(new BoxLayout(extrasPanel, BoxLayout.PAGE_AXIS));
		extrasPanel.setBorder(new TitledBorder(new EtchedBorder(), "Extra configs for option selected"));

		
		matOrderJSpinner = new JSpinner (new SpinnerNumberModel(
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
			3,			// initial value
			3,			// minimum value
			12,			// maximum value
			1			// step
		));

		kernelVals_GUI = new KernelGrid(order);

<<<<<<< HEAD
		matrixOrder.addChangeListener(new ChangeListener(){
=======
		matOrderJSpinner.addChangeListener(new ChangeListener(){
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
			public void stateChanged(ChangeEvent e) {
				JSpinner gf = (JSpinner)e.getSource();
				order = (Integer)gf.getValue();
				updateKernel(order);
<<<<<<< HEAD
			}
		});


		JPanel inputToKernelPanel = new JPanel();
		inputToKernelPanel.setLayout(new BoxLayout(inputToKernelPanel, BoxLayout.LINE_AXIS));
		inputToKernelPanel.add(kernelLabel);
		inputToKernelPanel.add(matrixOrder);

		extras.add(inputToKernelPanel);
		extras.add(kernelVals_GUI);

		add(loadImage);
		add(smoothBox);
		add(histogramBox);
		add(laplacianBox);
		add(kirschBox);
		add(contrastBox);
		add(extras);
		add(applyButton);
=======
			}	
		});

		applyOperationBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent at){	
				new OutputImageModal(img, ImageTransformPanel.this);
			}
		});

		
		inputToKernelPanel.setLayout(new BoxLayout(inputToKernelPanel, BoxLayout.LINE_AXIS));
		inputToKernelPanel.add(kernelLabel);
		inputToKernelPanel.add(matOrderJSpinner);

		extrasPanel.add(inputToKernelPanel);
		extrasPanel.add(kernelVals_GUI);

		add(loadImageBtn);
		add(smoothRdioBtn);
		add(histogramRdioBtn);
		add(lapicianRdioBtn);
		add(kirschRdioBtn);
		add(contrastRdioBtn);
		add(extrasPanel);
		add(applyOperationBtn);
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4

	}

	public void setImagePath(String imagePath){
		this.imagePath = imagePath;
	}

	public void updateKernel(int order){
<<<<<<< HEAD
		extras.remove(kernelVals_GUI);
		extras.revalidate();
		extras.repaint();
		kernelVals_GUI = new KernelGrid(order);
		extras.add(kernelVals_GUI);
		extras.revalidate();
		extras.repaint();
=======
		extrasPanel.remove(kernelVals_GUI);
		extrasPanel.revalidate();
		extrasPanel.repaint();
		kernelVals_GUI = new KernelGrid(order);
		extrasPanel.add(kernelVals_GUI);
		extrasPanel.revalidate();
		extrasPanel.repaint();
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
	}

	public JPanel getKernel(){
		return kernelVals_GUI;
	}
}


class KernelGrid extends JPanel{

	private final ArrayList<JButton> list = new ArrayList<JButton>();
	private int [][] data;
	private int order;

    private JButton getGridButton(final int row, final int column) {
        int index = row * order + column;
        return list.get(index);
    }

	private JButton createGridButton(final int row, final int col, int value) {
        JButton b = new JButton(String.valueOf(value));
        b.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	JButton gb = KernelGrid.this.getGridButton(row, col);
                String val = JOptionPane.showInputDialog(null, "Enter your value for this cell");
                if (val != null){
                	gb.setText(val);
                	data[row][col] = Integer.parseInt(val);
                }
                printKernelCells();
            }
        });
        return b;
    }

    private void resetData(int [][] data){
    	for (int i =0; i < data.length; i++){
    		for (int j = 0; j < data[0].length; j++){
    			data[i][j] = 0;
    		}
    	}
    }

    public KernelGrid(int order){
    	this.order = order;
    	data = new int[order][order];
    	resetData(data);
    	setLayout(new GridLayout(order, order));
        for (int i = 0; i < order * order; i++) {
            int row = i / order;
            int col = i % order;
            JButton gb = createGridButton(row, col, 0);
            list.add(gb);
            add(gb);
        }
	}

	public int [][] getValueData(){
		return data;
	}

	public void printKernelCells(){
		StringBuilder sb = new StringBuilder();
		for (int i =0; i < data.length; i++){
    		for (int j = 0; j < data[0].length; j++){
    			sb.append(data[i][j] + " ");
    		}
    		sb.append("\n");
    	}
    	System.out.println(sb.toString());
	}

}


class OutputImageModal extends JDialog{

	private JLabel outImgLabel;
<<<<<<< HEAD
	private JButton saveButton, noSaveButton;
=======
	private JButton saveBtn, noSaveBtn;
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
	private ImageUtils imageUtils = new ImageUtils();
	private JPanel imagePanel, commandPanel;

	public OutputImageModal(BufferedImage img, ImageTransformPanel transformPanel){

		imagePanel = new JPanel(new BorderLayout());
		commandPanel = new JPanel(new FlowLayout());
<<<<<<< HEAD
=======
		saveBtn = new JButton("Save");
		noSaveBtn = new JButton("Don't Save");
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4

		outImgLabel = new JLabel();
		outImgLabel.setBorder(new TitledBorder(new EtchedBorder(), "Output Image"));

		ArrayList<Integer> scaledDimens = imageUtils.getScaledDimensions(img, new Dimension(500, 500));
		outImgLabel.setIcon(new ImageIcon(imageUtils.resize(img, scaledDimens.get(0), scaledDimens.get(1))));

<<<<<<< HEAD
		saveButton = new JButton("Save");
		noSaveButton = new JButton("Don't Save");

		ActionListener lst = new ActionListener(){
=======
		
		ActionListener saveListner = new ActionListener(){
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
			public void actionPerformed(ActionEvent avt){
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");
				fileChooser.setCurrentDirectory(new File(new File("").getAbsolutePath(), "output"));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Bitmap Images", "bmp");
    			fileChooser.setFileFilter(filter);
				int userSelection = fileChooser.showSaveDialog(null);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
 				    File file = fileChooser.getSelectedFile();
 				    transformPanel.setImagePath(file.getAbsolutePath());
 				    try{
     					ImageIO.write(img, "bmp", file);
     				} catch (IOException | RuntimeException ex){
     					ex.printStackTrace();
     				}
				}
				dispose();
			}
		};
<<<<<<< HEAD
		saveButton.addActionListener(lst);
		getRootPane().setDefaultButton(saveButton);
		getRootPane().registerKeyboardAction(lst,
			KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW);


		ActionListener ast = new ActionListener(){
=======

		saveBtn.addActionListener(saveListner);
		getRootPane().setDefaultButton(saveBtn);
		getRootPane().registerKeyboardAction(saveListner,
			KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW);

		
		ActionListener exitListner = new ActionListener(){
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
			public void actionPerformed(ActionEvent et){
				dispose();
			}
		};
<<<<<<< HEAD
		noSaveButton.addActionListener(ast);
		getRootPane().registerKeyboardAction(ast,
=======
		noSaveBtn.addActionListener(exitListner );
		getRootPane().registerKeyboardAction(exitListner ,
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
			KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW);

		imagePanel.add(outImgLabel, BorderLayout.PAGE_START);

<<<<<<< HEAD
		commandPanel.add(saveButton);
		commandPanel.add(noSaveButton);
=======
		commandPanel.add(saveBtn);
		commandPanel.add(noSaveBtn);
>>>>>>> 86e487e9d78b8521685f9da767d50758214a7ec4
		imagePanel.add(commandPanel, BorderLayout.PAGE_END);

		add(imagePanel);
		pack();
		setVisible(true);
	}

}
