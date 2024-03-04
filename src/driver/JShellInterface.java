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

import java.util.Stack;

/**
 * The Interface used for the real JShell and MockJShells used for testing.
 */
public interface JShellInterface {

	/**
	 * Getter method for this JShell's root directory
	 * 
	 * @return The root directory
	 */
	Directory getRootDir();

	/**
	 * Setter method for this JShell's current directory
	 * 
	 * @param dir
	 *            The new directory
	 */
	void setCurrentDir(Directory dir);

	/**
	 * Getter method for this JShell's current directory
	 * 
	 * @return The current directory
	 */
	Directory getCurrentDir();

	/**
	 * Print and error message to the command line
	 * 
	 * @param string
	 *            The error message
	 */
	void printError(String string);

	/**
	 * Public getter method for the directory stack
	 * 
	 * @return The directory stack of the shell
	 */
	Stack<Directory> getDirStack();
}
