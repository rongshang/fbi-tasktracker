package task.repository.model.model_show;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-4-22
 * Time: ÏÂÎç4:18
 * To change this template use File | Settings | File Templates.
 */
public class WorkorderInfoShow implements Serializable {
    private String pkid;
    private String parentPkid;
    private String id;
    private String type;
    private String name;
    private String signDate;
    private String startDate;
    private String endDate;
    private String attachment;
    private String remark;
    private String archivedFlag;
    private String createdBy;
    private String createdByName;
    private String createdTime;
    private String lastUpdBy;
    private String lastUpdByName;
    private String lastUpdTime;
    private Integer recVersion;
    private Boolean isSeled;
    public String getPkid() {
        return pkid;
    }

    public WorkorderInfoShow(String pkid, String id,String name, String remark,
                             String lastUpdBy,String lastUpdTime, Integer recVersion, Boolean isSeled) {
        this.pkid = pkid;
        this.id = id;
        this.name = name;
        this.remark = remark;
        this.lastUpdBy=lastUpdBy;
        this.lastUpdTime=lastUpdTime;
        this.recVersion = recVersion;
        this.isSeled=isSeled;
    }

    public WorkorderInfoShow(String id, String name, String remark,
                             String cttStartDate, String cttEndDate) {
        this.id = id;
        this.name = name;
        this.remark = remark;
        this.startDate = cttStartDate;
        this.endDate = cttEndDate;
    }

    public WorkorderInfoShow() {
    }

    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment == null ? null : attachment.trim();
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
