package model;

public class InvokeRelation extends Base {
    Integer id;
    Integer caller_method_id;
    Integer callee_id_method;
    Integer callee_id_member;
    Integer type;

    public InvokeRelation(){
        setParam(false);
        setLocal(false);
        setReturn(false);
        setMember(false);
        setCallee_id_member(null);
        setCallee_id_method(null);
    }


    @Override
    public String toString() {
        return "InvokeRelation{" +
                "" + id +
                ", " + caller_method_id +
                ", " + callee_id_method +
                ", " + callee_id_member +
                ", " + type +
                ", Return:" + Return +
                ", param:" + param +
                ",member: " + member +
                ", local：" + local +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCaller_method_id() {
        return caller_method_id;
    }

    public void setCaller_method_id(Integer caller_method_id) {
        this.caller_method_id = caller_method_id;
    }

    public Integer getCallee_id_method() {
        return callee_id_method;
    }

    public void setCallee_id_method(Integer callee_id_method) {
        this.callee_id_method = callee_id_method;
    }

    public Integer getCallee_id_member() {
        return callee_id_member;
    }

    public void setCallee_id_member(Integer callee_id_member) {
        this.callee_id_member = callee_id_member;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isReturn() {
        return Return;
    }

    public void setReturn(boolean aReturn) {
        Return = aReturn;
    }

    public boolean isParam() {
        return param;
    }

    public void setParam(boolean param) {
        this.param = param;
    }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    boolean Return;
    boolean param;
    boolean member;
    boolean local;
}
