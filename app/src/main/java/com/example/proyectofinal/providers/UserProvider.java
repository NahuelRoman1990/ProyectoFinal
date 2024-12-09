package com.example.proyectofinal.providers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.proyectofinal.model.User;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.FindCallback;

import java.util.ArrayList;
import java.util.List;

public class UserProvider {

    public LiveData<String> createUser(User user) {
        MutableLiveData<String> result = new MutableLiveData<>();
        ParseObject userObject = new ParseObject("User");
        userObject.put("user_id", user.getId());
        userObject.put("email", user.getEmail());
        userObject.put("password", user.getPassword());

        userObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    result.setValue("Usuario creado correctamente");
                } else {
                    result.setValue("Error al crear usuario");
                }
            }
        });

        return result;
    }

    public LiveData<User> getUser(String email) {
        MutableLiveData<User> userData = new MutableLiveData<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("email", email);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> users, ParseException e) {
                if (e == null && users.size() > 0) {
                    ParseObject userObject = users.get(0);
                    User user = new User();
                    user.setId(userObject.getString("user_id"));
                    user.setEmail(userObject.getString("email"));
                    user.setPassword(userObject.getString("password"));

                    userData.setValue(user);
                } else {
                    userData.setValue(null);
                }
            }
        });

        return userData;
    }

    public LiveData<String> updateUserProfile(User user) {
        MutableLiveData<String> result = new MutableLiveData<>();

        // Crear una consulta para obtener el usuario por su ID
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("user_id", user.getId());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> users, ParseException e) {
                if (e == null && users.size() > 0) {
                    ParseObject userObject = users.get(0);
                    userObject.put("email", user.getEmail());
                    userObject.put("password", user.getPassword());
                    userObject.put("name", user.getName()); // Si tienes un campo de nombre
                    userObject.put("profile_picture", user.getProfilePictureUrl()); // Si tienes un campo para la foto

                    // Guardar el usuario actualizado en Parse
                    userObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                result.setValue("Perfil actualizado correctamente");
                            } else {
                                result.setValue("Error al actualizar perfil");
                            }
                        }
                    });
                } else {
                    result.setValue("Usuario no encontrado");
                }
            }
        });

        return result;
    }


    public LiveData<String> deleteUser(String userId) {
        MutableLiveData<String> result = new MutableLiveData<>();

        // Crear una consulta para obtener el usuario por su ID
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("user_id", userId);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> users, ParseException e) {
                if (e == null && users.size() > 0) {
                    ParseObject userObject = users.get(0);

                    // Eliminar el usuario de Parse
                    userObject.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                result.setValue("Usuario eliminado correctamente");
                            } else {
                                result.setValue("Error al eliminar usuario");
                            }
                        }
                    });
                } else {
                    result.setValue("Usuario no encontrado");
                }
            }
        });

        return result;
    }

    public LiveData<User> getCurrentUser(Context context) {
        MutableLiveData<User> currentUserData = new MutableLiveData<>();
        String currentUserId = getCurrentUserIdFromSession(context);  // Obtenemos el user_id desde la sesión

        if (currentUserId != null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
            query.whereEqualTo("user_id", currentUserId);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> users, ParseException e) {
                    if (e == null && users.size() > 0) {
                        ParseObject userObject = users.get(0);
                        User user = new User();
                        user.setId(userObject.getString("user_id"));
                        user.setEmail(userObject.getString("email"));
                        user.setPassword(userObject.getString("password"));
                        // Rellena los otros detalles del usuario si es necesario

                        currentUserData.setValue(user);  // Pasamos el usuario a LiveData
                    } else {
                        currentUserData.setValue(null);  // Usuario no encontrado
                    }
                }
            });
        } else {
            currentUserData.setValue(null);  // Si no hay sesión activa, devuelve null
        }

        return currentUserData;
    }

    public static void saveCurrentUser(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_id", user.getId());  // Guardamos el ID del usuario
        editor.putString("email", user.getEmail());  // Guardamos el correo electrónico, si es necesario
        // Puedes agregar más detalles si lo deseas, por ejemplo el nombre, foto, etc.
        editor.apply();  // Guardamos los cambios
    }

    public static String getCurrentUserIdFromSession(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        return preferences.getString("user_id", null);  // Devuelve null si no hay sesión activa
    }

    public LiveData<List<String>> getUserPosts(String userId) {
        MutableLiveData<List<String>> postUrls = new MutableLiveData<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.whereEqualTo("user_id", userId);

        query.findInBackground((posts, e) -> {
            if (e == null) {
                List<String> urls = new ArrayList<>();
                for (ParseObject post : posts) {
                    urls.add(post.getString("image_url"));
                }
                postUrls.setValue(urls);
            } else {
                postUrls.setValue(null);
            }
        });

        return postUrls;
    }

}
