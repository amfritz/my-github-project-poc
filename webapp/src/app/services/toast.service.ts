import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ToastMessage, ToastSuccess, ToastType } from '../models/toast.model';

@Injectable({
    providedIn: 'root'
})
export class ToastService {

    constructor() { }

    toastSubject = new BehaviorSubject<ToastMessage>({message: '', type: ToastSuccess});
    toast = this.toastSubject.asObservable();

    showToast(toastMessage: string, toastType: ToastType) {
        // console.log('show toast: ', toastMessage);
        this.toastSubject.next({message: toastMessage, type: toastType });
    }

}
