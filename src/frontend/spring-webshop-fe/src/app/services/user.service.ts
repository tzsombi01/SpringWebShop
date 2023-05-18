import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IUser } from '../interfaces/user';
import { User } from '../classes/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  user: IUser;
  userChanged: EventEmitter<IUser> = new EventEmitter<IUser>();
  private readonly urlToDemoEndpoint: string;

  constructor(private http: HttpClient) { 
    this.urlToDemoEndpoint = "http://localhost:8080/api/v1/demo";
    this.user = { 
      name: "some", 
      email: "yeey", 
      profilePictureUrl: "assets/img/profile_picture_default.PNG"
    };
  }

  public getUsersData(): void {
    this.getUserById().subscribe(
      (response: IUser) => {
        this.user = new User(
          response.name,
          response.email
        );
        this.userChanged.emit(this.user);
      },
      (error: any) => console.error(error),
      () => console.log('User retrieved')
    );
  }

  private getUserById(): Observable<IUser> {
    return this.http.get<IUser>(this.urlToDemoEndpoint);
  }

  public getUserName(): string {
    return this.user.name;
  }

  public getUserEmail(): string {
    return this.user.email;
  }

  public getUserProfilePictureUrl(): string {
    return this.user.profilePictureUrl!;
  }
}
