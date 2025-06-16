import {Component, computed, inject, input, OnInit, signal} from '@angular/core';
import { ProjectEntity } from '../../models/project';
import { ProjectsService } from '../../services/projects.service';
import {ProjectEvents} from '../../models/project-events';
import {ViewProjectDetailsComponent} from './view-project-details/view-project-details.component';
import {ViewProjectEventComponent} from './view-project-event/view-project-event.component';
import {RouterLink} from '@angular/router';
import {ToastService} from '../../services/toast.service';
import {FormsModule} from '@angular/forms';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-view-project',
  imports: [ViewProjectDetailsComponent, ViewProjectEventComponent, RouterLink, FormsModule],
  templateUrl: './view-project.component.html',
  styleUrl: './view-project.component.css'
})
export class ViewProjectComponent implements OnInit {
  projectId = input.required<string>();
  private projectService = inject(ProjectsService);
  project :ProjectEntity| null =null;
  savedProject: ProjectEntity| null = null;
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
  newEventDescription = signal<string>('');
  newEventDate = signal<string>('');
  newEventTime = signal<string>('');
  canAdd = computed(() => this.newEventDescription().length > 0);
  datePipe = new DatePipe('en-US');

  ngOnInit(): void {
      // check to see if it's loaded. on a page reload it will be null
      this.project = this.projectService.findProjectById(this.projectId()) || null;
      if (this.project === null) {
          this.projectService.getProjectById(this.projectId()).subscribe( {
              next: (resp) => {
                  this.project = resp
                  this.savedProject = {...this.project};
              },
              error: (err) => this.toastService.showToast("An error occurred: " + err.toString(), 'error'),
          });
      } else {
          this.savedProject = {...this.project};
      }

      // the project id is a path parameter, so it will be set
      this.projectService.getProjectEvents(this.projectId()).subscribe( {
          next: (resp) => this.events.set(resp),
          error: (err) => console.log('error', err)
      });

      this.initNewDate();
  }

  initNewDate() {
      this.newEventDate.set(this.datePipe.transform(new Date(), 'YYYY-MM-dd' ) || '');
      this.newEventTime.set(this.datePipe.transform(new Date(), 'HH:mm' ) || '');
  }

  changeSort() {
      this.sort.set(this.sort() === 'asc' ? 'desc' : 'asc');
  }

  onAdd() {
      // console.log('add new event');

      // create iso string
      let dateSt = this.datePipe.transform(new Date(this.newEventDate() + 'T' + this.newEventTime()), 'yyyy-MM-ddTHH:mm:ss.SSSZ') || '' ;
      let evt:ProjectEvents = {
          id: '', eventDescription: this.newEventDescription(), projectId: this.projectId(), eventDate: dateSt,
          userId: 'amfritz',  repoName: this.project?.repo.name || '',
      }
      console.log('new event: ', evt);
      this.projectService.createProjectEvent(evt).subscribe( {
          next: (resp) => {
              this.events.update(val => [...val, resp]);
              this.newEventDescription.set('');
              this.initNewDate();
              this.toastService.showToast("Save successful.", 'success');
          },
          error: (err) => this.toastService.showToast("An error occurred saving the new item: " + err.toString(), 'error'),
      })
      //
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

    handleSubmit(newProject: ProjectEntity) {
      if (this.project === null) {
      console.log('project is null on save');
          return;
      }

      console.log('submit: ', newProject);
      if (newProject.status === 'archived' && this.savedProject?.status === 'active') {
          if (!confirm('Status is set to archive, are you sure you want to archive this project?')) {
              return;
          }
      }

      this.projectService.updateProject(newProject).subscribe( {
          next: (resp) => {
              this.savedProject = {...  resp};
              this.toastService.showToast("Save successful.",'success' );
          },
          error: (err) => console.log('error', err)
      });
  }

    onDeleteProject() {
      if (confirm('Are you sure you want to delete this project?')) {
          console.log('delete project');
      }
    }

    updateEventDesc(value: string, event: ProjectEvents) {
      if (value === '') {
          // delete it
          // todo -- add confirm delete
      }  else {
          // create a copy of the object and only update it in the list if the update is successful
          let req = {...event};
          req.eventDescription = value;
          this.updateEvent(req);
      }
  }

    onVisited(eventProject: ProjectEvents) {
      let req = {...eventProject};
      req.isNewEvent = false;
      this.updateEvent(req, false);
    }

    private updateEvent(event: ProjectEvents, showToast: boolean = true) {
        this.projectService.updateProjectEvent(event).subscribe( {
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
