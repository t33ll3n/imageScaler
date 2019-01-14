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

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
//import org.w3c.dom.NamedNodeMap;
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

			System.out.println("Scaling");

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

			BufferedImage resizedImg = new BufferedImage(finalw, finalh, img.getType()); // type
																							// has
																							// been
																							// changed
			Graphics2D g2 = resizedImg.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(img, 0, 0, finalw, finalh, null);
			g2.dispose();

			// saveIMage(resizedImg)
			saveImage(resizedImg, name, i, dat);

			// set progress
			setProgress((i + 1) * 100 / imageArray.length);
			// System.out.println((i + 1) * 100 / imageArray.length);
		}

		if (logic.getDelete()) {
			deleteOriginals();
		}

		return 0;
	}

	private BufferedImage loadImage(File dat) { // argument dat contains path to
												// image
		BufferedImage img = null;

		int orientation = -1;
		try {
			orientation = readExifData(dat);
		} catch (ImageReadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			img = ImageIO.read(dat); // Loads image to memory
		} catch (IOException e) {
			// error code
			System.out.println("Problem reading an image!");
			System.out.println(e.getMessage());
		}

		BufferedImage newImage = null;

		if (orientation != -1) {
			// rotate image

			switch (orientation) {
			case 6:
				// rotate right for 90 degress
				newImage = rotateRightFor90(img);
				break;
			case 8:
				// rotate left for 90 degress
				newImage = rotateLeftFor90(img);
				break;
			default:
				newImage = img;
			}

		} else {
			return img;
		}

		return newImage;
	}

	private void saveImage(BufferedImage img, String imeSlike, int counter, File dat) {
		counter += 1;
		if (logic.getInFolder()) {
			dat = new File(dat + "\\" + imeSlike);
			dat.mkdir();
		}
		if (counter < 10) {
			dat = new File(dat + "\\" + imeSlike + "0" + counter + ".JPG"); // creates
																			// path
																			// to
																			// image
		} else {
			dat = new File(dat + "\\" + imeSlike + counter + ".JPG");
		}

		try { // writes image
			ImageIO.write(img, "jpg", dat); // saves image
		} catch (IOException e) {
			// error code
			System.out.println("Issues saving image!");
			System.out.println(e.getMessage());
		}
	}

	private void deleteOriginals() {
		for (int j = 0; j < imageArray.length; j++) {
			imageArray[j].delete();
		}
	}

	private int readExifData(File file) throws ImageReadException, IOException {

		System.out.println("readEXIF");

		IImageMetadata metadata = Imaging.getMetadata(file);
		
		if (metadata == null) {
			return -1;
		}

		if (!(metadata instanceof JpegImageMetadata)) {
			throw new RuntimeException("Support only " + JpegImageMetadata.class.getSimpleName());
		}
		// Only jpeg images goes throw
		final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

		TiffField field = jpegMetadata.findEXIFValueWithExactMatch(TiffTagConstants.TIFF_TAG_ORIENTATION);
		if (field != null) {
			System.out.println("filed" + field.getValueDescription());
			return Integer.parseInt(field.getValueDescription());
		} else {
			System.out.println("Filed is null");
			return -1;
		}

	}

	private BufferedImage rotateRightFor90(BufferedImage img) {
		BufferedImage newImage = new BufferedImage(img.getHeight(), img.getWidth(), img.getType());

		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				newImage.setRGB(i, j, img.getRGB(j, i));
			}
		}

		return newImage;
	}

	private BufferedImage rotateLeftFor90(BufferedImage img) {
		BufferedImage newImage = new BufferedImage(img.getHeight(), img.getWidth(), img.getType());

		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				newImage.setRGB(i, (img.getWidth() - 1 - j), img.getRGB(j, i));
			}
		}

		return newImage;
	}

}
