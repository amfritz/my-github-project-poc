export interface GitHubCommit {
    sha: string;
    node_id: string;
    commit: {
        author: {
            date:string;
            name:string;
        }
        message: string;
    }
}
