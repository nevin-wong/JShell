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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LoadJShell extends ShellCommand {

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
		return "loadJShell localFilePath\n"
				+ "Loads a previously saved JShell session onto a fresh "
				+ "JShell sessions.\n"
				+ "This command only works on fresh JShell sessions.";
	}

	/**
	 * 
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
			PrintError.reportError(shell, "loadJShell",
					"Invalid number of arguments.");
			return;
		}
		if (shell.getComHis().getSize() <= 1) {
			try {
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(parameters[1]));
				JShell newShell = (JShell) in.readObject();
				shell.updateShell(newShell);
				in.close();
			} catch (IOException e) {
				System.out.println(
						"File not found run saveJShell in another session "
								+ "first");
			} catch (ClassNotFoundException e) {
				System.err.println(e.getMessage());
			}
		} else {
			PrintError.reportError(shell,
					"loadJShell: there are unsaved changes in this session,"
							+ "\nstart a fresh shell to load a previously "
							+ "saved session.");
			return;
		}
	}

}
