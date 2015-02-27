package task.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.repository.dao.WorkorderInfoMapper;
import task.repository.dao.not_mybatis.MyWorkorderInfoMapper;
import task.repository.model.WorkorderInfo;
import task.repository.model.WorkorderInfoExample;

import java.util.List;

/**
 * Created by Administrator on 2015/2/13.
 */
@Service
public class WorkorderAssignService {

    private static final Logger logger = LoggerFactory.getLogger(WorkorderAssignService.class);

    @Autowired
    private MyWorkorderInfoMapper myWorkorderInfoMapper;
    @Autowired
    private WorkorderInfoMapper workorderInfoMapper;
    /***
     * ���ݹ���ID���߹�������ȡ�������Ĺ�����Ϣ
     * param:orderPkId(����id),orderName(������)
     * @return List<WorkorderInfo>
     */
    public List<WorkorderInfo> getWorkorderInfoByIdOrName(String Id,String orderName){
        List<WorkorderInfo> workorderInfos = null;
        try{
            WorkorderInfoExample example = new WorkorderInfoExample();
            example.createCriteria().andArchivedFlagEqualTo("0");
            WorkorderInfoExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotBlank(Id)){
                criteria.andIdLike("%"+ Id+"%");
            }
            if(StringUtils.isNotBlank(orderName)){
                criteria.andNameLike("%"+ orderName+"%");
            }
            example.setOrderByClause("ID ASC") ;
            workorderInfos = workorderInfoMapper.selectByExample(example);
        }catch (Exception e){
            logger.info("WorkorderAssignService���е�getWorkorderInfoByPkIdOrName�쳣:"+e.toString());
        }
        return workorderInfos;
    }
}
