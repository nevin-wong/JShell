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
 * The PushDirOntoStack command saves the current directory by pushing it onto a
 * directory stack and changes the new current directory to user-specified one.
 */

public class PushDirOntoStack extends ShellCommand {

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
		return "pushd DIR \nSaves the current working directory by pushing "
				+ "onto directory stack and then \n"
				+ "changes the new current working directory to DIR. The push "
				+ "must be \n"
				+ "consistent as per the LIFO behavior of a stack. The pushd "
				+ "command saves \n"
				+ "the old current working directory in directory stack so that"
				+ " it can be returned \n"
				+ "to at any time (via the popd command). The size of the "
				+ "directory stack \n"
				+ "is dynamic and dependent on the pushd and the popd "
				+ "commands.";
	}

	/**
	 * Tell the JShell to push the current directory into its directory stack
	 * and change its current directory
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
		if (parameters.length != 2) {
			PrintError.reportError(shell, "pushd",
					"Invalid number of arguments.");
			return;
		}
		Directory prevCurrDir = shell.getCurrentDir();
		Directory currDir = shell.getCurrentDir();
		String[] subDir = {};
		if (parameters[1].indexOf("/") == 0) {
			if (parameters[1].equals("/")) {
				shell.setCurrentDir(shell.getRootDir());
				return;
			}
			currDir = shell.getRootDir();
			parameters[1] = parameters[1].substring(1);
		}
		subDir = parameters[1].split("/");
		for (int i = 0; i < subDir.length; i++) {
			if (subDir[i].equals("..") || subDir[i].equals(".")) {
				if (subDir[i].equals("..")) {
					currDir = currDir.getParentDir();
				}
			} else {
				if (currDir.isSubDir(subDir[i]) == -1) {
					PrintError.reportError(shell, "pushd",
							"That is not a valid directory.");
					return;
				} else {
					int index = currDir.isSubDir(subDir[i]);
					currDir = (Directory) currDir.getDirContents().get(index);
				}
			}
		}
		shell.setCurrentDir(currDir);
		shell.getDirStack().add(prevCurrDir);
	}
}
