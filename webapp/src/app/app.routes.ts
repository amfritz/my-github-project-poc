import { Routes } from '@angular/router';
import { CreateProjectComponent } from './projects/create-project/create-project.component';
import { CommitSelectComponent } from './projects/commit-list/commit-list.component';
import { ProjectsComponent } from './projects/projects.component';

export const routes: Routes = [
    {
        path: '',
        component: ProjectsComponent,
        title: 'Projects List'
    },
    {
        path: 'create-project',
        component: CreateProjectComponent,
        title: 'Create New Project'
    },
    {
        path: 'repo/:repoName',
        component: CommitSelectComponent,
        title: 'Select repository to watch'
    }
];
