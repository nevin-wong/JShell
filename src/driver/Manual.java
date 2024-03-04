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

/**
 * The Manual command prints documentation for one specific command so a user
 * knows how to use it.
 */

public class Manual extends ShellCommand {

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
	 * This is quite meta, provides the manual for how to use this command
	 * 
	 * @return The manual
	 */
	public static String getManual() {
		return "man CMD\n" + "Print documentation for CMD(s)";
	}

	/**
	 * Tell the shell to print the desired manual for a given command
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
			PrintError.reportError(shell, "man",
					"Invalid number of arguments.");
			return;
		}
		int i;
		String command = parameters[1];
		if (shell.getCmdToClass().containsKey(command)) {
			try {
				Method getMan = shell.getCmdToClass().get(command)
						.getDeclaredMethod("getManual");
				try {
					String manual = (String) getMan.invoke(null);
					stdout.sendLine(manual);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		} else {
			PrintError.reportError(shell, "man",
					command + " is not a valid command.");
			return;
		}
		stdout.closeStream();
	}
}
