import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.*;
import java.util.regex.*;
import javax.imageio.*;
import java.lang.System.*;
import java.io.FileOutputStream.*;
import java.nio.file.Files;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.MenuBar;
import java.util.concurrent.Semaphore;
import java.nio.charset.*;
import java.nio.file.*;

public class Launcher {
	private static String configFile = "../.config";
	private static String absolutePathOfImages = "/usr/share/icons/hicolor";
	private static String absolutePathForExecutableFiles = "/usr/bin";
	private String launcherConfigurationFile;
	private String iconName;
	private String iconType;
	private StringBuffer strLauncherConfigurationFile;
	private List<FindImagesPaths> listOfImages;
	private List<File> listOfAllPrograms; 
	private List<FindPathsFromExecutableFiles> listOfExecutableFiles;
	private List<String> listOfPathsFromExecutableFiles;
	private List<Thread> listOfThreads;

	/** Launcher ( )
	  * type: Launcher
	  * params: -
	  * Create a new Launcher object.
	**/
	public Launcher ( ) {

	}

	/** Launcher ( )
	  * type: Launcher
	  * params: String launcherConfigurationFile
	  * Create a new Launcher object.
	**/
	public Launcher ( String launcherConfigurationFile ) {

		this.launcherConfigurationFile = launcherConfigurationFile;
	}

	/** createTheLauncherWindow ( )
	  * type: void
	  * params: Launcher launcher, int width, int height
	  * Create the window of app.
	**/
	public void createTheLauncherWindow ( Launcher launcher, int width, int height ) {

		JFrame launcherWindow = new JFrame ( );
		launcherWindow.setSize ( width, height );
		launcherWindow.setTitle ( "Launcher" );
		launcherWindow.setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );
		launcherWindow.setLayout( new GridLayout ( 1, 3 ) );
		launcherWindow.setVisible ( true );

		createMenu ( launcherWindow, launcher );
	}
	
	/** createMenu ( )
	  * type: void
	  * params: JFrame launcherWindow, Launcher launcher
	  * Create the menu of the app.
	**/
	private void createMenu ( JFrame launcherWindow, Launcher launcher ) {
		
		JMenu menu = new JMenu ( "Menu" );
	    menu.setBackground ( Color.LIGHT_GRAY );
	    menu.setMnemonic ( KeyEvent.VK_C );
	 
	    JMenuItem createFileMenuItem = new JMenuItem ( "Add" );
	    createFileMenuItem.addActionListener ( new CreateFileMenuItemActionListener ( ) );    
	    createFileMenuItem.setMnemonic ( KeyEvent.VK_F );
	    menu.add ( createFileMenuItem );

	    JMenuItem createDirMenuItem = new JMenuItem ( "Create Directory" );
		createDirMenuItem.addActionListener ( new CreateDirMenuItemActionListener ( ) );
	    createDirMenuItem.setMnemonic ( KeyEvent.VK_N );
	    menu.add ( createDirMenuItem );

	    JMenuItem deleteMenuItem = new JMenuItem ( "Delete" );
	    deleteMenuItem.addActionListener ( new DeleteMenuItemActionListener ( ) );
	   	deleteMenuItem.setMnemonic ( KeyEvent.VK_D );
	    menu.add ( deleteMenuItem );

	    JMenuItem exitMenuItem = new JMenuItem ( "Exit" );
	    exitMenuItem.addActionListener ( new ExitMenuItemActionListener ( ) );
	    exitMenuItem.setMnemonic ( KeyEvent.VK_Q );
		menu.add ( exitMenuItem );

	    JMenuBar bar = new JMenuBar ( );
	    bar.add ( menu );
	    launcherWindow.setJMenuBar ( bar );

	    createAllImageButtonsInFrame ( launcherWindow, listOfExecutableFiles, launcher );
	}

	/** findImagesNames ( )
	  * type: void
	  * params: JFrame launcherWindow, Launcher launcher
	  * Find the names of the images.
	**/
	public void findImagesNames ( Launcher launcher ) {

		try {
		    String inputLine;	
		    FindImagesPaths imageNode;
		    FindPathsFromExecutableFiles executableFileNode;
		    
		    int width=128;
		    int height=128;

		    File file = new File ( configFile );
			File currentFile;
		    FileReader fReader = new FileReader( file );
		    BufferedReader in = new BufferedReader( fReader );
		    
		    strLauncherConfigurationFile = new StringBuffer ( );
		    while ( ( inputLine = in.readLine ( ) ) != null ) {

		    	strLauncherConfigurationFile.append ( inputLine );
		    }
		    fReader.close ( );
			
			Pattern nodeP = Pattern.compile( "([a-zA-Z-]+)" );
			Matcher nodeM = nodeP.matcher( strLauncherConfigurationFile );
			
			while ( nodeM.find ( ) ) {
					
				imageNode = new FindImagesPaths ( );
				executableFileNode = new FindPathsFromExecutableFiles ( );

				imageNode.setRootFolderPath ( absolutePathOfImages );
				currentFile = imageNode.findTheRequestedFile ( imageNode.createListWithTheRightFolders ( ), nodeM.group ( 1 ) );

				imageNode.setAbsolutePath ( currentFile.getAbsolutePath ( ) );
				imageNode.findNameOfImage ( currentFile.getName ( ) );
				imageNode.findTypeOfImage ( currentFile.getName ( ) );
				imageNode.setAbsoluteNameOfImage ( imageNode.getImageName ( ), imageNode.getImageType ( ) );
				imageNode.setParentFolderPath ( currentFile.getParentFile ( ).getPath ( ) );
				imageNode.setResolutionOfImage ( currentFile );

				executableFileNode.setRootFolderPath ( absolutePathForExecutableFiles );
				executableFileNode.findFilePath ( imageNode.getImageName ( ) );

				try {

					width = width + 128;
					listOfImages.add ( imageNode );
					listOfExecutableFiles.add ( executableFileNode );
				}catch ( Exception ex ) {

					listOfImages = new LinkedList<FindImagesPaths> ( );
					listOfExecutableFiles = new LinkedList<FindPathsFromExecutableFiles> ( );

					listOfImages.add ( imageNode );
					listOfExecutableFiles.add ( executableFileNode );
				}
			}

			launcher.createTheLauncherWindow ( launcher, width, height );
		}catch( FileNotFoundException ex ) {
	     
	      System.out.println( "The specified file was not found at " + launcherConfigurationFile );
	    }catch( IOException ex ) {
	      
	      System.out.println( "IOException occured while reading from file " + launcherConfigurationFile );
	    }
	}

	/** createAllImageButtonsInFrame ( )
	  * type: void
	  * params: JFrame launcherWindow, List<FindPathsFromExecutableFiles> listOfExecutableFiles, Launcher launcher
	  * Create all the buttons for the app.
	**/
	public void createAllImageButtonsInFrame ( JFrame launcherWindow, List<FindPathsFromExecutableFiles> listOfExecutableFiles, Launcher launcher ) {

		Iterator<FindImagesPaths> findIt;
		FindImagesPaths currentNode;
		Thread executeThread=null;
		boolean inTheList=false;

		findIt = listOfImages.listIterator ( );
		while ( findIt.hasNext ( ) ) {

		  	currentNode = findIt.next ( );
		   	try {

    			executeThread = new Thread ( new ExecuteThread ( launcherWindow, currentNode, listOfExecutableFiles, launcher ) );
				executeThread.start( );
	 
    			listOfThreads.add ( executeThread );
    			executeThread.join( );
    		}catch( Exception e ) {
    			
    			listOfThreads = new LinkedList<Thread> ( );
    			listOfThreads.add ( executeThread );
    		}
	    }
	}
	
	/** loadPropertyFileIcons ( )
	  * type: void
	  * params: JButton button, FindImagesPaths currentNode
	  * Load the file icons property.
	**/
	public void loadPropertyFileIcons ( JButton button, FindImagesPaths currentNode ) {

		Properties prop = new Properties ( );
		InputStream input = null;
		String iconString=null;
		ImageIcon image=null;
		String programms;

		try {

			try {
						
				image = new ImageIcon ( currentNode.getAbsolutePath ( ) );
				button.setIcon ( image );

			} catch (Exception ex) {
				System.err.println("Couldn't find file: ");
			}

		}catch ( Exception ex ) {
			System.out.println ( "Error" );
		}

	}

	public class CreateFileMenuItemActionListener implements ActionListener {
		private String newApp;

		public CreateFileMenuItemActionListener ( ) {

		}

		public void actionPerformed ( ActionEvent e ) {
			JFrame addWindow = new JFrame ( );
			int width = 300;
			int height = 300;

			addWindow.setSize ( width, height );
			addWindow.setTitle ( "Add New App" );
			addWindow.setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );
			
			JLabel addJLabel = new JLabel("Add app: ", JLabel.RIGHT);
			addJLabel.setSize ( 50, 50 );

			JTextField userText = new JTextField( "Test" );
			userText.setSize ( 100, 100 );

			addWindow.add ( addJLabel );
			addWindow.add ( userText );
			addWindow.setVisible ( true );

			/*
			try {
  				
  				Files.write ( Paths.get ( configFile ), newApp.getBytes ( ), StandardOpenOption.APPEND );
			}catch ( IOException ioex ) {
    			//exception handling left as an exercise for the reader
			}*/
		}
	}

	public class CreateDirMenuItemActionListener implements ActionListener {

		public void actionPerformed ( ActionEvent e ) {
			
		}
	}

	public class DeleteMenuItemActionListener implements ActionListener {

		public void actionPerformed ( ActionEvent e ) {
			
		}
	}

	public class ExitMenuItemActionListener implements ActionListener {

		public void actionPerformed ( ActionEvent e ) {
			
		}
	}
	
	public static void main ( String args[] ) {
	
		Launcher launcher = new Launcher ( ); 
		launcher.findImagesNames ( launcher );
	}
}
