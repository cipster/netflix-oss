import {NgModule} from '@angular/core';
import {LoginComponent} from './login.component';
import {RouterModule, Routes} from "@angular/router";


const authRoutes: Routes = [
    {path: 'login', component: LoginComponent}
];

@NgModule({
    imports: [
        RouterModule.forRoot(authRoutes)
    ],
    exports: [
        RouterModule
    ]
})
export class LoginRoutingModule {
}
