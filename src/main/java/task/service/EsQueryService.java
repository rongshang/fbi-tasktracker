package task.service;

import task.repository.model.model_show.*;
import task.repository.dao.not_mybatis.MyQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-5
 * Time: ����8:50
 * To change this template use File | Settings | File Templates.
 */
@Service
public class EsQueryService {
    @Autowired
    private MyQueryMapper myQueryMapper;

    public List<QryShow> getCSList(String strCttType,String strBelongToPkid){
        return myQueryMapper.getCSList(strCttType,strBelongToPkid) ;
    }

    public List<QryShow> getCSStlMList(String strCstplInfoPkidPara,String strPeriodNoPara){
        return myQueryMapper.getCSStlMList(strCstplInfoPkidPara,strPeriodNoPara) ;
    }
}
