import { Component, inject, input, OnInit } from '@angular/core';
import { GithubRepoService } from '../../services/github-repo.service';
import { GitHubCommit } from '../../models/git-hub-commit';
import { ProjectsService } from '../../services/projects.service';
import { ActivatedRoute } from '@angular/router';
import { GitHubRepo } from '../../models/git-hub-repo';

@Component({
  selector: 'app-commit-select',
  imports: [],
  templateUrl: './commit-select.component.html',
  styleUrl: './commit-select.component.css'
})
export class CommitSelectComponent implements OnInit {
  repoName = input.required<string>();
  route = inject(ActivatedRoute);
  private githubService = inject(GithubRepoService);
  private projectService = inject(ProjectsService);
  commits : GitHubCommit[]|undefined  = undefined;
  repo: GitHubRepo|undefined;

  ngOnInit(): void {
      this.route.queryParams.subscribe( params => {
        this.repo = JSON.parse(params['repo']);
        console.log('repo: ', this.repo);
      });
      this.githubService.getRepoCommits(this.repoName()).subscribe({
          next: (resp) => this.commits = resp,
          error: (err) => console.log('error', err)
        });
  }

  watchRepo() {
   // this.projectService.addWatchedRepo()
  }

}
