import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Usuariosadmincomponent } from './usuariosadmincomponent';

describe('Usuariosadmincomponent', () => {
  let component: Usuariosadmincomponent;
  let fixture: ComponentFixture<Usuariosadmincomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Usuariosadmincomponent],
    }).compileComponents();

    fixture = TestBed.createComponent(Usuariosadmincomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
