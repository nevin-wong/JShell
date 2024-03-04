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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * The Interpreter interprets user commands from the JShell to perform the
 * user's desired outcomes
 */

public class Interpreter {

	/**
	 * Splits a given user command into an array of strings. Phrases written in
	 * quotes ("") will remain exactly as is, otherwise any spaces will be
	 * interpreted as a gap in between parameters. The last parameter will be
	 * the string containing information regarding redirection.
	 * 
	 * For example, "command "something in quotes" random>>file" becomes
	 * {"command", ""something in quotes"", "random", ">>file"}.
	 * 
	 * @param userCommand
	 *            A command the user has input into the JShell
	 * @return The array of strings ready to be passed on to a ShellCommand
	 */
	private static String[] splitCmdIntoParams(String userCommand) {
		String[] parameters = new String[0];
		String outPath = "";
		int i;
		boolean quoteMode = false;
		String currString = "";
		for (i = 0; i < userCommand.length(); i++) {
			if (quoteMode) {
				if (userCommand.charAt(i) == '"') {
					currString = currString + userCommand.charAt(i);
					quoteMode = false;
					parameters = extendArray(parameters, currString);
					currString = "";
				} else {
					currString = currString + userCommand.charAt(i);
				}
			} else {
				if (userCommand.charAt(i) == '"') {
					if (currString != "") {
						parameters = extendArray(parameters, currString);
						currString = "";
					}
					quoteMode = true;
					currString = currString + userCommand.charAt(i);
				} else if (userCommand.charAt(i) == '>') {
					outPath = userCommand.substring(i);
					break;
				} else if (userCommand.charAt(i) != ' ') {
					currString = currString + userCommand.charAt(i);
				} else if (userCommand.charAt(i) == ' ' && currString != "") {
					parameters = extendArray(parameters, currString);
					currString = "";
				}
			}
		}
		if (currString != "") {
			parameters = extendArray(parameters, currString);
			currString = "";
		}
		parameters = extendArray(parameters, outPath);
		return parameters;
	}

	/**
	 * Helper function for the above static method to extend the array of
	 * parameters.
	 * 
	 * @param array
	 *            The array to extend
	 * @param finalElement
	 *            The final String to add
	 * @return The new extended array
	 */
	private static String[] extendArray(String[] array, String finalElement) {
		String[] newArray = Arrays.copyOf(array, array.length + 1);
		newArray[newArray.length - 1] = finalElement;
		return newArray;
	}

	/**
	 * Helper function for interpret get the output type of the redirection.
	 * 
	 * @param redirectInfo
	 *            The string that contains the redirection part of the command
	 * @return 0 for print to the shell, 1 for overwrite a file , 2 for append
	 *         to a file
	 */
	private static int getOutputType(String redirectInfo) {
		if (redirectInfo == "") {
			return 0;
		}
		int type = 1;
		if (redirectInfo.substring(1).stripLeading().charAt(0) == '>') {
			type = 2; // i.e. find another > after the first >
		}
		return type;
	}

	/**
	 * Helper function for interpret to determine whether a given string
	 * corresponds to a valid command.
	 * 
	 * @param shell
	 *            The JShell whose command to Class HashMap is to be checked to
	 *            see if the command is valid in it
	 * @param cmd
	 *            The command to be checked
	 * @return Whether the given string is a valid command
	 */
	private static boolean isValidCmd(JShell shell, String cmd) {
		return shell.getCmdToClass().containsKey(cmd);
	}

	/**
	 * Calls the appropriate commands to carry out the user's desired outcomes
	 * according to a given command, according to the JShell's command to Class
	 * HashMap
	 * 
	 * @param userCommand
	 *            A command the user has input into the JShell
	 * @param shell
	 *            The specific instance of JShell the user is using
	 */
	public static void interpret(String userCommand, JShell shell) {
		File outFile = null;
		String parameters[] = Interpreter.splitCmdIntoParams(userCommand);
		if (parameters.length == 1) {
			PrintError.reportError(shell, "Error: no command entered.");
			return;
		}
		if (!isValidCmd(shell, parameters[0])) {
			PrintError.reportError(shell,
					"Error: " + parameters[0] + " is not a valid command.");
			return;
		}
		String redirectInfo = parameters[parameters.length - 1];
		parameters = Arrays.copyOf(parameters, parameters.length - 1);
		int outputType = getOutputType(redirectInfo);
		if (outputType != 0 && (producesStdOut(shell, parameters[0]))) {
			redirectInfo = redirectInfo.replace(">", "").strip();
			Path path = new Path(redirectInfo);
			Directory dir = getFinalDir(path, shell);
			if (dir == null) {
				PrintError.reportError(shell,
						"Error: that is not a valid directory "
								+ "to redirect to.");
				return;
			}
			String filename = path
					.getPathElements()[path.getPathElements().length - 1];
			int index = dir.containsFile(filename);
			if (dir.isSubDir(filename) != -1) {
				PrintError.reportError(shell,
						"Error: There is a directory with the same name.");
				return;
			}
			if (StorageUnit.hasForbidChar(filename)) {
				PrintError.reportError(shell, "Error: file name " + filename
						+ " has forbidden characters.");
				return;
			}
			if (index != -1) { // File exists
				outFile = (File) dir.getFile(index);
			} else { // File does not exist
				outFile = new File(filename, "", dir);
				dir.addFile(outFile);
			}
		}
		callCommand(parameters, outputType, outFile, shell);
	}

	/**
	 * Helper for interpret, determines the final directory of a given path.
	 * 
	 * @param path
	 *            The path to check
	 * @param shell
	 *            The JShell to use for its current directory
	 * @return The final directory of it is a valid path to one, null otherwise.
	 */
	private static Directory getFinalDir(Path path, JShell shell) {
		if (path.getPath().equals("/")) {
			return null; // Disallow redirection to "/"
		}
		Directory dir = shell.getCurrentDir();
		if (path.isAbsolute()) { // path is absolute
			dir = shell.getRootDir();
		}
		dir = (Directory) path.verifyPath(shell, true, dir);
		if (dir == null) { // Check if the second last element is valid dir
			if (path.getPathElements()[path.getPathElements().length - 2]
					.equals(".")) {
				dir = shell.getCurrentDir();
			} else if (path.getPathElements()[path.getPathElements().length - 2]
					.equals("..")) {
				dir = shell.getCurrentDir().getParentDir();
			} else if (path.getPathElements().length == 2
					&& path.getPath().startsWith("/")) {
				dir = shell.getRootDir();
			}
		}
		return dir;
	}

	/**
	 * Helper for interpret, checks if a command produces StdOut. Used to
	 * determine whether to create a new file.
	 * 
	 * @param shell
	 *            The JShell whose command to Class HashMap is to be used to
	 *            determine its Class
	 * @param cmd
	 *            The string representation of the command
	 * @return Whether or not this command produces StdOut
	 */
	private static boolean producesStdOut(JShell shell, String cmd) {
		try {
			Method method = shell.getCmdToClass().get(cmd)
					.getDeclaredMethod("producesStdOut");
			try {
				return (boolean) method.invoke(null);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Helper for interpret, takes in parameters (without redirection part) and
	 * calls the appropriate command using the shell's command to Class HashMap
	 * 
	 * @param parameters
	 *            The parameters (with the redirection info stripped off)
	 * @param outputType
	 *            0 for printing to the shell, 1 for overwriting a File and, 2
	 *            for appending to a File
	 * @param outFile
	 *            If outputType is 1 or 2, this is the the File in question
	 * @param shell
	 *            The JShell that sent the call to interpret
	 */
	private static void callCommand(String[] parameters, int outputType,
			File outFile, JShell shell) {
		String command = parameters[0]; // The first word is the command
		try {
			Method perform = shell.getCmdToClass().get(command)
					.getDeclaredMethod("performOutcome", shell.getClass(),
							String[].class, int.class, File.class);
			try {
				perform.invoke(null, shell, parameters, outputType, outFile);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
}