package com.ruoyi.system.service.impl;

import java.sql.*;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.dto.CheckDeviceIntegralityDTO;
import com.ruoyi.system.domain.param.*;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.po.*;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.DesktopService;
import com.ruoyi.system.service.DeviceListService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.ruoyi.common.constant.TestKitConstants.*;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
public class DesktopServiceImpl implements DesktopService {
    @Autowired
    private TCarlineMapper tCarlineMapper;
    @Autowired
    private TClusterMapper tClusterMapper;
    @Autowired
    private TCarlineInfoMapper tCarlineInfoMapper;
    @Autowired
    private TMatrixMapper tMatrixMapper;
    @Autowired
    private TComponentDataMapper tComponentDataMapper;
    @Autowired
    private TCarlineComponentMapper tCarlineComponentMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private TDesktopRecordMapper tDesktopRecordMapper;
    @Autowired
    private DeviceListService deviceListService;
    @Autowired
    private TDataLogMapper desktopLogMapper;

    private static final Logger log = LoggerFactory.getLogger(DesktopServiceImpl.class);

    @Override
    public AjaxResult submit(DesktopSubmitParam desktopSubmitParam) {
        List<DesktopRecordParam> desktopRecordParams = desktopSubmitParam.getDesktopRecordParams();
        log.info("desktopRecordParams的参数为：{}",desktopRecordParams);
        List<TDataLog> allDesktopLogs = new ArrayList<>();
        //用户校验（设置客户端登录后好像不用了）
        if (CollectionUtils.isEmpty(desktopRecordParams) || StringUtils.isEmpty(desktopSubmitParam.getLocalHostAcoount()) || StringUtils.isEmpty(desktopSubmitParam.getLocalHostPassword())) {
            return AjaxResult.error("Userlist or params can't be empty");
        }
        String localHostAcoount = desktopSubmitParam.getLocalHostAcoount();
        SysUser sysUser = sysUserMapper.selectUserByUserName(localHostAcoount);
        String localHostPassword = desktopSubmitParam.getLocalHostPassword();
        if (sysUser == null || StringUtils.isEmpty(localHostPassword) || !SecurityUtils.matchesPassword(localHostPassword, sysUser.getPassword())) {
            return AjaxResult.error("User information is wrong");
        }
        String LocalHostUserId = sysUser.getUserId().toString();
        Map<String, TDesktopRecord> insertDesktopRecords = new HashMap<>();
        Map<String, TDesktopRecord> updateDesktopRecords = new HashMap<>();
        Map<String, DesktopRecordParam> desktopRecordParamHashMap = new HashMap<>();
        List<Long> deleteDesktopRecordUids = new ArrayList<Long>();
        //
        for (DesktopRecordParam desktopRecordParam : desktopRecordParams) {
            log.info("desktopRecordParam的参数为：{}",desktopRecordParam);
            TDesktopRecord tDesktopRecord = new TDesktopRecord();
            buildDesktopRecord(tDesktopRecord, LocalHostUserId,desktopSubmitParam.getLocalHostAcoount(),desktopRecordParam);
            String indexUid = desktopRecordParam.getIndexUid();
            desktopRecordParamHashMap.put(indexUid,desktopRecordParam);
            DeviceInfoVo desktopDevice = desktopRecordParam.getDesktopDevice();
            if (StringUtils.isEmpty(indexUid) || desktopDevice == null || desktopDevice.getCarlineInfoUid() == null|| desktopDevice.getVersionCode() == null) {
                continue;
            }
            log.info("2 desktopRecordParam的参数为：{}",desktopRecordParam);
            if (OPERATION_TYPE_INSERT.equals(desktopRecordParam.getOperationType())) {
                autoInstertDesktopDevice(tDesktopRecord, desktopDevice);
                insertDesktopRecords.put(indexUid, tDesktopRecord);
            } else if (OPERATION_TYPE_UPDATE.equals(desktopRecordParam.getOperationType())) {
                if (StringUtils.isEmpty(desktopRecordParam.getRecordUid())){
                    return AjaxResult.error("recorduid can't be empty");
                }
                autoInstertDesktopDevice(tDesktopRecord, desktopDevice);
                updateDesktopRecords.put(indexUid, tDesktopRecord);
            } else if (OPERATION_TYPE_DELETE.equals(desktopRecordParam.getOperationType())) {
                deleteDesktopRecordUids.add(Long.valueOf(desktopRecordParam.getRecordUid()));
            }
        }
        String operIp = desktopSubmitParam.getOperIp();
        if (StringUtils.isEmpty(operIp)){
            operIp = "192.168.0.1";
        }
        log.info("3 desktopRecordParam的参数为：{}",insertDesktopRecords.keySet());
        for (String indexUid : insertDesktopRecords.keySet()) {
            TDesktopRecord tDesktopRecord = insertDesktopRecords.get(indexUid);
            tDesktopRecordMapper.insert(tDesktopRecord);
            Integer recordIndex = getRecordIndex(tDesktopRecord);
            DesktopRecordParam desktopRecordParam = desktopRecordParamHashMap.get(indexUid);
            buildOperationLog(recordIndex,allDesktopLogs, LocalHostUserId, operIp, desktopRecordParam,tDesktopRecord, OPERATION_TYPE_INSERT);
        }
        log.info("4 desktopRecordParam的参数为：{}",updateDesktopRecords.keySet());
        for (String indexUid : updateDesktopRecords.keySet()) {
            TDesktopRecord tDesktopRecord = updateDesktopRecords.get(indexUid);
            tDesktopRecordMapper.updateById(tDesktopRecord);
            Integer recordIndex = getRecordIndex(tDesktopRecord);
            DesktopRecordParam desktopRecordParam = desktopRecordParamHashMap.get(indexUid);
            buildOperationLog(recordIndex,allDesktopLogs, LocalHostUserId, operIp, desktopRecordParam,tDesktopRecord, OPERATION_TYPE_UPDATE);
        }
        for (TDataLog tDataLog :allDesktopLogs){
            log.info("5 desktopRecordParam的参数为：{}",tDataLog);
            desktopLogMapper.insert(tDataLog);
        }
        if(deleteDesktopRecordUids != null && deleteDesktopRecordUids.size() > 0) {
            log.info("6 desktopRecordParam的参数为：{}",deleteDesktopRecordUids);
            tDesktopRecordMapper.deleteTDesktopRecordByUids(deleteDesktopRecordUids.toArray(new Long[deleteDesktopRecordUids.size()]));
        }
        return AjaxResult.success(insertDesktopRecords);
    }

    @Override
    public AjaxResult login(DesktopLoginParam desktopLoginParam) {
        return null;
    }


    @Test
    public void test1() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            //1. 注册驱动
            Class.forName("com.mysql.jdbc.Driver");
            //2.获取连接对象
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testkit?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8", "root", "123456");
            //3.定义sql
            String sql = "select * from t_carline";
            //4.获取执行sql对象
            stmt = conn.createStatement();
            //5.执行sql，返回结果集
            rs = stmt.executeQuery(sql);
            //6.处理结果
            //循环判断游标是否是最后一行末尾。
            //一行一行的移动，再逐个列获取数据
            while (rs.next()) {

                //获取数据
                //6.2 获取数据
                /*
                boolean next(): 游标向下移动一行，判断当前行是否是最后一行末尾(是否有数据)，
                            如果是，则返回false，如果不是则返回true
                        * getXxx(参数):获取数据
                        * Xxx：代表数据类型   如： int getInt() ,	String getString()
                        * 参数：
                            1. int：代表列的编号,从1开始   如： getString(1)
                            2. String：代表列名称。 如： getDouble("balance")
                */
                int id = rs.getInt(1);
                String name = rs.getString("name");
                double balance = rs.getDouble(3);

                System.out.println(id + "---" + name + "---" + balance);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getdb(DesktopGetDBParam desktopGetDBParam) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            //1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2.获取连接对象
            conn = DriverManager.getConnection("jdbc:mysql://testkit-mysql:3306/testkit?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8", "root", "12345678");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testkit?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8", "root", "123456");
            //4.获取执行sql对象
            stmt = conn.createStatement();
            //5.执行sql，返回结果集
            String sql = "select * from " + desktopGetDBParam.getTableName();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String, Object> oneDataMap = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    if (rs.getObject(i) != null) {
                        oneDataMap.put(columnName, rs.getObject(i));
                    } else {
                        oneDataMap.put(columnName, null);
                    }
                }
                list.add(oneDataMap);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CheckDeviceIntegralityDTO checkDeviceIntegrality(DesktopSubmitParam desktopSubmitParam) {
        CheckDeviceIntegralityDTO checkDeviceIntegralityDTO = new CheckDeviceIntegralityDTO();
        checkDeviceIntegralityDTO.setIsIntegrated(true);

        List<DesktopRecordParam> desktopRecordParams = desktopSubmitParam.getDesktopRecordParams();
        if (desktopSubmitParam == null || desktopRecordParams == null || desktopRecordParams.size() == 0){
            checkDeviceIntegralityDTO.setIsIntegrated(false);
            checkDeviceIntegralityDTO.setMessage("Can't submit empty data");
            return checkDeviceIntegralityDTO;
        }
        Set<Long> carlineInfoUids = new HashSet<>();
        for (DesktopRecordParam desktopRecordParam:desktopRecordParams){
            DeviceInfoVo desktopDevice = desktopRecordParam.getDesktopDevice();
            if (desktopDevice == null){
                checkDeviceIntegralityDTO.setIsIntegrated(false);
                checkDeviceIntegralityDTO.setMessage("Record doesn‘t reference deviceInfo");
                return checkDeviceIntegralityDTO;
            }
            if (desktopDevice.getCarlineInfoUid() == null){
                checkDeviceIntegralityDTO.setIsIntegrated(false);
                checkDeviceIntegralityDTO.setMessage("Device doesn‘t have carlineInfoUid");
                return checkDeviceIntegralityDTO;
            }
            carlineInfoUids.add(desktopDevice.getCarlineInfoUid());
        }

        List<TCarlineInfo> tCarlineInfos = tCarlineInfoMapper.selectList(new QueryWrapper<TCarlineInfo>()
                .in("carline_info_uid", carlineInfoUids.toArray(new Long[carlineInfoUids.size()])));
        if (CollectionUtils.isEmpty(tCarlineInfos) || tCarlineInfos.size() != carlineInfoUids.size()){
            checkDeviceIntegralityDTO.setIsIntegrated(false);
            checkDeviceIntegralityDTO.setMessage("Device doesn‘t exist,please delete corrent uncommitted records and update datebase");
            return checkDeviceIntegralityDTO;
        }
        return checkDeviceIntegralityDTO;
    }

    private Integer getRecordIndex(TDesktopRecord tDesktopRecord) {
        Integer recordIndex = desktopLogMapper.selectMaxRecordIndex(tDesktopRecord.getUid());
        if (recordIndex == null){
            recordIndex = 1;
        }else {
            recordIndex = recordIndex + 1;
        }
        return recordIndex;
    }

    private static void buildOperationLog(Integer recordIndex, List<TDataLog> allDesktopLogs, String LocalHostUserId, String operIp, DesktopRecordParam desktopRecordParam, TDesktopRecord tDesktopRecord, String operationType) {
        List<DesktopLogParam> TDataLogs = desktopRecordParam.getDesktopLogParams();
        if (TDataLogs != null && TDataLogs.size() > 0){
            for (DesktopLogParam desktopLogParam:TDataLogs){
                TDataLog tDataLogPO = new TDataLog();
                tDataLogPO.setLocalhostUserId(Long.valueOf(LocalHostUserId));
                tDataLogPO.setOperUserName(desktopRecordParam.getTester());
                tDataLogPO.setOperationUid(tDesktopRecord.getUid().toString());
                tDataLogPO.setOperationType(operationType);
                tDataLogPO.setOperationFiled(desktopLogParam.getOperationFiled());
                tDataLogPO.setBeforeOperationValue(desktopLogParam.getBeforeOperationValue());
                tDataLogPO.setAfterOperationValue(desktopLogParam.getAfterOperationValue());
                tDataLogPO.setRequestMethod("POST");
                tDataLogPO.setOperTime(desktopRecordParam.getOperTime());
                tDataLogPO.setOperUrl("/desktop/submit");
                tDataLogPO.setOperIp(operIp);
                tDataLogPO.setDataIndex(recordIndex);
                allDesktopLogs.add(tDataLogPO);
            }
        }
    }


    private void autoInstertDesktopDevice(TDesktopRecord tDesktopRecord, DeviceInfoVo desktopDevice) {
        List<TCarlineInfo> tCarlineInfos = tCarlineInfoMapper.selectList(new QueryWrapper<TCarlineInfo>()
                .eq("original_carline_info_uid", desktopDevice.getCarlineInfoUid())
                .eq("version_code", desktopDevice.getVersionCode()));
        Long carlineInfoUid = null;
        if (tCarlineInfos != null && tCarlineInfos.size() > 0) {
            carlineInfoUid = tCarlineInfos.get(0).getCarlineInfoUid();
            tDesktopRecord.setDataUid(carlineInfoUid);
        } else {
            desktopDevice.setOriginalCarlineInfoUid(desktopDevice.getCarlineInfoUid());
            desktopDevice.setBasicType(BASIC_TYPE_DESKTOP_DEVICE);
            //save
            carlineInfoUid = deviceListService.insertBasicInfo(desktopDevice).getCarlineInfoUid();
            //t_component_data与其连接表的保存
            deviceListService.buildUpdateComponent(desktopDevice, carlineInfoUid);
        }
        tDesktopRecord.setDataUid(carlineInfoUid);
    }


    private static void buildDesktopRecord(TDesktopRecord tDesktopRecord, String LocalHostUserId, String localHostAcoount,DesktopRecordParam desktopRecordParam) {
        if (StringUtils.isNotEmpty(desktopRecordParam.getRecordUid()) && OPERATION_TYPE_UPDATE.equals(desktopRecordParam.getOperationType())){
            tDesktopRecord.setUid(Long.valueOf(desktopRecordParam.getRecordUid()));
        }
        tDesktopRecord.setLocalhostUserId(LocalHostUserId);
        tDesktopRecord.setOperUserName(desktopRecordParam.getTester());
        tDesktopRecord.setOperLocation("there");
        tDesktopRecord.setStatus(1);
        tDesktopRecord.setOperIp("192.168.0.1");
        tDesktopRecord.setLocalHostAcoount(localHostAcoount);
        tDesktopRecord.setOperTime(desktopRecordParam.getTestDate());
        tDesktopRecord.setFunctionGroupType(desktopRecordParam.getFunctionGroupType());
        tDesktopRecord.setTaskType(desktopRecordParam.getTaskType());
        tDesktopRecord.setMileacge(desktopRecordParam.getMileacge());
        tDesktopRecord.setFallBackScreen(desktopRecordParam.getFallBackScreen());
        tDesktopRecord.setTestHour(desktopRecordParam.getTestHour());
        tDesktopRecord.setLocation(desktopRecordParam.getLocation());
        tDesktopRecord.setSystemReset(desktopRecordParam.getSystemReset());
        tDesktopRecord.setNaviReset(desktopRecordParam.getNaviReset());
        tDesktopRecord.setBlackMap(desktopRecordParam.getBlackMap());
        tDesktopRecord.setInitializingOccurred(desktopRecordParam.getInitializingOccurred());
        tDesktopRecord.setBussleep(desktopRecordParam.getBussleep());
        tDesktopRecord.setPlannedTicket(desktopRecordParam.getPlannedTicket());
        tDesktopRecord.setComment(desktopRecordParam.getComment());
    }
}



















