// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app"
import { initializeApp as initAdmin } from "firebase-admin/app"
import { getAnalytics } from "firebase/analytics"
import { getAuth } from "firebase/auth"
import '../server/environment/loadEnvVars'

// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: process.env.FIREBASE_API_KEY,
  authDomain: process.env.FIREBASE_AUTH_DOMAIN,
  projectId: process.env.FIREBASE_PROJECT_ID,
  storageBucket: process.env.FIREBASE_STORAGE_BUCKET,
  messagingSenderId: process.env.FIREBASE_MESSAGING_SENDER_ID,
  appId: process.env.FIREBASE_APP_ID,
  measurementId: process.env.FIREBASE_MEASUREMENET_ID
}

// Initialize Firebase
const app = initializeApp(firebaseConfig)

initAdmin()

// Initialize Firebase Authentication and get a reference to the service
const auth = getAuth(app)
auth.useDeviceLanguage()

export { auth, getAnalytics }