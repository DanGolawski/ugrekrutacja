import { CommonModule, registerLocaleData } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import localePl from '@angular/common/locales/pl';
import {
  FormArray,
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { ConversionService } from '../services/conversion.service';
import { Computer, ConversionRequest } from '../models/conversion.models';

registerLocaleData(localePl);

@Component({
  selector: 'app-conversion-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './conversion-page.component.html',
  styleUrl: './conversion-page.component.css'
})
export class ConversionPageComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly conversionService = inject(ConversionService);

  readonly computers = this.formBuilder.array([
    this.createComputerForm('ACER Aspire', 345, '2026-07-03'),
    this.createComputerForm('DELL Latitude', 543, '2026-07-12'),
    this.createComputerForm('HP Victus', 346, '2026-07-15')
  ]);

  readonly form = this.formBuilder.group({
    computers: this.computers
  });

  results: Computer[] = [];
  isSaving = false;
  errorMessage = '';
  successMessage = '';

  addComputer(): void {
    this.computers.push(this.createComputerForm('', null, ''));
  }

  removeComputer(index: number): void {
    if (this.computers.length > 1) {
      this.computers.removeAt(index);
    }
  }

  submit(): void {
    this.errorMessage = '';
    this.successMessage = '';
    this.results = [];

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const request: ConversionRequest[] = this.computers.getRawValue().map(computer => ({
      name: computer.name.trim(),
      amountUsd: Number(computer.amountUsd),
      accountingDate: computer.accountingDate
    }));

    this.isSaving = true;
    this.conversionService.convertAndSave(request).subscribe({
      next: results => {
        this.results = results;
        this.successMessage = 'Operacja przebiegła pomyślnie. Komputery zostały przeliczone i zapisane.';
        this.isSaving = false;
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = this.getErrorMessage(error);
        this.isSaving = false;
      }
    });
  }

  private createComputerForm(name: string, amountUsd: number | null, accountingDate: string) {
    return this.formBuilder.nonNullable.group({
      name: [name, [Validators.required, Validators.maxLength(150)]],
      amountUsd: [amountUsd ?? 0, [Validators.required, Validators.min(0.01)]],
      accountingDate: [accountingDate, Validators.required]
    });
  }

  private getErrorMessage(error: HttpErrorResponse): string {
    if (error.status === 0) {
      return 'Nie udało się połączyć z serwerem. Sprawdź, czy backend jest uruchomiony.';
    }

    if (error.status === 400) {
      return 'Backend odrzucił dane. Sprawdź nazwy, kwoty i daty księgowania.';
    }

    return 'Wystąpił błąd podczas przeliczania i zapisywania. Spróbuj ponownie.';
  }
}
