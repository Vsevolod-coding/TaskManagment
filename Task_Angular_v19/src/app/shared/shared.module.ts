import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink, RouterOutlet } from '@angular/router';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatCardActions, MatCardContent, MatCardModule, MatCardTitle} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import { provideHttpClient } from '@angular/common/http';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatSelectModule} from '@angular/material/select';
import {MatNativeDateModule} from '@angular/material/core';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterLink, RouterOutlet,
    MatToolbarModule, MatNativeDateModule, MatSelectModule, MatDatepickerModule, MatButtonModule, MatCardModule, MatCardTitle, MatCardContent, MatFormFieldModule, MatIconModule, MatInputModule, MatCardActions, MatSnackBarModule
  ],
  exports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterLink, RouterOutlet,
    MatToolbarModule, MatNativeDateModule, MatSelectModule, MatDatepickerModule, MatButtonModule, MatCardModule, MatCardTitle, MatCardContent, MatFormFieldModule, MatIconModule, MatInputModule, MatCardActions, MatSnackBarModule
  ],
  providers: [provideHttpClient()]
})
export class SharedModule { }
