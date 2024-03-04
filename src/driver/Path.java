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
 * A Path is a collection of directories listed in succession separated by
 * delimiter '/' the final element in a path may be either a File or Directory
 * 
 */

public class Path {

	/** A string representation of the path to be checked */
	private String path;
	/**
	 * The path stored as an array of strings, with the name of each
	 * directory/file in the path being its own element in the array
	 */
	private String[] pathElements;
	/**
	 * Marks whether path is absolute (from root) or relative to the current
	 * directory
	 */
	private boolean absolute;

	/**
	 * Initializes a Path variable with the given string. Note that if the path
	 * is absolute, index 0 of the array will be the empty string, and if path
	 * only contains forward slashes, pathElements.length == 0
	 * 
	 * @param path
	 *            The string representation of the path to be stored
	 */
	public Path(String path) {
		this.setPath(path);
		this.pathElements = (path.split("/"));
		this.absolute = this.determineAbsolute();
	}

	/**
	 * Public getter method for the string path
	 * 
	 * @return The string representation of the path of a path variable
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Public getter method for pathElements
	 * 
	 * @return The array of strings, pathElements of a path variable
	 */
	public String[] getPathElements() {
		return pathElements;
	}

	/**
	 * Public getter method for the boolean absolute
	 * 
	 * @return The boolean absolute of a path variable
	 */
	public boolean isAbsolute() {
		return absolute;
	}

	/**
	 * Determines the starting directory of a path
	 * 
	 * @param shell
	 *            The JShell where the file system is
	 */
	public Directory determineStartDir(JShellInterface shell) {
		if (this.isAbsolute()) {
			return shell.getRootDir();
		}
		return shell.getCurrentDir();
	}

	/**
	 * Changes the path that the path variable refers to
	 * 
	 * @param path
	 *            The string representation of the path to be referred to
	 */
	public void setPath(String path) {
		this.path = path;
		this.pathElements = (path.split("/"));
		this.absolute = this.determineAbsolute();
	}

	/**
	 * Cycles through a path to see if the path is valid up to second last entry
	 * in path
	 * 
	 * 
	 * @param startDir
	 *            The start directory of the path
	 * @param shell
	 *            The JShell to perform in
	 * @return null, if path is invalid, final directory checked by the function
	 *         if valid
	 */
	private Directory cyclePath(Directory startDir, JShellInterface shell) {
		String[] pathElements = this.getPathElements();
		int index = 0;

		if (this.path.equals("/")) {// path is root directory
			return shell.getRootDir();
		}
		if (pathElements.length == 0) { // path is invalid
			return null;
		}
		if (this.isAbsolute()) { // skip empty string
			index++;
		}

		while (startDir != null && index < pathElements.length - 1) {
			startDir = determinePathElement(startDir, index);
			index++;
		}

		return startDir;
	}

	/**
	 * Determines whether the given index of this.path.getPathElements() is
	 * valid in the given directory
	 * 
	 * @param directory
	 *            The directory where the element of the path is checked in
	 * @param index
	 *            The index of the element in path that is checked for validity
	 */
	private Directory determinePathElement(Directory parentDir, int index) {
		int directoryIndex = parentDir.isSubDir(pathElements[index]);
		String[] pathElements = this.getPathElements();

		if (pathElements[index].equals("..")) {
			if (parentDir.getName().equals("/")) {
				return parentDir;
			}
			return parentDir.getParentDir();
		}
		if (pathElements[index].equals(".")) {
			return parentDir;
		}

		if (directoryIndex != -1) {
			return (Directory) parentDir.getDirContents().get(directoryIndex);
		}

		return null;
	}

	/**
	 * Determines if the Path is absolute
	 * 
	 * @return True if absolute, False otherwise
	 */
	public boolean determineAbsolute() {
		if (this.path.startsWith("/")) {
			return true;
		}
		return false;
	}

	/**
	 * Determines whether the final element is a valid file or directory
	 * 
	 * @param parentDir
	 *            The directory where the final element should be stored
	 * @return the final element as a StorageUnit if it exists/valid, null
	 *         otherwise
	 */
	private StorageUnit determineFinalElement(Directory parentDir) {
		String[] pathElements = this.getPathElements();
		int finalIndex;
		if (pathElements.length != 0) {
			finalIndex = pathElements.length - 1;
		} else {
			return parentDir;
		}

		int directoryIndex = parentDir.isSubDir(pathElements[finalIndex]);
		int fileIndex = parentDir.containsFile(pathElements[finalIndex]);

		if (pathElements[finalIndex].equals("..")) {
			if (parentDir.getName().equals("/")) {
				return parentDir;
			}
			return parentDir.getParentDir();
		}
		if (pathElements[finalIndex].equals(".")) {
			return parentDir;
		}
		if (directoryIndex != -1) {
			return parentDir.getDirContents().get(directoryIndex);
		}
		if (fileIndex != -1) {
			return parentDir.getDirContents().get(fileIndex);
		}

		return null;
	}

	/**
	 * Determines if entire path is valid
	 * 
	 * @param shell
	 *            The JShell to perform in
	 * @param returnParent
	 *            Determines whether or not the parent of the final element is
	 *            returned
	 * @param startDir
	 *            The directory where cyclePath will begin in
	 * @return Return the final StorageUnit in the path (or parent of final of
	 *         returnParent true)
	 */
	public StorageUnit verifyPath(JShellInterface shell, boolean returnParent,
			Directory startDir) {

		Directory parentOfFinal = this.cyclePath(startDir, shell);
		if (returnParent) {
			return parentOfFinal;
		}
		if (parentOfFinal != null) {
			StorageUnit finalItem = this.determineFinalElement(parentOfFinal);
			return finalItem;
		}

		return null;
	}

	/**
	 * Determines if entire path is valid
	 * 
	 * @param shell
	 *            The JShell to perform in
	 * @param returnParent
	 *            Determines whether or not the parent of the final element is
	 *            returned
	 * @return Return the final StorageUnit in the path (or parent of final of
	 *         returnParent true)
	 */
	public StorageUnit verifyPath(JShellInterface shell, boolean returnParent) {
		Directory startDir = this.determineStartDir(shell);
		Directory parentOfFinal = this.cyclePath(startDir, shell);
		if (returnParent) {
			return parentOfFinal;
		}
		if (parentOfFinal != null) {
			StorageUnit finalItem = this.determineFinalElement(parentOfFinal);
			return finalItem;
		}

		return null;
	}
}
