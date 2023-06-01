import { Component } from '@angular/core';
import { CreditCardResponse } from 'src/app/interfaces/creditCardResponse';
import { Product } from 'src/app/interfaces/product';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-profile-body',
  templateUrl: './profile-body.component.html',
  styleUrls: ['./profile-body.component.scss']
})
export class ProfileBodyComponent {

  protected displayCategory: string = "sellingProducts";

  constructor(private userService: UserService) { }

  public setDisplayCategory(displayCategory: string): void {
    this.displayCategory = displayCategory;
  }

  public getCards(): CreditCardResponse[] {
    return this.userService.user?.creditCards ?? [];
  }

  public getProducts(): Product[] {
    return this.userService.user?.sellingProducts;
  }

  public productsAreEmpty(): boolean {
    return (this.userService.user?.sellingProducts?.length ?? 0) === 0 ;
  }

  public creditCardsAreEmpty(): boolean {
    return (this.userService.user?.creditCards?.length ?? 0) === 0 ;
  }
}
