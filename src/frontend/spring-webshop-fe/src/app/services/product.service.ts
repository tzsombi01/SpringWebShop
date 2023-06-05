import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductWrapper } from '../interfaces/productResponse';
import { Product } from '../interfaces/product';
import { environment } from 'src/environments/environment';
import { UserService } from './user.service';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private readonly BASE_URL: string = environment.BASE_URL;
  private readonly urlToAllProductsEndp: string;
  private readonly urlToBuyProductEndp: string;

  products: Product[];
  productsChanged: EventEmitter<Product[]> = new EventEmitter<Product[]>();

  constructor(private http: HttpClient, private userService: UserService, private authService: AuthService) { 
    this.urlToAllProductsEndp = "/api/v1/products";
    this.urlToBuyProductEndp = "/api/v1/products/buy";
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
      ? `${ this.BASE_URL + this.urlToAllProductsEndp }?${ queryParams.join("&") }`
      : this.BASE_URL + this.urlToAllProductsEndp
    );

    return products;
  }

  public buyProduct(product: Product): boolean {
    if (! this.userService.isUserLoggedIn()) {
      alert("You must be logged in to buy a product!");
      return false;
    }

    let authHeader = new HttpHeaders({ Authorization: "Bearer " + this.authService.getToken() });
    const requestHeaders = { headers: authHeader };
    let buy: Observable<null> = this.http.post<null>(
      `${ this.BASE_URL + this.urlToBuyProductEndp }/${ product.id }?userId=${ this.userService.getUserId() }`,
      null,
      requestHeaders
    );

    buy.subscribe(
      () => alert("Successful Purchase"),
      (error: any) => console.error(error),
      () => console.log("Successful Purchase")
    );

    return true;
  }
  
}
