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
 * The Tree command displays the entire file system as a tree on the shell.
 */
public class Tree extends ShellCommand {

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
		return "tree\n" + "The the tree command takes in no input "
				+ "parameter.\nWhen the user types in the tree you must, "
				+ "starting from the root directory (‘\\’)\ndisplay the "
				+ "entire file"
				+ "system as a tree. For every level of the tree, you must "
				+ "\nindent by a tab character.";
	}

	/**
	 * Recursive helper function for performOutcome that prints a StorageUnit as
	 * a tree. A number of tabs is indented before, which is the depth of the
	 * StorageUnit in the storage system.
	 * 
	 * @param toPrint
	 *            The StorageUnit to print out as a tree
	 * @param depth
	 *            The depth of the StorageUnit in the storage system
	 * @param stdout
	 *            The StdOut to print to
	 */
	private static void printTree(StorageUnit toPrint, int depth,
			StdOut stdout) {
		int i;
		for (i = 0; i < depth; i++) {
			stdout.send("\t");
		}
		stdout.sendLine(toPrint.name);
		if (toPrint.isDirectory()) {
			for (StorageUnit unit : ((Directory) toPrint).getDirContents()) {
				printTree(unit, depth + 1, stdout);
			}
		}
	}

	/**
	 * Tell the shell to print to its command line the entire file system as a
	 * tree.
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
		if (parameters.length != 1) {
			PrintError.reportError(shell, "tree",
					"Invalid number of arguments.");
			return;
		}
		Tree.printTree(shell.getRootDir(), 0, stdout);
		stdout.closeStream();
	}

}
