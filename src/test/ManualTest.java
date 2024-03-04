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
import driver.Manual;
import driver.Storage;

public class ManualTest {

	JShell shell;
	File stdOutFile;

	/** Used to test print statements */
	private final PrintStream printed = System.out;
	private final ByteArrayOutputStream consoleStreamCaptor = 
			new ByteArrayOutputStream();

	/**
	 * Set up a new JShell and a File to send StdOut to.
	 * 
	 * @throws ClassNotFoundException
	 */
	@Before
	public void setUp() throws ClassNotFoundException {
		shell = new JShell();
		stdOutFile = new File("file", "", null);
		shell.getCmdToClass().put("cmd1",
				Class.forName("test.MockStdOutCommand"));
		shell.getCmdToClass().put("cmd2",
				Class.forName("test.MockNonStdOutCommand"));
		System.setOut(new PrintStream(consoleStreamCaptor));
	}

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
		manual = Manual.getManual();
		assertEquals(manual, "man CMD\n" + "Print documentation for CMD(s)");
	}

	@Test
	public void testPerformOutcomeForCmd1() {
		String[] parameters = {"man", "cmd1"};
		Manual.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals(stdOutFile.getContents(),
				"cmd1 \nI am a command that produces StdOut.");
	}

	@Test
	public void testPerformOutcomeForCmd2() {
		String[] parameters = {"man", "cmd2"};
		Manual.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals(stdOutFile.getContents(),
				"cmd2 \nI am a command that does not produce StdOut.");
	}

	/**
	 * Test getting the manual for man.
	 */
	@Test
	public void testPerformOutcomeForMan() {
		String[] parameters = {"man", "man"};
		Manual.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals(stdOutFile.getContents(),
				"man CMD\n" + "Print documentation for CMD(s)");
	}

	/**
	 * Test getting the manual for cp.
	 */
	@Test
	public void testPerformOutcomeForCp() {
		String[] parameters = {"man", "cp"};
		Manual.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals(stdOutFile.getContents(), "cp OLDPATH NEWPATH\n"
				+ "Copy item OLDPATH to NEWPATH. Both OLD-PATH and NEWPATH may "
				+ "be " + "relative to \nthe current directory or may be "
				+ "full paths. If "
				+ "NEWPATH is adirectory, copy \nthe item into the directory.");
	}

	/**
	 * Test that getting the manual that doesn't exist will not work.
	 */
	@Test
	public void testPerformOutcomeForNonExistentMethod() {
		String[] parameters = {"man", "abbas"};
		Manual.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals(stdOutFile.getContents(), "");
		assertEquals("man: abbas is not a valid command.",
				consoleStreamCaptor.toString().trim());
	}

	/**
	 * Test that getting the manuals for multiple commands will not work.
	 */
	@Test
	public void testPerformOutcomeForMultipleCommands() {
		String[] parameters = {"man", "cp", "history"};
		Manual.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals(stdOutFile.getContents(), "");
		assertEquals("man: Invalid number of arguments.",
				consoleStreamCaptor.toString().trim());
	}

	/**
	 * Test that just entering man without any other arguments will not work.
	 */
	@Test
	public void testPerformOutcomeForNoCommands() {
		String[] parameters = {"man"};
		Manual.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals(stdOutFile.getContents(), "");
		assertEquals("man: Invalid number of arguments.",
				consoleStreamCaptor.toString().trim());
	}
}
