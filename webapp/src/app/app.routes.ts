import { Routes } from '@angular/router';
import { CreateProjectComponent } from './projects/create-project/create-project.component';
import { CommitSelectComponent } from './projects/commit-list/commit-list.component';
import { ProjectsComponent } from './projects/projects.component';
import { ViewProjectComponent } from './projects/view-project/view-project.component';

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
        path: 'project/{projectId}',
        component: ViewProjectComponent,
        title: 'View Project'
    },
    {
        path: 'repo/:repoName',
        component: CommitSelectComponent,
        title: 'Select repository to watch'
    }
];
