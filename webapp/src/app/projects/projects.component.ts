import {Component, inject, OnInit} from '@angular/core';
import { ProjectsService } from '../services/projects.service';
import { ProjectEntity } from '../models/project';
import { RouterLink } from '@angular/router';
import {DisplayToggleComponent} from '../shared/display-toggle/display-toggle.component';

@Component({
    selector: 'app-watched-repos',
    imports: [RouterLink, DisplayToggleComponent],
    templateUrl: './projects.component.html',
    styleUrl: './projects.component.css'
})
export class ProjectsComponent implements OnInit {
  private projectService = inject(ProjectsService);
  projects: ProjectEntity[] = [];
  isLoading = false;

  ngOnInit(): void {
      this.projects = this.projectService.loadedProjects();
      // if (this.projects.length === 0) {
      if (!this.projectService.isInitialized() || this.projects.length === 0) {
        this.isLoading = true;
        this.projectService.getProjects().subscribe({
          next: (resp) => {
            this.projects = resp;
            this.isLoading = false;
          },
          error: (err) => {
            console.log("error ", err);
            this.isLoading = false;
          }
        })
      }
  }

}
