package task.repository.model.model_show;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-4-22
 * Time: ����4:18
 * To change this template use File | Settings | File Templates.
 */
public class WorkorderInfoShow implements Serializable {
    private String pkid;
    private String parentPkid;
    private String belongToId;
    private String id;
    private String name;
    private String signDate;
    private String signPartA;
    private String signPartAName;
    private String signPartB;
    private String signPartBName;
    private String cttStartDate;
    private String cttEndDate;
    private String attachment;
    private String remark;
    private String flowStatus;
    private String strStatusFlagBegin;
    private String strStatusFlagEnd;
    private String flowStatusReason;
    private String flowStatusRemark;
    private String archivedFlag;
    private String createdBy;
    private String createdByName;
    private String createdTime;
    private String lastUpdBy;
    private String lastUpdByName;
    private String lastUpdTime;
    private String spareField;
    private Integer recVersion;
    private String type;
    private Boolean isSeled;
    public String getPkid() {
        return pkid;
    }

    public WorkorderInfoShow(String pkid, String id,String name, String remark,
                             String flowStatus, String flowStatusReason, String lastUpdBy,
                             String lastUpdTime, Integer recVersion, Boolean isSeled) {
        this.pkid = pkid;
        this.id = id;
        this.name = name;
        this.remark = remark;
        this.flowStatus = flowStatus;
        this.flowStatusReason = flowStatusReason;
        this.lastUpdBy=lastUpdBy;
        this.lastUpdTime=lastUpdTime;
        this.recVersion = recVersion;
        this.isSeled=isSeled;
    }

    public WorkorderInfoShow(String id, String name, String remark, String signPartAName,
                             String signPartBName, String cttStartDate, String cttEndDate,
                             String flowStatus, String flowStatusReason) {
        this.id = id;
        this.name = name;
        this.remark = remark;
        this.signPartAName = signPartAName;
        this.signPartBName = signPartBName;
        this.cttStartDate = cttStartDate;
        this.cttEndDate = cttEndDate;
        this.flowStatus = flowStatus;
        this.flowStatusReason = flowStatusReason;
    }

    public WorkorderInfoShow() {
    }

    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
    }

    public String getBelongToId() {
        return belongToId;
    }

    public void setBelongToId(String belongToId) {
        this.belongToId = belongToId;
    }

    public String getParentPkid() {
        return parentPkid;
    }

    public void setParentPkid(String parentPkid) {
        this.parentPkid = parentPkid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate == null ? null : signDate.trim();
    }

    public String getSignPartA() {
        return signPartA;
    }

    public void setSignPartA(String signPartA) {
        this.signPartA = signPartA == null ? null : signPartA.trim();
    }

    public String getSignPartB() {
        return signPartB;
    }

    public void setSignPartB(String signPartB) {
        this.signPartB = signPartB == null ? null : signPartB.trim();
    }

    public String getSignPartAName() {
        return signPartAName;
    }

    public void setSignPartAName(String signPartAName) {
        this.signPartAName = signPartAName;
    }

    public String getSignPartBName() {
        return signPartBName;
    }

    public void setSignPartBName(String signPartBName) {
        this.signPartBName = signPartBName;
    }

    public String getCttStartDate() {
        return cttStartDate;
    }

    public void setCttStartDate(String cttStartDate) {
        this.cttStartDate = cttStartDate == null ? null : cttStartDate.trim();
    }

    public String getCttEndDate() {
        return cttEndDate;
    }

    public void setCttEndDate(String cttEndDate) {
        this.cttEndDate = cttEndDate == null ? null : cttEndDate.trim();
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment == null ? null : attachment.trim();
    }

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getFlowStatusRemark() {
        return flowStatusRemark;
    }

    public void setFlowStatusRemark(String flowStatusRemark) {
        this.flowStatusRemark = flowStatusRemark;
    }

    public String getFlowStatusReason() {
        return flowStatusReason;
    }

    public void setFlowStatusReason(String flowStatusReason) {
        this.flowStatusReason = flowStatusReason;
    }

    public String getArchivedFlag() {
        return archivedFlag;
    }

    public void setArchivedFlag(String archivedFlag) {
        this.archivedFlag = archivedFlag == null ? null : archivedFlag.trim();
    }

    public String getStrStatusFlagBegin() {
        return strStatusFlagBegin;
    }

    public void setStrStatusFlagBegin(String strStatusFlagBegin) {
        this.strStatusFlagBegin = strStatusFlagBegin;
    }

    public String getStrStatusFlagEnd() {
        return strStatusFlagEnd;
    }

    public void setStrStatusFlagEnd(String strStatusFlagEnd) {
        this.strStatusFlagEnd = strStatusFlagEnd;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdBy() {
        return lastUpdBy;
    }

    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy;
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

    public String getSpareField() {
        return spareField;
    }

    public void setSpareField(String spareField) {
        this.spareField = spareField;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getRecVersion() {
        return recVersion;
    }

    public void setRecVersion(Integer recVersion) {
        this.recVersion = recVersion;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsSeled() {
        return isSeled;
    }

    public void setIsSeled(Boolean isSeled) {
        this.isSeled = isSeled;
    }


}
