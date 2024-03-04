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
import java.sql.Driver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import driver.*;

public class SearchTest {
	JShell shell;
	Directory root;
	Directory dir1;
	File file1;
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
		stdOutFile = new File("file", "", null);
		System.setOut(new PrintStream(consoleStreamCaptor));
	}

	@Test
	public void testGetManual() {
		assertEquals("search path ... -type [f|d] -name expression\n"
				+ "This command takes in at least three arguments. "
				+ "Searches for files/directories\nas specified after "
				+ "-type that has the name expression which is specified "
				+ "after -name.\nHowever, if path or [f|d] or "
				+ "expression is not specified, then give back an error",
				driver.Search.getManual());
	}

	@Test
	public void testPerformOutComeForExistingDir() {
		String[] parameters = {"search", "/", "-type", "d", "-name",
				"\"dir1\""};
		driver.Search.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("/dir1", stdOutFile.getContents());
	}

	@Test
	public void testPerformOutComeForExistingFile() {
		file1 = new File("file1", "123", dir1);
		dir1.addFile(file1);
		String[] parameters = {"search", "/", "-type", "f", "-name",
				"\"file1\""};
		driver.Search.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("dir1/file1", stdOutFile.getContents());
	}

	@Test
	public void testPerformOutComeForNonExistingDir() {
		String[] parameters = {"search", "/", "-type", "d", "-name",
				"\"dir3\""};
		driver.Search.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("", stdOutFile.getContents());
	}

	@Test
	public void testPerformOutComeWithNoTypeOrName() {
		String[] parameters = {"search", "/", "ty", "d", "-name", "\"dir3\""};
		driver.Search.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("search: -type and -name are required parameters.",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testPerformOutComeWithNoTypeBeforeName() {
		String[] parameters = {"search", "/", "-type", "s", "-name",
				"\"dir3\""};
		driver.Search.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("search: Please specify search type after -type [f/d]",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testPerformOutComeWithNoDoubleQuotes() {
		String[] parameters = {"search", "/", "-type", "d", "-name", "dir3"};
		driver.Search.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("search: Please enclose name with double quotes (\"\")",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testPerformOutComeWithTwoNames() {
		String[] parameters = {"search", "/", "-type", "d", "-name", "\"dir3\"",
				"\"dir4\""};
		driver.Search.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("search: Please specify only one name",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testPerformOutComeInvalNumArgs() {
		String[] parameters = {"search", "/"};
		driver.Search.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("search: Invalid number of arguments.",
				consoleStreamCaptor.toString().trim());
	}

}
