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

import driver.*;

public class TransferFileTest {

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

	@After
	public void tearDown() {
		shell.exit();
	}

	@Test
	public void testNumberofParamsNotThree() {
		assertEquals(TransferFile.validateNumberOfParameters(shell, "mv", 2), false);
		assertEquals(TransferFile.validateNumberOfParameters(shell, "cp", 1), false);
	}

	@Test
	public void testNumberofParamsThree() {
		assertEquals(TransferFile.validateNumberOfParameters(shell, "asfd", 3), true);
	}

	@Test
	public void testValidateParentsNotShared() {
		assertEquals(TransferFile.validateParents(dir1, dir2, shell, null), true);
	}

	@Test
	public void testValidateParentsParAndChild() {
		String[] str = { "sadf", "sdfg", "rtyhr" };
		assertEquals(TransferFile.validateParents(dir2, dir21, shell, str), false);
	}

	@Test
	public void testValidateParentsChildAndPar() {
		assertEquals(TransferFile.validateParents(dir21, dir2, shell, null), true);
	}

	@Test
	public void testValidateParentsAncestors() {
		String[] str = { "sadf", "sdfg", "rtyhr" };
		assertEquals(TransferFile.validateParents(root, dir21, shell, str), false);
	}

	@Test
	public void testValidateNamesNotSharedDir() {
		assertEquals(TransferFile.validateNames(dir1, dir2, shell, null), true);
	}

	@Test
	public void testValidateNamesNotSharedFile() {
		assertEquals(TransferFile.validateNames(file1, dir2, shell, null), true);
	}

	@Test
	public void testValidateNamesFailCaseDir() {
		String[] str = { "sadf", "sdfg", "rtyhr" };
		assertEquals(TransferFile.validateNames(new Directory("dir21", null), dir2, shell, str), false);
	}

	@Test
	public void testValidateNamesFailCaseFile() {
		String[] str = { "sadf", "sdfg", "rtyhr" };
		assertEquals(TransferFile.validateNames(new File("dir21", "same name as dir21", null), dir2, shell, str),
				false);
	}

	@Test
	public void testValidateNamesEmptyString() {
		assertEquals(TransferFile.validateNames(new File("", "test empty string", null), dir2, shell, null), true);
	}

	@Test
	public void testFileToFileIsFile() {
		assertEquals(TransferFile.validateFileToFile(file1, shell, null), true);
	}

	@Test
	public void testFileToFileNotFile() {
		String[] str = { "sadf", "sdfg", "rtyhr" };
		assertEquals(TransferFile.validateFileToFile((StorageUnit) dir1, shell, str), false);
	}

	@Test
	public void testFileToFileNull() {
		String[] str = { "sadf", "sdfg", "rtyhr" };
		assertEquals(TransferFile.validateFileToFile(null, shell, str), false);
	}

	@Test
	public void testdetermineNewStorageUnitFile() {
		assertEquals(TransferFile.determineNewStorageUnit("/very/cool").getName(), "cool");
		assertEquals(TransferFile.determineNewStorageUnit("/very/cool").isFile(), true);
	}

	@Test
	public void testdetermineNewStorageUnitDir() {
		assertEquals(TransferFile.determineNewStorageUnit("/very/coo/").getName(), "coo");
		assertEquals(TransferFile.determineNewStorageUnit("/very/coo/").isFile(), false);
	}

}
