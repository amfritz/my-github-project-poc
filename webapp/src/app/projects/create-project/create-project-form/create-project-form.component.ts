import {Component, input, model, output, signal} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

export interface NewProject {
    name: string;
    description: string;
    useCommits: boolean;
}

@Component({
  selector: 'app-create-project-form',
    imports: [
        ReactiveFormsModule,
        FormsModule
    ],
  templateUrl: './create-project-form.component.html',
  styleUrl: './create-project-form.component.css'
})
export class CreateProjectFormComponent {
    name = model<string>('');
    description = model<string>('');
    useCommits = signal<boolean>(true);
    canCreate = input<boolean>(true);
    save = output<NewProject>();
    cancel = output<void>();

    createProject() {
        this.save.emit({
            name: this.name(),
            description: this.description(),
            useCommits: this.useCommits()
        });
    }

}
