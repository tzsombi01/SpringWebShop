import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { AuthGuard } from './auth/auth.guard';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

const routes: Routes = [
  { path: "home", title: "Home", component: HomeComponent },
  { path: "profile", title: "Profile", component: ProfileComponent, canActivate: [AuthGuard] },
  { path: "login", title: "Login", component: LoginComponent },
  { path: "register", title: "Register", component: RegisterComponent },
  { path: "",  title: "Home", redirectTo: "home", pathMatch: "full" },
  { path: "**", redirectTo: "home" }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
