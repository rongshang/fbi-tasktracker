package task.view.workorder;

import task.common.enums.EnumResType;
import task.common.enums.EnumFlowStatus;
import task.repository.model.model_show.WorkorderInfoShow;
import task.service.*;
import task.view.flow.EsCommon;
import task.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: ����4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class WorkorderInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(WorkorderInfoAction.class);
    @ManagedProperty(value = "#{workorderInfoService}")
    private WorkorderInfoService workorderInfoService;
    @ManagedProperty(value = "#{workorderItemService}")
    private WorkorderItemService workorderItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;

    private WorkorderInfoShow workorderInfoShowQry;
    private WorkorderInfoShow workorderInfoShowSel;
    private WorkorderInfoShow workorderInfoShowAdd;
    private WorkorderInfoShow workorderInfoShowUpd;
    private WorkorderInfoShow workorderInfoShowDel;
    private List<WorkorderInfoShow> workorderInfoShowList;

    private String strSubmitType;
    /*����ά������㼶���ֵ���ʾ*/
    private StyleModel styleModel;
    //����ʱ�����ã��������̹رգ������Ͻ�������
    @PostConstruct
    public void init() {
        try {
            initData();
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
        }
    }
    public void initData() {
        try {
            this.workorderInfoShowList = new ArrayList<WorkorderInfoShow>();
            workorderInfoShowQry = new WorkorderInfoShow();
            workorderInfoShowQry.setCttType(EnumResType.RES_TYPE0.getCode());
            workorderInfoShowSel = new WorkorderInfoShow();
            workorderInfoShowSel.setCttType(EnumResType.RES_TYPE0.getCode());
            workorderInfoShowAdd = new WorkorderInfoShow();
            workorderInfoShowAdd.setCttType(EnumResType.RES_TYPE0.getCode());
            workorderInfoShowUpd = new WorkorderInfoShow();
            workorderInfoShowUpd.setCttType(EnumResType.RES_TYPE0.getCode());
            workorderInfoShowDel = new WorkorderInfoShow();
            workorderInfoShowDel.setCttType(EnumResType.RES_TYPE0.getCode());
            styleModel = new StyleModel();
            styleModel.setDisabled_Flag("false");
            strSubmitType = "";
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
            MessageUtil.addError("��ʼ��ʧ��");
        }
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = workorderInfoService.getStrMaxCttId(EnumResType.RES_TYPE0.getCode());
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "TKCTT" + ToolUtil.getStrToday() + "001";
            } else {
                if (strMaxId.length() > 3) {
                    String strTemp = strMaxId.substring(strMaxId.length() - 3).replaceFirst("^0+", "");
                    if (ToolUtil.strIsDigit(strTemp)) {
                        intTemp = Integer.parseInt(strTemp);
                        intTemp = intTemp + 1;
                        strMaxId = strMaxId.substring(0, strMaxId.length() - 3) + StringUtils.leftPad(intTemp.toString(), 3, "0");
                    } else {
                        strMaxId += "001";
                    }
                }
            }
            workorderInfoShowAdd.setId(strMaxId);
            workorderInfoShowUpd.setId(strMaxId);
        } catch (Exception e) {
            logger.error("�ܰ���ͬ��Ϣ��ѯʧ��", e);
            MessageUtil.addError("�ܰ���ͬ��Ϣ��ѯʧ��");
        }
    }

    public String onQueryAction(String strQryFlag,String strQryMsgOutPara) {
        try {
            if (strQryFlag.equals("Qry")) {

            } else if (strQryFlag.contains("Mng")) {
                workorderInfoShowQry.setStrStatusFlagBegin(null);
                workorderInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
            } else if (strQryFlag.contains("Check")) {
                if (strQryFlag.contains("DoubleCheck")) {
                    workorderInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS1.getCode());
                    workorderInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS2.getCode());
                }else{
                    workorderInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS0.getCode());
                    workorderInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS1.getCode());
                }
            }  else if (strQryFlag.contains("Approve")) {
                if (strQryFlag.equals("ApprovedQry")) {
                    workorderInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS3.getCode());
                    workorderInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                }else{
                    workorderInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS2.getCode());
                    workorderInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                }
            }
            this.workorderInfoShowList.clear();
            workorderInfoShowList = workorderInfoService.selectCttByStatusFlagBegin_End(workorderInfoShowQry);

            if(strQryMsgOutPara.equals("true"))  {
                if (workorderInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("û�в�ѯ�����ݡ�");
                }
            }
        } catch (Exception e) {
            logger.error("��ͬ��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��ͬ��Ϣ��ѯʧ��");
        }
        return null;
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        workorderInfoShowAdd = new WorkorderInfoShow();
        workorderInfoShowAdd.setCttType(EnumResType.RES_TYPE0.getCode());
    }
    public void selectRecordAction(
                                   String strSubmitTypePara,
                                   WorkorderInfoShow workorderInfoShowPara) {
        try {
            strSubmitType = strSubmitTypePara;
            // ��ѯ
            workorderInfoShowPara.setCreatedByName(workorderInfoService.getUserName(workorderInfoShowPara.getCreatedBy()));
            workorderInfoShowPara.setLastUpdByName(workorderInfoService.getUserName(workorderInfoShowPara.getLastUpdBy()));
            if (strSubmitTypePara.equals("Sel")) {
                 workorderInfoShowSel = (WorkorderInfoShow) BeanUtils.cloneBean(workorderInfoShowPara);
            }else if (strSubmitTypePara.equals("Add")) {
                workorderInfoShowAdd = (WorkorderInfoShow) BeanUtils.cloneBean(workorderInfoShowPara);
            }else if (strSubmitTypePara.equals("Upd")) {
                 workorderInfoShowUpd = (WorkorderInfoShow) BeanUtils.cloneBean(workorderInfoShowPara);
            }else if (strSubmitTypePara.equals("Del")){
                workorderInfoShowDel = (WorkorderInfoShow) BeanUtils.cloneBean(workorderInfoShowPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * ����������Ŀ���
     */
    private boolean submitPreCheck(WorkorderInfoShow workorderInfoShowPara) {
        if (StringUtils.isEmpty(workorderInfoShowPara.getId())) {
            MessageUtil.addError("�������ͬ�ţ�");
            return false;
        } else if (StringUtils.isEmpty(workorderInfoShowPara.getName())) {
            MessageUtil.addError("�������ͬ����");
            return false;
        } else if (StringUtils.isEmpty(workorderInfoShowPara.getSignDate())) {
            MessageUtil.addError("������ǩ�����ڣ�");
            return false;
        }
        if (StringUtils.isEmpty(workorderInfoShowPara.getSignPartA())) {
            MessageUtil.addError("������ǩ���׷���");
            return false;
        } else if (StringUtils.isEmpty(workorderInfoShowPara.getSignPartB())) {
            MessageUtil.addError("������ǩ���ҷ���");
            return false;
        } else if (StringUtils.isEmpty(workorderInfoShowPara.getCttStartDate())) {
            MessageUtil.addError("�������ͬ��ʼʱ�䣡");
            return false;
        } else if (StringUtils.isEmpty(workorderInfoShowPara.getCttEndDate())) {
            MessageUtil.addError("�������ͬ��ֹʱ�䣡");
            return false;
        }
        return true;
    }
    /**
     * �ύά��Ȩ��
     *
     * @param
     */
    public void onClickForMngAction() {
        if (strSubmitType.equals("Add")) {
            if (!submitPreCheck(workorderInfoShowAdd)) {
                return;
            }
            WorkorderInfoShow workorderInfoShowTemp =new WorkorderInfoShow();
            workorderInfoShowTemp.setCttType(workorderInfoShowAdd.getCttType());
            workorderInfoShowTemp.setCttType(workorderInfoShowAdd.getCttType());
            if (workorderInfoService.getListByModelShow(workorderInfoShowTemp).size()>0) {
                MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
            } else {
                addRecordAction(workorderInfoShowAdd);
                MessageUtil.addInfo("����������ɡ�");
                resetActionForAdd();
            }
        } else if (strSubmitType.equals("Upd")) {
			if (!submitPreCheck(workorderInfoShowUpd)) {
	         return;
	        }
            updRecordAction(workorderInfoShowUpd);
            MessageUtil.addInfo("����������ɡ�");
        } else if (strSubmitType.equals("Del")) {
            deleteRecordAction(workorderInfoShowDel);
            MessageUtil.addInfo("ɾ��������ɡ�");
        }
        onQueryAction("Mng","false");
    }

    private void addRecordAction(WorkorderInfoShow workorderInfoShowPara) {
        try {
            workorderInfoShowPara.setCttType(EnumResType.RES_TYPE0.getCode());
            if (workorderInfoShowPara.getCttType().equals(EnumResType.RES_TYPE0.getCode())) {
                workorderInfoShowPara.setParentPkid("ROOT");
            }
            workorderInfoService.insertRecord(workorderInfoShowPara);
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(WorkorderInfoShow workorderInfoShowPara) {
        try {
            workorderInfoShowPara.setCttType(EnumResType.RES_TYPE0.getCode());
            workorderInfoService.updateRecord(workorderInfoShowPara);
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void deleteRecordAction(WorkorderInfoShow workorderInfoShowPara) {
        try {
            workorderInfoShowPara.setCttType(EnumResType.RES_TYPE0.getCode());
            int deleteRecordNumOfCttItem= workorderItemService.deleteRecord(workorderInfoShowPara);
            int deleteRecordNumOfCtt= workorderInfoService.deleteRecord(workorderInfoShowPara.getPkid());
            if (deleteRecordNumOfCtt<=0&&deleteRecordNumOfCttItem<=0){
                MessageUtil.addInfo("�ü�¼��ɾ����");
                return;
            }
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*�����ֶ� Start*/

    public WorkorderItemService getWorkorderItemService() {
        return workorderItemService;
    }

    public void setWorkorderItemService(WorkorderItemService workorderItemService) {
        this.workorderItemService = workorderItemService;
    }

    public WorkorderInfoService getWorkorderInfoService() {
        return workorderInfoService;
    }

    public void setWorkorderInfoService(WorkorderInfoService workorderInfoService) {
        this.workorderInfoService = workorderInfoService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public WorkorderInfoShow getWorkorderInfoShowQry() {
        return workorderInfoShowQry;
    }

    public void setWorkorderInfoShowQry(WorkorderInfoShow workorderInfoShowQry) {
        this.workorderInfoShowQry = workorderInfoShowQry;
    }

    public WorkorderInfoShow getWorkorderInfoShowAdd() {
        return workorderInfoShowAdd;
    }

    public void setWorkorderInfoShowAdd(WorkorderInfoShow workorderInfoShowAdd) {
        this.workorderInfoShowAdd = workorderInfoShowAdd;
    }

    public WorkorderInfoShow getWorkorderInfoShowDel() {
        return workorderInfoShowDel;
    }

    public void setWorkorderInfoShowDel(WorkorderInfoShow workorderInfoShowDel) {
        this.workorderInfoShowDel = workorderInfoShowDel;
    }

    public List<WorkorderInfoShow> getWorkorderInfoShowList() {
        return workorderInfoShowList;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public WorkorderInfoShow getWorkorderInfoShowUpd() {
        return workorderInfoShowUpd;
    }

    public void setWorkorderInfoShowUpd(WorkorderInfoShow workorderInfoShowUpd) {
        this.workorderInfoShowUpd = workorderInfoShowUpd;
    }

    public WorkorderInfoShow getWorkorderInfoShowSel() {
        return workorderInfoShowSel;
    }

    public void setWorkorderInfoShowSel(WorkorderInfoShow workorderInfoShowSel) {
        this.workorderInfoShowSel = workorderInfoShowSel;
    }
    /*�����ֶ� End*/
}
