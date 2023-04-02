import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductWrapper } from '../interfaces/productResponse';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private readonly urlToAllProductsEndp: string;

  constructor(private http: HttpClient) { 
    this.urlToAllProductsEndp = "http://localhost:8080/api/v1/products";
  }

  public getAllProducts(): Observable<ProductWrapper> {
    return this.http.get<ProductWrapper>(this.urlToAllProductsEndp);
  }
}
