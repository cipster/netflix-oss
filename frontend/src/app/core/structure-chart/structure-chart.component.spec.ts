import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {StructureChartComponent} from './structure-chart.component';

describe('StructureChartComponent', () => {
    let component: StructureChartComponent;
    let fixture: ComponentFixture<StructureChartComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [StructureChartComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(StructureChartComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
