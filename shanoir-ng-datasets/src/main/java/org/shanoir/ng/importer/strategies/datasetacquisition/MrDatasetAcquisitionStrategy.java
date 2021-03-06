/**
 * Shanoir NG - Import, manage and share neuroimaging data
 * Copyright (C) 2009-2019 Inria - https://www.inria.fr/
 * Contact us on https://project.inria.fr/shanoir/
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.shanoir.ng.importer.strategies.datasetacquisition;

import java.io.IOException;
import java.time.Duration;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.shanoir.ng.dataset.Dataset;
import org.shanoir.ng.datasetacquisition.DatasetAcquisition;
import org.shanoir.ng.datasetacquisition.mr.MrDatasetAcquisition;
import org.shanoir.ng.datasetacquisition.mr.MrProtocol;
import org.shanoir.ng.dicom.DicomProcessing;
import org.shanoir.ng.importer.dto.DatasetsWrapper;
import org.shanoir.ng.importer.dto.ImportJob;
import org.shanoir.ng.importer.dto.Serie;
import org.shanoir.ng.importer.strategies.dataset.DatasetStrategy;
import org.shanoir.ng.importer.strategies.protocol.MrProtocolStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * MR Dataset Acquisition Strategy used to create new Mr Dataset Acquisition.
 * Called by the ImportService. Requires an importJob
 * 
 * Refer to Interface for more information
 * 
 * @author atouboul
 *
 */
@Component
public class MrDatasetAcquisitionStrategy implements DatasetAcquisitionStrategy {
	
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(MrDatasetAcquisitionStrategy.class);

	@Autowired
	DicomProcessing dicomProcessing;
	
	@Autowired
	MrProtocolStrategy mrProtocolStrategy;
	
	@Autowired
	DatasetStrategy<Dataset> mrDatasetStrategy;
	
	@Override
	public DatasetAcquisition generateDatasetAcquisitionForSerie(Serie serie, int rank, ImportJob importJob) {
		MrDatasetAcquisition mrDatasetAcquisition = new MrDatasetAcquisition();
		LOG.info("Generating DatasetAcquisition for   : " +serie.getSequenceName() + " - " + serie.getProtocolName() + " - Rank:" + rank);
		Attributes dicomAttributes = null;
		try {
			// TODO ATO : should always be a dicom: add check
			dicomAttributes = dicomProcessing.getDicomObjectAttributes(serie.getFirstDatasetFileForCurrentSerie(),serie.getIsEnhancedMR());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Unable to retrieve dicom attributes in file " + serie.getFirstDatasetFileForCurrentSerie().getPath(),e); 
		}
		mrDatasetAcquisition.setRank(rank);
		mrDatasetAcquisition.setSortingIndex(serie.getSeriesNumber());
		mrDatasetAcquisition.setSoftwareRelease(dicomAttributes.getString(Tag.SoftwareVersions));		
		mrDatasetAcquisition.setAcquisitionEquipmentId(importJob.getFrontAcquisitionEquipmentId());
		
		MrProtocol mrProtocol = mrProtocolStrategy.generateMrProtocolForSerie(dicomAttributes, serie);
		mrDatasetAcquisition.setMrProtocol(mrProtocol);
	
		// TODO ATO add Compatibility check between study card Equipment and dicomEquipment if not done at front level. 
		DatasetsWrapper<Dataset> datasetsWrapper = mrDatasetStrategy.generateDatasetsForSerie(dicomAttributes, serie, importJob);
		for (Dataset dataset : datasetsWrapper.getDatasets()) {
			dataset.setDatasetAcquisition(mrDatasetAcquisition);
		}
		mrDatasetAcquisition.setDatasets(datasetsWrapper.getDatasets());
		
		// total acquisition time
		if(mrDatasetAcquisition.getMrProtocol().getAcquisitionDuration() == null) {
			Double totalAcquisitionTime = null;
			if (datasetsWrapper.getFirstImageAcquisitionTime() != null && datasetsWrapper.getLastImageAcquisitionTime() != null) {
				Duration duration = Duration.between(datasetsWrapper.getLastImageAcquisitionTime(), datasetsWrapper.getFirstImageAcquisitionTime());
				totalAcquisitionTime = new Double(duration.toMillis());
				mrDatasetAcquisition.getMrProtocol().setAcquisitionDuration(totalAcquisitionTime);
			} else {
				mrDatasetAcquisition.getMrProtocol().setAcquisitionDuration(null);
			}
		}
		
		return mrDatasetAcquisition;
	}
	
	

}
