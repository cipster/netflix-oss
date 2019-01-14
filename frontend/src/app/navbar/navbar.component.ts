import {Component, OnInit, ViewChild} from '@angular/core';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {AuthService} from "../login/auth.service";
import {MenuService} from "./menu.service";
import {Router} from "@angular/router";
import {MatSidenav} from "@angular/material";

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {

    @ViewChild(MatSidenav) sidenav: MatSidenav;
    isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
        .pipe(
            map(result => result.matches)
        );
    menuButtons = this.menuService.getMenuButtons();

    constructor(private breakpointObserver: BreakpointObserver,
                private menuService: MenuService,
                private router: Router,
                private authService: AuthService) {
    }

    ngOnInit() {
        this.router.events.subscribe(() => {
            this.isHandset$.subscribe((isMobile) => {
                if (isMobile) {
                    this.sidenav.close();
                }
            });
        });
    }

    logout() {
        this.authService.logout();
    }
}
