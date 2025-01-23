import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {}

  login(loginDTO: any): Observable<any> {
    return this.http.post('http://localhost:8080/api/auth/login', loginDTO);
  }

  signup(signupDTO: any): Observable<any> {
    return this.http.post('http://localhost:8080/api/auth/signup', signupDTO);
  }
}
