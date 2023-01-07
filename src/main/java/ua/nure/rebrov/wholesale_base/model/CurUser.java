package ua.nure.rebrov.wholesale_base.model;

public class CurUser {
    private static User user;

    public static User login(User u){
        if(user==null){
            user = u;
        }
        return user;
    }

    public static User get(){
        return user;
    }

    public static void logout(){
        user=null;
    }
}
