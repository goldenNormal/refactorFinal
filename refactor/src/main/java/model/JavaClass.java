package model;

import java.util.List;

public class JavaClass  extends Base{
    Integer id;

    public String getFullname() {
        return fullname;
    }

    @Override
    public String toString() {
        return "JavaClass{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", parent_id=" + parent_id +
                ", ListOfInterface='" + ListOfInterface + '\'' +
                '}';
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    String fullname;



    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getListOfInterface() {
        return ListOfInterface;
    }

    public void setListOfInterface(String listOfInterface) {
        ListOfInterface = listOfInterface;
    }

    Integer parent_id;
    String ListOfInterface;


}
