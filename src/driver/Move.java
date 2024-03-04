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

import java.util.ArrayList;

/**
 * The Move class is responsible for both moving, renaming and overwriting
 * StorageUnits according to the user's specifications
 */
public class Move extends ShellCommand {

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
		return "mv OLDPATH NEWPATH\n"
				+ "Move item OLDPATH to NEWPATH. Both OLD-PATH and NEWPATH may "
				+ "be " + "relative to \nthe current directory or may be "
				+ "full paths. If "
				+ "NEWPATH is a directory, move \nthe item into the directory.";
	}

	/**
	 * Tells the JShell to move, rename, overwrite or any combination of the
	 * three, a StorageUnit to another StorageUnit
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

		StorageUnit toMove = new Path(parameters[1]).verifyPath(shell, false);
		StorageUnit moveHere = new Path(parameters[2]).verifyPath(shell, false);
		Directory moveHereParent = (Directory) new Path(parameters[2])
				.verifyPath(shell, true);

		if (toMove == null) {
			PrintError.reportError(shell, "mv", "Cannot stat '" + parameters[1]
					+ "': No such file or directory");
			return;
		}
		if (moveHere != null) {
			executeExistingPath(toMove, moveHere, moveHereParent, shell,
					parameters);
		} else if (moveHereParent != null) {
			executeNewPath(toMove, moveHere, moveHereParent, shell, parameters);
		} else {
			PrintError.reportError(shell, parameters[0],
					"Cannot stat '" + parameters[2] + "': Path is invalid.");
		}
	}

	/**
	 * Helper function for performOutcome, executes the case where the
	 * destination path (moveHere) is an existing StorageUnit, moves/overwrites
	 * toMove to moveHere
	 * 
	 * @param toMove
	 *            The StorageUnit that is to be moved
	 * @param moveHere
	 *            The StorageUnit where toMove is moved to/overwritten
	 * @param moveHereParent
	 *            The directory which contains moveHere
	 * @param shell
	 *            The JShell where the command is being performed on
	 * @param parameters
	 *            The parameters from the interpreter the command is to work
	 *            with
	 */
	private static void executeExistingPath(StorageUnit toMove,
			StorageUnit moveHere, Directory moveHereParent, JShell shell,
			String[] parameters) {
		if (toMove == moveHere) {
			PrintError.reportError(shell, "mv", "'" + parameters[1] + "' and '"
					+ parameters[2] + "' are the same file");
			return;
		}
		if (moveHere.isDirectory()) {
			if (!TransferFile.validateNames(toMove, (Directory) moveHere, shell,
					parameters)) {
				return;
			} else if (toMove.isDirectory()
					&& TransferFile.validateParents((Directory) toMove,
							(Directory) moveHere, shell, parameters)) {
				move(toMove, (Directory) moveHere);
			} else if (toMove.isFile()) {
				move(toMove, (Directory) moveHere);
			} else {
				return;
			}
		} else if (moveHere.isFile()) {
			if (!TransferFile.validateFileToFile(toMove, shell, parameters)) {
				return;
			}
			moveAndOverwriteFile((File) toMove, (File) moveHere);
		}
	}

	/**
	 * Helper function for performOutcome, executes the case where the
	 * destination path (moveHere) is a new StorageUnit, moves and
	 * renames/overwrites toMove to moveHere
	 * 
	 * @param toMove
	 *            The StorageUnit that is to be moved
	 * @param moveHere
	 *            The StorageUnit where toMove is to overwrite/create
	 * @param moveHereParent
	 *            The directory which is to contain moveHere
	 * @param shell
	 *            The JShell where the command is being performed on
	 * @param parameters
	 *            The parameters from the interpreter the command is to work
	 *            with
	 */
	private static void executeNewPath(StorageUnit toMove, StorageUnit moveHere,
			Directory moveHereParent, JShell shell, String[] parameters) {
		StorageUnit moveHereNew = TransferFile
				.determineNewStorageUnit(parameters[2]);
		Path destPath = new Path(parameters[2]);
		String newStorageUnitName = destPath
				.getPathElements()[destPath.getPathElements().length - 1];
		if (toMove.isDirectory() && TransferFile.validateParents(
				(Directory) toMove, moveHereParent, shell, parameters)) {
			moveAndOverwriteDir(toMove, moveHereParent, newStorageUnitName);
		} else if (toMove.isFile() && moveHereNew.isFile()) {
			moveAndCreateFile((File) toMove, (File) moveHereNew,
					moveHereParent);
		} else {
			PrintError.reportError(shell, parameters[0], "Cannot move file '"
					+ parameters[1] + "' into a directory that doesn't exist.");
		}
	}

	/**
	 * Moves StorageUnit toMove inside of Directory moveHere
	 * 
	 * @param toMove
	 *            The StorageUnit to be moved
	 * @param moveHere
	 *            The destination of toMove
	 */
	private static void move(StorageUnit toMove, Directory moveHere) {

		ArrayList<StorageUnit> parentContents = toMove.getParentDir()
				.getDirContents();
		int indexRemove = -1;
		for (int i = 0; i < parentContents.size(); i++) {
			if (parentContents.get(i).name.equals(toMove.name)) {
				indexRemove = i;
				break;
			}
		}
		toMove.getParentDir().getDirContents().remove(indexRemove);
		moveHere.addFile(toMove);
	}

	/**
	 * Moves StorageUnit toMove inside Directory moveHereParent, renames toMove
	 * to newName
	 * 
	 * @param toMove
	 *            The StorageUnit to be moved and renamed
	 * @param moveHereParent
	 *            The directory toMove is to be moved into
	 * @param newName
	 *            The new name toMove is given
	 */
	private static void moveAndOverwriteDir(StorageUnit toMove,
			Directory moveHereParent, String newName) {

		move(toMove, moveHereParent);
		toMove.setName(newName);
	}

	/**
	 * Overwrites the contents of moveHere with the contents of toMove, removes
	 * toMove in the process
	 * 
	 * @param toMove
	 *            The file that is to overwrite moveHere
	 * @param moveHere
	 *            The file is that is to be overwritten
	 */
	private static void moveAndOverwriteFile(File toMove, File moveHere) {

		ArrayList<StorageUnit> parentContents = toMove.getParentDir()
				.getDirContents();
		parentContents.remove(toMove);

		moveHere.overwrite(toMove.getContents());
	}

	/**
	 * Creates new File at moveHereParent with the contents of toMove, and
	 * removes toMove
	 * 
	 * @param toMove
	 *            The file that is to be moved
	 * @param moveHereNew
	 *            The file with the name of the file to be created
	 * @param moveHereParent
	 *            The directory to hold the file to be created
	 */
	private static void moveAndCreateFile(File toMove, File moveHereNew,
			Directory moveHereParent) {

		ArrayList<StorageUnit> parentContents = toMove.getParentDir()
				.getDirContents();
		parentContents.remove(toMove);

		moveHereParent.addFile(new File(moveHereNew.getName(),
				toMove.getContents(), moveHereParent));
	}
}
