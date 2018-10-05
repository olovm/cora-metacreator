package se.uu.ub.cora.metacreator.recordlink;

import se.uu.ub.cora.metacreator.RecordCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class RecordLinkCompleter extends RecordCompleter implements ExtendedFunctionality {

    public RecordLinkCompleter(String implementingTextType) {
        super(implementingTextType);
    }

    public static RecordLinkCompleter forTextLinkedRecordType(String implementingTextType) {
        return new RecordLinkCompleter(implementingTextType);
    }

    @Override
    public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
        this.spiderDataGroup = spiderDataGroup;
        addValuesToDataGroup();
    }

}
