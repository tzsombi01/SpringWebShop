import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IUser } from '../interfaces/user';
import { User } from '../classes/user';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly BASE_URL: string = environment.BASE_URL;
  private readonly urlToDemoEndpoint: string;
  private readonly urlToMeEndpoint: string;

  user!: IUser;
  userChanged: EventEmitter<IUser> = new EventEmitter<IUser>();

  constructor(private http: HttpClient) { 
    this.urlToDemoEndpoint = "/api/v1/demo";
    this.urlToMeEndpoint = "/api/v1/users/me";
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
    return this.http.get<IUser>(this.BASE_URL + this.urlToMeEndpoint, requestHeaders);
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
    return this.http.get<IUser>(this.BASE_URL + this.urlToDemoEndpoint);
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
