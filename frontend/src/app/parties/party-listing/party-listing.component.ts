import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'grub-party-listing',
    templateUrl: './party-listing.component.html',
    styleUrls: ['./party-listing.component.scss']
})
export class PartyListingComponent implements OnInit {

    parties = [
        {
            id: 1,
            name: 'Cipster ZZP',
            type: 'INDIVIDUAL',
            countryOfResidence: 'RO'
        }, {
            id: 2,
            name: 'CW BV',
            type: 'COMPANY',
            countryOfResidence: 'NL'
        }, {
            id: 3,
            name: 'Wealthy Client BV',
            type: 'COMPANY',
            countryOfResidence: 'NL'
        }];

    ngOnInit() {
    }

}
