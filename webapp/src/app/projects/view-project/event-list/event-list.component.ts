import {Component, computed, inject, input, OnInit, signal} from '@angular/core';
import {AddEventComponent, NewEvent} from "../add-event/add-event.component";
import {ViewProjectEventComponent} from "../view-project-event/view-project-event.component";
import {ProjectEvents} from '../../../models/project-events';
import {ProjectsService} from '../../../services/projects.service';
import {ToastService} from '../../../services/toast.service';

@Component({
  selector: 'app-event-list',
    imports: [
        AddEventComponent,
        ViewProjectEventComponent
    ],
  templateUrl: './event-list.component.html',
  styleUrl: './event-list.component.css'
})
export class EventListComponent implements OnInit {
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
    private projectService = inject(ProjectsService);
    projectId = input.required<string>();
    editable = input<boolean>(true);
    toastService = inject(ToastService);

    ngOnInit() {
        // the project id is a path parameter, so it will be set
        this.projectService.getProjectEvents(this.projectId()).subscribe({
            next: (resp) => this.events.set(resp),
            error: (err) => console.log('error', err)
        });
    }

    changeSort() {
        this.sort.set(this.sort() === 'asc' ? 'desc' : 'asc');
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
        }
        // TODO -- events probably don't need the repo name because that's at the project level
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
