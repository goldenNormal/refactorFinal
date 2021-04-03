package model;

public class JavaMember extends Base {
    Integer id;
    String name;
    Integer class_id;
    String accessLevel;

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "JavaMember{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", class_id=" + class_id +
                ", accessLevel='" + accessLevel + '\'' +
                ", Static=" + Static +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getClass_id() {
        return class_id;
    }

    public void setClass_id(Integer class_id) {
        this.class_id = class_id;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public boolean isStatic() {
        return Static;
    }

    public void setStatic(boolean aStatic) {
        Static = aStatic;
    }

    boolean Static;
}
