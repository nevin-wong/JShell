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

import driver.ListFiles;
import driver.LoadJShell;
import org.junit.Before;
import org.junit.Test;
import driver.JShell;

public class LoadJShellTest {

	JShell shell;

	@Before
	public void setUp() {
		shell = new JShell();
	}

	@Test
	public void testGetManual() {
		String manual;
		manual = LoadJShell.getManual();
		assertEquals(manual, "loadJShell localFilePath\n"
				+ "Loads a previously saved JShell session onto a fresh "
				+ "JShell sessions.\n"
				+ "This command only works on fresh JShell sessions.");
	}

	@Test
	public void testPerformOutcome() {
		LoadJShell.performOutcome(shell,
				new String[]{"loadJShell", "./test.txt"}, 0, null);
		ListFiles.performOutcome(shell, new String[]{"ls"}, 0, null);
	}
}
