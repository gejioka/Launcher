import java.lang.Thread;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.util.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;

public class ExecuteThread implements Runnable {
	JFrame launcherWindow;
	FindImagesPaths currentNode;
	List<FindPathsFromExecutableFiles> listOfExecutableFiles;
	Launcher launcher;

	/** ExecuteThread ( )
	  * type: ExecuteThread
	  * params: JFrame launcherWindow, FindImagesPaths currentNode, List<FindPathsFromExecutableFiles> listOfExecutableFiles, Launcher launcher
	  * Create a new ExecuteThread object.
	**/
	public ExecuteThread ( JFrame launcherWindow, FindImagesPaths currentNode, List<FindPathsFromExecutableFiles> listOfExecutableFiles, Launcher launcher ) {

		this.launcherWindow = launcherWindow;
		this.currentNode = currentNode;
		this.listOfExecutableFiles = listOfExecutableFiles;
		this.launcher = launcher;
	}

	public void run ( ) {

		JButton button = new JButton ( );
    	button.addActionListener ( new ExecuteActionListener ( currentNode.getImageName ( ), listOfExecutableFiles ) );
    	launcherWindow.add ( button );
		launcher.loadPropertyFileIcons ( button, currentNode );
	}

	public class ExecuteActionListener implements ActionListener {
		private String nameOfExecuteFile;
		private List<FindPathsFromExecutableFiles> listOfExecutableFiles;

		/** ExecuteActionListener ( )
		  * type: ExecuteActionListener
		  * params: String nameOfExecuteFile, List<FindPathsFromExecutableFiles> listOfExecutableFiles
		  * Create a new ExecuteActionListener object.
		**/
		public ExecuteActionListener ( String nameOfExecuteFile, List<FindPathsFromExecutableFiles> listOfExecutableFiles ) {

			this.nameOfExecuteFile = nameOfExecuteFile;
			this.listOfExecutableFiles = listOfExecutableFiles;
		}

		public void actionPerformed ( ActionEvent e ) {

			Iterator<FindPathsFromExecutableFiles> imIt;
			FindPathsFromExecutableFiles currentNode;

			imIt = listOfExecutableFiles.listIterator ( );
			try {

		    	while ( imIt.hasNext ( ) ) {

		    		currentNode = imIt.next ( );

		    		System.out.println ( currentNode.getExecutableFileNameWithoutExtension ( ) + " " + nameOfExecuteFile );
		    		if ( nameOfExecuteFile.contains ( currentNode.getExecutableFileNameWithoutExtension ( ) ) || currentNode.getExecutableFileNameWithoutExtension ( ).contains ( nameOfExecuteFile ) ) {
		    			
		    			Process proc = Runtime.getRuntime( ).exec( new String[] { "/bin/bash", "deskopen", currentNode.getExecutableFilePath ( ) } );
		    			
		    		}
				}
			}catch ( Exception ex ) {
				
				System.out.println ( "Here" );
			}
		}
	}
}