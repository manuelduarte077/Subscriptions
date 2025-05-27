// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getFirestore } from "firebase/firestore";
import { getAnalytics } from "firebase/analytics";

// Your web app's Firebase configuration
// Para un proyecto real, estas credenciales deberían estar en variables de entorno
const firebaseConfig = {
  apiKey: "AIzaSyDenZSebioXr-nRHNpWc8Q1NWcChL4wXlc",
  authDomain: "neveforgett.firebaseapp.com",
  projectId: "neveforgett",
  storageBucket: "neveforgett.firebasestorage.app",
  messagingSenderId: "1072962980273",
  appId: "1:1072962980273:web:ea225fe09d15285ae81590",
  measurementId: "G-T27D2882K6"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

// Initialize Firebase services
export const auth = getAuth(app);
export const db = getFirestore(app);
export const analytics = getAnalytics(app);

export default app;
