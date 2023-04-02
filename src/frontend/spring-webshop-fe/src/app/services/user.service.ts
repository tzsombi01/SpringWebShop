import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IUser } from '../interfaces/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly urlToDemoEndpoint: string;

  constructor(private http: HttpClient) { 
    this.urlToDemoEndpoint = "http://localhost:8080/api/v1/demo";
  }

  public getUserById(): Observable<IUser> {
    return this.http.get<IUser>(this.urlToDemoEndpoint);
  }
}
