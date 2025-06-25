export interface ProjectEvents {
    id: string;
    projectId: string;
    eventDescription: string;
    eventDate: string;
    isNewEvent?: boolean;
    createdDt?: string;
    updatedDt?: string;
}
