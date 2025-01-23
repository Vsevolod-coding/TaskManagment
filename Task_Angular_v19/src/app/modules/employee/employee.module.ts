import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { provideRouter, Routes } from '@angular/router';
import { EmployeeDashboardComponent } from './components/employee-dashboard/employee-dashboard.component';
import { ViewTaskDetailsComponent } from './components/view-task-details/view-task-details.component';
import { provideHttpClient } from '@angular/common/http';

const routes: Routes = [
  {path: "dashboard", component: EmployeeDashboardComponent},
  {path: "task/:id/view", component: ViewTaskDetailsComponent}
]

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ],
  providers: [provideRouter(routes), provideHttpClient()]
})
export class EmployeeModule { }
