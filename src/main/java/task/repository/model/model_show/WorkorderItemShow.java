package task.repository.model.model_show;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 13-2-17
 * Time: 下午9:54
 * To change this template use File | Settings | File Templates.
 */

public class WorkorderItemShow implements Serializable {
    /*编号，用作显示用，不在数据库里存储，动态显示.依据grade,orderid*/
    /*task.WORKORDER_ITEM.PKID*/
    private String pkid;
    private String id;
    /*task.WORKORDER_ITEM.NAME/task.ES_ITEM_INFO.ITEM_CONTENT*/
    private String itemContent;
    /*task.ES_ITEM_INFO.ARCHIVED_FLAG*/
    private String archivedFlag;
    /*task.ES_ITEM_INFO.ORIGIN_FLAG*/
    private String originFlag;
    /*task.ES_ITEM_INFO.CREATED_BY*/
    private String createdBy;
    private String createdByName;
    /*task.ES_ITEM_INFO.CREATED_TIME*/
    private String createdTime;
    /*task.ES_ITEM_INFO.LAST_UPD_BY*/
    private String lastUpdBy;
    private String lastUpdByName;
    /*task.ES_ITEM_INFO.LAST_UPD_TIME*/
    private String lastUpdTime;
    /*task.ES_ITEM_INFO.REMARK*/
    private String remark;
    /*task.ES_ITEM_INFO.REC_VERSION*/
    private Integer recVersion;
    /*task.ES_ITEM_INFO.LEVELIDX*/
    private Integer levelidx;
    /*task.WORKORDER_ITEM.PARENT_PKID*/
    private String parentPkid;
    /*task.WORKORDER_ITEM.INFO_PKID*/
    private String infoPkid;
    /*task.WORKORDER_ITEM.TID*/
    private String tid;

    public WorkorderItemShow() {

    }
    public WorkorderItemShow(String strInfoPkid) {
        infoPkid=strInfoPkid;
    }

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getParentPkid() {
        return parentPkid;
    }

    public void setParentPkid(String parentPkid) {
        this.parentPkid = parentPkid;
    }

    public String getOriginFlag() {
        return originFlag;
    }

    public void setOriginFlag(String originFlag) {
        this.originFlag = originFlag;
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

    public String getArchivedFlag() {
        return archivedFlag;
    }

    public void setArchivedFlag(String archivedFlag) {
        this.archivedFlag = archivedFlag;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    public Integer getLevelidx() {
        return levelidx;
    }

    public void setLevelidx(Integer levelidx) {
        this.levelidx = levelidx;
    }

    public String getInfoPkid() {
        return infoPkid;
    }

    public void setInfoPkid(String infoPkid) {
        this.infoPkid = infoPkid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}