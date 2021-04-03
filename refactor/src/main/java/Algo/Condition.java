package Algo;

public class Condition {
    //假设等待重构的方法 与 B有很大的联系
    //以下是一些待考虑的因素

    //是否只与B的静态方法有联系
    private boolean onlyRelateWithStatic;

    //B的对象b，是否是本方法的参数
    private boolean isParamOfFunc;

    //B的对象b，是否是本方法中的临时局部变量
    private boolean isLocalInFunc;

    //B的对象b，是否是本方法所在类的成员变量
    private boolean isClassMember;

    //本方法是否带有面向对象属性
    private boolean hasOOproperty;

    //B的对象b，是否可能被本方法返回
    private boolean isLikelyReturn;

    Condition(){
        onlyRelateWithStatic=true;
        isParamOfFunc=false;
        isLocalInFunc=false;
        isClassMember=false;
        hasOOproperty=false;
        isLikelyReturn=false;
    }

    public void setOnlyRelateWithStatic(boolean onlyRelateWithStatic) {
        this.onlyRelateWithStatic = onlyRelateWithStatic;
    }

    public void setParamOfFunc(boolean paramOfFunc) {
        isParamOfFunc = paramOfFunc;
    }

    public void setLocalInFunc(boolean localInFunc) {
        isLocalInFunc = localInFunc;
    }

    public void setClassMember(boolean classMember) {
        isClassMember = classMember;
    }

    public void setHasOOproperty(boolean hasOOproperty) {
        this.hasOOproperty = hasOOproperty;
    }

    public void setLikelyReturn(boolean likelyReturn) {
        isLikelyReturn = likelyReturn;
    }

    public boolean isOnlyRelateWithStatic() {
        return onlyRelateWithStatic;
    }



    public boolean isParamOfFunc() {
        return isParamOfFunc;
    }


    public boolean isLocalInFunc() {
        return isLocalInFunc;
    }


    public boolean isClassMember() {
        return isClassMember;
    }


    public boolean isHasOOproperty() {
        return hasOOproperty;
    }


    public boolean isLikelyReturn() {
        return isLikelyReturn;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "onlyRelateWithStatic=" + onlyRelateWithStatic +
                ", isParamOfFunc=" + isParamOfFunc +
                ", isLocalInFunc=" + isLocalInFunc +
                ", isClassMember=" + isClassMember +
                ", hasOOproperty=" + hasOOproperty +
                ", isLikelyReturn=" + isLikelyReturn +
                '}';
    }
}
