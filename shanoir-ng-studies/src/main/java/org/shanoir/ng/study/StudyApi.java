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

package org.shanoir.ng.study;

import java.util.List;

import org.shanoir.ng.shared.dto.IdNameDTO;
import org.shanoir.ng.shared.exception.ErrorModel;
import org.shanoir.ng.shared.exception.RestServiceException;
import org.shanoir.ng.study.dto.SimpleStudyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-23T10:35:29.288Z")

@Api(value = "studies", description = "the studies API")
@RequestMapping("/studies")
public interface StudyApi {

	@ApiOperation(value = "", notes = "Deletes a study", response = Void.class, tags = {})
	@ApiResponses(value = { @ApiResponse(code = 204, message = "study deleted", response = Void.class),
			@ApiResponse(code = 401, message = "unauthorized", response = Void.class),
			@ApiResponse(code = 403, message = "forbidden", response = Void.class),
			@ApiResponse(code = 404, message = "no study found", response = Void.class),
			@ApiResponse(code = 500, message = "unexpected error", response = Void.class) })
	@RequestMapping(value = "/{studyId}", produces = { "application/json" }, method = RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ADMIN', 'EXPERT')")
	ResponseEntity<Void> deleteStudy(
			@ApiParam(value = "id of the study", required = true) @PathVariable("studyId") Long studyId);

	@ApiOperation(value = "", notes = "If exists, returns the studies that the user is allowed to see", response = Study.class, tags = {})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "found studies", response = StudyDTO.class),
			@ApiResponse(code = 401, message = "unauthorized", response = Study.class),
			@ApiResponse(code = 403, message = "forbidden", response = Study.class),
			@ApiResponse(code = 404, message = "no study found", response = Study.class),
			@ApiResponse(code = 500, message = "unexpected error", response = Study.class) })
	@RequestMapping(value = "", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<List<StudyDTO>> findStudies();

	@ApiOperation(value = "", notes = "Returns id and name for all the studies", response = IdNameDTO.class, responseContainer = "List", tags = {})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "found studies", response = IdNameDTO.class, responseContainer = "List"),
			@ApiResponse(code = 204, message = "no study found", response = Void.class),
			@ApiResponse(code = 401, message = "unauthorized", response = Void.class),
			@ApiResponse(code = 403, message = "forbidden", response = Void.class),
			@ApiResponse(code = 500, message = "unexpected error", response = ErrorModel.class) })
	@RequestMapping(value = "/names", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<List<IdNameDTO>> findStudiesNames();

	@ApiOperation(value = "", notes = "If exists, returns the studies that the user is allowed to see and to import", response = SimpleStudyDTO.class, tags = {})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "found studies by user and equipment", response = SimpleStudyDTO.class),
			@ApiResponse(code = 401, message = "unauthorized", response = SimpleStudyDTO.class),
			@ApiResponse(code = 403, message = "forbidden", response = SimpleStudyDTO.class),
			@ApiResponse(code = 404, message = "no study found", response = SimpleStudyDTO.class),
			@ApiResponse(code = 500, message = "unexpected error", response = SimpleStudyDTO.class) })
	@RequestMapping(value = "/list_for_import", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<List<SimpleStudyDTO>> findStudiesForImport();

	@ApiOperation(value = "", notes = "If exists, returns the study corresponding to the given id", response = Study.class, tags = {})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "found study", response = Study.class),
			@ApiResponse(code = 401, message = "unauthorized", response = Study.class),
			@ApiResponse(code = 403, message = "forbidden", response = Study.class),
			@ApiResponse(code = 404, message = "no study found", response = Study.class),
			@ApiResponse(code = 500, message = "unexpected error", response = Study.class) })
	@RequestMapping(value = "/{studyId}", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<StudyDTO> findStudyById(
			@ApiParam(value = "id of the study", required = true) @PathVariable("studyId") Long studyId);

	@ApiOperation(value = "", notes = "Saves a new study", response = Study.class, tags = {})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "created study", response = Study.class),
			@ApiResponse(code = 401, message = "unauthorized", response = Study.class),
			@ApiResponse(code = 403, message = "forbidden", response = Study.class),
			@ApiResponse(code = 422, message = "bad parameters", response = Study.class),
			@ApiResponse(code = 500, message = "unexpected error", response = Study.class) })
	@RequestMapping(value = "", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN', 'EXPERT')")
	ResponseEntity<StudyDTO> saveNewStudy(
			@ApiParam(value = "study to create", required = true) @RequestBody Study study, BindingResult result)
			throws RestServiceException;

	@ApiOperation(value = "", notes = "Updates a study", response = Void.class, tags = {})
	@ApiResponses(value = { @ApiResponse(code = 204, message = "study updated", response = Void.class),
			@ApiResponse(code = 401, message = "unauthorized", response = Void.class),
			@ApiResponse(code = 403, message = "forbidden", response = Void.class),
			@ApiResponse(code = 422, message = "bad parameters", response = Void.class),
			@ApiResponse(code = 500, message = "unexpected error", response = Void.class) })
	@RequestMapping(value = "/{studyId}", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.PUT)
	@PreAuthorize("hasAnyRole('ADMIN', 'EXPERT')")
	ResponseEntity<Void> updateStudy(
			@ApiParam(value = "id of the study", required = true) @PathVariable("studyId") Long studyId,
			@ApiParam(value = "study to update", required = true) @RequestBody Study study, BindingResult result)
			throws RestServiceException;

}
