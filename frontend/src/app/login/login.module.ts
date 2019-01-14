import {NgModule} from '@angular/core';
import {FlexLayoutModule} from '@angular/flex-layout';
import {LoginComponent} from './login.component';
import {CommonModule} from '@angular/common';
import {LoginRoutingModule} from "./login-routing.module";
import {MaterialModule} from "../material.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        MaterialModule,
        FlexLayoutModule,
        LoginRoutingModule
    ],
    declarations: [
        LoginComponent
    ]
})
export class LoginModule {
}
