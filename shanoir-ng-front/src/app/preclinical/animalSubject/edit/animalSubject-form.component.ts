import { Component,  Input } from '@angular/core';
import { FormGroup,  Validators, FormControl } from '@angular/forms';
import {  ActivatedRoute } from '@angular/router';

import { PreclinicalSubject } from '../shared/preclinicalSubject.model';
import { AnimalSubject } from '../shared/animalSubject.model';
import { Subject } from '../../../subjects/shared/subject.model';
import { AnimalSubjectService } from '../shared/animalSubject.service';
import { Reference }   from '../../reference/shared/reference.model';
import { ReferenceService } from '../../reference/shared/reference.service';
import { PathologyService } from '../../pathologies/pathology/shared/pathology.service';
import { SubjectPathologyService } from '../../pathologies/subjectPathology/shared/subjectPathology.service';
import { SubjectTherapyService } from '../../therapies/subjectTherapy/shared/subjectTherapy.service';
import { ImagedObjectCategory } from '../../../subjects/shared/imaged-object-category.enum';
import { ModesAware } from "../../shared/mode/mode.decorator";
import { Study } from '../../../studies/shared/study.model';
import { IdNameObject } from '../../../shared/models/id-name-object.model';
import { SubjectStudy } from '../../../subjects/shared/subject-study.model';
import { StudyService } from '../../../studies/shared/study.service';
import { EntityComponent } from '../../../shared/components/entity/entity.component.abstract';
import { preventInitialChildAnimations, slideDown } from '../../../shared/animations/animations';
import * as AppUtils from '../../../utils/app.utils';
import * as shajs from 'sha.js';
import * as PreclinicalUtils from '../../utils/preclinical.utils';

@Component({
    selector: 'animalSubject-form',
    templateUrl: 'animalSubject-form.component.html',
    styleUrls: ['../../../subjects/subject/subject.component.css', 'animalSubject-form.component.css'],
    providers: [AnimalSubjectService, ReferenceService, PathologyService, SubjectPathologyService, SubjectTherapyService],
    animations: [slideDown, preventInitialChildAnimations]
})
    
@ModesAware
export class AnimalSubjectFormComponent extends EntityComponent<PreclinicalSubject> {

    private readonly ImagedObjectCategory = ImagedObjectCategory;
    private readonly HASH_LENGTH: number = 14;
    private studies: IdNameObject[];
    private nameValidators = [Validators.required, Validators.minLength(2), Validators.maxLength(64)];
    species: Reference[] = [];
    strains: Reference[] = [];
    biotypes: Reference[] = [];
    providers: Reference[] = [];
    stabulations: Reference[] = [];
    references: Reference[] = [];

    @Input() preFillData: Subject;
    @Input() displayPathologyTherapy: boolean = true;
    private subjectStudyList: SubjectStudy[] = [];
    private selectedStudy : IdNameObject;
    private selectedStudyId: number; 
    private hasNameUniqueError: boolean = false;
    private existingSubjectError: string;
    private isSubjectFoundPromise: Promise<void>;


    constructor(private route: ActivatedRoute,
            private animalSubjectService: AnimalSubjectService,
            private studyService: StudyService, 
            private referenceService: ReferenceService,
            private subjectPathologyService: SubjectPathologyService,
            private subjectTherapyService: SubjectTherapyService,) {

        super(route, 'preclinical-subject');
    }

    public get preclinicalSubject(): PreclinicalSubject { return this.entity; }
    public set preclinicalSubject(preclinicalSubject: PreclinicalSubject) { this.entity = preclinicalSubject; }

    initView(): Promise<void> {
        return new  Promise<void>(resolve => {
            this.preclinicalSubject = new PreclinicalSubject();
            this.preclinicalSubject.subject = new Subject();
            this.preclinicalSubject.animalSubject = new AnimalSubject();
            this.preclinicalSubject.animalSubject.id = this.id;
            this.animalSubjectService.getAnimalSubject(this.id).then(animalSubject => {
                this.animalSubjectService.getSubject(animalSubject.subjectId).then((subject) => {
                    this.preclinicalSubject.animalSubject = animalSubject;
                    this.preclinicalSubject.subject = subject;
                    // subjectStudy
                    if (this.preclinicalSubject.subject.subjectStudyList && this.preclinicalSubject.subject.subjectStudyList.length > 0){
                        this.subjectStudyList = [];
                        for (let study of this.preclinicalSubject.subject.subjectStudyList) {
                            let newSubjectStudy: SubjectStudy = this.getSubjectStudy(study);
                            this.subjectStudyList.push(newSubjectStudy);
                        }
                        this.preclinicalSubject.subject.subjectStudyList = this.subjectStudyList;
                    }
                });
            });
            resolve();
        });
    }

    initEdit(): Promise<void> {
        this.loadData();
        return new  Promise<void>(resolve => {
            this.preclinicalSubject = new PreclinicalSubject();
            this.preclinicalSubject.subject = new Subject();
            this.preclinicalSubject.animalSubject = new AnimalSubject();
            this.preclinicalSubject.animalSubject.id = this.id;
            this.animalSubjectService.getAnimalSubject(this.id).then(animalSubject => {
                animalSubject.specie = this.getReferenceById(animalSubject.specie);
                animalSubject.strain = this.getReferenceById(animalSubject.strain);
                animalSubject.biotype = this.getReferenceById(animalSubject.biotype);
                animalSubject.provider = this.getReferenceById(animalSubject.provider);
                animalSubject.stabulation = this.getReferenceById(animalSubject.stabulation);
                this.animalSubjectService.getSubject(animalSubject.subjectId).then((subject) => {
                    this.preclinicalSubject.animalSubject = animalSubject;
                    this.preclinicalSubject.subject = subject;
                    // subjectStudy
                    if (this.preclinicalSubject.subject.subjectStudyList && this.preclinicalSubject.subject.subjectStudyList.length > 0){
                        this.subjectStudyList = [];
                        for (let study of this.preclinicalSubject.subject.subjectStudyList) {
                            let newSubjectStudy: SubjectStudy = this.getSubjectStudy(study);
                            this.subjectStudyList.push(newSubjectStudy);
                        }
                        this.preclinicalSubject.subject.subjectStudyList = this.subjectStudyList;
                    }
                });
            });
            resolve();
        });
    }

    initCreate(): Promise<void> {
        this.loadData();
        this.preclinicalSubject = new PreclinicalSubject();
        this.preclinicalSubject.subject = new Subject();
        this.preclinicalSubject.animalSubject = new AnimalSubject();
        this.preclinicalSubject.subject.preclinical = true;
        this.preclinicalSubject.subject.imagedObjectCategory = ImagedObjectCategory.LIVING_ANIMAL;
        return Promise.resolve();
    }

    loadData() {
        this.referenceService.getReferencesByCategory(PreclinicalUtils.PRECLINICAL_CAT_SUBJECT).then(references => {
            this.references = references;
            this.sortReferences();
        });
        this.loadAllStudies();
    }
    
    loadAllStudies(): void {
        this.studyService.getStudiesNames()
            .then(studies => {
                this.studies = studies;
            })
            .catch((error) => {
                // TODO: display error
                console.error("error getting study list!");
        });
    }

    
    getSubjectStudy(subjectStudy: SubjectStudy): SubjectStudy{
    	let fixedSubjectStudy = new SubjectStudy();
    	fixedSubjectStudy.id = subjectStudy.id;
    	fixedSubjectStudy.subjectStudyIdentifier = subjectStudy.subjectStudyIdentifier;
    	fixedSubjectStudy.subjectType = subjectStudy.subjectType;
    	fixedSubjectStudy.physicallyInvolved = subjectStudy.physicallyInvolved;
    	fixedSubjectStudy.subject = this.getSubject();
    	fixedSubjectStudy.study = subjectStudy.study;
    	fixedSubjectStudy.subjectId = this.preclinicalSubject.subject.id;
    	fixedSubjectStudy.studyId = subjectStudy.study.id;
    	return fixedSubjectStudy;
    }
    
    getStudyById(id: number): Study{
    	if (this.studies && this.studies.length > 0){
    		for (let s of this.studies){
    			if (s.id === id){
    				let study: Study = new Study();
    				study.id = s.id;
    				study.name = s.name;
    				return study;
    			}
    		}
    	}
    	return null;
    }

	getSubject(): Subject{
		let subject = new Subject();
		subject.id = this.preclinicalSubject.subject.id;
		subject.name = this.preclinicalSubject.subject.name;
		return subject;
	}
	
     displaySex(): boolean {
        if (this.animalSelected()) {
        	return true;
        } else {
            return false;
        }
    }
    
    public animalSelected(): boolean {
        return this.preclinicalSubject && this.preclinicalSubject.subject && this.preclinicalSubject.subject.imagedObjectCategory != null
            && (this.preclinicalSubject.subject.imagedObjectCategory.toString() != "PHANTOM"
                && this.preclinicalSubject.subject.imagedObjectCategory.toString() != "ANATOMICAL_PIECE");
    }

    buildForm(): FormGroup {
        let sexFC : FormControl;
        if (this.animalSelected()) {
            sexFC = new FormControl(this.preclinicalSubject.subject.sex, [Validators.required]);
        } else {
            sexFC = new FormControl(this.preclinicalSubject.subject.sex);
        }
        let subjectForm = this.formBuilder.group({
            'imagedObjectCategory': [this.preclinicalSubject.subject.imagedObjectCategory, [Validators.required]],
            'isAlreadyAnonymized': [],
            'name': [this.preclinicalSubject.subject.name, this.nameValidators.concat([this.registerOnSubmitValidator('unique', 'name')])],
            'specie': [this.preclinicalSubject.animalSubject.specie, [Validators.required]],
            'strain': [this.preclinicalSubject.animalSubject.strain, [Validators.required]],
            'biotype': [this.preclinicalSubject.animalSubject.biotype, [Validators.required]],
            'provider': [this.preclinicalSubject.animalSubject.provider, [Validators.required]],
            'stabulation': [this.preclinicalSubject.animalSubject.stabulation, [Validators.required]],
            'sex': sexFC,
            'subjectStudyList': []
        });
        return subjectForm;

    }


    
    onChangeImagedObjectCategory(){
    	if (!this.animalSelected()){
        	this.setSex();
        }
        this.buildForm();
    }

    //params should be category and then reftype
    goToRefPage(...params: string[]): void {
        let category;
        let reftype;
        if (params && params[0]) category = params[0];
        if (params && params[1]) reftype = params[1];
        if (category && !reftype) this.router.navigate(['/preclinical-reference/create'], { queryParams: { category: category } });
        if (category && reftype) this.router.navigate(['/preclinical-reference/create'], { queryParams: {category: category, reftype: reftype } });
    }

    goToEdit(id?: number): void {
        super.goToEdit(this.preclinicalSubject.animalSubject.id);
    }

    protected save(): Promise<void> {
        return new  Promise<void>(resolve => {
            if (this.preclinicalSubject.animalSubject.id){
                this.updateSubject().then(() => {
                    this.onSave.next(this.preclinicalSubject);
                    this.chooseRouteAfterSave(this.entity);
                    this.msgBoxService.log('info', 'The preclinical-subject n°' + this.preclinicalSubject.animalSubject.id + ' has been successfully updated');
                });
            }else{
                this.addSubject().then( () => {
                    this.onSave.next(this.preclinicalSubject);
                    this.chooseRouteAfterSave(this.entity);
                    this.msgBoxService.log('info', 'The new preclinical-subject has been successfully saved under the number ' + this.preclinicalSubject.animalSubject.id);
                });
                
            }
            resolve();
        });
    }

    protected chooseRouteAfterSave(entity: PreclinicalSubject) {
        this.breadcrumbsService.currentStep.notifySave(entity);
        if (this.breadcrumbsService.previousStep && this.breadcrumbsService.previousStep.isWaitingFor(this.breadcrumbsService.currentStep)) {
            this.breadcrumbsService.goBack();
        }
        else {
            this.goToView(entity.animalSubject.id);
        }
    }

    addSubject(): Promise<void> {
        if (!this.preclinicalSubject ) { 
            return Promise.resolve();
        }
        return new  Promise<void>(resolve => {
            this.preclinicalSubject.subject.identifier = this.generateSubjectIdentifier();
            if (!this.animalSelected()){
                this.setSex();
            }
            Promise.resolve(this.animalSubjectService.createSubject(this.preclinicalSubject.subject))
            .then((subject) => {
                this.preclinicalSubject.subject = subject;
                this.preclinicalSubject.animalSubject.subjectId = subject.id;
                this.animalSubjectService.createAnimalSubject(this.preclinicalSubject.animalSubject)
                .then((animalSubject) => {
                    this.preclinicalSubject.id = animalSubject.id;
                    this.preclinicalSubject.animalSubject = animalSubject;
                    //Then add pathologies
                    if (this.preclinicalSubject && this.preclinicalSubject.pathologies) {
                        for (let patho of this.preclinicalSubject.pathologies) {
                            //patho.subject = subject;
                            this.subjectPathologyService.create(this.preclinicalSubject, patho);
                        }
                    }
                    //Then add therapies
                    if (this.preclinicalSubject && this.preclinicalSubject.therapies) {
                        for (let therapy of this.preclinicalSubject.therapies) {
                            this.subjectTherapyService.createSubjectTherapy(this.preclinicalSubject, therapy);
                        }
                    }
                    resolve();
                });
            });
        });
    }

    updateSubject(): Promise<void> {
        return new  Promise<void>(resolve => {
            if (this.preclinicalSubject && this.preclinicalSubject.subject){	
                this.generateSubjectIdentifier();
                this.preclinicalSubject.subject.subjectStudyList = this.subjectStudyList;
                this.animalSubjectService.updateSubject(this.preclinicalSubject.subject.id, this.preclinicalSubject.subject)
                    .then(subject => {
                        if (this.preclinicalSubject.animalSubject){
                             this.animalSubjectService.updateAnimalSubject(this.preclinicalSubject.animalSubject);
                        }
                        resolve();
                    }, (error: any) => {
                        this.manageRequestErrors(error);
                    }
                );
            }
        });
    }
    
    setSex(): void {
    	this.preclinicalSubject.subject.sex = 'M';
    }

    sortReferences() {
    if (this.references){
        for (let ref of this.references) {
            switch (ref.reftype) {
                case PreclinicalUtils.PRECLINICAL_SUBJECT_SPECIE:
                    this.species.push(ref);
                    break;
                case PreclinicalUtils.PRECLINICAL_SUBJECT_BIOTYPE:
                    this.biotypes.push(ref);
                    break;
                case PreclinicalUtils.PRECLINICAL_SUBJECT_STRAIN:
                    this.strains.push(ref);
                    break;
                case PreclinicalUtils.PRECLINICAL_SUBJECT_PROVIDER:
                    this.providers.push(ref);
                    break;
                case PreclinicalUtils.PRECLINICAL_SUBJECT_STABULATION:
                    this.stabulations.push(ref);
                    break;
                default:
                    break;
            }
        }
        }
    }

    getReferenceById(reference: any): Reference {
        if (reference) {
            for (let ref of this.references) {
                if (reference.id == ref.id) {
                    return ref;
                }
            }
        }
        return null;
    }
    
    
    
    onStudySelect() {
        this.selectedStudy.selected = true;
        let newSubjectStudy: SubjectStudy = new SubjectStudy();
        newSubjectStudy.physicallyInvolved = false;
        newSubjectStudy.study = new Study();
        newSubjectStudy.study.id = this.selectedStudy.id;
        newSubjectStudy.study.name = this.selectedStudy.name;
        this.subjectStudyList.push(newSubjectStudy);
        this.preclinicalSubject.subject.subjectStudyList = this.subjectStudyList;
    }
    
    
    removeSubjectStudy(subjectStudy: SubjectStudy):void {
        for (let study of this.studies) {
            if (subjectStudy.study.id == study.id) study.selected = false;
        }
        const index: number = this.subjectStudyList.indexOf(subjectStudy);
        if (index !== -1) {
            this.subjectStudyList.splice(index, 1);
        }
    }
    
    
    generateSubjectIdentifier(): string {
        let hash;
        if (this.preclinicalSubject && this.preclinicalSubject.subject) {
            hash = this.preclinicalSubject.subject.name
        }
        return this.getHash(hash);
    }

    getHash(stringToBeHashed: string): string {
        let hash = shajs('sha').update(stringToBeHashed).digest('hex');
        let hex = "";
        hex = hash.substring(0, this.HASH_LENGTH);
        return hex;
    }
    
    
    private manageRequestErrors(error: any): void {
        this.hasNameUniqueError = AppUtils.hasUniqueError(error, 'name');
    }
    

}