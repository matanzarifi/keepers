package keepers.nlp.models;

public class DictionaryEntry {
	private String phrase = "";
	private int sixToEight = 0;
	private int nineToEleven = 0;
	private int twelveToFourteen = 0;
	private int fifteenPlus = 0;
	
	public DictionaryEntry () {
		
	}
	
	public DictionaryEntry (String phrase, int sixEight, int nineEleven, int twelveFourteen, int fifteenP) {
		this.phrase = phrase;
		this.sixToEight = sixEight;
		this.nineToEleven = nineEleven;
		this.twelveToFourteen = twelveFourteen;
		this.fifteenPlus = fifteenP;
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public int getSixToEight() {
		return sixToEight;
	}

	public void setSixToEight(int sixToEight) {
		this.sixToEight = sixToEight;
	}

	public int getNineToEleven() {
		return nineToEleven;
	}

	public void setNineToEleven(int nineToEleven) {
		this.nineToEleven = nineToEleven;
	}

	public int getTwelveToFourteen() {
		return twelveToFourteen;
	}

	public void setTwelveToFourteen(int twelveToFourteen) {
		this.twelveToFourteen = twelveToFourteen;
	}

	public int getFifteenPlus() {
		return fifteenPlus;
	}

	public void setFifteenPlus(int fifteenPlus) {
		this.fifteenPlus = fifteenPlus;
	}

}
