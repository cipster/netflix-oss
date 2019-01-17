import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PartyListingComponent} from './party-listing/party-listing.component';
import {MaterialModule} from "../material.module";
import {FlexLayoutModule} from "@angular/flex-layout";
import {PartyDetailComponent} from './party-detail/party-detail.component';
import {AppRoutingModule} from "../app-routing.module";
import {CoreModule} from "../core/core.module";

@NgModule({
    declarations: [
        PartyListingComponent,
        PartyDetailComponent,
    ],
    imports: [
        CommonModule,
        AppRoutingModule,
        MaterialModule,
        FlexLayoutModule,
        CoreModule,
    ]
})
export class PartiesModule {
}
