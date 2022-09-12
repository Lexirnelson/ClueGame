package clueGame;

public class BadConfigFormatException extends Exception {
		public BadConfigFormatException(String message) {
			super(message);
		}
		
		public BadConfigFormatException() {
			super("File Formatting Error");
		}
}
