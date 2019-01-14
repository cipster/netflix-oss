import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from "./login/auth.guard";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {SettingsComponent} from "./settings/settings.component";

const routes: Routes = [
    {
        path: 'dashboard',
        canActivate: [AuthGuard],
        pathMatch: 'full',
        component: DashboardComponent
    },
    {
        path: 'settings',
        canActivate: [AuthGuard],
        pathMatch: 'full',
        component: SettingsComponent
    },
    {
        path: '',
        redirectTo: '/dashboard',
        canActivate: [AuthGuard],
        pathMatch: 'full'
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
