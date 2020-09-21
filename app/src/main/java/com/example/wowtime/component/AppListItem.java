package com.example.wowtime.component;

public class AppListItem {
    private String name;
<<<<<<< HEAD:app/src/main/java/com/example/wowtime/entity/AppListItem.java

//    private String allowed;

=======
    //    private String allowed;
>>>>>>> o-ligei-develop:app/src/main/java/com/example/wowtime/component/AppListItem.java
    private int icon;

    public AppListItem(){
    }

    public AppListItem(String name,int icon){
        this.name = name;
//        this.allowed = allowed;
        this.icon = icon;
    }

    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

<<<<<<< HEAD:app/src/main/java/com/example/wowtime/entity/AppListItem.java

=======
>>>>>>> o-ligei-develop:app/src/main/java/com/example/wowtime/component/AppListItem.java
//    public String getAllowed(){return this.allowed;}
//    public void setAllowed(String allowed){this.allowed = allowed;}

    public int getIcon(){return icon;}
    public void setIcon(int icon){this.icon = icon;}
}
