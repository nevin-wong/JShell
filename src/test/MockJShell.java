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

import java.util.HashMap;
import java.util.Stack;

import driver.Directory;
import driver.JShellInterface;

public class MockJShell implements JShellInterface {

	Directory currDir;
	Directory rootDir;
	Stack<Directory> dirStack;

	@Override
	public Directory getRootDir() {
		rootDir.setParentDir(rootDir);
		return rootDir;
	}

	@Override
	public Directory getCurrentDir() {
		return currDir;
	}

	public MockJShell() {
		rootDir = new Directory("/", null);
		rootDir.setParentDir(rootDir);
		currDir = rootDir;
		dirStack = new Stack<Directory>();

	}

	@Override
	public void setCurrentDir(Directory dir) {
		this.currDir = dir;
	}

	@Override
	public void printError(String string) {
	}

	@Override
	public Stack<Directory> getDirStack() {
		return dirStack;
	}

	public void exit() {
		// TODO Auto-generated method stub
		
	}
}
