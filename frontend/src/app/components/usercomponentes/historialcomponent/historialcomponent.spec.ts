import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Historialcomponent } from './historialcomponent';

describe('Historialcomponent', () => {
  let component: Historialcomponent;
  let fixture: ComponentFixture<Historialcomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Historialcomponent],
    }).compileComponents();

    fixture = TestBed.createComponent(Historialcomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
