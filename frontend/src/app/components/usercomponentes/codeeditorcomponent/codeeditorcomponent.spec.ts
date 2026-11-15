import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Codeeditorcomponent } from './codeeditorcomponent';

describe('Codeeditorcomponent', () => {
  let component: Codeeditorcomponent;
  let fixture: ComponentFixture<Codeeditorcomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Codeeditorcomponent],
    }).compileComponents();

    fixture = TestBed.createComponent(Codeeditorcomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
