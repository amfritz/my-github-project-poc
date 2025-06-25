import {Component, input, output, signal} from '@angular/core';
import {GitHubRepo} from '../../models/git-hub-repo';

@Component({
  selector: 'app-select-repository',
  imports: [],
  templateUrl: './select-repository.component.html',
  styleUrl: './select-repository.component.css'
})
export class SelectRepositoryComponent {
    repositories = input.required<GitHubRepo[]>();
    selected = output<GitHubRepo|undefined>();
    selectedRepo = signal<GitHubRepo|undefined>( undefined);

    selectRepo(repo: GitHubRepo) {
        if (this.selectedRepo()?.name === repo.name) {
            // currently selected, so unselect it
            this.selectedRepo.set(undefined);
            this.selected.emit(undefined);
        } else {
            this.selected.emit(repo);
            this.selectedRepo.set(repo);
        }
    }

}
