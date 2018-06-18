/*
 * Copyright 2018 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.uu.ub.cora.metacreator.group;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.record.DataException;

public class PGroupConstructorTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String authToken;
	List<SpiderDataElement> metadataChildReferences;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		authToken = "testUser";
		metadataChildReferences = DataCreatorForPresentationsConstructor.createChildren();
	}

	@Test
	public void testGroupConstructornForInput() {
		PGroupConstructor constructor = new PGroupConstructor(authToken);
		SpiderDataGroup pGroup = constructor
				.constructPGroupWithIdDataDividerPresentationOfChildrenAndMode("someTestPGroup",
						"testSystem", "someTestGroup", metadataChildReferences, "input");
		assertEquals(pGroup.getAttributes().get("type"), "pGroup");
		assertEquals(pGroup.getNameInData(), "presentation");
		assertCorrectRecordInfo(pGroup);
		assertCorrectPresentationOf(pGroup);

		assertCorrectChildReferences(pGroup);
		assertEquals(instanceFactory.spiderRecordReaders.size(), 12);
	}

	private void assertCorrectChildReferences(SpiderDataGroup pGroup) {
		SpiderDataGroup childReferences = pGroup.extractGroup("childReferences");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 0, "0",
				"identifierTypeCollectionVarText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 1, "1",
				"identifierTypePCollVar", "presentationCollectionVar");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 2, "2",
				"identifierValueTextVarText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 3, "3",
				"identifierValuePVar", "presentationVar");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 4, "4",
				"identifierResLinkText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 5, "5",
				"identifierPResLink", "presentationResourceLink");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 6, "6",
				"identifierLinkText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 7, "7",
				"identifierPLink", "presentationRecordLink");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 8, "8",
				"identifierChildGroupText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 9, "9",
				"identifierChildPGroup", "presentationGroup");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 10, "10",
				"identifierChildHasNoPresentationTextVarText", "coraText");
		assertEquals(childReferences.getChildren().size(), 11);

	}

	private void assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(
			SpiderDataGroup childReferences, int index, String repeatId, String id,
			String recordType) {
		SpiderDataGroup firstChild = (SpiderDataGroup) childReferences.getChildren().get(index);
		assertEquals(firstChild.getNameInData(), "childReference");
		assertEquals(firstChild.getRepeatId(), repeatId);

		assertEquals(firstChild.getAllGroupsWithNameInData("refGroup").size(), 1);
		SpiderDataGroup refGroup = firstChild.extractGroup("refGroup");
		assertEquals(refGroup.getRepeatId(), "0");

		SpiderDataGroup firstChildRef = refGroup.extractGroup("ref");
		assertEquals(firstChildRef.extractAtomicValue("linkedRecordType"), recordType);
		assertEquals(firstChildRef.extractAtomicValue("linkedRecordId"), id);
		String typeAttribute = "presentation";
		if ("coraText".equals(recordType)) {
			typeAttribute = "text";
		}
		assertEquals(firstChildRef.getAttributes().get("type"), typeAttribute);
	}

	private void assertCorrectRecordInfo(SpiderDataGroup pGroup) {
		SpiderDataGroup recordInfo = pGroup.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), "someTestPGroup");

		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "testSystem");
	}

	private void assertCorrectPresentationOf(SpiderDataGroup pGroup) {
		SpiderDataGroup presentationOf = pGroup.extractGroup("presentationOf");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordType"), "metadataGroup");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "someTestGroup");
	}

	@Test
	public void testGroupConstructorForOutput() {
		PGroupConstructor constructor = new PGroupConstructor(authToken);
		SpiderDataGroup pGroup = constructor
				.constructPGroupWithIdDataDividerPresentationOfChildrenAndMode("someTestPGroup",
						"testSystem", "someTestGroup", metadataChildReferences, "output");
		assertEquals(pGroup.getAttributes().get("type"), "pGroup");
		assertCorrectRecordInfo(pGroup);
		assertCorrectPresentationOf(pGroup);

		assertCorrectOutputChildReferences(pGroup);
	}

	private void assertCorrectOutputChildReferences(SpiderDataGroup pGroup) {
		SpiderDataGroup childReferences = pGroup.extractGroup("childReferences");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 0, "0",
				"identifierTypeCollectionVarText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 1, "1",
				"identifierTypeOutputPCollVar", "presentationCollectionVar");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 2, "2",
				"identifierValueTextVarText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 3, "3",
				"identifierValueOutputPVar", "presentationVar");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 4, "4",
				"identifierResLinkText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 5, "5",
				"identifierOutputPResLink", "presentationResourceLink");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 6, "6",
				"identifierLinkText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 7, "7",
				"identifierOutputPLink", "presentationRecordLink");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 8, "8",
				"identifierChildGroupText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 9, "9",
				"identifierChildOutputPGroup", "presentationGroup");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 10, "10",
				"identifierChildHasNoPresentationTextVarText", "coraText");

		assertEquals(childReferences.getChildren().size(), 11);

	}

	@Test(expectedExceptions = DataException.class)
	public void testGroupConstructorWithNoIdentifiedChildren() {
		PGroupConstructor constructor = new PGroupConstructor(authToken);
		List<SpiderDataElement> childReferences = new ArrayList<SpiderDataElement>();

		SpiderDataGroup childRef = DataCreatorForPresentationsConstructor
				.createMetadataChildRefWithIdAndRepeatId("identifierChildGroupWithUnclearEnding",
						"5");
		childReferences.add(childRef);

		constructor.constructPGroupWithIdDataDividerPresentationOfChildrenAndMode("someTestPGroup",
				"testSystem", "someTestGroup", childReferences, "output");
	}
}
