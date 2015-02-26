package task.repository.model.model_show;

public class OperResShow {
    private String pkid;
    private String tid;
    private String operId;
    private String operPkid;
    private String operName;
    private String resPkid;
    private String resName;
    private String archivedFlag;
    private String createdBy;
    private String createdByName;
    private String createdTime;
    private String lastUpdBy;
    private String lastUpdByName;
    private String lastUpdTime;
    private String remark;
    private Integer recVersion;
    private Boolean isSel;
    public String getPkid() {
        return pkid;
    }
    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
    }
    public String getTid() {
        return tid;
    }
    public void setTid(String tid) {
        this.tid = tid == null ? null : tid.trim();
    }
    public String getOperId() {
        return operId;
    }
    public void setOperId(String operId) {
        this.operId = operId;
    }
    public String getOperPkid() {
        return operPkid;
    }
    public void setOperPkid(String operPkid) {
        this.operPkid = operPkid == null ? null : operPkid.trim();
    }

    public String getResPkid() {
        return resPkid;
    }

    public void setResPkid(String resPkid) {
        this.resPkid = resPkid;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getArchivedFlag() {
        return archivedFlag;
    }
    public void setArchivedFlag(String archivedFlag) {
        this.archivedFlag = archivedFlag == null ? null : archivedFlag.trim();
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }
    public String getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime == null ? null : createdTime.trim();
    }
    public String getLastUpdBy() {
        return lastUpdBy;
    }
    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy == null ? null : lastUpdBy.trim();
    }
    public String getLastUpdTime() {
        return lastUpdTime;
    }
    public void setLastUpdTime(String lastUpdTime) {
        this.lastUpdTime = lastUpdTime == null ? null : lastUpdTime.trim();
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
    public Integer getRecVersion() {
        return recVersion;
    }
    public void setRecVersion(Integer recVersion) {
        this.recVersion = recVersion;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getLastUpdByName() {
        return lastUpdByName;
    }

    public void setLastUpdByName(String lastUpdByName) {
        this.lastUpdByName = lastUpdByName;
    }

    public Boolean getIsSel() {
        return isSel;
    }

    public void setIsSel(Boolean isSel) {
        this.isSel = isSel;
    }
}