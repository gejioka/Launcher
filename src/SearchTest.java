import java.io.*;
import java.util.*;
import java.util.regex.*;

public class SearchTest {
	private String rootFolderPath;
	private String fileForSearch;

	public SearchTest ( ) {

	}

	public void setRootFolderPath ( String rootFolderPath ) {

		this.rootFolderPath = rootFolderPath;
	}

	public String getRootFolderPath ( ) {

		return rootFolderPath;
	}

	public void setFileForSearch ( String fileForSearch ) {

		this.fileForSearch = fileForSearch;
	}

	public String getFileForSearch ( ) {

		return fileForSearch;
	}

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

	public String findTheRequestedFile ( List<File> listOfChildren, String fileName ) {
		Iterator<File> listIt = listOfChildren.listIterator ( );
		File currentFolder;
		File[] currentChildren;
		int numOfChildren;

		while ( listIt.hasNext ( ) ) {

			currentFolder = listIt.next ( );
			currentChildren = currentFolder.listFiles ( );

			// earch for the specific folder.
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

					return currentChildren[numOfChildren].getName ( );
				}
			}
		}

		return null;
	} 
}