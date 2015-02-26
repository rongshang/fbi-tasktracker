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
import task.repository.model.*;
import task.repository.model.model_show.WorkorderItemShow;
import task.service.*;
import task.view.flow.EsCommon;
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

    /*������*/
    private String strWorkorderInfoPkid;

    /*�ύ����*/
    private String strSubmitType;

    //��ʾ�Ŀ���
    private String strNotPassToStatus;
    private String strFlowType;
    private List<WorkorderItemShow> workorderItemShowListExcel;
    private Map beansMap;

    @PostConstruct
    public void init() {
        try {
            Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            beansMap = new HashMap();
            if (parammap.containsKey("strWorkorderInfoPkid")) {
                strWorkorderInfoPkid = parammap.get("strWorkorderInfoPkid").toString();
                workorderInfo = workorderInfoService.getCttInfoByPkId(strWorkorderInfoPkid);
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
            if(ToolUtil.getStrIgnoreNull(strWorkorderInfoPkid).length()!=0) {
                // ������¼���List
                attachmentList=ToolUtil.getListAttachmentByStrAttachment(workorderInfo.getAttachment());
                // ���Excel��ͷ
                beansMap.put("cttInfo", workorderInfo);
                //workorderItemList = workorderItemService.getEsItemList(strWorkorderInfoPkid);
                // Excel�����γ�
                workorderItemShowListExcel = new ArrayList<>();
                for (WorkorderItemShow itemUnit : workorderItemShowList) {
                    WorkorderItemShow itemUnitTemp = (WorkorderItemShow) BeanUtils.cloneBean(itemUnit);
                    itemUnitTemp.setId(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getId()));
                    workorderItemShowListExcel.add(itemUnitTemp);
                }
                beansMap.put("cttItemShowListExcel", workorderItemShowListExcel);
            }
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
            MessageUtil.addError("��ʼ��ʧ��");
        }
    }

    /*����*/
    public void resetAction() {
        workorderItemShowSel = new WorkorderItemShow(strWorkorderInfoPkid);
        workorderItemShowAdd = new WorkorderItemShow(strWorkorderInfoPkid);
        workorderItemShowUpd = new WorkorderItemShow(strWorkorderInfoPkid);
        workorderItemShowDel = new WorkorderItemShow(strWorkorderInfoPkid);
    }
    public void initForAdd(){
        strSubmitType="Add";
        workorderItemShowAdd = new WorkorderItemShow();
        workorderItemShowAdd.setInfoPkid(strWorkorderInfoPkid);
        workorderItemShowAdd.setLevelidx(workorderItemService.getMaxLevelidxPlusOne(strWorkorderInfoPkid));
    }
    /*�ҵ����¼�*/
    public void selectRecordAction(String strSubmitTypePara, WorkorderItemShow workorderItemShowPara) {
        try {
            strSubmitType = strSubmitTypePara;
            if (strSubmitTypePara.equals("Sel")) {
                workorderItemShowSel = (WorkorderItemShow) BeanUtils.cloneBean(workorderItemShowPara);
            } else  if (strSubmitTypePara.equals("Upd")) {
                workorderItemShowUpd = (WorkorderItemShow) BeanUtils.cloneBean(workorderItemShowPara);
            } else if (strSubmitTypePara.equals("Del")) {
                workorderItemShowDel = (WorkorderItemShow) BeanUtils.cloneBean(workorderItemShowPara);
            }
        } catch (Exception e) {
            logger.error("ѡ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*�ύǰ�ļ�飺�����������*/
    private Boolean subMitActionPreCheck() {
        WorkorderItemShow workorderItemShowTemp = new WorkorderItemShow(strWorkorderInfoPkid);
        if (strSubmitType.equals("Add")) {
            workorderItemShowTemp = workorderItemShowAdd;
        }
        if (strSubmitType.equals("Upd")) {
            workorderItemShowTemp = workorderItemShowUpd;
        }
        if (StringUtils.isEmpty(workorderItemShowTemp.getId())) {
            MessageUtil.addError("�������ţ�");
            return false;
        }
        return true;
    }
    public void submitThisRecordAction(){
        try{
            /*�ύǰ�ļ��*/
            if(strSubmitType .equals("Del")) {
                workorderItemService.deleteRecordByPkid(workorderItemShowDel.getPkid()) ;
            }else{
                if(!subMitActionPreCheck()){
                    return ;
                }
                if(strSubmitType .equals("Upd")) {
                    workorderItemService.updateRecord(workorderItemShowUpd) ;
                }
                else if(strSubmitType .equals("Add")) {
                    workorderItemService.insertRecord(workorderItemShowAdd);
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
                if(ToolUtil.getIgnoreSpaceOfStr(itemUnit.getId()).equals(strNo)) {
                    workorderItemShowTemp =(WorkorderItemShow)BeanUtils.cloneBean(itemUnit);
                    break;
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return workorderItemShowTemp;
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
    /**
     * ����Ȩ�޽������
     *
     * @param strPowerTypePara
     */
    public void onClickForPowerAction(String strPowerTypePara) {
        try {
            strPowerTypePara=strFlowType+strPowerTypePara;
                if (strPowerTypePara.equals("MngPass")) {

                    //cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("����¼����ɣ�");
                } else if (strPowerTypePara.equals("MngFail")) {

                    //cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("����¼��δ�꣡");
                }

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
        return strWorkorderInfoPkid;
    }
    public void setStrBelongToPkid(String strWorkorderInfoPkid) {
        this.strWorkorderInfoPkid = strWorkorderInfoPkid;
    }
    public String getStrSubmitType() {
        return strSubmitType;
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
/*�����ֶ�End*/
}