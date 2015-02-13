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
import task.repository.model.model_show.WorkorderItemShow;
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
public class WorkorderItemAction {
    private static final Logger logger = LoggerFactory.getLogger(WorkorderItemAction.class);
    @ManagedProperty(value = "#{workorderInfoService}")
    private WorkorderInfoService workorderInfoService;
    @ManagedProperty(value = "#{workorderItemService}")
    private WorkorderItemService workorderItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;

    private WorkorderInfo workorderInfo;
    private WorkorderItemShow workorderItemShowSel;
    private WorkorderItemShow workorderItemShowAdd;
    private WorkorderItemShow workorderItemShowUpd;
    private WorkorderItemShow workorderItemShowDel;
    private List<WorkorderItem> workorderItemList;
    private List<WorkorderItemShow> workorderItemShowList;

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
    private List<WorkorderItemShow> workorderItemShowListExcel;
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
                workorderInfo = workorderInfoService.getCttInfoByPkId(strCttInfoPkid);

                strPassVisible = "true";
                strPassFailVisible = "true";
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
            workorderItemList =new ArrayList<>();
            workorderItemShowList =new ArrayList<>();
            attachmentList=new ArrayList<>();
            /*��ʼ������״̬�б�*/
            if(ToolUtil.getStrIgnoreNull(strCttInfoPkid).length()!=0) {
                // ������¼���List
                attachmentList=ToolUtil.getListAttachmentByStrAttachment(workorderInfo.getAttachment());
                // ���Excel��ͷ
                beansMap.put("cttInfo", workorderInfo);
                workorderItemList = workorderItemService.getEsItemList(
                        strBelongToType, strCttInfoPkid);
                recursiveDataTable("root", workorderItemList);
                workorderItemShowList = getTkcttItemList_DoFromatNo(workorderItemShowList);
                setTkcttItemList_AddTotal();
                // Excel�����γ�
                workorderItemShowListExcel = new ArrayList<>();
                for (WorkorderItemShow itemUnit : workorderItemShowList) {
                    // ��ͬ���ۣ������������
                    itemUnit.setUnitPrice(
                                ToolUtil.getBdFrom0ToNull(itemUnit.getUnitPrice()));
                    itemUnit.setQuantity(
                                ToolUtil.getBdFrom0ToNull(itemUnit.getQuantity()));
                    itemUnit.setAmount(
                                ToolUtil.getBdFrom0ToNull(itemUnit.getAmount()));

                    WorkorderItemShow itemUnitTemp = (WorkorderItemShow) BeanUtils.cloneBean(itemUnit);
                    itemUnitTemp.setStrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrNo()));
                    workorderItemShowListExcel.add(itemUnitTemp);
                }
                beansMap.put("cttItemShowListExcel", workorderItemShowListExcel);
            }
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
            MessageUtil.addError("��ʼ��ʧ��");
        }
    }

    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId, List<WorkorderItem> workorderItemListPara) {
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<WorkorderItem> subWorkorderItemList = new ArrayList<>();
        // ͨ������id�������ĺ���
        subWorkorderItemList = getEsCttItemListByParentPkid(strLevelParentId, workorderItemListPara);
        for (WorkorderItem itemUnit : subWorkorderItemList) {
            WorkorderItemShow workorderItemShowTemp = null;
            String strCreatedByName = workorderInfoService.getUserName(itemUnit.getCreatedBy());
            String strLastUpdByName = workorderInfoService.getUserName(itemUnit.getLastUpdBy());
            // �㼶��
            workorderItemShowTemp = new WorkorderItemShow(
                    itemUnit.getPkid(),
                    itemUnit.getBelongToPkid(),
                    itemUnit.getParentPkid(),
                    itemUnit.getGrade(),
                    itemUnit.getOrderid(),
                    itemUnit.getName(),
                    itemUnit.getUnit(),
                    itemUnit.getUnitPrice(),
                    itemUnit.getQuantity(),
                    itemUnit.getAmount(),
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
                    "",
                    ""
            );
            workorderItemShowList.add(workorderItemShowTemp);
            recursiveDataTable(workorderItemShowTemp.getPkid(), workorderItemListPara);
        }
    }

    /*����group��orderid��ʱ���Ʊ��strNo*/
    private List<WorkorderItemShow> getTkcttItemList_DoFromatNo(
            List<WorkorderItemShow> workorderItemShowListPara) {
        String strTemp = "";
        Integer intBeforeGrade = -1;
        for (WorkorderItemShow itemUnit : workorderItemShowListPara) {
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
        return workorderItemShowListPara;
    }

    private void setTkcttItemList_AddTotal() {
        List<WorkorderItemShow> workorderItemShowListTemp = new ArrayList<WorkorderItemShow>();
        workorderItemShowListTemp.addAll(workorderItemShowList);

        workorderItemShowList.clear();
        // С��
        BigDecimal bdTotal = new BigDecimal(0);
        BigDecimal bdAllTotal = new BigDecimal(0);
        WorkorderItemShow itemUnit = new WorkorderItemShow();
        WorkorderItemShow itemUnitNext = new WorkorderItemShow();
        for (int i = 0; i < workorderItemShowListTemp.size(); i++) {
            itemUnit = workorderItemShowListTemp.get(i);
            bdTotal = bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getAmount()));
            bdAllTotal = bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getAmount()));
            workorderItemShowList.add(itemUnit);

            if (i + 1 < workorderItemShowListTemp.size()) {
                itemUnitNext = workorderItemShowListTemp.get(i + 1);
                if (itemUnitNext.getParentPkid().equals("root")) {
                    WorkorderItemShow workorderItemShowTemp = new WorkorderItemShow();
                    workorderItemShowTemp.setName("�ϼ�");
                    workorderItemShowTemp.setPkid("total" + i);
                    workorderItemShowTemp.setAmount(bdTotal);
                    workorderItemShowList.add(workorderItemShowTemp);
                    bdTotal = new BigDecimal(0);
                }
            } else if (i + 1 == workorderItemShowListTemp.size()) {
                WorkorderItemShow workorderItemShowTemp = new WorkorderItemShow();
                workorderItemShowTemp.setName("�ϼ�");
                workorderItemShowTemp.setPkid("total" + i);
                workorderItemShowTemp.setAmount(bdTotal);
                workorderItemShowList.add(workorderItemShowTemp);
                bdTotal = new BigDecimal(0);

                // �ܺϼ�
                workorderItemShowTemp = new WorkorderItemShow();
                workorderItemShowTemp.setName("�ܺϼ�");
                workorderItemShowTemp.setPkid("total_all" + i);
                workorderItemShowTemp.setAmount(bdAllTotal);
                workorderItemShowList.add(workorderItemShowTemp);
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
        workorderItemShowSel = new WorkorderItemShow(strCttInfoPkid);
        workorderItemShowAdd = new WorkorderItemShow(strCttInfoPkid);
        workorderItemShowUpd = new WorkorderItemShow(strCttInfoPkid);
        workorderItemShowDel = new WorkorderItemShow(strCttInfoPkid);
    }

    public void blurCalculateAmountAction() {
        BigDecimal bigDecimal;
        if (strSubmitType.equals("Add")) {
            if (workorderItemShowAdd.getUnitPrice() == null || workorderItemShowAdd.getQuantity() == null) {
                bigDecimal = null;
            } else {
                bigDecimal = workorderItemShowAdd.getUnitPrice().multiply(workorderItemShowAdd.getQuantity());
            }
            workorderItemShowAdd.setAmount(bigDecimal);
        }
        if (strSubmitType.equals("Upd")) {
            if (workorderItemShowUpd.getUnitPrice() == null || workorderItemShowUpd.getQuantity() == null) {
                bigDecimal = null;
            } else {
                bigDecimal = workorderItemShowUpd.getUnitPrice().multiply(workorderItemShowUpd.getQuantity());
            }
            workorderItemShowUpd.setAmount(bigDecimal);
        }
    }

    /*�ҵ����¼�*/
    public void selectRecordAction(String strSubmitTypePara, WorkorderItemShow workorderItemShowPara) {
        try {
            if (strSubmitTypePara.equals("Add")) {
                return;
            }
            strSubmitType = strSubmitTypePara;
            if (strSubmitTypePara.equals("Sel")) {
                workorderItemShowSel = (WorkorderItemShow) BeanUtils.cloneBean(workorderItemShowPara);
                workorderItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(workorderItemShowSel.getStrNo()));
            }
            if (strSubmitTypePara.equals("Upd")) {
                workorderItemShowUpd = (WorkorderItemShow) BeanUtils.cloneBean(workorderItemShowPara);
                workorderItemShowUpd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(workorderItemShowUpd.getStrNo()));
            } else if (strSubmitTypePara.equals("Del")) {
                workorderItemShowDel = (WorkorderItemShow) BeanUtils.cloneBean(workorderItemShowPara);
                workorderItemShowDel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(workorderItemShowDel.getStrNo()));
            }
        } catch (Exception e) {
            logger.error("ѡ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public Boolean blurStrNoToGradeAndOrderidAction() {
        WorkorderItemShow workorderItemShowTemp = new WorkorderItemShow(strCttInfoPkid);
        if (strSubmitType.equals("Add")) {
            workorderItemShowTemp = workorderItemShowAdd;
        }
        if (strSubmitType.equals("Upd")) {
            workorderItemShowTemp = workorderItemShowUpd;
        }
        String strIgnoreSpaceOfStr = ToolUtil.getIgnoreSpaceOfStr(workorderItemShowTemp.getStrNo());
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
            if(getEsCttItemByStrNo(strIgnoreSpaceOfStr, workorderItemShowList)!=null){
            }
            else{ //�ñ��벻����
            }
        }
        Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");

        if (intLastIndexof < 0) {
            List<WorkorderItem> itemHieRelapListSubTemp = new ArrayList<>();
            itemHieRelapListSubTemp = getEsCttItemListByParentPkid("root", workorderItemList);

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
            workorderItemShowTemp.setGrade(1) ;
            workorderItemShowTemp.setOrderid(Integer.parseInt(strIgnoreSpaceOfStr));
            workorderItemShowTemp.setParentPkid("root");
        } else {
            String strParentNo = strIgnoreSpaceOfStr.substring(0, intLastIndexof);
            WorkorderItemShow workorderItemShowTemp1 = new WorkorderItemShow();
            workorderItemShowTemp1 = getEsCttItemByStrNo(strParentNo, workorderItemShowList);
            if (workorderItemShowTemp1 == null || workorderItemShowTemp1.getPkid() == null) {
                MessageUtil.addError("��ȷ������ı�ţ�����" + strParentNo + "�����ڣ�");
                return strNoBlurFalse();
            } else {
                List<WorkorderItem> itemHieRelapListSubTemp = new ArrayList<>();
                itemHieRelapListSubTemp = getEsCttItemListByParentPkid(
                        workorderItemShowTemp1.getPkid(),
                        workorderItemList);
                if (itemHieRelapListSubTemp.size() == 0) {
                    if (!workorderItemShowTemp.getStrNo().equals(strParentNo + ".1")) {
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
                workorderItemShowTemp.setGrade(strTemps.length);
                workorderItemShowTemp.setOrderid(Integer.parseInt(strTemps[strTemps.length - 1]));
                workorderItemShowTemp.setParentPkid(workorderItemShowTemp1.getPkid());
            }
        }
        return true ;
    }
    /*�ύǰ�ļ�飺�����������*/
    private Boolean subMitActionPreCheck() {
        WorkorderItemShow workorderItemShowTemp = new WorkorderItemShow(strCttInfoPkid);
        if (strSubmitType.equals("Add")) {
            workorderItemShowTemp = workorderItemShowAdd;
        }
        if (strSubmitType.equals("Upd")) {
            workorderItemShowTemp = workorderItemShowUpd;
        }
        if (StringUtils.isEmpty(workorderItemShowTemp.getStrNo())) {
            MessageUtil.addError("�������ţ�");
            return false;
        }
        if (StringUtils.isEmpty(workorderItemShowTemp.getName())) {
            MessageUtil.addError("���������ƣ�");
            return false;
        }
        if ((workorderItemShowTemp.getUnitPrice() != null &&
                workorderItemShowTemp.getUnitPrice().compareTo(BigDecimal.ZERO) != 0) ||
                (workorderItemShowTemp.getQuantity() != null &&
                        workorderItemShowTemp.getQuantity().compareTo(BigDecimal.ZERO) != 0)) {
            /*��ǰ̨�ؼ�,�������BigDecimal���ͱ���Ϊnull�ģ��Զ�ת��Ϊ0����������ģ�����null*/
            if (StringUtils.isEmpty(workorderItemShowTemp.getUnit())) {
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
                workorderItemService.setAfterThisOrderidSubOneByNode(workorderItemShowDel);
            }else{
                if(!subMitActionPreCheck()){
                    return ;
                }
                /*itemUnitConstruct��grade,orderid,parentpkid*/
                if(!blurStrNoToGradeAndOrderidAction()){
                    return ;
                }
                if(strSubmitType .equals("Upd")) {
                    workorderItemService.updateRecord(workorderItemShowUpd) ;
                }
                else if(strSubmitType .equals("Add")) {
                     WorkorderItem workorderItemTemp = workorderItemService.fromModelShowToModel(workorderItemShowAdd);
                    if (workorderItemService.isExistSameRecordInDb(workorderItemTemp)){
                        MessageUtil.addInfo("�ñ�Ŷ�Ӧ��¼�Ѵ��ڣ�������¼�롣");
                        return;
                    }
                    workorderItemService.setAfterThisOrderidPlusOneByNode(workorderItemShowAdd);
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
    private List<WorkorderItem> getEsCttItemListByParentPkid(String strLevelParentPkid,
             List<WorkorderItem> workorderItemListPara) {
        List<WorkorderItem> tempWorkorderItemList =new ArrayList<WorkorderItem>();
        /*�ܿ��ظ��������ݿ�*/
        for(WorkorderItem itemUnit: workorderItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempWorkorderItemList.add(itemUnit);
            }
        }
        return tempWorkorderItemList;
    }
    /*���ܰ���ͬ�б��и��ݱ���ҵ���*/
    private WorkorderItemShow getEsCttItemByStrNo(
             String strNo,
             List<WorkorderItemShow> workorderItemShowListPara){
        WorkorderItemShow workorderItemShowTemp =null;
        try{
            for(WorkorderItemShow itemUnit: workorderItemShowListPara){
                if(ToolUtil.getIgnoreSpaceOfStr(itemUnit.getStrNo()).equals(strNo)) {
                    workorderItemShowTemp =(WorkorderItemShow)BeanUtils.cloneBean(itemUnit);
                    break;
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return workorderItemShowTemp;
    }
    private Boolean strNoBlurFalse(){
        workorderItemShowSel.setPkid("") ;
        workorderItemShowSel.setParentPkid("");
        workorderItemShowSel.setGrade(null);
        workorderItemShowSel.setOrderid(null);
        return false;
    }
    private boolean checkPreMng(WorkorderInfo workorderInfoPara) {
        if (StringUtils.isEmpty(workorderInfoPara.getId())) {
            return false;
        } else if (StringUtils.isEmpty(workorderInfoPara.getName())) {
            return false;
        } else if (StringUtils.isEmpty(workorderInfoPara.getSignDate())) {
            return false;
        }
        return true;
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
            workorderInfo.setAttachment(sbTemp.toString());
            workorderInfoService.updateRecord(workorderInfo);
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
            workorderInfo.setAttachment(sb.toString());
            workorderInfoService.updateRecord(workorderInfo);
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
    public WorkorderItemService getWorkorderItemService() {
        return workorderItemService;
    }
    public void setWorkorderItemService(WorkorderItemService workorderItemService) {
        this.workorderItemService = workorderItemService;
    }
    public EsCommon getEsCommon() {
        return esCommon;
    }
    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }
    public WorkorderItemShow getWorkorderItemShowSel() {
        return workorderItemShowSel;
    }
    public void setWorkorderItemShowSel(WorkorderItemShow workorderItemShowSel) {
        this.workorderItemShowSel = workorderItemShowSel;
    }
    public List<WorkorderItemShow> getWorkorderItemShowList() {
        return workorderItemShowList;
    }
    public void setWorkorderItemShowList(List<WorkorderItemShow> workorderItemShowList) {
        this.workorderItemShowList = workorderItemShowList;
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
    public WorkorderItemShow getWorkorderItemShowAdd() {
        return workorderItemShowAdd;
    }
    public void setWorkorderItemShowAdd(WorkorderItemShow workorderItemShowAdd) {
        this.workorderItemShowAdd = workorderItemShowAdd;
    }
    public WorkorderItemShow getWorkorderItemShowDel() {
        return workorderItemShowDel;
    }
    public void setWorkorderItemShowDel(WorkorderItemShow workorderItemShowDel) {
        this.workorderItemShowDel = workorderItemShowDel;
    }
    public WorkorderItemShow getWorkorderItemShowUpd() {
        return workorderItemShowUpd;
    }
    public void setWorkorderItemShowUpd(WorkorderItemShow workorderItemShowUpd) {
        this.workorderItemShowUpd = workorderItemShowUpd;
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
    public WorkorderInfo getWorkorderInfo() {
        return workorderInfo;
    }
    public WorkorderInfoService getWorkorderInfoService() {
        return workorderInfoService;
    }
    public void setWorkorderInfoService(WorkorderInfoService workorderInfoService) {
        this.workorderInfoService = workorderInfoService;
    }
    public List<WorkorderItemShow> getWorkorderItemShowListExcel() {
        return workorderItemShowListExcel;
    }
    public void setWorkorderItemShowListExcel(List<WorkorderItemShow> workorderItemShowListExcel) {
        this.workorderItemShowListExcel = workorderItemShowListExcel;
    }
    public void setWorkorderInfo(WorkorderInfo workorderInfo) {
        this.workorderInfo = workorderInfo;
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