package task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.repository.dao.PtenudetailMapper;
import task.repository.dao.PtenumainMapper;
import task.repository.model.Ptenumain;
import task.repository.model.PtenumainExample;
import task.repository.model.Ptenudetail;
import task.repository.model.PtenudetailExample;
import task.repository.model.PtenudetailKey;
import skyline.util.ToolUtil;

import java.util.List;

/**
 * Created by Administrator on 2014/8/27.
 */
@Service
public class EnuService {
    @Autowired
    private PtenudetailMapper enudetailMapper;
    @Autowired
    private PtenumainMapper enumainMapper;

    public List<Ptenumain> selectMainListByModel(Ptenumain ptenumainPara) {
        PtenumainExample example= new PtenumainExample();
        example.createCriteria()
                .andEnunameLike("%"+ ToolUtil.getStrIgnoreNull(ptenumainPara.getEnuname())+"%");
        example.setOrderByClause(" ENUNAME ASC ") ;
        List<Ptenumain> enumainTemp=enumainMapper.selectByExample(example);
        return enumainTemp;
    }
    public boolean mainIsExistInDb(Ptenumain enumainPara) {
        PtenumainExample example = new PtenumainExample();
        example.createCriteria().andEnutypeEqualTo(enumainPara.getEnutype());
        return enumainMapper.countByExample(example) >= 1;
    }
    public void insertMainRecord(Ptenumain enumainPara) {
        enumainMapper.insertSelective(enumainPara);
    }
    public void updateMainRecord(Ptenumain enumainPara){
        enumainMapper.updateByPrimaryKeySelective(enumainPara);
    }
    public int deleteMainByPrimaryKey(String enutype){
        return enumainMapper.deleteByPrimaryKey(enutype);
    }

    public List<Ptenudetail> selectDetailListByModel(Ptenudetail enudetailPara) {
        PtenudetailExample example= new PtenudetailExample();
        example.createCriteria()
                .andEnutypeLike("%" + ToolUtil.getStrIgnoreNull(enudetailPara.getEnutype()) + "%")
                .andEnuitemlabelLike("%" + ToolUtil.getStrIgnoreNull(enudetailPara.getEnuitemlabel()) + "%");
        example.setOrderByClause(" dispno ");
        List<Ptenudetail> enudetailTemp=enudetailMapper.selectByExample(example);
        return enudetailTemp;
    }
    public boolean detailIsExistInDb(Ptenudetail enudetailPara) {
        PtenudetailExample example = new PtenudetailExample();
        example.createCriteria()
                .andEnutypeEqualTo(enudetailPara.getEnutype())
                .andEnuitemvalueEqualTo(enudetailPara.getEnuitemvalue());
        return enudetailMapper .countByExample(example) >= 1;
    }
    public boolean detailRelateIsExistInDb(Ptenudetail enudetailPara) {
        PtenudetailExample example = new PtenudetailExample();
        example.createCriteria()
                .andEnutypeEqualTo(enudetailPara.getEnutype());
        return enudetailMapper .countByExample(example) >= 1;
    }
    public void insertDetailRecord(Ptenudetail enudetailPara) {
        enudetailMapper.insertSelective(enudetailPara);
    }
    public void updateDetailRecord(Ptenudetail enudetailPara){
        enudetailMapper.updateByPrimaryKeySelective(enudetailPara);
    }
    public int deleteDetailByPrimaryKey(Ptenudetail enudetailPara){
        return enudetailMapper.deleteByPrimaryKey(fromDetailtoKey(enudetailPara));
    }

    private PtenudetailKey fromDetailtoKey(Ptenudetail enudetailPara) {
        PtenudetailKey enudetailKeyTemp=new PtenudetailKey();
        enudetailKeyTemp.setEnuitemvalue(enudetailPara.getEnuitemvalue());
        enudetailKeyTemp.setEnutype(enudetailPara.getEnutype());
        return enudetailKeyTemp;
    }
}
