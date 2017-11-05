package successgraduate.appointment;

/**
 * Created by Lee jh on 2017-05-14.
 */

public class User {

    private String userID;
    private boolean selected;

    public User(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }



}