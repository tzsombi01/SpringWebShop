import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IUser } from '../interfaces/user';
import { User } from '../classes/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  user!: IUser;
  userChanged: EventEmitter<IUser> = new EventEmitter<IUser>();
  private readonly urlToDemoEndpoint: string;
  private readonly urlToMeEndpoint: string;

  constructor(private http: HttpClient) { 
    this.urlToDemoEndpoint = "http://localhost:8080/api/v1/demo";
    this.urlToMeEndpoint = "http://localhost:8080/api/v1/users/me";
  }

  public me(token: string) {
    this.getMeUserData(token).subscribe(
      (response: IUser) => {
        this.user = new User(
          response.id,
          response.firstName,
          response.lastName,
          response.email,
          response.sellingProducts,
          response.creditCards,
          response.profilePictureUrl
        );
        this.userChanged.emit(this.user);
      },
      (error: any) => console.error(error),
      () => console.log('User retrieved')
    );
  }

  private getMeUserData(token: string): Observable<IUser> {
    let customHeaders = new HttpHeaders({ Authorization: "Bearer " + token});
    const requestHeaders = { headers: customHeaders };
    return this.http.get<IUser>(this.urlToMeEndpoint, requestHeaders);
  }

  public isUserLoggedIn(): boolean {
    return this.user !== undefined;
  }

  public getUsersData(): void {
    this.getUser().subscribe(
      (response: IUser) => {
        this.user = new User(
          response.id,
          response.firstName,
          response.lastName,
          response.email,
          response.sellingProducts,
          response.creditCards,
          response.profilePictureUrl
        );
        this.userChanged.emit(this.user);
      },
      (error: any) => console.error(error),
      () => console.log('User retrieved')
    );
  }

  private getUser(): Observable<IUser> {
    return this.http.get<IUser>(this.urlToDemoEndpoint);
  }

  public getUserName(): string {
    return this.user.firstName + " " + this.user.lastName;
  }

  public getUserEmail(): string {
    return this.user.email;
  }

  public getUserProfilePictureUrl(): string {
    return this.user.profilePictureUrl!;
  }
}
