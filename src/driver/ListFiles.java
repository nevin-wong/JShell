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
 * The ListFiles command is used by the user to print the contents of a specific
 * directory in the file system, sometimes recursively.
 */

public class ListFiles extends ShellCommand {

	/**
	 * Returns if this command produces StdOut. (used by the Interpreter to know
	 * whether or not to make a new file)
	 * 
	 * @return Whether or not the command produces StdOut
	 */
	public static boolean producesStdOut() {
		return true;
	}

	/**
	 * Provides the manual for how to use this command
	 * 
	 * @return The manual
	 */
	public static String getManual() {
		return "ls [-R] [PATH ...] \nIf no paths are given, print the contents "
				+ "(file or directory) of the current \ndirectory, with a new "
				+ "line following each of the content (file or directory). \n"
				+ "Otherwise, for each path p, the order listed: \n    - If p "
				+ "specifies a file, print p \n    - If p specifies a "
				+ "directory, print p, a colon, then the contents of that \n"
				+ "      directory, then an extra new line. \n    - If p does "
				+ "not exist, print a suitable message.\n If –R is present, "
				+ "recursively list all subdirectories of each given path";
	}

	/**
	 * Tell the JShell to list all the contents of a specific directory in the
	 * file system.
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
		StdOut stdout = new StdOut(shell, outputType, outputFile);
		boolean recursive = false;

		for (int i = 1; i < parameters.length; i++) {
			if (parameters[i].equals("-R")) {
				recursive = true;
			}
		}

		boolean cont = true;

		if (parameters.length == 1) {
			list(stdout, shell.getCurrentDir());
		} else if (parameters.length == 2 && recursive) {
			cont = parseParameter(shell, ".", stdout, recursive);
		} else {
			for (int i = 1; i < parameters.length; i++) {
				if (!parameters[i].equals("-R")) {
					cont = parseParameter(shell, parameters[i], stdout,
							recursive);
				}
				if (!cont) {
					break;
				}
			}
		}
		stdout.closeStream();
	}

	/**
	 * Prints all StorageUnit names in an ArrayList of StorageUnits.
	 * 
	 * @param stdout
	 *            The StdOut to send the contents to
	 * @param dir
	 *            The Directory whose contents are to be send to stdout
	 */
	private static void list(StdOut stdout, Directory dir) {
		// Function used to print all files in directory
		for (StorageUnit unit : dir) { // Uses dir's iterator
			stdout.sendLine(unit.name);
		}
		stdout.sendLine("");
	}

	/**
	 * Perform the actual listing of a given path userInput
	 * 
	 * @param shell
	 *            The JShell the path lives in
	 * @param userInput
	 *            The path the user types in
	 * @param stdout
	 *            the stdOut the user specifies
	 * @param recursive
	 *            Whether to print the directories recursively
	 * @return True, if the path is valid, false otherwise
	 */
	private static boolean parseParameter(JShell shell, String userInput,
			StdOut stdout, boolean recursive) {
		Path path = new Path(userInput);
		StorageUnit listThis = path.verifyPath(shell, false);

		if (listThis == null) {
			PrintError.reportError(shell, "ls", "Cannot access '"
					+ path.getPath() + "', no such file or directory.");
			return false;
		} else if (listThis.isFile()) {
			stdout.sendLine(path.getPath());
		} else {
			stdout.sendLine(userInput + ":");
			list(stdout, ((Directory) listThis));
		}

		if (recursive && listThis.isDirectory()) {
			for (StorageUnit x : ((Directory) listThis).getDirContents()) {
				if (x.isDirectory()) {
					if (userInput.endsWith("/")) {
						parseParameter(shell, userInput + x.name, stdout,
								recursive);
					} else {
						parseParameter(shell, userInput + "/" + x.name, stdout,
								recursive);
					}
				}
			}
		}
		return true;
	}
}
