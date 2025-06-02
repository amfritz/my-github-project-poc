import { Component, inject, input, OnInit } from '@angular/core';
import { ProjectEntity } from '../../models/project';
import { ProjectsService } from '../../services/projects.service';

@Component({
  selector: 'app-view-project',
  imports: [],
  templateUrl: './view-project.component.html',
  styleUrl: './view-project.component.css'
})
export class ViewProjectComponent implements OnInit {
  projectId = input.required<string>();
  private projectService = inject(ProjectsService);
  project: ProjectEntity | undefined = undefined;

  ngOnInit(): void {
    this.project = this.projectService.getProjectById(this.projectId());
      
  }

}
