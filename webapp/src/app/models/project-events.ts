export interface ProjectEvents {
    id: string;
    userId: string;
    projectId: string;
    eventDescription: string;
    eventDate: string;
    repoName?: string;
    branch_name?: string;
    isNewEvent?: boolean;
    createdDt?: string;
    updatedDt?: string;
}
