import { Routes } from '@angular/router';
import { RepoSelectComponent } from './repo/repo-select/repo-select.component';
import { CommitSelectComponent } from './repo/commit-select/commit-select.component';

export const routes: Routes = [
    {
        path: '',
        component: RepoSelectComponent,
        title: 'Select repositories'
    },
    {
        path: 'repo/:repoName',
        component: CommitSelectComponent,
    }
];
