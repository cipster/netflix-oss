import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StructureChartComponent} from './structure-chart/structure-chart.component';
import {SearchBarComponent} from './search-bar/search-bar.component';
import {MaterialModule} from "../material.module";
import {FlexLayoutModule} from "@angular/flex-layout";

@NgModule({
    declarations: [
        StructureChartComponent,
        SearchBarComponent,
    ],
    imports: [
        CommonModule,
        MaterialModule,
        FlexLayoutModule
    ],
    exports: [
        SearchBarComponent,
        StructureChartComponent,
    ]
})
export class CoreModule {
}
