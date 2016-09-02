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
	private String parentFolderPath;
	private String executableFileNameWithoutExtension;

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

	/** setExecutableFileNameWithoutExtension ( )
	  * type: void
	  * params: String executableFileName
	  * Set the name of the executable file without its extension.
	**/
	public void setExecutableFileNameWithoutExtension ( String executableFileName ) {
		int position = executableFileName.lastIndexOf ( "." );
		
		if ( position > 0 ) {

    		executableFileNameWithoutExtension = executableFileName.substring ( 0, position );
		}
	}

	/** getExecutableFileNameWithoutExtension ( )
	  * type: String
	  * params: -
	  * Return the name of the executable file without its extension.
	**/
	public String getExecutableFileNameWithoutExtension ( ) {

		return executableFileNameWithoutExtension;
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

	/** setParentFolderPath ( )
	  * type: void
	  * params: String parentFolderPath
	  * Set the path of parent of the specific file.
	**/
	public void setParentFolderPath ( String parentFolderPath ) {

		this.parentFolderPath = parentFolderPath;
	}

	/** getParentFolderPath ( )
	  * type: String
	  * params: -
	  * Return the path of the parent of the specific file.
	**/
	public String getParentFolderPath ( ) {

		return parentFolderPath;
	}
}	