export interface RegisterRequestDto {
    email: string;
    username: string;
    displayName: string;
    password: string;
    confirmPassword: string;
}

export interface LoginRequestDto {
    login: string;
    password: string;
}