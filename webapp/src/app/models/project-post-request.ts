export interface ProjectPostRequest {
    name: string;
    description: string;
    repo: {
        id: string;
        name: string;
        url: string;
        isPrivate: boolean;
        createdAt?: string;
    }
    events: CreateEvents[];
}

export interface CreateEvents {
    eventDescription: string;
    eventDate: string;
    repoName?: string;
    isNewEvent?: boolean;
}
