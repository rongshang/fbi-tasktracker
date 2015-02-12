package task.view.flow;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-3-26
 * Time: ����6:12
 * To change this template use File | Settings | File Templates.
 */

import task.repository.model.CttInfo;
import task.service.CttInfoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.service.ToolsService;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User Think
 * Date 13-3-26
 * Time ����6:12
 */
@ManagedBean
@ViewScoped
public class EsFlowControl implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(EsFlowControl.class);
    private static final SelectItem siAllForSelect= new SelectItem("","ȫ��");
    private static final SelectItem siNullForSelect= new SelectItem("","");

    @ManagedProperty(value = "#{toolsService}")
    private ToolsService toolsService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;

    private List<SelectItem> flowStatusFromDBList;
    private List<SelectItem> flowStatusReasonList;
    private List<SelectItem> flowStatusReasonFromDBList;
    private List<SelectItem> achivedFlagList;

    @PostConstruct
    public void init() {
        try {
            this.flowStatusFromDBList = toolsService.getEnuSelectItemList("FLOW_STATUS", false, false);
            this.flowStatusReasonFromDBList= toolsService.getEnuSelectItemList("FLOW_STATUS_REASON", false, false);
            this.achivedFlagList = toolsService.getEnuSelectItemList("ARCHIVED_FLAG", true, false);
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
        }
    }

    public String getLabelByValueInStatusFlaglist(String strValue){
        if(!StringUtils.isEmpty(strValue)){
            for(SelectItem itemUnit:flowStatusFromDBList){
                if(itemUnit.getValue().equals(strValue)){
                    return itemUnit.getLabel();
                }
            }
        }
        return "";
    }


    public CttInfo getCttInfoByPkId(String strPkid) {
        return cttInfoService.getCttInfoByPkId(strPkid);
    }

    public String getLabelByValueInPreStatusFlaglist(String strValue){
        if(!StringUtils.isEmpty(strValue)){
            for(SelectItem itemUnit:flowStatusReasonFromDBList){
                if(itemUnit.getValue().equals(strValue)){
                    return itemUnit.getLabel();
                }
            }
        }
        return "";
    }

    public List<SelectItem> getBackToStatusFlagList(String strFlowStatusPara){
        List<SelectItem> flowStatusListTemp=new ArrayList<>();
        if(strFlowStatusPara.equals("Qry")){
            flowStatusListTemp.addAll(flowStatusFromDBList);
            flowStatusListTemp.add(0,siAllForSelect);
        }else if(strFlowStatusPara.equals("Mng")){
            for(SelectItem itemUnit:flowStatusFromDBList) {
                if(itemUnit.getLabel().contains("��ʼ")){
                    flowStatusListTemp.add(itemUnit);
                    flowStatusListTemp.add(0,siNullForSelect);
                }
            }
        }else if(strFlowStatusPara.equals("Check")){
            for(SelectItem itemUnit:flowStatusFromDBList) {
                if(itemUnit.getLabel().equals("��ʼ")){
                    flowStatusListTemp.add(itemUnit);
                }
            }
        }else if(strFlowStatusPara.equals("DoubleCheck")){
            for(SelectItem itemUnit:flowStatusFromDBList) {
                if(itemUnit.getLabel().equals("��ʼ")||
                        itemUnit.getLabel().equals("���")){
                    flowStatusListTemp.add(itemUnit);
                }
            }
        }else if(strFlowStatusPara.equals("Approve")){
            for(SelectItem itemUnit:flowStatusFromDBList) {
                if(itemUnit.getLabel().equals("��ʼ")||
                        itemUnit.getLabel().equals("���")||
                        itemUnit.getLabel().equals("����")){
                    flowStatusListTemp.add(itemUnit);
                }
            }
        }
        return flowStatusListTemp;
    }

    /*�����ֶ� Start*/
    public ToolsService getToolsService() {
        return toolsService;
    }

    public void setToolsService(ToolsService toolsService) {
        this.toolsService = toolsService;
    }

    public List<SelectItem> getAchivedFlagList() {
        return achivedFlagList;
    }

    public void setAchivedFlagList(List<SelectItem> achivedFlagList) {
        this.achivedFlagList = achivedFlagList;
    }

    public List<SelectItem> getPreStatusFlagList() {
        return flowStatusReasonList;
    }

    public void setPreStatusFlagList(List<SelectItem> flowStatusReasonList) {
        this.flowStatusReasonList = flowStatusReasonList;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }
}
