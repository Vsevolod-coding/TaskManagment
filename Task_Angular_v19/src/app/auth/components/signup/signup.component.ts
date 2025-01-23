import { Component, Inject } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';



@Component({
  selector: 'app-signup',
  imports: [SharedModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent {

  signupForm!:FormGroup;
  
  hidePassword:boolean = true;

  constructor(
    private fb: FormBuilder,
    private service: AuthService,
    private snackbar: MatSnackBar,
    private router: Router
  ) {}
  
  ngOnInit(){
    this.signupForm = this.fb.group({
      name:[null,[Validators.required]],
      email:[null,[Validators.required, Validators.email]],
      password:[null,[Validators.required]],
      confirmPassword:[null,[Validators.required]]
    })
  }

  togglePasswordVisibility(){
    this.hidePassword = !this.hidePassword
  }

  signUp() {
    const password = this.signupForm.get("password")?.value;
    const confirmPassword = this.signupForm.get("confirmPassword")?.value;

    if (password !== confirmPassword) {
      this.snackbar.open("Пароли не совпадают!", "Закрыть", { duration: 5000, panelClass: 'error-snackbar' });
      return;
    }

    console.log(this.signupForm.value);
    this.service.signup(this.signupForm.value).subscribe((res) => {
      console.log(res);
      if (res.id != null) {
        this.snackbar.open("Вы успешно зарегистрировались!", "Закрыть", { duration: 5000 });
        this.router.navigateByUrl("/login");
      } else {
        this.snackbar.open("Не получилось зарегистрироваться!", "Закрыть", { duration: 5000, panelClass:'error-snackbar' });
      }
    });
  }
  
}
