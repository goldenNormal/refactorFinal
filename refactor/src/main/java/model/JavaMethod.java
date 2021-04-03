package model;

public class JavaMethod  extends Base{
    Integer id;
    Integer classId;
    String name;
    boolean coverParent;
    boolean virtual;
    boolean Static;
    String accessLevel;

    public boolean isCoverParent() {
        return coverParent;
    }

    @Override
    public String toString() {
        return "JavaMethod{" +
                "id=" + id +
                ", classId=" + classId +
                ", name='" + name + '\'' +
                ", coverParent=" + coverParent +
                ", virtual=" + virtual +
                ", Static=" + Static +
                ", accessLevel='" + accessLevel + '\'' +
                '}';
    }

    public void setCoverParent(boolean coverParent) {
        this.coverParent = coverParent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    public boolean isStatic() {
        return Static;
    }

    public void setStatic(boolean aStatic) {
        Static = aStatic;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }
}
