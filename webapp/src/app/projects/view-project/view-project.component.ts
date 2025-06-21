import { Component, computed, inject, input, OnInit, signal } from '@angular/core';
import { ProjectEntity } from '../../models/project';
import { ProjectsService } from '../../services/projects.service';
import { ViewProjectDetailsComponent } from './view-project-details/view-project-details.component';
import { Router, RouterLink } from '@angular/router';
import { ToastService } from '../../services/toast.service';
import { FormsModule } from '@angular/forms';
import {EventListComponent} from './event-list/event-list.component';

@Component({
    selector: 'app-view-project',
    imports: [ViewProjectDetailsComponent, RouterLink, FormsModule, EventListComponent],
    templateUrl: './view-project.component.html',
    styleUrl: './view-project.component.css'
})
export class ViewProjectComponent implements OnInit {
    projectId = input.required<string>();
    private projectService = inject(ProjectsService);
    project: ProjectEntity | null = null;
    savedProject = signal<ProjectEntity | null>(null);
    toastService = inject(ToastService);
    isLoading = signal<boolean>(false);

    router = inject(Router);
    status = signal<string>('');
    editable = computed(() => this.status() === 'active');

    // TODO -- refactor this component.
    // something for the delete action

    ngOnInit(): void {
        // check to see if it's loaded. on a page reload it will be null
        this.project = this.projectService.findProjectById(this.projectId()) || null;
        if (this.project) {
            this.savedProject.set({ ...this.project });
            this.status.set(this.project.status);
        } else {
            this.projectService.getProjectById(this.projectId()).subscribe({
                next: (resp) => {
                    console.log('project: ', resp);
                    this.project = { ...resp };
                    this.savedProject.set({ ...resp });
                    this.status.set(resp.status);
                },
                error: (err) => this.toastService.showToast("An error occurred: " + err.toString(), 'error'),
            });
        }
    }

    handleSubmit(newProject: ProjectEntity) {
        if (this.project === null) {
            console.log('project is null on save');
            return;
        }

        if (newProject.status === 'archived' && this.savedProject()?.status === 'active') {
            if (!confirm('Status is set to archive, are you sure you want to archive this project?')) {
                return;
            }
        }

        this.projectService.updateProject(newProject).subscribe({
            next: (resp) => {
                this.savedProject.set({ ...resp });
                this.project = { ...resp };
                this.status.set(resp.status);
                this.toastService.showToast("Save successful.", 'success');
            },
            error: (err) => console.log('error', err)
        });
    }

    onDeleteProject() {
        if (confirm('Are you sure you want to delete this project?')) {
            console.log('delete project');
            this.isLoading.set(true);
            this.projectService.deleteProject(this.projectId()).subscribe({
                next: () => {
                    this.toastService.showToast("Delete successful.", 'success');
                    this.isLoading.set(false);
                    this.router.navigate(['/']).then();
                },
                error: (err) => {
                    console.log('error', err);
                    this.toastService.showToast("An error occurred: " + err.toString(), 'error');
                }
            });
        }
    }

}
