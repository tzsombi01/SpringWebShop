import { Component, OnInit } from '@angular/core';
import { User } from '../interfaces/user';
import { UserserviceService } from '../userservice.service';

@Component({
  selector: 'app-header-component',
  templateUrl: './header-component.html',
  styleUrls: ['./header-component.scss']
})
export class HeaderComponent implements OnInit {
  webshopLogoUrl: string;
  user: User;

  constructor(private userService: UserserviceService) {
    this.webshopLogoUrl = "assets/img/webshop_logo.jpg";
    this.user = {name: "some", email: "yeey"};
  }

  ngOnInit(): void {
    this.getUsersData();
  }

  private getUsersData(): void {
    this.userService.getUserById().subscribe(
      (response: User) => {
        this.user = response;
      },  
      (error: any) => console.error(error),
      () => console.log('User retrieved')
    );
  }
}
