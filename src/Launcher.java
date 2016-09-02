import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.FlowLayout;
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
	private static String absolutePathOfImages = "/usr/share/icons";
	private static String absolutePathForExecutableFiles = "/usr/share/applications";
	private String launcherConfigurationFile;
	private StringBuffer strLauncherConfigurationFile;
	private List<FindImagesPaths> listOfImages;
	private List<File> listOfAllPrograms; 
	private List<FindPathsFromExecutableFiles> listOfExecutableFiles;
	private List<String> listOfPathsFromExecutableFiles;
	private List<Thread> listOfThreads;
	private boolean resize;
	private JFrame launcherWindow;

	/** Launcher ( )
	  * type: Launcher
	  * params: -
	  * Create a new Launcher object.
	**/
	public Launcher ( ) {

	}

	/** Launcher ( )
	  * type: Launcher
	  * params: boolean resize
	  * Create a new Launcher object.
	**/
	public Launcher ( boolean resize ) {

		this.resize = resize;
	}

	/** Launcher ( )
	  * type: Launcher
	  * params: String launcherConfigurationFile
	  * Create a new Launcher object.
	**/
	public Launcher ( String launcherConfigurationFile ) {

		this.launcherConfigurationFile = launcherConfigurationFile;
	}

	/** Launcher ( )
	  * type: Launcher
	  * params: String launcherConfigurationFile, boolean resize
	  * Create a new Launcher object.
	**/
	public Launcher ( String launcherConfigurationFile, boolean resize ) {

		this.launcherConfigurationFile = launcherConfigurationFile;
		this.resize = resize;
	}

	/** createTheLauncherWindow ( )
	  * type: void
	  * params: Launcher launcher, int width, int height
	  * Create the window of app.
	**/
	public void createTheLauncherWindow ( Launcher launcher, int width, int height ) {

		if ( !resize ) {

			launcherWindow = new JFrame ( );
			resize = true;	
		}else {

			launcherWindow.dispose ( );
			launcherWindow = new JFrame ( );
		}
		
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
	    createFileMenuItem.addActionListener ( new CreateFileMenuItemActionListener ( launcher ) );    
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
		    File image;
		    File currFile;
		    File wrongFile;	
		    Search searchImage;
		    Search searchExecFile;
		    FindImagesPaths imageNode;
		    FindPathsFromExecutableFiles executableFileNode;
		    Iterator<File> fileIt;
		    File currentFile;		    
		    int width			= 0;
		    int height			= 160;
		    File file 			= new File ( configFile );
		    FileReader fReader 	= new FileReader( file );
		    BufferedReader in 	= new BufferedReader( fReader );
		    
		    strLauncherConfigurationFile = new StringBuffer ( );
		    while ( ( inputLine = in.readLine ( ) ) != null ) {

		    	strLauncherConfigurationFile.append ( inputLine );
		    }

		    fReader.close ( );
			
			Pattern nodeP = Pattern.compile( "([a-zA-Z-]+)" );
			Matcher nodeM = nodeP.matcher( strLauncherConfigurationFile );
			
			while ( nodeM.find ( ) ) {
				
				searchImage = new Search ( absolutePathOfImages, nodeM.group ( 1 ), true ); 
				imageNode = new FindImagesPaths ( );
				executableFileNode = new FindPathsFromExecutableFiles ( );

				imageNode.setRootFolderPath ( absolutePathOfImages );
				searchImage.searchForSpecificFile ( );
				searchImage.createListWithImages ( );
				searchImage.removeSVGImages ( );
				image = searchImage.findImageWithCorrectSize ( 128 );
				imageNode.setAbsolutePath ( image.getAbsolutePath ( ) );
				imageNode.findNameOfImage ( image.getName ( ) );
				imageNode.findTypeOfImage ( image.getName ( ) );
				imageNode.setAbsoluteNameOfImage ( imageNode.getImageName ( ), imageNode.getImageType ( ) );
				imageNode.setParentFolderPath ( image.getParentFile ( ).getPath ( ) );

				searchExecFile = new Search ( absolutePathForExecutableFiles, nodeM.group ( 1 ), false );
				executableFileNode.setRootFolderPath ( absolutePathForExecutableFiles );
				wrongFile = searchExecFile.searchForSpecificFile ( );
				searchExecFile.setNodeToTheList ( wrongFile );

				fileIt = searchExecFile.getListOfFiles ( ).listIterator ( );
				while ( fileIt.hasNext ( ) ) {

					currFile = fileIt.next ( );

					executableFileNode.setExecutableFileName ( currFile.getName ( ) );
					executableFileNode.setExecutableFileNameWithoutExtension ( executableFileNode.getExecutableFileName ( ) );
					executableFileNode.setExecutableFilePath ( currFile.getPath ( ) );
					executableFileNode.setParentFolderPath ( currFile.getParent ( ) );
				}

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
		private Launcher launcher;

		public CreateFileMenuItemActionListener ( Launcher launcher ) {

			this.launcher = launcher;
		}

		public void actionPerformed ( ActionEvent e ) {
			final JFrame addWindow;
			JLabel headerLabel;
			JLabel statusLabel;
			JPanel controlPanel;

			addWindow = new JFrame ( );

			addWindow.setSize ( 400, 300 );
			addWindow.setTitle ( "Add New App" );
			addWindow.setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );
			addWindow.setLayout( new GridLayout ( 3, 1 ) );
			
			headerLabel = new JLabel ( "", JLabel.CENTER );
			statusLabel = new JLabel("",JLabel.CENTER);    

    		statusLabel.setSize(250,100);

			controlPanel = new JPanel();
        	controlPanel.setLayout(new FlowLayout());

        	addWindow.add ( headerLabel );
        	addWindow.add ( controlPanel );
        	addWindow.add ( statusLabel );

			addWindow.setVisible ( true );

			headerLabel.setText ( "Add a new application you want to launch.");

			JLabel  namelabel = new JLabel("New app to add: ", JLabel.RIGHT);
			final JTextField userText = new JTextField( 10 );

			JButton addButton = new JButton("OK");
			addButton.addActionListener(new ActionListener() {
         		
         		public void actionPerformed(ActionEvent e) {     
        			String textToAppend;
        			File file;

        			file = new File ( configFile );

        			try {
    					
    					if ( file.length ( ) == 0 ) {
    						
    						textToAppend = userText.getText ( ) + "/";
    						Files.write( Paths.get(configFile), textToAppend.getBytes(), StandardOpenOption.APPEND);
    					}else {

    						textToAppend = userText.getText ( ) + "/";
    						Files.write( Paths.get(configFile), textToAppend.getBytes(), StandardOpenOption.APPEND);
    					}
					}catch (IOException ioex) {
    					
    					//exception handling left as an exercise for the reader
					}

        			addWindow.setVisible ( false );

					listOfImages.clear ( );
					listOfExecutableFiles.clear ( );
					
					findImagesNames ( launcher );
         		}
      		});

			controlPanel.add ( namelabel );
			controlPanel.add ( userText );
			controlPanel.add ( addButton );

			addWindow.setVisible ( true );
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
	
		Launcher launcher = new Launcher ( false ); 
		launcher.findImagesNames ( launcher );
	}
}
