export const ToastSuccess = 'success';
export const ToastError = 'error';
export const ToastInfo = 'info';

export type ToastType = 'success' | 'error' | 'info';

export interface ToastMessage  {
    message: string;
    type: ToastType;
}

