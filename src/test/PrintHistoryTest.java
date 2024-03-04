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

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Before;
import org.junit.Test;

import driver.*;

public class PrintHistoryTest {
	JShell shell;
	Directory root;
	File stdOutFile;

	/** Used to test print statements */
	private final PrintStream printed = System.out;
	private final ByteArrayOutputStream consoleStreamCaptor = 
			new ByteArrayOutputStream();

	@Before
	public void setUp() {
		shell = new JShell();
		root = shell.getRootDir();
		stdOutFile = new File("file", "", null);
		System.setOut(new PrintStream(consoleStreamCaptor));
	}

	@Test
	public void testGetManual() {
		assertEquals("history [number] \n"
				+ "This command will print out recent "
				+ "commands, one command per " + "line. i.e. \r\n"
				+ "    1. cd ..\r\n" + "    2. mkdir textFolder\r\n"
				+ "    3. echo �Hello World�\r\n" + "    4. fsjhdfks\r\n"
				+ "    5. history\r\n"
				+ "The above output from history has two columns. The first "
				+ "column is\r\n" + "numbered such that the line with the "
				+ "highest number is the most recent command.\r\n"
				+ "The most recent command is history. The "
				+ "second column contains the actual\r\n"
				+ "command. Note: Your output should also contain as output "
				+ "any syntactical errors\r\n"
				+ "typed by the user as seen on line 4.\n"
				+ "We can truncate the output by specifying"
				+ " a number (>=0) after the command.\r\n"
				+ "For instance, if we want to only see the "
				+ "last 3 commands typed, we can type the\r\n"
				+ "following on the command line:\n" + "    history 3\r\n"
				+ "And the output will be as follows:\r\n"
				+ "    4. fsjhdfks\r\n" + "    5. history\r\n"
				+ "    6. history 3", driver.PrintHistory.getManual());
	}

	@Test
	public void testPerformOutComeWithoutNumber() {
		shell.addCom("mkdir hello");
		shell.addCom("hello");
		shell.addCom("history");
		String[] parameters = {"history"};
		driver.PrintHistory.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("1. mkdir hello\n2. hello\n3. history",
				stdOutFile.getContents());
	}

	@Test
	public void testPerformOutComeWithNumber() {
		shell.addCom("mkdir hello");
		shell.addCom("hello");
		shell.addCom("history 1");
		String[] parameters = {"history", "1"};
		driver.PrintHistory.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("3. history 1\n", stdOutFile.getContents());
	}

	@Test
	public void testPerformOutComeWithInvalNumArgs() {
		shell.addCom("mkdir hello");
		shell.addCom("hello");
		shell.addCom("history 1");
		String[] parameters = {"history", "1", "202"};
		driver.PrintHistory.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("history: Invalid number of arguments.",
				consoleStreamCaptor.toString().trim());
	}

}
