import {Component, input, model, OnInit, output} from '@angular/core';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-edit-item',
  imports: [FormsModule],
  templateUrl: './edit-item.component.html',
  styleUrl: './edit-item.component.css'
})
export class EditItemComponent implements OnInit {
    description = input.required<string>();
    ngModelDesc = model('');
    initialDescription = '';
    close = output<string>();

    ngOnInit(): void {
        this.initialDescription = this.description();
        this.ngModelDesc.set(this.description());
    }

    onSubmit() {
        this.close.emit(this.ngModelDesc());
    }

    onCancel() {
        this.close.emit(this.initialDescription);
    }
    
    onKeyDown(event: KeyboardEvent) {
        if (event.key === 'Escape') {
            this.onCancel();
        }
    }
}
