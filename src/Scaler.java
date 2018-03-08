import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.swing.SwingWorker;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Scaler extends SwingWorker<Integer, String> {

	private File[] imageArray;
	private Logic logic;
	private int dim1;
	private int dim2;
	private File dat;
	private String name;
	
	public Scaler(File[] imageArray, Logic logic) {
		this.imageArray = imageArray;
		this.logic = logic;
		this.dim1 = logic.getDim1();
		this.dim2 = logic.getDim2();
		this.dat = logic.getDat();
		this.name = logic.getName();
	}

	@Override
	protected Integer doInBackground() throws Exception {

		BufferedImage img;
		
		
		for (int i = 0; i < imageArray.length; i++) {
			
			img = loadImage(imageArray[i]);
			
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

			BufferedImage resizedImg = new BufferedImage(finalw, finalh, img.getType()); //type has been changed
			Graphics2D g2 = resizedImg.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(img, 0, 0, finalw, finalh, null);
			g2.dispose();

			
			//saveIMage(resizedImg)
			saveImage(resizedImg, name, i, dat);
			
			
			
			//set progress
			setProgress((i + 1) * 100 / imageArray.length);
			//System.out.println((i + 1) * 100 / imageArray.length);
		}
		
		if (logic.getDelete()){
			deleteOriginals();
		}

		return 0;
	}
	
	private BufferedImage loadImage(File dat) { // argument dat contains path to image
		BufferedImage img = null;
		
		ExifData(dat);

		try {			
			img = ImageIO.read(dat); // Loads image to memory
		} catch (IOException e) {
			// error code
			System.out.println("Problem reading an image!");
			System.out.println(e.getMessage());
		}

		return img; // vrne sliko v main metodo
	}
	
	private void saveImage(BufferedImage img, String imeSlike, int counter, File dat) {
		counter += 1;
		if (logic.getInFolder()){
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
			System.out.println("Issues saving image!");
			System.out.println(e.getMessage());
		}
	}
	
	private void deleteOriginals(){
		for (int j = 0; j < imageArray.length; j++) {
			imageArray[j].delete();
		}
	}
	
	public void ExifData(File dat) {
		
		//Does not provide all EXIF data
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(dat);
			Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

			if (readers.hasNext()) {
				ImageReader reader = readers.next();

				reader.setInput(iis, true);

				IIOMetadata metadata = reader.getImageMetadata(0);
				String[] names = metadata.getMetadataFormatNames();

				for (int i = 0; i < names.length; i++) {
					System.out.println(names[i]);
					displayMetaData(metadata.getAsTree(names[i]));
				}
			}

		} catch (IOException e) {
			// error code
			System.out.println(e.getMessage());
		}
	}
	
	private void displayMetaData(Node node){
		displayMetaData(node, 0);
	}
	
	private void displayMetaData(Node node, int level){
		System.out.println(node.getNodeName());
		NamedNodeMap map = node.getAttributes();
		if (map != null){
			//print attributes
			for (int i = 0; i < map.getLength(); i++) {
				Node attr = map.item(i);
				System.out.println(attr.getNodeName() + ": " + attr.getNodeValue());
			}
		} else {
			System.out.println("map is null");
		}
		
		
		Node child = node.getFirstChild();
		
		//no children
		if (child == null){
			return;
		}
		
		while(child != null){
			//display children
			displayMetaData(child, level + 1);
			child = child.getNextSibling();
		}
	}

}
