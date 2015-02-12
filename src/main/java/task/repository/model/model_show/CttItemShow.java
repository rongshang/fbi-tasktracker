package task.repository.model.model_show;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 13-2-17
 * Time: ����9:54
 * To change this template use File | Settings | File Templates.
 */

public class CttItemShow implements Serializable {
    /*��ţ�������ʾ�ã��������ݿ���洢����̬��ʾ.����grade,orderid*/
    private String strNo;
    /*��Ӧ��ţ�������ʾ�ã��������ݿ���洢����̬��ʾ������correspondingPkid*/
    private String strCorrespondingItemNo;
    /*��Ӧ������ƣ�������ʾ�ã��������ݿ���洢����̬��ʾ������correspondingPkid*/
    private String strCorrespondingItemName;

    /*task.CTT_ITEM.PKID*/
    private String pkid;
    /*task.CTT_ITEM.BELONG_TO_TYPE*/
    private String belongToType;
    /*task.CTT_ITEM.BELONG_TO_PKID*/
    private String belongToPkid;
    /*task.CTT_ITEM.ORDERID*/
    private Integer orderid;
    /*task.CTT_ITEM.GRADE*/
    private Integer grade;
    /*task.CTT_ITEM.PARENT_PKID*/
    private String parentPkid;
    /*task.CTT_ITEM.CORRESPONDING_PKID*/
    private String correspondingPkid;

    /*task.CTT_ITEM.NAME/task.ES_ITEM_INFO.NAME*/
    private String name;
    /*task.CTT_ITEM.REMARK/task.ES_ITEM_INFO.REMARK*/
    private String remark;

    /*task.ES_ITEM_INFO.UNIT*/
    private String unit;
    /*task.ES_ITEM_INFO.CONTRACT_UNIT_PRICE*/
    private BigDecimal contractUnitPrice;
    /*task.ES_ITEM_INFO.CONTRACT_QUANTITY*/
    private BigDecimal contractQuantity;
    /*task.ES_ITEM_INFO.CONTRACT_AMOUNT*/
    private BigDecimal contractAmount;
    /*task.ES_ITEM_INFO.SIGN_PART_A_PRICE*/
    private BigDecimal signPartAPrice;
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
    // ��Ҫ��Խ����
    private String spareField;

    public CttItemShow() {

    }

    public CttItemShow(String strBelongToType,
                       String strBelongToPkid) {
        //To change body of created methods use File | Settings | File Templates.
        /*task.CTT_ITEM.BelongToType*/
        this.belongToType =strBelongToType ;
        /*task.CTT_ITEM.ITEMBELONGTOPKID*/
        this.belongToPkid =strBelongToPkid ;
    }

    public CttItemShow(
            String strPkId,
            String strBelongToType,
            String strBelongToPkid,
            String strParentPkid,
            Integer intGrade,
            Integer intOrderid,
            String strName,
            String strUnit,
            BigDecimal bdContractUnitPrice,
            BigDecimal bdContractQuantity,
            BigDecimal bdContractAmount,
            BigDecimal bdSignPartAPrice,
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
            String strNo,
            String strCorrespondingItemNo,
			String spareField) {
        /*task.ES_ITEM_INFO.PKID*/
        this.pkid=strPkId;
        /*task.CTT_ITEM.BelongToType*/
        this.belongToType =strBelongToType ;
        /*task.CTT_ITEM.ITEMBELONGTOPKID*/
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
        /*task.CTT_ITEM.CORRESPONDING_PKID*/
        this.correspondingPkid =strCorrespondingPkid;

        /*task.ES_ITEM_INFO.UNIT*/
        this.unit=strUnit;
        /*task.ES_ITEM_INFO.CONTRACT_UNIT_PRICE*/
        this.contractUnitPrice=bdContractUnitPrice;
        /*task.ES_ITEM_INFO.CONTRACT_QUANTITY*/
        this.contractQuantity=bdContractQuantity;
        /*task.ES_ITEM_INFO.CONTRACT_AMOUNT*/
        this.contractAmount=bdContractAmount;
        /*task.ES_ITEM_INFO.SIGN_PART_A_PRICE*/
        this.signPartAPrice=bdSignPartAPrice;
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
        this.strCorrespondingItemNo =strCorrespondingItemNo;
		this.spareField=spareField;
    }

    public boolean equals(Object obj)
    {
        if (this == obj){
            return true;
        }

        if (obj.getClass() == CttItemShow.class)
        {
            CttItemShow itemForTkcttAndCstpl = (CttItemShow)obj;
            return ((itemForTkcttAndCstpl.pkid==null&&this.pkid==null)||
                      itemForTkcttAndCstpl.pkid.equals(this.pkid))
                    &&((itemForTkcttAndCstpl.belongToType==null&&this.belongToType==null)||
                        itemForTkcttAndCstpl.belongToType .equals(this.belongToType))
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
                    &&(itemForTkcttAndCstpl.contractUnitPrice==this.contractUnitPrice)
                    &&(itemForTkcttAndCstpl.contractQuantity==this.contractQuantity)
                    &&(itemForTkcttAndCstpl.contractAmount==this.contractAmount)
                    &&(itemForTkcttAndCstpl.signPartAPrice==this.signPartAPrice)
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

    public String getBelongToType() {
        return belongToType;
    }

    public void setBelongToType(String belongToType) {
        this.belongToType = belongToType;
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

    public BigDecimal getContractUnitPrice() {
        return contractUnitPrice;
    }

    public void setContractUnitPrice(BigDecimal contractUnitPrice) {
        this.contractUnitPrice = contractUnitPrice;
    }

    public BigDecimal getContractQuantity() {
        return contractQuantity;
    }

    public void setContractQuantity(BigDecimal contractQuantity) {
        this.contractQuantity = contractQuantity;
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public BigDecimal getSignPartAPrice() {
        return signPartAPrice;
    }

    public void setSignPartAPrice(BigDecimal signPartAPrice) {
        this.signPartAPrice = signPartAPrice;
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

    public void setStrNo(String strNo) {
        this.strNo = strNo;
    }

    public String getStrCorrespondingItemNo() {
        return strCorrespondingItemNo;
    }

    public void setStrCorrespondingItemNo(String strCorrespondingItemNo) {
        this.strCorrespondingItemNo = strCorrespondingItemNo;
    }

    public String getStrCorrespondingItemName() {
        return strCorrespondingItemName;
    }

    public void setStrCorrespondingItemName(String strCorrespondingItemName) {
        this.strCorrespondingItemName = strCorrespondingItemName;
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

    public String getSpareField() {
        return spareField;
    }

    public void setSpareField(String spareField) {
        this.spareField = spareField;
    }
}