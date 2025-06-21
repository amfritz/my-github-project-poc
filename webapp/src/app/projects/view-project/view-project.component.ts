import { Component, computed, inject, input, OnInit, signal } from '@angular/core';
import { ProjectEntity } from '../../models/project';
import { ProjectsService } from '../../services/projects.service';
import { ProjectEvents } from '../../models/project-events';
import { ViewProjectDetailsComponent } from './view-project-details/view-project-details.component';
import { ViewProjectEventComponent } from './view-project-event/view-project-event.component';
import { Router, RouterLink } from '@angular/router';
import { ToastService } from '../../services/toast.service';
import { FormsModule } from '@angular/forms';
import {AddEventComponent, NewEvent} from './add-event/add-event.component';

@Component({
    selector: 'app-view-project',
    imports: [ViewProjectDetailsComponent, ViewProjectEventComponent, RouterLink, FormsModule, AddEventComponent],
    templateUrl: './view-project.component.html',
    styleUrl: './view-project.component.css'
})
export class ViewProjectComponent implements OnInit {
    projectId = input.required<string>();
    private projectService = inject(ProjectsService);
    project: ProjectEntity | null = null;
    savedProject = signal<ProjectEntity | null>(null);
    events = signal<ProjectEvents[]>([]);
    sort = signal<string>('asc');
    sortedEvents = computed(() => {
        let sort = this.sort();
        if (sort === 'asc') {
            return this.events().sort((a, b) => new Date(a.eventDate).getTime() - new Date(b.eventDate).getTime());
        } else {
            return this.events().sort((a, b) => new Date(b.eventDate).getTime() - new Date(a.eventDate).getTime());
        }
    });
    toastService = inject(ToastService);
    isLoading = signal<boolean>(false);

    router = inject(Router);
    editable = computed(() => this.project?.status === 'active');

    // TODO -- refactor this component.
    // edit is false after loading from the url.
    // something for the delete action

    ngOnInit(): void {
        // check to see if it's loaded. on a page reload it will be null
        this.project = this.projectService.findProjectById(this.projectId()) || null;
        if (this.project) {
            this.savedProject.set({ ...this.project });
        } else {
            this.projectService.getProjectById(this.projectId()).subscribe({
                next: (resp) => {
                    console.log('project: ', resp);
                    this.project = { ...resp };
                    this.savedProject.set({ ...resp });
                },
                error: (err) => this.toastService.showToast("An error occurred: " + err.toString(), 'error'),
            });
        }

        // the project id is a path parameter, so it will be set
        this.projectService.getProjectEvents(this.projectId()).subscribe({
            next: (resp) => this.events.set(resp),
            error: (err) => console.log('error', err)
        });
    }

    changeSort() {
        this.sort.set(this.sort() === 'asc' ? 'desc' : 'asc');
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

    // event component handlers below
    // set new event to not new once visited.
    onVisited(eventProject: ProjectEvents) {
        let req = { ...eventProject };
        req.isNewEvent = false;
        this.updateEvent(req, false);
    }

    // add a user created event
    onAddEvent(newEvent: NewEvent) {
        let evt: ProjectEvents = {
            id: '', eventDescription: newEvent.description, projectId: this.projectId(), eventDate: newEvent.date, isNewEvent: true,
            repoName: this.project?.repo.name || '',
        }
        // console.log('new event: ', evt);
        this.projectService.createProjectEvent(evt).subscribe({
            next: (resp) => {
                this.events.update(val => [...val, resp]);
                this.toastService.showToast("Save successful.", 'success');
            },
            error: (err) => this.toastService.showToast("An error occurred saving the new item: " + err.toString(), 'error'),
        })
    }

    updateEventDesc(value: string, event: ProjectEvents) {
        // create a copy of the object and only update it in the list if the update is successful
        let req = { ...event };
        req.eventDescription = value;
        this.updateEvent(req);
    }

    onDeleteEvent(event: ProjectEvents) {
        this.projectService.deleteProjectEvent(event.projectId, event.id).subscribe({
            next: () => this.events.update(val => val.filter(e => e.id !== event.id)),
            error: (err) => console.log('error', err)
        });
    }

    private updateEvent(event: ProjectEvents, showToast: boolean = true) {
        this.projectService.updateProjectEvent(event).subscribe({
            next: (resp) => {
                this.events.update(val => {
                    const newVal = [...val];
                    newVal.splice(newVal.findIndex(e => e.id === resp.id), 1, resp);
                    return newVal;
                });
                if (showToast) {
                    this.toastService.showToast("Save successful.", 'success');
                }
            },
            error: (err) => this.toastService.showToast("An error occurred: " + err.toString(), 'error'),
        });
    }

}
