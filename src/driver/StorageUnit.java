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

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A StorageUnit stores data and lives in a JShell's storage system. Subclasses
 * of StorageUnit are File and Directory.
 */

public abstract class StorageUnit implements Serializable {

	/** The name that the StorageUnit is identified by */
	protected String name;
	/** The directory the StorageUnit lives in */
	protected Directory parentDir;

	/**
	 * Public getter method for the name
	 * 
	 * @return The name of the StorageUnit
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Public setter method for the namme
	 * 
	 * @param name
	 *            The name to be changed to
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Public getter method for the parent directory
	 * 
	 * @return The StorageUnit's location
	 */
	public Directory getParentDir() {
		return parentDir;
	}

	/**
	 * Public getter method for the absolute path of a storage unit
	 * 
	 * @return The absolute path of a StorageUnit as a path variable
	 */
	public Path getFullPath(JShell shell) {
		String pathString = "/";
		StorageUnit currDir = this;

		if (currDir != shell.getRootDir()) {
			pathString = currDir.getParentDir().name + "/" + currDir.name
					+ pathString;
			// append name of parent directory to current directory
			currDir = currDir.getParentDir();

			while (currDir != shell.getRootDir()) {
				pathString = currDir.getParentDir().name + "/" + pathString;
				// keep appending parent directory until we hit root
				currDir = currDir.getParentDir();
			}
		}
		if (!pathString.equals("/")) {
			return new Path(pathString.substring(1)); // return absolute path
		} else {
			return new Path(pathString);
		}
	}

	/**
	 * Public setter method for the parent directory
	 * 
	 * @param parent
	 *            The new location of the StorageUnit
	 */
	public void setParentDir(Directory parent) {
		this.parentDir = parent;
	}

	/**
	 * Checks if a string has any of the characters not allowed in the name of a
	 * StorageUnit
	 * 
	 * @param filename
	 *            The string to be checked for forbidden characters
	 * @return If the name does indeed have problematic characters
	 */
	public static boolean hasForbidChar(String filename) {
		return (filename.contains("/") || filename.contains(".")
				|| filename.contains(" ") || filename.contains("!")
				|| filename.contains("@") || filename.contains("#")
				|| filename.contains("$") || filename.contains("%")
				|| filename.contains("^") || filename.contains("&")
				|| filename.contains("*") || filename.contains("(")
				|| filename.contains(")") || filename.contains("{")
				|| filename.contains("}") || filename.contains("~")
				|| filename.contains("|") || filename.contains("<")
				|| filename.contains(">") || filename.contains("?"));
	}

	/**
	 * Checks if this is equal to the current directory or any of the current
	 * directory's ancestors
	 * 
	 * @param shell
	 *            The JShell that the current directory and this live in
	 * @return true, if this is equal to currDir or any of its ancestors, false
	 *         otherwise
	 */
	public boolean checkParents(JShell shell) {
		ArrayList<Directory> currDirParents = new ArrayList<Directory>();
		Directory currDir = shell.getCurrentDir();
		currDirParents.add(currDir);

		while (currDir.getParentDir() != shell.getRootDir()) {
			currDirParents.add(currDir.getParentDir());
			currDir = currDir.getParentDir();
		}

		currDirParents.add(shell.getRootDir());

		for (int i = 0; i < currDirParents.size(); i++) {
			if (currDirParents.get(i) == this) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if this is an ancestor to directory
	 * 
	 * @param directory
	 *            The directory that this is compared to
	 * @param shell
	 *            The JShell that the directory lives in
	 * @return true if this is an ancestor to directory, false otherwise
	 */

	public boolean checkParents(Directory directory, JShellInterface shell) {
		ArrayList<Directory> directoryParents = new ArrayList<Directory>();
		directoryParents.add(directory);

		while (directory.getParentDir() != shell.getRootDir()) {
			directoryParents.add(directory.getParentDir());
			directory = directory.getParentDir();
		}

		directoryParents.add(shell.getRootDir());

		for (int i = 0; i < directoryParents.size(); i++) {
			if (directoryParents.get(i) == this) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if this is a directory
	 * 
	 * @return True, if this is a directory, false otherwise
	 */
	public boolean isDirectory() {
		return this.getClass().getSimpleName().equals("Directory");
	}

	/**
	 * Checks if this is a file
	 * 
	 * @return True, if this is a file, false otherwise
	 */
	public boolean isFile() {
		return this.getClass().getSimpleName().equals("File");
	}

	/**
	 * Checks if this.name is equal to any of the StorageUnit names in directory
	 * 
	 * @param directory
	 *            The directory where the storage units are that will be
	 *            compared to this
	 * @return True, if this is a file, false otherwise
	 */
	public boolean checkIdenticalNames(Directory directory) {
		for (StorageUnit x : directory.getDirContents()) {
			if (this.name.equals(x.name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns an identical StorageUnit that can be modified independently from
	 * this
	 * 
	 * @param parentDir
	 *            The directory where the cloned StorageUnit will be located
	 * @return The cloned StorageUnit
	 */
	public abstract StorageUnit clone(Directory parentDir);

	/** Sets all instance variables to null */
	public abstract void delete();

	protected abstract void deleteRec();
}
