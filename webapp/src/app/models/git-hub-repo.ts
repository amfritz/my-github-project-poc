import { GitHubCommit } from "./git-hub-commit";

 export interface GitHubRepo {
    id: string;
    name: string;
    full_name: string;
    description: string;
    html_url: string;
    isPrivate: boolean;
    // not used by the back end, but client only from here
    commits?: GitHubCommit[];
  }
