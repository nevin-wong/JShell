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

import java.util.ArrayList;

/**
 * The Remove command is used by the user to remove a specified directory in the
 * file system.
 */

public class Remove extends ShellCommand {

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
		return "rm DIR\nRemoves the directory from the file system. This also "
				+ "removes all the\nchildren of DIR.";
	}

	/**
	 * Tell the JShell to remove a specified file/directory.
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

		if (!cont) {
			return;
		}

		Path toDelete = new Path(parameters[1]);
		Directory startDir;
		if (toDelete.isAbsolute()) {
			startDir = shell.getRootDir();
		} else {
			startDir = shell.getCurrentDir();
		}
		StorageUnit toDeleteDir = toDelete.verifyPath(shell, false, startDir);
		cont = checkDirectory(shell, toDeleteDir);
		if (!cont) {
			return;
		}
		cont = checkAncestor(shell, toDeleteDir);
		if (!cont) {
			return;
		}

		ArrayList<StorageUnit> parentContents = ((Directory) toDeleteDir)
				.getParentDir().getDirContents();
		int indexRemove = -1;
		for (int i = 0; i < parentContents.size(); i++) {
			if (parentContents.get(i).name.equals(toDeleteDir.name)) {
				indexRemove = i;
				break;
			}
		}
		parentContents.remove(indexRemove);
		toDeleteDir.delete();
		toDeleteDir = null;
	}

	/**
	 * Checks number of parameters and prints error when needed
	 * 
	 * @param paramNum
	 *            The number of wanted parameters
	 * @param shell
	 *            The shell in use
	 * @return
	 */
	public static boolean checkParam(int paramNum, JShellInterface shell) {
		if (paramNum != 2) {
			PrintError.reportError(shell, "rm", "Invalid number of parameters");
			return false;
		}
		return true;
	}

	/**
	 * Checks of a StorageUnit exists in a JShell's shell
	 * 
	 * @param shell
	 *            The shell in use
	 * @param toDeleteDir
	 *            The StorageUnit to be deleted and checked
	 * @return
	 */
	public static boolean checkDirectory(JShell shell,
			StorageUnit toDeleteDir) {
		if (toDeleteDir == null || !toDeleteDir.isDirectory()) {
			PrintError.reportError(shell, "rm",
					"Cannot delete, no such directory.");
			return false;
		}
		return true;
	}

	/**
	 * Checks if a StorageUnit is an ancestor
	 * 
	 * @param shell
	 *            The JShell in use
	 * @param toDeleteDir
	 *            The StorageUnit to be deleted and checked
	 * @return
	 */
	public static boolean checkAncestor(JShell shell, StorageUnit toDeleteDir) {
		if (toDeleteDir.checkParents(shell)) {
			PrintError.reportError(shell, "rm",
					"Cannot remove current working directory or any of its "
							+ "ancestors");
			return false;
		}
		return true;
	}
}
