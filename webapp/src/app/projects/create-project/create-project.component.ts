import {Component, inject, OnInit, signal} from '@angular/core';
import {GithubRepoService} from '../../services/github-repo.service';
import {GitHubRepo} from '../../models/git-hub-repo';
import {CommitSelectComponent} from "../commit-list/commit-list.component";
import {GitHubCommit} from '../../models/git-hub-commit';
import {ProjectsService} from '../../services/projects.service';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {CreateEvents, ProjectPostRequest} from '../../models/project-post-request';
import {ToastService} from '../../services/toast.service';
import {SelectRepositoryComponent} from '../select-repository/select-repository.component';
import {CreateProjectFormComponent, NewProject} from './create-project-form/create-project-form.component';

@Component({
    selector: 'app-create-project',
    imports: [CommitSelectComponent, FormsModule, SelectRepositoryComponent, CreateProjectFormComponent],
    templateUrl: './create-project.component.html',
    styleUrl: './create-project.component.css'
})
export class CreateProjectComponent implements OnInit {
    private githubService = inject(GithubRepoService);
    private projectService = inject(ProjectsService);
    // projects = input.required<ProjectEntity[]>();
    gitRepos = signal<GitHubRepo[]>([]);
    gitCommits = signal<GitHubCommit[]>([]);
    selectedRepo = signal<GitHubRepo | undefined>(undefined);
    projectName = signal<string>('');
    projectDescription = signal<string>('');
    newProject: ProjectPostRequest = {
        name: '',
        description: '',
        repo: {name: '', id: '', url: '', isPrivate: false, createdAt: ''},
        events: []
    };
    isLoading = signal<boolean>(false);
    router = inject(Router);
    toastService = inject(ToastService);
    loadingMessage = '';


    ngOnInit(): void {
        // todo -- need a pretty isloading
        this.loadingMessage = 'Loading repositories...';
        this.isLoading.set(true);
        this.githubService.getUserRepos().subscribe({
            next: (resp) => {
                // filter out any repos already attached to a project
                this.gitRepos.set([... resp.filter(r =>
                    this.projectService.loadedProjects().filter(p => p.repo.name === r.name).length === 0)]);
                this.isLoading.set(false);
                this.selectRepo(undefined);
            },
            error: (err) => {
                this.toastService.showToast("An error occurred loading the repositories: " + err.toString(), 'error');
                this.isLoading.set(false);
            }
        });
    }

    cancelNavigation() {
        this.router.navigate(['/']);
    }

    selectRepo(repo: GitHubRepo | undefined) {
        if (repo === undefined) {
            // currently selected, so unselect it
            this.selectedRepo.set(undefined);
            this.gitCommits.set([]);
            return;
        } else {
            // set the selected class on the element
            this.selectedRepo.set(repo);
            this.projectName.set(repo.name);
            this.projectDescription.set(repo.description);

            // the assumption is that commits won't occur while working here, so save api results
            if (this.selectedRepo()?.commits === undefined) {
                this.githubService.getRepoCommits(repo.name).subscribe({
                    next: (resp) => {
                        repo.commits = resp;
                        this.gitCommits.set([...resp]);
                        //console.log(this.commits);
                    },
                    error: (err) => console.log('error', err)
                });
            } else
                { // @ts-ignore
                    this.gitCommits.set([...this.selectedRepo()?.commits]);
                }
            }
        }

    createProject(newProject: NewProject) {
        // todo -- on success, navigate to new project
        let initEvents:CreateEvents[] = [];
        if (newProject.useCommits) {
            for (let i = 0; i < this.gitCommits().length; i++) {
                let commit = this.gitCommits()[i];
                initEvents.push({
                    eventDescription: commit.commit.message, eventDate: commit.commit.author.date,
                    isNewEvent: false, repoName: this.selectedRepo?.name
                });
            }
        }
        let projectRequest: ProjectPostRequest = {
            name: newProject.name,
            description: newProject.description,
            repo: {
                id: this.selectedRepo()?.id || '',
                name: this.selectedRepo()?.name || '',
                url: this.selectedRepo()?.html_url || '',
                isPrivate: this.selectedRepo()?.isPrivate || false
            },
            events: initEvents
    };

        this.loadingMessage = 'Creating project...';
        this.isLoading.set(true);
        this.projectService.createProject(projectRequest).subscribe({
            next: (resp) => {
                this.router.navigate(['/project', resp.projectId]);
                this.isLoading.set(false);
            },
            error: (err) => this.toastService.showToast("An error occurred saving the new item: " + err.toString(), 'error'),
        });
    }


}
