import {Injectable} from '@angular/core';
import {MenuButton} from "./menu.button";

@Injectable({
    providedIn: 'root'
})
export class MenuService {

    constructor() {
    }

    static getMenuButtons(): MenuButton[] {
        return [
            new MenuButton('/tasks', 'Tasks', 'ballot'),
            new MenuButton('/clients', 'Clients', 'work'),
            new MenuButton('/parties', 'Parties', 'business'),
            new MenuButton('/assignments', 'Assignments', 'assignment'),
            new MenuButton('/dashboard', 'Dashboard', 'dashboard'),
        ];
    }
}
