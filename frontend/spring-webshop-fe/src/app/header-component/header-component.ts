import { Component, OnInit } from '@angular/core';
import { User } from '../user';

@Component({
  selector: 'app-header-component',
  templateUrl: './header-component.html',
  styleUrls: ['./header-component.scss']
})
export class HeaderComponent implements OnInit {
  webshopLogoUrl: string;
  user: User;

  constructor() { 
    this.webshopLogoUrl = "assets/img/webshop_logo.jpg";
    this.user = this.getUsersData();
  }

  ngOnInit(): void { }

  private getUsersData(): User {
    // TODO to be replaced by an API call
    const user: User = {
      name: "Random Name",
      email: "wuhuu@gmail.com",
      profilePictureUrl: "assets/img/profile_picture_default.PNG"
    };
    return user;
  }
}
