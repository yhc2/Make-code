import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;


public class Decode {

	public static final int CHAR_MAX = 256; 
	
	public static void main(String[] args) {
		System.out.println("This program decodes a file with a Huffman code.");
		System.out.println();
		Scanner console = new Scanner(System.in);
		System.out.print("encode file name? ");
		String inFile = console.nextLine();
		System.out.print("code file name? ");
		String codeFile = console.nextLine();
		System.out.print("output file name? ");
		String outputFile = console.nextLine();
		console.close();

		// open code file and record codes
		try {
			Scanner codeInput = new Scanner(new File(codeFile));
			HuffmanTree hTree = new HuffmanTree(codeInput);
			
			// open encode file, open output, decode
			BitInputStream input = new BitInputStream(inFile);
			PrintStream output = new PrintStream(new File(outputFile));
			hTree.decode(input, output, CHAR_MAX);
			output.close();

		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
}
