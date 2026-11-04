import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Headeradmincomponent } from './headeradmincomponent';

describe('Headeradmincomponent', () => {
  let component: Headeradmincomponent;
  let fixture: ComponentFixture<Headeradmincomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Headeradmincomponent],
    }).compileComponents();

    fixture = TestBed.createComponent(Headeradmincomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
