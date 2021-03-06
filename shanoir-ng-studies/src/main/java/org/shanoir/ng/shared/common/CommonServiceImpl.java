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

package org.shanoir.ng.shared.common;

import org.shanoir.ng.center.Center;
import org.shanoir.ng.center.CenterRepository;
import org.shanoir.ng.shared.dto.IdNameDTO;
import org.shanoir.ng.study.Study;
import org.shanoir.ng.study.StudyRepository;
import org.shanoir.ng.subject.Subject;
import org.shanoir.ng.subject.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of shared service.
 * 
 * @author ifakhfakh
 *
 */
@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	private StudyRepository studyRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private CenterRepository centerRepository;

	@Override
	public CommonIdNamesDTO findByIds(final CommonIdsDTO commonIdsDTO) {
		CommonIdNamesDTO names = new CommonIdNamesDTO();
		if (commonIdsDTO.getStudyId() != null) {
			final Study study = studyRepository.findOne(commonIdsDTO.getStudyId());
			if (study != null) {
				names.setStudy(new IdNameDTO(commonIdsDTO.getStudyId(), study.getName()));
			}
		}
		if (commonIdsDTO.getCenterId() != null) {
			final Center center = centerRepository.findOne(commonIdsDTO.getCenterId());
			if (center != null) {
				names.setCenter(new IdNameDTO(commonIdsDTO.getCenterId(), center.getName()));
			}
		}
		if (commonIdsDTO.getSubjectId() != null) {
			final Subject subject = subjectRepository.findOne(commonIdsDTO.getSubjectId());
			if (subject != null) {
				names.setSubject(new IdNameDTO(commonIdsDTO.getSubjectId(), subject.getName()));
			}
		}

		return names;
	}

}
