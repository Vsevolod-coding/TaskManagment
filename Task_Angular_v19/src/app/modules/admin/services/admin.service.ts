import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StorageService } from '../../../auth/services/storage/storage.service';
import { catchError, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http:HttpClient) { }

  getUsers(): Observable<any> {
    return this.http.get('http://127.0.0.1:8080/api/admin/users', {
      headers: this.createAuthorizationHeader()
    });
  }

  postTask(taskDto:any): Observable<any> {
    return this.http.post('http://127.0.0.1:8080/api/admin/task', taskDto, {
      headers: this.createAuthorizationHeader()
    });
  }

  getTasks(): Observable<any> {
    return this.http.get('http://127.0.0.1:8080/api/admin/tasks', {
      headers: this.createAuthorizationHeader()
    });
  }

  private createAuthorizationHeader():HttpHeaders{
    console.log('Authorization Token:', StorageService.getToken());
    return new HttpHeaders().set(
      'Authorization', 'Bearer ' + StorageService.getToken()
    );
  }
}
