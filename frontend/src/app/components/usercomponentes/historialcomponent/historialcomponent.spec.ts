import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { Historialcomponent } from './historialcomponent';

describe('Historialcomponent', () => {
  let component: Historialcomponent;
  let fixture: ComponentFixture<Historialcomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Historialcomponent],
      imports: [FormsModule],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(Historialcomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
