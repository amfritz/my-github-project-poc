import { Component, inject, input, OnInit } from '@angular/core';
import { ProjectEntity } from '../../models/project';
import { ProjectsService } from '../../services/projects.service';
import { DatePipe } from '@angular/common';
import { ViewProjectEventsComponent } from "./view-project-events/view-project-events.component";

@Component({
  selector: 'app-view-project',
  imports: [DatePipe, ViewProjectEventsComponent],
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
