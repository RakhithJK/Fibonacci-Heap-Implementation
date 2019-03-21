public class DuckDuckGo {

	/**
	 * @param args
	 */
	// This is the main class of this project
	// It accepts input file name as a command-line argument
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		KeywordConstruct k = new KeywordConstruct();
		k.parse(args[0]);
	}

}
