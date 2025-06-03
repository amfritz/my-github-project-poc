import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { ErrorService } from './error.service';
import { catchError, throwError } from 'rxjs';
import { GitHubCommit } from '../models/git-hub-commit';
import { GitHubRepo } from '../models/git-hub-repo';

@Injectable({
  providedIn: 'root'
})
export class GithubRepoService {
  private baseUrl = '/api/repos';
  private httpClient = inject(HttpClient);
  private error = inject(ErrorService);

  constructor() { }

  getUserRepos() {
    return this.httpClient
      .get<GitHubRepo[]>(this.baseUrl)
      .pipe(
        catchError((error) => {
          console.log(error);
          return throwError(
            () => new Error("error")
          );
        })
      )
  }

  getRepoCommits(repoName: string) {
    return this.httpClient
      .get<GitHubCommit[]>(`${this.baseUrl}/${repoName}/commits` )
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
