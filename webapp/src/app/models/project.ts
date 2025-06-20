
export interface ProjectEntity {
    id?: string;
    projectId?: string;
    name: string;
    description: string;
    status: 'archived' | 'active' | '';
    repo: {
        id: string;
        name: string;
        url: string;
        isPrivate: boolean;
        hookId?: string;
        hookUrl?: string;
        createdAt?: string;
    }
    createdAt?: string;
    updatedAt?: string;
}
