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
import driver.PushDirOntoStack;
import driver.Storage;

public class PushDirOntoStackTest {

	JShell shell;

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
	public void testProducesStdOut() {
		assertFalse(PushDirOntoStack.producesStdOut());
	}

	@Test
	public void testGetManual() {
		assertEquals("pushd DIR \nSaves the current working directory by "
				+ "pushing " + "onto directory stack and then \n"
				+ "changes the new current working directory to DIR. The push "
				+ "must be \n"
				+ "consistent as per the LIFO behavior of a stack. The pushd "
				+ "command saves \n"
				+ "the old current working directory in directory stack so that"
				+ " it can be returned \n"
				+ "to at any time (via the popd command). The size of the "
				+ "directory stack \n"
				+ "is dynamic and dependent on the pushd and the popd "
				+ "commands.", PushDirOntoStack.getManual());
	}

	@Test
	public void testPerformOutcomeOneParam() {
		String[] parameters = {"pushd"};
		PushDirOntoStack.performOutcome(shell, parameters, 0, null);
		assertEquals("pushd: Invalid number of arguments.",
				consoleStreamCaptor.toString().trim());
	}
	
	@Test
	public void testPerformOutcomeThreeParams() {
		String[] parameters = {"pushd", "/dir", "two"};
		PushDirOntoStack.performOutcome(shell, parameters, 0, null);
		assertEquals("pushd: Invalid number of arguments.",
				consoleStreamCaptor.toString().trim());
	}
	
	@Test
	public void testPerformOutcomeThreeParams2() {
		String[] parameters = {"pushd", "dir2", "dir3"};
		PushDirOntoStack.performOutcome(shell, parameters, 0, null);
		assertEquals("pushd: Invalid number of arguments.",
				consoleStreamCaptor.toString().trim());
	}
	
	@Test
	public void testPerformOutcomePushTheRootOntoStack() {
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
		//    At this point: the storage system looks like this:
		//    Say we want to cd into dir2
		//
		//    / <-- CURRENT DIRECTORY
		//        file1
		//        file2
		//        dir1
		//	          file3
		//	          dir3
		//		          dir4
		//        dir2 <-- GOAL
		//	          file4
		String[] parameters = {"pushd", "dir2"};
		PushDirOntoStack.performOutcome(shell, parameters, 0, null);
		// Test that the stack picked up the root and the shell changed its
		// current directory to dir2
		assertEquals(dir2, shell.getCurrentDir());
		assertEquals(shell.getRootDir(), shell.getDirStack().firstElement());
	}
	
	@Test
	public void testPerformOutcomePushNonRootDirOntoStack() {
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
		shell.setCurrentDir(dir2);
		//    At this point: the storage system looks like this:
		//    Say we want to cd into dir1
		//
		//    /
		//        file1
		//        file2
		//        dir1 <-- GOAL
		//	          file3
		//	          dir3
		//		          dir4
		//        dir2 <-- CURRENT DIRECTORY
		//	          file4
		String[] parameters = {"pushd", "/dir1"};
		PushDirOntoStack.performOutcome(shell, parameters, 0, null);
		// Test that the stack picked up the root and the shell changed its
		// current directory to dir2
		assertEquals(dir1, shell.getCurrentDir());
		assertEquals(dir2, shell.getDirStack().firstElement());
	}

	@Test
	public void testPerformOutcomeWithAbsPath() {
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
		shell.setCurrentDir(dir2);
		//    At this point: the storage system looks like this:
		//    Say we want to cd into dir1
		//
		//    /
		//        file1
		//        file2
		//        dir1
		//	          file3
		//	          dir3
		//		          dir4 <-- GOAL
		//        dir2 <-- CURRENT DIRECTORY
		//	          file4
		String[] parameters = {"pushd", "../dir1/dir3/dir4"};
		PushDirOntoStack.performOutcome(shell, parameters, 0, null);
		// Test that the stack picked up the root and the shell changed its
		// current directory to dir2
		assertEquals(dir4, shell.getCurrentDir());
		assertEquals(dir2, shell.getDirStack().firstElement());
	}
	
	@Test
	public void testPerformOutcomeWithRelPath() {
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
		shell.setCurrentDir(dir2);
		//    At this point: the storage system looks like this:
		//    Say we want to cd into dir1
		//
		//    /
		//        file1
		//        file2
		//        dir1
		//	          file3
		//	          dir3
		//		          dir4 <-- GOAL
		//        dir2 <-- CURRENT DIRECTORY
		//	          file4
		String[] parameters = {"pushd", "/dir1/dir3/dir4"};
		PushDirOntoStack.performOutcome(shell, parameters, 0, null);
		// Test that the stack picked up the root and the shell changed its
		// current directory to dir2
		assertEquals(dir4, shell.getCurrentDir());
		assertEquals(dir2, shell.getDirStack().firstElement());
	}
}
