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

public class Launcher {
	private static final String configFile = ".config";
	private static final int WIDTH=1000;
	private static final int HEIGHT=120;
	private String launcherConfigurationFile;
	private String iconName;
	private String iconType;
	private StringBuffer strLauncherConfigurationFile;
	private List<FindImagesPaths> listOfImages;
	private List<File> listOfAllPrograms; 
	private List<FindPathsFromExecutableFiles> listOfExecutableFiles;
	private List<String> listOfPathsFromExecutableFiles;
	private List<Thread> listOfThreads;

	public Launcher ( ) {

	}

	public Launcher ( String launcherConfigurationFile ) {

		this.launcherConfigurationFile = launcherConfigurationFile;
	}

	public void createTheLauncherWindow ( Launcher launcher ) {

		JFrame launcherWindow = new JFrame ( );
		launcherWindow.setSize ( WIDTH, HEIGHT );
		launcherWindow.setTitle ( "Launcher" );
		launcherWindow.setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );
		launcherWindow.setLayout( new GridLayout ( 1, 3 ) );
		launcherWindow.setVisible ( true );

		createMenu ( launcherWindow, launcher );
	}

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

	    findImagesNames ( launcherWindow, launcher );
	}

	public void findImagesNames ( JFrame launcherWindow, Launcher launcher ) {

		try {

			File file = new File ( launcherConfigurationFile );
		    FileReader fReader = new FileReader( file );
		    BufferedReader in = new BufferedReader( fReader );
		    String inputLine;	
		    FindImagesPaths imageNode;
		    FindPathsFromExecutableFiles executableFileNode;
		    String absolutePathForImages="/home/tsokos";
		    String relativePathForImages="~/";
		    String absolutePathForExecutableFiles="/usr/bin";
		    String parentFolderName="img";
		    int firstTime=1;	

		    strLauncherConfigurationFile = new StringBuffer ( );
		    while ( ( inputLine = in.readLine ( ) ) != null ) {

		    	strLauncherConfigurationFile.append ( inputLine );
		    }

		    fReader.close ( );

			Pattern nodeP = Pattern.compile( "(Program Name: )([a-zA-Z-]+)([\\p{Punct}]+)([a-zA-Z]+)" );
			Matcher nodeM = nodeP.matcher( strLauncherConfigurationFile );

			while ( nodeM.find ( ) ) {

				if ( !nodeM.group ( 2 ).isEmpty ( ) && !nodeM.group ( 4 ).isEmpty ( ) ) {
					
					imageNode = new FindImagesPaths ( );
					executableFileNode = new FindPathsFromExecutableFiles ( );

					iconName = nodeM.group ( 2 );
					iconType = nodeM.group ( 4 );

					imageNode.setRelativePath ( relativePathForImages );
					imageNode.setAbsolutePathForImage ( absolutePathForImages );
					imageNode.setImageName ( iconName );
					imageNode.setImageType ( iconType );
					imageNode.setAbsoluteNameOfImage ( iconName, iconType );
					imageNode.setImagePath ( imageNode.findImagePath ( iconName, iconType ) );

					executableFileNode.setAbsolutePathForExecutableFiles ( absolutePathForExecutableFiles );
					executableFileNode.setExecutableFileName ( iconName );
					executableFileNode.setExecutableFilePath ( executableFileNode.findExecutableFilePath ( ) );

					if ( firstTime == 1 ) {

						listOfImages = new LinkedList<FindImagesPaths> ( );
						listOfExecutableFiles = new LinkedList<FindPathsFromExecutableFiles> ( );

						listOfImages.add ( imageNode );
						listOfExecutableFiles.add ( executableFileNode );

	 					firstTime = 0;
					}else {
						
						listOfImages.add ( imageNode );
						listOfExecutableFiles.add ( executableFileNode );
					}
				}
			}

			createAllImageButtonsInFrame ( launcherWindow, listOfExecutableFiles, launcher );
		}catch( FileNotFoundException ex ) {
	     
	      System.out.println( "The specified file was not found at "+ launcherConfigurationFile );
	    }catch( IOException ex ) {
	      
	      System.out.println( "IOException occured while reading from file "+ launcherConfigurationFile );
	    }
	}

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
	
	public void loadPropertyFileIcons ( JButton button, FindImagesPaths currentNode ) {

		Properties prop = new Properties ( );
		InputStream input = null;
		String iconString=null;
		ImageIcon image=null;
		String programms;

		try {

			input = new FileInputStream ( currentNode.findImagePath ( "icon", "properties" ) );
			prop.load ( input );

			iconString = prop.getProperty ( currentNode.getImageType ( ), currentNode.getAbsoluteNameOfImage ( ) );

			try {
						
				image = new ImageIcon ( currentNode.getImagePath ( ) );
				button.setIcon ( image );

			} catch (Exception ex) {
				System.err.println("Couldn't find file: ");
			}

		}catch ( Exception ex ) {
			System.out.println ( "Error" );
		}

	}

	public class CreateFileMenuItemActionListener implements ActionListener {

		public void actionPerformed ( ActionEvent e ) {

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
	
		Launcher launcher = new Launcher ( configFile ); 
		launcher.createTheLauncherWindow ( launcher );
	}
}