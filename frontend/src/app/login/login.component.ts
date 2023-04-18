import { Component, OnDestroy } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { AuthService } from '../shared/auth.service';
import { Subscription, from } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnDestroy {
  firebaseError: string = ''
  loginSubscription?: Subscription
  loginSpinner: boolean = false


  /* You can define the control with just the initial value, but if your controls
   * need sync or async validation, add sync and async validators as the second and
   * third items in the array. */
  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  })

  constructor(
    private fb: FormBuilder,
    public authService: AuthService
  ) {}

  ngOnDestroy(): void {
    this.loginSubscription?.unsubscribe()
  }

  onSubmit(): void {
    this.loginSpinner = true

    this.loginSubscription = from(this.authService.signIn(
      this.loginForm.controls['email'].value!,
      this.loginForm.controls['password'].value!
    )).subscribe(
      result => {
        this.firebaseError = result ?? ''
        this.loginSpinner = false
      }
    )
  }
}
