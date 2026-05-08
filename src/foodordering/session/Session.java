package foodordering.session;

import foodordering.model.User;

public class Session{

    private static User currentUser;
    private Session(){

    }

    public static void login(User u){
        currentUser = u;
    }
    public static User getUser(){
        return currentUser;
    }
    public static boolean isLoggedIn(){
        return currentUser !=null;
    }
    public static boolean isAdmin(){
        return isLoggedIn() && currentUser.isAdmin(); 
    }
    public static void logout(){
        currentUser = null;
    }
}
