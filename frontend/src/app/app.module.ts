import {LOCALE_ID, NgModule} from '@angular/core';
import {FlexLayoutModule} from '@angular/flex-layout';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {ExternalConfigurationService} from './external-configuration.service';
import {AngularHalModule} from 'angular6-hal-client';
import {NavbarComponent} from './navbar/navbar.component';
import {MaterialModule} from './material.module';
import {HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule} from '@angular/common/http';
import {AuthInterceptor} from './interceptors/auth-interceptor.service';
import {LoginModule} from './login/login.module';
import {LoadingBarHttpClientModule} from "@ngx-loading-bar/http-client";
import {LoadingBarRouterModule} from "@ngx-loading-bar/router";
import {LoadingBarHttpModule} from "@ngx-loading-bar/http";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {SettingsComponent} from "./settings/settings.component";
import {DragDropModule} from "@angular/cdk/drag-drop";
import {CoreModule} from "./core/core.module";
import {PartiesModule} from "./parties/parties.module";
import {registerLocaleData} from "@angular/common";
import localeEn from '@angular/common/locales/en';
import localeNl from '@angular/common/locales/nl';

registerLocaleData(localeEn, 'en');
registerLocaleData(localeNl, 'nl');

@NgModule({
    declarations: [
        AppComponent,
        NavbarComponent,
        DashboardComponent,
        SettingsComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        DragDropModule,
        ReactiveFormsModule,
        FlexLayoutModule,
        LoadingBarHttpModule,
        LoadingBarRouterModule,
        LoadingBarHttpClientModule,
        AppRoutingModule,
        AngularHalModule.forRoot(),
        HttpClientModule,
        HttpClientXsrfModule,
        MaterialModule,
        LoginModule,
        CoreModule,
        PartiesModule,
    ],
    providers: [
        {provide: 'ExternalConfigurationService', useClass: ExternalConfigurationService},
        {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
        {provide: LOCALE_ID, useValue: 'nl'},
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
