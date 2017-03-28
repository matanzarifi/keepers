package keepers.nlp.models;

public enum DictionaryLanguages {
	HEBREW("he","DICTIONARY_HE"), ENGLISH("en","DICTIONARY_EN"), FRENCH("fr","DICTIONARY_FR"), ITALIAN("it","DICTIONARY_IT");
	
	private String languageCode;
	private String tableName;
	
	DictionaryLanguages(String code, String table) {
		languageCode = code;
		tableName = table;
	}
	
	public String getLanguageCode() {
		return languageCode;
	}
	
	public String getTableName() {
		return tableName;
	}
}
