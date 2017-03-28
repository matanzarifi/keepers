package keepers.nlp.models;

public enum AgeGroups {
	_6_TO_8("6-8"), _9_TO_11("9-11"), _12_TO_14("12-14"), _15_PLUS("15+");
	
	private String group;
	
	AgeGroups(String g) {
		group = g;
	}
	
	String getGroup() {
		return group;
	}
}
