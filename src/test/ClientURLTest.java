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
import org.junit.Before;
import org.junit.Test;

import driver.*;

public class ClientURLTest {

	JShell shell;
	Directory root, dir1;
	File file1;
	File stdOutFile;

	/** Used to test print statements */
	private final PrintStream printed = System.out;
	private final ByteArrayOutputStream consoleStreamCaptor = 
			new ByteArrayOutputStream();

	String txtContents = "There was once a king who had an illness, and no "
			+ "one believed that he\n"
			+ "would come out of it with his life.  He had three sons w"
			+ "ho were much\n"
			+ "distressed about it, and went down into the palace-garde"
			+ "n and wept.\n"
			+ "There they met an old man who inquired as to the cause "
			+ "of their\n"
			+ "grief.  They told him that their father was so ill that "
			+ "he would most\n"
			+ "certainly die, for nothing seemed to cure him.  Then the"
			+ " old man\n"
			+ "said, \"I know of one more remedy, and that is the water"
			+ " of life.  If\n"
			+ "he drinks of it he will become well again, but it is hard"
			+ " to find.\"\n"
			+ "The eldest said, \"I will manage to find it.\" And went t"
			+ "o the sick\n"
			+ "king, and begged to be allowed to go forth in search of t"
			+ "he water of\n"
			+ "life, for that alone could save him.  \"No,\" said the k"
			+ "ing, \"the\n"
			+ "danger of it is too great.  I would rather die.\"\n" + "\n"
			+ "But he begged so long that the king consented.  The prince"
			+ " thought in\n"
			+ "his heart, \"If I bring the water, then I shall be best be"
			+ "loved of my\n"
			+ "father, and shall inherit the kingdom.\" So he set out, an"
			+ "d when he\n"
			+ "had ridden forth a little distance, a dwarf stood there in "
			+ "the road\n"
			+ "who called to him and said, \"Whither away so fast?\" \"Sil"
			+ "ly shrimp,\"\n"
			+ "said the prince, very haughtily, \"it is nothing to do wit"
			+ "h you.\" And\n"
			+ "rode on.  But the little dwarf had grown angry, and had wi"
			+ "shed an\n"
			+ "evil wish.  Soon after this the prince entered a ravine, "
			+ "and the\n"
			+ "further he rode the closer the mountains drew together, a"
			+ "nd at last\n"
			+ "the road became so narrow that he could not advance a step"
			+ " further.\n"
			+ "It was impossible either to turn his horse or to dismount "
			+ "from the\n"
			+ "saddle, and he was shut in there as if in prison.  The sic"
			+ "k king\n" + "waited long for him, but he came not.\n" + "\n"
			+ "Then the second son said, \"father, let me go forth to se"
			+ "ek the\n"
			+ "water.\" And thought to himself, \"If my brother is dead,"
			+ " then the\n"
			+ "kingdom will fall to me.\" At first the king would not al"
			+ "low him to go\n"
			+ "either, but at last he yielded, so the prince set out on "
			+ "the same\n"
			+ "road that his brother had taken, and he too met the dwa"
			+ "rf, who\n" + "stopped him to ask whither he was going in such h"
			+ "aste.  \"Little\n"
			+ "shrimp,\" said the prince, \"that is nothing to do wi"
			+ "th you.\" And rode\n"
			+ "on without giving him another look.  But the dwarf bewitc"
			+ "hed him, and\n"
			+ "he, like the other, rode into a ravine, and could neither g"
			+ "o forwards\n" + "nor backwards.  So fare haughty people.\n"
			+ "\n"
			+ "As the second son also remained away, the youngest begged t"
			+ "o be\n"
			+ "allowed to go forth to fetch the water, and at last the kin"
			+ "g was\n"
			+ "obliged to let him go.  When he met the dwarf and the latte"
			+ "r asked\n"
			+ "him whither he was going in such haste, he stopped, gave hi"
			+ "m an\n"
			+ "explanation, and said, \"I am seeking the water of life, for"
			+ " my father\n" + "is sick unto death.\"\n" + "\n"
			+ "\"Do you know, then, where that is to be found?\"\n" + "\n"
			+ "\"No,\" said the prince.\n" + "\n"
			+ "\"As you have borne yourself as is seemly, and not haughtil"
			+ "y like your\n"
			+ "false brothers, I will give you the information and tell yo"
			+ "u how you\n"
			+ "may obtain the water of life.  It springs from a fountain i"
			+ "n the\n"
			+ "courtyard of an enchanted castle, but you will not be able "
			+ "to make\n"
			+ "your way to it, if I do not give you an iron wand and two s"
			+ "mall\n"
			+ "loaves of bread.  Strike thrice with the wand on the iron d"
			+ "oor of the\n"
			+ "castle and it will spring open, inside lie two lions with g"
			+ "aping\n"
			+ "jaws, but if you throw a loaf to each of them, they will be"
			+ " quieted.\n"
			+ "Then hasten to fetch some of the water of life before the c"
			+ "lock\n"
			+ "strikes twelve else the door will shut again, and you will "
			+ "be\n" + "imprisoned.\"\n" + "\n"
			+ "The prince thanked him, took the wand and the bread, and se"
			+ "t out on\n"
			+ "his way.  When he arrived, everything was as the dwarf had "
			+ "said.  The\n"
			+ "door sprang open at the third stroke of the wand, and when "
			+ "he had\n"
			+ "appeased the lions with the bread, he entered the castle, a"
			+ "nd came to\n"
			+ "a large and splendid hall, wherein sat some enchanted princ"
			+ "es whose\n"
			+ "rings he drew off their fingers.  A sword and a loaf of brea"
			+ "d were\n"
			+ "lying there, which he carried away.  After this, he enter"
			+ "ed a\n"
			+ "chamber, in which was a beautiful maiden who rejoiced when "
			+ "she saw\n"
			+ "him, kissed him, and told him that he had set her free, and "
			+ "should\n"
			+ "have the whole of her kingdom, and that if he would return in"
			+ " a year\n"
			+ "their wedding should be celebrated.  Likewise she told him wh"
			+ "ere the\n"
			+ "spring of the water of life was, and that he was to hasten a"
			+ "nd draw\n"
			+ "some of it before the clock struck twelve.  Then he went onw"
			+ "ards, and\n"
			+ "at last entered a room where there was a beautiful newly-mad"
			+ "e bed,\n"
			+ "and as he was very weary, he felt inclined to rest a little."
			+ "  So he\n" + "lay down and fell asleep.\n" + "\n"
			+ "When he awoke, it was striking a quarter to twelve.  He spran"
			+ "g up in\n"
			+ "a fright, ran to the spring, drew some water in a cup which "
			+ "stood\n"
			+ "near, and hastened away.  But just as he was passing throug"
			+ "h the iron\n"
			+ "door, the clock struck twelve, and the door fell to with s"
			+ "uch\n" + "violence that it carried away a piece of his heel.\n"
			+ "\n" + "He, however, rejoicing at having obtained the water of li"
			+ "fe, went\n"
			+ "homewards, and again passed the dwarf.  When the latter s"
			+ "aw the sword\n"
			+ "and the loaf, he said, \"With these you have won great we"
			+ "alth, with\n"
			+ "the sword you can slay whole armies, and the bread will n"
			+ "ever come to\n"
			+ "an end.\" But the prince would not go home to his father "
			+ "without his\n"
			+ "brothers, and said, \"Dear dwarf, can you not tell me wh"
			+ "ere my two\n"
			+ "brothers are?  They went out before I did in search of the"
			+ " water of\n" + "life, and have not returned.\"\n" + "\n"
			+ "\"They are imprisoned between two mountains,\" said th"
			+ "e dwarf. \"I have\n"
			+ "condemned them to stay there, because they were so hau"
			+ "ghty.\" Then the\n"
			+ "prince begged until the dwarf released them, but he warn"
			+ "ed him and\n"
			+ "said, \"Beware of them, for they have bad hearts.\" When "
			+ "his brothers\n"
			+ "came, he rejoiced, and told them how things had gone with"
			+ " him, that\n"
			+ "he had found the water of life and had brought a cupful aw"
			+ "ay with\n"
			+ "him, and had rescued a beautiful princess, who was willi"
			+ "ng to wait a\n"
			+ "year for him, and then their wedding was to be celebrated"
			+ " and he\n" + "would obtain a great kingdom.\n" + "\n"
			+ "After that they rode on together, and chanced upon a lan"
			+ "d where war\n"
			+ "and famine reigned, and the king already thought he mus"
			+ "t perish, for\n"
			+ "the scarcity was so great.  Then the prince went to him"
			+ " and gave him\n"
			+ "the loaf, wherewith he fed and satisfied the whole of h"
			+ "is kingdom,\n"
			+ "and then the prince gave him the sword also wherewith h"
			+ "e slew the\n"
			+ "hosts of his enemies, and could now live in rest and pe"
			+ "ace.  The\n"
			+ "prince then took back his loaf and his sword, and the t"
			+ "hree brothers\n"
			+ "rode on.  But after this they entered two more countries w"
			+ "here war\n"
			+ "and famine reigned and each time the prince gave his loaf "
			+ "and his\n"
			+ "sword to the kings, and had now delivered three kingdoms, "
			+ "and after\n"
			+ "that they went on board a ship and sailed over the sea.  Du"
			+ "ring the\n"
			+ "passage, the two eldest conversed apart and said, \"The you"
			+ "ngest has\n"
			+ "found the water of life and not we, for that our father wil"
			+ "l give him\n"
			+ "the kingdom - the kingdom which belongs to us, and he will"
			+ " rob us of\n"
			+ "all our fortune.\" They then began to seek revenge, and p"
			+ "lotted with\n"
			+ "each other to destroy him.  They waited until they found "
			+ "him fast\n"
			+ "asleep, then they poured the water of life out of the cu"
			+ "p, and took\n"
			+ "it for themselves, but into the cup they poured salt sea"
			+ "-water.\n" + "\n"
			+ "Now therefore, when they arrived home, the youngest took"
			+ " his cup to\n"
			+ "the sick king in order that he might drink out of it, an"
			+ "d be cured.\n"
			+ "But scarcely had he drunk a very little of the salt sea-w"
			+ "ater than he\n"
			+ "became still worse than before.  And as he was lamenting "
			+ "over this,\n"
			+ "the two eldest brothers came, and accused the youngest o"
			+ "f having\n"
			+ "intended to poison him, and said that they had brought h"
			+ "im the true\n"
			+ "water of life, and handed it to him.  He had scarcely ta"
			+ "sted it, when\n"
			+ "he felt his sickness departing, and became strong and he"
			+ "althy as in\n" + "the days of his youth.\n" + "\n"
			+ "After that they both went to the youngest, mocked him, an"
			+ "d said, \"You\n"
			+ "certainly found the water of life, but you have had the p"
			+ "ain, and we\n"
			+ "the gain, you should have been cleverer, and should have "
			+ "kept your\n"
			+ "eyes open.  We took it from you whilst you were asleep at"
			+ " sea, and\n"
			+ "when a year is over, one of us will go and fetch the beau"
			+ "tiful\n"
			+ "princess. But beware that you do not disclose aught of th"
			+ "is to our\n"
			+ "father, indeed he does not trust you, and if you say a si"
			+ "ngle word,\n"
			+ "you shall lose your life into the bargain, but if you keep"
			+ " silent,\n" + "you shall have it as a gift.\"\n" + "\n"
			+ "The old king was angry with his youngest son, and thought "
			+ "he had\n"
			+ "plotted against his life.  So he summoned the court togeth"
			+ "er and had\n"
			+ "sentence pronounced upon his son, that he should be secret"
			+ "ly shot.\n"
			+ "And once when the prince was riding forth to the chase, su"
			+ "specting no\n"
			+ "evil, the king's huntsman was told to go with him, and whe"
			+ "n they were\n"
			+ "quite alone in the forest, the huntsman looked so sorrowfu"
			+ "l that the\n"
			+ "prince said to him, \"Dear huntsman, what ails you?\" The "
			+ "huntsman\n"
			+ "said, \"I cannot tell you, and yet I ought.\" Then the pri"
			+ "nce said,\n"
			+ "\"Say openly what it is, I will pardon you.\" \"Alas,\" sa"
			+ "id the\n"
			+ "huntsman, \"I am to shoot you dead, the king has ordered m"
			+ "e to do it.\"\n"
			+ "Then the prince was shocked, and said, \"Dear huntsman, l"
			+ "et me live,\n"
			+ "there, I give you my royal garments, give me your common"
			+ " ones in\n"
			+ "their stead.\" The huntsman said, \"I will willingly do "
			+ "that, indeed I\n"
			+ "would not have been able to shoot you.\" Then they excha"
			+ "nged clothes,\n"
			+ "and the huntsman returned home, while the prince went fu"
			+ "rther into\n" + "the forest.\n" + "\n"
			+ "After a time three waggons of gold and precious stones ca"
			+ "me to the\n"
			+ "king for his youngest son, which were sent by the three k"
			+ "ings who had\n"
			+ "slain their enemies with the prince's sword, and maintain"
			+ "ed their\n"
			+ "people with his bread, and who wished to show their grati"
			+ "tude for it.\n"
			+ "The old king then thought, \"Can my son have been innocen"
			+ "t?\" And said\n"
			+ "to his people, \"Would that he were still alive, how it gr"
			+ "ieves me\n"
			+ "that I have suffered him to be killed.\" \"He still liv"
			+ "es,\" said the\n"
			+ "huntsman, \"I could not find it in my heart to carry out yo"
			+ "ur\n"
			+ "command.\" And told the king how it had happened.  Then a st"
			+ "one fell\n"
			+ "from the king's heart, and he had it proclaimed in every coun"
			+ "try that\n"
			+ "his son might return and be taken into favor again.\n" + "\n"
			+ "The princess, however, had a road made up to her palace whic"
			+ "h was\n"
			+ "quite bright and golden, and told her people that whosoever c"
			+ "ame\n"
			+ "riding straight along it to her, would be the right one and was"
			+ " to be\n"
			+ "admitted, and whoever rode by the side of it, was not the right "
			+ "one\n" + "and was not to be admitted.\n" + "\n"
			+ "As the time was now close at hand, the eldest thought he would "
			+ "hasten\n"
			+ "to go to the king's daughter, and give himself out as her rescu"
			+ "er,\n"
			+ "and thus win her for his bride, and the kingdom to boot.  Ther"
			+ "efore\n"
			+ "he rode forth, and when he arrived in front of the palace, "
			+ "and saw\n"
			+ "the splendid golden road, he thought, it would be a sin and"
			+ " a shame\n"
			+ "if I were to ride over that.  And turned aside, and rode on"
			+ " the right\n"
			+ "side of it. But when he came to the door, the servants told h"
			+ "im that\n"
			+ "he was not the right one, and was to go away again.\n" + ""
			+ "\n"
			+ "Soon after this the second prince set out, and when he came"
			+ " to the\n"
			+ "golden road, and his horse had put one foot on it, he though"
			+ "t, it\n"
			+ "would be a sin and a shame, a piece might be trodden off.  "
			+ "And he\n"
			+ "turned aside and rode on the left side of it, and when he r"
			+ "eached the\n"
			+ "door, the attendants told him he was not the right one, and"
			+ " he was to\n" + "go away again.\n" + "\n"
			+ "When at last the year had entirely expired, the third son l"
			+ "ikewise\n"
			+ "wished to ride out of the forest to his beloved, and with h"
			+ "er forget\n"
			+ "his sorrows.  So he set out and thought of her so incessant"
			+ "ly, and\n"
			+ "wished to be with her so much, that he never noticed the go"
			+ "lden road\n"
			+ "at all.  So his horse rode onwards up the middle of it, and"
			+ " when he\n"
			+ "came to the door, it was opened and the princess received h"
			+ "im with\n"
			+ "joy, and said he was her saviour, and lord of the kingdom, "
			+ "and their\n"
			+ "wedding was celebrated with great rejoicing.  When it was o"
			+ "ver she\n"
			+ "told him that his father invited him to come to him, and h"
			+ "ad forgiven\n" + "him.\n" + "\n"
			+ "So he rode thither, and told him everything, how his brot"
			+ "hers had\n"
			+ "betrayed him, and how he had nevertheless kept silence.  "
			+ "The old king\n"
			+ "wished to punish them, but they had put to sea, and never "
			+ "came back\n" + "as long as they lived.";

	String htmlContents = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 "
			+ "Strict//EN\" "
			+ "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
			+ "<html>\n" + "<head>\n" + "<title>Test HTML File</title>\n"
			+ "<meta http-equiv=\"Content-Type\" content=\"text/html;charset="
			+ "utf-8\" />\n" + "</head>\n" + "<body>\n" + "\n"
			+ "<p>This is a very simple HTML file.</p>\n" + "\n" + "</body>\n"
			+ "</html>";

	@Before
	public void setUp() {
		shell = new JShell();
		root = shell.getRootDir();
		dir1 = new Directory("dir1", root);
		file1 = new File("073txt", "123", dir1);
		stdOutFile = new File("file", "", null);
		System.setOut(new PrintStream(consoleStreamCaptor));
		dir1.addFile(file1);
	}

	@Test
	public void testGetManual() {
		assertEquals("curl URL\n"
				+ "Retrieve the file at that URL and add it to the current "
				+ "working directory.\n" + "Example1:\n"
				+ "curl http://www.cs.cmu.edu/spok/grimmtmp/073.txt\n"
				+ "Will get the contents of the file, "
				+ "i.e. 073.txt and create a "
				+ "file called 073.txt with the contents in \nthe current "
				+ "working directory.\n" + "Example2:\n"
				+ "curl http://www.ub.edu/gilcub/SIMPLE/simple.html\n"
				+ "Will get the contents of the file, i.e. simple.html "
				+ "(raw HTML) and create a file called simple.html with the\n"
				+ "contents in the current working " + "directory.",
				driver.ClientURL.getManual());
	}

	@Test
	public void testPerformOutcomeWithoutSameTxtFile() {
		String[] parameters = {"curl",
				"http://www.cs.cmu.edu/~spok/grimmtmp/073.txt"};

		driver.ClientURL.performOutcome(shell, parameters, 0, stdOutFile);
		int index = root.containsFile("073txt");
		if (index != -1) {
			File file = (File) root.getDirContents().get(index);
			assertEquals(txtContents, file.getContents());
		}
	}

	@Test
	public void testPerformOutcomeWithoutSameHtmlFile() {
		String[] parameters = {"curl",
				"http://www.brainjar.com/java/host/test.html"};

		driver.ClientURL.performOutcome(shell, parameters, 0, stdOutFile);
		int index = root.containsFile("testhtml");
		if (index != -1) {
			File file = (File) root.getDirContents().get(index);
			assertEquals(htmlContents, file.getContents());
		}
	}

	@Test
	public void testPerformOutcomeWithSameTxtFile() {
		String[] parameters = {"curl",
				"http://www.cs.cmu.edu/~spok/grimmtmp/073.txt"};
		shell.setCurrentDir(dir1);
		driver.ClientURL.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("curl: Filename already exists: 073txt",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testPerformOutcomeInvalidNumArgs() {
		String[] parameters = {"curl",
				"http://www.cs.cmu.edu/~spok/grimmtmp/073.txt", "hello"};
		driver.ClientURL.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("curl: Invalid number of arguments.",
				consoleStreamCaptor.toString().trim());
	}

	@Test
	public void testPerformOutcomeInvalidURL() {
		String[] parameters = {"curl", "http://collin.chan"};
		driver.ClientURL.performOutcome(shell, parameters, 1, stdOutFile);
		assertEquals("curl: Could not read from this URL.",
				consoleStreamCaptor.toString().trim());
	}

}
