package keepers.nlp.models;

public enum SeverityLevel {
	GOOD(0), EASY(1), MEDIUM(2), HARD(3), CRITICAL(4);
	
	private int severity;
	
	SeverityLevel(int s) {
		severity = s;
	}
	
	int getSeverity() {
		return severity;
	}
}
