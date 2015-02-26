package task.repository.model.model_show;

public class WorkorderAppointShow {

    private String powerType;
    private String powerPkid;
    private String powerPkidName;
    private String periodNo;

    private String flowStatus;
    private String flowStatusName;
    private String flowStatusReason;
    private String flowStatusReasonName;
    private String createdBy;
    private String createdByName;
    private String createdTime;
    private String lastUpdBy;
    private String lastUpdByName;
    private String lastUpdTime;
    private String spareField;

    public String getPowerType() {
        return powerType;
    }

    public void setPowerType(String powerType) {
        this.powerType = powerType;
    }

    public String getPowerPkid() {
        return powerPkid;
    }

    public void setPowerPkid(String powerPkid) {
        this.powerPkid = powerPkid;
    }

    public String getPowerPkidName() {
        return powerPkidName;
    }

    public void setPowerPkidName(String powerPkidName) {
        this.powerPkidName = powerPkidName;
    }

    public String getPeriodNo() {
        return periodNo;
    }

    public void setPeriodNo(String periodNo) {
        this.periodNo = periodNo;
    }

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getStatusFlagName() {
        return flowStatusName;
    }

    public void setStatusFlagName(String flowStatusName) {
        this.flowStatusName = flowStatusName;
    }

    public String getPreStatusFlagName() {
        return flowStatusReasonName;
    }

    public void setPreStatusFlagName(String flowStatusReasonName) {
        this.flowStatusReasonName = flowStatusReasonName;
    }

    public String getPreStatusFlag() {
        return flowStatusReason;
    }

    public void setPreStatusFlag(String flowStatusReason) {
        this.flowStatusReason = flowStatusReason;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getLastUpdBy() {
        return lastUpdBy;
    }

    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy;
    }

    public String getLastUpdByName() {
        return lastUpdByName;
    }

    public void setLastUpdByName(String lastUpdByName) {
        this.lastUpdByName = lastUpdByName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastUpdTime() {
        return lastUpdTime;
    }

    public void setLastUpdTime(String lastUpdTime) {
        this.lastUpdTime = lastUpdTime;
    }

    public String getFlowStatusReasonName() {
        return flowStatusReasonName;
    }

    public void setFlowStatusReasonName(String flowStatusReasonName) {
        this.flowStatusReasonName = flowStatusReasonName;
    }

    public String getFlowStatusReason() {
        return flowStatusReason;
    }

    public void setFlowStatusReason(String flowStatusReason) {
        this.flowStatusReason = flowStatusReason;
    }

    public String getFlowStatusName() {
        return flowStatusName;
    }

    public void setFlowStatusName(String flowStatusName) {
        this.flowStatusName = flowStatusName;
    }

    public String getSpareField() {
        return spareField;
    }

    public void setSpareField(String spareField) {
        this.spareField = spareField;
    }
}