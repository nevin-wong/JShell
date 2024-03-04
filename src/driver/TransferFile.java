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
 * The TransferFile class contains helper methods for the Move and Copy class
 */
public class TransferFile {

	/**
	 * Checks the number of parameters the user has entered, and prints an error
	 * message if the number is invalid
	 * 
	 * @param shell
	 *            The JShell where the paths should be located
	 * @param operation
	 *            The string "mv" or "cp", depending on what the user called
	 * @param paramLength
	 *            Number of parameters user entered
	 * @return True, if paramLength == 3, False otherwise
	 */
	public static boolean validateNumberOfParameters(JShellInterface shell,
			String operation, int paramLength) {
		if (paramLength != 3) {
			PrintError.reportError(shell, operation,
					"Invalid number of parameters");
			return false;
		}
		return true;
	}

	/**
	 * Checks whether toMove is an ancestor of moveHere, and prints an error if
	 * it is
	 * 
	 * @param toMove
	 *            The directory that is checked to be an ancestor of toMove
	 * @param moveHere
	 *            The directory that toMove is compared to
	 * @param shell
	 *            The JShell toMove and moveHere are located in
	 * @param parameters
	 *            The command the user entered on the command line
	 * @return True if toMove is not an ancestor of moveHere, False otherwise
	 */
	public static boolean validateParents(Directory toMove, Directory moveHere,
			JShellInterface shell, String[] parameters) {
		if (toMove == null
				|| moveHere == null | toMove.checkParents(moveHere, shell)) {
			PrintError.reportError(shell, parameters[0],
					"Cannot move/copy an ancestor directory into a "
							+ "child directory");
			return false;
		}
		return true;
	}

	/**
	 * Checks if the name of toMove is equal to any of the names of the
	 * StorageUnits in moveHere, and prints an error if it is
	 * 
	 * @param toMove
	 *            The StorageUnit that has the name we want to check uniqueness
	 *            for
	 * @param moveHere
	 *            The directory where its contents are checked against toMove
	 * @param shell
	 *            The JShell where toMove and moveHere are located in
	 * @param parameters
	 *            The command the user entered on the command line
	 * @return True if toMove does not share a name with any of the contents of
	 *         moveHere
	 */
	public static boolean validateNames(StorageUnit toMove, Directory moveHere,
			JShellInterface shell, String[] parameters) {
		if (toMove == null || moveHere == null
				|| toMove.checkIdenticalNames(moveHere)) {
			PrintError.reportError(shell, parameters[0],
					"Cannot move/copy '" + parameters[1] + "' into '"
							+ parameters[2]
							+ "' since a file/directory with the same "
							+ "name already exists in '" + parameters[2] + "'");
			return false;
		}
		return true;
	}

	/**
	 * Checks if toMove is a file, and prints an error if it is not
	 * 
	 * @param toMove
	 *            The StorageUnit that is checked whether of not it is a file
	 * @param shell
	 *            The JShell toMove lives in
	 * @param parameters
	 *            The command the user entered on the command line
	 * @return True if toMove is a file, false otherwise
	 */
	public static boolean validateFileToFile(StorageUnit toMove,
			JShellInterface shell, String[] parameters) {
		if (toMove != null && toMove.isFile()) {
			return true;
		} else {
			PrintError.reportError(shell, parameters[0],
					"Cannot overwrite non-directory '" + parameters[2]
							+ "' with directory '" + parameters[1] + "'");
			return false;
		}
	}

	/**
	 * Checks whether dest is a new file path or a new directory path and
	 * returns a StorageUnit with same name as the element at the end of the
	 * path
	 * 
	 * @param dest
	 *            The path the user entered
	 * @return A File/Directory with the name of the final element of the path
	 *         depending on whether the path is a file or directory path
	 */
	public static StorageUnit determineNewStorageUnit(String dest) {
		Path moveHerePath = new Path(dest);
		String newStorageUnitName;
		if (moveHerePath.getPathElements().length > 0) {
			newStorageUnitName = moveHerePath
					.getPathElements()[moveHerePath.getPathElements().length
							- 1];
		} else {
			return null;
		}
		if (dest.endsWith("/")) {
			return new Directory(newStorageUnitName, null);
		} else {
			return new File(newStorageUnitName, null, null);
		}
	}
}
