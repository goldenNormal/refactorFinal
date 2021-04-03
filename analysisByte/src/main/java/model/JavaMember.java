package model;

public class JavaMember extends Base {
    Integer id;
    String name;
    Integer class_id; //所在类的类id
    String accessLevel;
    Integer type_id;//自己本身的类id

    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

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
                ", type_id=" + type_id +
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
