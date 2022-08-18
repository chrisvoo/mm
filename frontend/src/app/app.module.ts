import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NavbarComponent } from './navbar/navbar.component';
import { PlaylistComponent } from './playlist/playlist.component';
import { PlaylistEditComponent } from './playlist/playlist-edit/playlist-edit.component';
import { PlaylistItemComponent } from './playlist/playlist-item/playlist-item.component';
import { HomeComponent } from './home/home.component';
import { LikesComponent } from './likes/likes.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    PlaylistComponent,
    PlaylistEditComponent,
    PlaylistItemComponent,
    HomeComponent,
    LikesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
