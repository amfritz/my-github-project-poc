import {Component, computed, inject, input, OnInit, signal} from '@angular/core';
import { ProjectEntity } from '../../models/project';
import { ProjectsService } from '../../services/projects.service';
import {ProjectEvents} from '../../models/project-events';
import {ViewProjectDetailsComponent} from './view-project-details/view-project-details.component';
import {ViewProjectEventComponent} from './view-project-event/view-project-event.component';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-view-project',
  imports: [ViewProjectDetailsComponent, ViewProjectEventComponent, RouterLink],
  templateUrl: './view-project.component.html',
  styleUrl: './view-project.component.css'
})
export class ViewProjectComponent implements OnInit {
  projectId = input.required<string>();
  private projectService = inject(ProjectsService);
  project :ProjectEntity| null =null;
  events = signal<ProjectEvents[]>([]);
  canAdd = computed(() => this.events().findIndex(e => e.id === '') === -1);
  sort = signal<string>('asc');
  sortedEvents = computed(() => {
      let sort = this.sort();
      if (sort === 'asc') {
          return this.events().sort((a, b) => new Date(a.eventDate).getTime() - new Date(b.eventDate).getTime());
      } else {
          return this.events().sort((a, b) => new Date(b.eventDate).getTime() - new Date(a.eventDate).getTime());
      }
  });

  ngOnInit(): void {
      // check to see if it's loaded. on a page reload it will be null
      this.project = this.projectService.findProjectById(this.projectId()) || null;
      if (this.project === null) {
          this.projectService.getProjectById(this.projectId()).subscribe( {
              next: (resp) => this.project = resp,
              error: (err) => console.log('error', err)
          });
      }

      // the project id is a path parameter, so it will be set
      this.projectService.getProjectEvents(this.projectId()).subscribe( {
          next: (resp) => this.events.set(resp),
          error: (err) => console.log('error', err)
      });

  }

  changeSort() {
      this.sort.set(this.sort() === 'asc' ? 'desc' : 'asc');
  }

  onAdd() {
      console.log('add new event');
      let evt:ProjectEvents = {
          id: '', eventDescription: '', projectId: this.projectId(), eventDate: new Date().toISOString(),
          userId: 'amfritz',  repoName: this.project?.repo.name || '',
      }
      this.events.update(val => [...val, evt]);
  }

  onDelete(event: ProjectEvents) {
      // alert('delete: ' + eventId);
      if (event.id === '') {
          this.events.update( val => val.filter(e => e.id !== event.id));
          return;
      }
      this.projectService.deleteProjectEvent(event.projectId, event.id).subscribe( {
          next: () => this.events.update( val => val.filter(e => e.id !== event.id)),
          error: (err) => console.log('error', err)
      });
  }

    updateEventDesc(value: string, event: ProjectEvents) {
      if (value === '') {
          // delete it
          // todo -- add confirm delete
      } else if (event.id === '')
      {
          // create new event
          let req = {...event};
          req.eventDescription = value;
          this.projectService.createProjectEvent(req).subscribe( {
              next: (resp) => {
                  this.events.update(val => {
                      const newVal = [...val];
                      newVal.splice(newVal.findIndex(e => e.id === ''), 1, resp);
                      return newVal;
                  });
              },
              error: (err) => console.log('error', err)
          })
      } else {
          // create a copy of the object and only update it in the list if the update is successful
          let req = {...event};
          req.eventDescription = value;
          this.updateEvent(req);
      }

  }

    onVisited(eventProject: ProjectEvents) {
      let req = {...eventProject};
      req.isNewEvent = false;
      this.updateEvent(req);
    }

    private updateEvent(event: ProjectEvents) {
        this.projectService.updateProjectEvent(event).subscribe( {
            next: (resp) => {
                this.events.update(val => {
                    const newVal = [...val];
                    newVal.splice(newVal.findIndex(e => e.id === resp.id), 1, resp);
                    return newVal;
                });

            },
            error: (err) => console.log('error', err)
        });
    }

}
