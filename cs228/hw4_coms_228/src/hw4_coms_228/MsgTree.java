package hw4_coms_228;

import java.util.ArrayList;

/**
 * MsgTree is a class which has multiple methods which allow for a certain
 * traversal (message) to be encoded via binary. a MsgTree object is essentially
 * a binary tree. messages are drawn from trees due to their unique property
 * that any nodes that have actual letters/numbers assigned to them cannot have
 * children (all values are leaves).
 * 
 * @author andrew mcmahon
 *
 */
public class MsgTree {
	/**
	 * char value which represents the value of a node (^ for internal nodes,
	 * anything else for anything else)
	 */
	public char payloadChar;

	/**
	 * MsgTree which represents the left subtree of a MsgTree, used for recursion
	 */
	public MsgTree left;

	/**
	 * MsgTree which represents the right subtree of a MsgTree, used for recursion
	 */
	public MsgTree right;

	/**
	 * static int which tracks the current index in a encodingString used to
	 * construct MsgTree objects necessary because recursion doesn't reset the index
	 */
	private static int staticCharIdx = 0;

	/**
	 * static String that acts as a list of all payload characters, organized in
	 * preorder traversal for easy access
	 */
	private static String preOrderList = "";

	/**
	 * static ArrayList of type String which tracks the binary code followed by the
	 * current leaf's value. split up by $ signs which make it so you can access the
	 * variables and their respective codes without any crossover
	 * 
	 * i know it may be an overcomplicated approach but this way made the most sense
	 * of me
	 */
	private static ArrayList<String> letterList = new ArrayList<String>();

	/**
	 * static String variable which tracks the decoded message to be printed after
	 * printCodes() is run. necessary because it must be printed AFTER printCodes()
	 * print statements, and decode needs to be ran first
	 */
	private static String message = "";

	/**
	 * "Constructor building the tree from a string"
	 * 
	 * @param encodingString is the string used to build the tree
	 */
	public MsgTree(String encodingString) {
		if (encodingString.charAt(staticCharIdx) == '^') {
			payloadChar = '^';
			staticCharIdx++;
			this.left = new MsgTree(encodingString);
			staticCharIdx++;
			this.right = new MsgTree(encodingString);
		} else {
			payloadChar = encodingString.charAt(staticCharIdx);
			left = null;
			right = null;
			// add letter to list of chars in order of preorder traversal
			preOrderList += payloadChar;
		}

	}

	/**
	 * "Constructor for a single node with null children"
	 * 
	 * @param payloadChar is the value of the leaf which has no children
	 */
	public MsgTree(char payloadChar) {
		this.payloadChar = payloadChar;
		this.left = null;
		this.right = null;
	}

	/**
	 * "method to print characters and their binary codes"
	 * 
	 * @param root is the tree used represented with characters
	 * @param code is the binary code which describes the traversal needed for the
	 *             tree
	 */
	public static void printCodes(MsgTree root, String code) {
		// preorder traversal
		decode(root, code);
		System.out.println("\n" + "character  code" + "\n" + "-------------------------");
		for (int i = 0; i < preOrderList.length(); i++) {
			String reverseCode = "";
			String codeReturn = "";
			// char to find in the list of codes (used char to get around substring() out of
			// bounds error
			char temp = preOrderList.charAt(i);

			// ran into an issue where codes with 0 and 1 got confused what was the key
			// and what was the path, so i made a change using spanish keyboard
			String finder = "";
			if (temp == '0') {
				finder = "á";
			} else if (temp == '1') {
				finder = "é";
			} else {
				finder = temp + "";
			}
			for (int j = 0; j < letterList.size(); j++) {
				// find the char from list organized via preorder traversal,
				// then loop backwards to get the binary code
				if (letterList.get(j).equals(finder)) {
					for (int x = j - 1; letterList.get(x) != "$"; x--) {
						reverseCode += letterList.get(x);
					}
					// now that we have the binary code in reverse order, flip it back
					// this is how it must be beacuse the code is tracked line by line
					// and we don't know what the payload is until the end
					StringBuilder sb = new StringBuilder(reverseCode);
					codeReturn = sb.reverse().toString();
					// print results
					if (finder.equals(" ")) {
						// for space
						System.out.println("/s" + "\t" + "    " + codeReturn);
					} else if (finder.equals("\n")) {
						// for new line
						System.out.println("/n" + "\t" + "    " + codeReturn);
					}

					else {
						if (finder.equals("á")) {
							// for '0' character
							finder = "0";
						} else if (finder.equals("é")) {
							// for '1' character
							finder = "1";
						}

						System.out.println(finder + "\t" + "    " + codeReturn);
					}
					// need to break to check for next code
					break;
				}
			}
		}
		// print message & format
		System.out.println("-------------------------" + "\n" + "\n" + message);
		root.statistics(code, message);
	}

	/**
	 * decode is a method which constructs a message based on a given tree and a
	 * sequence of binary numbers which represent how to traverse the tree (0 for
	 * left, 1 for right).
	 * 
	 * @param tree is the tree with leaves
	 * @param msg  is the encoding of the message in binary
	 */
	public static void decode(MsgTree tree, String msg) {
		// msg is 1 & 0's
		// copy tree over
		MsgTree t = tree;
		int index = 0;
		String returner = "";
		// add a $ char which seperates the codes and letters
		letterList.add("$");
		// Start at root
		while (index <= msg.length()) {
			if (t.left == null && t.right == null) {

				if (t.payloadChar == '0') {
					// temp variable that fixes error where 0's and 1's were being read
					// as part of the binary code and not the payloadChar value
					letterList.add("á");
				} else if (t.payloadChar == '1') {
					letterList.add("é");
				} else {
					// add a characters to make sure directions traveled are readable and have a
					// payload assigned
					letterList.add(t.payloadChar + "");
				}
				// add a $ to separate from the next set of binary and its respective
				// payloadChar
				letterList.add("$");
				returner += t.payloadChar;
				t = tree;
				if (index == msg.length()) {
					break;
				}
			} else if (msg.charAt(index) == '0' && t.left != null) {
				// add direction traveled to list for printCodes
				letterList.add("0");
				// go left
				index++;
				// create subtree
				t = t.left;
			} else if (msg.charAt(index) == '1' && t.right != null) {
				// add direction traveled to list for printCodes
				letterList.add("1");
				// go right
				index++;
				// create subtree
				t = t.right;
			}
		}
		// set static message variable to the complete message
		message = returner;
	}

	/**
	 * extra credit method which calculates stats of the message
	 * 
	 * @param binary  is the binary representation of the messgae
	 * @param message is the message which is found by traversing the tree using the
	 *                binary encoding
	 */
	private void statistics(String binary, String message) {
		// The space savings calculation assumes that an uncompressed character is
		// encoded
		// with 16 bits. It is defined as (1 – compressedBits/uncompressedBits)*100.

		System.out.println("STATISTICS:");
		System.out
				.println("Avg bits/char:   " + "\t" + Math.floor((double) binary.length() / (double) message.length()));
		System.out.println("Total Characters:" + "\t" + message.length());
		System.out.println("Space Saving:   " + "\t"
				+ Math.round((1 - (binary.length() / (16.0 * message.length()))) * 1000) / 10.0);
	}
}
