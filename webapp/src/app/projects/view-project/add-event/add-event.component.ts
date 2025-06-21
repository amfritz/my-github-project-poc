import {Component, computed, input, OnInit, output, signal} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {DatePipe} from '@angular/common';

export interface NewEvent {
    description: string;
    date: string;
}

@Component({
  selector: 'app-add-event',
    imports: [
        FormsModule
    ],
  templateUrl: './add-event.component.html',
  styleUrl: './add-event.component.css'
})
export class AddEventComponent implements OnInit {
    newEventDescription = signal<string>('');
    newEventDate = signal<string>('');
    newEventTime = signal<string>('');

    editable = input<boolean>(true);
    create = output<NewEvent>();

    canAdd = computed(() => this.newEventDescription().length > 0);
    datePipe = new DatePipe('en-US');

    ngOnInit(): void {
        this.initNewDate();
    }

    initNewDate() {
        this.newEventDate.set(this.datePipe.transform(new Date(), 'YYYY-MM-dd') || '');
        this.newEventTime.set(this.datePipe.transform(new Date(), 'HH:mm') || '');
    }

    onAdd() {
        let dateSt = this.datePipe.transform(new Date(this.newEventDate() + 'T' + this.newEventTime()), 'yyyy-MM-ddTHH:mm:ss.SSSZ') || '';

        this.create.emit({
            description: this.newEventDescription(),
            date: dateSt
        });
        this.newEventDescription.set('');
    }

}
