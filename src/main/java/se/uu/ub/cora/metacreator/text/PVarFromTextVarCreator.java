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

package se.uu.ub.cora.metacreator.text;

import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class PVarFromTextVarCreator implements ExtendedFunctionality {

	private static final String PRESENTATION_VAR = "presentationVar";
	private String userId;
	private String id;
	private String dataDividerString;

	@Override
	public void useExtendedFunctionality(String userId, SpiderDataGroup spiderDataGroup) {
		this.userId = userId;

		extractIdAndDataDividerFromSpiderDataGroup(spiderDataGroup);
		PVarCreator pVarCreator = PVarCreator.withTextVarIdAndDataDivider(id, dataDividerString);

		if (pVarDoesNotExistInStorage(id + "PVar")) {
			SpiderDataGroup createdInputPVar = pVarCreator.createInputPVar();
			SpiderRecordCreator spiderRecordCreator = SpiderInstanceProvider
					.getSpiderRecordCreator();
			spiderRecordCreator.createAndStoreRecord(userId, PRESENTATION_VAR, createdInputPVar);
		}
		if (pVarDoesNotExistInStorage(id + "OutputPVar")) {
			SpiderDataGroup createdOutputPVar = pVarCreator.createOutputPVar();
			SpiderRecordCreator spiderRecordCreatorOutput = SpiderInstanceProvider
					.getSpiderRecordCreator();
			spiderRecordCreatorOutput.createAndStoreRecord(userId, PRESENTATION_VAR,
					createdOutputPVar);
		}
	}

	private void extractIdAndDataDividerFromSpiderDataGroup(SpiderDataGroup spiderDataGroup) {
		SpiderDataGroup recordInfoGroup = spiderDataGroup.extractGroup("recordInfo");
		id = extractIdFromSpiderDataGroup(recordInfoGroup);
		dataDividerString = extractDataDividerFromSpiderDataGroup(recordInfoGroup);
	}

	private String extractIdFromSpiderDataGroup(SpiderDataGroup recordInfoGroup) {
		return recordInfoGroup.extractAtomicValue("id");
	}

	private String extractDataDividerFromSpiderDataGroup(SpiderDataGroup recordInfoGroup) {
		return recordInfoGroup.extractGroup("dataDivider").extractAtomicValue("linkedRecordId");
	}

	private boolean pVarDoesNotExistInStorage(String pVarId) {
		try {
			SpiderRecordReader spiderRecordReader = SpiderInstanceProvider.getSpiderRecordReader();
			spiderRecordReader.readRecord(userId, PRESENTATION_VAR, pVarId);
		} catch (RecordNotFoundException e) {
			return true;
		}
		return false;
	}
}
