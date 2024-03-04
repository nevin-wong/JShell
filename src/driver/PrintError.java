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
 * PrintError is used by commands to print any error messages on JShell's
 * command line and any errors thrown by the operating system.
 */

public class PrintError {

	/**
	 * Reports an error message for a JShell to print
	 * 
	 * @param shell
	 *            The specified instance of JShell
	 * @param errMsg
	 *            The error message to be reported
	 */
	public static void reportError(JShellInterface shell, String errMsg) {
		shell.printError(errMsg);
	}

	/**
	 * Reports an error message from a specific command for a JShell to print
	 * 
	 * @param shell
	 *            The specified instance of JShell
	 * @param cmd
	 *            The name of the command that encountered the error
	 * @param errMsg
	 *            The error message to be reported
	 */
	public static void reportError(JShellInterface shell, String cmd,
			String errMsg) {
		shell.printError(cmd + ": " + errMsg);
	}
}