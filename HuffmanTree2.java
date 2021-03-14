import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class HuffmanTree2 {

	private static class HuffmanTreeNode implements Comparable<HuffmanTreeNode> {
		public Integer data, freq;
		public HuffmanTreeNode left, right;

		public HuffmanTreeNode() {
		}

		public HuffmanTreeNode(int freq, int data) {
			this.data = new Integer(data);
			this.freq = new Integer(freq);
		}

		public HuffmanTreeNode(HuffmanTreeNode left, HuffmanTreeNode right) {
			this.left = left;
			this.right = right;
			this.freq = new Integer(left.freq + right.freq);
		}

		@Override
		public int compareTo(HuffmanTreeNode o) {
			return (int) Math.signum(freq - o.freq);
		}
	}

	private HuffmanTreeNode overallRoot;

	private static String subTreetoString(HuffmanTreeNode root, String route) {
		if (root == null)
			return "";
		return root.data != null ? root.data.toString() + "\n" + route
				: subTreetoString(root.left, route + "0") + "\n" + subTreetoString(root.right, route + "1");
	}

	private void subTreeAddNode(HuffmanTreeNode root, String route, int ascii_code) {
		if (root == null)
			return;
		if (route.isEmpty())
			root.data = ascii_code;
		else {
			HuffmanTreeNode next;
			if (route.charAt(0) == '0') {
				if (root.left == null)
					root.left = new HuffmanTreeNode();
				next = root.left;
			} else {
				if (root.right == null)
					root.right = new HuffmanTreeNode();
				next = root.right;
			}
			subTreeAddNode(next, route.substring(1), ascii_code);
		}
	}

	private void constructFromBinary(BitInputStream input, HuffmanTreeNode root) {
		if (input.readBit() == 1)
			root.data = read9(input);
		else {
			constructFromBinary(input, root.left = new HuffmanTreeNode());
			constructFromBinary(input, root.right = new HuffmanTreeNode());
		}
	}

	private void serializeToBinary(BitOutputStream output, HuffmanTreeNode root) {
		if (root == null)
			return;
		output.writeBit(root.data != null ? 1 : 0);
		if (root.data != null)
			write9(output, root.data);
		else {
			serializeToBinary(output, root.left);
			serializeToBinary(output, root.right);
		}
	}

	// pre : 0 <= n < 512
	// post: writes a 9-bit representation of n to the given output stream
	private void write9(BitOutputStream output, int n) {
		for (int i = 0; i < 9; i++) {
			output.writeBit(n % 2);
			n /= 2;
		}
	}

	// pre : an integer n has been encoded using write9 or its equivalent
	// post: reads 9 bits to reconstruct the original integer
	private int read9(BitInputStream input) {
		int multiplier = 1;
		int sum = 0;
		for (int i = 0; i < 9; i++) {
			sum += multiplier * input.readBit();
			multiplier *= 2;
		}
		return sum;
	}

	// construct from counts array
	public HuffmanTree2(int[] counts) {
		PriorityQueue<HuffmanTreeNode> q = new PriorityQueue<HuffmanTreeNode>();
		for (int ascii_code = 0; ascii_code < counts.length; ascii_code++)
			if (counts[ascii_code] != 0)
				q.offer(new HuffmanTreeNode(counts[ascii_code], ascii_code));
		// add EOF character
		q.offer(new HuffmanTreeNode(1, counts.length));

		while (q.size() != 1)
			q.offer(new HuffmanTreeNode(q.remove(), q.remove()));
		overallRoot = q.remove();
	}

	// construct from code file
	public HuffmanTree2(Scanner input) {
		overallRoot = new HuffmanTreeNode();
		String ascii_code, route;
		try {
			while ((ascii_code = input.nextLine()) != null && (route = input.nextLine()) != null)
				subTreeAddNode(overallRoot, route, Integer.parseInt(ascii_code));
		} catch (NoSuchElementException e) {
			return;
		}
	}

	public HuffmanTree2(BitInputStream input) {
		overallRoot = new HuffmanTreeNode();
		constructFromBinary(input, overallRoot);
	}

	public void assign(String[] codes) {
		Scanner scan = new Scanner(this.toString());
		while (scan.hasNextLine()) {
			int ascii_code = Integer.parseInt(scan.nextLine());
			codes[ascii_code] = scan.nextLine();
		}
		scan.close();
	}

	public void writeHeader(BitOutputStream output) {
		serializeToBinary(output, overallRoot);
	}

	public void write(PrintStream out) {
		out.print(this);
	}

	public String toString() {
		return subTreetoString(overallRoot, new String());
	}

	public void decode(BitInputStream input, PrintStream output, int eof) {
		HuffmanTreeNode cur = overallRoot;
		int b;
		while ((cur.data == null || cur.data != eof) && (b = input.readBit()) != -1) {
			if (cur.data != null) {
				output.write(cur.data);
				cur = overallRoot;
			}
			cur = (b == 0) ? cur.left : cur.right;
		}
	}
}