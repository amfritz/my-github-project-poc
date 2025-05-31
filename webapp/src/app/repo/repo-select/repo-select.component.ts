import { Component, inject, OnInit } from '@angular/core';
import { GithubRepoService } from '../../services/github-repo.service';
import { GitHubRepo } from '../../models/git-hub-repo';
import { CommitSelectComponent } from "../commit-select/commit-select.component";
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-repo-select',
  imports: [ RouterLink],
  templateUrl: './repo-select.component.html',
  styleUrl: './repo-select.component.css'
})
export class RepoSelectComponent implements OnInit {
  private githubService = inject(GithubRepoService);
  gitRepos:GitHubRepo[]|undefined = undefined;
 
  ngOnInit(): void
  {
    this.githubService.getUserRepos().subscribe({
        next: (resp) => this.gitRepos = resp,
        error: (err) => console.log('error', err)
      });
  }

  makeJson(item: GitHubRepo)
  {
    return JSON.stringify(item);
  }

}
