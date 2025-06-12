import {Component, input, model, output} from '@angular/core';
import {DatePipe} from "@angular/common";
import {ProjectEvents} from '../../../models/project-events';

@Component({
  selector: 'app-view-project-event',
    imports: [
        DatePipe
    ],
  templateUrl: './view-project-event.component.html',
  styleUrl: './view-project-event.component.css'
})
export class ViewProjectEventComponent {
    event = input.required<ProjectEvents>();
    canDelete = input<boolean>(true);
    delete = output();
    value = output<string>();
    visited = output();

    onMouseEnter() {
        if (this.event().isNewEvent) {
            this.visited.emit();
        }
    }

    editDescription() {
        let val = window.prompt('Enter a description', this.event().eventDescription);
        if (val!= null) {
            if (val === '' && this.canDelete()) {
                this.delete.emit();
            } else if (val !== this.event().eventDescription) {
                this.value.emit(val);
            }
        }
    }

}
