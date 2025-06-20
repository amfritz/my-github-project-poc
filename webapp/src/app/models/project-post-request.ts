export interface ProjectPostRequest {
    userId: string;
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
    userId: string;
    eventDescription: string;
    eventDate: string;
    repoName?: string;
    isNewEvent?: boolean;
}
