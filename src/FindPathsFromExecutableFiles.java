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
	private String absolutePath;

	public FindPathsFromExecutableFiles ( ) {

	}

	public void setExecutableFileName ( String executableFileName ) {

		this.executableFileName = executableFileName;
	}

	public String getExecutableFileName ( ) {
	
		return executableFileName;		
	}

	public void setExecutableFilePath ( String executableFilePath ) {

		this.executableFilePath = executableFilePath;
	}

	public String getExecutableFilePath ( ) {

		return executableFilePath;
	}

	public void setAbsolutePathForExecutableFiles ( String absolutePath ) {

		this.absolutePath = absolutePath;
	}

	public String getAbsolutePathForExecutableFiles ( ) {

		return absolutePath;
	}

	public String findExecutableFilePath ( ) {

		return absolutePath + "/" + executableFileName;
	}
} 