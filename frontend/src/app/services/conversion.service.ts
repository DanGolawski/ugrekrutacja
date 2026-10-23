import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Computer, ComputerSearch, ConversionRequest, PageResponse } from '../models/conversion.models';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ConversionService {
  private readonly http = inject(HttpClient);

  convertAndSave(computers: ConversionRequest[]): Observable<Computer[]> {
    return this.http.post<Computer[]>(`${environment.apiUrl}/conversions`, { computers });
  }

  searchComputers(search: ComputerSearch): Observable<PageResponse<Computer>> {
    return this.http.get<PageResponse<Computer>>(`${environment.apiUrl}/computers`, {
      params: {
        ...(search.name ? { name: search.name } : {}),
        ...(search.accountingDate ? { accountingDate: search.accountingDate } : {}),
        sortBy: search.sortBy,
        direction: search.direction,
        page: search.page,
        size: search.size
      }
    });
  }

  exportComputersXml(): Observable<Blob> {
    return this.http.get(`${environment.apiUrl}/computers/export/xml`, {
      responseType: 'blob'
    });
  }
}
