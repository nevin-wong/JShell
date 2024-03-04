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

package test;

import driver.File;
import driver.JShell;
import driver.ShellCommand;

/**
 * Mock command that produces StdOut that is referred to as cmd1
 */
public class MockStdOutCommand extends ShellCommand {

	public static JShell receivedShell;
	public static String receivedParams[];
	public static int receivedOutputType;
	public static File receivedOutputFile;

	public static boolean producesStdOut() {
		return true;
	}

	public static String getManual() {
		return "cmd1 \nI am a command that produces StdOut.";
	}

	public static void performOutcome(JShell shell, String[] parameters,
			int outputType, File outputFile) {
		shell.println("I am cmd1 and I have just been called.");
		receivedShell = shell;
		receivedParams = parameters;
		receivedOutputType = outputType;
		receivedOutputFile = outputFile;
	}

}
