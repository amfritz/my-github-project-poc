import {Component, inject, input, OnInit, output} from '@angular/core';
import {DatePipe} from '@angular/common';
import {ProjectEntity} from '../../../models/project';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ToastService} from '../../../services/toast.service';

@Component({
    selector: 'app-view-project-details',
    imports: [ ReactiveFormsModule ],
    templateUrl: './view-project-details.component.html',
    styleUrl: './view-project-details.component.css'
})
export class ViewProjectDetailsComponent implements OnInit {
    project = input.required<ProjectEntity>();
    updated = output<ProjectEntity>();
    delete = output();
    canDelete = input<boolean>(true);
    isArchived = false;
    datePipe = new DatePipe('en-US');
    toastService = inject(ToastService);
    form = new FormGroup({
        projectName: new FormControl('', {
            validators: [Validators.required]
        }),
        projectDescription: new FormControl('', {
            validators: [ Validators.maxLength(5000)]
        }),
        projectStatus: new FormControl<'active' | 'archived' | ''>(''),
        createdDt: new FormControl({
            value: '',
            disabled: true
        }),
        repository: new FormGroup({
            repositoryName: new FormControl({value: '', disabled: true}),
            repoHookId: new FormControl({value: '', disabled: true}),
            repoHookUrl: new FormControl({value: '', disabled: true}),
        })

    });

    ngOnInit(): void {
        this.form.patchValue({
            projectName: this.project().name,
            projectDescription: this.project().description,
            projectStatus: this.project().status,
            createdDt: this.datePipe.transform(this.project().createdAt, 'M/d/yyyy') ,
            repository: {
                repositoryName: this.project().repo.name,
                repoHookUrl: this.project().repo.url,
                repoHookId: this.project().repo.hookId,
            }
        });
        if (this.project().status === 'archived') {
            this.form.controls.projectName.disable();
            this.form.controls.projectDescription.disable();
            this.form.controls.projectStatus.disable();
        }
        this.isArchived = this.project().status === 'archived';

    }

    onSubmit() {

        if (this.form.valid) {
            // this.project.update( val => {
            //     return  {
            //             ...val,
            //             name: this.form.value.projectName || this.project().name,
            //             description: this.form.value.projectDescription || this.project().description,
            //             status: this.form.value.projectStatus || this.project().status,
            //         };
            // })
            let req:ProjectEntity = {
                ...this.project(),
                name: this.form.value.projectName || this.project().name,
                description: this.form.value.projectDescription || this.project().description,
                status: this.form.value.projectStatus || this.project().status,
            };
            this.updated.emit(req);
        }
        else {
            this.toastService.showToast("Please correct the errors below.", 'error');
        }
    }
}
