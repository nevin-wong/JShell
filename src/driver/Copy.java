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
 * The Copy class is responsible for both copying and overwriting StorageUnits
 * according to the user's specifications
 */
public class Copy extends ShellCommand {

	/**
	 * Returns if this command produces StdOut. (used by the Interpreter to know
	 * whether or not to make a new file)
	 * 
	 * @return Whether or not the command produces StdOut
	 */
	public static boolean producesStdOut() {
		return false;
	}

	/**
	 * Provides the manual for how to use this command
	 *
	 * @return The manual
	 */
	public static String getManual() {
		return "cp OLDPATH NEWPATH\n"
				+ "Copy item OLDPATH to NEWPATH. Both OLD-PATH and NEWPATH may "
				+ "be " + "relative to \nthe current directory or may be "
				+ "full paths. If "
				+ "NEWPATH is adirectory, copy \nthe item into the directory.";
	}

	/**
	 * Tells the JShell to copy, overwrite or copy and overwrite, a StorageUnit
	 * to another StorageUnit
	 * 
	 * @param shell
	 *            The JShell the command is to be performed on
	 * @param parameters
	 *            The parameters from the interpreter the command is to work
	 *            with
	 * @param outputType
	 *            An integer representing the type of destination: 0 represents
	 *            the command line, 1 represents overwriting a file, and 2
	 *            represents appending to a file
	 * @param outputFile
	 *            If outputType is 1 or 2, this is the file we are
	 *            overwriting/appending to, otherwise null
	 */
	public static void performOutcome(JShell shell, String[] parameters,
			int outputType, File outputFile) {

		if (!TransferFile.validateNumberOfParameters(shell, parameters[0],
				parameters.length)) {
			return;
		}

		StorageUnit toCopy = new Path(parameters[1]).verifyPath(shell, false);
		StorageUnit copyHere = new Path(parameters[2]).verifyPath(shell, false);
		Directory copyHereParent = (Directory) new Path(parameters[2])
				.verifyPath(shell, true);

		if (toCopy == null) {
			PrintError.reportError(shell, "mv", "Cannot stat '" + parameters[1]
					+ "': No such file or directory");
			return;
		}
		if (copyHere != null) {
			executeExistingPath(copyHere, toCopy, shell, parameters);
		} else if (copyHereParent != null) {
			executeNewPath(copyHere, toCopy, copyHereParent, shell, parameters);
		} else {
			PrintError.reportError(shell, parameters[0],
					"Cannot stat '" + parameters[2] + "': Path is invalid.");
		}

	}

	/**
	 * Helper function for performOutcome, executes the case where the
	 * destination path (copyHere) is an existing StorageUnit, copies/overwrites
	 * toCopy to copyHere
	 * 
	 * @param toCopy
	 *            The StorageUnit that is to be copied
	 * @param copyHere
	 *            The StorageUnit where toCopy is copied to/overwritten
	 * @param copyHereParent
	 *            The directory which contains copyHere
	 * @param shell
	 *            The JShell where the command is being performed on
	 * @param parameters
	 *            The parameters from the interpreter the command is to work
	 *            with
	 */
	private static void executeExistingPath(StorageUnit copyHere,
			StorageUnit toCopy, JShell shell, String[] parameters) {
		if (toCopy == copyHere) {
			PrintError.reportError(shell, "mv", "'" + parameters[1] + "' and '"
					+ parameters[2] + "' are the same file");
			return;
		}
		if (copyHere.isDirectory()) {
			if (!TransferFile.validateNames(toCopy, (Directory) copyHere, shell,
					parameters)) {
				return;
			} else if (toCopy.isDirectory()
					&& TransferFile.validateParents((Directory) toCopy,
							(Directory) copyHere, shell, parameters)) {
				copy(toCopy, (Directory) copyHere);
			} else if (toCopy.isFile()) {
				copy(toCopy, (Directory) copyHere);
			} else {
				return;
			}
		} else if (copyHere.isFile()) {
			if (!TransferFile.validateFileToFile(copyHere, shell, parameters)) {
				return;
			}
			copyAndOverwriteFile((File) toCopy, (File) copyHere);
		}
	}

	/**
	 * Helper function for performOutcome, executes the case where the
	 * destination path (copyHere) is a new StorageUnit, copies and overwrites
	 * toCopy to copyCopy
	 * 
	 * @param toCopy
	 *            The StorageUnit that is to be copied
	 * @param copyHere
	 *            The StorageUnit where toCopy is to overwrite/create
	 * @param copyHereParent
	 *            The directory which is to contain copyHere
	 * @param shell
	 *            The JShell where the command is being performed on
	 * @param parameters
	 *            The parameters from the interpreter the command is to work
	 *            with
	 */
	private static void executeNewPath(StorageUnit copyHere, StorageUnit toCopy,
			Directory copyHereParent, JShell shell, String[] parameters) {
		StorageUnit copyHereNew = TransferFile
				.determineNewStorageUnit(parameters[2]);
		Path destPath = new Path(parameters[2]);
		String newStorageUnitName = destPath
				.getPathElements()[destPath.getPathElements().length - 1];
		if (toCopy.isDirectory() && TransferFile.validateParents(
				(Directory) toCopy, copyHereParent, shell, parameters)) {
			copyAndOverwriteDir(toCopy, copyHereParent, newStorageUnitName);
		} else if (toCopy.isFile() && copyHereNew.isFile()) {
			copyAndCreateFile((File) toCopy, (File) copyHereNew,
					copyHereParent);
		} else {
			PrintError.reportError(shell, parameters[0], "Cannot copy file '"
					+ parameters[1] + "' into a directory that doesn't exist.");
		}
	}

	/**
	 * Copies StorageUnit toCopy inside of Directory copyHere
	 * 
	 * @param toCopy
	 *            The StorageUnit to be copied
	 * @param copyHere
	 *            The destination of toCopy
	 */
	public static void copy(StorageUnit toCopy, Directory copyHere) {
		StorageUnit toAdd = toCopy.clone((Directory) copyHere);
		copyHere.addFile(toAdd);
	}

	/**
	 * Copies StorageUnit toCopy inside Directory copyHereParent, renames toCopy
	 * to newName
	 * 
	 * @param toCopy
	 *            The StorageUnit to be copied and renamed
	 * @param copyHereParent
	 *            The directory toCopy is to be copied into
	 * @param newName
	 *            The new name toCopy is given
	 */
	private static void copyAndOverwriteDir(StorageUnit toCopy,
			Directory copyHereParent, String newName) {

		copy(toCopy, copyHereParent);
		toCopy.setName(newName);
	}

	/**
	 * Overwrites the contents of copyHere with the contents of toCopy
	 * 
	 * @param toCopy
	 *            The file that is to overwrite copyHere
	 * @param copyHere
	 *            The file is that is to be overwritten
	 */

	public static void copyAndOverwriteFile(File toCopy, File copyHere) {
		copyHere.overwrite(toCopy.getContents());
	}

	/**
	 * Creates new File at copyHereParent with the contents of toCopy
	 * 
	 * @param toCopy
	 *            The file that is to have its contents copied
	 * @param copyHereNew
	 *            The file with the name of the file to be created
	 * @param copyHereParent
	 *            The directory to hold the file to be created
	 */
	private static void copyAndCreateFile(File toCopy, File copyHereNew,
			Directory copyHereParent) {

		copyHereParent.addFile(new File(copyHereNew.getName(),
				toCopy.getContents(), copyHereParent));
	}
}
