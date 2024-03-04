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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import driver.File;
import driver.JShell;
import driver.StdOut;

public class StdOutTest {

	JShell shell;
	StdOut stdoutPrint, stdoutAppend, stdoutOverwrite;
	File appendHere, overwriteHere;

	/** Used to test print statements */
	private final PrintStream printed = System.out;
	private ByteArrayOutputStream consoleStreamCaptor = new ByteArrayOutputStream();

	@Before
	public void setUp() throws Exception {
		shell = new JShell();
		appendHere = new File("appendHere", "hello", shell.getCurrentDir());
		overwriteHere = new File("overwriteHere", "hello",
				shell.getCurrentDir());
		shell.getCurrentDir().addFile(appendHere);
		shell.getCurrentDir().addFile(overwriteHere);
		stdoutPrint = new StdOut(shell, 0, null);
		stdoutOverwrite = new StdOut(shell, 1, overwriteHere);
		stdoutAppend = new StdOut(shell, 2, appendHere);
		System.setOut(new PrintStream(consoleStreamCaptor));
	}

	@After
	public void tearDown() throws Exception {
		System.setOut(printed);
	}

	@Test
	public void testSendLineForPrinting() {
		stdoutPrint.sendLine("Here's a line.");
		assertEquals("Here's a line.", consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testSendLineForAppending() {
		stdoutAppend.sendLine("Here's a line.");
		assertEquals("helloHere's a line.\n", appendHere.getContents());
	}

	@Test
	public void testSendLineForAppendingTwice() {
		stdoutAppend.sendLine("Here's a line.");
		assertEquals("helloHere's a line.\n", appendHere.getContents());
		stdoutAppend.sendLine("Here's another.");
		assertEquals("helloHere's a line.\nHere's another.\n",
				appendHere.getContents());
	}

	@Test
	public void testSendLineForOverwriting() {
		stdoutOverwrite.sendLine("Here's a line.");
		assertEquals("Here's a line.\n", overwriteHere.getContents());
		stdoutOverwrite = new StdOut(shell, 1, overwriteHere);
		stdoutOverwrite.sendLine("Here's another.");
		assertEquals("Here's another.\n", overwriteHere.getContents());
	}

	@Test
	public void testLineForPrinting() {
		stdoutPrint.send("Here's a line.");
		assertEquals("Here's a line.", consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testLineForAppending() {
		stdoutAppend.send("Here's a line.");
		assertEquals("helloHere's a line.", appendHere.getContents());
	}

	@Test
	public void testSendForAppendingTwice() {
		stdoutAppend.send("Here's a line.");
		assertEquals("helloHere's a line.", appendHere.getContents());
		stdoutAppend.send("Here's another.");
		assertEquals("helloHere's a line.Here's another.",
				appendHere.getContents());
	}

	@Test
	public void testSendForOverwriting() {
		stdoutOverwrite.send("Here's a line.");
		assertEquals("Here's a line.", overwriteHere.getContents());
		stdoutOverwrite = new StdOut(shell, 1, overwriteHere);
		stdoutOverwrite.send("Here's another.");
		assertEquals("Here's another.", overwriteHere.getContents());
	}

	/**
	 * Test that this removes the last newline
	 */
	@Test
	public void testCloseStreamNewlineAtTheEnd() {
		overwriteHere.overwrite("Stuff with a newline at the end\n");
		stdoutOverwrite.closeStream();
		assertEquals("Stuff with a newline at the end",
				overwriteHere.getContents());
	}

	/**
	 * Test that this does not touch the last character
	 */
	@Test
	public void testCloseStreamNoNewlineAtTheEnd() {
		overwriteHere.overwrite("Content with no newline at the end");
		stdoutOverwrite.closeStream();
		assertEquals("Content with no newline at the end",
				overwriteHere.getContents());
	}

	/**
	 * Test that this does nothing, i.e. program does not crash
	 */
	@Test
	public void testCloseStreamNoContent() {
		overwriteHere.overwrite("");
		stdoutOverwrite.closeStream();
		assertEquals("", overwriteHere.getContents());
	}
}
