package com.ruoyi.system.service.impl;

import java.io.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.reflect.ReflectUtils;
import com.ruoyi.system.domain.AutoSaveVersionVO;
import com.ruoyi.system.domain.DeviceCompareVO;
import com.ruoyi.system.domain.dto.*;
import com.ruoyi.system.domain.enums.ComponentTypeMapping;
import com.ruoyi.system.domain.param.DeviceCompareParam;
import com.ruoyi.system.domain.param.DeviceListParam;
import com.ruoyi.system.domain.po.*;
import com.ruoyi.system.domain.vo.DeviceInfoComponent;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import com.ruoyi.system.domain.vo.DeviceListVo;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.DeviceListService;
import io.netty.util.internal.StringUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.hutool.core.text.StrPool.C_SPACE;
import static com.github.pagehelper.page.PageMethod.offsetPage;
import static com.ruoyi.common.constant.TestKitConstants.*;
import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
public class DeviceListServiceImpl implements DeviceListService {
    public static final Logger log = LoggerFactory.getLogger(DeviceListServiceImpl.class);

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
    private SysDictDataMapper dictDataMapper;

    @Autowired
    private TDtcReportMapper tdtcReportMapper;
    @Autowired
    private SysDictDataServiceImpl sysDictDataService;

    /**
     * 查询车型基本数据列表
     *
     * @return `车型基本数据`
     */
    @Override
    public List queryDeviceList(DeviceListParam deviceListParam) {
        List<DeviceListVo> deviceListVos = tCarlineInfoMapper.queryDeviceList(deviceListParam);
        return deviceListVos;
    }

    private final static Integer MAXCIRCLE = 10;

    @Override
    @Transactional
    public AjaxResult updateDeviceInfo(DeviceInfoVo deviceInfoVo) {
        if (deviceInfoVo == null || deviceInfoVo.getCarlineInfoUid() == null) {
            return AjaxResult.error("CarlineInfoUid can't be empty");
        }
        //查重
        List<Long> carlineDuplicateUids= tCarlineInfoMapper.countCarlineDuplicateExcludedSameVersion(deviceInfoVo.getDeviceName(),deviceInfoVo.getCarlineInfoUid(),BASIC_TYPE_WEB_DEVICE);
        if (!CollectionUtils.isEmpty(carlineDuplicateUids)){
            return AjaxResult.error("This device name already exists");
//            deleteTCarlineByUids(carlineDuplicateUids.toArray(new Long[carlineDuplicateUids.size()]));
        }
        //相关表字段获取
        Long carlineInfoUid = deviceInfoVo.getCarlineInfoUid();
        TCarlineInfo tCarlineInfo = tCarlineInfoMapper.selectById(carlineInfoUid);
        if (tCarlineInfo == null) {
            return AjaxResult.error("This carlineInfo doesn't exit");
        }
        Long clusterUid = tCarlineInfo.getClusterUid();
        TCluster tCluster = tClusterMapper.selectById(clusterUid);
        Long carlineUid = tCluster.getCarlineUid();
        TCarline tCarline = tCarlineMapper.selectById(carlineUid);


        //tCluster 筛选有无额外版本，并删除相关版本与配件信息
        List<CorrentVersionDeviceDTO> correntVersionDeviceDTOS = tClusterMapper.selectCorrentVersionDeviceDTO(carlineInfoUid);
        if (CollectionUtils.isEmpty(correntVersionDeviceDTOS)) {
            return AjaxResult.error("Current version doesn't exit");
        }
        if (correntVersionDeviceDTOS.size() == MAXCIRCLE){
            cascadeDeleteVersion(correntVersionDeviceDTOS.get(0).getCarlineInfoUid(), correntVersionDeviceDTOS.get(0).getClusterUid());
        }

        //save -tCluster
        tCluster.setUid(null);
//        tCluster.setdeviceCircleNum(nextDeviceNum);
        buildTClusterPO(deviceInfoVo, tCluster);
        tClusterMapper.insert(tCluster);
        //t_carline_info
        tCarlineInfo.setCarlineInfoUid(null);
        tCarlineInfo.setClusterUid(tCluster.getUid());
        buildTCarlineInfoPO(deviceInfoVo, tCarlineInfo);
        tCarlineInfoMapper.insert(tCarlineInfo);
        //t_carline
        tCarline.setUpdateTime(new Date());
        buildCarlinePO(deviceInfoVo, tCarline);
        tCarlineMapper.updateById(tCarline);
        //t_component_data与其连接表的保存
        buildUpdateComponent(deviceInfoVo, tCarlineInfo.getCarlineInfoUid());

        //保存
        return AjaxResult.success(tCarlineInfo.getCarlineInfoUid());
    }

    @Test
    public void test22() throws ClassNotFoundException {
        Class clazz = Class.forName(PARSE_DTC_REPORT_DTO_PATH);
        Field[] declaredFields = clazz.getDeclaredFields();
        String str = new String("{\n" +
                "    \"variant\": {\n" +
                "        {\n" +
                "            \"SearchedOdxFileVersion\": \"EV_HCP3([\\\\S]*?)Node[\\\\s\\\\S]*?\",\n" +
                "            \"Systembezeichnung\": \"[ ]*MU-T{0,1}([BHP])[\\\\\\\\s\\\\\\\\S]*?\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"SWVersion\": null,\n" +
                "    \"HWVersion\": null,\n" +
                "    \"HWTeilenummer\": null\n" +
                "}");
        JSONObject jsonObject = JSON.parseObject(str);
        ParseDTCReportDTO parseObject = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<ParseDTCReportDTO>() {
        });
//        System.out.println(parseObject);
        if (StringUtils.isNotEmpty(parseObject.getVariant())) {
            Map parse = (Map) JSON.parse(parseObject.getVariant());
            Map<String, String> o2 = (Map) parse.get("");
            for (String str2 : o2.keySet()) {
//                System.out.println(str2);
//                System.out.println(o2.get(str2));
            }
        }
        //以属性名进行反射，若值为空则直接取标签值，不为空则进行正则
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            if (jsonObject.containsKey(fieldName) && ReflectUtils.invokeGetter(parseObject, fieldName) == null) {
                System.out.println(fieldName);
            } else if (jsonObject.containsKey(fieldName) && ReflectUtils.invokeGetter(parseObject, fieldName) != null) {
                Map parse = (Map) JSON.parse(ReflectUtils.invokeGetter(parseObject, fieldName));
                Map<String, String> o2 = (Map) parse.get("");
                for (String str2 : o2.keySet()) {
                    System.out.println(str2);
                    System.out.println(o2.get(str2));
                }
            }
        }
    }

    @Test
    public void test23() throws ClassNotFoundException {
        String str = new String("{\n" +
                "    \"SearchedOdxFileVersion\": \"EV_(HCP5|[^_]+?(?=AU|MEB|UNECE)|[^_]+)[\\\\S ]*?\"\n" +
                "}");
        JSONObject jsonObject = JSON.parseObject(str);
        Map<String, String> parseObject = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Map>() {
        });
        for (String str2 : parseObject.keySet()) {
            System.out.println(parseObject.get(str2));
        }
    }

    static String PARSE_DTC_REPORT_DTO_PATH = "com.ruoyi.system.domain.dto.ParseDTCReportDTO";
    static String T_DTC_REPORT_PATH = "com.ruoyi.system.domain.po.TDTCReport";

    @Override
    public AjaxResult importDTCReport(InputStream IN, boolean b, String operName) {
        try {
            Map<String, Map> reportMapVO = getReoirtMapVO(IN);
            return AjaxResult.success(reportMapVO);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Map> getReoirtMapVO(InputStream IN) throws ClassNotFoundException {
        Map<String,Map> reportMapVO = new HashMap<>();
        Document docResult = XmlUtil.readXML(IN);
        Field[] parseDTCReportDTOFields = Class.forName(PARSE_DTC_REPORT_DTO_PATH).getDeclaredFields();
        Map<String, List<TDTCReport>> dtcReportMaps = new HashMap<>();
        for (TDTCReport tdtcReport : tdtcReportMapper.selectList(new QueryWrapper<>())) {
            List<TDTCReport> tempReports;
            if (dtcReportMaps.get(tdtcReport.getLevel()) == null) {
                tempReports = new ArrayList<>();
            } else {
                tempReports = dtcReportMaps.get(tdtcReport.getLevel());
            }
            tempReports.add(tdtcReport);
            dtcReportMaps.put(tdtcReport.getLevel(), tempReports);
        }
        for (String level : dtcReportMaps.keySet()) {
            if (level.equals("//")) {
                continue;
            }
            List<TDTCReport> dtcReports = dtcReportMaps.get(level);
            NodeList nodeListByXPath = XmlUtil.getNodeListByXPath(level, docResult);
            for (TDTCReport tdtcReport : dtcReports) {
                String conditions = tdtcReport.getSelectCondition();
                Map<String, String> conditionMap = JSON.parseObject(JSON.parseObject(conditions).toJSONString(), new TypeReference<Map>() {
                });
                for (int i = 0; i < nodeListByXPath.getLength(); i++) {
                    Node item = nodeListByXPath.item(i);
                    TDTCReportDTO reportDTO = XmlUtil.xmlToBean(item, TDTCReportDTO.class);
                    if (reportDTO == null) {
                        continue;
                    }
                    for (String condition : conditionMap.keySet()) {
                        String conditionValue = conditionMap.get(condition);
                        String dtcValue = ReflectUtils.invokeGetter(reportDTO, condition).toString();
                        if (dtcValue.equals(conditionValue) || dtcValue.contains(conditionValue)) {
                            Map<String, String> componentMap = new HashMap<>();
                            JSONObject jsonObject = JSON.parseObject(tdtcReport.getSelectValue());
                            ParseDTCReportDTO parseObject = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<ParseDTCReportDTO>() {
                            });
                            for (Field field : parseDTCReportDTOFields) {
                                String fieldName = field.getName();
                                Object invokeGetter = ReflectUtils.invokeGetter(parseObject, fieldName);
                                if (jsonObject.containsKey(fieldName) && invokeGetter == null) {
                                    componentMap.put(fieldName, ReflectUtils.getFieldValue(reportDTO, fieldName));
                                } else if (jsonObject.containsKey(fieldName) && invokeGetter != null) {
                                    regionRegexValue(reportDTO, componentMap, fieldName, invokeGetter.toString());
                                }
                            }
                            String componentName = tdtcReport.getComponentName();
                            if (StringUtils.isEmpty(componentName)) {
                                componentMap.put("componentName", tdtcReport.getComponentType());
                            } else {
                                regionRegexValue(reportDTO, componentMap, "componentName", componentName);
                            }
                            componentMap.put("ecuId", tdtcReport.getEcuId());
                            componentMap.put("componentType", tdtcReport.getComponentType());
                            if ("005F".equals(tdtcReport.getEcuId())) {
                                Map<String, String> basicInfo = getbasicInfo(docResult, reportDTO, componentMap);
                                reportMapVO.put("basicInfo",basicInfo);
                            }
                            reportMapVO.put(componentMap.get("ecuId"),componentMap);
                        }
                    }
                }
            }
        }
        return reportMapVO;
    }

    private Map<String, String> getbasicInfo(Document docResult, TDTCReportDTO reportDTO, Map<String, String> componentMap) {
        Map<String, String> basicInfo = new HashMap<>();
        String[] dictTypes = new String[]{"clusterName", "projectType", "platformType", "marketType",
                "functionGroupType", "variantType", "taskType", "carlineModelType", "goldenCarType", "goldenClusterNameType"};
        Map<String, Map<String, String>> dictMap = getDictMap(dictTypes);

        //Date
        String dateStr = StrUtil.trimEnd(XmlUtil.getNodeListByXPath("//Datum", docResult).item(0).getTextContent());
        String timeStr = StrUtil.trimEnd(XmlUtil.getNodeListByXPath("//Zeit", docResult).item(0).getTextContent());
        basicInfo.put("dtcTime", dateStr + " " + timeStr);

        //VIN
        String VIN = StrUtil.trimEnd(XmlUtil.getNodeListByXPath("//Fahrgestellnummer", docResult).item(0).getTextContent());
        basicInfo.put("VIN", VIN);
        String projectName = "HCP3";
        //MIB3
        //SWVERSION variant
        String variant = StrUtil.trimEnd(componentMap.get("variant"));
        String swVersion = StrUtil.trimEnd(componentMap.get("SWVersion"));
        if ("B".equals(variant) || "H".equals(variant) || "P".equals(variant)) {
            projectName = "MIB3";
            char[] chars = swVersion.toUpperCase().toCharArray();
            Boolean isPureNum = true;
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] <= 90 && chars[j] >= 65) {
                    isPureNum = false;
                    break;
                }
            }
            if (isPureNum) {
                swVersion = "P" + swVersion;
            } else {
                if (chars.length == 4 && chars[0] == 90 && chars[1] >= 48 &&
                        chars[1] <= 57 && chars[2] >= 48 && chars[2] <= 57 && chars[3] >= 48 && chars[3] <= 57) {
                    if (chars[1] >= 53) {
                        swVersion ="E3" + chars[1] + chars[2] + chars[3];
                    } else {
                        swVersion ="E4" + chars[1] + chars[2] + chars[3];
                    }
                }
            }
            componentMap.put("SWVersion", swVersion);
        }

        //carline
        String carline = StrUtil.trimEnd(XmlUtil.getNodeListByXPath("//UserProjekt", docResult).item(0).getTextContent());
        if (carline.contains("AU316/x")) {
            if ("LFV".equals(VIN.substring(0, 3))) {//todo:前三位部位这两种情况怎么办？
                carline = carline + " " + "A";
            } else if ("LSV".equals(VIN.substring(0, 3))) {
                carline = carline + " " + "A+";
            }
        }
        if (carline.contains("AU38X-PA A3 (AB4)") || carline.equals("AU38X-PA A3 (AB4)")){
            carline = "AU38X A3 (AB4)";
        }
        if (carline.contains("AU416/2 eQ5 SUV (only COP Audi)") || carline.equals("AU416/2 eQ5 SUV (only COP Audi)")){
            carline = "AU416/x eQ5 (Q6 etron)";
        }
        carline = carline.toUpperCase();
        basicInfo.put("carline",carline);
        String carlineModelType = getDictValue("carlineModelType", dictMap, carline, DICT_MATRIXTYPE_NON);
        basicInfo.put("carlineModelType",carlineModelType);
        //CLUSTER
        String clusterNum = "-";
        String clusterName = "-";
        if ("HCP3".equals(projectName)) {
            clusterNum = "43";
        } else if ("MIB3".equals(projectName)) {
            String tempCluNumber = swVersion;
            Pattern pattern = Pattern.compile("[pePE]\\d{4}");
            Matcher matcher = pattern.matcher(tempCluNumber);
            if (matcher.find()) {
                tempCluNumber = matcher.group(0);
                clusterNum = tempCluNumber.substring(1, 3);
                String trainNum = tempCluNumber.substring(1, 5);
                if ("36".equals(clusterNum)){
                    if ("_AS_AUASUV_".equals(carline)){
                        clusterNum = "35.2";
                    }else {
                        clusterNum = "35";
                    }
                }else if ("38".equals(clusterNum)){
                    if (Long.valueOf(trainNum) < 3853){
                        clusterNum = "37";
                    }else if ("3853".equals(trainNum)){
                        clusterNum = "35";
                    }
                }else if ("40".equals(clusterNum)){
                    if (Long.valueOf(trainNum) < 4051){
                        clusterNum = "39";
                    }else{
                        clusterNum = "3A";
                    }
                }
            }
        }
        basicInfo.put("MUSW", swVersion);
        basicInfo.put("clusterNum", clusterNum);
        if (!"-".equals(clusterNum) && StringUtils.isNotEmpty(clusterNum)){
            clusterName = getDictValue("clusterName", dictMap, "CLU" + clusterNum, DICT_MATRIXTYPE_NON);
        }
        basicInfo.put("clusterName", clusterName);

        //CLUSTER END
        basicInfo.put("projectName", projectName);
        String projectType = null;
        if (!"-".equals(projectName) && StringUtils.isNotEmpty(projectName)){
            projectType = getDictValue("projectType", dictMap, projectName, DICT_MATRIXTYPE_NON);
            basicInfo.put("projectType", projectType);
        }

        //variant
        variant = StrUtil.trimEnd(variant);
        if ("P".equals(variant)) {
            variant = "PREMIUM";
        } else if ("H".equals(variant)) {
            variant = "HIGH";
        } else if ("B".equals(variant)) {
            variant = "BASIC";
        }
        basicInfo.put("Variant", variant);
        String variantType = null;
        if (!"-".equals(variant) && StringUtils.isNotEmpty(variant)){
            variantType = getDictValue("variantType", dictMap, variant, DICT_MATRIXTYPE_NON);
            basicInfo.put("variantType", variantType);
        }

        //market
        String market = StrUtil.trimEnd(regexStr(reportDTO.getSystembezeichnung(), "[ ]*\\S*-([^-]*)[ ]*"));
        basicInfo.put("market", StringUtils.getCleanStr(market));
        String marketType = null;
        if (!"-".equals(market) && StringUtils.isNotEmpty(market)){
            marketType = getDictValue("marketType", dictMap, market, DICT_MATRIXTYPE_NON);
            basicInfo.put("marketType", marketType);
        }

        //platform
        QueryWrapper<TMatrix> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cluster_name",clusterName);
        queryWrapper.eq("project_type",projectType);
        queryWrapper.eq("carline_model_type",carlineModelType);
        queryWrapper.eq("variant_type",variantType);
        List<TMatrix> tMatrixs = tMatrixMapper.selectList(queryWrapper);
        if (StringUtils.isNotEmpty(tMatrixs) && StringUtils.isNotEmpty(marketType)){
            for (TMatrix tMatrix:tMatrixs){
                if (StringUtils.isEmpty(tMatrix.getMarketTypes())){
                    continue;
                }
                for (String marketStr : tMatrix.getMarketTypes().split(",")) {
                    if (marketType.equals(marketStr)){
                        basicInfo.put("platformType",tMatrix.getPlatformType());
                        SysDictData sysDictData = dictDataMapper.selectOne(new QueryWrapper<SysDictData>()
                                .eq("dict_type", "platformType").eq("dict_value", tMatrix.getPlatformType()));
                        basicInfo.put("platformName",sysDictData.getDictLabel());
                        break;
                    }
                }
            }
        }

        return basicInfo;
    }


    @Test
    public void test21() throws IOException, ParseException {
//        System.out.println(getGoldenCWNumByFileName("CL37 SOP 25-22_&_CL35.2_Goldern Car_MIB3_Minimal List_CW39.5"));
        /*
        List<File> files = FileUtil.loopFiles(AUTO_IMPORT_DTC_PATH);
        System.out.println("总共大小为：" + files.size());
        for (File file:files){
            System.out.println(file.getName());
            long l = file.lastModified();
            Date date = new Date(l);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            System.out.println(simpleDateFormat.format(date));
            System.out.println(file.getParent());
            System.out.println(file.getCanonicalPath());
            System.out.println();
        }*/
    }



    private Date getDTCFileDateByFileName(String fileName) {
        try {
            if ("AM".equals(fileName.substring(fileName.length() - 2,fileName.length())) ||
                    "PM".equals(fileName.substring(fileName.length() - 2,fileName.length())) ){
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(fileName.split("_")[fileName.split("_").length - 2]);
                stringBuffer.append(fileName.split("_")[fileName.split("_").length - 1]);
                stringBuffer.delete(stringBuffer.length() - 2,stringBuffer.length());
                Date fileDate = new SimpleDateFormat("yyyyMMddhhmmss").parse(stringBuffer.toString());
                return fileDate;
            }else {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(fileName.split("_")[fileName.split("_").length - 2].replaceAll("\\.",""));
                stringBuffer.append(fileName.split("_")[fileName.split("_").length - 1].replace(".xml","").replaceAll("\\.",""));
                Date fileDate = new SimpleDateFormat("ddMMyyyyhhmmss").parse(stringBuffer.toString());
                return fileDate;
            }
        } catch (ParseException e) {
            log.error("解析错误，该字符串为：{}",fileName);
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void test2930() throws IOException {
        File[] files =  new File(AUTO_IMPORT_DTC_PATH).listFiles();
        String benchPath = null;
        String carPath = null;
        for (File file:files){
            if (file.getName().toUpperCase().contains("CAR") || "CAR".equals(file.getName().toUpperCase())){
                carPath = file.getCanonicalPath();
            }
            if (file.getName().toUpperCase().contains("BENCH") || "BENCH".equals(file.getName().toUpperCase())){
                benchPath = file.getCanonicalPath();
            }
        }
        System.out.println(carPath);
        System.out.println(benchPath);
    }

    @Test
    public void test333() throws IOException, ClassNotFoundException {
        File[] files =  new File(AUTO_IMPORT_DTC_PATH).listFiles();
        for (File file:files){
            String firePath = null;
            if (file.getName().toUpperCase().contains("CAR") || "CAR".equals(file.getName().toUpperCase())
                    || file.getName().toUpperCase().contains("BENCH") || "BENCH".equals(file.getName().toUpperCase())){
                firePath = file.getCanonicalPath();
            }else {
                continue;
            }
            for (File dtcFile:FileUtil.loopFiles(firePath)){
                String deviceType = null;
                if (FileNameUtil.isType(dtcFile.getName(),"xml")){
                    InputStream fileInputStream = IoUtil.toStream(dtcFile);
                    String deviceName = null;
                    Map<String, Map> reportMapVO = getReoirtMapVO(fileInputStream);
                    if (!reportMapVO.isEmpty()){
                        DeviceInfoVo deviceInfoVo = buildDeviceInfoVo(reportMapVO,deviceType,deviceName);
                        insertDeviceInfo(deviceInfoVo);
                    }
                }
            }
        }
    }
    @Override
    @Test
    public void quarzImportDTCReport() throws ClassNotFoundException, IOException {
        File[] files =  new File(AUTO_IMPORT_DTC_PATH).listFiles();
        for (File file:files){
            String firePath = null;
            String deviceType = null;
            String deviceName = null;
            if (file.getName().toUpperCase().contains("CAR") || "CAR".equals(file.getName().toUpperCase())){
                firePath = file.getCanonicalPath();
                deviceType = DEVICE_TYPE_CAR;
            }else if (file.getName().toUpperCase().contains("BENCH") || "BENCH".equals(file.getName().toUpperCase())){
                firePath = file.getCanonicalPath();
                deviceType = DEVICE_TYPE_BENCH;
            }else {
                continue;
            }
            for (File dtcFile:FileUtil.loopFiles(firePath)){
                String devicePath = dtcFile.getCanonicalPath();
                deviceName = dtcFile.getParentFile().getName().split("_")[0];
                Date lastDateByFileName = new Date(0);
                File lastFileByFileName = null;
                Long lastDateByFileDate = 0L;
                File lastFileByFileDate = null;
                File lastFile = null;
                //找到最近的一版时间
                for (File deviceFile:FileUtil.loopFiles(devicePath)){
                    Date correntDate = getDTCFileDateByFileName(deviceFile.getName());
                    if (DateUtil.compare(lastDateByFileName,correntDate) < 0){
                        lastFileByFileName = deviceFile;
                        lastDateByFileName = correntDate;
                    }
                    if (deviceFile.lastModified() > lastDateByFileDate){
                        lastDateByFileDate = deviceFile.lastModified();
                        lastFileByFileDate = deviceFile;
                    }
                }
                if (lastFileByFileName != null && !lastFileByFileName.getName().equals(lastFileByFileDate.getName())){
                    //build
                    Date preciseDateByName = getPreciseDate(lastFileByFileName);
                    Date preciseDateByDate = getPreciseDate(lastFileByFileDate);
                    if (DateUtil.compare(preciseDateByName,preciseDateByDate) < 0){
                        lastFile = lastFileByFileName;
                    }else {
                        lastFile = lastFileByFileDate;
                    }
                }else {
                    lastFile = lastFileByFileName;//如果二者相同则任意一种都可以
                }
                //查找最近的一版并比较是否更新
                RefreshDeviceDTO refreshDeviceDTO = tClusterMapper.selectLastestCluster(deviceName,deviceType);
                if (refreshDeviceDTO == null || refreshDeviceDTO.getCarlineInfoUid() == null){
                    continue;
                }

                //更新
                //build
                InputStream fileInputStream = IoUtil.toStream(lastFile);
                Map<String, Map> reportMapVO = getReoirtMapVO(fileInputStream);
                if (reportMapVO.isEmpty()){
                    return;
                }
                //时间比较
                String dtcTimeStr = null;
                Date dtcTimeDate = null;
                if (reportMapVO.get("basicInfo") != null && reportMapVO.get("basicInfo").get("dtcTime") != null) {
                    dtcTimeStr = reportMapVO.get("basicInfo").get("dtcTime").toString();
                    dtcTimeDate = DateUtil.parse(dtcTimeStr, new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"));
                }
                if (dtcTimeDate == null || DateUtil.compare(lastDateByFileName,refreshDeviceDTO.getUpdateTime()) > 0){
                    continue;
                }
                //插入
                DeviceInfoVo deviceInfoVo = buildDeviceInfoVo(reportMapVO,deviceType,deviceName);
                deviceInfoVo.setIsRefresh(true);
                deviceInfoVo.setCarlineInfoUid(refreshDeviceDTO.getCarlineInfoUid());
                deviceInfoVo.setDtcTime(dtcTimeStr);
                updateDeviceInfo(deviceInfoVo);
            }
        }
    }
    @Override
    public AjaxResult quarzImportDTCReport(Long carlineInfoUid) throws ClassNotFoundException, IOException {
        TCarlineInfo tCarlineInfo = null;
//        TCluster tCluster = null;
        AjaxResult ajaxResult = AjaxResult.error("Don't have such file");
        if (carlineInfoUid != null){
            tCarlineInfo = tCarlineInfoMapper.selectById(carlineInfoUid);
//            tCluster = tClusterMapper.selectById(tCarlineInfo.getClusterUid());
        }
        log.info("准备读取文件，读取文件路径为：{}",AUTO_IMPORT_DTC_PATH);

        File[] files =  new File(AUTO_IMPORT_DTC_PATH).listFiles();
        if (files == null || files.length == 0){
            log.error("该文件为空或者没有权限读取");
            return AjaxResult.error("Don't have such file");
        }
        log.info("开始读取文件，文件大小为{}",files.length);
        for (File file:files){
            String firePath = null;
            String deviceType = null;
            String deviceName = null;
            if (file.getName().toUpperCase().contains("CAR") || "CAR".equals(file.getName().toUpperCase())){
                firePath = file.getCanonicalPath();
                deviceType = DEVICE_TYPE_CAR;
                log.info("读取Car文件,文件名称为{}",file.getName());
            }else if (file.getName().toUpperCase().contains("BENCH") || "BENCH".equals(file.getName().toUpperCase())){
                firePath = file.getCanonicalPath();
                deviceType = DEVICE_TYPE_BENCH;
                log.info("读取bench文件,文件名称为{}",file.getName());
            }else {
                log.info("读取到未知文件,文件名称为{}",file.getName());
                continue;
            }
            /*if (tCluster != null && !deviceType.equals(tCluster.getDeviceType())){
                continue;
            }*/

            for (File dtcFile:FileUtil.loopFiles(firePath)){
                if (dtcFile == null){
                    continue;
                }
                String devicePath = dtcFile.getCanonicalPath();
                deviceName = dtcFile.getParentFile().getName().split("_")[0];
                if (StringUtils.isEmpty(deviceName)){
                    log.info("未找到符合格式的父级deviceName，其父级名称为：{}",dtcFile.getParentFile().getName());
                    continue;
                }
                if (tCarlineInfo != null && !deviceName.equals(tCarlineInfo.getDeviceName())){
                    continue;
                }
                log.error("当前读取的deviceName为：{}",deviceName);
                Date lastDateByFileName = new Date(0);
                File lastFileByFileName = null;
                Long lastDateByFileDate = 0L;
                File lastFileByFileDate = null;
                File lastFile = null;
                //找到最近的一版时间
                for (File deviceFile:FileUtil.loopFiles(devicePath)){
                    Date correntDate = getDTCFileDateByFileName(deviceFile.getName());
                    if (DateUtil.compare(lastDateByFileName,correntDate) < 0){
                        lastFileByFileName = deviceFile;
                        lastDateByFileName = correntDate;
                    }
                    if (deviceFile.lastModified() > lastDateByFileDate){
                        lastDateByFileDate = deviceFile.lastModified();
                        lastFileByFileDate = deviceFile;
                    }
                }
                if (lastFileByFileName != null && !lastFileByFileName.getName().equals(lastFileByFileDate.getName())){
                    //build
                    Date preciseDateByName = getPreciseDate(lastFileByFileName);
                    Date preciseDateByDate = getPreciseDate(lastFileByFileDate);
                    if (DateUtil.compare(preciseDateByName,preciseDateByDate) < 0){
                        lastFile = lastFileByFileName;
                    }else {
                        lastFile = lastFileByFileDate;
                    }
                }else {
                    lastFile = lastFileByFileDate;//如果二者相同则任意一种都可以
                }
                //查找最近的一版并比较是否更新
                RefreshDeviceDTO refreshDeviceDTO = tClusterMapper.selectLastestCluster(deviceName,deviceType);
                if (refreshDeviceDTO == null || refreshDeviceDTO.getCarlineInfoUid() == null){
                    continue;
                }

                //更新
                //build
                if (lastFile == null){
                    continue;
                }
                InputStream fileInputStream = IoUtil.toStream(lastFile);
                Map<String, Map> reportMapVO = getReoirtMapVO(fileInputStream);
                if (reportMapVO.isEmpty()){
                    return null;
                }
                //时间比较
                String dtcTimeStr = null;
                Date dtcTimeDate = null;
                if (reportMapVO.get("basicInfo") != null && reportMapVO.get("basicInfo").get("dtcTime") != null) {
                    dtcTimeStr = reportMapVO.get("basicInfo").get("dtcTime").toString();
                    dtcTimeDate = DateUtil.parse(dtcTimeStr, new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"));
                }
                if (dtcTimeDate == null || DateUtil.compare(lastDateByFileName,refreshDeviceDTO.getUpdateTime()) > 0){
                    continue;
                }
                //插入
                DeviceInfoVo deviceInfoVo = buildDeviceInfoVo(reportMapVO,deviceType,deviceName);
                deviceInfoVo.setIsRefresh(true);
                deviceInfoVo.setCarlineInfoUid(refreshDeviceDTO.getCarlineInfoUid());
                deviceInfoVo.setDtcTime(dtcTimeStr);
                ajaxResult = updateDeviceInfo(deviceInfoVo);
            }
        }
            return ajaxResult;
    }

    private Date getPreciseDate(File lastFile) throws ClassNotFoundException {
        InputStream fileInputStream = IoUtil.toStream(lastFile);
        Map<String, Map> reportMapVO = getReoirtMapVO(fileInputStream);
        Date dtcTimeDate = null;
        if (reportMapVO.get("basicInfo") != null && reportMapVO.get("basicInfo").get("dtcTime") != null) {
            String dtcTimeStr = reportMapVO.get("basicInfo").get("dtcTime").toString();
            dtcTimeDate = DateUtil.parse(dtcTimeStr, new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"));
        }
        return dtcTimeDate;
    }

    private DeviceInfoVo buildDeviceInfoVo(Map<String, Map> reportMapVO,String deviceType,String deviceName) {
        DeviceInfoVo deviceInfoVo = new DeviceInfoVo();
        List<DeviceInfoComponent> deviceInfoComponents = new ArrayList<>();
        for (String ecuId:reportMapVO.keySet()){
            if (StringUtils.isEmpty(ecuId)){
                continue;
            }
            Map<String,String> map = reportMapVO.get(ecuId);
            if ("basicInfo".equals(ecuId)){
                deviceInfoVo.setCarlineType(deviceType);
                deviceInfoVo.setDeviceName(deviceName);
                deviceInfoVo.setClusterName(map.get("clusterName"));
                deviceInfoVo.setProjectType(map.get("projectType"));
                deviceInfoVo.setPlatformType(map.get("platformType"));
                deviceInfoVo.setMarketType(map.get("marketType"));
                deviceInfoVo.setCarlineModelType(map.get("carlineModelType"));
                deviceInfoVo.setVariantType(map.get("variantType"));
                deviceInfoVo.setVinCode(map.get("VIN"));
                deviceInfoVo.setBasicType(BASIC_TYPE_WEB_DEVICE);
            }else if ("005F-Data Medium".equals(ecuId)){
                deviceInfoVo.setDbVersion(map.get("SWVersion"));
            }else if ("005F-ZDC".equals(ecuId)){
                DeviceInfoComponent deviceInfoComponent = new DeviceInfoComponent();
                deviceInfoComponent.setComponentName(map.get("componentName"));
                deviceInfoComponent.setComponentType(map.get("componentType").toUpperCase());
                deviceInfoComponent.setOtherVersion(map.get("ZdcVersion"));
                deviceInfoComponent.setComponentInstanceName(map.get("ZdcName"));
                deviceInfoComponent.setEcuId(ecuId);
                deviceInfoComponents.add(deviceInfoComponent);
            }else {
                DeviceInfoComponent deviceInfoComponent = new DeviceInfoComponent();
                deviceInfoComponent.setComponentType(map.get("componentType").toUpperCase());
                deviceInfoComponent.setComponentName(map.get("componentName"));
                deviceInfoComponent.setPartNumber(map.get("HWTeilenummer"));
                deviceInfoComponent.setHwVersion(map.get("HWVersion"));
                deviceInfoComponent.setSwVersion(map.get("SWVersion"));
                deviceInfoComponent.setEcuId(ecuId);
                deviceInfoComponents.add(deviceInfoComponent);
            }
        }
        deviceInfoVo.setDeviceInfoComponents(deviceInfoComponents);
        return deviceInfoVo;
    }

    @Override
    public List<AutoSaveVersionVO> autoSaveVersionList(String carlineInfoUid) {
        if (StringUtils.isEmpty(carlineInfoUid)){
            return null;
        }
        List<AutoSaveVersionVO> autoSaveVersionVOS = tClusterMapper.selectAutoSaveVersionList(carlineInfoUid);
        return autoSaveVersionVOS;
    }

    private static String regionRegexValue(TDTCReportDTO reportDTO, Map<String, String> componentMap, String fieldName, String filedValue) {
        Map<String, String> componentNameMap = JSON.parseObject(JSON.parseObject(filedValue).toJSONString(), new TypeReference<Map>() {
        });
        for (String key : componentNameMap.keySet()) {
            Object o = ReflectUtils.invokeGetter(reportDTO, key);
            if (o == null){
                continue;
            }
            String var = regexStr(o.toString(), componentNameMap.get(key));
            if (StringUtils.isNotEmpty(var)) {
                componentMap.put(fieldName, var);
                return var;
            }
        }
        return null;
    }

    private static String regexStr(String content, String regex) {
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(regex)){
            return null;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    @Test
    public void test1() {
//        String regex = "EV_HCP3([\\S]*?)Node[\\s\\S]*?";
//        String content = "EV_HCP3BaseNodeAU41X_001001";
        String content = "MU-TH-N-CN   ";
        String regex = "[ ]*MU-T{0,1}([BHP])[\\\\s\\\\S]*?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            System.out.println(matcher.group(1));
        } else {
            System.out.println("false");
        }
    }

    /**
     * 批量删除
     *
     * @param carlineInfoUids
     * @return
     */
    @Override
    public int deleteTCarlineByUids(Long[] carlineInfoUids) {
        //相关表字段
        for (Long carlineInfoUid : carlineInfoUids) {
            TCarlineInfo tCarlineInfo = tCarlineInfoMapper.selectById(carlineInfoUid);
            if (tCarlineInfo == null) {
                continue;
            }
            TCluster tCluster = tClusterMapper.selectById(tCarlineInfo.getClusterUid());
            TCarline tCarline = tCarlineMapper.selectById(tCluster.getCarlineUid());
            cascadeDeleteVersion(carlineInfoUid, tCluster,tCarline);
        }
        return 1;
    }

    @Override
    public DeviceInfoVo getInfo(Long carlineInfoUid) {
        DeviceInfoVo deviceInfoVo = tCarlineInfoMapper.queryDeviceInfo(carlineInfoUid);
        List<DeviceInfoComponent> deviceInfoComponents = tCarlineInfoMapper.queryDeviceComponent(carlineInfoUid);
        if (CollectionUtils.isEmpty(deviceInfoComponents)) {
            return null;
        }
        Map<String, Map<String,GoldenInfoComponentDTO>> goldenInfoComponentDTOMap = null;
        if (deviceInfoVo != null && StringUtils.isNotEmpty(deviceInfoVo.getCarlineType()) &&
                StringUtils.isNotEmpty(deviceInfoVo.getClusterName()) && StringUtils.isNotEmpty(deviceInfoVo.getMarketType())) {
            List<GoldenInfoComponentDTO> goldenInfoComponentDTOS = getGoldenInfoComponents(deviceInfoVo.getCarlineModelType(), deviceInfoVo.getClusterName(), deviceInfoVo.getMarketType());
            goldenInfoComponentDTOMap = buildCompareMap(goldenInfoComponentDTOS);
        }
        Map<String, DeviceInfoComponent> deviceInfoComponentMap = new HashMap<>();
        //默认结构
        for (TDTCReport tdtcReport : tdtcReportMapper.selectList(new QueryWrapper<TDTCReport>().notLike("ecu_id", "Vehicle"))) {
            DeviceInfoComponent deviceInfoComponent = new DeviceInfoComponent();
            deviceInfoComponent.setComponentType(tdtcReport.getComponentType().toUpperCase());
            deviceInfoComponent.setEcuId(tdtcReport.getEcuId());
            if (StringUtils.isNotEmpty(tdtcReport.getComponentType())){
                deviceInfoComponentMap.put(tdtcReport.getComponentType().toUpperCase(),deviceInfoComponent);
            }
        }

        //true
        for (DeviceInfoComponent deviceInfoComponent : deviceInfoComponents) {
            if (CollectionUtils.isEmpty(deviceInfoComponents)){
                break;
            }
            String componentType = deviceInfoComponent.getComponentType();
            if (StringUtils.isEmpty(componentType)) {
                continue;
            }
            DeviceInfoComponent componentMap;
            if (deviceInfoComponentMap.get(componentType) == null) {
                componentMap = new DeviceInfoComponent();
            } else {
                componentMap = deviceInfoComponentMap.get(componentType);
            }
            if (deviceInfoComponent.getCarlineInfoUid() != null) {
                componentMap.setCarlineInfoUid(deviceInfoComponent.getCarlineInfoUid());
            }
            if (StringUtils.isNotEmpty(deviceInfoComponent.getComponentType())) {
                componentMap.setComponentType(deviceInfoComponent.getComponentType().toUpperCase());
            }
            if (StringUtils.isNotEmpty(deviceInfoComponent.getEcuId())) {
                componentMap.setEcuId(deviceInfoComponent.getEcuId());
            }
            if (StringUtils.isNotEmpty(deviceInfoComponent.getComponentInstanceName())){
                componentMap.setComponentInstanceName(deviceInfoComponent.getComponentInstanceName());
            }
            if (StringUtils.isNotEmpty(deviceInfoComponent.getComponentName())) {
                componentMap.setComponentName(deviceInfoComponent.getComponentName());
            }
            String partnumber = deviceInfoComponent.getPartNumber();
            if (StringUtils.isNotEmpty(partnumber)){
//                partnumber.replaceAll("\\u00A0+", "");
                partnumber = StrUtil.removeAll(partnumber, C_SPACE);
            }
            componentMap.setPartNumber(partnumber);
            if (StringUtils.isNotEmpty(deviceInfoComponent.getVariantType())){
                componentMap.setVariantType(deviceInfoComponent.getVariantType());
            }
            if (StringUtils.isNotEmpty(deviceInfoComponent.getComponentVersion()) && StringUtils.isNotEmpty(deviceInfoComponent.getWareType())) {
                deviceInfoComponent.setVariantType(componentMap.getVariantType());//todo youhua
                if ("SW".equals(deviceInfoComponent.getWareType())) {
                    componentMap.setSwSort(deviceInfoComponent.getSort());//联调后删除
                    componentMap.setSwVersion(deviceInfoComponent.getComponentVersion());//联调后删除
                    Map<String, DeviceCompareVO> deviceCompareMap = getDeviceCompareMap(goldenInfoComponentDTOMap, deviceInfoComponent, componentMap,deviceInfoVo.getVariantType());
                    componentMap.setDeviceCompareMap(deviceCompareMap);
                }
                if ("HW".equals(deviceInfoComponent.getWareType())) {
                    componentMap.setHwSort(deviceInfoComponent.getSort());//联调后删除
                    componentMap.setHwVersion(deviceInfoComponent.getComponentVersion());//联调后删除
                    Map<String, DeviceCompareVO> deviceCompareMap = getDeviceCompareMap(goldenInfoComponentDTOMap, deviceInfoComponent, componentMap,deviceInfoVo.getVariantType());
                    componentMap.setDeviceCompareMap(deviceCompareMap);
                }
                if ("OT".equals(deviceInfoComponent.getWareType())) {
                    componentMap.setOtherVersion(deviceInfoComponent.getComponentVersion());
                }
            }
            deviceInfoComponentMap.put(componentType, componentMap);
        }
        deviceInfoVo.setDeviceInfoComponentMap(deviceInfoComponentMap);
        return deviceInfoVo;
    }

    private Map<String, DeviceCompareVO> getDeviceCompareMap(Map<String, Map<String,GoldenInfoComponentDTO>> goldenInfoComponentDTOMap,
                                                             DeviceInfoComponent deviceInfoComponent, DeviceInfoComponent deviceMap,String variantType) {
        Map<String, DeviceCompareVO> deviceCompareVOMap = new HashMap<>();
        if (goldenInfoComponentDTOMap != null){
            DeviceCompareVO deviceCompareVO = getDeviceCompareVO(deviceInfoComponent,goldenInfoComponentDTOMap,variantType);
            if (deviceMap.getDeviceCompareMap() == null){
                deviceCompareVOMap = new HashMap<>();
            }else {
                deviceCompareVOMap = deviceMap.getDeviceCompareMap();
            }
            deviceCompareVOMap.put(deviceInfoComponent.getWareType(),deviceCompareVO);
        }
        return deviceCompareVOMap;
    }

    public List<GoldenInfoComponentDTO> getGoldenInfoComponents(String carlineModelType, String ClusterNameType, String marketType) {
        //虽然都是CLU，但名称相同，映射的字典表不同，这里需要做个转换
        String goldenClusterNameType = tMatrixMapper.selectGoldenClusterNameType(ClusterNameType);
        List<Long> goldenCarTypes = tMatrixMapper.selectGoldenCarType(carlineModelType, goldenClusterNameType);
        //理论上对应的有且仅有一个，但目前有重复的 todo
        //https://sumomoriaty.oss-cn-beijing.aliyuncs.com/zdcar/202212011111630.png
        if (CollectionUtils.isEmpty(goldenCarTypes)) {
            return null;
        }
        Long goldenCarType = goldenCarTypes.get(0);
        //因为carline/sop字段混乱，carlineModelType暂不参与比较
        return tCarlineInfoMapper.selectGoldenCarlineInfo(carlineModelType, ClusterNameType, marketType, goldenCarType);
        //结果：https://sumomoriaty.oss-cn-beijing.aliyuncs.com/zdcar/202212011422541.png
    }

    @Override
    public AjaxResult compareOneComponent(DeviceCompareParam deviceCompareParam) {
        String carlineModelType = deviceCompareParam.getCarlineModelType();
        String clusterName = deviceCompareParam.getClusterName();
        String marketType = deviceCompareParam.getMarketType();
        String componentType = deviceCompareParam.getComponentType();
        String wareType = deviceCompareParam.getWareType();
        String componentVersion = deviceCompareParam.getComponentVersion();
        if (StringUtils.isEmpty(carlineModelType) || StringUtils.isEmpty(clusterName) || StringUtils.isEmpty(marketType) ||
                StringUtils.isEmpty(componentType) || !(StringUtils.isNotEmpty(wareType) || StringUtils.isNotEmpty(componentVersion))) {
            return AjaxResult.error("Params can't be empty");
        }
        List<GoldenInfoComponentDTO> goldenInfoComponentDTOS = getGoldenInfoComponents(carlineModelType, clusterName, marketType);
        if (CollectionUtils.isEmpty(goldenInfoComponentDTOS)) {
            return AjaxResult.error("This device's goldenInfo doesn't exit");
        }
        Map<String, Map<String,GoldenInfoComponentDTO>> goldenInfoComponentDTOMap = buildCompareMap(goldenInfoComponentDTOS);
        DeviceInfoComponent deviceInfoComponent = new DeviceInfoComponent();
        deviceInfoComponent.setComponentVersion(componentVersion);
        deviceInfoComponent.setComponentType(componentType.toUpperCase());
        deviceInfoComponent.setPartNumber(deviceCompareParam.getPartNumber());
        deviceInfoComponent.setWareType(wareType);
        if (deviceCompareParam.getSort() != null){
            deviceInfoComponent.setSort(deviceCompareParam.getSort());
        }else {
            deviceInfoComponent.setSort(0);
        }
        DeviceCompareVO deviceCompareVO = getDeviceCompareVO(deviceInfoComponent, goldenInfoComponentDTOMap,deviceCompareParam.getVariantType());
        if (deviceCompareVO != null) {
            return AjaxResult.success(deviceCompareVO);
        }else {
            return AjaxResult.error("this goldenInfo doesn't hava matched components");
        }
    }

    /**
     * select tcd.ware_type as wareType,tcd.component_type as componentType,tcd.component_version as componentVersion,
     *        tc.cluster_name as clusterName,tci.carline_model_type as carlineModelType,tci.market_type as marketType,
     *        tcd.part_number as partNumber
     * from t_component_data tcd
     * left join t_carline_component tcc on (tcc.hw_version_uid = tcd.uid or tcc.sw_version_uid = tcd.uid)
     * left join t_carline_info tci on tci.carline_info_uid = tcc.carline_info_uid
     * left join t_cluster tc on tc.carline_uid = tci.carline_info_uid
     * left join t_carline tca on tca.uid = tc.carline_uid
     * where component_type = 'BCM1'
     * @param deviceInfoComponent
     * @param goldenInfoComponentMap
     * @param deviceVariantType
     * @return
     */
    private DeviceCompareVO getDeviceCompareVO(DeviceInfoComponent deviceInfoComponent, Map<String, Map<String,GoldenInfoComponentDTO>> goldenInfoComponentMap,String deviceVariantType) {
        for (String goldenComponentType : goldenInfoComponentMap.keySet()) {
            Map<String, GoldenInfoComponentDTO> partComponentMap = goldenInfoComponentMap.get(goldenComponentType);
            if ("-".equals(goldenComponentType) || partComponentMap == null){
                continue;
            }
            String cleanStr = cleanStr(goldenComponentType);
            String deviceComponentType = cleanStr(deviceInfoComponent.getComponentType());
            String deviceComponentVersion = deviceInfoComponent.getComponentVersion();
            if ((deviceComponentType.contains(cleanStr) || cleanStr.contains(deviceComponentType) || deviceComponentType.equals(cleanStr))
                    && StringUtils.isNotEmpty(deviceComponentVersion)) {
                GoldenInfoComponentDTO goldenInfoComponentDTO = null;
                //PN,VariantType对比筛选出比对配件
                if (StringUtils.isNotEmpty(deviceInfoComponent.getPartNumber()) && partComponentMap.containsKey(deviceInfoComponent.getPartNumber())){
                    goldenInfoComponentDTO = partComponentMap.get(deviceInfoComponent.getPartNumber());
                }else {
                    for (String partNumber:partComponentMap.keySet()){
                        goldenInfoComponentDTO = partComponentMap.get(partNumber);
                        if (StringUtils.isNotEmpty(deviceVariantType) && deviceVariantType.equals(partComponentMap.get(partNumber).getVariantType())){
                            goldenInfoComponentDTO = partComponentMap.get(partNumber);
                            break;
                        }
                    }
                }

                DeviceCompareVO deviceCompareVO = new DeviceCompareVO();
                Integer compareNum = compareComponent(deviceInfoComponent, goldenInfoComponentDTO);
                if (compareNum.equals(0)) {
                    continue;
                }
                deviceCompareVO.setCompareNum(compareNum);
                deviceCompareVO.setVariantType(goldenInfoComponentDTO.getVariantType());
                if ("HW".equals(deviceInfoComponent.getWareType())) {
                    deviceCompareVO.setDeviceSort(deviceInfoComponent.getSort());
                    deviceCompareVO.setDeviceComponentVersion(deviceInfoComponent.getComponentVersion());
                    deviceCompareVO.setMinimal(goldenInfoComponentDTO.getMinimalHW());
                    deviceCompareVO.setCurrentVersion(goldenInfoComponentDTO.getHwComponentVersion());//将删
                    deviceCompareVO.setGoldenSort(goldenInfoComponentDTO.getHwSort());
                    deviceCompareVO.setGoldenVersion(goldenInfoComponentDTO.getHwComponentVersion());
                    return deviceCompareVO;
                }
                if ("SW".equals(deviceInfoComponent.getWareType())) {
                    deviceCompareVO.setDeviceSort(deviceInfoComponent.getSort());
                    deviceCompareVO.setDeviceComponentVersion(deviceInfoComponent.getComponentVersion());
                    deviceCompareVO.setGoldenSort(goldenInfoComponentDTO.getSwSort());
                    deviceCompareVO.setCurrentVersion(goldenInfoComponentDTO.getSwComponentVersion());//将删
                    deviceCompareVO.setGoldenVersion(goldenInfoComponentDTO.getSwComponentVersion());
                    return deviceCompareVO;
                }
            }
        }
        return null;
    }

    private int compareComponent(DeviceInfoComponent deviceInfoComponent, GoldenInfoComponentDTO goldenInfoComponentDTO) {
        String goldenComponentVersion;
        Integer goldenComponentSort;
        Integer minimalMinNum = 999999;
        String minimal = goldenInfoComponentDTO.getMinimalHW();
        log.info("minimalVersion:{}",goldenInfoComponentDTO.getMinimalHW());
        log.info("deviceComponentVersion:{}",deviceInfoComponent.getComponentVersion());
        if ("SW".equals(deviceInfoComponent.getWareType())){
            goldenComponentVersion = goldenInfoComponentDTO.getSwComponentVersion();
            goldenComponentSort = goldenInfoComponentDTO.getSwSort();
        }else {
            goldenComponentVersion = goldenInfoComponentDTO.getHwComponentVersion();
            goldenComponentSort = goldenInfoComponentDTO.getHwSort();
            if (StringUtils.isNotEmpty(minimal)) {
                minimalMinNum = cleanNum(minimal, MIN);
            }
        }
        log.info("goldenComponentVersion:{}",goldenComponentVersion);
        if (StringUtils.isEmpty(goldenComponentVersion)) {
            return 0;
        }


        Integer goldenNormalVersionMinNum = cleanNum(goldenComponentVersion, MIN);
        Integer deviceVersionNum = cleanNum(deviceInfoComponent.getComponentVersion(), MIN);
        log.info("minimalMinNum:{}",goldenInfoComponentDTO.getMinimalHW());
        log.info("minimalMinNum:{}",goldenComponentVersion);
        log.info("deviceVersionNum:{}",deviceInfoComponent.getComponentVersion());
        if (goldenComponentSort == null || goldenComponentSort == 0 ||
                goldenInfoComponentDTO.getSort() == null || goldenInfoComponentDTO.getSort() == 0 ||
                goldenComponentSort.equals(goldenInfoComponentDTO.getSort())){
            if (deviceVersionNum >= goldenNormalVersionMinNum) {
                return 3;
            } else if (deviceVersionNum >= minimalMinNum) {
                return 2;
            } else {
                return 1;
            }
        }else if (goldenComponentSort > goldenInfoComponentDTO.getSort()){
            return 3;
        }else{
            return 1;
        }
    }

    Integer MIN = -1;
    Integer MAX = 1;

    @Test
    public void test2222(){
        Integer num = cleanNum("0/X533", 0);
        System.out.println(num);

    }

    

    private Integer cleanNum(String deviceComponent, Integer getNum) {
        if ("-".equals(deviceComponent)){
            return 99999;
        }
        String[] split = deviceComponent.split("/");
        if (MAX.equals(getNum)) {
            deviceComponent = new String(split[split.length - 1]);
        } else {
            deviceComponent = new String(split[0]);
        }
        split = deviceComponent.split("-");
        if (MAX.equals(getNum)) {
            deviceComponent = new String(split[split.length - 1]);
        } else {
            deviceComponent = new String(split[0]);
        }
        char[] chars = deviceComponent.toCharArray();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if (i == 0 && chars[i] == 48){
                continue;
            }
            if ((chars[i] >= 48 && chars[i] <= 57)) {
                buffer.append(chars[i]);//去除特殊格式
            }
        }
        if (StringUtils.isEmpty(buffer)){
            return 0;
        }
        return Integer.valueOf(buffer.toString());
    }

    private String cleanStr(String deviceComponent) {
        if (StringUtils.isNotEmpty(deviceComponent) && ("BCM1".equals(deviceComponent.toUpperCase()) || "BCM2".equals(deviceComponent.toUpperCase()) )){
            return deviceComponent;
        }
        char[] chars = deviceComponent.toCharArray();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] >= 19968 && chars[i] <= 40869) || (chars[i] >= 97 && chars[i] <= 122) || (chars[i] >= 65 && chars[i] <= 90)) {
                buffer.append(chars[i]);//去除特殊格式
            }
        }
        return new String(buffer.toString().toUpperCase());
    }

    private Map<String, Map<String,GoldenInfoComponentDTO>> buildCompareMap(List<GoldenInfoComponentDTO> goldenInfoComponentDTOS) {
        Map<String, Map<String,GoldenInfoComponentDTO>> componentDTOMap = new HashMap<>();
        if (StringUtils.isEmpty(goldenInfoComponentDTOS)){
            return null;
        }
        for (GoldenInfoComponentDTO goldenInfoComponentDTO : goldenInfoComponentDTOS) {
            String componentType = goldenInfoComponentDTO.getComponentType();
            Map<String,GoldenInfoComponentDTO> partComponentMap = null;
            GoldenInfoComponentDTO tempComponentDTO = null;
            if (componentDTOMap.get(componentType) != null){
                partComponentMap = componentDTOMap.get(componentType);
                if (partComponentMap.containsKey(goldenInfoComponentDTO.getPartNumber())){
                    tempComponentDTO = partComponentMap.get(goldenInfoComponentDTO.getPartNumber());
                }else {
                    tempComponentDTO = goldenInfoComponentDTO;
                }
            }else {
                tempComponentDTO = goldenInfoComponentDTO;
                partComponentMap = new HashMap<>();
            }
            //hw sw
            if ("HW".equals(goldenInfoComponentDTO.getWareType())){
                tempComponentDTO.setHwComponentVersion(goldenInfoComponentDTO.getComponentVersion());
                tempComponentDTO.setHwSort(goldenInfoComponentDTO.getSort());
                tempComponentDTO.setMinimalHW(goldenInfoComponentDTO.getMinimalHW());
            }
            if ("SW".equals(goldenInfoComponentDTO.getWareType())){
                tempComponentDTO.setSwComponentVersion(goldenInfoComponentDTO.getComponentVersion());
                tempComponentDTO.setSwSort(goldenInfoComponentDTO.getSort());
            }
            partComponentMap.put(goldenInfoComponentDTO.getPartNumber(),tempComponentDTO);
            componentDTOMap.put(componentType, partComponentMap);
        }
        return componentDTOMap;
    }


    /**
     * 导入设备信息
     *
     * @param deviceInfoVoList
     * @param b
     * @param operName
     * @return
     */
    @Override
    public String importDevice(Map<String, List<ImportDeviceDTO>> deviceInfoVoList, boolean b, String operName) {
        String[] dictTypes = new String[]{"clusterName", "projectType", "platformType", "marketType", "functionGroupType", "variantType", "taskType", "carlineModelType", "goldenCarType", "goldenClusterNameType"};
        Map<String, Map<String, String>> dictMap = getDictMap(dictTypes);
        for (String key : deviceInfoVoList.keySet()) {
            if (StringUtils.isEmpty(key) || !(key.equals("Devices") || key.equals("Benches"))) {
                continue;
            }
            List<ImportDeviceDTO> importDeviceDTOS = deviceInfoVoList.get(key);
            if (CollectionUtils.isEmpty(importDeviceDTOS)) {
                continue;
            }
            for (ImportDeviceDTO importDeviceDTO : importDeviceDTOS) {
                //判空
                if (importDeviceDTO == null || StringUtils.isEmpty(importDeviceDTO.getCarline())|| StringUtils.isEmpty(importDeviceDTO.getDeviceName())) {
                    continue;
                }
                //查重并删除同名重复项
                List<Long> carlineDuplicateUids= tCarlineInfoMapper.countCarlineDuplicate(importDeviceDTO.getDeviceName());
                /*if (!CollectionUtils.isEmpty(carlineDuplicateUids)){
                    deleteTCarlineByUids(carlineDuplicateUids.toArray(new Long[carlineDuplicateUids.size()]));
                }*/
                //build
                TCarline tCarline = new TCarline();
                TCluster tCluster = new TCluster();
                TCarlineInfo tCarlineInfo = new TCarlineInfo();
                if (key.equals("Devices")) {//这里的Device其实是Car
                    tCluster.setDeviceType(DEVICE_TYPE_CAR);
                } else {
                    tCluster.setDeviceType(DEVICE_TYPE_BENCH);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getDeviceName())) {
                    tCarlineInfo.setDeviceName(importDeviceDTO.getDeviceName());
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getCLU())) {
                    String clusterName = getDictValue("clusterName", dictMap, importDeviceDTO.getCLU(), DICT_MATRIXTYPE_NON);
                    tCluster.setClusterName(clusterName);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getProject())) {
                    String projectType = getDictValue("projectType", dictMap, importDeviceDTO.getProject(), DICT_MATRIXTYPE_NON);
                    tCluster.setprojectType(projectType);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getPlatform())) {
                    String platformType = getDictValue("platformType", dictMap, importDeviceDTO.getPlatform(), DICT_MATRIXTYPE_NON);
                    tCarlineInfo.setPlatformType(platformType);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getMarket())) {
                    String marketType = getDictValue("marketType", dictMap, importDeviceDTO.getMarket(), DICT_MATRIXTYPE_NON);
                    tCarlineInfo.setMarketType(marketType);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getCarline())) {
                    String carlineModelType = getDictValue("carlineModelType", dictMap, importDeviceDTO.getCarline(), DICT_MATRIXTYPE_NON);
                    tCarline.setCarlineModelType(carlineModelType);
                    tCarlineInfo.setCarlineModelType(carlineModelType);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getVariantType())) {
                    String variantType = getDictValue("variantType", dictMap, importDeviceDTO.getVariantType(), DICT_MATRIXTYPE_NON);
                    tCarlineInfo.setVariantType(variantType);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getVIN())) {
                    String vin = importDeviceDTO.getVIN();
                    tCarlineInfo.setVinCode(vin);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getDB())) {
                    String db = importDeviceDTO.getDB();
                    tCarlineInfo.setDbVersion(db);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getLastUpdated())) {
                    String lastUpdated = importDeviceDTO.getLastUpdated();
                    tCluster.setLastUpdated(lastUpdated);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getResp())) {
                    String resp = importDeviceDTO.getResp();
                    tCarlineInfo.setResp(resp);
                }
                String versionCode = DateUtil.format(new Date(), "yyMMddHHmmss");
                tCarlineInfo.setVersionCode(versionCode);
                tCarlineInfo.setBasicType(BASIC_TYPE_WEB_DEVICE);
                //版本名称
                StringBuffer autoSaveVersionName = new StringBuffer(versionCode).append("_MIB3");
                tCluster.setAutoSaveVersionName(autoSaveVersionName.toString());
                tCluster.setUpdateTime(new Date());
                tCluster.setCreateTime(new Date());
                if (CollectionUtils.isEmpty(carlineDuplicateUids)) {
                    tCarlineMapper.insert(tCarline);
                    tCluster.setCarlineUid(tCarline.getUid());
                }else {
                    //tCluster 筛选有无额外版本，并删除相关版本与配件信息
                    List<CorrentVersionDeviceDTO> correntVersionDeviceDTOS = tClusterMapper.selectCorrentVersionDeviceDTO(carlineDuplicateUids.get(0));
                    if (correntVersionDeviceDTOS.size() == MAXCIRCLE){
                        cascadeDeleteVersion(correntVersionDeviceDTOS.get(0).getCarlineInfoUid(), correntVersionDeviceDTOS.get(0).getClusterUid());
                    }
                    tCluster.setCarlineUid(correntVersionDeviceDTOS.get(0).getCarlineUid());
                }
                tClusterMapper.insert(tCluster);
                tCarlineInfo.setClusterUid(tCluster.getUid());
                tCarlineInfoMapper.insert(tCarlineInfo);
                Long carlineInfoUid = tCarlineInfo.getCarlineInfoUid();


                String[] componentTypes = {"MU","ASTERIX","KOMBI","GATEWAY","CONBOXOCU"};
                for (String componentType:componentTypes){
                    String hwProperty = componentType + "HW";
                    String swProperty = componentType + "SW";
                    if ("CONBOXOCU".equals(componentType)){
                        componentType = "CONBOX/OCU";
                    }
                    insertImportComponent(ReflectUtils.invokeGetter(importDeviceDTO,hwProperty),ReflectUtils.invokeGetter(importDeviceDTO,swProperty), carlineInfoUid, componentType);
                }
            }
        }
        return "SUCCESS!";
    }

    private void insertImportComponent(String hwVersion,String swVersion,Long carlineInfoUid, String componentType) {
        Map hwComponentBuffer = new HashMap<String,Long>();
        Map swComponentBuffer = new HashMap<String,Long>();
        List<TComponentData> componentBufferList = tComponentDataMapper.selectList(new QueryWrapper<TComponentData>()
                .eq("component_type", componentType).isNull("component_name").isNull("part_number"));
        for (TComponentData componentData:componentBufferList){
            if ("HW".equals(componentData.getWareType())){
                hwComponentBuffer.put(componentData.getComponentVersion(),componentData.getUid());
            }
            if ("SW".equals(componentData.getWareType())){
                swComponentBuffer.put(componentData.getComponentVersion(),componentData.getUid());
            }
        }
        Long hwUid = buildImportComponent(componentType, "HW", hwVersion, hwComponentBuffer);
        Long swUid = buildImportComponent(componentType, "SW", swVersion, swComponentBuffer);
        TCarlineComponent tCarlineComponent = new TCarlineComponent();
        tCarlineComponent.setSwVersionUid(swUid);
        tCarlineComponent.setHwVersionUid(hwUid);
        tCarlineComponent.setCreateTime(new Date());
        tCarlineComponent.setUpdateTime(new Date());
        tCarlineComponent.setCarlineInfoUid(carlineInfoUid);
        tCarlineComponentMapper.insert(tCarlineComponent);
    }

    private Long buildImportComponent(String componentType, String wareType, String componentVersion,Map<String,Long> componentBufferMap) {
        if (StringUtils.isEmpty(componentVersion) && StringUtils.isEmpty(wareType)) {
            return null;
        }
        TComponentData componentData = new TComponentData();
        componentData.setComponentType(componentType.toUpperCase());
//        componentData.setComponentName(componentType);
        componentData.setIsAvaliabel(1);
        componentData.setPartNumber(null);//导入不存在partNumber
        componentData.setSort(0);
        componentData.setWareType(wareType);
        componentData.setComponentVersion(componentVersion);
        if (componentBufferMap.containsKey(componentVersion)){
            return componentBufferMap.get(componentVersion);
        }else {
            insertComponent(new TCarlineComponent(), wareType, componentData,componentBufferMap);
            return componentData.getUid();
        }
    }

    private String getComponentType(String componentName) {
        for (ComponentTypeMapping typeMapping : ComponentTypeMapping.values()) {
            if (componentName.toUpperCase().contains(typeMapping.getCode()) || componentName.equals(typeMapping.getCode())){
                return typeMapping.getName();
            }
        }
        return "-";
    }

    private void insertComponent(TCarlineComponent tCarlineComponent, String wareType, TComponentData componentData){
        tCarlineComponent.setUid(null);
        /*TComponentData tComponentData = tComponentDataMapper.selectOne(new QueryWrapper<TComponentData>()
                .eq("component_type", componentData.getComponentType()).eq("component_name", componentData.getComponentName())
                .eq("ware_type", componentData.getWareType()).eq("component_version", componentData.getComponentVersion()).eq("part_number", componentData.getPartNumber()));
        */
        TComponentData tComponentData = null;
        if (StringUtils.isEmpty(componentData.getWareType())){
            return;
        }
        if (tComponentData != null) {
            if ( "SW".equals(wareType)) {
                tCarlineComponent.setSwVersionUid(tComponentData.getUid());
            } else if ("HW".equals(wareType)) {
                tCarlineComponent.setHwVersionUid(tComponentData.getUid());
            }else if ("OT".equals(wareType)) {
                tCarlineComponent.setOtherVersionUid(tComponentData.getUid());
            }
        } else {
            componentData.setUid(null);
            tComponentDataMapper.insert(componentData);
            if ("SW".equals(wareType)) {
                tCarlineComponent.setSwVersionUid(componentData.getUid());
            }
            if ("HW".equals(wareType)) {
                tCarlineComponent.setHwVersionUid(componentData.getUid());
            }
            if ("OT".equals(wareType)) {
                tCarlineComponent.setOtherVersionUid(componentData.getUid());
            }
        }
    }

    private void insertComponent(TCarlineComponent tCarlineComponent, String wareType, TComponentData componentData,Map<String,Long> componentBufferMap) {
        insertComponent(tCarlineComponent,wareType,componentData);
        componentBufferMap.put(componentData.getComponentVersion(),componentData.getUid());
    }

    @Test
    public void test3332(){
        String partnumber = "wjii kdpowkp kdowpkpo ";
        if (StringUtils.isNotEmpty(partnumber)){
//            partnumber = partnumber.replaceAll("\\u00A0+", "");
            partnumber = StrUtil.removeAll(partnumber, C_SPACE);
        }
        System.out.println(partnumber);
    }


    @Override
    public void buildUpdateComponent(DeviceInfoVo deviceInfoVo, Long carlineInfoUid) {
        if (null != deviceInfoVo.getDeviceInfoComponents() && deviceInfoVo.getDeviceInfoComponents().size() > 0) {
            for (DeviceInfoComponent deviceInfoComponent : deviceInfoVo.getDeviceInfoComponents()) {
                TComponentData componentData = new TComponentData();
                componentData.setComponentType(deviceInfoComponent.getComponentType().toUpperCase());
                componentData.setComponentName(deviceInfoComponent.getComponentName());
                componentData.setPartNumber(deviceInfoComponent.getPartNumber());
                componentData.setIsAvaliabel(1);
                componentData.setComponentInstanceName(deviceInfoComponent.getComponentInstanceName());
                String partnumber = deviceInfoComponent.getPartNumber();
                if (StringUtils.isNotEmpty(partnumber)){
//                    partnumber.replaceAll("\\u00A0+", "");
                    partnumber = StrUtil.removeAll(partnumber, C_SPACE);
                }
                deviceInfoComponent.setPartNumber(partnumber);
                componentData.setSort(0);
                TCarlineComponent tCarlineComponent = new TCarlineComponent();
                tCarlineComponent.setCarlineInfoUid(carlineInfoUid);
                tCarlineComponent.setVariantType(deviceInfoVo.getVariantType());
                if (StringUtils.isNotEmpty(deviceInfoComponent.getHwVersion())) {
                    String wareType = "HW";
                    componentData.setWareType("HW");
                    componentData.setComponentVersion(deviceInfoComponent.getHwVersion());
                    insertComponent(tCarlineComponent, wareType, componentData);
                }
                if (StringUtils.isNotEmpty(deviceInfoComponent.getSwVersion())) {
                    componentData.setWareType("SW");
                    String wareType = "SW";
                    componentData.setComponentVersion(deviceInfoComponent.getSwVersion());
                    insertComponent(tCarlineComponent, wareType, componentData);
                }
                if (StringUtils.isNotEmpty(deviceInfoComponent.getOtherVersion())){
                    String wareType = "OT";
                    componentData.setWareType("OT");
                    componentData.setComponentVersion(deviceInfoComponent.getOtherVersion());
                    insertComponent(tCarlineComponent, wareType, componentData);
                }
                tCarlineComponentMapper.insert(tCarlineComponent);
            }
        }
    }

    @Override
    public Long[] selectAllGolden() {
        List<Long> carlineInfoUids = tCarlineInfoMapper.selectAllGolden();
        return carlineInfoUids.toArray(new Long[carlineInfoUids.size()]);
    }

    /**
     * 查询字典值
     *
     * @param dictTypeName 查询的字典type名
     * @param dictMap      预设的Map
     * @param dictLabel    label值
     * @param matrixType   matrix类型
     * @return
     */
    private String getDictValue(String dictTypeName, Map<String, Map<String, String>> dictMap, String dictLabel, String matrixType) {
        String dictValue;
        Map<String, String> dictLabelMap = dictMap.get(dictTypeName);
        if (StringUtil.isNullOrEmpty(dictLabel)) {
            return null;
        }
        if (dictLabelMap == null) {
            dictLabelMap = new HashMap<String, String>();
        }
        if (!("carlineModelType".equals(dictTypeName) ||"goldenCarType".equals(dictTypeName)
                ||"clusterName".equals(dictTypeName)||"goldenClusterNameType".equals(dictTypeName))){
            dictLabel = StringUtils.getCleanStr(dictLabel);
        }else {
            dictLabel = dictLabel.toUpperCase();
        }
        //假设加入的值字典中不存在
        if (StringUtil.isNullOrEmpty(dictLabelMap.get(dictLabel))) {
            SysDictData sysDictData = new SysDictData();
            sysDictData.setDictType(dictTypeName);
            sysDictData.setMatrixType(matrixType);
            sysDictData.setStatus(DICT_STATUS_NORMAL);
            sysDictData.setDictLabel(dictLabel);
            Integer dictValueNum;
            if (dictDataMapper.selectMaxDictType(dictTypeName) != null) {
                dictValueNum = dictDataMapper.selectMaxDictType(dictTypeName) + 1;
            } else {
                dictValueNum = 1;
            }
            dictValue = dictValueNum.toString();
            sysDictData.setDictValue(dictValue);
            dictDataMapper.insert(sysDictData);
//            sysDictDataService.insertMatrixDictData(sysDictData);
            dictLabelMap.put(dictLabel, sysDictData.getDictValue());
            dictMap.put(dictTypeName, dictLabelMap);
        } else {
            //假如存在则刷为将状态刷为0
            dictValue = dictLabelMap.get(dictLabel);
//            dictDataMapper.updateDictDataStatus(dictTypeName, dictLabel, dictValue, "0",matrixType);
        }
        return dictValue;
    }

    /**
     * 手写字典映射，此处可公共或者通过反射去优化
     */
    private Map<String, Map<String, String>> getDictMap(String[] dictTypes) {
        QueryWrapper<SysDictData> dictMappingWrapper = new QueryWrapper<>();
        dictMappingWrapper.in("dict_type", dictTypes);
        Map<String, Map<String, String>> dictMap = new HashMap<>();
        for (SysDictData sysDictData : dictDataMapper.selectList(dictMappingWrapper)) {
            Map<String, String> DictDataMap;
            if (dictMap.get(sysDictData.getDictType()) != null) {
                DictDataMap = dictMap.get(sysDictData.getDictType());
            } else {
                DictDataMap = new HashMap<>();
            }
            DictDataMap.put(sysDictData.getDictLabel(), sysDictData.getDictValue());
            dictMap.put(sysDictData.getDictType(), DictDataMap);
        }
        return dictMap;
    }


    /**
     * 级联删除某一副本关联内容
     * TODO:考虑级联内容
     * @param carlineInfoUid
     * @param exClusterVersion
     */
    private void cascadeDeleteVersion(Long carlineInfoUid, Long exClusterVersion) {
        //关于向下配件的级联删除
        List<TCarlineComponent> tCarlineComponents = tCarlineComponentMapper.selectList(new QueryWrapper<TCarlineComponent>().eq("carline_info_uid", carlineInfoUid));
        if (tCarlineComponents != null && tCarlineComponents.size() > 0) {
            for (TCarlineComponent tCarlineComponent : tCarlineComponents) {
                List<TCarlineComponent> ifDeleteTCarlineSWComponents = tCarlineComponentMapper.selectList(new QueryWrapper<TCarlineComponent>().eq("sw_version_uid", tCarlineComponent.getSwVersionUid()));
                if (ifDeleteTCarlineSWComponents != null && ifDeleteTCarlineSWComponents.size() == 1) {
                    tComponentDataMapper.deleteById(ifDeleteTCarlineSWComponents.get(0).getSwVersionUid());
                }
                List<TCarlineComponent> ifDeleteTCarlineHWComponents = tCarlineComponentMapper.selectList(new QueryWrapper<TCarlineComponent>().eq("hw_version_uid", tCarlineComponent.getHwVersionUid()));
                if (ifDeleteTCarlineHWComponents != null && ifDeleteTCarlineHWComponents.size() == 1) {
                    tComponentDataMapper.deleteById(ifDeleteTCarlineHWComponents.get(0).getHwVersionUid());
                }
                List<TCarlineComponent> ifDeleteTCarlineOTComponents = tCarlineComponentMapper.selectList(new QueryWrapper<TCarlineComponent>().eq("other_version_uid", tCarlineComponent.getOtherVersionUid()));
                if (ifDeleteTCarlineOTComponents != null && ifDeleteTCarlineOTComponents.size() == 1) {
                    tComponentDataMapper.deleteById(ifDeleteTCarlineOTComponents.get(0).getOtherVersionUid());
                }
            }
        }
        tCarlineComponentMapper.delete(new QueryWrapper<TCarlineComponent>().eq("carline_info_uid", carlineInfoUid));
        tCarlineInfoMapper.deleteById(carlineInfoUid);
        //向上的级联删除
        tClusterMapper.deleteById(exClusterVersion);
    }

    /**
     * 级联删除某一副本关联内容
     * TODO:考虑级联内容
     * @param carlineInfoUid
     * @param exClusterVersion
     */
    private void cascadeDeleteVersion(Long carlineInfoUid, TCluster exClusterVersion,TCarline tCarline) {
        cascadeDeleteVersion(carlineInfoUid,exClusterVersion.getUid());
        tCarlineMapper.deleteById(tCarline);
    }

    @Override
    @Transactional
    public AjaxResult insertDeviceInfo(DeviceInfoVo deviceInfoVo) {
        if (deviceInfoVo == null || StringUtils.isEmpty(deviceInfoVo.getDeviceName())) {
            return AjaxResult.error("deviceName can't be blank");
        }
        //deviceName不得重复
        //查重
        List<Long> carlineDuplicateUids= tCarlineInfoMapper.countCarlineDuplicate(deviceInfoVo.getDeviceName());
        if (!CollectionUtils.isEmpty(carlineDuplicateUids)){
            return AjaxResult.error("deviceName can't be duplicated");
        }
        TCarlineInfo tCarlineInfo = insertBasicInfo(deviceInfoVo);

        //t_component_data与其连接表的保存
        buildUpdateComponent(deviceInfoVo, tCarlineInfo.getCarlineInfoUid());
        //保存
        return AjaxResult.success("device save success");
    }


    @Override
    public TCarlineInfo insertBasicInfo(DeviceInfoVo deviceInfoVo) {
        //创建tcarline
        TCarline tCarline = new TCarline();
        tCarline.setCreateTime(new Date());
        buildCarlinePO(deviceInfoVo, tCarline);
        tCarlineMapper.insert(tCarline);

        //t_cluster
        TCluster tCluster = new TCluster();
        tCluster.setdeviceCircleNum(1);
        tCluster.setCarlineUid(tCarline.getUid());
        tCluster.setCreateTime(new Date());
        buildTClusterPO(deviceInfoVo, tCluster);
        tClusterMapper.insert(tCluster);

        //t_carline_info
        TCarlineInfo tCarlineInfo = new TCarlineInfo();
        tCarlineInfo.setClusterUid(tCluster.getUid());
        tCarlineInfo.setCreateTime(new Date());
        buildTCarlineInfoPO(deviceInfoVo, tCarlineInfo);
        tCarlineInfoMapper.insert(tCarlineInfo);
        return tCarlineInfo;
    }


    Map<String, String> cleanMap;

    private static void buildCarlinePO(DeviceInfoVo deviceInfoVo, TCarline tCarline) {
        tCarline.setCarlineModelType(deviceInfoVo.getCarlineModelType());
//        tCarline.setgoldenCarName(deviceInfoVo.getGoldenCarName());
        tCarline.setStatus(1);
        tCarline.setUpdateTime(new Date());
        tCarline.setIsShow(1);
    }

    private static void buildTCarlineInfoPO(DeviceInfoVo deviceInfoVo, TCarlineInfo tCarlineInfo) {
        String versionCode = deviceInfoVo.getVersionCode();
        if (StringUtils.isEmpty(deviceInfoVo.getVersionCode())) {
            versionCode = DateUtil.format(new Date(), "yyMMddHHmmss");
        }
        tCarlineInfo.setVersionCode(versionCode);
        String basicType = deviceInfoVo.getBasicType();
        if (StringUtils.isEmpty(basicType)) {
            basicType = BASIC_TYPE_WEB_DEVICE;
        }
        tCarlineInfo.setBasicType(basicType);
        if (deviceInfoVo.getOriginalCarlineInfoUid() != null) {
            tCarlineInfo.setOriginalCarlineInfoUid(deviceInfoVo.getOriginalCarlineInfoUid());
        }
        tCarlineInfo.setCarlineModelType(deviceInfoVo.getCarlineModelType());
        tCarlineInfo.setPlatformType(deviceInfoVo.getPlatformType());
        tCarlineInfo.setMarketType(deviceInfoVo.getMarketType());
        tCarlineInfo.setVinCode(deviceInfoVo.getVinCode());
        tCarlineInfo.setDeviceName(deviceInfoVo.getDeviceName());
        tCarlineInfo.setVariantType(deviceInfoVo.getVariantType());
        tCarlineInfo.setDbVersion(deviceInfoVo.getDbVersion());
        tCarlineInfo.setUpdateTime(new Date());
    }

    @Test
    public void test2() {
        String now = DateUtil.format(new Date(), "yyMMddHHmmss");
        System.out.println(now);
//        DateUtil.format("")
//        DateUtil.compare()
    }

    private static void buildTClusterPO(DeviceInfoVo deviceInfoVo, TCluster tCluster) {
        tCluster.setprojectType(deviceInfoVo.getProjectType());//暂定工程项空缺不单独建表直接以名称替代
        tCluster.setLastUpdated(deviceInfoVo.getClusterLastUpdateName());//用户项暂时亦不单独建表以名称替代
        tCluster.setDeviceType(deviceInfoVo.getCarlineType());
        tCluster.setClusterName(deviceInfoVo.getClusterName());
        tCluster.setStatus(1);
        tCluster.setUpdateTime(new Date());
        tCluster.setIsShow(1);
        //版本名称
        /*时间戳_dtc_文件名   时间戳_manual*/
        String versionTimeStamp = DateUtil.format(new Date(), "yyMMddHHmmss");
        if (StringUtils.isNotEmpty(deviceInfoVo.getDtcTime())){
            Date dtcTimeDate = DateUtil.parse(deviceInfoVo.getDtcTime(), new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"));
            versionTimeStamp = DateUtil.format(dtcTimeDate, "yyMMddHHmmss");
        }
        StringBuffer autoSaveVersionName = new StringBuffer(versionTimeStamp).append("_");
        if (deviceInfoVo.getIsRefresh() != null){
            if (deviceInfoVo.getIsRefresh()){
                autoSaveVersionName.append("REFRESH");
            }
        }
        if (deviceInfoVo.getIsImportDTC() != null){
            if (deviceInfoVo.getIsImportDTC()){
                autoSaveVersionName.append("DTC");
            }else {
                autoSaveVersionName.append("MANUAL");
            }
        }
        if (deviceInfoVo.getIsModified() != null){
            if (deviceInfoVo.getIsModified()){
                autoSaveVersionName.append("(M)");
            }
        }
        tCluster.setAutoSaveVersionName(autoSaveVersionName.toString());
    }

}



















