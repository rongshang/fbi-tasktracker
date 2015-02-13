package task.view.workorder;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: 下午1:53
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
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
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

    //附件
    private List<AttachmentModel> attachmentList;
    private HtmlGraphicImage image;
    //上传下载文件
    private StreamedContent downloadFile;

    /*所属类型*/
    private String strBelongToType;
    /*所属号*/
    private String strCttInfoPkid;

    /*提交类型*/
    private String strSubmitType;

    /*控制控件在画面上的可用与现实Start*/
    private StyleModel styleModelNo;
    private StyleModel styleModel;
    //显示的控制
    private String strPassVisible;
    private String strPassFailVisible;
    private String strNotPassToStatus;
    private String strFlowType;
    private List<WorkorderItemShow> workorderItemShowListExcel;
    private Map beansMap;
    // 录入备注
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
                workorderInfo = cttInfoService.getCttInfoByPkId(strCttInfoPkid);

                strPassVisible = "true";
                strPassFailVisible = "true";
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
            workorderItemList =new ArrayList<>();
            workorderItemShowList =new ArrayList<>();
            attachmentList=new ArrayList<>();
            /*初始化流程状态列表*/
            if(ToolUtil.getStrIgnoreNull(strCttInfoPkid).length()!=0) {
                // 附件记录变成List
                attachmentList=ToolUtil.getListAttachmentByStrAttachment(workorderInfo.getAttachment());
                // 输出Excel表头
                beansMap.put("cttInfo", workorderInfo);
                workorderItemList = cttItemService.getEsItemList(
                        strBelongToType, strCttInfoPkid);
                recursiveDataTable("root", workorderItemList);
                workorderItemShowList = getTkcttItemList_DoFromatNo(workorderItemShowList);
                setTkcttItemList_AddTotal();
                // Excel报表形成
                workorderItemShowListExcel = new ArrayList<>();
                for (WorkorderItemShow itemUnit : workorderItemShowList) {
                    // 合同单价，工程量，金额
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
            logger.error("初始化失败", e);
            MessageUtil.addError("初始化失败");
        }
    }

    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId, List<WorkorderItem> workorderItemListPara) {
        // 根据父层级号获得该父层级下的子节点
        List<WorkorderItem> subWorkorderItemList = new ArrayList<>();
        // 通过父层id查找它的孩子
        subWorkorderItemList = getEsCttItemListByParentPkid(strLevelParentId, workorderItemListPara);
        for (WorkorderItem itemUnit : subWorkorderItemList) {
            WorkorderItemShow workorderItemShowTemp = null;
            String strCreatedByName = cttInfoService.getUserName(itemUnit.getCreatedBy());
            String strLastUpdByName = cttInfoService.getUserName(itemUnit.getLastUpdBy());
            // 层级项
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

    /*根据group和orderid临时编制编号strNo*/
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
        // 小计
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
                    workorderItemShowTemp.setName("合计");
                    workorderItemShowTemp.setPkid("total" + i);
                    workorderItemShowTemp.setAmount(bdTotal);
                    workorderItemShowList.add(workorderItemShowTemp);
                    bdTotal = new BigDecimal(0);
                }
            } else if (i + 1 == workorderItemShowListTemp.size()) {
                WorkorderItemShow workorderItemShowTemp = new WorkorderItemShow();
                workorderItemShowTemp.setName("合计");
                workorderItemShowTemp.setPkid("total" + i);
                workorderItemShowTemp.setAmount(bdTotal);
                workorderItemShowList.add(workorderItemShowTemp);
                bdTotal = new BigDecimal(0);

                // 总合计
                workorderItemShowTemp = new WorkorderItemShow();
                workorderItemShowTemp.setName("总合计");
                workorderItemShowTemp.setPkid("total_all" + i);
                workorderItemShowTemp.setAmount(bdAllTotal);
                workorderItemShowList.add(workorderItemShowTemp);
            }
        }
    }

    /*重置*/
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

    /*右单击事件*/
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
            logger.error("选择数据失败，", e);
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
            MessageUtil.addError("请确认输入的编号，编号" + strIgnoreSpaceOfStr + "格式不正确！");
            return strNoBlurFalse();
        }

        //该编码已经存在
        if(!strSubmitType.equals("Upd")){
            if(getEsCttItemByStrNo(strIgnoreSpaceOfStr, workorderItemShowList)!=null){
            }
            else{ //该编码不存在
            }
        }
        Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");

        if (intLastIndexof < 0) {
            List<WorkorderItem> itemHieRelapListSubTemp = new ArrayList<>();
            itemHieRelapListSubTemp = getEsCttItemListByParentPkid("root", workorderItemList);

            if (itemHieRelapListSubTemp.size() == 0) {
                if (!strIgnoreSpaceOfStr.equals("1")) {
                    MessageUtil.addError("请确认输入的编号！该编号不符合规范，应输入1！");
                    return strNoBlurFalse();
                }
            } else {
                if (itemHieRelapListSubTemp.size() + 1 < Integer.parseInt(strIgnoreSpaceOfStr)) {
                    MessageUtil.addError("请确认输入的编号！该编号不符合规范，应输入" + (itemHieRelapListSubTemp.size() + 1) + "！");
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
                MessageUtil.addError("请确认输入的编号！父层" + strParentNo + "不存在！");
                return strNoBlurFalse();
            } else {
                List<WorkorderItem> itemHieRelapListSubTemp = new ArrayList<>();
                itemHieRelapListSubTemp = getEsCttItemListByParentPkid(
                        workorderItemShowTemp1.getPkid(),
                        workorderItemList);
                if (itemHieRelapListSubTemp.size() == 0) {
                    if (!workorderItemShowTemp.getStrNo().equals(strParentNo + ".1")) {
                        MessageUtil.addError("请确认输入的编号！该编号不符合规范，应输入" + strParentNo + ".1！");
                        return strNoBlurFalse();
                    }
                } else {
                    String strOrderid = strIgnoreSpaceOfStr.substring(intLastIndexof + 1);
                    if (itemHieRelapListSubTemp.size() + 1 < Integer.parseInt(strOrderid)) {
                        MessageUtil.addError("请确认输入的编号！该编号不符合规范，应输入" + strParentNo + "." +
                                (itemHieRelapListSubTemp.size() + 1) + "！");
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
    /*提交前的检查：必须项的输入*/
    private Boolean subMitActionPreCheck() {
        WorkorderItemShow workorderItemShowTemp = new WorkorderItemShow(strCttInfoPkid);
        if (strSubmitType.equals("Add")) {
            workorderItemShowTemp = workorderItemShowAdd;
        }
        if (strSubmitType.equals("Upd")) {
            workorderItemShowTemp = workorderItemShowUpd;
        }
        if (StringUtils.isEmpty(workorderItemShowTemp.getStrNo())) {
            MessageUtil.addError("请输入编号！");
            return false;
        }
        if (StringUtils.isEmpty(workorderItemShowTemp.getName())) {
            MessageUtil.addError("请输入名称！");
            return false;
        }
        if ((workorderItemShowTemp.getUnitPrice() != null &&
                workorderItemShowTemp.getUnitPrice().compareTo(BigDecimal.ZERO) != 0) ||
                (workorderItemShowTemp.getQuantity() != null &&
                        workorderItemShowTemp.getQuantity().compareTo(BigDecimal.ZERO) != 0)) {
            /*绑定前台控件,可输入的BigDecimal类型本来为null的，自动转换为0，不可输入的，还是null*/
            if (StringUtils.isEmpty(workorderItemShowTemp.getUnit())) {
                MessageUtil.addError("请输入单位！");
                return false;
            }
        }
        return true;
    }
    public void submitThisRecordAction(){
        try{
            /*提交前的检查*/
            if(strSubmitType .equals("Del")) {
                cttItemService.setAfterThisOrderidSubOneByNode(workorderItemShowDel);
            }else{
                if(!subMitActionPreCheck()){
                    return ;
                }
                /*itemUnitConstruct的grade,orderid,parentpkid*/
                if(!blurStrNoToGradeAndOrderidAction()){
                    return ;
                }
                if(strSubmitType .equals("Upd")) {
                    cttItemService.updateRecord(workorderItemShowUpd) ;
                }
                else if(strSubmitType .equals("Add")) {
                     WorkorderItem workorderItemTemp = cttItemService.fromModelShowToModel(workorderItemShowAdd);
                    if (cttItemService.isExistSameRecordInDb(workorderItemTemp)){
                        MessageUtil.addInfo("该编号对应记录已存在，请重新录入。");
                        return;
                    }
                    cttItemService.setAfterThisOrderidPlusOneByNode(workorderItemShowAdd);
                    resetAction();
                }
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

    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<WorkorderItem> getEsCttItemListByParentPkid(String strLevelParentPkid,
             List<WorkorderItem> workorderItemListPara) {
        List<WorkorderItem> tempWorkorderItemList =new ArrayList<WorkorderItem>();
        /*避开重复链接数据库*/
        for(WorkorderItem itemUnit: workorderItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempWorkorderItemList.add(itemUnit);
            }
        }
        return tempWorkorderItemList;
    }
    /*在总包合同列表中根据编号找到项*/
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
        String excelFilename = "总包合同-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
        JxlsManager jxls = new JxlsManager();
        jxls.exportList(excelFilename, beansMap,"oriTkctt.xls");
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
            workorderInfo.setAttachment(sbTemp.toString());
            cttInfoService.updateRecord(workorderInfo);
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
            workorderInfo.setAttachment(sb.toString());
            cttInfoService.updateRecord(workorderInfo);
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
    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }
    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
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
/*智能字段End*/
}
