package se.uu.ub.cora.metacreator.numbervar;

import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class NumberVarCompleter implements ExtendedFunctionality {

	private String implementingTextType;

	public static NumberVarCompleter forImplementingTextType(String implementingTextType) {
		return new NumberVarCompleter(implementingTextType);
	}

	private NumberVarCompleter(String implementingTextType) {
		this.implementingTextType = implementingTextType;
	}

	@Override
	public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
		MetadataCompleter completer = new MetadataCompleter();
		completer.completeSpiderDataGroupWithLinkedTexts(spiderDataGroup, implementingTextType);
	}

	public String getImplementingTextType() {
		return implementingTextType;
	}

}