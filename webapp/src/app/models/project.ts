
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
            isPrivate: false,
            createdAt: undefined
        }
    };
}
