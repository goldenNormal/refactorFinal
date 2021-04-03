package com.golden;

/**
 * @author HuangYihong
 * @date 2021-03-25 22:16
 */

public class Statistics{
    String projectName;
    int classCnt;
    int methodCnt;
    int memberCnt;
    int invokeCnt;
    Double oriScore;
    Double refactScore;
    Double disturbanceRate;
    Double stability;
    String execTime;
    Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getClassCnt() {
        return classCnt;
    }

    public void setClassCnt(int classCnt) {
        this.classCnt = classCnt;
    }

    public int getMethodCnt() {
        return methodCnt;
    }

    public void setMethodCnt(int methodCnt) {
        this.methodCnt = methodCnt;
    }

    public int getMemberCnt() {
        return memberCnt;
    }

    public void setMemberCnt(int memberCnt) {
        this.memberCnt = memberCnt;
    }

    public int getInvokeCnt() {
        return invokeCnt;
    }

    public void setInvokeCnt(int invokeCnt) {
        this.invokeCnt = invokeCnt;
    }

    public Double getOriScore() {
        return oriScore;
    }

    public void setOriScore(Double oriScore) {
        this.oriScore = oriScore;
    }

    public Double getRefactScore() {
        return refactScore;
    }

    public void setRefactScore(Double refactScore) {
        this.refactScore = refactScore;
    }

    public Double getDisturbanceRate() {
        return disturbanceRate;
    }

    public void setDisturbanceRate(Double disturbanceRate) {
        this.disturbanceRate = disturbanceRate;
    }

    public Double getStability() {
        return stability;
    }

    public void setStability(Double stability) {
        this.stability = stability;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "projectName='" + projectName + '\'' +
                ", classCnt=" + classCnt +
                ", methodCnt=" + methodCnt +
                ", memberCnt=" + memberCnt +
                ", invokeCnt=" + invokeCnt +
                ", oriScore=" + oriScore +
                ", refactScore=" + refactScore +
                ", disturbanceRate=" + disturbanceRate +
                ", stability=" + stability +
                ", execTime='" + execTime + '\'' +
                ", id=" + id +
                '}';
    }

    public String getExecTime() {
        return execTime;
    }

    public void setExecTime(String execTime) {
        this.execTime = execTime;
    }
}
