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

public class ChangeDirectoryTest {
	
	JShell shell;
	Directory root;
	Directory dir1;
	File stdOutFile;

	/** Used to test print statements */
	private final PrintStream printed = System.out;
	private final ByteArrayOutputStream consoleStreamCaptor = 
			new ByteArrayOutputStream();

	@Before
	public void setUp() {
		shell = new JShell();
		root = shell.getRootDir();
		dir1 = new Directory("dir1", root);
		root.addFile(dir1);
		shell.setCurrentDir(root);
		stdOutFile = new File("file", "", null);
		System.setOut(new PrintStream(consoleStreamCaptor));
	}

	@Test
	public void testGetManual() {
		assertEquals("cd DIR \nChange directory to DIR, which may be relative "
				+ "to the "
				+ "current directory or \nmay be a full path. As with "
				+ "Unix, .. " + "means a parent directory and a . means \nthe "
				+ "current "
				+ "directory. The directory must be /, the forward "
				+ "slash. The "
				+ "foot of \nthe file system is a single slash: /.  ",
				driver.ChangeDirectory.getManual());
	}

	@Test
	public void testPerformOutComeWithRelativePath() {
		String[] parameters = {"cd", "dir1"};
		driver.ChangeDirectory.performOutcome(shell, parameters, 0, null);
		assertEquals("dir1", shell.getCurrentDir().getName());
		assertEquals("/", shell.getCurrentDir().getParentDir().getName());
	}

	@Test
	public void testPerformOutComeWithAbsPath() {
		String[] parameters = {"cd", "/dir1"};
		driver.ChangeDirectory.performOutcome(shell, parameters, 0, null);
		assertEquals("dir1", shell.getCurrentDir().getName());
		assertEquals("/", shell.getCurrentDir().getParentDir().getName());
	}

	@Test
	public void testPerformOutComeWithDotPath() {
		String[] parameters = {"cd", "./dir1/.."};
		driver.ChangeDirectory.performOutcome(shell, parameters, 0, null);
		assertEquals("/", shell.getCurrentDir().getName());
		assertEquals("/", shell.getCurrentDir().getParentDir().getName());
	}

	@Test
	public void testPerformOutComeInvalidPathOne() {
		String[] parameters = {"cd", "//"};
		driver.ChangeDirectory.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("cd: That is not a valid path.",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testPerformOutComeInvalidPathTwo() {
		String[] parameters = {"cd", "/dir2"};
		driver.ChangeDirectory.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("cd: That is not a valid path.",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testPerformOutComeInvalidArg() {
		String[] parameters = {"cd"};
		driver.ChangeDirectory.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("cd: Invalid number of arguments.",
				consoleStreamCaptor.toString().trim());
	}
}
