import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { provideRouter, Routes } from '@angular/router';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { PostTaskComponent } from './components/post-task/post-task.component';
import { UpdateTaskComponent } from './components/update-task/update-task.component';
import { ViewTaskDetailsComponent } from './components/view-task-details/view-task-details.component';
import { provideHttpClient } from '@angular/common/http';

const routes: Routes = [
  {path: "dashboard", component: AdminDashboardComponent},
  {path: "task/post", component: PostTaskComponent},
  {path: "task/:id/edit", component: UpdateTaskComponent},
  {path: "task/:id/view", component: ViewTaskDetailsComponent}
]

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ],
  providers: [provideRouter(routes), provideHttpClient()]
})
export class AdminModule { }
