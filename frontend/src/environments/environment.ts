import { FirebaseOptions } from "firebase/app";

export interface EnvOptions {
  production: boolean,
  firebase: FirebaseOptions
}

export const environment: EnvOptions = {
  production: false,
  firebase: {
    apiKey: "AIzaSyBWJd0IqeT3n4P4aTlr6oyy7l52i7JU290",
    authDomain: "music-manager-4404e.firebaseapp.com",
    projectId: "music-manager-4404e",
    storageBucket: "music-manager-4404e.appspot.com",
    messagingSenderId: "371197725405",
    appId: "1:371197725405:web:349a2744dbbd8aee53e934",
    measurementId: "G-X0SCGMQPYQ"
  }
};
