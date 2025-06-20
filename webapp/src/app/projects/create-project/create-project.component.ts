import { Component, inject, OnInit } from '@angular/core';
import { GithubRepoService } from '../../services/github-repo.service';
import { GitHubRepo } from '../../models/git-hub-repo';
import { CommitSelectComponent } from "../commit-list/commit-list.component";
import { GitHubCommit } from '../../models/git-hub-commit';
import { ProjectsService } from '../../services/projects.service';
import { Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {Router} from '@angular/router';
import {ProjectPostRequest} from '../../models/project-post-request';
import {ToastService} from '../../services/toast.service';

@Component({
  selector: 'app-create-project',
  imports: [CommitSelectComponent, FormsModule],
  templateUrl: './create-project.component.html',
  styleUrl: './create-project.component.css'
})
export class CreateProjectComponent implements OnInit {
  private githubService = inject(GithubRepoService);
  private projectService = inject(ProjectsService);
  // projects = input.required<ProjectEntity[]>();
  gitRepos:GitHubRepo[] = [];
  gitCommits:GitHubCommit[] = [];
  selectedRepo:GitHubRepo|undefined = undefined;
  location = inject(Location);
  newProject: ProjectPostRequest = {userId: 'amfritz', name: '', description: '', repo: { name: '', id: '', url: '', isPrivate: false, createdAt:''}, events: []};
  isLoading = false;
  router = inject(Router);
  useCommitList = true;
  toastService = inject(ToastService);


  ngOnInit(): void
  {
    // todo -- need a pretty isloading
    this.isLoading = true;
    this.githubService.getUserRepos().subscribe({
        next: (resp) => {
          this.loadRepos(resp);
          this.isLoading = false;
          if (this.gitRepos.length > 0) {
              this.selectRepo(this.gitRepos[0]);
          }
        },
        error: (err) => {
          console.log('error', err);
          this.isLoading = false;
        }
      });
  }

  loadRepos(repos:GitHubRepo[]) {
    let projects = this.projectService.loadedProjects();
    this.gitRepos = repos.filter( r => projects.filter(p => p.repo.name === r.name).length === 0);
  }

  selectRepo(repo:GitHubRepo) {
    if (this.selectedRepo?.name === repo.name) {
      // currently selected, so unselect it
      this.selectedRepo = undefined;
      this.gitCommits = [];
      this.newProject = {userId: 'amfritz', name: '', description: '', repo: { name: '', id: '', url: '', isPrivate: false, createdAt:''}, events: []};

        return;
    }

    // set the selected class on the element
    this.selectedRepo = repo;

    this.newProject.name = repo.name;
    this.newProject.repo.id = repo.id;
    this.newProject.repo.name = repo.name;
    this.newProject.repo.url = repo.html_url;
    this.newProject.repo.isPrivate = repo.isPrivate || false;
    this.newProject.repo.url = repo.html_url;
    this.newProject.userId = 'amfritz'; // todo -- get this from auth service
    this.newProject.description = repo.description;

    // the assumption is that commits won't occur while working here, so save api results
    if (this.selectedRepo.commits === undefined) {
      this.githubService.getRepoCommits(repo.name).subscribe({
              next: (resp) => {
                repo.commits = resp;
                this.gitCommits = resp;
                //console.log(this.commits);
              },
              error: (err) => console.log('error', err)
            });
    } else {
      this.gitCommits = this.selectedRepo.commits;
    }

  }

  createProject () {
    // todo -- on success, navigate to new project
      if (this.useCommitList) {
          for (let i = 0; i < this.gitCommits.length; i++) {
              let commit = this.gitCommits[i];
              this.newProject.events.push({userId: 'amfritz', eventDescription: commit.commit.message, eventDate: commit.commit.author.date,
                    isNewEvent: true, repoName: this.selectedRepo?.name});
          }
      }
    this.projectService.createProject(this.newProject).subscribe( {
      next: (resp) => this.router.navigate(['/project', resp.projectId]),
      error: (err ) => this.toastService.showToast("An error occurred saving the new item: " + err.toString() , 'error'),
          });
  }


}
