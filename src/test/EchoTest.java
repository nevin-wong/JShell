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

import driver.Echo;
import driver.File;
import driver.JShell;
import driver.PushDirOntoStack;
import driver.Storage;
import driver.Tree;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EchoTest {

	JShell shell;
	File stdOutFile;

	/** Used to test print statements */
	private final PrintStream printed = System.out;
	private final ByteArrayOutputStream consoleStreamCaptor = 
			new ByteArrayOutputStream();

	/**
	 * Set up a new JShell and a File to send StdOut to.
	 */
	@Before
	public void setUp() {
		shell = new JShell();
		stdOutFile = new File("file", "", null);
		System.setOut(new PrintStream(consoleStreamCaptor));
	}

	/**
	 * Destroy the only reference to the storage system
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		System.setOut(printed);
	}

	/**
	 * Test to see if the right manual is returned for getManual
	 */
	@Test
	public void testGetManual() {
		String manual = new String();
		manual = Echo.getManual();
		assertEquals(manual, "echo STRING [> OUTFILE]\n"
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
				+ "appends to OUTFILE instead of overwrites");
	}

	@Test
	public void testPerformOutcomeOneParam() {
		String[] parameters = {"echo"};
		Echo.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("echo: invalid number of parameters",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testPerformOutcomeThreeParams() {
		String[] parameters = {"echo", "this", "that"};
		Echo.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("echo: invalid number of parameters",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testPerformOutcomeEchoingAWord() {
		String[] parameters = {"echo", "\"PHLEBOTINUM\""};
		Echo.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("PHLEBOTINUM", stdOutFile.getContents());
	}

	@Test
	public void testPerformOutcomeEchoingAPhraseWithWhitespace() {
		String[] parameters = {"echo",
				"\"    My name is Amazon Echo and here is some "
						+ "space            \""};
		Echo.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("    My name is Amazon Echo and here is some space    "
				+ "        ", stdOutFile.getContents());
	}
}
