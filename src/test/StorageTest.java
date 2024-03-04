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

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import driver.Storage;

public class StorageTest {

	Storage storage;

	@Before
	public void setUp() throws Exception {
		storage = Storage.createNewStorage();
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
	}

	/**
	 * Ensure the root is not null after initialization and that its name is /
	 */
	@Test
	public void testStorageInitialization() {
		assertFalse(storage.getRoot().equals(null));
		assertEquals("/", storage.getRoot().getName());
	}

	/**
	 * Test that if multiple storages are created, they are the same instance
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateNewStorage() throws Exception {
		Storage storage2 = Storage.createNewStorage();
		Storage storage3 = Storage.createNewStorage();
		Storage storage4 = Storage.createNewStorage();
		assertTrue(storage == storage2);
		assertTrue(storage2 == storage3);
		assertTrue(storage3 == storage4);
		assertTrue(storage4 == storage);
	}
}
