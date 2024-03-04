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
 * The MakeDirectory command is used by the user to make new directories in the
 * file system.
 */

public class MakeDirectory extends ShellCommand {

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
		return "mkdir DIR ... \n" + "Create directories, "
				+ "each of which may be relative to the current directory "
				+ "or \nmay be a full path. If creating a DIR results in any "
				+ "kind of error, do not proceed "
				+ "\nwith creating the rest. However, "
				+ "if some DIRs are created "
				+ "successfully, and another \ncreation "
				+ "results in an error, then give back an error "
				+ "specific to this DIR.";
	}

	/**
	 * Tell the JShell to make two new directories according to the user's
	 * specifications.
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
		if (parameters.length < 2) {
			PrintError.reportError(shell, "mkdir",
					"Invalid number of arguments.");
			return;
		}
		Directory currDir;
		Path path = new Path("");
		for (int i = 1; i < parameters.length; i++) {
			path.setPath(parameters[i]);
			int success = pathToDir(path, shell);
			if (success == 0) {
				return;
			}
		}
	}

	/**
	 * Converts a path into a new directory if the path is valid. Returns an
	 * integer that signifies the result of the operation
	 *
	 * @param path
	 *            The path to be checked
	 * @param shell
	 *            The JShell the command is to be performed on
	 *
	 * @return 0 if there was an error with creating the new directory 1 if
	 *         creating the new directory from path is successful
	 */
	private static int pathToDir(Path path, JShell shell) {
		Directory parent = (Directory) path.verifyPath(shell, true);
		String[] elements = path.getPathElements();
		if (parent == null) {
			PrintError.reportError(shell, "mkdir",
					"Directory does not exist: " + path.getPath());
			return 0;
		} else {
			if (path.getPath().equals("/")) {
				PrintError.reportError(shell, "mkdir",
						"Root directory already exists!");
				return 0;
			} else {
				if (parent.isSubDir(elements[elements.length - 1]) == -1) {
					// create dir
					int nDir = createDir(parent, elements[elements.length - 1],
							shell);
					if (nDir == 0) {
						return 0;
					}
				} else {
					PrintError.reportError(shell, "mkdir",
							"Directory already exists: " + path.getPath());
					return 0;
				}
			}
		}
		return 1;
	}

	/**
	 * Creates a directory given parent, name and shell. Returns an integer that
	 * signifies the result of the operation
	 *
	 * @param parent
	 *            The parent directory of the directory to be created in
	 * @param name
	 *            The name of the new directory to be created
	 * @param shell
	 *            The JShell the command is to be performed on
	 *
	 * @return 0 if there was an error creating the directory and 1 if creating
	 *         the directory was successful
	 *
	 */
	private static int createDir(Directory parent, String name, JShell shell) {
		if (!StorageUnit.hasForbidChar(name)) {
			if (parent.containsFile(name) == -1) {
				Directory nDir = new Directory(name, parent);
				if (nDir == null) {
					return 0;
				}
				parent.addFile(nDir);
				return 1;
			} else {
				PrintError.reportError(shell, "mkdir",
						"Directory name cannot be the same as filename: "
								+ name);
				return 0;
			}
		} else {
			PrintError.reportError(shell, "mkdir",
					"Directory name contains forbidden " + "character(s): "
							+ name);
			return 0;
		}
	}

}