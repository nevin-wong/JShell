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
 * The PopDirFromStack command is used by the user to remove the top entry from
 * the JShell's directory stack and make it its current directory.
 */

public class PopDirFromStack extends ShellCommand {

	/**
	 * Returns if this command produces StdOut. (used by the Interpreter to know
	 * whether or not to make a new file)
	 * 
	 * @return Whether or not the command produces StdOut
	 */
	public static boolean producesStdOut() {
		return false;
	}

	/**
	 * Provides the manual for how to use this command
	 * 
	 * @return The manual
	 */
	public static String getManual() {
		return "popd \nRemove the top entry from the directory stack, and cd "
				+ "into it. The removal \n"
				+ "must be consistent as per the LIFO behavior of a stack. "
				+ "The popd command \n"
				+ "removes the top most directory from the "
				+ "directory stack and makes it the \n"
				+ "current working directory. If there is no directory onto "
				+ "the stack, then give \n" + "appropriate error message.";
	}

	/**
	 * Tell the JShell to remove the top entry from the directory stack and make
	 * it its current directory.
	 * 
	 * @param shell
	 *            The JShell the command is to be performed on
	 * @param parameters
	 *            The parameters from the interpreter the command is to work
	 *            with
	 * @param outputType
	 *            An integer representing the type of destination: 0 represents
	 *            the command line, 1 represents overwriting a file, and 2
	 *            represents appending to a file
	 * @param outputFile
	 *            If outputType is 1 or 2, this is the file we are
	 *            overwriting/appending to, otherwise null
	 */
	public static void performOutcome(JShell shell, String[] parameters,
			int outputType, File outputFile) {

		boolean cont = true;

		cont = checkParam(parameters.length, shell);

		Stack<Directory> dirStack = shell.getDirStack(); // get shell.dirStack's
															// reference
		cont = checkStackSize(dirStack.size(), shell);

		if (cont) {

			Directory topDir = dirStack.pop(); // remove top entry

			shell.setCurrentDir(topDir); // change to the now removed top entry
		}
	}

	/**
	 * Check if the number of parameters the user enters is valid
	 * 
	 * @param paramNum
	 *            The number of parameters the user entered
	 * @param shell
	 *            The JShell where the file system is
	 * @return True if paramNum is 1, false otherwise
	 */
	public static boolean checkParam(int paramNum, JShellInterface shell) {
		if (paramNum != 1) {
			PrintError.reportError(shell, "popd",
					"Invalid number of arguments.");
			return false;
		}
		return true;
	}

	/**
	 * Check if the stack size is valid
	 * 
	 * @param stackSize
	 *            The size of the directory stack
	 * @param shell
	 *            The JShell where the directory stack is located
	 * @return True if paramNum is not 0, false if 0
	 */
	public static boolean checkStackSize(int stackSize, JShellInterface shell) {
		if (stackSize == 0) {
			PrintError.reportError(shell, "popd", "Directory stack is empty.");
			return false;
		}
		return true;
	}
}
