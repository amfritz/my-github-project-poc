import {Component, signal} from '@angular/core';

@Component({
  selector: 'app-display-toggle',
  imports: [],
  templateUrl: './display-toggle.component.html',
  styleUrl: './display-toggle.component.css'
})
export class DisplayToggleComponent {
    showAbout = signal<boolean>(false);
}
