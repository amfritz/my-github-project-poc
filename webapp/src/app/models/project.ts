export interface ProjectEntity {
    id?: string;
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
    createdAt?: string;    
}

export function emptyProject(): ProjectEntity {
    return {
        userId: '',
        name: '',
        description: '',
        repo: {
            id: '',
            name: '',
            url: '',
            isPrivate: false,
            createdAt: undefined
        }
    };
}