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

package org.shanoir.ng.importer;

import org.shanoir.ng.importer.dicom.query.DicomQuery;
import org.shanoir.ng.importer.model.ImportJob;
import org.shanoir.ng.shared.exception.RestServiceException;
import org.shanoir.ng.shared.exception.ShanoirException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-08-10T08:45:26.334Z")

@Api(value = "importer", description = "the importer API")
@RequestMapping("/importer")
public interface ImporterApi {

    @ApiOperation(value = "Upload files", notes = "Upload files", response = Void.class, tags={ "Upload files", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "success returns file path", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input / Bad Request", response = Void.class),
        @ApiResponse(code = 409, message = "Already exists - conflict", response = Void.class),
        @ApiResponse(code = 200, message = "Unexpected Error", response = Error.class) })
    @RequestMapping(value = "/upload/",
        produces = { "application/json" }, 
        consumes = { "multipart/form-data" },
        method = RequestMethod.POST)
    ResponseEntity<Void> uploadFiles(@ApiParam(value = "file detail") @RequestPart("files") MultipartFile[] files) throws RestServiceException;

    @ApiOperation(value = "Upload one DICOM .zip file from Shanoir uploader with importJob json file", notes = "Upload DICOM .zip file", response = Void.class, tags={ "Upload one DICOM .zip file from shup", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "success returns file path", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input / Bad Request", response = Void.class),
        @ApiResponse(code = 409, message = "Already exists - conflict", response = Void.class),
        @ApiResponse(code = 200, message = "Unexpected Error", response = Error.class) })
    @RequestMapping(value = "/upload_dicom_shup/",
        produces = { "application/json" }, 
        consumes = { "multipart/form-data" },
        method = RequestMethod.POST)
    ResponseEntity<Void> uploadDicomZipFileFromShup(@ApiParam(value = "file detail") @RequestPart("file") MultipartFile dicomZipFile) throws RestServiceException, ShanoirException;
    
    @ApiOperation(value = "Upload one DICOM .zip file", notes = "Upload DICOM .zip file", response = Void.class, tags={ "Upload one DICOM .zip file", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "success returns file path", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input / Bad Request", response = Void.class),
        @ApiResponse(code = 409, message = "Already exists - conflict", response = Void.class),
        @ApiResponse(code = 200, message = "Unexpected Error", response = Error.class) })
    @RequestMapping(value = "/upload_dicom/",
        produces = { "application/json" }, 
        consumes = { "multipart/form-data" },
        method = RequestMethod.POST)
    ResponseEntity<ImportJob> uploadDicomZipFile(@ApiParam(value = "file detail") @RequestPart("file") MultipartFile dicomZipFile) throws RestServiceException;
    
    @ApiOperation(value = "Start import job", notes = "Start import job", response = Void.class, tags={ "Start import job", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "import job started", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input / Bad Request", response = Void.class),
        @ApiResponse(code = 500, message = "unexpected error", response = Error.class) })
    @RequestMapping(value = "/start_import_job/",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Void> startImportJob(@ApiParam(value = "ImportJob", required=true) @RequestBody ImportJob importJob) throws RestServiceException;
    
    @ApiOperation(value = "ImportFromPACS: Query PACS", notes = "ImportFromPACS: Query PACS", response = Void.class, tags={ "ImportFromPACS: Query PACS", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "query the PACS started", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input / Bad Request", response = Void.class),
        @ApiResponse(code = 500, message = "unexpected error", response = Error.class) })
    @RequestMapping(value = "/query_pacs/",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<ImportJob> queryPACS(@ApiParam(value = "DicomQuery", required=true) @RequestBody DicomQuery dicomQuery) throws RestServiceException;

}
