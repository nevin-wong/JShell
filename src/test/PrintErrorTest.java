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
import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import driver.File;
import driver.JShell;
import driver.PrintError;
import driver.Storage;

public class PrintErrorTest {

	JShell shell;
	File stdOutFile;

	/** Used to test print statements */
	private final PrintStream printed = System.out;
	private final ByteArrayOutputStream consoleStreamCaptor = new ByteArrayOutputStream();

	/**
	 * Set up a new JShell and a File to send StdOut to.
	 */
	@Before
	public void setUp() {
		shell = new JShell();
		System.setOut(new PrintStream(consoleStreamCaptor));
	}

	@After
	public void tearDown() throws Exception {
		System.setOut(printed);
	}

	@Test
	public void testReportErrorJShellString() {
		PrintError.reportError(shell, "Error: we have an error.");
		assertEquals("Error: we have an error.",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testReportErrorJShellStringString() {
		PrintError.reportError(shell, "cmdname",
				"this command has reported an error.");
		assertEquals("cmdname: this command has reported an error.",
				consoleStreamCaptor.toString().trim());
	}
	
	@Test
	public void testReportErrorJShellStringString2() {
		PrintError.reportError(shell, "cmd",
				"this other command has reported an error.");
		assertEquals("cmd: this other command has reported an error.",
				consoleStreamCaptor.toString().trim());
	}

}
