import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Traductorcomponent } from './traductorcomponent';

describe('Traductorcomponent', () => {
  let component: Traductorcomponent;
  let fixture: ComponentFixture<Traductorcomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Traductorcomponent],
    }).compileComponents();

    fixture = TestBed.createComponent(Traductorcomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
