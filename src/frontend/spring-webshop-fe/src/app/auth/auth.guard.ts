import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { UserService } from '../services/user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {

  constructor(private router: Router, private userService: UserService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | 
  Promise<boolean | UrlTree> | boolean | UrlTree {
    
    if (this.userService.isUserLoggedIn()) {
      return true;
    }

    this.router.navigate(['/home']);
    return false;
  }
  
}
