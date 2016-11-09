/*
*	File: ImageProcessGUI.java
*
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

public class ImageProcessGUI extends JFrame{

	private LoadImagePanel loadPanel;
	private ImageTransformPanel transformPanel;
	public String originalLoadedImageName = "";

	public ImageProcessGUI(){
		super("Image Processing Application");
		getContentPane().setLayout(new BorderLayout());

		loadPanel = new LoadImagePanel("Original Image");
		transformPanel = new ImageTransformPanel("Steps to generate output images", loadPanel);

		add(loadPanel, BorderLayout.CENTER);
		add(transformPanel, BorderLayout.LINE_END);

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
	}
}

class LoadImagePanel extends JPanel{

	private JLabel labelImage;
	private JLabel imageLabel;
	private JButton loadImage;
	private ImageUtils imageUtils = new ImageUtils();
	private String path = " ";

	public LoadImagePanel(String title){
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new TitledBorder(new EtchedBorder(), title));

		labelImage = new JLabel("Image:");
		imageLabel = new JLabel();
		loadImage = new JButton("Load Image to be Compared Against");
		loadImage.addActionListener(new ActionListener(){
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
						imageLabel.setIcon(new ImageIcon(imageUtils.resize(img, scaledDimens.get(0), scaledDimens.get(1))));
					} catch (IOException evt){
						evt.printStackTrace();
					}
				}
			}
		});

		add(loadImage);
		add(labelImage);
		add(imageLabel);
	}


	public String getPath(){
		return path;
	}

}


class ImageTransformPanel extends JPanel{

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
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new TitledBorder(new EtchedBorder(), titled));

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
				} else if (option == JOptionPane.NO_OPTION && " ".equals(imagePath)){
					setImagePath(loadPanel.getPath());
					System.out.println(imagePath);
				}

				smoothBox.setEnabled(true);
				histogramBox.setEnabled(true);
				laplacianBox.setEnabled(true);
				kirschBox.setEnabled(true);
				contrastBox.setEnabled(true);
			}
		});

		// Will remove hardcoded values
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
					}  	 } else if(e.getSource() == laplacianBox){
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
			3,			// initial value
			3,			// minimum value
			12,			// maximum value
			1			// step
		));

		kernelVals_GUI = new KernelGrid(order);

		matrixOrder.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				JSpinner gf = (JSpinner)e.getSource();
				order = (Integer)gf.getValue();
				updateKernel(order);
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

	}

	public void setImagePath(String imagePath){
		this.imagePath = imagePath;
	}

	public void updateKernel(int order){
		extras.remove(kernelVals_GUI);
		extras.revalidate();
		extras.repaint();
		kernelVals_GUI = new KernelGrid(order);
		extras.add(kernelVals_GUI);
		extras.revalidate();
		extras.repaint();
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
	private JButton saveButton, noSaveButton;
	private ImageUtils imageUtils = new ImageUtils();
	private JPanel imagePanel, commandPanel;

	public OutputImageModal(BufferedImage img, ImageTransformPanel transformPanel){

		imagePanel = new JPanel(new BorderLayout());
		commandPanel = new JPanel(new FlowLayout());

		outImgLabel = new JLabel();
		outImgLabel.setBorder(new TitledBorder(new EtchedBorder(), "Output Image"));

		ArrayList<Integer> scaledDimens = imageUtils.getScaledDimensions(img, new Dimension(500, 500));
		outImgLabel.setIcon(new ImageIcon(imageUtils.resize(img, scaledDimens.get(0), scaledDimens.get(1))));

		saveButton = new JButton("Save");
		noSaveButton = new JButton("Don't Save");

		ActionListener lst = new ActionListener(){
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
		saveButton.addActionListener(lst);
		getRootPane().setDefaultButton(saveButton);
		getRootPane().registerKeyboardAction(lst,
			KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW);


		ActionListener ast = new ActionListener(){
			public void actionPerformed(ActionEvent et){
				dispose();
			}
		};
		noSaveButton.addActionListener(ast);
		getRootPane().registerKeyboardAction(ast,
			KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW);

		imagePanel.add(outImgLabel, BorderLayout.PAGE_START);

		commandPanel.add(saveButton);
		commandPanel.add(noSaveButton);
		imagePanel.add(commandPanel, BorderLayout.PAGE_END);

		add(imagePanel);
		pack();
		setVisible(true);
	}

}
