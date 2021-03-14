import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;




public class HuffmanTree {
	private HuffmanNode root;
	
	/** Representation of a binary tree node */
	private class HuffmanNode implements Comparable<HuffmanNode>{
		// frequency of the character
		private int frequency;
		// ASCII value of the character
		private int character;
		// the left child of this tree node
		private HuffmanNode left;
		// the right child of this tree node
		private HuffmanNode right;
		
		/**
		 * Construct a new Huffman tree node
		 * with the ASCII value of the character, its frequency,
		 * its left and right child 
		 * @param character
		 * @param frequency
		 * @param left
		 * @param right
		 */
		public HuffmanNode (int character, int frequency, HuffmanNode left, HuffmanNode right) {
			this.character = character;
			this.frequency = frequency;
			this.left = left;
			this.right = right;
		}
		
		public HuffmanNode(){
			this.character = 0;
			this.frequency = 0;
		}
		
		/**
		 * Override compareTo() 
		 * Compare the frequency of the character
		 * Return 1 when the frequency is bigger than the comparing node
		 */
		@Override
		public int compareTo(HuffmanNode n) {
			// TODO Auto-generated method stub
			int compare = this.frequency - n.frequency;
			if (compare > 0 ) {
				return 1;
			}
			else if (compare == 0) {
				return 0;
			} 
			else {
				return -1;
			}
		}
		
		/**
		 * Return true when it reaches the end of the tree
		 * @return
		 */
		public boolean endOfTree() {
			return (this.left == null && this.right == null);
		}

	}
	
	/**
	 * Construct a Huffman tree using the given array of frequencies 
	 * where count[i] is the number of occurrences of the character with ASCII value i.
	 * @param count
	 */
	public HuffmanTree(int[] count) {
		
		// check if count is empty
		if (count.length == 0) {
			throw new IllegalArgumentException("There are no characters to create a new tree");
		}
		// priority queue to store the nodes
		PriorityQueue<HuffmanNode> queue = new PriorityQueue<HuffmanNode>();
		// the end of file character which has frequency 1 and ASCII value 256
		HuffmanNode eof = new HuffmanNode(256, 1, null, null);
		
		for (int i = 0; i < count.length; i++) {
			int tempFrequency = count[i];
			// store the node into the priority queue if it has at least one frequency
			if (tempFrequency > 0) {
				queue.offer(new HuffmanNode(i, tempFrequency, null, null));
				
			}
		}
		queue.offer(eof);
		
		while(queue.size() > 1) {
			HuffmanNode left = queue.remove();
			HuffmanNode right = queue.remove();
			int sum = left.frequency + right.frequency;
			// set ASCII value of branch character to 32
			HuffmanNode branch = new HuffmanNode(32, sum, left, right);
			queue.offer(branch);
		}
		// root is last node of the queue
		root = queue.remove();
		
	}

	/**
	 * Write the current tree to the given output stream in standard format
	 * 
	 * @param output
	 * 				the PrintStream value to write the information to file
	 */
	public void write(PrintStream output) {
		// find the binary encoding values of the tree and 
		// write to file and System 
		String value = "";
		binaryEncoding(root, value, output);

	}

	/**
	 * The recursive method for writing the binary encoding file
	 * 
	 * @param node
	 * 				a branch node starting with overallRoot
	 * @param s
	 * 				string value to concatenate the binary encoding value
	 * @param out
	 * 				the PrintStream value to write the information to file
	 */
	private void binaryEncoding(HuffmanNode node, String s, PrintStream out) { 

		// if both left and right nodes are null
		// this is a leaf - print the character and the value
		if (node.left == null && node.right == null) { 
//			System.out.println(node.character);
//			System.out.println(s);
			out.println(node.character);
			out.println(s);
			return;
		} 

		// keep calling down the left and then down the right
		binaryEncoding(node.left, s + "0", out); 
		binaryEncoding(node.right, s + "1", out); 
	} 
	
	/** 
	 * Construct a HuffmanTree with the input file
	 * 
	 * @param inputFile
	 */
	public HuffmanTree(Scanner input) {

		while (input.hasNext()) {
			
			int n = Integer.parseInt(input.nextLine());
			String code = input.nextLine();

			// create new leaf and place it
			HuffmanNode node = new HuffmanNode();
			node.character = n;
			node.frequency = node.character;
			
			if (root != null) {
				putLeaf(node, code);

			} else {
				putRoot(node,code);
			}
		}
	}
	
	

	private void putLeaf(HuffmanNode node, String code) {
		helper(root, node, code);
	}
	
	/**
	 * Recursive method for putLeaf
	 * @param parent
	 * @param node
	 * @param code
	 */
	private void helper(HuffmanNode parent, HuffmanNode node, String code) {
		Character current = code.charAt(0);
		// base case
		// if it's the last digit
		// place the node directly
		if (code.length() == 1) {
			if (current == '0') {
				parent.left = node;
			} else {
				parent.right = node;
			}
		// recursion steps
		} else {
			if (current == '0') {
				if (parent.left == null) {
					parent.left = new HuffmanNode();
				}
				helper(parent.left, node, code.substring(1));
			} else {
				if (parent.right == null) {
					parent.right = new HuffmanNode();
				}
				helper(parent.right, node, code.substring(1));
			}
		}
	}
	
	private void putRoot(HuffmanNode node, String code) {
		// Traverse the tree path in reverse order
		Character current = code.charAt(code.length()-1);
		HuffmanNode temp = new HuffmanNode();
		
		// base case
		if (code.length() == 1) {
			if (current == '0') {
				temp.left = node;
			} else {
				temp.right = node;
			}
			root = temp;
		// recursion steps
		} else {
			if (current == '0') {
				temp.left = node;
				putRoot(temp, code.substring(0, code.length()-1));
			} else {
				temp.right = node;
				putRoot(temp, code.substring(0, code.length()-1));

			}
		}
		
		
	}
	
	/**
	 * Read from the inputStream and write the characters 
	 * onto the outputStream until it reaches the 
	 * end of file character 
	 *  
	 * @param input 
	 * input stream to read
	 * @param output
	 * output stream to write
	 * @param eof 
	 * end of file character
	 */
	public void decode(BitInputStream input, PrintStream output, int eof) {
		HuffmanNode current = root;

		// stop reading when it reaches the end of file character
		while (current.character != eof) {
			if (current.endOfTree()) {
				// write the code to the outputStream when it reaches the end of the tree
				output.write(current.character);
				// repeat again
				current = root;
			} else if (input.readBit() == 0) {
				// if read "0", go toward the left subtree
				current = current.left;
			} else {
				// if read "1", go toward the right subtree
				current = current.right;
			}
		}
	}
	
	
	
}

	





