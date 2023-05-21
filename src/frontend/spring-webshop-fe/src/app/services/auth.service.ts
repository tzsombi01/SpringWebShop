import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TokenResponse } from '../interfaces/tokenResponse';
import { Observable } from 'rxjs/internal/Observable';
import { UserService } from './user.service';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  private readonly BASE_URL: string | undefined = environment.BASE_URL;
  private readonly urlToAuthenticationEndpoint: string;
  private readonly urlToRegisterEndpoint: string;

  private token!: string;

  constructor(private http: HttpClient, private userService: UserService) { 
    this.urlToAuthenticationEndpoint = "/api/v1/auth/authenticate";
    this.urlToRegisterEndpoint = "/api/v1/auth/register";
  }

  public login(userName: string, password: string): void {
    const body = { email: userName, password: password }
    this.authenticate(body).subscribe(
      (response: TokenResponse) => {
        this.token = response.token;
        this.userService.me(this.token);
      },
      (error: any) => {
        alert("Email and / or password are incorrect");
        console.log(error);
      },
      () => console.log("User Logged In"));
  }

  private authenticate(body: Object): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(this.BASE_URL + this.urlToAuthenticationEndpoint, body);
  }

  public getToken(): string | undefined {
    return this.token;
  }
}
