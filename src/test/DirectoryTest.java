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

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import driver.*;

public class DirectoryTest {

	Directory dir1 = new Directory("dir1", null);

	@Test
	public void testConstructor() {
		assertEquals("dir1", dir1.getName());
		assertEquals(null, dir1.getParentDir());
	}

	@Test
	public void testEqualsEqual() {
		Directory dir2 = new Directory("dir1", null);
		assertEquals(true, dir1.equals(dir2));
	}

	@Test
	public void testEqualsNotEqual() {
		File f2 = new File("file2", "123", null);
		assertEquals(false, dir1.equals(f2));
	}

	@Test
	public void testHashCode() {
		assertEquals(32, dir1.hashCode());
	}

	@Test
	public void testAddFile() {
		File f3 = new File("file3", "123", dir1);
		dir1.addFile(f3);
		assertEquals(f3, (File) dir1.getDirContents().get(0));
	}

	@Test
	public void testContainsFile() {
		File f4 = new File("file4", "123", dir1);
		dir1.addFile(f4);
		assertEquals(0, dir1.containsFile("file4"));
	}

	@Test
	public void testIsSubDir() {
		Directory dir2 = new Directory("dir2", dir1);
		dir1.addFile(dir2);
		assertEquals(0, dir1.isSubDir("dir2"));
	}

	@Test
	public void testDelFile() {
		File f4 = new File("file4", "123", dir1);
		dir1.addFile(f4);
		dir1.delFile(f4);
		assertEquals(-1, dir1.containsFile("file4"));
	}

	@Test
	public void testGetDirContentsEmpty() {
		ArrayList<StorageUnit> contents = new ArrayList<StorageUnit>();
		assertEquals(contents, dir1.getDirContents());
	}

	@Test
	public void testGetDirContentsNonEmpty() {
		File f2 = new File("file2", "123", null);
		dir1.addFile(f2);
		ArrayList<StorageUnit> contents = new ArrayList<StorageUnit>();
		contents.add(f2);
		assertEquals(contents, dir1.getDirContents());
	}

	@Test
	public void testGetFile() {
		File f2 = new File("file2", "123", null);
		dir1.addFile(f2);
		assertEquals(f2, dir1.getFile(0));
	}
	
	@Test
	public void testClone() {
		Directory newParentDir = new Directory("newParent", null);
		Directory newDir = dir1.clone(newParentDir);
		assertEquals(dir1, newDir);
	}
	
	@Test
	public void testIterator() {
		Directory dir2 = new Directory("dir2", dir1);
		dir1.addFile(dir2);
		File f3 = new File("file3", "123", dir1);
		dir1.addFile(f3);
		File f4 = new File("file4", "123", dir1);
		dir1.addFile(f4);
		File f5 = new File("file5", "123", dir1);
		dir1.addFile(f5);
		ArrayList<StorageUnit> actual = new ArrayList<StorageUnit>();
		ArrayList<StorageUnit> expected = new ArrayList<StorageUnit>();
		for (StorageUnit unit : dir1) {
			actual.add(unit);
		}
		expected.add(dir2);
		expected.add(f3);
		expected.add(f4);
		expected.add(f5);
		assertEquals(expected, actual);
	}
}
