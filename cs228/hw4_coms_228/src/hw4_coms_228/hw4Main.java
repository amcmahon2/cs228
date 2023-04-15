package hw4_coms_228;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * hw4Main is the test class for MsgTree to run methods such as printCodes.
 * 
 * @author andrew mcmahon
 *
 */
public class hw4Main {
	public static void main(String[] args) throws IOException {
		System.out.println("File name to decode:");
		Scanner scan = new Scanner(System.in);
		String file = scan.nextLine();
		scan.close();
		String content = new String(Files.readAllBytes(Paths.get(file))).trim();

		// find where the binary starts
		// ternary operator hehe :)
		int split = (content.indexOf('0') < content.indexOf('1')) ? (content.indexOf('0')) : (content.indexOf('1'));

		// check if tree structure has 0 or 1 in it (constitution.arch)
		// this works bc if the file has a 0 or 1 before the last new line
		// it means its part of the file, not the start of the binary code
		if (split < content.lastIndexOf("\n")) {
			// more than 1 space
			split = content.lastIndexOf("\n") + 1;
		}
		// tree structure given with carrots and stuff
		String structure = content.substring(0, split);
		// binary code
		String binary = content.substring(split).trim();
		MsgTree mt = new MsgTree(structure);
		MsgTree.printCodes(mt, binary);
	}
}
