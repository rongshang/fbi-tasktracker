package task.service;

import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import task.repository.dao.DeptMapper;
import task.repository.dao.OperMapper;
import task.repository.dao.not_mybatis.MyDeptAndOperMapper;
import task.repository.model.*;
import task.repository.model.not_mybatis.DeptOperShow;
import org.primefaces.model.UploadedFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import skyline.util.ToolUtil;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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

    List<SelectItem> selectItemList=new ArrayList<>();

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

    public boolean findChildRecordsByPkid(String strDeptOperPkidPara) {
        DeptExample example = new DeptExample();
        example.createCriteria()
                .andParentpkidEqualTo(strDeptOperPkidPara);
        OperExample operExample=new OperExample();
        operExample.createCriteria()
                .andDeptPkidEqualTo(strDeptOperPkidPara);
        return (deptMapper.selectByExample(example).size()>0||operMapper.selectByExample(operExample).size()>0);
    }
    public List<DeptOperShow> selectDeptAndOperRecords(String parentPkidPara) {
        return myDeptAndOperMapper.selectDeptAndOperRecords(parentPkidPara);
    }

    public List<Dept> getDeptListByModelShow(DeptOperShow deptOperShowPara) {
        DeptExample example=new DeptExample();
        DeptExample.Criteria criteria = example.createCriteria();
        //可以为NULL的项
        // 部门ID
        if(!ToolUtil.getStrIgnoreNull(deptOperShowPara.getDeptId()).equals("")){
            criteria.andIdLike("%"+deptOperShowPara.getDeptId()+"%");
        }
        // 部门名称
        if(!ToolUtil.getStrIgnoreNull(deptOperShowPara.getDeptName()).equals("")){
            criteria.andNameLike("%"+deptOperShowPara.getDeptName()+"%");
        }
        // 接收者Pkid
        if(!ToolUtil.getStrIgnoreNull(deptOperShowPara.getDeptParentPkid()).equals("")){
            criteria.andParentpkidEqualTo(deptOperShowPara.getDeptParentPkid());
        }
        // 备注内容
        if(!ToolUtil.getStrIgnoreNull(deptOperShowPara.getDeptRemark()).equals("")){
            criteria.andRemarkLike("%"+ deptOperShowPara.getDeptRemark()+"%");
        }
        example.setOrderByClause("NAME ASC") ;
        return deptMapper.selectByExample(example);
    }
    public List<Oper> getOperListByModelShow(DeptOperShow deptOperShowPara){
        OperExample example=new OperExample();
        OperExample.Criteria criteria = example.createCriteria();
        //可以为NULL的项
        // 部门ID
        if(!ToolUtil.getStrIgnoreNull(deptOperShowPara.getOperId()).equals("")){
            criteria.andIdLike("%"+deptOperShowPara.getOperId()+"%");
        }
        // 部门名称
        if(!ToolUtil.getStrIgnoreNull(deptOperShowPara.getOperName()).equals("")){
            criteria.andNameLike("%"+deptOperShowPara.getOperName()+"%");
        }
        // 接收者Pkid
        if(!ToolUtil.getStrIgnoreNull(deptOperShowPara.getDeptPkid()).equals("")){
            criteria.andDeptPkidEqualTo(deptOperShowPara.getDeptPkid());
        }
        // 备注内容
        if(!ToolUtil.getStrIgnoreNull(deptOperShowPara.getOperRemark()).equals("")){
            criteria.andRemarkLike("%"+ deptOperShowPara.getOperRemark()+"%");
        }
        example.setOrderByClause("NAME ASC") ;
        return operMapper.selectByExample(example);
    }
    public List<DeptOperShow> getDeptOperShowList(){
        return myDeptAndOperMapper.getDeptAndOperShowList();
    }
    private void recursiveOperTreeNode(String strParentPkidPara) {
        SelectItemGroup selectItemGroup=new SelectItemGroup();
        DeptOperShow deptOperShow_DeptPara =new DeptOperShow();
        DeptOperShow deptOperShow_OperPara =new DeptOperShow();

        List<Dept> depListTemp;
        List<Oper> operListTemp;
        SelectItem [] selectItemList_Oper;

        deptOperShow_DeptPara.setDeptParentPkid(strParentPkidPara);
        depListTemp = getDeptListByModelShow(deptOperShow_DeptPara);
        for (Dept deptUnit : depListTemp) {
            deptOperShow_OperPara.setDeptPkid(deptUnit.getPkid());
            operListTemp = getOperListByModelShow(deptOperShow_OperPara);
            selectItemGroup.setValue(deptUnit.getPkid());
            selectItemGroup.setLabel(deptUnit.getName());
            int intOperCounts=operListTemp.size();
            if(intOperCounts>0) {
                selectItemList_Oper = new SelectItem[operListTemp.size()];
                for (int i = 0; i < operListTemp.size(); i++) {
                    selectItemList_Oper[i] = new SelectItem(operListTemp.get(i).getPkid(), operListTemp.get(i).getName());
                }
                selectItemGroup.setSelectItems(selectItemList_Oper);
            }else{
                selectItemList_Oper = new SelectItem[1];
                selectItemList_Oper[0]=new SelectItem(null,null);
                selectItemGroup.setSelectItems(selectItemList_Oper);
            }
            try {
                SelectItemGroup selectItemGroupTemp=(SelectItemGroup)BeanUtils.cloneBean(selectItemGroup);
                selectItemList.add(selectItemGroupTemp);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            recursiveOperTreeNode(deptUnit.getPkid());
        }
    }

    public List<SelectItem> getDeptOperSelectItemList() {
        selectItemList.clear();
        recursiveOperTreeNode("ROOT");
        return selectItemList;
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

}
