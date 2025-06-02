import { inject, Injectable, signal } from '@angular/core';
import { ErrorService } from './error.service';
import { HttpClient } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import { ProjectEntity } from '../models/project';
import { ProjectEvents } from '../models/project-events';

@Injectable({
  providedIn: 'root'
})
export class ProjectsService {
  private baseUrl = '/api/v1/projects';
  private httpClient = inject(HttpClient);
  private error = inject(ErrorService);

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

  getProjectById(projectId: string) {
    return this.loadedProjects().find(p => p.id === projectId) || undefined;
  }

  createProject(project: ProjectEntity, addCommits: boolean) {
    return this.httpClient.post<ProjectEntity>(`${this.baseUrl}?add-commits=${addCommits}`,project)
      .pipe(
        tap((newProject) => {
          this.projects().push(newProject);
        }),
        catchError((error) => {
          console.log(error);
          return throwError(
            () => new Error("error")
          );
        })
      )          
  }

    getProjectEvents(projectId: string) {
      return this.httpClient
        .get<ProjectEvents[]>(this.baseUrl)
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
