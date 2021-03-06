package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.data.DataAtomicProvider;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataGroupProvider;
import se.uu.ub.cora.data.DataRecordLinkProvider;

public class SearchGroupCreator extends GroupCreator {

	private String recordType;

	public SearchGroupCreator(String id, String dataDivider, String recordType) {
		super(id, dataDivider);
		this.recordType = recordType;
	}

	public static SearchGroupCreator withIdIdAndDataDividerAndRecordType(String id,
			String dataDivider, String recordType) {
		return new SearchGroupCreator(id, dataDivider, recordType);
	}

	@Override
	public DataGroup createGroup(String childReferenceId) {
		super.createGroup(childReferenceId);
		addChildren();
		return topLevelDataGroup;
	}

	private void addChildren() {
		addLinkChildWithNameInDataLinkedTypeAndLinkedIdAndRepeatId("recordTypeToSearchIn",
				"recordType", recordType, "0");
		addLinkChildWithNameInDataLinkedTypeAndLinkedId("metadataId", "metadataGroup",
				"autocompleteSearchGroup");
		addLinkChildWithNameInDataLinkedTypeAndLinkedId("presentationId", "presentationGroup",
				"autocompleteSearchPGroup");

		addTexts();

		topLevelDataGroup.addChild(DataAtomicProvider
				.getDataAtomicUsingNameInDataAndValue("searchGroup", "autocomplete"));
	}

	private void addLinkChildWithNameInDataLinkedTypeAndLinkedId(String nameInData,
			String linkedRecordType, String linkedRecordId) {
		DataGroup recordTypeToSearchIn = createLinkChildWithNameInDataAndLinkedTypeAndLinkedId(
				nameInData, linkedRecordType, linkedRecordId);
		topLevelDataGroup.addChild(recordTypeToSearchIn);
	}

	private void addLinkChildWithNameInDataLinkedTypeAndLinkedIdAndRepeatId(String nameInData,
			String linkedRecordType, String linkedRecordId, String repeatId) {
		DataGroup linkChild = createLinkChildWithNameInDataAndLinkedTypeAndLinkedId(nameInData,
				linkedRecordType, linkedRecordId);
		linkChild.setRepeatId(repeatId);
		topLevelDataGroup.addChild(linkChild);
	}

	private DataGroup createLinkChildWithNameInDataAndLinkedTypeAndLinkedId(String nameInData,
			String linkedRecordType, String linkedRecordId) {
		return DataRecordLinkProvider.getDataRecordLinkAsLinkUsingNameInDataTypeAndId(nameInData,
				linkedRecordType, linkedRecordId);
	}

	private void addTexts() {
		addLinkChildWithNameInDataLinkedTypeAndLinkedId("textId", "coraText", id + "Text");
		addLinkChildWithNameInDataLinkedTypeAndLinkedId("defTextId", "coraText", id + "DefText");
	}

	@Override
	DataGroup createTopLevelDataGroup() {
		return DataGroupProvider.getDataGroupUsingNameInData("search");
	}

	@Override
	void addAttributeType() {
		// not implemented for search
	}

	@Override
	protected void addChildReferencesWithChildId(String refRecordInfoId) {
		// not implemented for search
	}
}
