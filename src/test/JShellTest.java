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
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import driver.Directory;
import driver.JShell;
import driver.Storage;

/**
 * JUnit test file to test all public methods of a JShell.
 */
public class JShellTest {

	JShell shell;

	/** Used to test print statements */
	private final PrintStream printed = System.out;
	private final ByteArrayOutputStream consoleStreamCaptor = 
			new ByteArrayOutputStream();

	/**
	 * Sets up a JShell called shell.
	 */
	@Before
	public void setUp() {
		shell = new JShell();
		System.setOut(new PrintStream(consoleStreamCaptor));
	}

	@After
	public void tearDown() {
		System.setOut(printed);
	}

	/**
	 * Test JShell initializer and its behaviour; test that the current
	 * directory is set to the root, that isActive is set to true, and the stack
	 * is empty.
	 * 
	 * @throws ClassNotFoundException
	 */
	@Test
	public final void testJShellInitailizer() {
		assertEquals(shell.getCurrentDir(), shell.getRootDir());
		//assertTrue(shell.isActive());
		assertTrue(shell.getDirStack().isEmpty());
	}

	/**
	 * Test if the CmdToClass HashMap is populated correctly
	 * 
	 * @throws ClassNotFoundException
	 */
	@Test
	public final void testJShellCmdToClass() throws ClassNotFoundException {
		assertEquals(shell.getCmdToClass().get("exit"),
				Class.forName("driver.Exit"));
		assertEquals(shell.getCmdToClass().get("mkdir"),
				Class.forName("driver.MakeDirectory"));
		assertEquals(shell.getCmdToClass().get("cd"),
				Class.forName("driver.ChangeDirectory"));
		assertEquals(shell.getCmdToClass().get("ls"),
				Class.forName("driver.ListFiles"));
		assertEquals(shell.getCmdToClass().get("pwd"),
				Class.forName("driver.PrintWorkingDirectory"));
		assertEquals(shell.getCmdToClass().get("pushd"),
				Class.forName("driver.PushDirOntoStack"));
		assertEquals(shell.getCmdToClass().get("popd"),
				Class.forName("driver.PopDirFromStack"));
		assertEquals(shell.getCmdToClass().get("history"),
				Class.forName("driver.PrintHistory"));
		assertEquals(shell.getCmdToClass().get("cat"),
				Class.forName("driver.ConcatenateFile"));
		assertEquals(shell.getCmdToClass().get("echo"),
				Class.forName("driver.Echo"));
		assertEquals(shell.getCmdToClass().get("man"),
				Class.forName("driver.Manual"));
		assertEquals(shell.getCmdToClass().get("saveJShell"),
				Class.forName("driver.SaveJShell"));
		assertEquals(shell.getCmdToClass().get("loadJShell"),
				Class.forName("driver.LoadJShell"));
		assertEquals(shell.getCmdToClass().get("search"),
				Class.forName("driver.Search"));
		assertEquals(shell.getCmdToClass().get("tree"),
				Class.forName("driver.Tree"));
		assertEquals(shell.getCmdToClass().get("rm"),
				Class.forName("driver.Remove"));
		assertEquals(shell.getCmdToClass().get("mv"),
				Class.forName("driver.Move"));
		assertEquals(shell.getCmdToClass().get("cp"),
				Class.forName("driver.Copy"));
		assertEquals(shell.getCmdToClass().get("curl"),
				Class.forName("driver.ClientURL"));
	}

	/**
	 * Test that updating a shell retains the old one's StorageUnits,
	 * CommandHistory, and current working directory.
	 */
	@Test
	public final void testUpdateShell() {
		JShell shell2 = new JShell();
		Directory newCurrDir = new Directory("new", shell2.getCurrentDir());
		shell2.getCurrentDir().addFile(newCurrDir);
		shell2.setCurrentDir(newCurrDir);
		shell2.addCom("random command");
		shell2.addCom("command random");
		shell2.addCom("tree");
		shell2.getDirStack().push(newCurrDir);
		shell.updateShell(shell2);
		assertEquals(shell, shell2);
	}

	/**
	 * Test the equals method
	 */
	@Test
	public final void testEqualsObject() {
		JShell shell2 = new JShell();
		Directory newCurrDir = new Directory("new", shell2.getCurrentDir());
		shell2.getCurrentDir().addFile(newCurrDir);
		shell2.setCurrentDir(newCurrDir);
		shell2.addCom("random command");
		shell2.addCom("command random");
		shell2.addCom("tree");
		shell.updateShell(shell2);
		assertTrue(shell.equals(shell2));
	}

	/**
	 * After initialization of a JShell, test that getting the current directory
	 * is the root
	 */
	@Test
	public final void testGetAndSetCurrentDirStillRoot() {
		assertEquals(shell.getCurrentDir(), shell.getRootDir());
	}

	/**
	 * After changing the current directory a directory other than the root,
	 * test the current directory of the JShell is correct.
	 */
	@Test
	public final void testGetAndSetCurrentDirAfterChanging() {
		Directory newCurrDir = new Directory("new", shell.getCurrentDir());
		shell.getCurrentDir().addFile(newCurrDir);
		shell.setCurrentDir(newCurrDir);
		assertEquals(shell.getCurrentDir(), newCurrDir);
	}

	/**
	 * Test getRootDir is the one in the only instance of Storage even after
	 * adding other stuff to Storage
	 */
	@Test
	public final void testGetRootDir() {
		assertEquals(shell.getRootDir(), Storage.createNewStorage().getRoot());
		Directory newCurrDir = new Directory("new", shell.getCurrentDir());
		shell.getCurrentDir().addFile(newCurrDir);
		shell.setCurrentDir(newCurrDir);
		assertEquals(shell.getRootDir(), Storage.createNewStorage().getRoot());
	}

	/**
	 * Test that the directory stack pushes and pops properly
	 */
	@Test
	public final void testGetDirStack() {
		Directory newDir1 = new Directory("new", shell.getCurrentDir());
		shell.getCurrentDir().addFile(newDir1);
		shell.setCurrentDir(newDir1);
		Directory newDir2 = new Directory("new", shell.getCurrentDir());
		shell.getCurrentDir().addFile(newDir2);
		shell.setCurrentDir(newDir2);
		shell.getDirStack().push(newDir1);
		shell.getDirStack().push(newDir2);
		assertEquals(shell.getDirStack().pop(), newDir2);
		assertEquals(shell.getDirStack().pop(), newDir1);
	}

	/**
	 * Test adding commands to ComHis and getting command history
	 */
	@Test
	public final void testAddComAndGetComHis() {
		shell.addCom("command one two three");
		shell.addCom("command two three four");
		shell.addCom("tree");
		shell.addCom("cp one two");
		assertEquals(shell.getComHis().getSize(), 4);
		ArrayList<String> lst = new ArrayList<String>();
		lst.add("command one two three");
		lst.add("command two three four");
		lst.add("tree");
		lst.add("cp one two");
		assertEquals(shell.getComHis().getCmds(), lst);
	}

	/**
	 * Test printing a line.
	 */
	@Test
	public final void testPrintln() {
		shell.println("i am a shell talking to you, hello!");
		assertEquals("i am a shell talking to you, hello!",
				consoleStreamCaptor.toString().trim());
	}

	/**
	 * Test printing a string that is not a line.
	 */
	@Test
	public final void testPrint() {
		shell.print("i am a shell talking to you, hello!");
		assertEquals("i am a shell talking to you, hello!",
				consoleStreamCaptor.toString().trim());
	}

	/**
	 * Test printing an error to the command line.
	 */
	@Test
	public final void testPrintError() {
		shell.printError("Error: Something is wrong!!");
		assertEquals("Error: Something is wrong!!",
				consoleStreamCaptor.toString().trim());
	}

	/**
	 * Test the shell is not active after exiting
	 */
	@Test
	public final void testExit() {
		shell.exit();
		assertFalse(shell.isActive());
	}

}
