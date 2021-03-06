/*
 * Copyright 2016 Olov McKie
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

package se.uu.ub.cora.metacreator.numbervar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataAtomicFactory;
import se.uu.ub.cora.data.DataAtomicProvider;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataGroupFactory;
import se.uu.ub.cora.data.DataGroupProvider;
import se.uu.ub.cora.data.DataRecordLink;
import se.uu.ub.cora.data.DataRecordLinkFactory;
import se.uu.ub.cora.data.DataRecordLinkProvider;
import se.uu.ub.cora.metacreator.DataRecordLinkFactorySpy;
import se.uu.ub.cora.metacreator.recordtype.DataAtomicFactorySpy;
import se.uu.ub.cora.metacreator.recordtype.DataGroupFactorySpy;

@Test
public class PNumVarConstructorTest {

	private PNumVarConstructor pNumVarConstructor;
	private String id;
	private String dataDividerString;

	private DataGroupFactory dataGroupFactory;
	private DataAtomicFactory dataAtomicFactory;
	private DataRecordLinkFactory dataRecordLinkFactory;

	@BeforeMethod
	public void setUp() {
		dataGroupFactory = new DataGroupFactorySpy();
		DataGroupProvider.setDataGroupFactory(dataGroupFactory);
		dataAtomicFactory = new DataAtomicFactorySpy();
		DataAtomicProvider.setDataAtomicFactory(dataAtomicFactory);
		dataRecordLinkFactory = new DataRecordLinkFactorySpy();
		DataRecordLinkProvider.setDataRecordLinkFactory(dataRecordLinkFactory);

		id = "someNumberVar";
		dataDividerString = "cora";
		pNumVarConstructor = PNumVarConstructor.withTextVarIdAndDataDivider(id, dataDividerString);
	}

	@Test
	public void testCreateInputPNumVarFromMetadataIdAndDataDivider() {

		assertNotNull(pNumVarConstructor);
		DataGroup createdPNumVar = pNumVarConstructor.createInputPNumVar();

		assertEquals(createdPNumVar.getNameInData(), "presentation");

		assertCorrectAttribute(createdPNumVar);

		assertCorrectRecordInfo(createdPNumVar, "somePNumVar");

		assertEquals(createdPNumVar.getChildren().size(), 3);
		assertCorrectPresentationOf(id, createdPNumVar);
		assertEquals(createdPNumVar.getFirstAtomicValueWithNameInData("mode"), "input");

	}

	private void assertCorrectAttribute(DataGroup createdPVar) {
		Map<String, String> attributes = createdPVar.getAttributes();
		assertEquals(attributes.size(), 1);
		assertEquals(attributes.get("type"), "pNumVar");
	}

	private void assertCorrectRecordInfo(DataGroup createdPNumVar, String id) {
		DataGroup recordInfo = createdPNumVar.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), id);

		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordType"), "system");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "cora");
	}

	private void assertCorrectPresentationOf(String id, DataGroup createdPVar) {
		DataRecordLink presentationOf = (DataRecordLink) createdPVar
				.getFirstGroupWithNameInData("presentationOf");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadataNumberVariable");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordId"), id);
	}

	@Test
	public void testCreateOutputPVarFromMetadataIdAndDataDivider() {

		assertNotNull(pNumVarConstructor);
		DataGroup createdPNumVar = pNumVarConstructor.createOutputPNumVar();

		assertEquals(createdPNumVar.getNameInData(), "presentation");

		assertCorrectAttribute(createdPNumVar);

		assertCorrectRecordInfo(createdPNumVar, "someOutputPNumVar");

		assertEquals(createdPNumVar.getChildren().size(), 3);
		assertCorrectPresentationOf(id, createdPNumVar);

		assertEquals(createdPNumVar.getFirstAtomicValueWithNameInData("mode"), "output");

	}
}
