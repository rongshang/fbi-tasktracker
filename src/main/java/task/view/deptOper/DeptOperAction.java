package task.view.deptOper;

import skyline.security.DESHelper;
import task.repository.model.Dept;
import task.repository.model.Oper;
import task.repository.model.RsTidKeys;
import task.repository.model.not_mybatis.DeptOperShow;
import task.service.DeptOperService;
import jxl.write.WriteException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.security.MD5Helper;
import skyline.util.JxlsManager;
import skyline.util.MessageUtil;
import task.service.RsTidKeysService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by XIANGYANG on 2014/8/11.
 */
@ManagedBean
@ViewScoped
public class DeptOperAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(DeptOperAction.class);
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;
    @ManagedProperty(value = "#{rsTidKeysService}")
    private RsTidKeysService rsTidKeysService;

    private TreeNode deptOperRoot;
    private TreeNode currentSelectedNode;
    private String strSubmitType;
    private Dept deptAdd;
    private Dept deptUpd;
    private Dept deptDel;
    private Oper operAdd;
    private Oper operUpd;
    private Oper operDel;
    private List<SelectItem> deptSIList;
    private List<SelectItem> operSexSIList;
    private List<SelectItem> operIsSuperSIList;
    private List<SelectItem> enableSIList;
    private List<SelectItem> operTypeSIList;
    private String strConfirmPasswd;
    private List<DeptOperShow> deptOperShowFowExcelList;
    private Map beansMap;
    private String strPasswd;

    @PostConstruct
    public void init() {
        try {
            deptOperShowFowExcelList=new ArrayList<>();
            beansMap = new HashMap();
            initVariables();
            initData();
            beansMap.put("deptOperShowFowExcelList", deptOperShowFowExcelList);
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
        }
    }

    private void initVariables() {
        deptAdd = new Dept();
        deptUpd = new Dept();
        deptDel = new Dept();
        operAdd = new Oper();
        operUpd = new Oper();
        operDel = new Oper();
    }

    private void initData() {
        try {
            operSexSIList = new ArrayList<>();
            operSexSIList.add(new SelectItem("1", "��"));
            operSexSIList.add(new SelectItem("0", "Ů"));
            operIsSuperSIList = new ArrayList<>();
            operIsSuperSIList.add(new SelectItem("0", "��"));
            operIsSuperSIList.add(new SelectItem("1", "��"));
            enableSIList= new ArrayList<>();
            enableSIList.add(new SelectItem("1", "����"));
            enableSIList.add(new SelectItem("0", "������"));
            operTypeSIList= new ArrayList<>();
            operTypeSIList.add(new SelectItem("2", "ҵ����Ա"));
            operTypeSIList.add(new SelectItem("1", "ϵͳ����Ա"));
            deptSIList=new ArrayList<>();
            List<Dept> deptListTemp=deptOperService.getDeptList();
            for(Dept dept:deptListTemp){
                deptSIList.add(new SelectItem(dept.getPkid(),dept.getName()));
            }
            initDeptOper();
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
            MessageUtil.addError("��ʼ��ʧ��");
        }
    }

    private void initDeptOper() {
        deptOperRoot = new DefaultTreeNode("ROOT", null);
        DeptOperShow deptOperShowTemp = new DeptOperShow();
        deptOperShowTemp.setPkid("ROOT");
        deptOperShowTemp.setId("ROOT");
        deptOperShowTemp.setName("������Ա��Ϣ");
        deptOperShowTemp.setType("0");
        TreeNode node0 = new DefaultTreeNode(deptOperShowTemp, deptOperRoot);
        try {
            recursiveTreeNode("ROOT", node0);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        node0.setExpanded(true);
    }

    public void setMaxDeptIdPlusOne(String strMngTypePara){
        try {
            if("Add".equals(strMngTypePara)) {
                deptAdd.setId(deptOperService.getStrMaxOperId());
            }else if("Upd".equals(strMngTypePara)) {
                deptUpd.setId(deptOperService.getStrMaxOperId());
            }
        } catch (Exception e) {
            logger.error("ȡ������ź�ʧ��", e);
            MessageUtil.addError("ȡ������ź�ʧ�ܡ�");
        }
    }
    public void setMaxOperIdPlusOne(String strMngTypePara){
        try {
            if("Add".equals(strMngTypePara)) {
                operAdd.setId(deptOperService.getStrMaxOperId());
            }else if("Upd".equals(strMngTypePara)) {
                operUpd.setId(deptOperService.getStrMaxOperId());
            }
        } catch (Exception e) {
            logger.error("ȡ������û���ʧ��", e);
            MessageUtil.addError("ȡ������û���ʧ�ܡ�");
        }
    }

    private void recursiveTreeNode(String strParentPkidPara, TreeNode parentNode)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<DeptOperShow> deptOperShowTempList= deptOperService.selectDeptAndOperRecords(strParentPkidPara);
        for (int i = 0; i < deptOperShowTempList.size(); i++) {
            TreeNode childNodeTemp = new DefaultTreeNode(deptOperShowTempList.get(i), parentNode);
            DeptOperShow deptOperShowForExcelTemp= (DeptOperShow)BeanUtils.cloneBean(deptOperShowTempList.get(i));
            DeptOperShow deptOperShowForExcelTemp2=new DeptOperShow();
            if(("0").equals(deptOperShowForExcelTemp.getType())){
                deptOperShowForExcelTemp2.setDeptId(deptOperShowForExcelTemp.getId());
                deptOperShowForExcelTemp2.setDeptName(deptOperShowForExcelTemp.getName());
            }else{
                deptOperShowForExcelTemp2.setOperId(deptOperShowForExcelTemp.getId());
                deptOperShowForExcelTemp2.setOperName(deptOperShowForExcelTemp.getName());
            }
            deptOperShowFowExcelList.add(deptOperShowForExcelTemp2);
            if (currentSelectedNode!=null){
                DeptOperShow deptOperShow1= (DeptOperShow) currentSelectedNode.getData();
                DeptOperShow deptOperShow2= (DeptOperShow) childNodeTemp.getData();
                if ("ROOT".equals(deptOperShow1.getPkid())){
                    currentSelectedNode.setExpanded(true);
                }else {
                    if (deptOperShow1.getPkid().equals(deptOperShow2.getPkid())){
                        TreeNode treeNodeTemp=childNodeTemp;
                        while (!(treeNodeTemp.getData().equals("ROOT"))){
                            treeNodeTemp.setExpanded(true);
                            treeNodeTemp=treeNodeTemp.getParent();
                        }
                    }
                }
            }
            recursiveTreeNode(deptOperShowForExcelTemp.getPkid(), childNodeTemp);
        }
    }

    public void recursiveDeptOperTreeNode(TreeNode treeNodePara){
        treeNodePara.setExpanded(false);
        if (treeNodePara.getChildCount()!=0){
            for (int i=0;i<treeNodePara.getChildCount();i++){
                recursiveDeptOperTreeNode(treeNodePara.getChildren().get(i));
            }
        }
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        recursiveDeptOperTreeNode(event.getTreeNode());
    }

    public void findSelectedNode(DeptOperShow deptOperShowPara, TreeNode treeNodePara,String strSubmitTypePara) {
        if (treeNodePara.getChildCount() != 0) {
            for (int i = 0; i < treeNodePara.getChildCount(); i++) {
                TreeNode treeNodeTemp = treeNodePara.getChildren().get(i);
                if (deptOperShowPara == treeNodeTemp.getData()) {
                    if (strSubmitTypePara.contains("Add")){
                        currentSelectedNode = treeNodeTemp;
                    }else if (strSubmitTypePara.contains("Upd")||strSubmitTypePara.contains("Del")){
                        currentSelectedNode=treeNodeTemp.getParent();
                    }
                    return;
                }
                findSelectedNode(deptOperShowPara, treeNodeTemp,strSubmitTypePara);
            }
        }
    }
    public void selectRecordAction(String strSubmitTypePara,
                                     DeptOperShow deptOperShowPara) {
        try {
            findSelectedNode(deptOperShowPara,deptOperRoot,strSubmitTypePara);
            strSubmitType = strSubmitTypePara;
            if (strSubmitTypePara.contains("Dept")) {
                if (strSubmitTypePara.contains("Add")) {
                    deptAdd = new Dept();
                    deptAdd.setId(deptOperService.getStrMaxDeptId());
                    deptAdd.setParentpkid(deptOperShowPara.getPkid());
                } else {
                    if (strSubmitTypePara.contains("Upd")) {
                        deptUpd = new Dept();
                        deptUpd = (Dept) deptOperService.selectRecordByPkid(deptOperShowPara);
                    } else if (strSubmitTypePara.contains("Del")) {
                        deptDel = new Dept();
                        deptDel = (Dept) deptOperService.selectRecordByPkid(deptOperShowPara);
                    }
                }
            } else if (strSubmitTypePara.contains("Oper")) {
                if (strSubmitTypePara.contains("Add")){
                    operAdd = new Oper();
                    operAdd.setId(deptOperService.getStrMaxOperId());
                    operAdd.setDeptPkid(deptOperShowPara.getPkid());
                }else if (strSubmitTypePara.contains("Upd")) {
                    operUpd = new Oper();
                    operUpd = (Oper) deptOperService.selectRecordByPkid(deptOperShowPara);
                    strPasswd=operUpd.getPasswd();
                } else if (strSubmitTypePara.contains("Del")) {
                    operDel = new Oper();
                    operDel = (Oper) deptOperService.selectRecordByPkid(deptOperShowPara);
                }
            }
        } catch (Exception e) {
            logger.error("��ʼ����Ϣʧ�ܡ�", e);
            MessageUtil.addError("��ʼ����Ϣʧ�ܡ�");
        }
    }

    public void onClickForMngAction() {
        try {
            if (strSubmitType.contains("Dept")) {
                if (strSubmitType.contains("Add")) {
                    if (!submitDeptPreCheck(deptAdd)) {
                        return;
                    }
                    if (deptOperService.isExistInDeptDb(deptAdd)) {
                        MessageUtil.addError("�ñ�Ż����Ѵ��ڣ�������¼�룡");
                        return;
                    }
                    deptOperService.insertDeptRecord(deptAdd);
                } else if (strSubmitType.contains("Upd")) {
                    if (!submitDeptPreCheck(deptUpd)) {
                        return;
                    }
                    deptOperService.updateDeptRecord(deptUpd);
                } else if (strSubmitType.contains("Del")) {
                    if (deptOperService.findChildRecordsByPkid(deptDel.getPkid())) {
                        MessageUtil.addInfo("�ò������з�֧������Ա�����޷�ɾ����");
                        return;
                    }
                    deptOperService.deleteDeptRecord(deptDel);
                }
            } else if (strSubmitType.contains("Oper")) {
                if (strSubmitType.contains("Add")) {
                    if (!submitOperPreCheck(operAdd)) {
                        return;
                    }
                    //String md5=MD5Helper.getMD5String(tidkeysService.getTidkeysList("126").getKey());
                    RsTidKeys rsTidKeysPara=new RsTidKeys();
                    rsTidKeysPara.setTid("126");
                    List<RsTidKeys> rsTidKeysList=rsTidKeysService.getRsTidKeysList(rsTidKeysPara);
                    if(rsTidKeysList.size()>0){
                        RsTidKeys rsTidKeysTemp=rsTidKeysList.get(0);
                        DESHelper dESHelper = new DESHelper(rsTidKeysTemp.getEsKey());
                        String strOperCounts=dESHelper.decrypt(rsTidKeysTemp.getOperCounts());
                        int intUsersCounts=Integer.parseInt(strOperCounts);
                        Oper operTemp=new Oper();
                        int intExistRecordCountsInOperDb=deptOperService.existRecordCountsInOperDb(operTemp);
                        if (intExistRecordCountsInOperDb>=intUsersCounts) {
                            MessageUtil.addError("ϵͳ�����û���["+intUsersCounts+"]��ʵ���û���["
                                    +intExistRecordCountsInOperDb+"],�����޷���������û���");
                            return;
                        }
                    }

                    if (deptOperService.existRecordCountsInOperDb(operAdd)>0) {
                        MessageUtil.addError("�ñ���û��Ѵ��ڣ�������¼�룡");
                        return;
                    }
                    operAdd.setPasswd(MD5Helper.getMD5String(operAdd.getPasswd()));
                    operAdd.setTid("126");
                    deptOperService.insertOperRecord(operAdd);
                } else if (strSubmitType.contains("Upd")) {
                    if (!submitOperPreCheck(operUpd)) {
                        return;
                    }
                    if(!strPasswd.equals(operUpd.getPasswd())) {
                        operUpd.setPasswd(MD5Helper.getMD5String(operUpd.getPasswd()));
                    }
                    operUpd.setTid("126");
                    deptOperService.updateOperRecord(operUpd);
                } else if (strSubmitType.contains("Del")) {
                    deptOperService.deleteOperRecord(operDel);
                }
            }
            initVariables();
            initData();
            MessageUtil.addInfo("���ݴ���ɹ���");
        }catch (Exception e){
            logger.error("���ݴ���ʧ�ܡ�", e);
            MessageUtil.addError("���ݴ���ʧ�ܡ�");
        }
    }

    private boolean submitDeptPreCheck(Dept deptPara) {
        if (StringUtils.isEmpty(deptPara.getName())) {
            MessageUtil.addInfo("�����벿�����ƣ�");
            return false;
        }
        if (StringUtils.isEmpty(deptPara.getId())) {
            MessageUtil.addInfo("�����벿�ű�ţ�");
            return false;
        }
        return true;
    }
    private boolean submitOperPreCheck(Oper operPara) {
        if (StringUtils.isEmpty(operPara.getId())) {
            MessageUtil.addInfo("���������Ա��ţ�");
            return false;
        }
        if (StringUtils.isEmpty(operPara.getName())) {
            MessageUtil.addInfo("���������Ա���ƣ�");
            return false;
        }
        if (StringUtils.isEmpty(operPara.getPasswd())) {
            MessageUtil.addInfo("���������Ա���룡");
            return false;
        }
        if (!(StringUtils.isEmpty(operPara.getFile().getFileName()))) {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/operPicture");
            File file = new File(path+"/"+operPara.getFile().getFileName());
            if (file.exists()) {
                MessageUtil.addInfo("�ļ��Ѵ��ڣ����������ļ���");
                return false;
            }
        }
        return true;
    }
    public String onExportExcel()throws IOException, WriteException {
        if (this.deptOperShowFowExcelList.size() == 0) {
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "������Ա��Ϣ��-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"deptOper.xls");
            // ����״̬��Ʊ����Ҫ���ʱ���޸ĵ����ļ���
        }
        return null;
    }
    /*�����ֶ� Start*/

    public TreeNode getDeptOperRoot() {
        return deptOperRoot;
    }

    public void setDeptOperRoot(TreeNode deptOperRoot) {
        this.deptOperRoot = deptOperRoot;
    }

    public DeptOperService getDeptOperService() {
        return deptOperService;
    }

    public void setDeptOperService(DeptOperService deptOperService) {
        this.deptOperService = deptOperService;
    }

    public Dept getDeptAdd() {
        return deptAdd;
    }

    public void setDeptAdd(Dept deptAdd) {
        this.deptAdd = deptAdd;
    }

    public Dept getDeptUpd() {
        return deptUpd;
    }

    public void setDeptUpd(Dept deptUpd) {
        this.deptUpd = deptUpd;
    }

    public Dept getDeptDel() {
        return deptDel;
    }

    public void setDeptDel(Dept deptDel) {
        this.deptDel = deptDel;
    }

    public Oper getOperAdd() {
        return operAdd;
    }

    public void setOperAdd(Oper operAdd) {
        this.operAdd = operAdd;
    }

    public Oper getOperUpd() {
        return operUpd;
    }

    public void setOperUpd(Oper operUpd) {
        this.operUpd = operUpd;
    }

    public Oper getOperDel() {
        return operDel;
    }

    public void setOperDel(Oper operDel) {
        this.operDel = operDel;
    }

    public List<SelectItem> getDeptSIList() {
        return deptSIList;
    }

    public void setDeptSIList(List<SelectItem> deptSIList) {
        this.deptSIList = deptSIList;
    }

    public List<SelectItem> getOperSexSIList() {
        return operSexSIList;
    }

    public void setOperSexSIList(List<SelectItem> operSexSIList) {
        this.operSexSIList = operSexSIList;
    }

    public List<SelectItem> getOperIsSuperSIList() {
        return operIsSuperSIList;
    }

    public void setOperIsSuperSIList(List<SelectItem> operIsSuperSIList) {
        this.operIsSuperSIList = operIsSuperSIList;
    }

    public List<SelectItem> getEnableSIList() {
        return enableSIList;
    }

    public void setEnableSIList(List<SelectItem> enableSIList) {
        this.enableSIList = enableSIList;
    }

    public List<SelectItem> getOperTypeSIList() {
        return operTypeSIList;
    }

    public void setOperTypeSIList(List<SelectItem> operTypeSIList) {
        this.operTypeSIList = operTypeSIList;
    }

    public String getStrConfirmPasswd() {
        return strConfirmPasswd;
    }

    public void setStrConfirmPasswd(String strConfirmPasswd) {
        this.strConfirmPasswd = strConfirmPasswd;
    }

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map beansMap) {
        this.beansMap = beansMap;
    }

    public RsTidKeysService getRsTidKeysService() {
        return rsTidKeysService;
    }

    public void setRsTidKeysService(RsTidKeysService rsTidKeysService) {
        this.rsTidKeysService = rsTidKeysService;
    }
}
