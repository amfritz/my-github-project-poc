export interface ProjectEntity {
    id?: string;
    user_id: string;
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
