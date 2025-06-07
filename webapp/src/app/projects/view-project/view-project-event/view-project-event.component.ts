import {Component, input, output} from '@angular/core';
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
    delete = output();
    value = output<string>();
    visited = output();

    eventChange(event: Event) {
        const target = event.target as HTMLTextAreaElement;
        if (target.value === '') {
            this.delete.emit();
            return;
        }
        this.value.emit(target.value);
    }

    onMouseEnter() {
        if (this.event().isNewEvent) {
            this.visited.emit();
        }
    }

}
