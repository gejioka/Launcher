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

public class FindImagesPaths {
	private String relativePath;
	private String absolutePath;
	private String imageName;
	private String imagePath;
	private String imageType;
	private String absoluteName;
	private int totalProgramsNumber;

	public FindImagesPaths ( ) {
		
	}

	/*!
		setRelativePath(String relativePath)
		Sets relateive path for this object
	*/
	public void setRelativePath ( String relativePath ) {

		this.relativePath = relativePath;
	}

	public String getRelativePath ( ) {

		return relativePath;
	}

	public void setAbsolutePathForImage ( String absolutePath ) {

		this.absolutePath = absolutePath;
	}

	public String getAbsolutePath ( ) {

		return absolutePath;
	}

	public void setAbsoluteNameOfImage ( String imageName, String imageType ) {

		absoluteName = imageName + "." + imageType;
	}

	public String getAbsoluteNameOfImage ( ) {

		return absoluteName;
	}

	public String findImagePath ( String imageName, String imageType ) {
		File rootFile;
		File currentFile;
		File nextChild;
		File[] listOfChildren;
		List<File> listOfParents=null;
		int checkedFile=0;
		int i=0;
		String parentFolderName="img";

		try {
			
			rootFile = new File ( absolutePath );
			currentFile = rootFile;
			listOfChildren = currentFile.listFiles ( );

			while ( true ) {

				try {

					Iterator<File> fileIt = listOfParents.listIterator ( );
					File tempFile;

					checkedFile = 0;
					while ( fileIt.hasNext ( ) ) {

						tempFile = fileIt.next ( );
						if ( tempFile == currentFile ) {

							checkedFile = 1;
						}						
					}
				}catch ( Exception ex ) {
					
				}
				
				if ( listOfChildren != null && listOfChildren.length != 0 && checkedFile == 0 ) {

					currentFile = listOfChildren[0];
					listOfChildren = currentFile.listFiles ( );
				}else {
					
					if ( getAbsoluteNameOfImage ( ).equals ( currentFile.getName ( ) ) && currentFile.getParentFile ( ).getName ( ).equals ( parentFolderName ) ) {

						return currentFile.getPath ( );
					}else {
						
						if ( currentFile != rootFile ) {
							nextChild = currentFile;
							currentFile = currentFile.getParentFile ( );
							listOfChildren = currentFile.listFiles ( );
			
							i=0;
							while ( !listOfChildren[i].getName ( ).equals ( nextChild.getName ( ) ) ) {
								i++;
							}

							if ( i + 1 <= listOfChildren.length - 1 ) {
								
								currentFile = listOfChildren[i+1];
								listOfChildren = currentFile.listFiles ( );
							}else {
								
								try {

									listOfParents.add ( currentFile );
								}catch ( Exception ex ) {

									listOfParents = new LinkedList<File> ( );
									listOfParents.add ( currentFile );
								}
							}
						}else {
							if ( checkedFile == 1 )
								break;
						}
					}
				}
			}
		}catch ( ArrayIndexOutOfBoundsException ex ) {

			System.out.println ( "Array index out of bounds exception " + i );
		}

		return null;
	}

	public void setImageName ( String imageName ) {

		this.imageName = imageName;
	}

	public String getImageName ( ) {

		return imageName;
	}

	public void setImageType ( String imageType ) {

		this.imageType = imageType;
	}

	public String getImageType ( ) {

		return imageType;
	}

	public void setImagePath ( String imagePath ) {

		this.imagePath = imagePath;
	}

	public String getImagePath ( ) {

		return imagePath;
	}

	public void setTotalProgramsNumber ( int totalProgramsNumber ) {

		this.totalProgramsNumber = totalProgramsNumber;
	}

	public int getTotalProgramsNumber ( ) {

		return totalProgramsNumber;
	}

	public void setParentFolderPath ( String imagePath ) {

		File parentPath = new File ( imagePath );
		String parentNodePath;

		parentNodePath = parentPath.getParent ( );
	}
}