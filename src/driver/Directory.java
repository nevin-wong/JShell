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
import java.util.Iterator;
import java.util.Objects;

/**
 * A Directory is a type of StorageUnit (similar to a folder in a computer's
 * file system) that holds a collection of other StorageUnits.
 */

public class Directory extends StorageUnit
		implements
			Serializable,
			Iterable<StorageUnit> {

	/**
	 * An ArrayList containing all the contents of the Directory, similar to the
	 * contents of a computer's folder
	 */
	private ArrayList<StorageUnit> contents = new ArrayList<StorageUnit>();

	/**
	 * Initializes a Directory with the given name in a given location
	 * 
	 * @param name
	 *            The name to be given to the Directory
	 * @param parentDir
	 *            The location the Directory will live in
	 */
	public Directory(String name, Directory parentDir) {
		this.name = name;
		this.parentDir = parentDir;
	}

	/**
	 * Checks if another Object is equal to this Directory
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
		Directory directory = (Directory) o;
		return contents.equals(directory.contents);
	}

	/**
	 * Provides the hashCode for this Directory
	 * 
	 * @return The hashCode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(contents);
	}

	/**
	 * Public getter method for the Directory's contents
	 * 
	 * @return The Directory's contents
	 */
	public ArrayList<StorageUnit> getDirContents() {
		return contents;
	}

	/**
	 * Adds a StorageUnit to the Directory
	 * 
	 * @param fileName
	 *            The StorageUnit to be added
	 */
	public void addFile(StorageUnit fileName) {
		contents.add(fileName);
		fileName.setParentDir(this);
	}

	/**
	 * Deletes a StorageUnit from the Directory
	 * 
	 * @param fileName
	 *            The StorageUnit to be removed
	 */
	public void delFile(StorageUnit fileName) {
		contents.remove(fileName);
		fileName.setParentDir(null);
	}

	/**
	 * Gets a StorageUnit of a given index in the Directory
	 * 
	 * @param index
	 *            The desired index
	 * @return The StorageUnit in that index
	 */
	public StorageUnit getFile(int index) {
		return contents.get(index);
	}

	/**
	 * Checks if a Directory with a specific name is in the Directory's
	 * contents.
	 * 
	 * @param dirName
	 *            The name of the Directory to be checked if it is in the
	 *            Directory
	 * @return -1 if sub-directory named dirName is not in the Directory or
	 *         index of the sub-directory if it is
	 */
	public int isSubDir(String dirName) {
		int index = -1;
		for (int i = 0; i < contents.size(); i++) {
			// checks if the element is of type Directory
			if (contents.get(i).isDirectory()) {
				if (contents.get(i).name.equals(dirName)) {
					index = i;
				}
			}
		}
		return index;
	}

	/**
	 * Checks if a File with a specific name is in the Directory's contents.
	 * 
	 * @param fileName
	 *            The name of the File to be checked if it is in the Directory
	 * @return -1 if file named fileName is not in the Directory or index of the
	 *         file if it is
	 */
	public int containsFile(String fileName) {
		int index = -1;
		for (int i = 0; i < contents.size(); i++) {
			// checks if the element is of type File
			if (contents.get(i).isFile()) {
				if (contents.get(i).name.equals(fileName)) {
					index = i;
				}
			}
		}
		return index;
	}

	/**
	 * Returns an identical directory that can be modified independently from
	 * this
	 * 
	 * @param parentDir
	 *            The directory where the cloned directory will be located
	 * @return The cloned directory
	 */
	public Directory clone(Directory parentDir) {
		Directory clonedDir;
		clonedDir = new Directory(this.name, parentDir);
		clonedDir.contents = new ArrayList<StorageUnit>();

		for (StorageUnit x : this.contents) {
			if (x.isDirectory()) {
				Directory y = ((Directory) x).clone(clonedDir);
				clonedDir.contents.add(y);
			} else {
				File z = ((File) x).clone(clonedDir);
				clonedDir.contents.add(z);
			}
		}
		return clonedDir;
	}

	/**
	 * Sets this and all StorageUnits inside this to be null, does not remove
	 * references to this
	 * 
	 */
	public void delete() {
		this.parentDir.contents.remove(this);
		this.deleteRec();
	}

	/**
	 * Sets this and all StorageUnits inside this to be null, does not remove
	 * references to this, helper function for delete()
	 * 
	 */
	protected void deleteRec() {
		this.parentDir = null;
		this.name = null;

		for (StorageUnit x : this.contents) {
			x.deleteRec();
			x = null;
		}

		for (int i = 0; i < this.contents.size(); i++) {
			this.contents.remove(i);
		}

		this.contents = null;
	}
	/**
	 * ContentIterator is a nested class that iterates through StorageUnits in a
	 * Directory.
	 */
	private static class ContentIterator implements Iterator {

		/** The current index of the iterator */
		int currIndex;
		/** The ArrayList to iterate through */
		ArrayList<StorageUnit> toIterate;

		/**
		 * Constructor for a ChronologicalOrderIterator
		 * 
		 * @param toIterate
		 *            The ArrayList to iterate through
		 */
		public ContentIterator(ArrayList<StorageUnit> toIterate) {
			this.currIndex = 0;
			this.toIterate = toIterate;
		}

		/**
		 * Checks if there is a next StorageUnit in the ArrayList
		 * 
		 * @return If there exists a next StorageUnit in the current iteration
		 */
		@Override
		public boolean hasNext() {
			return (currIndex < toIterate.size());
		}

		/**
		 * Gives the next command of the current iteration
		 */
		@Override
		public Object next() {
			currIndex = currIndex + 1;
			return toIterate.get(currIndex - 1);
		}
	}

	/**
	 * Returns an appropriate Iterator used to iterate through this Directory
	 */
	@Override
	public Iterator<StorageUnit> iterator() {
		return new ContentIterator(contents);
	}

}
