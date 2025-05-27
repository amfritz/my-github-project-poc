import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';

 interface GitRepo {
    id: string;
    name: string;
    full_name: string;
    description: string;    
    html_url: string;
    private: boolean;    
  };

@Component({
  selector: 'app-root',
  imports: [],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'webapp';
  private http = inject(HttpClient);
  gitRepos:GitRepo[]|undefined = undefined;

 
  ngOnInit(): void {
      this.http.get<GitRepo[]>("/api/repos").subscribe({
        next: (resp) => this.gitRepos = resp,
        error: (err) => console.log('error', err)
      })
  }
}
