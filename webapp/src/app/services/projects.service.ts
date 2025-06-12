import { inject, Injectable, signal } from '@angular/core';
import { ErrorService } from './error.service';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {catchError, Observable, tap, throwError} from 'rxjs';
import { ProjectEntity } from '../models/project';
import { ProjectEvents } from '../models/project-events';

@Injectable({
  providedIn: 'root'
})
export class ProjectsService {
  private baseUrl = '/api/projects';
  private httpClient = inject(HttpClient);
  // private error = inject(ErrorService);

  private projects = signal<ProjectEntity[]>([]);
  loadedProjects = this.projects.asReadonly();

  constructor() { }

  getProjects() {
      return this.httpClient
        .get<ProjectEntity[]>(this.baseUrl)
        .pipe(
          tap((projects) => {
            // a user would generally only have one set of project, so we can save them
            // console.log('projects loaded: ', projects);
            // this.projects = projects;
            this.projects.set(projects);
          }
          ),
          catchError((error) => {
            console.log(error);
            return throwError(
              () => new Error("error")
            );
          })
        )
    }

    findProjectById(projectId: string) {
      return this.loadedProjects().find(p => p.id === projectId);
    }
  // so fix this service so it can handle that
  getProjectById(projectId: string) {
      return this.httpClient.get<ProjectEntity>(`${this.baseUrl}/${projectId}`)
          .pipe(
              catchError((error) => {
              console.log(error);
              return throwError(
                () => new Error("get project by id error")
              );
              })
          )
  }

  createProject(project: ProjectEntity, addCommits: boolean) {
    return this.httpClient.post<ProjectEntity>(`${this.baseUrl}?add-commits=${addCommits}`,project)
      .pipe(
        tap((newProject) => {
          this.projects.update(() => [...this.projects(), newProject]);
        }),
        catchError((error) => {
          console.log(error);
          return throwError(
            () => new Error("error")
          );
        })
      )
  }

  updateProject(project: ProjectEntity) {
      return this.httpClient.put<ProjectEntity>(`${this.baseUrl}/${project.id}`, project)
          .pipe(
              tap((resp) => {
                      this.projects.update(val => {
                          const newVal = [...val];
                          newVal.splice(newVal.findIndex(e => e.id === project.id), 1, resp);
                          return newVal;
                      })
                  }),
              catchError((error) => {
              console.log(error);
              return throwError(
                () => new Error("error")
              );
              })
          );
  }

    getProjectEvents(projectId: string) {
      return this.httpClient
        .get<ProjectEvents[]>(`${this.baseUrl}/${projectId}/events`)
        .pipe(
          catchError((error) => {
            console.log(error);
            return throwError(
              () => new Error("error")
            );
          })
        )
    }
    createProjectEvent(event: ProjectEvents) {
      return this.httpClient.post<ProjectEvents>(`${this.baseUrl}/${event.projectId}/events`, event)
        .pipe(
          catchError((error) => {
            console.log(error);
            return throwError(
              () => new Error("error")
            );
          })
        )
    }

    updateProjectEvent( event: ProjectEvents) {
      return this.httpClient.put<ProjectEvents>(`${this.baseUrl}/${event.projectId}/events/${event.id}`, event)
        .pipe(
          catchError((error) => {
            console.log(error);
            return throwError(
              () => new Error("error")
            );
          })
        )
    }

    deleteProjectEvent(projectId: string, eventId: string) {
      return this.httpClient.delete(`${this.baseUrl}/${projectId}/events/${eventId}`)
        .pipe(
          catchError((error) => {
            console.log(error);
            return throwError(
              () => new Error("error")
            );
          })
        )
    }

}
