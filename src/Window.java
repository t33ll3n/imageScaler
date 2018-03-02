//import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
//import java.io.File;
import java.awt.event.ActionEvent;
//import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

//import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.Font;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;

import javax.swing.JTextField;
import javax.swing.UIManager;

import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JProgressBar;

public class Window implements ActionListener {

	private JFrame frame;
	private JTextField width;
	private JTextField height;
	public JTextField name;
	public JLabel seleced;
	private JLabel lblError;
	private Logic logic;
	public JCheckBox chckbxSaveInFolders;
	public JCheckBox chckbxDeleteOriginals;
	private JProgressBar scalingProgressBar;


	public Window(Logic logic) {
		this.logic = logic;
		initialize();
		setLookAndFeel();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame("Image Scaler");
		frame.setBounds(100, 100, 280, 376);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Drop drop = new Drop(logic, this);
		new DropTarget(frame, DnDConstants.ACTION_COPY, drop, true);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.setBounds(165, 303, 89, 23);
		btnQuit.addActionListener(this);
		frame.getContentPane().add(btnQuit);
		
		JButton select = new JButton("Select Images");
		select.setFont(new Font("Tahoma", Font.PLAIN, 15));
		select.setBounds(13, 11, 137, 39);
		select.addActionListener(this);
		frame.getContentPane().add(select);
		
		JButton btnScale = new JButton("Start scaling");
		btnScale.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnScale.addActionListener(this);
		btnScale.setBounds(13, 287, 120, 39);
		frame.getContentPane().add(btnScale);
		
		JLabel lblWidth = new JLabel("Width: ");
		lblWidth.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblWidth.setBounds(13, 99, 47, 23);
		frame.getContentPane().add(lblWidth);
		
		JLabel lblHeight = new JLabel("Height:");
		lblHeight.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblHeight.setBounds(13, 130, 54, 23);
		frame.getContentPane().add(lblHeight);
		
		width = new JTextField();
		width.setText("800");
		width.setFont(new Font("Tahoma", Font.PLAIN, 15));
		width.setBounds(74, 98, 86, 25);
		frame.getContentPane().add(width);
		width.setColumns(10);
		
		height = new JTextField();
		height.setText("600");
		height.setFont(new Font("Tahoma", Font.PLAIN, 15));
		height.setBounds(74, 129, 86, 25);
		frame.getContentPane().add(height);
		height.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Name:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(13, 164, 46, 14);
		frame.getContentPane().add(lblNewLabel);
		
		name = new JTextField();
		name.setFont(new Font("Tahoma", Font.PLAIN, 15));
		name.setBounds(74, 160, 152, 25);
		frame.getContentPane().add(name);
		name.setColumns(10);
		
		seleced = new JLabel("0 images selected");
		seleced.setFont(new Font("Tahoma", Font.PLAIN, 15));
		seleced.setBounds(23, 61, 209, 23);
		frame.getContentPane().add(seleced);
		
		JButton btnNewButton = new JButton("X");
		btnNewButton.setForeground(Color.RED);
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 22));
		btnNewButton.setBounds(153, 11, 47, 39);
		btnNewButton.addActionListener(this);
		frame.getContentPane().add(btnNewButton);
		
		lblError = new JLabel("");
		lblError.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblError.setBounds(13, 246, 241, 30);
		frame.getContentPane().add(lblError);
		
		chckbxSaveInFolders = new JCheckBox("Save in folders");
		chckbxSaveInFolders.setBounds(13, 192, 147, 23);
		frame.getContentPane().add(chckbxSaveInFolders);
		
		chckbxDeleteOriginals = new JCheckBox("Delete originals");
		chckbxDeleteOriginals.setBounds(13, 220, 147, 23);
		frame.getContentPane().add(chckbxDeleteOriginals);
		
		scalingProgressBar = new JProgressBar(0, 100);
		scalingProgressBar.setBounds(13, 60, 137, 27);
		scalingProgressBar.setVisible(false);
		scalingProgressBar.setStringPainted(true);
		frame.getContentPane().add(scalingProgressBar);
		
	}
	
	private void setLookAndFeel(){
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			//ignore error. Default Java LookAndFeel
		}
	}
	
	public int showWarning(String warning){
		return JOptionPane.showConfirmDialog(frame, warning, "Warning", JOptionPane.YES_NO_OPTION);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		
		if (btn.getText().equals("Quit")){
			System.exit(0);
		} 
		else if (btn.getText().equals("X")) {
			logic.clearImageArray();
			scalingProgressBar.setVisible(false);
			seleced.setText(logic.imageArray.length + " images seleced!");
		}
		else if (btn.getText().equals("Select Images")){
			seleced.setText(logic.getPhotos() + " images selected");
			scalingProgressBar.setVisible(false);
			seleced.setVisible(true);
		} 
		else if (btn.getText().equals("Start scaling")){
			seleced.setVisible(false);
			scalingProgressBar.setVisible(true);
			
			seleced.setText("");
			if (logic.imageArray.length != 0){
				if (!name.getText().trim().equals("")){
					if (!height.getText().equals("")&&!width.getText().equals("")){
						try {
							logic.setDim2(Integer.parseInt(height.getText()));
							logic.setDim1(Integer.parseInt(width.getText()));
							logic.setName(name.getText());
							
							seleced.setText(logic.scaleImages());
							lblError.setText("");
						} catch (Exception ex) {
							System.out.println(ex.getMessage());
							lblError.setText("Invalid dimentions!");
						}
					} else {
						lblError.setText("Invalid dimentions!");
						//System.err.println("Invalid dimentions!");
					}				
				} else {
					lblError.setText("Enter image name");
					//System.err.println("Enter image name!");
				}
			} else {
				lblError.setText("Please select images first");
				//System.out.println("Please select images first!");	
			}
			
		}
		//scalingProgressBar.setVisible(false);
		//seleced.setVisible(true);
	}
	
	public void setScalingProgressBar(int value){
		scalingProgressBar.setValue(value);
		scalingProgressBar.repaint(); 	
	}
	
	public void incrementScalingProgressBar(int value){
		System.out.println("Update progress");
		int currValue = scalingProgressBar.getValue();
		int newValue = currValue + value;
		if (newValue > 100){
			newValue = 100;
		}
		scalingProgressBar.setValue(newValue);
		System.out.println("new value" + newValue);
		scalingProgressBar.repaint();
	}
}
