import { Component, input } from '@angular/core';
import { ProjectEntity } from '../../models/project';

@Component({
  selector: 'app-view-project',
  imports: [],
  templateUrl: './view-project.component.html',
  styleUrl: './view-project.component.css'
})
export class ViewProjectComponent {
  project = input.required<ProjectEntity>();

}
