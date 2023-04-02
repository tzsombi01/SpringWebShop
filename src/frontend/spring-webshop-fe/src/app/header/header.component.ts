import { Component, OnInit } from '@angular/core';
import { IUser } from '../interfaces/user';
import { UserService } from "../services/user.service";
import { User } from '../classes/user';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  readonly webshopLogoUrl: string;
  user: IUser;

  constructor(private userService: UserService) {
    this.webshopLogoUrl = "assets/img/webshop_logo.jpg";
    this.user = { name: "some", email: "yeey" };
  }

  ngOnInit(): void {
    this.getUsersData();
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
}
