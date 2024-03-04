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
import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import driver.Directory;
import driver.File;
import driver.JShell;
import driver.ListFiles;
import driver.PushDirOntoStack;
import driver.Storage;

public class ListFilesTest {

	JShell shell;
	File stdOutFile;

	/** Used to test print statements */
	private final PrintStream printed = System.out;
	private final ByteArrayOutputStream consoleStreamCaptor =
			new ByteArrayOutputStream();

	/**
	 * Set up a new JShell and a File to send StdOut to.
	 */
	@Before
	public void setUp() {
		shell = new JShell();
		stdOutFile = new File("file", "", null);
		System.setOut(new PrintStream(consoleStreamCaptor));
	}

	/**
	 * Destroy the only reference to the storage system
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		Field field = Storage.class.getDeclaredField("onlyReference");
		field.setAccessible(true);
		field.set(null, null);
		System.setOut(printed);
	}

	@Test
	public void testGetManual() {
		assertEquals("ls [-R] [PATH ...] \nIf no paths are given, print the "
				+ "contents "
				+ "(file or directory) of the current \ndirectory, with a new "
				+ "line following each of the content (file or directory). \n"
				+ "Otherwise, for each path p, the order listed: \n    - If p "
				+ "specifies a file, print p \n    - If p specifies a "
				+ "directory, print p, a colon, then the contents of that \n"
				+ "      directory, then an extra new line. \n    - If p does "
				+ "not exist, print a suitable message.\n If –R is present, "
				+ "recursively list all subdirectories of each given " + "path",
				ListFiles.getManual());
	}

	@Test
	public void testPerformOutcomeRoot() {
		File file1 = new File("file1", "stuff", shell.getRootDir());
		File file2 = new File("file2", "stuff", shell.getRootDir());
		Directory dir1 = new Directory("dir1", shell.getRootDir());
		Directory dir2 = new Directory("dir2", shell.getRootDir());
		shell.getRootDir().addFile(file1);
		shell.getRootDir().addFile(file2);
		shell.getRootDir().addFile(dir1);
		shell.getRootDir().addFile(dir2);
		File file3 = new File("file3", "stuff", dir1);
		dir1.addFile(file3);
		Directory dir3 = new Directory("dir3", dir1);
		dir1.addFile(dir3);
		Directory dir4 = new Directory("dir4", dir3);
		dir3.addFile(dir4);
		File file4 = new File("file4", "stuff", dir2);
		dir2.addFile(file4);
		// At this point: the storage system looks like this as a tree:
		//    /
		//        file1
		//        file2
		//        dir1
		//	          file3
		//	          dir3
		//		          dir4
		//        dir2
		//	          file4
		String[] parameters = {"ls"};
		ListFiles.performOutcome(shell, parameters, 1, stdOutFile);
		// Test that the stack picked up the root and the shell changed its
		// current directory to dir2
		assertEquals("file1\n" + "file2\n" + "dir1\n" + "dir2\n",
				stdOutFile.getContents());
	}

	@Test
	public void testPerformOutcomeRootRecursive() {
		File file1 = new File("file1", "stuff", shell.getRootDir());
		File file2 = new File("file2", "stuff", shell.getRootDir());
		Directory dir1 = new Directory("dir1", shell.getRootDir());
		Directory dir2 = new Directory("dir2", shell.getRootDir());
		shell.getRootDir().addFile(file1);
		shell.getRootDir().addFile(file2);
		shell.getRootDir().addFile(dir1);
		shell.getRootDir().addFile(dir2);
		File file3 = new File("file3", "stuff", dir1);
		dir1.addFile(file3);
		Directory dir3 = new Directory("dir3", dir1);
		dir1.addFile(dir3);
		Directory dir4 = new Directory("dir4", dir3);
		dir3.addFile(dir4);
		File file4 = new File("file4", "stuff", dir2);
		dir2.addFile(file4);
		// At this point: the storage system looks like this as a tree:
		//    /
		//        file1
		//        file2
		//        dir1
		//	          file3
		//	          dir3
		//		          dir4
		//        dir2
		//	          file4
		String[] parameters = {"ls", "-R"};
		ListFiles.performOutcome(shell, parameters, 1, stdOutFile);
		// Test that the stack picked up the root and the shell changed its
		// current directory to dir2
		assertEquals(".:\n" + "file1\n" + "file2\n" + "dir1\n" + "dir2\n" + "\n"
				+ "./dir1:\n" + "file3\n" + "dir3\n" + "\n" + "./dir1/dir3:\n"
				+ "dir4\n" + "\n" + "./dir1/dir3/dir4:\n" + "\n" + "./dir2:\n"
				+ "file4\n", stdOutFile.getContents());
	}

	@Test
	public void testPerformOutcomeNonRootAbsPathNonRecursive() {
		File file1 = new File("file1", "stuff", shell.getRootDir());
		File file2 = new File("file2", "stuff", shell.getRootDir());
		Directory dir1 = new Directory("dir1", shell.getRootDir());
		Directory dir2 = new Directory("dir2", shell.getRootDir());
		shell.getRootDir().addFile(file1);
		shell.getRootDir().addFile(file2);
		shell.getRootDir().addFile(dir1);
		shell.getRootDir().addFile(dir2);
		File file3 = new File("file3", "stuff", dir1);
		dir1.addFile(file3);
		Directory dir3 = new Directory("dir3", dir1);
		dir1.addFile(dir3);
		Directory dir4 = new Directory("dir4", dir3);
		dir3.addFile(dir4);
		File file4 = new File("file4", "stuff", dir2);
		dir2.addFile(file4);
		// At this point: the storage system looks like this as a tree:
		//    /
		//        file1
		//        file2
		//        dir1
		//	          file3
		//	          dir3
		//		          dir4
		//        dir2
		//	          file4
		String[] parameters = {"ls", "/dir1"};
		ListFiles.performOutcome(shell, parameters, 1, stdOutFile);
		// Test that the stack picked up the root and the shell changed its
		// current directory to dir2
		assertEquals(
				"/dir1:\n" + "file3\n" + "dir3\n",
				stdOutFile.getContents());
	}
	
	@Test
	public void testPerformOutcomeNonRootAbsPathRecursive() {
		File file1 = new File("file1", "stuff", shell.getRootDir());
		File file2 = new File("file2", "stuff", shell.getRootDir());
		Directory dir1 = new Directory("dir1", shell.getRootDir());
		Directory dir2 = new Directory("dir2", shell.getRootDir());
		shell.getRootDir().addFile(file1);
		shell.getRootDir().addFile(file2);
		shell.getRootDir().addFile(dir1);
		shell.getRootDir().addFile(dir2);
		File file3 = new File("file3", "stuff", dir1);
		dir1.addFile(file3);
		Directory dir3 = new Directory("dir3", dir1);
		dir1.addFile(dir3);
		Directory dir4 = new Directory("dir4", dir3);
		dir3.addFile(dir4);
		File file4 = new File("file4", "stuff", dir2);
		dir2.addFile(file4);
		// At this point: the storage system looks like this as a tree:
		//    /
		//        file1
		//        file2
		//        dir1
		//	          file3
		//	          dir3
		//		          dir4
		//        dir2
		//	          file4
		String[] parameters = {"ls", "-R", "/dir1"};
		ListFiles.performOutcome(shell, parameters, 1, stdOutFile);
		// Test that the stack picked up the root and the shell changed its
		// current directory to dir2
		assertEquals(
				"/dir1:\n" + "file3\n" + "dir3\n" + "\n" + "/dir1/dir3:\n"
						+ "dir4\n" + "\n" + "/dir1/dir3/dir4:\n" + "",
				stdOutFile.getContents());
	}

	@Test
	public void testPerformOutcomeNonRootRelPathRecursive() {
		File file1 = new File("file1", "stuff", shell.getRootDir());
		File file2 = new File("file2", "stuff", shell.getRootDir());
		Directory dir1 = new Directory("dir1", shell.getRootDir());
		Directory dir2 = new Directory("dir2", shell.getRootDir());
		shell.getRootDir().addFile(file1);
		shell.getRootDir().addFile(file2);
		shell.getRootDir().addFile(dir1);
		shell.getRootDir().addFile(dir2);
		File file3 = new File("file3", "stuff", dir1);
		dir1.addFile(file3);
		Directory dir3 = new Directory("dir3", dir1);
		dir1.addFile(dir3);
		Directory dir4 = new Directory("dir4", dir3);
		dir3.addFile(dir4);
		File file4 = new File("file4", "stuff", dir2);
		dir2.addFile(file4);
		// At this point: the storage system looks like this as a tree:
		//    /
		//        file1
		//        file2
		//        dir1
		//	          file3
		//	          dir3
		//		          dir4
		//        dir2
		//	          file4
		String[] parameters = {"ls", "-R", "dir1"};
		ListFiles.performOutcome(shell, parameters, 1, stdOutFile);
		// Test that the stack picked up the root and the shell changed its
		// current directory to dir2
		assertEquals(
				"dir1:\n" + "file3\n" + "dir3\n" + "\n" + "dir1/dir3:\n"
						+ "dir4\n" + "\n" + "dir1/dir3/dir4:\n" + "",
				stdOutFile.getContents());
	}

	@Test
	public void testPerformOutcomeNonRootDottedPathRecursive() {
		File file1 = new File("file1", "stuff", shell.getRootDir());
		File file2 = new File("file2", "stuff", shell.getRootDir());
		Directory dir1 = new Directory("dir1", shell.getRootDir());
		Directory dir2 = new Directory("dir2", shell.getRootDir());
		shell.getRootDir().addFile(file1);
		shell.getRootDir().addFile(file2);
		shell.getRootDir().addFile(dir1);
		shell.getRootDir().addFile(dir2);
		File file3 = new File("file3", "stuff", dir1);
		dir1.addFile(file3);
		Directory dir3 = new Directory("dir3", dir1);
		dir1.addFile(dir3);
		Directory dir4 = new Directory("dir4", dir3);
		dir3.addFile(dir4);
		File file4 = new File("file4", "stuff", dir2);
		dir2.addFile(file4);
		// At this point: the storage system looks like this as a tree:
		//    /
		//        file1
		//        file2
		//        dir1
		//	          file3
		//	          dir3
		//		          dir4
		//        dir2
		//	          file4
		String[] parameters = {"ls", "-R", "./dir1/../dir1"};
		ListFiles.performOutcome(shell, parameters, 1, stdOutFile);
		// Test that the stack picked up the root and the shell changed its
		// current directory to dir2
		assertEquals(
				"./dir1/../dir1:\n" + "file3\n" + "dir3\n" + "\n"
						+ "./dir1/../dir1/dir3:\n" + "dir4\n" + "\n"
						+ "./dir1/../dir1/dir3/dir4:\n" + "",
				stdOutFile.getContents());
	}

	@Test
	public void testPerformOutcomeFile() {
		File file1 = new File("file1", "stuff", shell.getRootDir());
		File file2 = new File("file2", "stuff", shell.getRootDir());
		Directory dir1 = new Directory("dir1", shell.getRootDir());
		Directory dir2 = new Directory("dir2", shell.getRootDir());
		shell.getRootDir().addFile(file1);
		shell.getRootDir().addFile(file2);
		shell.getRootDir().addFile(dir1);
		shell.getRootDir().addFile(dir2);
		File file3 = new File("file3", "stuff", dir1);
		dir1.addFile(file3);
		Directory dir3 = new Directory("dir3", dir1);
		dir1.addFile(dir3);
		Directory dir4 = new Directory("dir4", dir3);
		dir3.addFile(dir4);
		File file4 = new File("file4", "stuff", dir2);
		dir2.addFile(file4);
		// At this point: the storage system looks like this as a tree:
		//    /
		//        file1
		//        file2
		//        dir1
		//	          file3
		//	          dir3
		//		          dir4
		//        dir2
		//	          file4
		String[] parameters = {"ls", "file1"};
		ListFiles.performOutcome(shell, parameters, 1, stdOutFile);
		// Test that the stack picked up the root and the shell changed its
		// current directory to dir2
		assertEquals("file1", stdOutFile.getContents());
	}

	@Test
	public void testPerformOutcomeAbsFile() {
		File file1 = new File("file1", "stuff", shell.getRootDir());
		File file2 = new File("file2", "stuff", shell.getRootDir());
		Directory dir1 = new Directory("dir1", shell.getRootDir());
		Directory dir2 = new Directory("dir2", shell.getRootDir());
		shell.getRootDir().addFile(file1);
		shell.getRootDir().addFile(file2);
		shell.getRootDir().addFile(dir1);
		shell.getRootDir().addFile(dir2);
		File file3 = new File("file3", "stuff", dir1);
		dir1.addFile(file3);
		Directory dir3 = new Directory("dir3", dir1);
		dir1.addFile(dir3);
		Directory dir4 = new Directory("dir4", dir3);
		dir3.addFile(dir4);
		File file4 = new File("file4", "stuff", dir2);
		dir2.addFile(file4);
		// At this point: the storage system looks like this as a tree:
		//    /
		//        file1
		//        file2
		//        dir1
		//	          file3
		//	          dir3
		//		          dir4
		//        dir2
		//	          file4
		String[] parameters = {"ls", "/file1"};
		ListFiles.performOutcome(shell, parameters, 1, stdOutFile);
		// Test that the stack picked up the root and the shell changed its
		// current directory to dir2
		assertEquals("/file1", stdOutFile.getContents());
	}

	@Test
	public void testPerformOutcomeRelFile() {
		File file1 = new File("file1", "stuff", shell.getRootDir());
		File file2 = new File("file2", "stuff", shell.getRootDir());
		Directory dir1 = new Directory("dir1", shell.getRootDir());
		Directory dir2 = new Directory("dir2", shell.getRootDir());
		shell.getRootDir().addFile(file1);
		shell.getRootDir().addFile(file2);
		shell.getRootDir().addFile(dir1);
		shell.getRootDir().addFile(dir2);
		File file3 = new File("file3", "stuff", dir1);
		dir1.addFile(file3);
		Directory dir3 = new Directory("dir3", dir1);
		dir1.addFile(dir3);
		Directory dir4 = new Directory("dir4", dir3);
		dir3.addFile(dir4);
		File file4 = new File("file4", "stuff", dir2);
		dir2.addFile(file4);
		// At this point: the storage system looks like this as a tree:
		//    /
		//        file1
		//        file2
		//        dir1
		//	          file3
		//	          dir3
		//		          dir4
		//        dir2
		//	          file4
		String[] parameters = {"ls", "/file1"};
		ListFiles.performOutcome(shell, parameters, 1, stdOutFile);
		// Test that the stack picked up the root and the shell changed its
		// current directory to dir2
		assertEquals("/file1", stdOutFile.getContents());
	}
	
	@Test
	public void testPerformOutcomeNonExistentPath() {
		File file1 = new File("file1", "stuff", shell.getRootDir());
		File file2 = new File("file2", "stuff", shell.getRootDir());
		Directory dir1 = new Directory("dir1", shell.getRootDir());
		Directory dir2 = new Directory("dir2", shell.getRootDir());
		shell.getRootDir().addFile(file1);
		shell.getRootDir().addFile(file2);
		shell.getRootDir().addFile(dir1);
		shell.getRootDir().addFile(dir2);
		File file3 = new File("file3", "stuff", dir1);
		dir1.addFile(file3);
		Directory dir3 = new Directory("dir3", dir1);
		dir1.addFile(dir3);
		Directory dir4 = new Directory("dir4", dir3);
		dir3.addFile(dir4);
		File file4 = new File("file4", "stuff", dir2);
		dir2.addFile(file4);
		// At this point: the storage system looks like this as a tree:
		//    /
		//        file1
		//        file2
		//        dir1
		//	          file3
		//	          dir3
		//		          dir4
		//        dir2
		//	          file4
		String[] parameters = {"ls", "/fileAbbas"};
		ListFiles.performOutcome(shell, parameters, 1, stdOutFile);
		// Test that the stack picked up the root and the shell changed its
		// current directory to dir2
		assertEquals("", stdOutFile.getContents());
		assertEquals("ls: Cannot access '/fileAbbas', no such file or "
				+ "directory.",
				consoleStreamCaptor.toString().trim());
	}
	
	@Test
	public void testPerformOutcomePathExistentUpToSomeExtentButLastIsNot() {
		File file1 = new File("file1", "stuff", shell.getRootDir());
		File file2 = new File("file2", "stuff", shell.getRootDir());
		Directory dir1 = new Directory("dir1", shell.getRootDir());
		Directory dir2 = new Directory("dir2", shell.getRootDir());
		shell.getRootDir().addFile(file1);
		shell.getRootDir().addFile(file2);
		shell.getRootDir().addFile(dir1);
		shell.getRootDir().addFile(dir2);
		File file3 = new File("file3", "stuff", dir1);
		dir1.addFile(file3);
		Directory dir3 = new Directory("dir3", dir1);
		dir1.addFile(dir3);
		Directory dir4 = new Directory("dir4", dir3);
		dir3.addFile(dir4);
		File file4 = new File("file4", "stuff", dir2);
		dir2.addFile(file4);
		// At this point: the storage system looks like this as a tree:
		//    /
		//        file1
		//        file2
		//        dir1
		//	          file3
		//	          dir3
		//		          dir4
		//        dir2
		//	          file4
		String[] parameters = {"ls", "/dir1/dir3/dir4/dir5/dir10"};
		ListFiles.performOutcome(shell, parameters, 1, stdOutFile);
		// Test that the stack picked up the root and the shell changed its
		// current directory to dir2
		assertEquals("", stdOutFile.getContents());
		assertEquals("ls: Cannot access '/dir1/dir3/dir4/dir5/dir10', "
				+ "no such file or "
				+ "directory.",
				consoleStreamCaptor.toString().trim());
	}
}
