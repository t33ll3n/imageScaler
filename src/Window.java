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
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JCheckBox;

public class Window implements ActionListener {

	private JFrame frame;
	private JTextField width;
	private JTextField height;
	private JTextField name;
	private JLabel seleced;
	private JLabel lblError;
	private Scaler scaler;
	public JCheckBox chckbxSaveInFolders;
	public JCheckBox chckbxDeleteOriginals;


	public Window(Scaler scaler) {
		this.scaler = scaler;
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 280, 376);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
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
		seleced.setBounds(18, 50, 209, 23);
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
		
	}
	
	public int showWarning(String warning){
		return JOptionPane.showConfirmDialog(frame, warning, "Warning", JOptionPane.YES_NO_OPTION);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		
		if (btn.getText().equals("Quit")){
			System.exit(0);
		} else if (btn.getText().equals("X")) {
			scaler.clearImageArray();
			seleced.setText(scaler.imageArray.length + " images seleced!");
		}
		else if (btn.getText().equals("Select Images")){
			seleced.setText(scaler.getPhotos() + " images selected");
		} else if (btn.getText().equals("Start scaling")){
			seleced.setText(" ");
			if (scaler.imageArray.length != 0){
				if (!name.getText().equals("")){
					if (!height.getText().equals("")&&!width.getText().equals("")){
						try {
							scaler.dim2 = Integer.parseInt(height.getText());
							scaler.dim1 = Integer.parseInt(width.getText());
							scaler.name = name.getText();
							
							seleced.setText(scaler.scaleImages());
							lblError.setText("");
						} catch (Exception ex) {
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
	}
}
