import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Mejoracodigocomponent } from './mejoracodigocomponent';

describe('Mejoracodigocomponent', () => {
  let component: Mejoracodigocomponent;
  let fixture: ComponentFixture<Mejoracodigocomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Mejoracodigocomponent],
    }).compileComponents();

    fixture = TestBed.createComponent(Mejoracodigocomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
