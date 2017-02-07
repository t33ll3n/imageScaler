import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.channels.ShutdownChannelGroupException;
import java.nio.file.Files;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Scaler {
	Window window;
	
	public Scaler(){
		//Window window = new Window(this);
		this.window = new Window(this);
	}
	
	String name; //name form scalled images
	public File[] imageArray = new File[0];//array of images
	Scanner sc = new Scanner(System.in);
	int dim1; //dimention 1
	int dim2; //dimention 2

	public int getPhotos() {
		// Use filechooser to get images
		JButton open = new JButton();
		JFileChooser fc = new JFileChooser(); //initialize file chooser
		fc.setMultiSelectionEnabled(true); //enable multi file selection
		if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {

		}
		
		imageArray = fc.getSelectedFiles(); //get selected images and store them into an array
		
		return imageArray.length;

	}

	public void saveImage(BufferedImage img, String imeSlike, int counter, File dat) {
		counter += 1;
		if (window.chckbxSaveInFolders.isSelected()){
			dat = new File(dat + "\\" + imeSlike);
			dat.mkdir();
		}
		if (counter < 10){
			dat = new File(dat + "\\" + imeSlike + "0" + counter + ".JPG"); //creates path to image
		}
		else {
			dat = new File(dat + "\\" +  imeSlike + counter + ".JPG");
		}

		try { // writes image
			ImageIO.write(img, "jpg", dat); // saves image
		} catch (IOException e) {
			//error code
		}
	}

	public BufferedImage loadImage(File dat) { // argument dat contains path to image
		BufferedImage img = null;

		try {
			img = ImageIO.read(dat); // Loads image to memory
		} catch (IOException e) {
			// error code
		}

		return img; // vrne sliko v main metodo
	}

	public BufferedImage scaleThisImage(BufferedImage img, int type) {
		int finalw = img.getWidth();
		int finalh = img.getHeight();
		double factor = 1.0d;
		if (finalw > finalh) {
			factor = ((double) img.getHeight() / dim2);
			finalh = (int) (finalh / factor);
			finalw = (int) (finalw / factor);
		} else {
			factor = ((double) img.getWidth() / dim2);
			finalw = (int) (finalw / factor);
			finalh = (int) (finalh / factor);
		}

		BufferedImage resizedImg = new BufferedImage(finalw, finalh, type);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(img, 0, 0, finalw, finalh, null);
		g2.dispose();

		return resizedImg;
	}

	public String scaleImages() {
		if (window.chckbxDeleteOriginals.isSelected()){
			int reply = window.showWarning("Original images will be deleted. Do you want to proceed?");
			if (reply == JOptionPane.NO_OPTION){
				return imageArray.length + " images selected";
			}
		}
		
		JButton open = new JButton();
		JFileChooser fc = new JFileChooser(); //initialize file chooser
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
			
		}
		File dat = fc.getSelectedFile(); //path where images will be saved
		if (dat != null){
			dat = new File(dat + "\\scaled_images"); // in folder "scaled_images"
			dat.mkdir();
	
			for (int i = 0; i < imageArray.length; i++) {
				BufferedImage image = null;
				image = loadImage(imageArray[i]);
	
				// change image size
				if (image.getHeight() != dim1 || image.getWidth() != dim1) {
					if (image.getWidth() != dim2 || image.getHeight() != dim2) {
						image = scaleThisImage(image, image.getType()); //scale image
					}
				}
				
				saveImage(image, name, i, dat); // save image
			}
			if (window.chckbxDeleteOriginals.isSelected()){
				deleteOriginals();
			}
			return "Images scaled";
		}
		return " ";
	}
	
	public void clearImageArray(){
		imageArray = new File[0];
	}
	
	public void deleteOriginals(){
		for (int j = 0; j < imageArray.length; j++) {
			imageArray[j].delete();
		}
		
	}

}
