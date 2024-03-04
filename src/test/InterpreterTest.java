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

import driver.JShell;
import driver.Storage;
import driver.File;
import driver.Interpreter;

public class InterpreterTest {

	JShell shell;

	/** Used to test print statements */
	private final PrintStream printed = System.out;
	private final ByteArrayOutputStream consoleStreamCaptor = 
			new ByteArrayOutputStream();

	@Before
	public void setUp() throws Exception {
		shell = new JShell();
		// Add two mock commands for testing to the HashTable
		shell.getCmdToClass().put("cmd1",
				Class.forName("test.MockStdOutCommand"));
		shell.getCmdToClass().put("cmd2",
				Class.forName("test.MockNonStdOutCommand"));
		System.setOut(new PrintStream(consoleStreamCaptor));
	}

	@After
	public void tearDown() throws Exception {
		System.setOut(printed);
		Field field = Storage.class.getDeclaredField("onlyReference");
		field.setAccessible(true);
		field.set(null, null);
	}

	/**
	 * User entered command contains a command that is not in the shell's
	 * HashMap
	 */
	@Test
	public void testInterpretGibberishCommand() {
		Interpreter.interpret("cmd3 blaaa blaaaa", shell);
		assertEquals("Error: cmd3 is not a valid command.",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testInterpretActualGibberishCommand() {
		Interpreter.interpret("sdofijsd  okay  > yes", shell);
		assertEquals("Error: sdofijsd is not a valid command.",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testInterpretNothingEntered() {
		Interpreter.interpret("", shell);
		assertEquals("Error: no command entered.",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testInterpretJustWhitespaceEntered() {
		Interpreter.interpret("                    ", shell);
		assertEquals("Error: no command entered.",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testInterpretJustRedirectionEntered() {
		Interpreter.interpret("> yep", shell);
		assertEquals("Error: no command entered.",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testInterpretUserCommandHasSpaces() {
		Interpreter.interpret("   cmd1      so   here  "
				+ "  is      a load of     whitespace        ", shell);
		String want[] = {"cmd1", "so", "here", "is", "a", "load", "of",
				"whitespace"};
		assertArrayEquals(MockStdOutCommand.receivedParams, want);
	}

	@Test
	public void testInterpretUserCommandHasVariousLengthsOfWhiteSpace() {
		Interpreter.interpret(
				"   cmd2      so   here  "
						+ "  is a lot        offfff   whitespace        ",
				shell);
		String want[] = {"cmd2", "so", "here", "is", "a", "lot", "offfff",
				"whitespace"};
		assertArrayEquals(MockNonStdOutCommand.receivedParams, want);
	}

	@Test
	public void testInterpretUserCommandHasQuotes() {
		Interpreter
				.interpret("cmd1 \"quotes     like   this\" need to be parsed "
						+ "\"differently\"", shell);
		String want[] = {"cmd1", "\"quotes     like   this\"", "need", "to",
				"be", "parsed", "\"differently\""};
		assertArrayEquals(MockStdOutCommand.receivedParams, want);
	}

	@Test
	public void testInterpreterOverwritingAFileSpacesBeforeAndAfter() {
		File file = new File("fyle", "", shell.getCurrentDir());
		shell.getCurrentDir().addFile(file);
		Interpreter.interpret("cmd1 param param > fyle", shell);
		String want[] = {"cmd1", "param", "param"};
		assertArrayEquals(MockStdOutCommand.receivedParams, want);
		assertEquals(MockStdOutCommand.receivedOutputType, 1);
		assertEquals(MockStdOutCommand.receivedOutputFile, file);
	}

	@Test
	public void testInterpreterOverwritingAFileNoSpaces() {
		File file = new File("fyle", "", shell.getCurrentDir());
		shell.getCurrentDir().addFile(file);
		Interpreter.interpret("cmd1 param param>fyle", shell);
		String want[] = {"cmd1", "param", "param"};
		assertArrayEquals(MockStdOutCommand.receivedParams, want);
		assertEquals(MockStdOutCommand.receivedOutputType, 1);
		assertEquals(MockStdOutCommand.receivedOutputFile, file);
	}

	@Test
	public void testInterpreterOverwritingAFileSpaceBefore() {
		File file = new File("fyle", "", shell.getCurrentDir());
		shell.getCurrentDir().addFile(file);
		Interpreter.interpret("cmd1 param param >fyle", shell);
		String want[] = {"cmd1", "param", "param"};
		assertArrayEquals(MockStdOutCommand.receivedParams, want);
		assertEquals(MockStdOutCommand.receivedOutputType, 1);
		assertEquals(MockStdOutCommand.receivedOutputFile, file);
	}

	@Test
	public void testInterpreterOverwritingAFileSpaceAfter() {
		File file = new File("fyle", "", shell.getCurrentDir());
		shell.getCurrentDir().addFile(file);
		Interpreter.interpret("cmd1 param param> fyle", shell);
		String want[] = {"cmd1", "param", "param"};
		assertArrayEquals(MockStdOutCommand.receivedParams, want);
		assertEquals(MockStdOutCommand.receivedOutputType, 1);
		assertEquals(MockStdOutCommand.receivedOutputFile, file);
	}

	@Test
	public void testInterpreterOverwritingAFileLotsSpaces() {
		File file = new File("fyle", "", shell.getCurrentDir());
		shell.getCurrentDir().addFile(file);
		Interpreter.interpret("cmd1 param param     >      fyle", shell);
		String want[] = {"cmd1", "param", "param"};
		assertArrayEquals(MockStdOutCommand.receivedParams, want);
		assertEquals(MockStdOutCommand.receivedOutputType, 1);
		assertEquals(MockStdOutCommand.receivedOutputFile, file);
	}

	@Test
	public void testInterpreterAppendingToAFileSpacesBeforeAndAfter() {
		File file = new File("fyle", "", shell.getCurrentDir());
		shell.getCurrentDir().addFile(file);
		Interpreter.interpret("cmd1 param param >> fyle", shell);
		String want[] = {"cmd1", "param", "param"};
		assertArrayEquals(MockStdOutCommand.receivedParams, want);
		assertEquals(MockStdOutCommand.receivedOutputType, 2);
		assertEquals(MockStdOutCommand.receivedOutputFile, file);
	}

	@Test
	public void testInterpreterAppendingToAFileSpacesBefore() {
		File file = new File("fyle", "", shell.getCurrentDir());
		shell.getCurrentDir().addFile(file);
		Interpreter.interpret("cmd1 param param  >>fyle", shell);
		String want[] = {"cmd1", "param", "param"};
		assertArrayEquals(MockStdOutCommand.receivedParams, want);
		assertEquals(MockStdOutCommand.receivedOutputType, 2);
		assertEquals(MockStdOutCommand.receivedOutputFile, file);
	}

	@Test
	public void testInterpreterAppendingToAFileSpacesAfter() {
		File file = new File("fyle", "", shell.getCurrentDir());
		shell.getCurrentDir().addFile(file);
		Interpreter.interpret("cmd1 param param>>  fyle", shell);
		String want[] = {"cmd1", "param", "param"};
		assertArrayEquals(MockStdOutCommand.receivedParams, want);
		assertEquals(MockStdOutCommand.receivedOutputType, 2);
		assertEquals(MockStdOutCommand.receivedOutputFile, file);
	}

	@Test
	public void testInterpreterAppendingToAFileNoSpaces() {
		File file = new File("fyle", "", shell.getCurrentDir());
		shell.getCurrentDir().addFile(file);
		Interpreter.interpret("cmd1 param param>>fyle", shell);
		String want[] = {"cmd1", "param", "param"};
		assertArrayEquals(MockStdOutCommand.receivedParams, want);
		assertEquals(MockStdOutCommand.receivedOutputType, 2);
		assertEquals(MockStdOutCommand.receivedOutputFile, file);
	}

	@Test
	public void testInterpreterAppendingToAFileSpacesInBetween() {
		File file = new File("fyle", "", shell.getCurrentDir());
		shell.getCurrentDir().addFile(file);
		Interpreter.interpret("cmd1 param param>  >fyle", shell);
		String want[] = {"cmd1", "param", "param"};
		assertArrayEquals(MockStdOutCommand.receivedParams, want);
		assertEquals(MockStdOutCommand.receivedOutputType, 2);
		assertEquals(MockStdOutCommand.receivedOutputFile, file);
	}

	@Test
	public void testInterpreterAppendingToAFileLotsSpaces() {
		File file = new File("fyle", "", shell.getCurrentDir());
		shell.getCurrentDir().addFile(file);
		Interpreter.interpret("cmd1 param param      >  >     fyle", shell);
		String want[] = {"cmd1", "param", "param"};
		assertArrayEquals(MockStdOutCommand.receivedParams, want);
		assertEquals(MockStdOutCommand.receivedOutputType, 2);
		assertEquals(MockStdOutCommand.receivedOutputFile, file);
	}

	@Test
	public void testInterpreterFileOutputWhenCommandDoesntProduceStdout() {
		File file = new File("fyle", "", shell.getCurrentDir());
		shell.getCurrentDir().addFile(file);
		Interpreter.interpret("cmd2 param param      >  >     fyle", shell);
		String want[] = {"cmd2", "param", "param"};
		assertArrayEquals(MockNonStdOutCommand.receivedParams, want);
		assertEquals(MockNonStdOutCommand.receivedOutputType, 2);
		assertEquals(MockNonStdOutCommand.receivedOutputFile, null);
		// Ensure that this file was NOT created
		assertEquals(shell.getCurrentDir().containsFile("fyle"), 0);
	}

	/**
	 * All three together
	 */
	@Test
	public void testInterpreterSpacesQuotesAndRedirection() {
		File file = new File("fyle", "", shell.getCurrentDir());
		shell.getCurrentDir().addFile(file);
		Interpreter.interpret("cmd1 \"param    spaced\" param      >>     fyle",
				shell);
		String want[] = {"cmd1", "\"param    spaced\"", "param"};
		assertArrayEquals(MockStdOutCommand.receivedParams, want);
		assertEquals(MockStdOutCommand.receivedOutputType, 2);
		assertEquals(MockStdOutCommand.receivedOutputFile, file);
	}
}
