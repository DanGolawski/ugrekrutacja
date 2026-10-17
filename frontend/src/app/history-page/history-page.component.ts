import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Computer, ComputerSearch, PageResponse } from '../models/conversion.models';
import { ConversionService } from '../services/conversion.service';

@Component({
  selector: 'app-history-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './history-page.component.html',
  styleUrl: './history-page.component.css'
})
export class HistoryPageComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly conversionService = inject(ConversionService);

  readonly filters = this.formBuilder.nonNullable.group({
    name: '',
    accountingDate: ''
  });

  computers: Computer[] = [];
  totalElements = 0;
  totalPages = 0;
  page = 0;
  readonly pageSize = 10;
  sortBy: ComputerSearch['sortBy'] = 'accountingDate';
  direction: ComputerSearch['direction'] = 'desc';
  isLoading = false;
  errorMessage = '';
  hasSearched = false;

  ngOnInit(): void {
    this.loadComputers();
  }

  applyFilters(): void {
    this.page = 0;
    this.loadComputers();
  }

  clearFilters(): void {
    this.filters.reset({ name: '', accountingDate: '' });
    this.page = 0;
    this.loadComputers();
  }

  sort(field: ComputerSearch['sortBy']): void {
    if (this.sortBy === field) {
      this.direction = this.direction === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortBy = field;
      this.direction = 'asc';
    }
    this.page = 0;
    this.loadComputers();
  }

  changePage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.page = page;
      this.loadComputers();
    }
  }

  retry(): void {
    this.loadComputers();
  }

  get firstResult(): number {
    return this.totalElements === 0 ? 0 : this.page * this.pageSize + 1;
  }

  get lastResult(): number {
    return Math.min((this.page + 1) * this.pageSize, this.totalElements);
  }

  private loadComputers(): void {
    const filterValues = this.filters.getRawValue();
    const search: ComputerSearch = {
      ...(filterValues.name.trim() ? { name: filterValues.name.trim() } : {}),
      ...(filterValues.accountingDate ? { accountingDate: filterValues.accountingDate } : {}),
      sortBy: this.sortBy,
      direction: this.direction,
      page: this.page,
      size: this.pageSize
    };

    this.isLoading = true;
    this.errorMessage = '';
    this.conversionService.searchComputers(search).subscribe({
      next: (response: PageResponse<Computer>) => {
        this.computers = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.page = response.number;
        this.hasSearched = true;
        this.isLoading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.status === 0
          ? 'Nie udało się połączyć z serwerem. Sprawdź, czy backend jest uruchomiony.'
          : 'Nie udało się pobrać historii. Spróbuj ponownie.';
        this.isLoading = false;
      }
    });
  }
}
