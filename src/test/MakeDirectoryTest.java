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

import driver.*;

public class MakeDirectoryTest {
	JShell shell;
	Directory root;

	@Before
	public void setup() {
		shell = new JShell();
		root = shell.getRootDir();
	}

	@Test
	public void testPerformOutComeWithOneRelative() {
		String[] parameters = {"mkdir", "dir1"};
		driver.MakeDirectory.performOutcome(shell, parameters, 0, null);
		assertEquals("dir1", root.getDirContents().get(0).getName());
		assertEquals(root, root.getDirContents().get(0).getParentDir());
	}

	@Test
	public void testPerformOutComeWithAbsAndRelative() {
		String[] parameters = {"mkdir", "dir2", "/dir3", "dir4"};
		driver.MakeDirectory.performOutcome(shell, parameters, 0, null);
		assertEquals("dir2", root.getDirContents().get(1).getName());
		assertEquals(root, root.getDirContents().get(1).getParentDir());
		assertEquals("dir3", root.getDirContents().get(2).getName());
		assertEquals(root, root.getDirContents().get(2).getParentDir());
		assertEquals("dir4", root.getDirContents().get(3).getName());
		assertEquals(root, root.getDirContents().get(3).getParentDir());
	}
}
