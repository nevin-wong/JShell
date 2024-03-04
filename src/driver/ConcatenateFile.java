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
 * The ConcatenateFile command is used to print the contents of one or more
 * Files to the shell's command line, so the user can find out what is in
 * specific files.
 */

public class ConcatenateFile extends ShellCommand {

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
		return "cat FILE1 [FILE2 ...] \n"
				+ "Display the contents of FILE1 and other files "
				+ "(i.e. File2 ...) concatenated in \n"
				+ "the shell. You may want to "
				+ "use three line breaks to separate "
				+ "the contents of one file \n" + "from the other file.";
	}

	/**
	 * Tell the JShell to print the contents of one or more files.
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
		if (parameters.length < 2) {
			PrintError.reportError(shell, "cat",
					"Invalid number of arguments.");
			return;
		}
		Path p = new Path("");
		Directory cDir = shell.getCurrentDir();
		// for loop to loop through all the file paths specified
		for (int i = 1; i < parameters.length; i++) {
			p.setPath(parameters[i]);
			pathToCat(p, cDir, shell, stdout);
			// print line break
			if (i + 1 != parameters.length) {
				stdout.send("\n\n\n");
			}
		}
		stdout.closeStream();
	}

	/**
	 * Checks if given paths are valid and concatenates if valid.
	 * Returns an integer that signifies the result of the operation
	 *
	 * @param path
	 *            The (valid) path to the directory
	 * @param cDir
	 *            The current directory
	 * @param shell
	 *            The JShell in use
	 * @param stdout
	 *            The StdOut to send to
	 * @return 0 if operation is not successful and 1 if operation
	 * 				 is successful
	 */
	private static int pathToCat(Path path, Directory cDir, JShell shell,
			StdOut stdout) {
		if (path.getPath().equals("/")) {
			PrintError.reportError(shell, "cat",
					"The root is not a valid file!");
			return 0;
		}
		if (path.isAbsolute()) { // checks if the path is an absolute path
			cDir = shell.getRootDir();
		}
		String[] elements = path.getPathElements();
		Directory parent = (Directory) path.verifyPath(shell, true, cDir);
		// checks if the path is valid
		if (parent == null) {
			PrintError.reportError(shell, "cat",
					"Invalid file path specified: " + path.getPath());
			return 0;
		} else {
			if (parent.containsFile(elements[elements.length - 1]) == -1) {
				PrintError.reportError(shell, "cat", "File does not exist: "
						+ elements[elements.length - 1]);
				return 0;
			}
		}
		int success = catFiles(path, parent, shell, stdout);
		if (success == 0) {
			return 0;
		}
		return 1;
	}

	/**
	 * Concatenates files in a given path to a directory. Returns
	 * an integer that signifies the result of the operation
	 * 
	 * @param path
	 *            The (valid) path to the directory
	 * @param cDir
	 *            The current directory
	 * @param shell
	 *            The JShell in use
	 * @param stdout
	 *            The StdOut to send to
	 * @return 0 if operation is not successful and 1 if operation
	 * 				 is successful
	 */
	private static int catFiles(Path path, Directory cDir, JShell shell,
			StdOut stdout) {
		String[] pElements = path.getPathElements();
		ArrayList<StorageUnit> contents = cDir.getDirContents();
		// checking if file is valid under the Directory cDir
		int fIndex = cDir.containsFile(pElements[pElements.length - 1]);
		if (fIndex == -1) {
			PrintError.reportError(shell, "cat",
					"Invalid file path specified: " + path.getPath());
			return 0;
		}
		if (contents.get(fIndex).isFile()) {
			File file = (File) contents.get(fIndex);
			file.print(stdout); // printing contents of file
		} else {
			PrintError.reportError(shell, "cat",
					"Invalid file path specified: " + path.getPath());
			return 0;
		}
		return 1;
	}
}
