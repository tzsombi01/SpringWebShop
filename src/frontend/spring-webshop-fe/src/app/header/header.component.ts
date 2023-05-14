import {  
  AfterViewInit,
  Component, 
  OnInit, 
  ViewChild
} from '@angular/core';
import { IUser } from '../interfaces/user';
import { UserService } from "../services/user.service";
import { User } from '../classes/user';
import { ProductService } from '../services/product.service';
import { ProductWrapper } from '../interfaces/productResponse';
import { MatPaginator } from '@angular/material/paginator';
import { tap } from 'rxjs/internal/operators/tap';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, AfterViewInit {
  readonly webshopLogoUrl: string;
  user: IUser;
  
  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  constructor(private userService: UserService, private productService: ProductService) {
    this.webshopLogoUrl = "assets/img/webshop_logo.jpg";
    this.user = { name: "some", email: "yeey", profilePictureUrl: "assets/img/profile_picture_default.PNG" };
  }

  ngOnInit(): void {
    this.getUsersData();
  }

  ngAfterViewInit(): void {
      this.paginator.page
        .pipe(
          tap(() => this.onSearch())
        )
        .subscribe();
  }

  public getProductLength(): number {
      return this.productService.getProducts().length;
  }

  private getUsersData(): void {
    this.userService.getUserById().subscribe(
      (response: IUser) => {
        this.user = new User(
          response.name,
          response.email
        );
      },
      (error: any) => console.error(error),
      () => console.log('User retrieved')
    );
  }

  public onSearch() {
    this.productService.getAllProducts(
        this.paginator?.pageIndex ?? 0, 
        this.paginator?.pageSize ?? 2).subscribe(
      (response: ProductWrapper) => {
        console.log(response.content);
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
