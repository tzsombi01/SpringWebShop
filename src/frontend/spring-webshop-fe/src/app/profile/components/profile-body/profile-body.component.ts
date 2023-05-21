import { Component } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-profile-body',
  templateUrl: './profile-body.component.html',
  styleUrls: ['./profile-body.component.scss']
})
export class ProfileBodyComponent {

  protected displayCategory: string = "sellingProducts";

  constructor(protected userService: UserService) { }

  public setDisplayCategory(displayCategory: string): void {
    this.displayCategory = displayCategory;
  }
}
