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
    
}
