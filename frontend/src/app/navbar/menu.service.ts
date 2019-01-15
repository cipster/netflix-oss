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
            new MenuButton('/tasks', 'Tasks'),
            new MenuButton('/clients', 'Clients'),
            new MenuButton('/parties', 'Parties'),
            new MenuButton('/assignments', 'Assignments'),
            new MenuButton('/dashboard', 'Dashboard'),
        ];
    }
}
