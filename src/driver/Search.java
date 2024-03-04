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

import java.lang.reflect.Array;
import java.util.*;

/**
 * The Search command is used by the user to search for files/directories in the
 * file system.
 */

public class Search extends ShellCommand {

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
		return "search path ... -type [f|d] -name expression\n"
				+ "This command takes in at least three arguments. "
				+ "Searches for files/directories\nas specified after "
				+ "-type that has the name expression which is specified "
				+ "after -name.\nHowever, if path or [f|d] or "
				+ "expression is not specified, then give back an error";
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
		StdOut stdout = new StdOut(shell, outputType, outputFile);
		int tIndex = isValid(shell, parameters);
		if (tIndex == -1) {
			return;
		}
		Path path = new Path("");
		Directory currDir;
		// check for paths recursively
		for (int i = 1; i < tIndex; i++) {
			path.setPath(parameters[i]);
			if (path.isAbsolute()) {
				currDir = shell.getRootDir();
			} else {
				currDir = shell.getCurrentDir();
			}
			Directory parent = (Directory) path.verifyPath(shell, true,
					currDir);
			String name = parameters[parameters.length - 1]
					.replaceAll("^\"+|\"+$", "");
			recSearch(parent, name, parameters[tIndex + 1], stdout);
		}

		stdout.closeStream();
	}

	/**
	 * Recursively checks if a parent directory contains a file/directory
	 * depending on type that has name. Prints out the relative path using
	 * stdout if found.
	 *
	 * @param parent
	 *            The parent directory to be checked
	 * @param name
	 *            The name of the file/directory
	 * @param type
	 *            A string that signifies the type (f - for file and d - for
	 *            directory)
	 * @param stdout
	 *            Output stream
	 *
	 */
	public static void recSearch(Directory parent, String name, String type,
			StdOut stdout) {
		ArrayList<StorageUnit> contents = parent.getDirContents();
		// Base Case
		if (parent.getDirContents().isEmpty()) {
			return;
		}
		String finalParent = parent.getName();
		if (type.equals("f")) {
			if (parent.containsFile(name) != -1) {
				if (parent.getName().equals("/")) {
					finalParent = "";
				}
				stdout.sendLine(finalParent + "/" + name);
			}
		} else {
			if (parent.isSubDir(name) != -1) {
				if (parent.getName().equals("/")) {
					finalParent = "";
				}
				stdout.sendLine(finalParent + "/" + name);
			}
		}
		// Recursive Case
		for (StorageUnit sub : contents) {
			if (sub.getClass().getSimpleName().equals("Directory")) {
				Directory temp = (Directory) sub;
				recSearch(temp, name, type, stdout);
			}
		}
	}

	/**
	 * Checks if parameters are valid for function call
	 *
	 * @param shell
	 *            The JShell the command is to be performed on
	 * @param parameters
	 *            The parameters from the interpreter the command is to work
	 *            with
	 * @return -1 if it has errors and the index of "-type" in parameters if it
	 *         has no errors.
	 */
	private static int isValid(JShell shell, String[] parameters) {
		if (parameters.length < 6) {
			PrintError.reportError(shell, "search",
					"Invalid number of arguments.");
			return -1;
		}
		int tIndex = 0;
		int nIndex = 0;
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].equals("-type")) {
				tIndex = i;
			} else if (parameters[i].equals("-name")) {
				nIndex = i;
			}
		}
		if (tIndex == 0 || nIndex == 0) {
			PrintError.reportError(shell, "search",
					"-type and -name are required parameters.");
			return -1;
		}
		if (tIndex > nIndex) {
			PrintError.reportError(shell, "search",
					"-type should come before -name.");
			return -1;
		}
		if (nIndex + 1 == parameters.length - 1) {
			if (!parameters[tIndex + 1].equals("f")
					&& !parameters[tIndex + 1].equals("d")) {
				PrintError.reportError(shell, "search",
						"Please specify search type after -type [f/d]");
				return -1;
			}
			if (!parameters[nIndex + 1].matches("\".*\"")) {
				PrintError.reportError(shell, "search",
						"Please enclose name with double quotes (\"\")");
			}
		} else {
			PrintError.reportError(shell, "search",
					"Please specify only one name");
			return -1;
		}
		return tIndex;
	}
}
