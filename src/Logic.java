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

	public Logic() {
		window = new Window(this);
	}

	public File[] imageArray = new File[0];// array of images
	Scanner sc = new Scanner(System.in);
	private int dim1; // dimention 1
	private int dim2; // dimention 2
	boolean inFolder = false;
	boolean delete = false;
	private File dat;
	private String name; // name form scalled images
	private Scaler scaler;

	public int dropPhotos(List<File> file) {
		imageArray = file.toArray(new File[file.size()]);
		Arrays.sort(imageArray);
		LCS(imageArray);
		return imageArray.length;
	}

	public int getPhotos() {
		// Use filechooser to get images
		JButton open = new JButton();
		JFileChooser fc = new JFileChooser(); // initialize file chooser
		fc.setMultiSelectionEnabled(true); // enable multi file selection
		if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {

		}

		imageArray = fc.getSelectedFiles(); // get selected images and store
											// them into an array
		Arrays.sort(imageArray);
		LCS(imageArray);
		// window.setScalingProgressBar(0);
		return imageArray.length;

	}

	public String scaleImages() {
		if (window.chckbxDeleteOriginals.isSelected()) {
			int reply = window.showWarning("Original images will be deleted. Do you want to proceed?");
			if (reply == JOptionPane.NO_OPTION) {
				return imageArray.length + " images selected";
			}
		}

		JButton open = new JButton();
		JFileChooser fc = new JFileChooser(); // initialize file chooser
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {

		}
		dat = fc.getSelectedFile(); // path where images will be saved
		if (dat != null) {
			dat = new File(dat + "\\scaled_images"); // in folder
														// "scaled_images"
			dat.mkdir();

			int progressStep = (int) Math.ceil((100 / imageArray.length));
			// System.out.println(progressStep);
			window.setScalingProgressBar(0);

			// get checkbox save in folder
			if (window.chckbxSaveInFolders.isSelected()) {
				inFolder = true;
			}

			if (window.chckbxDeleteOriginals.isSelected()) {
				delete = true;
			}

			scaler = new Scaler(imageArray, this);

			// add listener to update progress bar
			scaler.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					// TODO Auto-generated method stub
					switch (evt.getPropertyName()) {

					// propertyName progress tells us progress value
					case "progress":
						window.setScalingProgressBar((Integer) evt.getNewValue());
					}

				}
			});
			scaler.execute();

			return "Images scaled";
		}
		return "";
	}

	public void clearImageArray() {
		imageArray = new File[0];
	}

	private void LCS(File imageArray[]) {
		if (imageArray.length == 0) {
			return;
		} else if (imageArray.length == 1) {
			// remove path and file type
			String imageName = imageArray[0].getName().toString().split("\\.")[0];
			window.name.setText(imageName);
			return;
		}
		String name = LongestCommonString(imageArray[0].getName(), imageArray[1].getName());
		if (imageArray.length > 2) {
			for (int i = 2; i < imageArray.length; i++) {
				name = LongestCommonString(name, imageArray[i].getName());
				// System.out.println("name:" + name);
			}
		}
		name = name.replace(" ", "_").toLowerCase();
		window.name.setText(name);
	}

	public static String LongestCommonString(String prvi, String drugi) {
		int[][] polje = new int[prvi.length()][drugi.length()];
		int longest = 0;
		String niz = "";

		for (int i = 0; i < prvi.length(); i++) {
			for (int j = 0; j < drugi.length(); j++) {
				if (prvi.charAt(i) == drugi.charAt(j)) {
					if (i == 0 || j == 0) {
						polje[i][j] = 1;
					} else {
						polje[i][j] = polje[i - 1][j - 1] + 1;
					}

					if (polje[i][j] > longest) {
						longest = polje[i][j];
						niz = prvi.substring(i - longest + 1, i + 1);
					}
				}
			}
		}

		return niz;
	}

	public int getDim1() {
		return dim1;
	}

	public int getDim2() {
		return dim2;
	}

	public boolean getInFolder() {
		return inFolder;
	}

	public boolean getDelete() {
		return delete;
	}

	public File getDat() {
		return dat;
	}

	public String getName() {
		return name;
	}

	public void setDim1(int value) {
		this.dim1 = value;
	}

	public void setDim2(int value) {
		this.dim2 = value;
	}

	public void setName(String name) {
		this.name = name;
	}

}
