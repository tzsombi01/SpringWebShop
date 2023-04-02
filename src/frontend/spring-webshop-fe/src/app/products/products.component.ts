import { Component, OnInit } from '@angular/core';
import { Product } from '../interfaces/product';
import { ProductWrapper } from '../interfaces/productResponse';
import { ProductService } from '../services/product.service';


@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {

  products: Product[];

  constructor(private productService: ProductService) {
    this.products = [];
  }

  ngOnInit(): void {
    this.getAllProducts();
  }

  private getAllProducts(): void {
    this.productService.getAllProducts().subscribe(
      (response: ProductWrapper) => {
        this.products = response.content;
      },
      (error: any) => console.error(error),
      () => console.log("Products retrieved")
    );
  }
}

