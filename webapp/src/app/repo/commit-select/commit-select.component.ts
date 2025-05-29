import { Component, inject, input, OnInit } from '@angular/core';
import { GithubRepoService } from '../../services/github-repo.service';
import { GitHubCommit } from '../../models/git-hub-commit';

@Component({
  selector: 'app-commit-select',
  imports: [],
  templateUrl: './commit-select.component.html',
  styleUrl: './commit-select.component.css'
})
export class CommitSelectComponent implements OnInit {
  repoName = input.required<string>();
  private githubService = inject(GithubRepoService);
  commits : GitHubCommit[]|undefined  = undefined;

ngOnInit(): void {
     this.githubService.getRepoCommits(this.repoName()).subscribe({
        next: (resp) => this.commits = resp,
        error: (err) => console.log('error', err)
      });
}

}
