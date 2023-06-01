import { Component } from '@angular/core';
import { NgForm, NgModel } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  
  hide: boolean = true;

  constructor(private authService: AuthService, private router: Router) { }

  async submit(register: NgForm, firstName: NgModel, lastName: NgModel, email: NgModel, password: NgModel) {
    if (! register.valid) {
      alert("Invalid fields");
      return;
    }

    this.authService.register(firstName.value, lastName.value, email.value, password.value);
    
    await this.router.navigate(["/home"]);
  }

  public async back() {
    await this.router.navigate(["/.."]);
  }
}
