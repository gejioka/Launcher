import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.awt.image.*;
import javax.activation.*;
import java.nio.file.*;

public class Search {
	private String rootFolder;
	private String nameOfFile;
	private List<File> listOfImages;
	private List<File> listOfFiles;
	private File file;
	private boolean isImage;
	private int widthOfImage;
	private int heightOfImage;
	private int WIDTH 	= 128;
	private int HEIGHT 	= 128;

	/** Search ( )
	  * type: Search
	  * params: String rootFolder, String nameOfFile, isImage
	  * Create a new object Search.
	**/
	public Search ( String rootFolder, String nameOfFile, boolean isImage ) {

		this.rootFolder = rootFolder;
		this.nameOfFile = nameOfFile;
		this.isImage = isImage;

		file = new File ( nameOfFile );
	}

	/** searchForSpecificFile ( )
	  * type: File
	  * params: -
	  * Return the file that was requested.
	**/
	public File searchForSpecificFile ( ) {	
		File topFolder;
		File[] children;
		int numberOfFiles=0;
		int numberOfThreads=0;
		int numOfDirectories=0;
		Thread[] threads=null;

		topFolder = new File ( rootFolder );
		children = topFolder.listFiles ( );

		createNewLists ( );

		// Find the number of threads which need for the jobs.
		for ( numberOfFiles = 0, numOfDirectories = 0; numberOfFiles < children.length; numberOfFiles++ ) {

			if ( children[numberOfFiles].isDirectory ( ) ) {

				numOfDirectories++;
			}
		}

		// Search for specific file in the first level of tree if didn't find it create threads to search all the levels.
		if ( numOfDirectories != 0 ) {

			threads = new Thread[numOfDirectories];
			for ( numberOfFiles = 0, numberOfThreads = 0; numberOfFiles < children.length; numberOfFiles++ ) {
				
				if ( children[numberOfFiles].isFile ( ) && children[numberOfFiles].getName ( ).contains ( nameOfFile ) ) {
					
					return children[numberOfFiles];
				}else if ( children[numberOfFiles].isDirectory ( ) ) {

					threads[numberOfThreads] =  new Thread ( new MultipleSearchFile ( children[numberOfFiles] ) );
					threads[numberOfThreads].start ( );
					numberOfThreads++;
				}
			}

			// Wait until all threads finish their jobs.
			for ( Thread thread : threads ) {
				try {
					
					thread.join ( );
				}catch ( Exception ex ) {

				} 
			}
		}else {

			setNodeToTheList ( simpleSearch ( children ) );
		}

		// Print the results.createList
		if ( isImage ) {
			
			if ( !listOfImages.isEmpty ( ) ) {

				printList ( listOfImages );
			}
		}else {

			printList ( listOfFiles );
		}

		return null;
	}

	/** isSVG ( )
	  * type: boolean
	  * params: String filename
	  * Check if an image has extension .svg.
	**/
	public boolean isSVG ( String filename ) {

		if ( filename.contains ( "svg" ) ) {

			return true;
		}

		return false;
	}

	/** checkImage ( )
	  * type: boolean
	  * params: String filepath
	  * Check if a file is image
	**/
	public void createListWithImages ( ) {
		Iterator<File> currImageIt = listOfImages.listIterator ( );
		File currFile;
		String mimetype=null;
		Path path;

		while ( currImageIt.hasNext ( ) ) { 
			
			currFile = currImageIt.next ( );
			path = currFile.toPath ( );

			try {
				
				mimetype = Files.probeContentType ( path );
			}catch ( IOException ioex ) {

				System.out.println ( "Something went wrong with I/O operation. " ); 
			}

			if ( !mimetype.contains ( "image" ) ) {
			
				currImageIt.remove ( );
			}
		}
	}

	public void removeSVGImages ( ) {
		Iterator<File> it = listOfImages.listIterator ( );
		File currFile=null;

		while ( it.hasNext ( ) ) {

			currFile = it.next ( );
			if ( isSVG ( currFile.getName ( ) ) ) {

				try {
					
					it.remove ( );
				}catch ( Exception ex ) {

					System.out.println ( "Something went wrong in remove." );
				}
			}
		}
	}

	/** findImageWithCorrectSize ( )
	  * type: File
	  * params: int imageSize
	  * Return the image with the correct size.
	**/
	public File findImageWithCorrectSize ( int imageSize ) {
		Iterator<File> imageIt = listOfImages.listIterator ( );
		boolean firstElement = true;
		File currFileOfList=null;
		File correctFile=null;
		ScaleImage scaleImage=null;
		BufferedImage correctImage=null;
		BufferedImage image=null;
		
		while ( imageIt.hasNext ( ) ) {
			
			currFileOfList = imageIt.next ( );
			
			scaleImage = new ScaleImage ( WIDTH, HEIGHT, currFileOfList.getAbsolutePath ( ), currFileOfList.getName ( ) );
			image = scaleImage.convertFileToBufferedImage ( );
			
			if ( firstElement ) {

				correctImage = image;
				correctFile = currFileOfList;

				firstElement = false;
			}

			if ( Math.abs ( image.getHeight ( ) - imageSize ) < Math.abs ( correctImage.getHeight ( ) - imageSize ) ) {

				correctImage = image;
				correctFile = currFileOfList;
			}
		}

		setWidthOfImage ( correctImage.getWidth ( ) );
		setHeightOfImage ( correctImage.getHeight ( ) );

		return correctFile;
	}

	/** printLists ( )
	  * type: void
	  * params: List<File> currList
	  * Print the nodes of a specific list.
	**/	
	public void printList ( List<File> currList ) {

		if ( !currList.isEmpty ( ) ) {
			
			Iterator<File> currIt = currList.listIterator ( );
			while ( currIt.hasNext ( ) ) {

				System.out.println ( currIt.next ( ) );
			}
		}
	}

	/** setWidthOfImage ( )
	  * type: void
	  * params: int widthOfImage
	  * Set the width of specific image.
	**/
	public void setWidthOfImage ( int widthOfImage ) {

		this.widthOfImage = widthOfImage;
	}

	/** getWidthOfImage ( )
	  * type: int
	  * params: -
	  * Return the width of specific image.
	**/
	public int getWidthOfImage ( ) {

		return widthOfImage;
	}

	/** setHeightOfImage ( )
	  * type: void
	  * params: int heightOfImage
	  * Set the height of specific image.
	**/
	public void setHeightOfImage ( int heightOfImage ) {

		this.heightOfImage = heightOfImage;
	}

	/** getHeightOfImage ( )
	  * type: int
	  * params: int widthOfImage
	  * Set the width of specific image.
	**/
	public int getHeightOfImage ( ) {

		return heightOfImage;
	}	

	/** setListOfImages ( )
	  * type: void
	  * params: List<File> listOfImages
	  * Set the list of all the images.
	**/
	public void setListOfImages ( List<File> listOfImages ) {

		this.listOfImages = listOfImages;
	}

	/** getListOfImages ( )
	  * type: List<File>
	  * params: -
	  * Return the list of images.
	**/
	public List<File> getListOfImages ( ) {

		return listOfImages;
	}

	/** setListOfFiles ( )
	  * type: void
	  * params: List<File> listOfFiles
	  * Set the list of files.
	**/
	public void setListOfFiles ( List<File> listOfFiles ) {

		this.listOfFiles = listOfFiles;
	}

	/** getListOfFiles ( )
	  * type: List<File>
	  * params: -
	  * Return the list of files.
	**/
	public List<File> getListOfFiles ( ) {

		return listOfFiles;
	}

	/** createNewList ( )
	  * type: void
	  * params: File currFile
	  * Create a new list.
	**/
	public void createNewLists ( ) {

		listOfImages = new LinkedList<File>( );
		listOfFiles = new LinkedList<File>( );
	}

	/** setNodeToTheList ( )
	  * type: void
	  * params: File currFile
	  * Set a new node to the specific list.
	**/
	public void setNodeToTheList ( File currFile ) {

		if ( isImage ) {

			if ( currFile != null && listOfImages.contains ( currFile ) == false ) {
				
				listOfImages.add ( currFile );
			}
		}else {

			if ( currFile != null && listOfFiles.contains ( currFile ) == false ) {

				listOfFiles.add ( currFile );
			}
		}
	}

	/** simpleSearch ( )
	  * type: File
	  * params: File listOfChildren
	  * Search for a specific file.
	**/
	public File simpleSearch ( File[] listOfChildren ) {
		int numberOfFiles=0;

		for ( numberOfFiles = 0; numberOfFiles < listOfChildren.length; numberOfFiles++ ) {

			if ( listOfChildren[numberOfFiles].isFile ( ) && listOfChildren[numberOfFiles].getName ( ).contains ( nameOfFile ) ) {
				
				return listOfChildren[numberOfFiles];
			}
		}

		return null;
	}

	public class MultipleSearchFile implements Runnable {
		File currRootFolder;
		Semaphore available = new Semaphore ( 1 );

		public MultipleSearchFile ( File currRootFolder ) {

			this.currRootFolder = currRootFolder;
		}

		public void run ( ) {
			File[] children;
			List<File> currList;
			File currFile;

			children = currRootFolder.listFiles ( );	
			currFile = multipleSearch ( children );
			try {
				available.acquire ( );

				setNodeToTheList ( currFile );

				available.release ( );
			}catch ( Exception ex ) {

			}
		}

		public File multipleSearch ( File[] listOfChildren ) {
			int numberOfFiles;
			File currFile=null;

			for ( numberOfFiles = 0; numberOfFiles < listOfChildren.length; numberOfFiles++ ) {
				
				if ( listOfChildren[numberOfFiles].isFile ( ) && listOfChildren[numberOfFiles].getName ( ).contains ( nameOfFile ) ) {

					return listOfChildren[numberOfFiles];
				}else if ( listOfChildren[numberOfFiles].isDirectory ( ) ) {
					
					currFile = multipleSearch ( listOfChildren[numberOfFiles].listFiles ( ) );
					try {
						
						available.acquire ( );
						
						setNodeToTheList ( currFile );
						
						available.release ( );
					}catch ( Exception ex ) {

						available.release ( );
					}
				}	
			}

			return null;
		} 
	}
}