package task.service;

import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import task.repository.dao.DeptMapper;
import task.repository.dao.OperMapper;
import task.repository.dao.not_mybatis.MyDeptAndOperMapper;
import task.repository.model.Dept;
import task.repository.model.DeptExample;
import task.repository.model.Oper;
import task.repository.model.OperExample;
import task.repository.model.not_mybatis.DeptOperShow;
import org.primefaces.model.UploadedFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import skyline.util.ToolUtil;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import java.io.*;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@Service
public class DeptOperService {
    @Resource
    private MyDeptAndOperMapper myDeptAndOperMapper;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private OperMapper operMapper;

    public TreeNode getDeptOperTreeNode(DeptOperShow deptOperShowPara){
        TreeNode deptOperShowRoot = new DefaultTreeNode(deptOperShowPara, null);
        deptOperShowRoot.setExpanded(true);
        recursiveOperTreeNode(deptOperShowPara.getPkid(), deptOperShowRoot);
        return deptOperShowRoot;
    }
    private void recursiveOperTreeNode(String strParentPkidPara, TreeNode parentNode) {
        List<DeptOperShow> deptOperShowListTemp = selectDeptAndOperRecords(strParentPkidPara);
        for (DeptOperShow deptOperShowUnit : deptOperShowListTemp) {
            TreeNode childNodeTemp = new DefaultTreeNode(deptOperShowUnit, parentNode);
            recursiveOperTreeNode(deptOperShowUnit.getPkid(), childNodeTemp);
        }
    }

    public boolean findChildRecordsByPkid(String strDeptOperPkidPara) {
        DeptExample example = new DeptExample();
        example.createCriteria()
                .andParentpkidEqualTo(strDeptOperPkidPara);
        OperExample operExample=new OperExample();
        operExample.createCriteria()
                .andDeptPkidEqualTo(strDeptOperPkidPara);
        return (deptMapper.selectByExample(example).size()>0||operMapper.selectByExample(operExample).size()>0);
    }

    public TreeNode getDeptOperTreeTableNode( DeptOperShow deptOperShowPara,TreeNode currentTreeNode){
        TreeNode deptOperRoot = new DefaultTreeNode(deptOperShowPara, null);
        deptOperRoot.setExpanded(true);
        recursiveOperTreeTableNode(deptOperShowPara.getPkid(), deptOperRoot,currentTreeNode);
        return deptOperRoot;
    }

    private void recursiveOperTreeTableNode(String strParentPkidPara, TreeNode parentNode,TreeNode currentTreeNode){
        List<DeptOperShow> deptOperShowListTemp= selectDeptAndOperRecords(strParentPkidPara);
        for (DeptOperShow deptOperShowUnit:deptOperShowListTemp) {
            TreeNode childNodeTemp = new DefaultTreeNode(deptOperShowUnit, parentNode);
            if(currentTreeNode!=null) {
                if (((DeptOperShow)currentTreeNode.getData()).getPkid().equals(deptOperShowUnit.getPkid())) {
                    // 把当前节点一直遍历到根节点全部打开
                    setActiveTreeNodeExpand(childNodeTemp);
                }
            }
            recursiveOperTreeTableNode(deptOperShowUnit.getPkid(), childNodeTemp,currentTreeNode);
        }
    }

    public void setActiveTreeNodeExpand(TreeNode treeNodePara){
        if (treeNodePara!=null){
            while (!(((DeptOperShow)treeNodePara.getData()).getPkid().equals("ROOT"))){
                treeNodePara.setExpanded(true);
                treeNodePara=treeNodePara.getParent();
            }
        }
    }

    public void recursiveDeptOperTreeNodeCollapse(TreeNode treeNodePara){
        treeNodePara.setExpanded(false);
        for (TreeNode treeNodeUnit:treeNodePara.getChildren()){
            recursiveDeptOperTreeNodeCollapse(treeNodeUnit);
        }
    }

    public List<DeptOperShow> selectDeptAndOperRecords(String parentPkidPara) {
        return myDeptAndOperMapper.selectDeptAndOperRecords(parentPkidPara);
    }

    public List<Dept> getDeptList() {
        DeptExample example=new DeptExample();
        return deptMapper.selectByExample(example);
    }

    public Object selectRecordByPkid(DeptOperShow deptOperShowPara) {
        if ("0".equals(deptOperShowPara.getType())){
            return deptMapper.selectByPrimaryKey(deptOperShowPara.getPkid());
        }else {
            return operMapper.selectByPrimaryKey(deptOperShowPara.getPkid());
        }
    }

    public String getStrMaxDeptId(){
        return ToolUtil.getMaxIdPlusOne("DEPT",myDeptAndOperMapper.getStrMaxDeptId()) ;
    }
    public String getStrMaxOperId(){
        return ToolUtil.getMaxIdPlusOne("OPER",myDeptAndOperMapper.getStrMaxOperId()) ;
    }

    public boolean isExistInDeptDb(Dept deptPara) {
            DeptExample deptExample=new DeptExample();
            deptExample.createCriteria()
                    .andIdEqualTo(deptPara.getId());
            return deptMapper.selectByExample(deptExample).size()>0;
    }
    public int existRecordCountsInOperDb(Oper operPara) {
        OperExample operExample=new OperExample();
        if(ToolUtil.getStrIgnoreNull(operPara.getId()).length()>0) {
            operExample.createCriteria()
                    .andIdEqualTo(operPara.getId());
        }
        return operMapper.selectByExample(operExample).size();
    }
    public void insertDeptRecord(Dept deptPara){
        deptPara.setCreatedBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        deptPara.setCreatedTime(ToolUtil.getStrLastUpdTime());
        deptMapper.insert(deptPara);
    }
    public void insertOperRecord(Oper operPara) {
        UploadedFile uploadedFile=operPara.getFile();
        String strFileName = uploadedFile.getFileName();
        if (!(StringUtils.isEmpty(strFileName))){
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/operPicture");
            BufferedInputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                File dirFile = new File(path);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
                File file = new File(dirFile, strFileName);
                inputStream = new BufferedInputStream(uploadedFile.getInputstream());
                fileOutputStream = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int num;
                while ((num = inputStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, num);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
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
            operPara.setAttachment(strFileName);
        }
        operPara.setArchivedFlag("0");
        operPara.setCreatedBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        operPara.setCreatedTime(ToolUtil.getStrLastUpdTime());
        operMapper.insert(operPara);
    }
    public void updateDeptRecord(Dept deptPara){
        deptPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        deptPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        deptMapper.updateByPrimaryKey(deptPara);
    }
    public void updateOperRecord(Oper operPara){
        UploadedFile uploadedFile=operPara.getFile();
        if (uploadedFile!=null){
            String strFileName = uploadedFile.getFileName();
            if (!(StringUtils.isEmpty(strFileName))){
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/operPicture");
                BufferedInputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                try {
                    File dirFile = new File(path);
                    if (!dirFile.exists()) {
                        dirFile.mkdirs();
                    }
                    Oper operTemp=operMapper.selectByPrimaryKey(operPara.getPkid());
                    String strDbFileName=operTemp.getAttachment();
                    File file=null;
                    if (strDbFileName!=null){
                        file = new File(dirFile, strDbFileName);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    file = new File(dirFile, strFileName);
                    inputStream = new BufferedInputStream(uploadedFile.getInputstream());
                    fileOutputStream = new FileOutputStream(file);
                    byte[] buf = new byte[1024];
                    int num;
                    while ((num = inputStream.read(buf)) != -1) {
                        fileOutputStream.write(buf, 0, num);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
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
                operPara.setAttachment(strFileName);
            }
        }
        operPara.setArchivedFlag("0");
        operPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        operPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        operMapper.updateByPrimaryKey(operPara);
    }
    public void deleteDeptRecord(Dept deptPara){
        deptMapper.deleteByPrimaryKey(deptPara.getPkid());
    }
    public void deleteOperRecord(Oper operPara){
        String strDbFileName=operPara.getAttachment();
        if (strDbFileName!=null&&!(StringUtils.isEmpty(strDbFileName))){
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/operPicture");
            File file = new File(path+"/"+strDbFileName);
            if (file.exists()) {
                file.delete();
            }
        }
        operMapper.deleteByPrimaryKey(operPara.getPkid());
    }
    public List<Oper> getOperList(){
        OperExample example=new OperExample();
        example.setOrderByClause("NAME ASC") ;
         return operMapper.selectByExample(example);
    }
    public Oper getOperByPkid(String strPkidPara){
        return operMapper.selectByPrimaryKey(strPkidPara);
    }
    public void updateRecord(Oper operPara){
        operMapper.updateByPrimaryKey(operPara);
    }
}
