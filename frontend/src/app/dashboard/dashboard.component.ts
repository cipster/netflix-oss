import {Component, OnInit} from '@angular/core';
import {CdkDragDrop, moveItemInArray} from "@angular/cdk/drag-drop";

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
    widgets = [
        {title: 'Widget 1', flex: 30, content: 'Content for Widget 1'},
        {title: 'Widget 2', flex: 30, content: 'Content for Widget 2'},
        {title: 'Widget 3', flex: 30, content: 'Content for Widget 3'},
        {title: 'Widget 4', flex: 30, content: 'Content for Widget 4'},
        {title: 'Widget 5', flex: 30, content: 'Content for Widget 5'},
        {title: 'Widget 6', flex: 30, content: 'Content for Widget 6'}];

    constructor() {
    }

    ngOnInit() {
    }

    drop(event: CdkDragDrop<string[]>) {
        moveItemInArray(this.widgets, event.previousIndex, event.currentIndex);
    }

}
