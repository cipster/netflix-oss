import {Resource} from 'angular6-hal-client';

export class User extends Resource {
    username: string;
    name: string;
    password: string;
    enabled: boolean;
    locked: boolean;
    accountExpiryDate: string;
    credentialsExpiryDate: string;
    roles: string[];
}
