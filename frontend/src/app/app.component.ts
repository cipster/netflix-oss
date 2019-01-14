import {Component, OnInit} from '@angular/core';

declare var device;

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

    ngOnInit() {
        document.addEventListener('deviceready', function () {
            alert(device.platform);
            console.log(device.platform);
            console.log('cucu')
        }, false);

        document.addEventListener('backbutton', function (evt) {
            if (device.platformId !== 'windows') {
                return;
            }

            let firstPageUrl = '/';
            if (window.location.href !== firstPageUrl) {
                window.history.back();
            } else {
                throw new Error('Exit'); // This will suspend the app
            }
        }, false);
    }
}
