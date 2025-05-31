import { inject, Injectable } from '@angular/core';
import { ErrorService } from './error.service';
import { HttpClient } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { ProjectEntity } from '../models/project';
import { ProjectEvents } from '../models/project-events';

@Injectable({
  providedIn: 'root'
})
export class ProjectsService {
  private baseUrl = '/api/v1/projects';
  private httpClient = inject(HttpClient);
  private error = inject(ErrorService);

  constructor() { }

  getProjects() {
      return this.httpClient
        .get<ProjectEntity[]>(this.baseUrl)
        .pipe(
          catchError((error) => {
            console.log(error);
            return throwError(
              () => new Error("error")
            );
          })
        )
    }

  createProject(project: ProjectEntity, addCommits: boolean) {
    return this.httpClient.post<ProjectEntity>(`${this.baseUrl}?add-commits=${addCommits}`,project);
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
