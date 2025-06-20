import { Component, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {ToastComponent} from './shared/toast/toast.component';

@Component({
  selector: 'app-root',
    imports: [RouterOutlet, ToastComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent  {
}
