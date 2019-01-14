import {Injectable, Injector} from '@angular/core';
import {User} from '../model/user.model';
import {RestService} from 'angular6-hal-client';

@Injectable()
export class UserService extends RestService<User> {
    constructor(injector: Injector) {
        super(User, 'users', injector);
    }
}
