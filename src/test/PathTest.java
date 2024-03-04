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

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import driver.*;

/**
 * The responsibility of PathTest is to test the Path class
 *
 */
public class PathTest {

	static MockJShell shell;
	static Directory root, dir1, dir2, dir21;
	static File file1, file2;

	@BeforeClass
	public static void setUpBefore() {
		shell = new MockJShell();
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
		shell.setCurrentDir(dir1);
	}

	@Test
	public void testDetermineAbs() {
		Path path = new Path("/wow/cool/wow");
		assertEquals(path.determineAbsolute(), true);
	}

	public void testDetermineRel() {
		Path path = new Path("d/di/da/di/di/da/");
		assertEquals(path.determineAbsolute(), false);
	}

	@Test
	public void testdetermineStartDirAbs() {
		Path path = new Path("/cool/nice/tree");
		assertEquals(path.determineStartDir(shell), root);
	}

	@Test
	public void testdetermineStartDirRel() {
		Path path = new Path("hey/soul/sister");
		assertEquals(path.determineStartDir(shell), dir1);
	}

	@Test
	public void testVerifyPathRoot() {
		Path path = new Path("/");
		assertEquals(path.verifyPath(shell, false), root);
		assertEquals(path.verifyPath(shell, true), root);
	}

	@Test
	public void testVerifyPathCurrDir() {
		Path path = new Path(".");
		assertEquals(path.verifyPath(shell, false), dir1);
		assertEquals(path.verifyPath(shell, true), dir1);
	}

	@Test
	public void testVerifyPathParentDir() {
		Path path = new Path("..");
		assertEquals(path.verifyPath(shell, false), root);
		assertEquals(path.verifyPath(shell, true), dir1);
	}

	@Test
	public void testVerifyPathFile1() {
		Path path = new Path("file1");

		assertEquals(path.verifyPath(shell, false), file1);
		assertEquals(path.verifyPath(shell, true), dir1);
	}

	@Test
	public void testVerifyPathFile1Abs() {
		Path path = new Path("/dir1/file1");
		assertEquals(path.verifyPath(shell, false), file1);
		assertEquals(path.verifyPath(shell, true), dir1);
	}

	@Test
	public void testVerifyPathInvalidFile1() {
		Path path = new Path("dir1/file1");
		assertEquals(path.verifyPath(shell, false), null);
		assertEquals(path.verifyPath(shell, true), null);
	}

	@Test
	public void testVerifyPathTriplePathFile() {
		Path path = new Path("/dir2/dir21/file2");
		assertEquals(path.verifyPath(shell, false), file2);
		assertEquals(path.verifyPath(shell, true), dir21);
	}

	@Test
	public void testVerifyPathInvalidTriplePathFile() {
		Path path = new Path("/dir2/nonexistDir/file2");
		assertEquals(path.verifyPath(shell, false), null);
		assertEquals(path.verifyPath(shell, true), null);
	}

	@Test
	public void testVerifyPathLastInvalid() {
		Path path = new Path("/dir2/dir21/fakefile");
		assertEquals(path.verifyPath(shell, false), null);
		assertEquals(path.verifyPath(shell, true), dir21);
	}

	@Test
	public void testVerifyPath2ndLastDoubleDot() {
		Path path = new Path("../dir21");
		shell.setCurrentDir(dir21);
		assertEquals(path.verifyPath(shell, false), dir21);
		assertEquals(path.verifyPath(shell, true), dir2);
	}

	@Test
	public void testVerifyPath2ndLastSingleDot() {
		Path path = new Path("./dir21");
		shell.setCurrentDir(dir2);
		assertEquals(path.verifyPath(shell, false), dir21);
		assertEquals(path.verifyPath(shell, true), dir2);
	}
}
