import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { ConversionService } from '../services/conversion.service';
import { ConversionPageComponent } from './conversion-page.component';

describe('ConversionPageComponent', () => {
  it('shows a success message after the request completes successfully', async () => {
    const conversionService = jasmine.createSpyObj<ConversionService>(
      'ConversionService',
      ['convertAndSave']
    );
    conversionService.convertAndSave.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [ConversionPageComponent],
      providers: [
        provideRouter([]),
        { provide: ConversionService, useValue: conversionService }
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(ConversionPageComponent);
    fixture.detectChanges();

    fixture.componentInstance.submit();
    fixture.detectChanges();

    const successMessage = fixture.nativeElement.querySelector('[role="status"]');
    expect(successMessage).toBeTruthy();
    expect(successMessage.textContent).toContain('Operacja przebiegła pomyślnie');
  });
});
