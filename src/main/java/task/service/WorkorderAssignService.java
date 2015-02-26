package task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.repository.dao.not_mybatis.MyWorkorderInfoMapper;
import task.repository.model.WorkorderInfo;

import java.util.List;

/**
 * Created by Administrator on 2015/2/13.
 */
@Service
public class WorkorderAssignService {

    private static final Logger logger = LoggerFactory.getLogger(WorkorderAssignService.class);

    @Autowired
    private MyWorkorderInfoMapper myWorkorderInfoMapper;
    /***
     * ���ݹ���ID���߹�������ȡ�������Ĺ�����Ϣ
     * param:orderPkId(����pkid),orderName(������)
     * @return List<WorkorderInfo>
     */
    public List<WorkorderInfo> getWorkorderInfoByPkIdOrName( String orderPkId,String orderName){
        List<WorkorderInfo> WorkorderInfos = null;
        try{
            WorkorderInfos = myWorkorderInfoMapper.getWorkorderInfoByPkIdOrName(orderPkId,orderName);
        }catch (Exception e){
            logger.info("WorkorderAssignService���е�getWorkorderInfoByPkIdOrName�쳣:"+e.toString());
        }
        return WorkorderInfos;
    }

}
