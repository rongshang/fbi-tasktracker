package task.view.workorder;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */
import task.common.enums.EnumInputFinishFlag;
import task.repository.model.model_show.AttachmentModel;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import skyline.util.JxlsManager;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;
import task.repository.model.*;
import task.repository.model.model_show.WorkorderInfoShow;
import task.repository.model.model_show.WorkorderItemShow;
import task.service.*;
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

    private WorkorderInfoShow workorderInfoShow;
    private WorkorderItemShow workorderItemShowSel;
    private WorkorderItemShow workorderItemShowAdd;
    private WorkorderItemShow workorderItemShowUpd;
    private WorkorderItemShow workorderItemShowDel;
    private List<WorkorderItemShow> workorderItemShowList;

    //附件
    private List<AttachmentModel> attachmentList;
    private HtmlGraphicImage image;
    //上传下载文件
    private StreamedContent downloadFile;

    /*所属号*/
    private String strWorkorderInfoPkid;

    /*提交类型*/
    private String strSubmitType;

    //显示的控制
    private List<WorkorderItemShow> workorderItemShowListExcel;
    private Map beansMap;

    @PostConstruct
    public void init() {
        try {
            Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            beansMap = new HashMap();
            if (parammap.containsKey("strWorkorderInfoPkid")) {
                strWorkorderInfoPkid = parammap.get("strWorkorderInfoPkid").toString();
                workorderInfoShow = workorderInfoService.getWorkorderInfoShowByPkId(strWorkorderInfoPkid);
                resetAction();
                initData();
            }
        }catch (Exception e){
            logger.error("初始化失败", e);
        }
    }

    /*初始化操作*/
    private void initData() {
        /*形成关系树*/
        try {
            workorderItemShowList =new ArrayList<>();
            attachmentList=new ArrayList<>();

            /*初始化流程状态列表*/
            if(ToolUtil.getStrIgnoreNull(strWorkorderInfoPkid).length()!=0) {
                // 输出Excel表头
                beansMap.put("workorderInfoShow", workorderInfoShow);
                // 附件记录变成List
                attachmentList=ToolUtil.getListAttachmentByStrAttachment(workorderInfoShow.getAttachment());
                workorderItemShowList = workorderItemService.getWorkorderItemListByInfoPkid(strWorkorderInfoPkid);
                // Excel报表形成
                workorderItemShowListExcel = new ArrayList<>();
                for (WorkorderItemShow itemUnit : workorderItemShowList) {
                    WorkorderItemShow itemUnitTemp = (WorkorderItemShow) BeanUtils.cloneBean(itemUnit);
                    itemUnitTemp.setId(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getId()));
                    workorderItemShowListExcel.add(itemUnitTemp);
                }
                beansMap.put("workorderItemShowListExcel", workorderItemShowListExcel);
            }
        }catch (Exception e){
            logger.error("初始化失败", e);
            MessageUtil.addError("初始化失败");
        }
    }

    /*重置*/
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
        workorderItemShowAdd.setLevelidx(workorderItemService.getMaxLevelidxPlusOne(strWorkorderInfoPkid).toString());
    }
    /*右单击事件*/
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
            logger.error("选择数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*提交前的检查：必须项的输入*/
    private Boolean subMitActionPreCheck(WorkorderItemShow workorderItemShowPara) {
        if (StringUtils.isEmpty(workorderItemShowPara.getId())) {
            MessageUtil.addError("请输入编号！");
            return false;
        }else if (workorderItemShowPara.getLevelidx().equals("")) {
            MessageUtil.addError("请输入顺序号！");
            return false;
        }
        return true;
    }
    public void submitThisRecordAction(){
        try{
            /*提交前的检查*/
            if(strSubmitType .equals("Add")) {
                if(!subMitActionPreCheck(workorderItemShowAdd)){
                    return;
                }
                workorderItemService.insertRecord(workorderItemShowAdd);
                initForAdd();
            }else if(strSubmitType .equals("Upd")) {
                if(!subMitActionPreCheck(workorderItemShowUpd)) {
                    return;
                }
                workorderItemService.updateRecord(workorderItemShowUpd);
            }else if(strSubmitType .equals("Del")) {
                workorderItemService.deleteRecordByPkid(workorderItemShowDel.getPkid()) ;
            }

            switch (strSubmitType){
                case "Add" : MessageUtil.addInfo("增加数据完成。");
                    break;
                case "Upd" : MessageUtil.addInfo("更新数据完成。");
                    break;
                case "Del" : MessageUtil.addInfo("删除数据完成。");
            }
            initData();
        } catch (Exception e) {
            switch (strSubmitType){
                case "Add" : MessageUtil.addError("增加数据失败，"+ e.getMessage());
                    break;
                case "Upd" : MessageUtil.addError("更新数据失败，"+ e.getMessage());
                    break;
                case "Del" : MessageUtil.addError("删除数据失败，"+ e.getMessage());
            }
        }
    }
    /**
     * 根据权限进行审核
     *
     * @param strPowerTypePara
     */
    public void onClickForWorkorderFinish(String strPowerTypePara) {
        try {
            if (strPowerTypePara.equals("MngPass")) {
                workorderInfoShow.setFinishFlag(EnumInputFinishFlag.INPUT_FINISH_FLAG1.getCode());
                String strResult= workorderInfoService.updateRecord(workorderInfoShow);
                if(("0").equals(strResult)){
                    MessageUtil.addInfo("工单录入完成！");
                }else if(("1").equals(strResult)){
                    MessageUtil.addError("当前数据已被更新，请重新取得！");
                }
            } else if (strPowerTypePara.equals("MngFail")) {
                workorderInfoShow.setFinishFlag(EnumInputFinishFlag.INPUT_FINISH_FLAG0.getCode());
                String strResult= workorderInfoService.updateRecord(workorderInfoShow);
                if(("0").equals(strResult)){
                    MessageUtil.addInfo("工单重新录入！");
                }else if(("1").equals(strResult)){
                    MessageUtil.addError("当前数据已被更新，请重新取得！");
                }
            }
        } catch (Exception e) {
            logger.error("工单录入完成操作失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void onExportExcel()throws IOException, WriteException {
        String excelFilename = "工单-" + workorderInfoShow.getSignDate() + ".xls";
        JxlsManager jxls = new JxlsManager();
        jxls.exportList(excelFilename, beansMap,"workorderItem.xls");
    }

    // 附件
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
            workorderInfoShow.setAttachment(sbTemp.toString());
            workorderInfoService.updateRecord(workorderInfoShow);
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void download(String strAttachment){
        try{
            if(StringUtils .isEmpty(strAttachment) ){
                MessageUtil.addError("路径为空，无法下载！");
                logger.error("路径为空，无法下载！");
            }
            else {
                String fileName=FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload")+"/"+strAttachment;
                File file = new File(fileName);
                InputStream stream = new FileInputStream(fileName);
                downloadFile = new DefaultStreamedContent(stream, new MimetypesFileTypeMap().getContentType(file), new String(strAttachment.getBytes("gbk"),"iso8859-1"));
            }
        } catch (Exception e) {
            logger.error("下载文件失败", e);
            MessageUtil.addError("下载文件失败,"+e.getMessage()+strAttachment);
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
                    MessageUtil.addError("附件已存在！");
                    return;
                }
            }

            attachmentList.add(attachmentModel);

            StringBuffer sb = new StringBuffer();
            for (AttachmentModel item : attachmentList) {
                sb.append(item.getCOLUMN_NAME() + ";");
            }
            if(sb.length()>4000){
                MessageUtil.addError("附件路径("+sb.toString()+")长度已超过最大允许值4000，不能入库，请联系系统管理员！");
                return;
            }
            workorderInfoShow.setAttachment(sb.toString());
            workorderInfoService.updateRecord(workorderInfoShow);
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

    /*智能字段Start*/
    public WorkorderItemService getWorkorderItemService() {
        return workorderItemService;
    }
    public void setWorkorderItemService(WorkorderItemService workorderItemService) {
        this.workorderItemService = workorderItemService;
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

    public WorkorderInfoShow getWorkorderInfoShow() {
        return workorderInfoShow;
    }

    public void setWorkorderInfoShow(WorkorderInfoShow workorderInfoShow) {
        this.workorderInfoShow = workorderInfoShow;
    }

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map beansMap) {
        this.beansMap = beansMap;
    }

    //文件
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
/*智能字段End*/
}