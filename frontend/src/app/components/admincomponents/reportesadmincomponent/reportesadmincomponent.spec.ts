import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Reportesadmincomponent } from './reportesadmincomponent';

describe('Reportesadmincomponent', () => {
  let component: Reportesadmincomponent;
  let fixture: ComponentFixture<Reportesadmincomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Reportesadmincomponent],
    }).compileComponents();

    fixture = TestBed.createComponent(Reportesadmincomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
