package tools;



public class Importer {
	
	private static void print(String message) {
		System.out.println(message);
	}

	private static void usage() {
		print("USAGE: java tools.Importer [-sql] <data-file>");
	}
	
	public static void main(String[] args) {
		usage();
	}
	
}

