import {Injectable} from '@angular/core';
import {MenuButton} from "./menu.button";

@Injectable({
    providedIn: 'root'
})
export class MenuService {

    constructor() {
    }

    getMenuButtons(): MenuButton[] {
        return [new MenuButton('/dashboard', 'Dashboard')];
    }
}
