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
import java.util.Objects;

/**
 * Storage is similar to a computer's file system, or root directory of a Unix
 * shell. i.e., it is the Directory in which all existing JShells work in.
 */

public class Storage implements Serializable {

	/** The root directory of the storage system */
	private Directory root;

	/** The one and only reference to the Storage system */
	private static Storage onlyReference = null;

	/**
	 * Initializer for Storage, made private as we cannot allow multiple to be
	 * created
	 */
	private Storage(String name, Directory parentDir) {
		this.root = new Directory("/", null);
		this.root.setParentDir(this.root);
		// parent of root directory is always the root itself
	}

	/**
	 * Checks if another Object is equal to this Storage
	 * 
	 * @param o
	 *            The other Object
	 * @return If they are equal
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Storage storage = (Storage) o;
		return root.equals(storage.root);
	}

	/**
	 * Provides the hashCode for this Storage
	 * 
	 * @return The hashCode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(root);
	}

	/**
	 * Public getter method for the root directory.
	 * 
	 * @return The root directory of the storage system
	 */
	public Directory getRoot() {
		return this.root;
	}

	/**
	 * Get the only instance of Storage, creates it if it doesn't exist
	 *
	 * @return A newly created Storage if there is none yet, and returns the
	 *         only instance of it if there already is one
	 */
	public static Storage createNewStorage() {
		if (onlyReference == null) {
			onlyReference = new Storage("/", null);
		}
		// Singleton Design Pattern is used to ensure only ONE Storage is
		// created
		return onlyReference;
	}
}
