import {
  AfterViewInit,
  Component,
  OnDestroy,
  ViewChild
} from '@angular/core';
import { UserService } from "../../../services/user.service";
import { ProductService } from '../../../services/product.service';
import { ProductWrapper } from '../../../interfaces/productResponse';
import { MatPaginator } from '@angular/material/paginator';
import { tap } from 'rxjs/internal/operators/tap';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements AfterViewInit, OnDestroy {
  readonly webshopLogoUrl: string;

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;
  paginatorSubscription!: Subscription;

  constructor(protected userService: UserService, private productService: ProductService) {
    this.webshopLogoUrl = "assets/img/webshop_logo.jpg";
  }

  ngAfterViewInit(): void {
    this.paginatorSubscription = this.paginator.page
      .pipe(
        tap(() => this.onSearch())
      )
      .subscribe();
  }

  ngOnDestroy(): void {
    this.paginatorSubscription.unsubscribe();
  }

  public getProductLength(): number {
    return this.productService.getProducts().length;
  }

  public onSearch() {
    this.productService.getAllProducts(
        this.paginator?.pageIndex ?? 0,
        this.paginator?.pageSize ?? 10).subscribe(
      (response: ProductWrapper) => {
        this.productService.products = response.content;
        this.productService.productsChanged.emit(response.content);
      },
      (error: any) => {
        console.error(error);
      },
      () => console.log('Queried for products')
    );
  }
}
