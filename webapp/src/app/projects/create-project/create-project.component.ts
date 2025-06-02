import { Component, inject, input, OnInit } from '@angular/core';
import { GithubRepoService } from '../../services/github-repo.service';
import { GitHubRepo } from '../../models/git-hub-repo';
import { CommitSelectComponent } from "../commit-list/commit-list.component";
import { GitHubCommit } from '../../models/git-hub-commit';
import { ProjectEntity, emptyProject } from '../../models/project';
import { ProjectsService } from '../../services/projects.service';
import { Location } from '@angular/common';
import { FormsModule } from '@angular/forms';

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
  newProject = emptyProject();

 
  ngOnInit(): void
  {
    console.log('create-project component init: ');
    console.dir(this.projectService.loadedProjects());
    // todo -- need a isloading 
    this.githubService.getUserRepos().subscribe({
        next: (resp) => this.loadRepos(resp),
        error: (err) => console.log('error', err)
      });
  }

  loadRepos(repos:GitHubRepo[]) {
    var projects = this.projectService.loadedProjects();
    this.gitRepos = repos.filter( r => projects.filter(p => p.repo.name === r.name).length === 0);
  }

  selectRepo(repo:GitHubRepo) {
    if (this.selectedRepo?.name === repo.name) {
      // currently selected, so unselect it
      this.selectedRepo = undefined;
      this.gitCommits = [];
      this.newProject = emptyProject();
      return;
    }
 
    // set the selelcted class on the elemetn
    this.selectedRepo = repo;

    this.newProject.name = repo.name;
    this.newProject.repo.id = repo.id;
    this.newProject.repo.name = repo.name; 
    this.newProject.repo.url = repo.html_url;
    this.newProject.repo.isPrivate = repo.isPrivate || false;
    this.newProject.repo.url = repo.html_url;
    this.newProject.userId = 'amfritz'; // todo -- get this from auth service
    this.newProject.description = repo.description;

    // assumption is that commits won't occure while working here, so save api results
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
   
    this.projectService.createProject(this.newProject, true).subscribe( {
      next: (resp) => this.location.back(),
      error: (err ) => console.log(err),
          })
  }


}
