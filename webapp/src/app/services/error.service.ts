import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ErrorService {
  private _error = signal('');

  error = this._error.asReadonly();

  // TODO this should be a toast or something
  // TODO this will need more work 
  showError(message: string) {
    console.log(message);
    this._error.set(message);
  }

  clearError() {
    this._error.set('');
  }
}
