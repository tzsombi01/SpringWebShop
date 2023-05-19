import { Component } from '@angular/core';
import { UserService } from '../services/user.service';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  userNameControl: FormControl = new FormControl("");
  passwordControl: FormControl = new FormControl("");
  hide: boolean;

  constructor(protected authService: AuthService, private router: Router) {
    this.hide = true;
  }

  public login(): void {
    this.authService.login(this.userNameControl.value, this.passwordControl.value);
    
    this.router.navigate(["/home"]);
  }
}
