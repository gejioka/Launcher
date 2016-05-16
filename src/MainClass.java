import java.io.*;

public class MainClass {
	
	public static void main ( String args[] ) {
		File rootFolder;
		SearchTest searchTest;

		searchTest = new SearchTest ( );
		searchTest.setRootFolderPath ( "/usr/share/icons/hicolor");

		//searchTest.setFileForSearch ( );
		System.out.println ( searchTest.findTheRequestedFile ( searchTest.createListWithTheRightFolders ( ), "sublime" ) );
	}
}