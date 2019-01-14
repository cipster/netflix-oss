import {Injectable} from '@angular/core';

import {Observable, of} from 'rxjs';
import {tap} from 'rxjs/operators';
import {Router} from "@angular/router";

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    isLoggedIn = true;
    // store the URL so we can redirect after logging in
    redirectUrl: string;

    constructor(private router: Router) {

    }

    login(): Observable<boolean> {

        return of(true).pipe(
            tap(val => {
                this.isLoggedIn = true
            })
        );
    }

    logout(): void {
        this.isLoggedIn = false;
        this.router.navigate(['/login'])
    }
}
