import { Component, inject, input, OnInit, resource } from '@angular/core';
import { GithubRepoService } from '../../services/github-repo.service';
import { GitHubCommit } from '../../models/git-hub-commit';
import { ProjectsService } from '../../services/projects.service';
import { ActivatedRoute } from '@angular/router';
import { GitHubRepo } from '../../models/git-hub-repo';

@Component({
  selector: 'app-commit-list',
  imports: [],
  templateUrl: './commit-list.component.html',
  styleUrl: './commit-list.component.css'
})
export class CommitSelectComponent  {
  commits = input.required<GitHubCommit[]>();

  //stuff = resource( this.repoName() , )

  // ngOnInit(): void {
  //     this.githubService.getRepoCommits(this.repoName()).subscribe({
  //         next: (resp) => {
  //           this.commits = resp;
  //           //console.log(this.commits);
  //         },
  //         error: (err) => console.log('error', err)
  //       });
  // }

}
