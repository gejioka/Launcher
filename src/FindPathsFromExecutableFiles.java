import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
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

public class FindPathsFromExecutableFiles {
	private String executableFileName;
	private String executableFilePath;
	private String rootFolderPath;

	/** FindPathsFromExecutableFiles ( )
	  * type: FindPathsFromExecutableFiles
	  * params: -
	  * Create a new object FindPathsFromExecutableFiles.
	**/
	public FindPathsFromExecutableFiles ( ) {

	}

	/** setExecutableFileName ( )
	  * type: void
	  * params: String executableFileName
	  * Set the name of the executable file.
	**/
	public void setExecutableFileName ( String executableFileName ) {

		this.executableFileName = executableFileName;
	}

	/** getExecutableFileName ( )
	  * type: String
	  * params: -
	  * Return the name of the executable file.
	**/
	public String getExecutableFileName ( ) {
	
		return executableFileName;		
	}

	/** setExecutableFilePath ( )
	  * type: void
	  * params: String executableFilePath
	  * Set the path of the executable file.
	**/
	public void setExecutableFilePath ( String executableFilePath ) {

		this.executableFilePath = executableFilePath;
	}

	/** getExecutableFilePath ( )
	  * type: String
	  * params: -
	  * Return the path of the executable file.
	**/
	public String getExecutableFilePath ( ) {

		return executableFilePath;
	}

	/** setRootFolderPath ( )
	  * type: void
	  * params: String rootFolderPath
	  * Set the path of the root folder.
	**/
	public void setRootFolderPath ( String rootFolderPath ) {

		this.rootFolderPath = rootFolderPath;
	}

	/** getRootFolderPath ( )
	  * type: String
	  * params: -
	  * Return the path of the root folder.
	**/
	public String getRootFolderPath ( ) {

		return rootFolderPath;
	}

	/** findFilePath ( )
	  * type: void
	  * params: String fileName
	  * Find the specific executable file.
	**/
	public void findFilePath ( String fileName ) {
		File rootFile;
		File[] listOfFiles;
		int i;

		rootFile = new File ( rootFolderPath );
		listOfFiles = rootFile.listFiles ( );

		for ( i = 0; i < listOfFiles.length; i++ ) {

			if ( listOfFiles[i].getName ( ).contains ( fileName ) && listOfFiles[i].canExecute ( ) ) {

				setExecutableFileName ( listOfFiles[i].getName ( ) );
				setExecutableFilePath ( listOfFiles[i].getPath ( ) );

				System.out.println ( listOfFiles[i].getName ( ) );
			}
		} 
	}

	// TODO: 1) If there are two different programms which contain the same fileName ask user if he want both or one of them.
} 	//		 2) If there are spaces between words replace them with '-'. 	