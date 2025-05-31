import { Component, inject, OnInit, signal } from '@angular/core';
import { GithubRepoService } from '../../services/github-repo.service';
import { GitHubRepo } from '../../models/git-hub-repo';
import { CommitSelectComponent } from "../commit-list/commit-list.component";
import { GitHubCommit } from '../../models/git-hub-commit';
import { ProjectEntity } from '../../models/project';
import { ProjectsService } from '../../services/projects.service';

@Component({
  selector: 'app-create-project',
  imports: [CommitSelectComponent],
  templateUrl: './create-project.component.html',
  styleUrl: './create-project.component.css'
})
export class CreateProjectComponent implements OnInit {
  private githubService = inject(GithubRepoService);
  private projectService = inject(ProjectsService);
  gitRepos:GitHubRepo[]|undefined = undefined;
  gitCommits:GitHubCommit[]|undefined = undefined;
  selectedRepo:GitHubRepo|undefined = undefined;
 
  ngOnInit(): void
  {
    this.githubService.getUserRepos().subscribe({
        next: (resp) => this.gitRepos = resp,
        error: (err) => console.log('error', err)
      });
  }

  showCommits(repo:GitHubRepo) {
    this.selectedRepo = repo;
    this.githubService.getRepoCommits(repo.name).subscribe({
        next: (resp) => {
          this.gitCommits = resp;
          //console.log(this.commits);
        },
        error: (err) => console.log('error', err)
      });
  }

  createProject () {
    let s:ProjectEntity = {
      user_id: 'amfritz',
      name: this.selectedRepo?.name || 'default-name',
      description: this.selectedRepo?.description || 'default-decription',
      repo: {
        id: this.selectedRepo?.id || 'degault-name',
        name: this.selectedRepo?.name || 'degault-name',
        url: this.selectedRepo?.html_url|| 'degault-name',
        isPrivate: this.selectedRepo?.isPrivate|| true,
      }
    }

    this.projectService.createProject(s, true).subscribe( {
      next: (resp) => console.log(resp),
      error: (err ) => console.log(err),
          })
  }


}
