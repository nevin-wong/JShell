// **********************************************************
// Assignment2:
// Student1: Collin Chan
// UTORID user_name: chancol7
// UT Student #: 1006200889
// Author: Collin Chan
//
// Student2: Jeff He
// UTORID user_name: Hejeff2
// UT Student #: 1006398783
// Author: Jeff He
//
// Student3: Nevin Wong
// UTORID user_name: wongnevi
// UT Student #: 1005391434
// Author: Nevin Wong
//
// Student4: David Huynh
// UTORID user_name: huynhd12
// UT Student #: 1005991937
// Author: David Huynh
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************

package driver;

/**
 * A File is a type of StorageUnit (similar to a .txt file) that stores data.
 * It's able to be printed to the command line of JShell and have its data be
 * modified.
 */

public class File extends StorageUnit {

	/** The string with the contents of the File, similar a .txt file's text */
	private String contents;

	/**
	 * Initializes a File with given data in a given directory.
	 * 
	 * @param name
	 *            The name to be given to the File
	 * @param text
	 *            The data the File is to store, in the form of a string
	 * @param parentDir
	 *            The location the File is the be created in
	 */
	public File(String name, String text, Directory parentDir) {
		this.name = name;
		this.contents = text;
		this.parentDir = parentDir;
	}

	/**
	 * Public getter method for a File's contents
	 * 
	 * @return The File's contents
	 */
	public String getContents() {
		return this.contents;
	}

	/**
	 * Appends data to the File's already existing contents
	 * 
	 * @param text
	 *            The string to be appended to the File's contents
	 */
	public void append(String text) {
		this.contents = this.contents + text;
	}

	/**
	 * Overwrites the File's contents
	 * 
	 * @param text
	 *            This is the data for the File's data to be overwritten by
	 */
	public void overwrite(String text) {
		this.contents = text;
	}

	/**
	 * Sends to an StdOut the File's contents
	 * 
	 * @param stdout
	 *            The StdOut to send to
	 */
	public void print(StdOut stdout) {
		stdout.sendLine(this.contents);
	}

	/**
	 * Returns an identical file that can be modified independently from this
	 * 
	 * @param parentDir
	 *            The directory where the cloned file will be located
	 * @return The cloned file
	 */
	public File clone(Directory parentDir) {
		return new File(this.name, this.contents, parentDir);
	}
	
	/** Sets all instance variables to null */
	public void delete() {
		this.contents = null;
		this.parentDir = null;
		this.name = null;
	}

	@Override
	protected void deleteRec() {
		// TODO Auto-generated method stub
		
	}
}
