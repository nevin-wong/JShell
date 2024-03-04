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

/**
 * A StdOut is a stream that a command that produces output uses to send output
 * to their destinations.
 */
public class StdOut {

	/**
	 * An integer representing the type of destination: 0 represents the command
	 * line, 1 represents overwriting a file, and 2 represents appending to a
	 * file.
	 */
	private int destinationType;
	/**
	 * If destinationType is 1 or 2, this is the File that is to be
	 * overwritten/appended to. Otherwise, it is null and we know the actual
	 * destination is shell's command line.
	 */
	private File destination;
	/** The JShell of the destination */
	private JShell shell;

	/**
	 * Initializes an instance of a StdOut
	 * 
	 * @param shell
	 *            The JShell of the destination of the StdOut
	 * @param type
	 *            An integer representing the type of destination
	 * @param destination
	 *            If type is 1 or 2, this is the File that is to be
	 *            overwritten/appended to. Otherwise, it's null.
	 */
	public StdOut(JShell shell, int type, File destination) {
		this.shell = shell;
		this.destinationType = type;
		this.destination = destination;
		if (destinationType == 1) {
			// If it's meant to be overwritten, empty it out first
			destination.overwrite("");
		}
	}

	/**
	 * Sends a given message to this StdOut's destination as a line.
	 * 
	 * @param message
	 *            The String to be sent to the destination of the StdOut
	 */
	public void sendLine(String message) {
		if (this.destinationType == 0) {
			this.shell.println(message);
		} else {
			this.destination.append(message + "\n");
		}
	}

	/**
	 * Sends a given message to this StdOut's destination.
	 * 
	 * @param message
	 *            The String to be sent to the destination of the StdOut
	 */
	public void send(String message) {
		if (this.destinationType == 0) {
			this.shell.print(message);
		} else {
			this.destination.append(message);
		}
	}

	/**
	 * Closes the StdOut stream. Used to delete the unnecessary newline at the
	 * end if the destination is a file.
	 */
	public void closeStream() {
		// i.e. if the destination is a file and its last character is a
		// newline, get rid of it
		if (this.destinationType != 0
				&& this.destination.getContents().length() > 0
				&& this.destination.getContents().charAt(
						this.destination.getContents().length() - 1) == '\n') {
			this.destination.overwrite(this.destination.getContents()
					.substring(0, this.destination.getContents().length() - 1));
		}
	}
}
