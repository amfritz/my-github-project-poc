import { Component, inject, input, OnInit } from '@angular/core';
import { ToastService } from '../../services/toast.service';

@Component({
    selector: 'app-toast',
    imports: [],
    templateUrl: './toast.component.html',
    styleUrl: './toast.component.css'
})
export class ToastComponent implements OnInit {
    toastService = inject(ToastService);
    message = '';
    type = '';
    isVisible = false;

    // TODO -- animate the toast. it's functional, but it can be prettier.
    // TODO -- there are some race conditions where when the app is initalized the empty toast will show when the page is displayed
    // fix that.
    ngOnInit(): void {
        this.toastService.toast.subscribe( (msg) => {
            // console.log('toast observable: ', msg);
            this.message = msg.message;
            this.type = msg.type;
            this.isVisible = msg.message !== '';
            setTimeout( () => this.isVisible = false, 3000);
        })
    }

}
