package task.view.workorder;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: ����1:53
 * To change this template use File | Settings | File Templates.
 */
import task.repository.model.model_show.AttachmentModel;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import skyline.util.JxlsManager;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;
import task.common.enums.*;
import task.repository.model.*;
import task.repository.model.model_show.CttItemShow;
import task.service.*;
import task.view.flow.EsCommon;
import task.view.flow.EsFlowControl;
import jxl.write.WriteException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean
@ViewScoped
public class WorkOrderItemAction {
    private static final Logger logger = LoggerFactory.getLogger(WorkOrderItemAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;

    private CttInfo cttInfo;
    private CttItemShow cttItemShowSel;
    private CttItemShow cttItemShowAdd;
    private CttItemShow cttItemShowUpd;
    private CttItemShow cttItemShowDel;
    private List<CttItem> cttItemList;
    private List<CttItemShow> cttItemShowList;

    //����
    private List<AttachmentModel> attachmentList;
    private HtmlGraphicImage image;
    //�ϴ������ļ�
    private StreamedContent downloadFile;

    /*��������*/
    private String strBelongToType;
    /*������*/
    private String strCttInfoPkid;

    /*�ύ����*/
    private String strSubmitType;

    /*���ƿؼ��ڻ����ϵĿ�������ʵStart*/
    private StyleModel styleModelNo;
    private StyleModel styleModel;
    //��ʾ�Ŀ���
    private String strPassVisible;
    private String strPassFailVisible;
    private String strNotPassToStatus;
    private String strFlowType;
    private List<CttItemShow> cttItemShowListExcel;
    private Map beansMap;
    // ¼�뱸ע
    private String strFlowStatusRemark;

    @PostConstruct
    public void init() {
        try {
            Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            beansMap = new HashMap();
            strBelongToType = EnumResType.RES_TYPE0.getCode();

            if (parammap.containsKey("strFlowType")) {
                strFlowType = parammap.get("strFlowType").toString();
            }

            if (parammap.containsKey("strCttInfoPkid")) {
                strCttInfoPkid = parammap.get("strCttInfoPkid").toString();
                cttInfo = cttInfoService.getCttInfoByPkId(strCttInfoPkid);

                strPassVisible = "true";
                strPassFailVisible = "true";
                if ("Mng".equals(strFlowType)) {
                    if (EnumFlowStatus.FLOW_STATUS0.getCode().equals(cttInfo.getFlowStatus())){
                        strPassVisible = "false";
                    }else {
                        strPassFailVisible = "false";
                    }
                }else {
                    if (("Check".equals(strFlowType)&&EnumFlowStatus.FLOW_STATUS1.getCode().equals(cttInfo.getFlowStatus()))
                            ||("DoubleCheck".equals(strFlowType) && EnumFlowStatus.FLOW_STATUS2.getCode().equals(cttInfo.getFlowStatus()))
                            ||("Approve".equals(strFlowType) && EnumFlowStatus.FLOW_STATUS3.getCode().equals(cttInfo.getFlowStatus()))){
                        strPassVisible = "false";
                    }
                }
                resetAction();
                initData();
            }
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
        }
    }

    /*��ʼ������*/
    private void initData() {
        /*�γɹ�ϵ��*/
        try {
            cttItemList =new ArrayList<>();
            cttItemShowList =new ArrayList<>();
            attachmentList=new ArrayList<>();
            /*��ʼ������״̬�б�*/
            if(ToolUtil.getStrIgnoreNull(strCttInfoPkid).length()!=0) {
                // ������¼���List
                attachmentList=ToolUtil.getListAttachmentByStrAttachment(cttInfo.getAttachment());
                // ���Excel��ͷ
                beansMap.put("cttInfo", cttInfo);
                cttItemList = cttItemService.getEsItemList(
                        strBelongToType, strCttInfoPkid);
                recursiveDataTable("root", cttItemList);
                cttItemShowList = getTkcttItemList_DoFromatNo(cttItemShowList);
                setTkcttItemList_AddTotal();
                // Excel�����γ�
                cttItemShowListExcel = new ArrayList<>();
                for (CttItemShow itemUnit : cttItemShowList) {
                    // ��ͬ���ۣ������������
                    itemUnit.setContractUnitPrice(
                                ToolUtil.getBdFrom0ToNull(itemUnit.getContractUnitPrice()));
                    itemUnit.setContractQuantity(
                                ToolUtil.getBdFrom0ToNull(itemUnit.getContractQuantity()));
                    itemUnit.setContractAmount(
                                ToolUtil.getBdFrom0ToNull(itemUnit.getContractAmount()));

                    CttItemShow itemUnitTemp = (CttItemShow) BeanUtils.cloneBean(itemUnit);
                    itemUnitTemp.setStrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrNo()));
                    cttItemShowListExcel.add(itemUnitTemp);
                }
                beansMap.put("cttItemShowListExcel", cttItemShowListExcel);
            }
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
            MessageUtil.addError("��ʼ��ʧ��");
        }
    }

    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId, List<CttItem> cttItemListPara) {
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<CttItem> subCttItemList = new ArrayList<>();
        // ͨ������id�������ĺ���
        subCttItemList = getEsCttItemListByParentPkid(strLevelParentId, cttItemListPara);
        for (CttItem itemUnit : subCttItemList) {
            CttItemShow cttItemShowTemp = null;
            String strCreatedByName = cttInfoService.getUserName(itemUnit.getCreatedBy());
            String strLastUpdByName = cttInfoService.getUserName(itemUnit.getLastUpdBy());
            // �㼶��
            cttItemShowTemp = new CttItemShow(
                    itemUnit.getPkid(),
                    itemUnit.getBelongToType(),
                    itemUnit.getBelongToPkid(),
                    itemUnit.getParentPkid(),
                    itemUnit.getGrade(),
                    itemUnit.getOrderid(),
                    itemUnit.getName(),
                    itemUnit.getUnit(),
                    itemUnit.getContractUnitPrice(),
                    itemUnit.getContractQuantity(),
                    itemUnit.getContractAmount(),
                    itemUnit.getSignPartAPrice(),
                    itemUnit.getArchivedFlag(),
                    itemUnit.getOriginFlag(),
                    itemUnit.getCreatedBy(),
                    strCreatedByName,
                    itemUnit.getCreatedTime(),
                    itemUnit.getLastUpdBy(),
                    strLastUpdByName,
                    itemUnit.getLastUpdTime(),
                    itemUnit.getRecVersion(),
                    itemUnit.getRemark(),
                    itemUnit.getCorrespondingPkid(),
                    "",
                    "",
                    itemUnit.getSpareField()
            );
            cttItemShowList.add(cttItemShowTemp);
            recursiveDataTable(cttItemShowTemp.getPkid(), cttItemListPara);
        }
    }

    /*����group��orderid��ʱ���Ʊ��strNo*/
    private List<CttItemShow> getTkcttItemList_DoFromatNo(
            List<CttItemShow> cttItemShowListPara) {
        String strTemp = "";
        Integer intBeforeGrade = -1;
        for (CttItemShow itemUnit : cttItemShowListPara) {
            if (itemUnit.getGrade().equals(intBeforeGrade)) {
                if (strTemp.lastIndexOf(".") < 0) {
                    strTemp = itemUnit.getOrderid().toString();
                } else {
                    strTemp = strTemp.substring(0, strTemp.lastIndexOf(".")) + "." + itemUnit.getOrderid().toString();
                }
            } else {
                if (itemUnit.getGrade() == 1) {
                    strTemp = itemUnit.getOrderid().toString();
                } else {
                    if (!itemUnit.getGrade().equals(intBeforeGrade)) {
                        if (itemUnit.getGrade().compareTo(intBeforeGrade) > 0) {
                            strTemp = strTemp + "." + itemUnit.getOrderid().toString();
                        } else {
                            Integer intTemp = ToolUtil.lookIndex(strTemp, '.', itemUnit.getGrade() - 1);
                            strTemp = strTemp.substring(0, intTemp);
                            strTemp = strTemp + "." + itemUnit.getOrderid().toString();
                        }
                    }
                }
            }
            intBeforeGrade = itemUnit.getGrade();
            itemUnit.setStrNo(ToolUtil.padLeft_DoLevel(itemUnit.getGrade(), strTemp));
        }
        return cttItemShowListPara;
    }

    private void setTkcttItemList_AddTotal() {
        List<CttItemShow> cttItemShowListTemp = new ArrayList<CttItemShow>();
        cttItemShowListTemp.addAll(cttItemShowList);

        cttItemShowList.clear();
        // С��
        BigDecimal bdTotal = new BigDecimal(0);
        BigDecimal bdAllTotal = new BigDecimal(0);
        CttItemShow itemUnit = new CttItemShow();
        CttItemShow itemUnitNext = new CttItemShow();
        for (int i = 0; i < cttItemShowListTemp.size(); i++) {
            itemUnit = cttItemShowListTemp.get(i);
            bdTotal = bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmount()));
            bdAllTotal = bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmount()));
            cttItemShowList.add(itemUnit);

            if (i + 1 < cttItemShowListTemp.size()) {
                itemUnitNext = cttItemShowListTemp.get(i + 1);
                if (itemUnitNext.getParentPkid().equals("root")) {
                    CttItemShow cttItemShowTemp = new CttItemShow();
                    cttItemShowTemp.setName("�ϼ�");
                    cttItemShowTemp.setPkid("total" + i);
                    cttItemShowTemp.setContractAmount(bdTotal);
                    cttItemShowList.add(cttItemShowTemp);
                    bdTotal = new BigDecimal(0);
                }
            } else if (i + 1 == cttItemShowListTemp.size()) {
                CttItemShow cttItemShowTemp = new CttItemShow();
                cttItemShowTemp.setName("�ϼ�");
                cttItemShowTemp.setPkid("total" + i);
                cttItemShowTemp.setContractAmount(bdTotal);
                cttItemShowList.add(cttItemShowTemp);
                bdTotal = new BigDecimal(0);

                // �ܺϼ�
                cttItemShowTemp = new CttItemShow();
                cttItemShowTemp.setName("�ܺϼ�");
                cttItemShowTemp.setPkid("total_all" + i);
                cttItemShowTemp.setContractAmount(bdAllTotal);
                cttItemShowList.add(cttItemShowTemp);
            }
        }
    }

    /*����*/
    public void resetAction() {
        strSubmitType = "Add";
        styleModelNo = new StyleModel();
        styleModelNo.setDisabled_Flag("false");
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        cttItemShowSel = new CttItemShow(strBelongToType, strCttInfoPkid);
        cttItemShowAdd = new CttItemShow(strBelongToType, strCttInfoPkid);
        cttItemShowUpd = new CttItemShow(strBelongToType, strCttInfoPkid);
        cttItemShowDel = new CttItemShow(strBelongToType, strCttInfoPkid);
    }

    public void blurCalculateAmountAction() {
        BigDecimal bigDecimal;
        if (strSubmitType.equals("Add")) {
            if (cttItemShowAdd.getContractUnitPrice() == null || cttItemShowAdd.getContractQuantity() == null) {
                bigDecimal = null;
            } else {
                bigDecimal = cttItemShowAdd.getContractUnitPrice().multiply(cttItemShowAdd.getContractQuantity());
            }
            cttItemShowAdd.setContractAmount(bigDecimal);
        }
        if (strSubmitType.equals("Upd")) {
            if (cttItemShowUpd.getContractUnitPrice() == null || cttItemShowUpd.getContractQuantity() == null) {
                bigDecimal = null;
            } else {
                bigDecimal = cttItemShowUpd.getContractUnitPrice().multiply(cttItemShowUpd.getContractQuantity());
            }
            cttItemShowUpd.setContractAmount(bigDecimal);
        }
    }

    /*�ҵ����¼�*/
    public void selectRecordAction(String strSubmitTypePara, CttItemShow cttItemShowPara) {
        try {
            if (strSubmitTypePara.equals("Add")) {
                return;
            }
            strSubmitType = strSubmitTypePara;
            if (strSubmitTypePara.equals("Sel")) {
                cttItemShowSel = (CttItemShow) BeanUtils.cloneBean(cttItemShowPara);
                cttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrNo()));
                cttItemShowSel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrCorrespondingItemNo()));
            }
            if (strSubmitTypePara.equals("Upd")) {
                cttItemShowUpd = (CttItemShow) BeanUtils.cloneBean(cttItemShowPara);
                cttItemShowUpd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowUpd.getStrNo()));
            } else if (strSubmitTypePara.equals("Del")) {
                cttItemShowDel = (CttItemShow) BeanUtils.cloneBean(cttItemShowPara);
                cttItemShowDel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowDel.getStrNo()));
            }
        } catch (Exception e) {
            logger.error("ѡ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public Boolean blurStrNoToGradeAndOrderidAction() {
        CttItemShow cttItemShowTemp = new CttItemShow(strBelongToType, strCttInfoPkid);
        if (strSubmitType.equals("Add")) {
            cttItemShowTemp = cttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")) {
            cttItemShowTemp = cttItemShowUpd;
        }
        String strIgnoreSpaceOfStr = ToolUtil.getIgnoreSpaceOfStr(cttItemShowTemp.getStrNo());
        if (StringUtils.isEmpty(strIgnoreSpaceOfStr)) {
            return true;
        }
        String strRegex = "[1-9]\\d*(\\.[1-9]\\d*)*";
        if (!strIgnoreSpaceOfStr.matches(strRegex)) {
            MessageUtil.addError("��ȷ������ı�ţ����" + strIgnoreSpaceOfStr + "��ʽ����ȷ��");
            return strNoBlurFalse();
        }

        //�ñ����Ѿ�����
        if(!strSubmitType.equals("Upd")){
            if(getEsCttItemByStrNo(strIgnoreSpaceOfStr, cttItemShowList)!=null){
            }
            else{ //�ñ��벻����
            }
        }
        Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");

        if (intLastIndexof < 0) {
            List<CttItem> itemHieRelapListSubTemp = new ArrayList<>();
            itemHieRelapListSubTemp = getEsCttItemListByParentPkid("root", cttItemList);

            if (itemHieRelapListSubTemp.size() == 0) {
                if (!strIgnoreSpaceOfStr.equals("1")) {
                    MessageUtil.addError("��ȷ������ı�ţ��ñ�Ų����Ϲ淶��Ӧ����1��");
                    return strNoBlurFalse();
                }
            } else {
                if (itemHieRelapListSubTemp.size() + 1 < Integer.parseInt(strIgnoreSpaceOfStr)) {
                    MessageUtil.addError("��ȷ������ı�ţ��ñ�Ų����Ϲ淶��Ӧ����" + (itemHieRelapListSubTemp.size() + 1) + "��");
                    return strNoBlurFalse();
                }
            }
            cttItemShowTemp.setGrade(1) ;
            cttItemShowTemp.setOrderid(Integer.parseInt(strIgnoreSpaceOfStr));
            cttItemShowTemp.setParentPkid("root");
        } else {
            String strParentNo = strIgnoreSpaceOfStr.substring(0, intLastIndexof);
            CttItemShow cttItemShowTemp1 = new CttItemShow();
            cttItemShowTemp1 = getEsCttItemByStrNo(strParentNo, cttItemShowList);
            if (cttItemShowTemp1 == null || cttItemShowTemp1.getPkid() == null) {
                MessageUtil.addError("��ȷ������ı�ţ�����" + strParentNo + "�����ڣ�");
                return strNoBlurFalse();
            } else {
                List<CttItem> itemHieRelapListSubTemp = new ArrayList<>();
                itemHieRelapListSubTemp = getEsCttItemListByParentPkid(
                        cttItemShowTemp1.getPkid(),
                        cttItemList);
                if (itemHieRelapListSubTemp.size() == 0) {
                    if (!cttItemShowTemp.getStrNo().equals(strParentNo + ".1")) {
                        MessageUtil.addError("��ȷ������ı�ţ��ñ�Ų����Ϲ淶��Ӧ����" + strParentNo + ".1��");
                        return strNoBlurFalse();
                    }
                } else {
                    String strOrderid = strIgnoreSpaceOfStr.substring(intLastIndexof + 1);
                    if (itemHieRelapListSubTemp.size() + 1 < Integer.parseInt(strOrderid)) {
                        MessageUtil.addError("��ȷ������ı�ţ��ñ�Ų����Ϲ淶��Ӧ����" + strParentNo + "." +
                                (itemHieRelapListSubTemp.size() + 1) + "��");
                        return strNoBlurFalse();
                    }
                }
                String strTemps[] = strIgnoreSpaceOfStr.split("\\.");
                cttItemShowTemp.setGrade(strTemps.length);
                cttItemShowTemp.setOrderid(Integer.parseInt(strTemps[strTemps.length - 1]));
                cttItemShowTemp.setParentPkid(cttItemShowTemp1.getPkid());
            }
        }
        return true ;
    }
    /*�ύǰ�ļ�飺�����������*/
    private Boolean subMitActionPreCheck() {
        CttItemShow cttItemShowTemp = new CttItemShow(strBelongToType, strCttInfoPkid);
        if (strSubmitType.equals("Add")) {
            cttItemShowTemp = cttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")) {
            cttItemShowTemp = cttItemShowUpd;
        }
        if (StringUtils.isEmpty(cttItemShowTemp.getStrNo())) {
            MessageUtil.addError("�������ţ�");
            return false;
        }
        if (StringUtils.isEmpty(cttItemShowTemp.getName())) {
            MessageUtil.addError("���������ƣ�");
            return false;
        }
        if ((cttItemShowTemp.getContractUnitPrice() != null &&
                cttItemShowTemp.getContractUnitPrice().compareTo(BigDecimal.ZERO) != 0) ||
                (cttItemShowTemp.getContractQuantity() != null &&
                        cttItemShowTemp.getContractQuantity().compareTo(BigDecimal.ZERO) != 0)) {
            /*��ǰ̨�ؼ�,�������BigDecimal���ͱ���Ϊnull�ģ��Զ�ת��Ϊ0����������ģ�����null*/
            if (StringUtils.isEmpty(cttItemShowTemp.getUnit())) {
                MessageUtil.addError("�����뵥λ��");
                return false;
            }
        }
        return true;
    }
    public void submitThisRecordAction(){
        try{
            /*�ύǰ�ļ��*/
            if(strSubmitType .equals("Del")) {
                cttItemService.setAfterThisOrderidSubOneByNode(cttItemShowDel);
            }else{
                if(!subMitActionPreCheck()){
                    return ;
                }
                /*itemUnitConstruct��grade,orderid,parentpkid*/
                if(!blurStrNoToGradeAndOrderidAction()){
                    return ;
                }
                if(strSubmitType .equals("Upd")) {
                    cttItemService.updateRecord(cttItemShowUpd) ;
                }
                else if(strSubmitType .equals("Add")) {
                     CttItem cttItemTemp = cttItemService.fromModelShowToModel(cttItemShowAdd);
                    if (cttItemService.isExistSameRecordInDb(cttItemTemp)){
                        MessageUtil.addInfo("�ñ�Ŷ�Ӧ��¼�Ѵ��ڣ�������¼�롣");
                        return;
                    }
                    cttItemService.setAfterThisOrderidPlusOneByNode(cttItemShowAdd);
                    resetAction();
                }
            }
            switch (strSubmitType){
                case "Add" : MessageUtil.addInfo("����������ɡ�");
                    break;
                case "Upd" : MessageUtil.addInfo("����������ɡ�");
                    break;
                case "Del" : MessageUtil.addInfo("ɾ��������ɡ�");
            }
            initData();
        } catch (Exception e) {
            switch (strSubmitType){
                case "Add" : MessageUtil.addError("��������ʧ�ܣ�"+ e.getMessage());
                    break;
                case "Upd" : MessageUtil.addError("��������ʧ�ܣ�"+ e.getMessage());
                    break;
                case "Del" : MessageUtil.addError("ɾ������ʧ�ܣ�"+ e.getMessage());
            }
        }
    }

    /*�������ݿ��в㼶��ϵ�����б�õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<CttItem> getEsCttItemListByParentPkid(String strLevelParentPkid,
             List<CttItem> cttItemListPara) {
        List<CttItem> tempCttItemList =new ArrayList<CttItem>();
        /*�ܿ��ظ��������ݿ�*/
        for(CttItem itemUnit: cttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempCttItemList.add(itemUnit);
            }
        }
        return tempCttItemList;
    }
    /*���ܰ���ͬ�б��и��ݱ���ҵ���*/
    private CttItemShow getEsCttItemByStrNo(
             String strNo,
             List<CttItemShow> cttItemShowListPara){
        CttItemShow cttItemShowTemp =null;
        try{
            for(CttItemShow itemUnit: cttItemShowListPara){
                if(ToolUtil.getIgnoreSpaceOfStr(itemUnit.getStrNo()).equals(strNo)) {
                    cttItemShowTemp =(CttItemShow)BeanUtils.cloneBean(itemUnit);
                    break;
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return cttItemShowTemp;
    }
    private Boolean strNoBlurFalse(){
        cttItemShowSel.setPkid("") ;
        cttItemShowSel.setParentPkid("");
        cttItemShowSel.setGrade(null);
        cttItemShowSel.setOrderid(null);
        return false;
    }
    private boolean checkPreMng(CttInfo cttInfoPara) {
        if (StringUtils.isEmpty(cttInfoPara.getId())) {
            return false;
        } else if (StringUtils.isEmpty(cttInfoPara.getName())) {
            return false;
        } else if (StringUtils.isEmpty(cttInfoPara.getSignDate())) {
            return false;
        }
        if (StringUtils.isEmpty(cttInfoPara.getSignPartA())) {
            return false;
        } else if (StringUtils.isEmpty(cttInfoPara.getSignPartB())) {
            return false;
        } else if (StringUtils.isEmpty(cttInfoPara.getCttStartDate())) {
            return false;
        } else if (StringUtils.isEmpty(cttInfoPara.getCttEndDate())) {
            return false;
        }
        return true;
    }
    /**
     * ����Ȩ�޽������
     *
     * @param strPowerTypePara
     */
    public void onClickForPowerAction(String strPowerTypePara) {
        try {
            strPowerTypePara=strFlowType+strPowerTypePara;
            if (strPowerTypePara.contains("Mng")) {
                if (strPowerTypePara.equals("MngPass")) {
                    if(!checkPreMng(cttInfo)){
                        MessageUtil.addError("��ͬ��Ϣδά���������޷�¼����ɣ�");
                        return ;
                    }
                    // ״̬��־����ʼ
                    cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // ԭ��¼�����
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON0.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("����¼����ɣ�");
                } else if (strPowerTypePara.equals("MngFail")) {
                    cttInfo.setFlowStatus(null);
                    cttInfo.setFlowStatusReason(null);
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
            }// ���
            else if (strPowerTypePara.contains("Check") && !strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("CheckPass")) {
                    // ״̬��־�����
                    cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // ԭ�����ͨ��
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON1.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("�������ͨ����");
                } else if (strPowerTypePara.equals("CheckFail")) {
                    // ״̬��־����ʼ
                    cttInfo.setFlowStatus(null);
                    // ԭ�����δ��
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON2.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("�������δ����");
                }
            } // ����
            else if (strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("DoubleCheckPass")) {
                    // ״̬��־������
                    cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // ԭ�򣺸���ͨ��
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("���ݸ���ͨ����");
                } else if (strPowerTypePara.equals("DoubleCheckFail")) {
                    // ����д����ʵ��Խ���˻�
                    if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())) {
                        cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())) {
                        cttInfo.setFlowStatus(null);
                    }
                    // ԭ�򣺸���δ��
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON4.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("���ݸ���δ����");
                }
            }// ��׼
            else if (strPowerTypePara.contains("Approve")) {
                if (strPowerTypePara.equals("ApprovePass")) {
                    // ״̬��־����׼
                    cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // ԭ����׼ͨ��
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON5.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("������׼ͨ����");
                } else if (strPowerTypePara.equals("ApproveFail")) {
                    // ����Ƿ�ʹ��
                    String strCttTypeTemp = "";
                    if (cttInfo.getCttType().equals(EnumResType.RES_TYPE0.getCode())) {
                        strCttTypeTemp = EnumResType.RES_TYPE1.getCode();
                    } else if (cttInfo.getCttType().equals(EnumResType.RES_TYPE1.getCode())) {
                        strCttTypeTemp = EnumResType.RES_TYPE2.getCode();
                    }

                    // ����д����ʵ��Խ���˻�
                    if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS2.getCode())) {
                        cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())) {
                        cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())) {
                        cttInfo.setFlowStatus(null);
                    }
                    // ԭ����׼δ��
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON6.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);


                }
            }
            strPassVisible="false";
            strPassFailVisible="false";
        } catch (Exception e) {
            logger.error("�������̻�ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void onExportExcel()throws IOException, WriteException {
        String excelFilename = "�ܰ���ͬ-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
        JxlsManager jxls = new JxlsManager();
        jxls.exportList(excelFilename, beansMap,"oriTkctt.xls");
    }

    // ����
    public void onViewAttachment(AttachmentModel attachmentModelPara) {
        image.setValue("/upload/" + attachmentModelPara.getCOLUMN_NAME());
    }
    public void delAttachmentRecordAction(AttachmentModel attachmentModelPara){
        try {
            File deletingFile = new File(attachmentModelPara.getCOLUMN_PATH());
            deletingFile.delete();
            attachmentList.remove(attachmentModelPara) ;
            StringBuffer sbTemp = new StringBuffer();
            for (AttachmentModel item : attachmentList) {
                sbTemp.append(item.getCOLUMN_PATH() + ";");
            }
            cttInfo.setAttachment(sbTemp.toString());
            cttInfoService.updateRecord(cttInfo);
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void download(String strAttachment){
        try{
            if(StringUtils .isEmpty(strAttachment) ){
                MessageUtil.addError("·��Ϊ�գ��޷����أ�");
                logger.error("·��Ϊ�գ��޷����أ�");
            }
            else {
                String fileName=FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload")+"/"+strAttachment;
                File file = new File(fileName);
                InputStream stream = new FileInputStream(fileName);
                downloadFile = new DefaultStreamedContent(stream, new MimetypesFileTypeMap().getContentType(file), new String(strAttachment.getBytes("gbk"),"iso8859-1"));
            }
        } catch (Exception e) {
            logger.error("�����ļ�ʧ��", e);
            MessageUtil.addError("�����ļ�ʧ��,"+e.getMessage()+strAttachment);
        }
    }
    public void upload(FileUploadEvent event) {
        BufferedInputStream inStream = null;
        FileOutputStream fileOutputStream = null;
        UploadedFile uploadedFile = event.getFile();
        AttachmentModel attachmentModel = new AttachmentModel();
        if (uploadedFile != null) {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload");
            File superFile = new File(path);
            if (!superFile.exists()) {
                superFile.mkdirs();
            }
            File descFile = new File(superFile, uploadedFile.getFileName());
            attachmentModel.setCOLUMN_ID(ToolUtil.getIntIgnoreNull(attachmentList.size()) + "");
            attachmentModel.setCOLUMN_NAME(uploadedFile.getFileName());
            attachmentModel.setCOLUMN_PATH(descFile.getAbsolutePath());
            for (AttachmentModel item : attachmentList){
                if (item.getCOLUMN_NAME().equals(attachmentModel.getCOLUMN_NAME())) {
                    MessageUtil.addError("�����Ѵ��ڣ�");
                    return;
                }
            }

            attachmentList.add(attachmentModel);

            StringBuffer sb = new StringBuffer();
            for (AttachmentModel item : attachmentList) {
                sb.append(item.getCOLUMN_NAME() + ";");
            }
            if(sb.length()>4000){
                MessageUtil.addError("����·��("+sb.toString()+")�����ѳ����������ֵ4000��������⣬����ϵϵͳ����Ա��");
                return;
            }
            cttInfo.setAttachment(sb.toString());
            cttInfoService.updateRecord(cttInfo);
            try {
                inStream = new BufferedInputStream(uploadedFile.getInputstream());
                fileOutputStream = new FileOutputStream(descFile);
                byte[] buf = new byte[1024];
                int num;
                while ((num = inStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, num);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    /*�����ֶ�Start*/
    public CttItemService getCttItemService() {
        return cttItemService;
    }
    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
    }
    public EsCommon getEsCommon() {
        return esCommon;
    }
    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }
    public CttItemShow getCttItemShowSel() {
        return cttItemShowSel;
    }
    public void setCttItemShowSel(CttItemShow cttItemShowSel) {
        this.cttItemShowSel = cttItemShowSel;
    }
    public List<CttItemShow> getCttItemShowList() {
        return cttItemShowList;
    }
    public void setCttItemShowList(List<CttItemShow> cttItemShowList) {
        this.cttItemShowList = cttItemShowList;
    }
    public String getStrBelongToPkid() {
        return strCttInfoPkid;
    }
    public void setStrBelongToPkid(String strCttInfoPkid) {
        this.strCttInfoPkid = strCttInfoPkid;
    }
    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }
    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }
    public String getStrSubmitType() {
        return strSubmitType;
    }
    public StyleModel getStyleModelNo() {
        return styleModelNo;
    }
    public StyleModel getStyleModel() {
        return styleModel;
    }
    public CttItemShow getCttItemShowAdd() {
        return cttItemShowAdd;
    }
    public void setCttItemShowAdd(CttItemShow cttItemShowAdd) {
        this.cttItemShowAdd = cttItemShowAdd;
    }
    public CttItemShow getCttItemShowDel() {
        return cttItemShowDel;
    }
    public void setCttItemShowDel(CttItemShow cttItemShowDel) {
        this.cttItemShowDel = cttItemShowDel;
    }
    public CttItemShow getCttItemShowUpd() {
        return cttItemShowUpd;
    }
    public void setCttItemShowUpd(CttItemShow cttItemShowUpd) {
        this.cttItemShowUpd = cttItemShowUpd;
    }
    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }
    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }
    public String getStrFlowType() {
        return strFlowType;
    }
    public void setStrFlowType(String strFlowType) {
        this.strFlowType = strFlowType;
    }
    public CttInfo getCttInfo() {
        return cttInfo;
    }
    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }
    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }
    public List<CttItemShow> getCttItemShowListExcel() {
        return cttItemShowListExcel;
    }
    public void setCttItemShowListExcel(List<CttItemShow> cttItemShowListExcel) {
        this.cttItemShowListExcel = cttItemShowListExcel;
    }
    public void setCttInfo(CttInfo cttInfo) {
        this.cttInfo = cttInfo;
    }

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map beansMap) {
        this.beansMap = beansMap;
    }

    //�ļ�
    public List<AttachmentModel> getAttachmentList() {
        return attachmentList;
    }
    public void setAttachmentList(List<AttachmentModel> attachmentList) {
        this.attachmentList = attachmentList;
    }
    public HtmlGraphicImage getImage() {
        return image;
    }
    public void setImage(HtmlGraphicImage image) {
        this.image = image;
    }
    public StreamedContent getDownloadFile() {
        return downloadFile;
    }
    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    public String getStrPassVisible() {
        return strPassVisible;
    }

    public String getStrPassFailVisible() {
        return strPassFailVisible;
    }

    public String getStrFlowStatusRemark() {
        return strFlowStatusRemark;
    }

    public void setStrFlowStatusRemark(String strFlowStatusRemark) {
        this.strFlowStatusRemark = strFlowStatusRemark;
    }
/*�����ֶ�End*/
}