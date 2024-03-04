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

import org.junit.Before;
import org.junit.Test;

import driver.Directory;
import driver.File;
import driver.JShell;
import driver.PrintWorkingDirectory;

public class PrintWorkingDirectoryTest {

	JShell shell;
	Directory root, dir1, dir2, dir21, dir12;
	File result;

	@Before
	public void setUp() {
		shell = new JShell();
		root = shell.getRootDir();

		dir1 = new Directory("dir1", root);
		dir2 = new Directory("dir2", root);
		dir21 = new Directory("dir21", dir2);
		dir12 = new Directory("dir21", dir1);

		root.addFile(dir1);
		root.addFile(dir2);
		dir2.addFile(dir21);
		dir1.addFile(dir12);

		result = new File("name", "", null);
	}

	@Test
	public void testInvalidParamDir() {
		shell.setCurrentDir(root);
		String[] input = {"pwd", "dir1"};
		PrintWorkingDirectory.performOutcome(shell, input, 1, result);
		assertEquals(result.getContents(), "");
	}

	@Test
	public void testInvalidParamRandom() {
		shell.setCurrentDir(root);
		String[] input = {"pwd", "asdf"};
		PrintWorkingDirectory.performOutcome(shell, input, 1, result);
		assertEquals(result.getContents(), "");
	}

	@Test
	public void testRoot() {
		shell.setCurrentDir(root);
		String[] input = {"pwd"};
		PrintWorkingDirectory.performOutcome(shell, input, 1, result);
		assertEquals(result.getContents(), "/");
	}

	@Test
	public void testDirectoryDepth1() {
		shell.setCurrentDir(dir1);
		String[] input = {"pwd"};
		PrintWorkingDirectory.performOutcome(shell, input, 1, result);
		assertEquals(result.getContents(), "/dir1/");
	}

	@Test
	public void testDirectoryDiffDepth1() {
		shell.setCurrentDir(dir2);
		String[] input = {"pwd"};
		PrintWorkingDirectory.performOutcome(shell, input, 1, result);
		assertEquals(result.getContents(), "/dir2/");
	}

	@Test
	public void testDirectoryDepth2() {
		shell.setCurrentDir(dir21);
		String[] input = {"pwd"};
		PrintWorkingDirectory.performOutcome(shell, input, 1, result);
		assertEquals(result.getContents(), "/dir2/dir21/");
	}

	@Test
	public void testDirectoryDepth2SameNameDiffLocation() {
		shell.setCurrentDir(dir12);
		String[] input = {"pwd"};
		PrintWorkingDirectory.performOutcome(shell, input, 1, result);
		assertEquals(result.getContents(), "/dir1/dir21/");
	}

}
