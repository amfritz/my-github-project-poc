import { Component, inject, input, OnInit, resource } from '@angular/core';
import { GithubRepoService } from '../../services/github-repo.service';
import { GitHubCommit } from '../../models/git-hub-commit';
import { ProjectsService } from '../../services/projects.service';
import { ActivatedRoute } from '@angular/router';
import { GitHubRepo } from '../../models/git-hub-repo';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-commit-list',
  imports: [DatePipe],
  templateUrl: './commit-list.component.html',
  styleUrl: './commit-list.component.css'
})
export class CommitSelectComponent  {
  commits = input.required<GitHubCommit[]>();

}
