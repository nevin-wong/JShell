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

import org.junit.Test;

import driver.*;

public class FileTest {

	@Test
	public void testSetAndGetName() {
		File file = new File("fileN", "fileC", null);
		file.setName("abbas");
		assertEquals("abbas", file.getName());
	}

	@Test
	public void testSetAndGetParent() {
		File file = new File("fileN", "fileC", null);
		Directory newParent = new Directory("newParent", null);
		file.setParentDir(newParent);
		assertEquals(newParent, file.getParentDir());
	}

	@Test
	public void checkIsDirAndIsFile() {
		File file = new File("fileN", "fileC", null);
		assertFalse(file.isDirectory());
		assertTrue(file.isFile());
	}

	@Test
	public void testConstructor() {
		File file = new File("fileN", "fileC", null);
		assertEquals(file.getName(), "fileN");
		assertEquals(file.getContents(), "fileC");
		assertEquals(file.getParentDir(), null);
	}

	@Test
	public void testAppend() {
		File file = new File("fileN", "fileC", null);
		file.append(" appended text.");
		assertEquals(file.getContents(), "fileC appended text.");
	}

	@Test
	public void testOverwrite() {
		File file = new File("fileN", "fileC", null);
		file.overwrite("overwriten text.");
		assertEquals(file.getContents(), "overwriten text.");
	}

	@Test
	public void testClone() {
		File file = new File("fileN", "fileC", null);
		File clone = file.clone(null);
		assertEquals(file.getName(), clone.getName());
		assertEquals(file.getContents(), clone.getContents());
		assertNotEquals(file, clone);
	}
}
