import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Solicitudesadmincomponent } from './solicitudesadmincomponent';

describe('Solicitudesadmincomponent', () => {
  let component: Solicitudesadmincomponent;
  let fixture: ComponentFixture<Solicitudesadmincomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Solicitudesadmincomponent],
    }).compileComponents();

    fixture = TestBed.createComponent(Solicitudesadmincomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
