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
 * The Echo command is used by the user mainly to manipulate a File's contents
 * by overwriting them or appending to them. It can also print user input
 * strings to their JShell.
 */

public class Echo extends ShellCommand {

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
		return "echo STRING [> OUTFILE]\n"
				+ "If OUTFILE is not provided, print STRING on the shell.\n"
				+ "Otherwise, put STRING into file OUTFILE.\n"
				+ "STRING is a string of characters "
				+ "surrounded by double quotation marks.\n"
				+ "This creates a new file if "
				+ "OUTFILE does not exists and erases "
				+ "the old contents if OUTFILE already exists.\n"
				+ "In either case, the only "
				+ "thing in OUTFILE should be STRING.\n"
				+ "echo STRING >> OUTFILE\n" + "Like the previous command, but"
				+ "appends to OUTFILE instead of overwrites";
	}

	/**
	 * Counts number of '>' in parameters
	 * 
	 * @param parameters
	 *            The parameters to be checked
	 * @return The number of instances of '>'
	 */
	private static int numArrow(String[] parameters) {
		int counter = 0;
		boolean passedQuote = false;
		for (int i = 1; i < parameters.length; i++) {
			for (int c = 0; c < parameters[i].length(); c++) {
				if (parameters[i].charAt(c) == '\"' && c != 0) {
					passedQuote = true;
				}
				if (passedQuote) {
					if (parameters[i].charAt(c) == '>') {
						counter += 1;
					}
				}
			}
		}
		return counter;
	}

	/**
	 * Parses input parameters into 2 pieces "String" = [0] and "FilePath+Name"
	 * = [1]
	 * 
	 * @param parameters
	 *            The parameters to be parsed
	 * @return The parameters, now parsed
	 */
	private static String[] parseParameters(String[] parameters) {
		if (parameters.length == 2) {
			return new String[]{parameters[1], ""};
		}
		if (parameters.length == 4) {
			return new String[]{parameters[1], parameters[3]};
		}
		for (int i = 1; i < parameters.length;) {
			if (parameters[i].charAt(parameters[i].length() - 1) == '>'
					&& i == 1) {
				parameters[i] = parameters[i].substring(0,
						parameters[i].length() - 1);
				if (parameters[i].length() == 0) {
					i++;
				}
			} else if (parameters[i].charAt(0) == '>' && i == 2) {
				parameters[i] = parameters[i].substring(1);
				if (parameters[i].length() == 0) {
					i++;
				}
			} else {
				i++;
			}
		}
		return new String[]{parameters[1], parameters[2]};
	}

	/**
	 * Takes in parameters to check if they are formatted correctly. Prints an
	 * error message if they are returns otherwise
	 * 
	 * @param shell
	 *            The JShell that is in use
	 * @param parsedParams
	 *            The params returned by parseParameters
	 * @param numArrow
	 *            The number of ">"
	 * @return true if there is an error in the parameters and false otherwise
	 */
	private static boolean errorHandle(JShell shell, String[] parsedParams,
			int numArrow, Directory dir) {
		if (numArrow != 0) {
			if (dir == null) {
				PrintError.reportError(shell, "echo",
						"directory does not exist");
				return true;
			}
		}
		// Check for empty string
		if (parsedParams[0].length() <= 1) {
			PrintError.reportError(shell, "echo", "No string attached; string "
					+ "must be surrounded by \"\"");
			return true;
		}
		// Checks for string to be surrounded by "."
		if (parsedParams[0].charAt(0) != '\"' || parsedParams[0]
				.charAt(parsedParams[0].length() - 1) != '\"') {
			PrintError.reportError(shell, "echo",
					"String must be surrounded " + "by \"\"");
			return true;
		}
		// Checks for empty file name
		if (numArrow != 0) {
			if (parsedParams[1].equals("")) {
				PrintError.reportError(shell, "echo",
						"FileName/OutFile cannot be empty");
				return true;
			}
		}
		// Check for string split up
		if (parsedParams[1].length() != 0) {
			if (parsedParams[0].charAt(parsedParams[0].length() - 1) == '\"'
					&& parsedParams[1].charAt(0) == '\"') {
				PrintError.reportError(shell, "echo",
						"\" is an invalid string character");
				return true;
			}
		}
		// Check for double quotes in string
		if (parsedParams[0].substring(1, parsedParams[0].length() - 1)
				.contains("\"")) {
			PrintError.reportError(shell, "echo",
					"\" is an invalid string character");
			return true;
		}
		return false;
	}

	/**
	 * Cycles through the path given and returns the end directory of the path
	 * 
	 * @param shell
	 *            The JShell that is in use
	 * @param filePath
	 *            The file path provided
	 * @return The end directory of the path
	 */
	private static Directory cycleDir(JShell shell, String filePath) {
		Directory currDir = shell.getCurrentDir();
		if (filePath.indexOf("/") == 0) {
			if (filePath.equals("/")) {
				PrintError.reportError(shell, "echo",
						"filename must be provided" + filePath);
			} else {
				currDir = shell.getRootDir();
				filePath = filePath.substring(1);
			}
		}
		if (StorageUnit.hasForbidChar(
				filePath.split("/")[filePath.split("/").length - 1])) {
			PrintError.reportError(shell, "echo",
					"FileName contains forbidden character(s): " + filePath
							.split("/")[filePath.split("/").length - 1]);
			return null;
		}
		Path newPath = new Path(filePath);
		return (Directory) newPath.verifyPath(shell, true, currDir);
	}

	/**
	 * Tell the JShell to either print a given string to the command line,
	 * overwrite a specific file in Storage or append to a specific file in
	 * Storage.
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
		if (parameters.length != 2) {
			PrintError.reportError(shell, "echo",
					"invalid number of parameters");
			return;
		}
		int numArrow = numArrow(parameters);
		String[] parsedParams = parseParameters(parameters);
		Directory dir = cycleDir(shell, parsedParams[1]);
		String fileName = parsedParams[1]
				.split("/")[parsedParams[1].split("/").length - 1];
		int index = 0;
		if (errorHandle(shell, parsedParams, numArrow, dir)) {
			return;
		}
		if (dir != null) {
			index = dir.containsFile(fileName);
		}
		parsedParams[0] = parsedParams[0].substring(1,
				parsedParams[0].length() - 1);
		if (numArrow == 0) { // Print string to shell command
			stdout.sendLine(parsedParams[0]);
			stdout.closeStream();
			return;
		}
		if (dir.isSubDir(fileName) != -1) {
			PrintError.reportError(shell, "echo",
					"There exists directory of the same name");
			return;
		}
		if (index != -1) { // File does not exist
			File nf = (File) dir.getFile(index);
			if (numArrow == 1) {// Overwrite file with string
				nf.overwrite(parsedParams[0]);
			} else {// Append to file with string
				nf.append(parsedParams[0]);
			}
		} else { // File exists
			File nf = new File(fileName, parsedParams[0], dir);
			dir.addFile(nf);
		}
	}
}
