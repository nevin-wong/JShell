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

package driver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

/**
 * JShell is an interactive Unix-like shell that allows a user to use nineteen
 * different commands to manipulate a virtual file system containing files and
 * directories.
 */

public class JShell implements Serializable, JShellInterface {

	/** Root directory stores everything */
	private Storage rootDir;
	/** Current directory user is working in */
	private Directory currentDir;
	/** Whether the JShell is active, turned off if the user wishes to exit */
	private transient boolean isActive;
	/** Directory stack of the shell */
	private Stack<Directory> dirStack;
	/**
	 * HashMap that maps commands to the Class that represents it e.g. ls maps
	 * to ListFiles
	 */
	private HashMap<String, Class> cmdToClass;
	/** Collection of the entire history of commands */
	private CommandHistory comHis;

	/**
	 * CommandHistory is a nested class that JShell uses to store all the
	 * commands the user has typed into the command line in chronological order.
	 * Iterator Design Pattern is used to iterate through a CommandHistory.
	 */
	public static class CommandHistory
			implements
				Iterable<String>,
				Serializable {
		/** The ArrayList used to store the commands in chronological order */
		private ArrayList<String> cmds;

		/**
		 * Initializes a new instance of CommandHistory
		 */
		public CommandHistory() {
			this.cmds = new ArrayList<String>();
		}

		/**
		 * Gets the ArrayList of commands for testing purposes.
		 * 
		 * @return The ArrayList of commands
		 */
		public ArrayList<String> getCmds() {
			return this.cmds;
		}

		/**
		 * Used to determine the number of commands
		 * 
		 * @return The number of commands in this CommandHistory
		 */
		public int getSize() {
			return this.cmds.size();
		}

		/**
		 * Used to add a command into this CommandHistory
		 * 
		 * @param command
		 *            The command to be added
		 */
		private void add(String command) {
			this.cmds.add(command);
		}

		/**
		 * ChronologicalOrderIterator is a nested nested class that iterates
		 * through a CommandHistory in the order that the user has typed their
		 * commands in.
		 */
		private static class ChronologicalOrderIterator implements Iterator {

			/** The current index of the iterator */
			int currIndex;
			/** The ArrayList to iterate through */
			ArrayList<String> toIterate;

			/**
			 * Constructor for a ChronologicalOrderIterator
			 * 
			 * @param toIterate
			 *            The ArrayList to iterate through
			 */
			public ChronologicalOrderIterator(ArrayList<String> toIterate) {
				this.currIndex = 0;
				this.toIterate = toIterate;
			}

			/**
			 * Checks if there is a next command in the ArrayList
			 */
			@Override
			public boolean hasNext() {
				return (currIndex < toIterate.size());
			}

			/**
			 * Gives the next command of the current iteration
			 */
			@Override
			public Object next() {
				currIndex = currIndex + 1;
				return toIterate.get(currIndex - 1);
			}

		}

		/**
		 * Returns an appropriate Iterator used to iterate through this
		 * CommandHistory
		 */
		public Iterator<String> iterator() {
			return new ChronologicalOrderIterator(cmds);
		}
	}

	/**
	 * Initializes an instance of the JShell, initializes all private variables
	 */
	public JShell() {
		this.rootDir = Storage.createNewStorage();
		this.currentDir = this.rootDir.getRoot(); // by default current
													// directory is root
		this.dirStack = new Stack<Directory>();
		this.isActive = true;
		this.cmdToClass = new HashMap<String, Class>();
		this.populateCmdToClass();
		this.comHis = new CommandHistory();
	}

	/**
	 * Used by loadJShell command to update this JShell to a previously saved
	 * one.
	 * 
	 * @param newShell
	 *            The new JShell to up updated to.
	 */
	public void updateShell(JShell newShell) {
		this.rootDir = newShell.rootDir;
		this.currentDir = newShell.currentDir;
		this.dirStack = newShell.dirStack;
		this.cmdToClass = newShell.cmdToClass;
		this.comHis = newShell.comHis;
	}

	/**
	 * Checks if another Object is equal to this JShell
	 * 
	 * @param o
	 *            The other Object
	 * @return If they are equal
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		JShell jShell = (JShell) o;
		return rootDir.equals(jShell.rootDir)
				&& currentDir.equals(jShell.currentDir)
				&& dirStack.equals(jShell.dirStack)
				&& cmdToClass.equals(jShell.cmdToClass)
				&& comHis.equals(jShell.comHis);
	}

	/**
	 * Provides the hashCode for this JShell
	 * 
	 * @return The hashCode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(rootDir, currentDir, dirStack, cmdToClass, comHis);
	}

	/**
	 * Helper method for JShell initializer, populates the JShell's command to
	 * Class HashMap
	 */
	private void populateCmdToClass() {
		try { // Would need to update only this if/when there come new commands
			this.cmdToClass.put("exit", Class.forName("driver.Exit"));
			this.cmdToClass.put("mkdir", Class.forName("driver.MakeDirectory"));
			this.cmdToClass.put("cd", Class.forName("driver.ChangeDirectory"));
			this.cmdToClass.put("ls", Class.forName("driver.ListFiles"));
			this.cmdToClass.put("pwd",
					Class.forName("driver.PrintWorkingDirectory"));
			this.cmdToClass.put("pushd",
					Class.forName("driver.PushDirOntoStack"));
			this.cmdToClass.put("popd",
					Class.forName("driver.PopDirFromStack"));
			this.cmdToClass.put("history",
					Class.forName("driver.PrintHistory"));
			this.cmdToClass.put("cat", Class.forName("driver.ConcatenateFile"));
			this.cmdToClass.put("echo", Class.forName("driver.Echo"));
			this.cmdToClass.put("man", Class.forName("driver.Manual"));
			this.cmdToClass.put("saveJShell",
					Class.forName("driver.SaveJShell"));
			this.cmdToClass.put("loadJShell",
					Class.forName("driver.LoadJShell"));
			this.cmdToClass.put("search", Class.forName("driver.Search"));
			this.cmdToClass.put("tree", Class.forName("driver.Tree"));
			this.cmdToClass.put("rm", Class.forName("driver.Remove"));
			this.cmdToClass.put("mv", Class.forName("driver.Move"));
			this.cmdToClass.put("cp", Class.forName("driver.Copy"));
			this.cmdToClass.put("curl", Class.forName("driver.ClientURL"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Public getter method for the current directory
	 * 
	 * @return The current directory of the shell
	 */
	public Directory getCurrentDir() {
		return this.currentDir;
	}

	/**
	 * Public setter method for the current directory
	 * 
	 * @param currentDir
	 *            The given directory to be the new current directory
	 */
	public void setCurrentDir(Directory currentDir) {
		this.currentDir = currentDir;
	}

	/**
	 * Public getter method for the root directory
	 * 
	 * @return The root directory of the shell that lives in the single instance
	 *         of Storage
	 */
	public Directory getRootDir() {
		return this.rootDir.getRoot();
	}

	/**
	 * Public getter method for the directory stack
	 * 
	 * @return The directory stack of the shell
	 */
	public Stack<Directory> getDirStack() {
		return this.dirStack;
	}

	/**
	 * Public getter method for the command to Class HashMap
	 * 
	 * @return The command to Class HashMap
	 */
	public HashMap<String, Class> getCmdToClass() {
		return this.cmdToClass;
	}

	/**
	 * Public getter method for the history of commands
	 *
	 * @return The CommandHistory of the history of commands
	 */
	public CommandHistory getComHis() {
		return this.comHis;
	}

	/**
	 * Public setter method for appending to the history of commands
	 * 
	 * @param command
	 *            The command to be added
	 */
	public void addCom(String command) {
		this.comHis.add(command);
	}

	/**
	 * Public getter method for isActive
	 * 
	 * @return Whether this JShell is active
	 */
	public boolean isActive() {
		return this.isActive;
	}

	/**
	 * Public exit method which the ShellCommand Exit uses
	 */
	public void exit() {
		this.isActive = false;
	}

	/**
	 * Prints a given message as a line on the shell
	 * 
	 * @param message
	 *            The message to be printed as a line
	 */
	public void println(String message) {
		System.out.println(message);
	}

	/**
	 * Prints a given message as a on the shell
	 * 
	 * @param message
	 *            The message to be printed
	 */
	public void print(String message) {
		System.out.print(message);
	}

	/**
	 * Prints a given error message on the shell
	 * 
	 * @param errMsg
	 *            The error message to be printed
	 */
	public void printError(String errMsg) {
		System.out.println(errMsg);
	}

	/**
	 * Continually prompts the user for input and sends to Interpreter
	 */
	public void run() {
		String userCommand;
		Scanner userInput = new Scanner(System.in);
		while (this.isActive) {
			this.print(currentDir.getFullPath(this).getPath() + "> ");
			userCommand = userInput.nextLine();
			addCom(userCommand);
			Interpreter.interpret(userCommand, this);
		}
		userInput.close();
	}

	/**
	 * Creates a new instance of the JShell and runs it
	 */
	public static void main(String[] args) {
		JShell shell = new JShell();
		shell.run();
	}
}