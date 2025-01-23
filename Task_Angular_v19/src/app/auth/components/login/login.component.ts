import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SharedModule } from '../../../shared/shared.module';
import { AuthService } from '../../services/auth/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, of } from 'rxjs';
import { StorageService } from '../../services/storage/storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [SharedModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

    loginForm!:FormGroup;
    
    hidePassword:boolean = true;
  
    constructor(
      private fb:FormBuilder,
      private service: AuthService,
      private snackbar: MatSnackBar,
      private router: Router
    ){}
    
    ngOnInit(){
      this.loginForm = this.fb.group({
        email:[null,[Validators.required, Validators.email]],
        password:[null,[Validators.required]],
      })
    }
  
    togglePasswordVisibility(){
      this.hidePassword = !this.hidePassword
    }

    login() {
      console.log(this.loginForm.value);
      this.service.login(this.loginForm.value).pipe(
        catchError(() => {
          this.snackbar.open('Неверные учетные данные', 'Закрыть', {
            duration: 3000
          });
          return of(null); 
        })
      ).subscribe({
        next: (response) => {
          console.log(response);
          if (response && response.userId) {
            this.snackbar.open("Вы успешно вошли в аккаунт!", "Закрыть", {duration: 5000});
            const user = {
              id: response.userId,
              role: response.userRole
            };
            StorageService.saveUser(user);
            StorageService.saveToken(response.jwt);
            if(StorageService.isAdminLoggedIn()) {
              this.router.navigateByUrl("/admin/dashboard");
            } else if (StorageService.isEmployeeLoggedIn()) {
              this.router.navigateByUrl("/employee/dashboard");
            } else {
              this.snackbar.open("Не удалось определить роль пользователя.", "Закрыть", {
                duration: 3000
              });
            }
          }
        }
      });
    }
  }