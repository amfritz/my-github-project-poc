import { inject, Injectable, signal } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {catchError, tap, throwError} from 'rxjs';
import { ProjectEntity } from '../models/project';
import { ProjectEvents } from '../models/project-events';
import {ProjectPostRequest} from '../models/project-post-request';
import {ResolveFn} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ProjectsService {
  private baseUrl = '/api/projects';
  private httpClient = inject(HttpClient);
  // private error = inject(ErrorService);

  private projects = signal<ProjectEntity[]>([]);
  loadedProjects = this.projects.asReadonly();
  private initialized = false;


  constructor() { }

    isInitialized() {
        return this.initialized;
    }

  getProjects() {
      this.initialized = true;
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
            console.log('caught error  in service: ', error);
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
              catchError((error:HttpErrorResponse) => {
                      if (error.status === 404) {
                          return throwError(
                              () => new Error(`Project ID ${projectId} not found`)
                          );
                      }
                      console.log('caught error  in service: ', error);
                      return throwError(
                          () => new Error("An error occurred trying to get the project"));
                  }
              )
          )
  }

    createProject(project: ProjectPostRequest) {
        return this.httpClient.post<ProjectEntity>(`${this.baseUrl}`,project)
            .pipe(
                tap((newProject) => {
                    this.projects.update(() => [...this.projects(), newProject]);
                }),
                catchError((error:HttpErrorResponse) => {
                    console.log(error);
                    return throwError(
                        () => new Error("an error occurred trying to create the project" + error.message )
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

  deleteProject(projectId: string) {
    return this.httpClient.delete(`${this.baseUrl}/${projectId}`)
      .pipe(
        tap(() => {
          this.projects.update(val => val.filter(p => p.id !== projectId));
        }),
        catchError((error: HttpErrorResponse) => {
          console.log(error);
          return throwError(
            () => new Error(error.message || "error" )
          );
        })
      )
  }

  // begin events apis
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

export const projectResolver: ResolveFn<ProjectEntity> = (route, state) => {
  const projectId = route.params['id'];
  return inject(ProjectsService).getProjectById(projectId);
};
