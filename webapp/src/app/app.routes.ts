import { Routes } from '@angular/router';
import { RepoSelectComponent } from './repo/repo-select/repo-select.component';
import { CommitSelectComponent } from './repo/commit-select/commit-select.component';
import { WatchedReposComponent } from './projects/watched-repos/watched-repos.component';

export const routes: Routes = [
    {
        path: '',
        component: WatchedReposComponent,
        title: 'Watched repositories'
    },
    {
        path: 'select-repos',
        component: RepoSelectComponent,
        title: 'Available repositories'
    },
    {
        path: 'repo/:repoName',
        component: CommitSelectComponent,
        title: 'Select repository to watch'
    }
];
