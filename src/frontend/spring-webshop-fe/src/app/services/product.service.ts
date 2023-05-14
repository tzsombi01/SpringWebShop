import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductWrapper } from '../interfaces/productResponse';
import { Product } from '../interfaces/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private readonly urlToAllProductsEndp: string;
  products: Product[];
  productsChanged: EventEmitter<Product[]> = new EventEmitter<Product[]>();

  constructor(private http: HttpClient) { 
    this.urlToAllProductsEndp = "http://localhost:8080/api/v1/products";
    this.products = [];
  }

  public getProducts(): Product[] {
    return this.products;
  }

  public getAllProducts(page?: number, pageSize?: number, order?: boolean, sortBy?: string): Observable<ProductWrapper> {
    let queryParams: string[] = [];
    if (page != null) queryParams.push("page=" + page); 
    if (pageSize != null) queryParams.push("pageSize=" + pageSize); 
    if (order != null) queryParams.push("order=" + order); 
    if (sortBy != null) queryParams.push("sortBy=" + sortBy);
    
    let products: Observable<ProductWrapper> = this.http.get<ProductWrapper>(
      queryParams.length > 0 
      ? `${ this.urlToAllProductsEndp }?${ queryParams.join("&") }`
      : this.urlToAllProductsEndp
    );

    return products;
  }
}
