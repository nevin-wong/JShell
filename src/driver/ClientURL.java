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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ClientURL extends ShellCommand {

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
		return "curl URL\n"
				+ "Retrieve the file at that URL and add it to the current "
				+ "working directory.\n" + "Example1:\n"
				+ "curl http://www.cs.cmu.edu/spok/grimmtmp/073.txt\n"
				+ "Will get the contents of the file, "
				+ "i.e. 073.txt and create a "
				+ "file called 073.txt with the contents in \nthe current "
				+ "working directory.\n" + "Example2:\n"
				+ "curl http://www.ub.edu/gilcub/SIMPLE/simple.html\n"
				+ "Will get the contents of the file, i.e. simple.html "
				+ "(raw HTML) and create a file called simple.html with the\n"
				+ "contents in the current working directory.";
	}

	/**
	 * Creates a file given the address and content inside the current working
	 * directory specified in shell and returns 1 if file was created and 0 if
	 * there was an error creating the file.
	 *
	 * @param address
	 *            The String array containing the url that was split by "/"
	 * @param content
	 *            The String that contains the contents for the file to be
	 *            created
	 * @param shell
	 *            The JShell the command is to be performed on
	 *
	 * @return An integer 1 if new file is created, 0 if there is an error
	 *         creating the file and -1 if file with same name already exits
	 */
	private static int createFile(String[] address, String content,
			JShell shell) {
		Directory currDir = shell.getCurrentDir();
		String fileName = address[address.length - 1];
		fileName = fileName.replace(".", "");
		if (currDir.containsFile(fileName) != -1) {
			PrintError.reportError(shell, "curl",
					"Filename already exists: " + fileName);
			return -1;
		}
		File nFile = new File(fileName, content, currDir);
		if (nFile != null) {
			currDir.addFile(nFile);
			return 1;
		}
		return 0;
	}

	/**
	 * Retrieves the file at a given URL and adds it to the JShell's current
	 * working directory.
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
			PrintError.reportError(shell, "curl",
					"Invalid number of arguments.");
			return;
		}
		String content = "";
		try {
			URL url = new URL(parameters[1]);
			Scanner urlInput = new Scanner(url.openStream());
			while (urlInput.hasNextLine()) {
				content += urlInput.nextLine();
				if (urlInput.hasNextLine()) {
					content += "\n";
				}
			}
			urlInput.close();
		} catch (MalformedURLException e) {
			PrintError.reportError(shell, "curl", "Could not reach this URL.");
			return;
		} catch (IOException e) {
			PrintError.reportError(shell, "curl",
					"Could not read from this URL.");
			return;
		}
		String[] urlElements = parameters[1].split("/");
		int success = createFile(urlElements, content, shell);
		if (success == 0) {
			PrintError.reportError(shell, "curl", "Unable to create file.");
		}
	}
}
