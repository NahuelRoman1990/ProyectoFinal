package com.example.proyectofinal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinal.providers.AuthProvider;

public class AuthViewModel extends ViewModel {

    private AuthProvider authProvider;

    public AuthViewModel() {
        authProvider = new AuthProvider();
    }

    public LiveData<Boolean> logOut() {
        return authProvider.logout();
    }

}