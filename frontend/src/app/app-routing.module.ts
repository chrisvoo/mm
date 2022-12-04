import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LikesComponent } from './likes/likes.component';
import { LoginComponent } from './login/login.component';
import { PlaylistEditComponent } from './playlist/playlist-edit/playlist-edit.component';
import { PlaylistItemComponent } from './playlist/playlist-item/playlist-item.component';
import { PlaylistComponent } from './playlist/playlist.component';

const routes: Routes = [
  { path: '', component: HomeComponent, pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'playlist', component: PlaylistComponent, children: [
    { path: '', component: PlaylistComponent },
    { path: 'new', component: PlaylistEditComponent}, // before :id, otherwise it will parse new as a number
    { path: ':id', component: PlaylistItemComponent },
    { path: ':id/edit', component: PlaylistEditComponent },
  ] },
  { path: 'likes', component: LikesComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
