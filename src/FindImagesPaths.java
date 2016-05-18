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
import java.util.regex.*;

public class FindImagesPaths {
	private String relativePath;
	private String absolutePath;
	private String imageName;
	private String imagePath;
	private String imageType;
	private String absoluteName;
	private String rootFolderPath;
	private String parentFolderPath;
	private int totalProgramsNumber;

	/** FindImagesPaths ( )
	  * type: FindImagesPaths
	  * params: -
	  * Create a new object FindImagesPath.
	**/
	public FindImagesPaths ( ) {
		
	}

	public void setRootFolderPath ( String rootFolderPath ) {

		this.rootFolderPath = rootFolderPath;
	}

	public String getRootFolderPath ( ) {

		return rootFolderPath;
	}

	/** setAbsolutePath ( )
	  * type: void
	  * params: string absolutePath
	  * Set the absolute path of images.
	**/
	public void setAbsolutePath ( String absolutePath ) {

		this.absolutePath = absolutePath;
	}

	/** getAbsolutePath ( )
	  * type: String
	  * params: -
	  * Get the absolute path of images.
	**/
	public String getAbsolutePath ( ) {

		return absolutePath;
	}

	/** setAbsoluteNameOfImage ( )
	  * type: void
	  * params: String imageName, String imageType
	  * Set the absolute name of image.
	**/
	public void setAbsoluteNameOfImage ( String imageName, String imageType ) {

		absoluteName = imageName + "." + imageType;
	}

	/** getAbsoluteNameOfImage ( )
	  * type: String
	  * params: -
	  * Get the absolute name of image.
	**/
	public String getAbsoluteNameOfImage ( ) {

		return absoluteName;
	}

	/** setImageName ( )
	  * type: void
	  * params: String imageName
	  * Set the name of the specific image.
	**/
	public void setImageName ( String imageName ) {

		this.imageName = imageName;
	}

	/** getImageName ( )
	  * type: String
	  * params: -
	  * Return the name of the specific image.
	**/
	public String getImageName ( ) {

		return imageName;
	}

	/** setImageType ( )
	  * type: void
	  * params: String ImageType
	  * Set the type of the specific image.
	**/
	public void setImageType ( String imageType ) {

		this.imageType = imageType;
	}

	/** getImageType ( )
	  * type: void
	  * params: -
	  * Return the type of the specific image.
	**/
	public String getImageType ( ) {

		return imageType;
	}

	/** setTotalProgramsNumber ( )
	  * type: void
	  * params: int totalProgramsNumber
	  * Set the total number of programs in launcher.
	**/
	public void setTotalProgramsNumber ( int totalProgramsNumber ) {

		this.totalProgramsNumber = totalProgramsNumber;
	}

	/** getTotalProgramsNumber ( )
	  * type: int
	  * params: -
	  * Return the total number of programs.
	**/
	public int getTotalProgramsNumber ( ) {

		return totalProgramsNumber;
	}

	/** setParentFolderPath ( )
	  * type: void
	  * params: String imagePath
	  * Set the path of the parent.
	**/
	public void setParentFolderPath ( String imagePath ) {

		File parentPath = new File ( imagePath );
		String parentNodePath;

		parentNodePath = parentPath.getParent ( );
	}

	/** getParentFolderPath ( )
	  * type: String
	  * params: -
	  * Return the parent folder path.
	**/
	public String getParentFolderPath ( ) {

		return parentNodePath;
	}

	/** createListWithTheRightFolders ( )
	  * type: List<File>
	  * params: -
	  * Return a list with the right folders.
	**/
	public List<File> createListWithTheRightFolders ( ) {
		File rootFolder = null;
		File currentFolder = null;
		File[] currentChildren = null;
		List<File> listOfParents = null;
		List<File> listOfChildren = null;
		String imageSize = null;
		int numOfChildren;
		int sizeOfSubstring;
		int sizeOfIMage;

		rootFolder = new File ( rootFolderPath );
		currentChildren = rootFolder.listFiles ( );
		
		for ( numOfChildren = 0; numOfChildren < currentChildren.length; numOfChildren++ ) {
			
			sizeOfSubstring = currentChildren[numOfChildren].getName ( ).indexOf('x');

			// Check if the specific folder contains characters and the right size for images and add it to the list.
			if ( sizeOfSubstring != -1 ) {
				
				imageSize = currentChildren[numOfChildren].getName ( ).substring ( 0, sizeOfSubstring );
				
				if ( !imageSize.matches ( "[a-zA-Z]*" ) ) {
					
					sizeOfIMage = Integer.parseInt ( imageSize );
					if ( sizeOfIMage >= 128 ) {
						
						try {
						
							listOfChildren.add ( currentChildren[numOfChildren] );
						}catch ( Exception ex ) {

							listOfChildren = new LinkedList<File> ( );
							listOfChildren.add ( currentChildren[numOfChildren] );
						}
					}
				}
			}
		}

		return listOfChildren;
	}  

	/** findNameOfImage ( )
	  * type: void
	  * params: String image
	  * Find the name of image without the extension.
	**/ 
	public void findNameOfImage ( String image ) {

		Pattern pattern = Pattern.compile( "(.*)\\." );
		Matcher m = pattern.matcher ( image );
		
		if ( m.find ( ) ) {
			
			setImageName ( m.group ( 1 ) );
		}
	}

	/** findImageType ( )
	  * type: void
	  * params: String image
	  * Find the type of image.
	**/
	public void findTypeOfImage ( String image ) {

		Pattern pattern = Pattern.compile( "(.*)\\.(.*)" );
		Matcher m = pattern.matcher ( image );
		
		if ( m.find ( ) ) {
			
			System.out.println ( m.group ( 2 ) );
			setImageType ( m.group ( 2 ) );
		}
	}

	/** findTheRequestedFile ( )
	  * type: String
	  * params: List<File> listOfChildren, String fileName
	  * Return the requested file.
	**/ 
	public String findTheRequestedFile ( List<File> listOfChildren, String fileName ) {
		Iterator<File> listIt = listOfChildren.listIterator ( );
		File currentFolder;
		File[] currentChildren;
		int numOfChildren;

		while ( listIt.hasNext ( ) ) {

			currentFolder = listIt.next ( );
			currentChildren = currentFolder.listFiles ( );

			// Search for the specific folder.
			for ( numOfChildren = 0; numOfChildren < currentChildren.length; numOfChildren++ ) {
				
				if ( currentChildren[numOfChildren].getName ( ).equals ( "apps" ) ) {

					currentFolder = currentChildren[numOfChildren];
					break;
				}
			}

			// Search for specific file.
			currentChildren = currentFolder.listFiles ( );
			for ( numOfChildren = 0; numOfChildren < currentChildren.length; numOfChildren++ ) {
				
				if ( currentChildren[numOfChildren].getName ( ).contains ( fileName ) ) {

					setAbsolutePath ( currentChildren[numOfChildren].getAbsolutePath ( ) );
					findNameOfImage ( currentChildren[numOfChildren].getName ( ) );
					findTypeOfImage ( currentChildren[numOfChildren].getName ( ) );
					
					return currentChildren[numOfChildren].getName ( );
				}
			}
		}

		return null;
	}
}