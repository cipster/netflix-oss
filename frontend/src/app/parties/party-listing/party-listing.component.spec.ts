import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PartyListingComponent} from './party-listing.component';

describe('PartyListingComponent', () => {
    let component: PartyListingComponent;
    let fixture: ComponentFixture<PartyListingComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [PartyListingComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(PartyListingComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
