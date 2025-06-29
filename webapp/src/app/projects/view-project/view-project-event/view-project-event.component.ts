import {Component, Host, HostListener, input, model, output, signal} from '@angular/core';
import {DatePipe} from "@angular/common";
import {ProjectEvents} from '../../../models/project-events';
import {EditItemComponent} from '../../../shared/edit-item/edit-item.component';

@Component({
  selector: 'app-view-project-event',
    imports: [
        DatePipe,
        EditItemComponent
    ],
    host: {
        '(mouseenter)': 'onMouseEnter()',
        '(mouseleave)': 'onMouseLeave()',
        '(focus)': 'onFocus()',
        '(click)': 'onFocus()',
    },
    templateUrl: './view-project-event.component.html',
    styleUrl: './view-project-event.component.css'
})
export class ViewProjectEventComponent {
    event = input.required<ProjectEvents>();
    editable = input<boolean>(true);
    canDelete = input<boolean>(true);
    delete = output();
    value = output<string>();
    visited = output();
    showEdit = signal(false);
    hasFocus = signal(false);
    selfDropFlag = false;
    dropTaget = false;

    onMouseEnter() {
        if (this.event().isNewEvent) {
            this.visited.emit();
        }
        this.hasFocus.set(true);
    }

    onMouseLeave() {
        this.hasFocus.set(false);
    }


    showEditDialog() {
        if (!this.editable()) {
            return;
        }
        this.showEdit.set(true);
    }
    updateEventDesc(newDesc:string) {
        if (newDesc !== null) {
            if (newDesc.trim() === '' && this.canDelete()) {
                this.delete.emit();
            } else if (newDesc !== this.event().eventDescription) {
                this.value.emit(newDesc);
            }
        }
        this.showEdit.set(false);
    }

    onFocus() {
        if (this.event().isNewEvent) {
            this.visited.emit();
        }
        this.hasFocus.set(true);
    }   

    onDragStart(drag:DragEvent){
        drag.dataTransfer?.setData('text/plain', JSON.stringify(this.event()));
        this.selfDropFlag = false;
        this.dropTaget = false;
    }

    onDragOver(drag:DragEvent){
        // apparently this is required by the api
        drag.preventDefault();
    }

    onDragEnd(drag:DragEvent){
        if (drag.dataTransfer?.dropEffect === 'none') {
            // not dropped on any target/cancel so don't do anything
            return;
        }

        if (this.selfDropFlag ) {
            this.selfDropFlag = false;
        }
        else {
            // emit delete event for the dropped item
            this.delete.emit();
        }
    }

    onDrop(drop:DragEvent){
        if(!this.editable()) {
            return;
        }
        this.dropTaget = true;
        drop.preventDefault();
        let val = drop.dataTransfer?.getData('text/plain');
        if (!val) {
            console.log('unable to retrieve drop data transfer');
            return;
        }
        let dropEvent =  JSON.parse(val) as ProjectEvents;
        // todo what if something  else is dropped here
        if (dropEvent.id === this.event().id) {
            // console.log('dropped on self');
            this.selfDropFlag = true;
            return;
        }
        this.selfDropFlag = false;
        let newDesc = this.event().eventDescription + '\n' + dropEvent.eventDescription;
        this.value.emit(newDesc);
    }

}
