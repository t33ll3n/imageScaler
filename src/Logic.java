import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Logic {
	
	private Window window;
	
	public Logic(){
		window = new Window(this);
	}

	
	public File[] imageArray = new File[0];//array of images
	Scanner sc = new Scanner(System.in);
	private int dim1; //dimention 1
	private int dim2; //dimention 2
	boolean inFolder = false;
	boolean delete = false;
	private File dat;
	private String name; //name form scalled images
	private Scaler scaler;
	
	public int dropPhotos(List<File> file){
		imageArray = file.toArray(new File[file.size()]);
		Arrays.sort(imageArray);
		LCS(imageArray);
		return imageArray.length;
	}

	public int getPhotos() {
		// Use filechooser to get images
		JButton open = new JButton();
		JFileChooser fc = new JFileChooser(); //initialize file chooser
		fc.setMultiSelectionEnabled(true); //enable multi file selection
		if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {

		}
		
		imageArray = fc.getSelectedFiles(); //get selected images and store them into an array
		Arrays.sort(imageArray);
		LCS(imageArray);
		//window.setScalingProgressBar(0);
		return imageArray.length;

	}

//	public void saveImage(BufferedImage img, String imeSlike, int counter, File dat) {
//		counter += 1;
//		if (window.chckbxSaveInFolders.isSelected()){
//			dat = new File(dat + "\\" + imeSlike);
//			dat.mkdir();
//		}
//		if (counter < 10){
//			dat = new File(dat + "\\" + imeSlike + "0" + counter + ".JPG"); //creates path to image
//		}
//		else {
//			dat = new File(dat + "\\" +  imeSlike + counter + ".JPG");
//		}
//
//		try { // writes image
//			ImageIO.write(img, "jpg", dat); // saves image
//		} catch (IOException e) {
//			//error code
//			System.out.println("Issues saving image!");
//			System.out.println(e.getMessage());
//		}
//	}

//	public BufferedImage loadImage(File dat) { // argument dat contains path to image
//		BufferedImage img = null;
//		
//		ExifData(dat);
//
//		try {			
//			img = ImageIO.read(dat); // Loads image to memory
//		} catch (IOException e) {
//			// error code
//			System.out.println("Problem reading an image!");
//			System.out.println(e.getMessage());
//		}
//
//		return img; // vrne sliko v main metodo
//	}

//	public BufferedImage scaleThisImage(BufferedImage img, int type) {
//		int finalw = img.getWidth();
//		int finalh = img.getHeight();
//		double factor = 1.0d;
//		if (finalw > finalh) {
//			factor = ((double) img.getHeight() / dim2);
//			finalh = (int) (finalh / factor);
//			finalw = (int) (finalw / factor);
//		} else {
//			factor = ((double) img.getWidth() / dim2);
//			finalw = (int) (finalw / factor);
//			finalh = (int) (finalh / factor);
//		}
//
//		BufferedImage resizedImg = new BufferedImage(finalw, finalh, type);
//		Graphics2D g2 = resizedImg.createGraphics();
//		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//		g2.drawImage(img, 0, 0, finalw, finalh, null);
//		g2.dispose();
//
//		return resizedImg;
//	}

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
		dat = fc.getSelectedFile(); //path where images will be saved
		if (dat != null){
			dat = new File(dat + "\\scaled_images"); // in folder "scaled_images"
			dat.mkdir();
			
			int progressStep = (int) Math.ceil((100 / imageArray.length));
//			System.out.println(progressStep);
			window.setScalingProgressBar(0);
	
//			for (int i = 0; i < imageArray.length; i++) {
//				window.incrementScalingProgressBar(progressStep);
//				BufferedImage image = null;
//				image = loadImage(imageArray[i]);
				
				//get checkbox save in folder
				if (window.chckbxSaveInFolders.isSelected()){
					inFolder = true;
				}
				
				if (window.chckbxDeleteOriginals.isSelected()){
					delete = true;
				}
	
				// change image size
//				if (image.getHeight() != dim1 || image.getWidth() != dim1) {
//					if (image.getWidth() != dim2 || image.getHeight() != dim2) {
//						
//						//call the scale function to do work in background
//						//new Thred Scaler
						
				
						scaler = new Scaler(imageArray, this);
						
						//add listener to update progress bar
						scaler.addPropertyChangeListener(new PropertyChangeListener() {
							
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								// TODO Auto-generated method stub
								switch (evt.getPropertyName()){
								
								//propertyName progress tells us progress value
								case "progress": 
									window.setScalingProgressBar((Integer) evt.getNewValue());
								}
								
							}
						});
						scaler.execute();
//						image = scaleThisImage(image, image.getType()); //scale image
//					}
//				}
				
				//saveImage(image, name, i, dat); // save image
				//window.incrementScalingProgressBar(progressStep);
				//window.setScalingProgressBar(progressStep);
				
//			}
//			if (window.chckbxDeleteOriginals.isSelected()){
//				deleteOriginals();
//			}
			return "Images scaled";
		}
		return "";
	}
	
	public void clearImageArray(){
		imageArray = new File[0];
	}
	
//	public void deleteOriginals(){
//		for (int j = 0; j < imageArray.length; j++) {
//			imageArray[j].delete();
//		}
//		
//	}
	
	private void LCS(File imageArray[]){
		if (imageArray.length == 0){
			return;
		}
		else if (imageArray.length == 1){
			//remove path and file type
			String imageName = imageArray[0].getName().toString().split("\\.")[0];
			window.name.setText(imageName);
			return;
		}
		String name = LongestCommonString(imageArray[0].getName(), imageArray[1].getName());
		if (imageArray.length > 2){
			for (int i = 2; i < imageArray.length; i++){
				name = LongestCommonString(name, imageArray[i].getName());
				//System.out.println("name:" + name);
			}
		}
		name = name.replace(" ", "_").toLowerCase();
		window.name.setText(name);
	}
	
	public static String LongestCommonString(String prvi, String drugi){
		 int[][] polje = new int[prvi.length()][drugi.length()];
		 int longest = 0;
		 String niz = "";
		 
		 for (int i = 0; i < prvi.length(); i++) {
			for (int j = 0; j < drugi.length(); j++) {
				if (prvi.charAt(i) == drugi.charAt(j)){
					if (i == 0 || j == 0){
						polje[i][j] = 1;
					} else {
						polje[i][j] = polje[i-1][j-1] + 1;
					}
					
					if (polje[i][j] > longest){
						longest = polje[i][j];
						niz = prvi.substring(i-longest +1, i+1);
					}
				}
			}
		}
		 
		return niz;
	 }

	public void ExifData(File dat){
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(dat);	
			Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
			
			if (readers.hasNext()){
				ImageReader reader = readers.next();
				
				reader.setInput(iis, true);
			
				IIOMetadata metadata = reader.getImageMetadata(0);
				String[] names = metadata.getMetadataFormatNames();
				
				for (int i = 0; i < names.length; i++){
					System.out.println(names[i]);
				}
			}
			
		} catch (IOException e) {
			// error code
			System.out.println(e.getMessage());
		}
	}
	
	public int getDim1(){
		return dim1;
	}
	
	public int getDim2(){
		return dim2;
	}
	
	public boolean getInFolder(){
		return inFolder;
	}
	
	public boolean getDelete(){
		return delete;
	}
	
	public File getDat(){
		return dat;
	}
	
	public String getName(){
		return name;
	}
	
	public void setDim1(int value){
		this.dim1 = value;
	}
	
	public void setDim2(int value){
		this.dim2 = value;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
}
