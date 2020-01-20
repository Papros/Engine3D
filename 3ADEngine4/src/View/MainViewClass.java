package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import Controller.ModelInput;


public class MainViewClass {
	
	
	private static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	private JFrame myFrame;
	private ModelInput MODEL;
	
	private MyScreen screen1;
	JMenuBar menuBar;
	JMenu menuFile;
	JMenuItem mOpen,mSave,mClose,mReset;
	JPanel screenHolder;

	CameraMovementController cameraController;
	
	public MainViewClass(ModelInput m){
		
		this.MODEL = m;
		myFrame = new JFrame("Model Viewer");
		cameraController = new CameraMovementController(this);
		wczytaj();
	
		myFrame.addKeyListener(cameraController);
		myFrame.setLayout(new BorderLayout());
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.setResizable(true);
		myFrame.setVisible(true);
		myFrame.setPreferredSize(new Dimension((int)(WIDTH),(int)(HEIGHT)));
		
		mOpen = new JMenuItem("Open",'O');
		mSave = new JMenuItem("Save",'S');
		mClose = new JMenuItem("Close");
		mReset = new JMenuItem("Reset",'R');
		
		mOpen.setActionCommand("OPEN");
		mSave.setActionCommand("SAVE");
		mClose.setActionCommand("CLOSE");
		mReset.setActionCommand("RESET");;
		
		MenuListener menuL = new MenuListener(this);
		mOpen.addActionListener(menuL);
		mSave.addActionListener(menuL);
		mClose.addActionListener(menuL);
		mReset.addActionListener(menuL);
		
		mOpen.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		mSave.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		menuFile = new JMenu("PLIK");
		menuFile.add(mOpen);
		menuFile.add(mSave);
		menuFile.add(mReset);
		menuFile.add(mClose);
		menuBar = new JMenuBar();
		menuBar.add(menuFile);
		

		screen1 = new MyScreen();
		//screen1.setLayout(null);
		screen1.setPreferredSize(new Dimension((int)(WIDTH),(int)(HEIGHT)));
		
		
		screenHolder = new JPanel();
		screenHolder.setPreferredSize(new Dimension(  (int)(WIDTH),(int)(HEIGHT) ));
		screenHolder.setBackground(Color.GRAY);
		screenHolder.add(screen1);
		
		myFrame.setJMenuBar(menuBar);
		myFrame.add(screenHolder);
		
		myFrame.pack();
		
	}
	
	
	private void wczytaj(){
		
		
	}
	
	

	public void msg(String string) {
		JOptionPane.showMessageDialog(null,string);
		
	}
	

	public void initiate() {
		MODEL.setDefaultSize(WIDTH,HEIGHT);
	}

	

	public void call(String a) {
		switch(a) {
		case "RESET":
			MODEL.refresh();
			break;
			
		case"OPEN":
			PickFile();
			
			break;
		case"SAVE":
			try {
				 String name = JOptionPane.showInputDialog("Proszê podaæ nazwê pliku: ");
				MODEL.saveFile(name);
				msg("Plik zosta³ zapisany poprawnie :) ");
			} catch (Exception e) {
			//catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				msg("B³¹d zapisu pliku, spróbuj ponownie :( ");
			}
			
			break;
		case"CLOSE":
			
			break;
		}
		
	}
	
	public boolean PickFile() {
		
		JFileChooser fc = new JFileChooser();
		
		if(fc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			
			try {
				
			if( MODEL.openFile(file) )
				return true;
			else
				return false;
			
			}catch(Exception e) {
				
				msg("Z³y format pliku lub plik uszkodzony");
				return false;
			}
		}
		
		
		return false;
	
	}

	
	
	
	public void display(BufferedImage img) {
		// TODO Auto-generated method stub
		screen1.setImage(img);
		refresh();
	}


	public void refresh() {
		// TODO Auto-generated method stub
		screen1.repaint();
	}


	public void moveCamera(int i, int j, int k) {
		MODEL.moveCamera(i,j,k);
	}

	public void moveCenter(int i, int j, int k) {
		MODEL.moveCenter(i,j,k);
	}

	public void printCam() {
		// TODO Auto-generated method stub
		MODEL.printCam();
	}


	public void printCenter() {
		// TODO Auto-generated method stub
		MODEL.printCenter();
	}
	
	public void rotate(int dir) {
		MODEL.rotate(dir);
	}


	public void rotateUp(int i) {
		// TODO Auto-generated method stub
		MODEL.rotateUp(i);
	}

}
