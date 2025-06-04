import { Component, inject, input, OnInit } from '@angular/core';
import { ProjectEvents } from '../../../models/project-events';
import { ProjectsService } from '../../../services/projects.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-view-project-events',
  imports: [DatePipe],
  templateUrl: './view-project-events.component.html',
  styleUrl: './view-project-events.component.css'
})
export class ViewProjectEventsComponent implements OnInit {
  projectId = input.required<string>();
  private projectService = inject(ProjectsService);
  projectEvents: ProjectEvents[] = [];
  isLoading = false;

  ngOnInit(): void {
    this.isLoading = true;
    this.projectService.getProjectEvents(this.projectId()).subscribe({
      next: (resp) => {
        this.projectEvents = resp.sort((a, b) => {
          return -(new Date(b.eventDate).getTime() - new Date(a.eventDate).getTime())
        });
        this.isLoading = false;
      },
      error: (err) => {
        console.log('error', err);
        this.isLoading = false;
      }
    });
  }
}
