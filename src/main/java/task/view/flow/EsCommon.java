package task.view.flow;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-3-26
 * Time: ����6:12
 * To change this template use File | Settings | File Templates.
 */

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.service.ToolsService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.model.SelectItem;
import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA
 * User Think
 * Date 13-3-26
 * Time ����6:12
 */
@ManagedBean
@ViewScoped
public class EsCommon implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(EsCommon.class);

    @ManagedProperty(value = "#{toolsService}")
    private ToolsService toolsService;

    private HtmlGraphicImage image;

    private List<SelectItem> originFlagList;

    private List<SelectItem> resTypeList; // ITEM׷�������б�
    //�ɱ��ƻ���
    private List<SelectItem> cstplItemNamelist;
    //�ְ���ͬ��
    private List<SelectItem> subcttItemNamelist;
    //�ͻ���Ϣ��
    private List<SelectItem> customerlist;

    //�ϴ������ļ�
    private StreamedContent downloadFile;

    private List<SelectItem> flowStatusList;
    private List<SelectItem> flowStatusReasonList;
    private List<SelectItem> achivedFlagList;

    @PostConstruct
    public void init() {
        try {
            this.originFlagList = toolsService.getEnuSelectItemList("ORIGIN_FLAG", false, false);
            this.resTypeList = toolsService.getEnuSelectItemList("RES_TYPE", true, false);
            this.cstplItemNamelist = toolsService.getEnuSelectItemList("CSTPLITEM_NAME", false, false);
            this.subcttItemNamelist= toolsService.getEnuSelectItemList("SUBCTTITEM_NAME", false, false);
            this.flowStatusReasonList= toolsService.getEnuSelectItemList("FLOW_STATUS_REASON", true, false);
            this.flowStatusList = toolsService.getEnuSelectItemList("FLOW_STATUS", true, false);
            this.achivedFlagList = toolsService.getEnuSelectItemList("ARCHIVED_FLAG", true, false);
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
        }
    }

    public String getCustNameByCustIdFromList(String strCustId){
        for(SelectItem itemUnit:customerlist){
            if(itemUnit.getValue().equals(strCustId)){
                return itemUnit.getLabel();
            }
        }
        return "";
    }

    public String originFlagListValueOfAlias(String strValue){
        if(!StringUtils.isEmpty(strValue)){
            return  originFlagList.get(Integer .parseInt(strValue)).getLabel() ;
        }
        return "";
    }

    public String resTypeListValueOfAlias(String strValue){
        if(!StringUtils.isEmpty(strValue)){
            if(resTypeList.get(0).getLabel().equals("ȫ��")){
                return  resTypeList.get(Integer .parseInt(strValue)+1).getLabel() ;
            }else{
                return  resTypeList.get(Integer .parseInt(strValue)).getLabel() ;
            }
        }
        else{
            return "";
        }
    }

    public Integer getIndexOfCstplItemNamelist(String strLabel){
        if(!StringUtils.isEmpty(strLabel)){
            for(SelectItem itemUnit:cstplItemNamelist){
                if(itemUnit.getLabel().equals(strLabel)){
                    return cstplItemNamelist.indexOf(itemUnit);
                }
            }
        }
        return -1;
    }
    public Integer getIndexOfSubcttItemNamelist(String strLabel){
        if(!StringUtils.isEmpty(strLabel)){
            for(SelectItem itemUnit:subcttItemNamelist){
                if(itemUnit.getLabel().equals(strLabel)){
                    return subcttItemNamelist.indexOf(itemUnit);
                }
            }
        }
        return -1;
    }

    public String getLabelByValueInresTypeList(String strValue){
        if(!StringUtils.isEmpty(strValue)){
            for(SelectItem itemUnit:resTypeList){
                if(itemUnit.getValue().equals(strValue)){
                    return itemUnit.getLabel();
                }
            }
        }
        return "";
    }

    //ְ���ֶ� begin
    public static Logger getLogger() {
        return logger;
    }

    public List<SelectItem> getFlowStatusList() {
        return flowStatusList;
    }

    public void setFlowStatusList(List<SelectItem> flowStatusList) {
        this.flowStatusList = flowStatusList;
    }

    public List<SelectItem> getFlowStatusReasonList() {
        return flowStatusReasonList;
    }

    public void setFlowStatusReasonList(List<SelectItem> flowStatusReasonList) {
        this.flowStatusReasonList = flowStatusReasonList;
    }

    public List<SelectItem> getAchivedFlagList() {
        return achivedFlagList;
    }

    public void setAchivedFlagList(List<SelectItem> achivedFlagList) {
        this.achivedFlagList = achivedFlagList;
    }

    public ToolsService getToolsService() {
        return toolsService;
    }

    public void setToolsService(ToolsService toolsService) {
        this.toolsService = toolsService;
    }

    public HtmlGraphicImage getImage() {
        return image;
    }

    public void setImage(HtmlGraphicImage image) {
        this.image = image;
    }

    public List<SelectItem> getOriginFlagList() {
        return originFlagList;
    }

    public void setOriginFlagList(List<SelectItem> originFlagList) {
        this.originFlagList = originFlagList;
    }

    public List<SelectItem> getResTypeList() {
        return resTypeList;
    }

    public void setResTypeList(List<SelectItem> resTypeList) {
        this.resTypeList = resTypeList;
    }

    public List<SelectItem> getCstplItemNamelist() {
        return cstplItemNamelist;
    }

    public void setCstplItemNamelist(List<SelectItem> cstplItemNamelist) {
        this.cstplItemNamelist = cstplItemNamelist;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    public List<SelectItem> getSubcttItemNamelist() {
        return subcttItemNamelist;
    }

    public void setSubcttItemNamelist(List<SelectItem> subcttItemNamelist) {
        this.subcttItemNamelist = subcttItemNamelist;
    }

    public List<SelectItem> getCustomerlist() {
        return customerlist;
    }

    public void setCustomerlist(List<SelectItem> customerlist) {
        this.customerlist = customerlist;
    }

//ְ���ֶ� End
}
