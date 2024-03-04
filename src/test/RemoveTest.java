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

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import driver.Directory;
import driver.File;
import driver.JShell;
import driver.Remove;

public class RemoveTest {

	static JShell shell;
	static Directory root, dir1, dir2, dir21;
	static File file1, file2;

	@BeforeClass
	public static void setUpBefore() {
		shell = new JShell();
		root = shell.getRootDir();

		dir1 = new Directory("dir1", root);
		dir2 = new Directory("dir2", root);
		dir21 = new Directory("dir21", dir2);
		file1 = new File("file1", "file1contents", dir1);
		file2 = new File("file2", "file2contents", dir21);

		root.addFile(dir1);
		root.addFile(dir2);
		dir1.addFile(file1);
		dir2.addFile(dir21);
		dir21.addFile(file2);
	}
	
	@Before
	public void setUp() {
		shell.setCurrentDir(root);
	}

	@Test
	public void testParamNum() {
		String[] test0 = { "rm", "sadfa" };
		String[] test1 = { "rm" };
		String[] test2 = { "rm", "dir1", "dir2" };

		assertEquals(Remove.checkParam(test0.length, shell), true);
		assertEquals(Remove.checkParam(test1.length, shell), false);
		assertEquals(Remove.checkParam(test2.length, shell), false);
	}

	@Test
	public void testCheckNonDirectory() {
		assertEquals(Remove.checkDirectory(shell, file1), false);
	}

	@Test
	public void testCheckDirectory() {
		assertEquals(Remove.checkDirectory(shell, dir1), true);
	}

	@Test
	public void testCheckDirectoryNull() {
		assertEquals(Remove.checkDirectory(shell, null), false);
	}

	@Test
	public void testCheckAncestor() {
		shell.setCurrentDir(dir21);
		assertEquals(Remove.checkAncestor(shell, dir2), false);
	}

	@Test
	public void testCheckAncestorRoot() {
		assertEquals(Remove.checkAncestor(shell, root), false);
	}

	@Test
	public void testCheckNonAncestor() {
		assertEquals(Remove.checkAncestor(shell, dir2), true);
	}

	@Test
	public void testRemoveDirWithDirAndFile() {
		String input[] = { "rm", "dir2" };
		Remove.performOutcome(shell, input, 0, null);
		assertEquals(root.getDirContents().size(), 1);
		assertEquals(root.getDirContents().get(0), dir1);
	}
}
