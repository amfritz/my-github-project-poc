
export interface ProjectEntity {
    id?: string;
    projectId?: string;
    userId: string;
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

export function emptyProject(): ProjectEntity {
    return {
        userId: '',
        name: '',
        description: '',
        status: '',
        repo: {
            id: '',
            name: '',
            url: '',
            hookId: '',
            isPrivate: false,
            createdAt: undefined
        }
    };
}
