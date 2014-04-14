package tools;



public class Exporter {
	
	private static void print(String message) {
		System.out.println(message);
	}
	
	private static void usage() {
		print("USAGE: java tools.Exporter [-sql] <data-file>");
	}

	public static void main(String[] args) {
		usage();
	}

}

