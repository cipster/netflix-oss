import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from "./login/auth.guard";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {SettingsComponent} from "./settings/settings.component";
import {PartyListingComponent} from "./parties/party-listing/party-listing.component";
import {PartyDetailComponent} from "./parties/party-detail/party-detail.component";

const routes: Routes = [
    {
        path: 'tasks',
        canActivate: [AuthGuard],
        pathMatch: 'full',
        component: DashboardComponent
    },
    {
        path: 'clients',
        canActivate: [AuthGuard],
        pathMatch: 'full',
        component: DashboardComponent
    },
    {
        path: 'parties',
        canActivate: [AuthGuard],
        pathMatch: 'full',
        component: PartyListingComponent
    },
    {
        path: 'parties/:id',
        canActivate: [AuthGuard],
        pathMatch: 'full',
        component: PartyDetailComponent
    },
    {
        path: 'assignments',
        canActivate: [AuthGuard],
        pathMatch: 'full',
        component: DashboardComponent
    },
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
        redirectTo: '/tasks',
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
