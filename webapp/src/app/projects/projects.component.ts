import { Component, inject, OnInit } from '@angular/core';
import { ProjectsService } from '../services/projects.service';
import { ProjectEntity } from '../models/project';
import { RouterLink } from '@angular/router';
import { CommitSelectComponent } from "./commit-list/commit-list.component";

@Component({
  selector: 'app-watched-repos',
  imports: [RouterLink],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.css'
})
export class ProjectsComponent implements OnInit {
  private projectService = inject(ProjectsService);
  projects: ProjectEntity[] = [];
  selectedRepoName ='';

  ngOnInit(): void {
      if (this.projects.length === 0) {
        this.projectService.getProjects().subscribe({
          next: (resp) => this.projects = resp,
          error: (err) => console.log("error ", err)
        })
      }
  }

  showCommits(project:ProjectEntity) {
    this.selectedRepoName = project.repo.name;
  }
}
