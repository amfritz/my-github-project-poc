import {Component, input} from '@angular/core';
import {DatePipe} from '@angular/common';
import {ProjectEntity} from '../../../models/project';

@Component({
  selector: 'app-view-project-details',
    imports: [
        DatePipe
    ],
  templateUrl: './view-project-details.component.html',
  styleUrl: './view-project-details.component.css'
})
export class ViewProjectDetailsComponent {
    project = input.required<ProjectEntity>();

}
