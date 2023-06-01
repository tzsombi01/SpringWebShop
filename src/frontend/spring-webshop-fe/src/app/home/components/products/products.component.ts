import { Component, OnInit } from '@angular/core';
import { Product } from '../../../interfaces/product';
import { ProductWrapper } from '../../../interfaces/productResponse';
import { ProductService } from '../../../services/product.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.getAllProducts();
  }

  public getProducts(): Product[] {
    return this.productService.getProducts();
  }

  public isProductsEmpty(): boolean {
    return this.productService.getProducts().length === 0;
  }

  private getAllProducts(): void {
    this.productService.getAllProducts().subscribe(
      (response: ProductWrapper) => {
        this.productService.products = response.content;
        this.productService.productsChanged.emit(response.content);
      },
      (error: any) => console.error(error),
      () => console.log("Products retrieved")
    );
  }
}

