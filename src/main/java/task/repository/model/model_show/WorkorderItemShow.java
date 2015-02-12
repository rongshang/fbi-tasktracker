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
    private String strNo;
    /*task.WORKORDER_ITEM.PKID*/
    private String pkid;
    /*task.WORKORDER_ITEM.BELONG_TO_PKID*/
    private String belongToPkid;
    /*task.WORKORDER_ITEM.ORDERID*/
    private Integer orderid;
    /*task.WORKORDER_ITEM.GRADE*/
    private Integer grade;
    /*task.WORKORDER_ITEM.PARENT_PKID*/
    private String parentPkid;
    /*task.WORKORDER_ITEM.CORRESPONDING_PKID*/
    private String correspondingPkid;

    /*task.WORKORDER_ITEM.NAME/task.ES_ITEM_INFO.NAME*/
    private String name;
    /*task.WORKORDER_ITEM.REMARK/task.ES_ITEM_INFO.REMARK*/
    private String remark;

    /*task.ES_ITEM_INFO.UNIT*/
    private String unit;
    /*task.ES_ITEM_INFO.CONTRACT_UNIT_PRICE*/
    private BigDecimal unitPrice;
    /*task.ES_ITEM_INFO.CONTRACT_QUANTITY*/
    private BigDecimal quantity;
    /*task.ES_ITEM_INFO.CONTRACT_AMOUNT*/
    private BigDecimal amount;
    /*task.ES_ITEM_INFO.ARCHIVED_FLAG*/
    private String archivedFlag;
    /*task.ES_ITEM_INFO.ORIGIN_FLAG*/
    private String originFlag;
    /*task.ES_ITEM_INFO.CREATED_BY*/
    private String createdBy;
    /*task.ES_ITEM_INFO.CREATED_TIME*/
    private String createdTime;
    /*task.ES_ITEM_INFO.LAST_UPD_BY*/
    private String lastUpdBy;
    /*task.ES_ITEM_INFO.LAST_UPD_TIME*/
    private String lastUpdTime;
    /*task.ES_ITEM_INFO.REC_VERSION*/
    private Integer recVersion;

    private String createdByName;
    private String lastUpdByName;

    public WorkorderItemShow() {

    }

    public WorkorderItemShow(String strBelongToPkid) {
        /*task.WORKORDER_ITEM.ITEMBELONGTOPKID*/
        this.belongToPkid =strBelongToPkid ;
    }

    public WorkorderItemShow(
            String strPkId,
            String strBelongToPkid,
            String strParentPkid,
            Integer intGrade,
            Integer intOrderid,
            String strName,
            String strUnit,
            BigDecimal bdUnitPrice,
            BigDecimal bdQuantity,
            BigDecimal bdAmount,
            String strArchivedFlag,
            String strOriginFlag,
            String strCreatedBy,
            String strCreatedByName,
            String dtCreatedTime,
            String strLastUpdBy,
            String strLastUpdByName,
            String dtLastUpdTime,
            Integer intRecVersion,
            String strRemark,
            String strCorrespondingPkid,
            String strNo) {
        /*task.ES_ITEM_INFO.PKID*/
        this.pkid=strPkId;
        /*task.WORKORDER_ITEM.ITEMBELONGTOPKID*/
        this.belongToPkid =strBelongToPkid ;
        this.strNo =strNo ;
        this.orderid =intOrderid;
        /*task.ES_ITEM_INFO.ID*/
        this.grade=intGrade;
        /*task.ES_ITEM_INFO.NAME*/
        this.name=strName;
        this.parentPkid =strParentPkid;
        /*task.ES_ITEM_INFO.REMARK*/
        this.remark=strRemark;
        /*task.WORKORDER_ITEM.CORRESPONDING_PKID*/
        this.correspondingPkid =strCorrespondingPkid;

        /*task.ES_ITEM_INFO.UNIT*/
        this.unit=strUnit;
        /*task.ES_ITEM_INFO.CONTRACT_UNIT_PRICE*/
        this.unitPrice=bdUnitPrice;
        /*task.ES_ITEM_INFO.CONTRACT_QUANTITY*/
        this.quantity=bdQuantity;
        /*task.ES_ITEM_INFO.CONTRACT_AMOUNT*/
        this.amount=bdAmount;
        /*task.ES_ITEM_INFO.ARCHIVED_FLAG*/
        this.archivedFlag=strArchivedFlag;
        /*task.ES_ITEM_INFO.ORIGIN_FLAG*/
        this.originFlag=strOriginFlag;
        /*task.ES_ITEM_INFO.CREATED_BY*/
        this.createdBy=strCreatedBy;
        this.createdByName=strCreatedByName;
        /*task.ES_ITEM_INFO.CREATED_TIME*/
        this.createdTime=dtCreatedTime;
        /*task.ES_ITEM_INFO.LAST_UPD_BY*/
        this.lastUpdBy=strLastUpdBy;
        this.lastUpdByName=strLastUpdByName;
        /*task.ES_ITEM_INFO.LAST_UPD_TIME*/
        this.lastUpdTime=dtLastUpdTime;
        /*task.ES_ITEM_INFO.REC_VERSION*/
        this.recVersion=intRecVersion;

        this.strNo =strNo ;
    }

    public boolean equals(Object obj)
    {
        if (this == obj){
            return true;
        }

        if (obj.getClass() == WorkorderItemShow.class)
        {
            WorkorderItemShow itemForTkcttAndCstpl = (WorkorderItemShow)obj;
            return ((itemForTkcttAndCstpl.pkid==null&&this.pkid==null)||
                      itemForTkcttAndCstpl.pkid.equals(this.pkid))
                    &&((itemForTkcttAndCstpl.belongToPkid==null&&this.belongToPkid==null)||
                        itemForTkcttAndCstpl.belongToPkid .equals(this.belongToPkid))
                    &&((itemForTkcttAndCstpl.strNo==null&&this.strNo==null)||
                        itemForTkcttAndCstpl.strNo .equals(this.strNo))
                    &&(itemForTkcttAndCstpl.orderid==this.orderid)
                    &&(itemForTkcttAndCstpl.grade==this.grade)
                    &&((itemForTkcttAndCstpl.name==null&&this.name==null)||
                        itemForTkcttAndCstpl.name .equals(this.name))
                    &&((itemForTkcttAndCstpl.parentPkid==null&&this.parentPkid==null)||
                        itemForTkcttAndCstpl.parentPkid .equals(this.parentPkid))
                    &&((itemForTkcttAndCstpl.remark==null&&this.remark==null)||
                        itemForTkcttAndCstpl.remark .equals(this.remark))
                    &&((itemForTkcttAndCstpl.correspondingPkid==null&&this.correspondingPkid==null)||
                        itemForTkcttAndCstpl.correspondingPkid .equals(this.correspondingPkid))
                    &&((itemForTkcttAndCstpl.unit==null&&this.unit==null)||
                        itemForTkcttAndCstpl.unit .equals(this.unit))
                    &&(itemForTkcttAndCstpl.unitPrice==this.unitPrice)
                    &&(itemForTkcttAndCstpl.quantity==this.quantity)
                    &&(itemForTkcttAndCstpl.amount==this.amount)
                    &&((itemForTkcttAndCstpl.archivedFlag==null&&this.archivedFlag==null)||
                        itemForTkcttAndCstpl.archivedFlag .equals(this.archivedFlag))
                    &&((itemForTkcttAndCstpl.originFlag==null&&this.originFlag==null)||
                        itemForTkcttAndCstpl.originFlag .equals(this.originFlag))
                    &&((itemForTkcttAndCstpl.createdBy==null&&this.createdBy==null)||
                        itemForTkcttAndCstpl.createdBy .equals(this.createdBy))
                    &&((itemForTkcttAndCstpl.createdTime==null&&this.createdTime==null)||
                        itemForTkcttAndCstpl.createdTime .equals(this.createdTime))
                    &&((itemForTkcttAndCstpl.lastUpdBy==null&&this.lastUpdBy==null)||
                        itemForTkcttAndCstpl.lastUpdBy .equals(this.lastUpdBy))
                    &&((itemForTkcttAndCstpl.lastUpdTime==null&&this.lastUpdTime==null)||
                        itemForTkcttAndCstpl.lastUpdTime .equals(this.lastUpdTime))
                    &&(itemForTkcttAndCstpl.recVersion==this.recVersion)
                    ;
        }
        return false;
    }

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getBelongToPkid() {
        return belongToPkid;
    }

    public void setBelongToPkid(String belongToPkid) {
        this.belongToPkid = belongToPkid;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentPkid() {
        return parentPkid;
    }

    public void setParentPkid(String parentPkid) {
        this.parentPkid = parentPkid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCorrespondingPkid() {
        return correspondingPkid;
    }

    public void setCorrespondingPkid(String correspondingPkid) {
        this.correspondingPkid = correspondingPkid;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public Integer getRecVersion() {
        return recVersion;
    }

    public void setRecVersion(Integer recVersion) {
        this.recVersion = recVersion;
    }

    public String getStrNo() {
        return strNo;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setStrNo(String strNo) {
        this.strNo = strNo;
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
}