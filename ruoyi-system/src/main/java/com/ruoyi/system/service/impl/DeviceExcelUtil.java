package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.annotation.Excel.Type;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.poi.ExcelHandlerAdapter;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.reflect.ReflectUtils;
import com.ruoyi.system.domain.dto.ImportDeviceDTO;
import com.ruoyi.system.domain.dto.ImportGoldenInfoDTO;
import com.ruoyi.system.domain.dto.ImportPartComponentDTO;
import com.ruoyi.system.mapper.TCleanMappingMapper;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel相关处理
 * 
 * @author ruoyi
 */
public class DeviceExcelUtil<T> extends ExcelUtil<T>
{
    @Autowired
    private TCleanMappingMapper tCleanMappingMapper;
    private static final Logger log = LoggerFactory.getLogger(DeviceExcelUtil.class);
    //该region首行
    Integer startRowNum = 0;
    //该region首列
    Integer startColNum = 0;

    /**
     * 工作薄对象
     */
    public DeviceExcelUtil(Class<T> clazz) {
        super(clazz);
    }

    /**
     * 获取单元格值
     * @param cell
     * @return
     */
    public static String getCellStringValue(Cell cell) {
        if (cell == null)
            return "";
        String cellValue = "";
        switch (cell.getCellTypeEnum().toString()) {
            case "STRING":// 字符串类型
                cellValue = cell.getStringCellValue();
                if (cellValue.trim().equals("") || cellValue.trim().length() <= 0)
                    cellValue = null;
                break;
            case "NUMERIC": // 数值类型
                short i = cell.getCellStyle().getDataFormat();
                if (DateUtil.isCellDateFormatted(cell) || i == 56) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
                            .getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy/MM/dd");
                    }
                    Date date = cell.getDateCellValue();
                    cellValue = sdf.format(date);
                }else {
                    double value = cell.getNumericCellValue();
                    cellValue = String.valueOf(value);
                }
                break;
            case "FORMULA": // 公式
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case "BLANK":
                cellValue = "";
                break;
            case "BOOLEAN":
                break;
            case "ERROR":
                break;
            default:
                break;
        }
        return cellValue;
    }

    /**
     * 对excel表单指定表格索引名转换成list
     *
     * @param is 输入流
     * @return 转换后集合
     */
    public List<T> importGoldenInfoExcel(InputStream is,List<ImportGoldenInfoDTO> importGoldenInfoDTOS) throws Exception
    {
        super.type = Type.IMPORT;
        super.wb = WorkbookFactory.create(is);
        int numberOfSheets = wb.getNumberOfSheets();
//        System.out.println("numberOfSheets:" + numberOfSheets);


        for (int i = 0;i < numberOfSheets;i++){
            Sheet sheet = wb.getSheetAt(i);
            String sheetName = new String(sheet.getSheetName());
//            System.out.println("sheetName" +i + ":" + sheetName);

            //
            while (true){//20:最多20个区块
                if (startColNum > 150){
                    break;
                }
                if (cellValueIsNotEmpty(sheet.getRow(startRowNum), startColNum)) {
                    log.info("startRowNum1:{},startColNum1:{}的值存在",startRowNum,startColNum);
                    String marketDetail = getMarketDetail(sheet);
                    List<ImportPartComponentDTO> list = getRegionList(sheet);
                    //
                    ImportGoldenInfoDTO importGoldenInfoDTO = new ImportGoldenInfoDTO();
                    importGoldenInfoDTO.setComponentDTOS(list);
                    importGoldenInfoDTO.setSheetName(sheetName);
                    importGoldenInfoDTO.setMarketDetail(marketDetail);
                    importGoldenInfoDTOS.add(importGoldenInfoDTO);
                    //
                    log.info("startRowNum2:{},startColNum2:{}变更后",startRowNum,startColNum);
                }else {
                    log.info("startRowNum3:{},startColNum3:{}的值不存在",startRowNum,startColNum);
                    getStartNum(sheet);
                    log.info("startRowNum4:{},startColNum4:{}查找后",startRowNum,startColNum);
                }
            }
            startRowNum = 0;
            startColNum = 0;
        }


        //获取首行
        List<T> list1 = new ArrayList<T>();

        return list1;
    }

    /**
     * 从首行往上寻找market的相关信息
     * @param sheet
     * @return
     */
    private String getMarketDetail(Sheet sheet) {
        for (int i = startRowNum - 1; i >= 0; i--){
            if (cellValueIsNotEmpty(sheet.getRow(i),startColNum)){
                return this.getCellValue(sheet.getRow(i), startColNum).toString();
            }
        }
        return null;
    }

    private Boolean cellValueIsNotEmpty(Row row, int a) {
        Object stringCellValue = this.getCellValue(row, a);
        Boolean cellValueIsNull = stringCellValue != null && StringUtils.isNotEmpty(stringCellValue.toString());
        return cellValueIsNull;
    }

    private void getStartNum(Sheet sheet) {
        Integer tempCol = startColNum;
        for (int col = startColNum;col < startColNum + 20; col++){
            for (int row = startRowNum; row < startRowNum + 20;row++){
                if (isCellNotEmpty(sheet.getRow(row),col)){
                    startRowNum = row;
                    startColNum = col;
                    return;
                }
            }
        }
        startColNum = tempCol + 19;//找到这儿说明该范围内没搜寻到
    }


    /**
     * 获取单元格值
     *
     * @param row 获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getTitleCellValue(Row row, int column)
    {
        if (row == null)
        {
            return row;
        }
        Object val = "";
        try
        {
            Cell cell = row.getCell(column);
            if (StringUtils.isNotNull(cell))
            {
                if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA)
                {
                    val = cell.getNumericCellValue();
                    if (DateUtil.isCellDateFormatted(cell))
                    {
                        val = DateUtil.getJavaDate((Double) val); // POI Excel 日期格式转换
                    }
                    else
                    {
                        if ((Double) val % 1 != 0)
                        {
                            val = new BigDecimal(val.toString());
                        }
                        else
                        {
                            val = new DecimalFormat("0").format(val);
                        }
                    }
                }
                else if (cell.getCellType() == CellType.STRING)
                {
                    val = cell.getStringCellValue();
                }
                else if (cell.getCellType() == CellType.BOOLEAN)
                {
                    val = cell.getBooleanCellValue();
                }
                else if (cell.getCellType() == CellType.ERROR)
                {
                    val = cell.getErrorCellValue();
                }

            }
        }
        catch (Exception e)
        {
            return val;
        }
        return val;
    }

    private static int MAXROW = 30;
    private static int MAXCOL = 200;
    /**
     * 获取某一区块下的GoldenInfo数据
     * @param sheet sheet页
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private List<ImportPartComponentDTO> getRegionList(Sheet sheet) throws InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<T>();
        Integer totalCol = 0;
        Row startRow = sheet.getRow(startRowNum);
        // 获取结束行
        int endRowNum = startRowNum;
        for (int i = startRowNum; i < startRowNum + MAXROW; i++){//heard.getPhysicalNumberOfCells() 200列的大值
            Boolean Cell0 = cellValueIsNotEmpty(sheet.getRow(i), startColNum + 1);
            Boolean Cell3 = cellValueIsNotEmpty(sheet.getRow(i), startColNum);
            Boolean Cell1 = cellValueIsNotEmpty(sheet.getRow(i + 1), startColNum + 1);
            Boolean Cell4 = cellValueIsNotEmpty(sheet.getRow(i + 1), startColNum);
            Boolean Cell2 = cellValueIsNotEmpty(sheet.getRow(i + 2), startColNum + 1);
            Boolean Cell5 = cellValueIsNotEmpty(sheet.getRow(i + 2), startColNum);

            //防止合并单元格，三行同假为假，但不可避免有的不完全合并下最后存在数据的，如https://sumomoriaty.oss-cn-beijing.aliyuncs.com/zdcar/202211181529968.png
            if (!(Cell0 || Cell1 || Cell2 || Cell3 || Cell4 || Cell5)){
                endRowNum = i;
                break;
            }

        }
//        System.out.println("此次循环最后一行的值为：" + endRowNum);
        //获取结束列
        Integer endColNum = startColNum;
        for (int i = startColNum + 1; i < startColNum + MAXCOL; i++){
            Boolean Cell0 = cellValueIsNotEmpty(sheet.getRow(startRowNum), i + 0);

            if (!(Cell0)){//列合并没法管
                endColNum = i;
                break;
            }
        }
//        System.out.println("此次循环最后一列的值为：" + endColNum);

        if (endRowNum > 0)
        {
            // 定义一个map用于存放excel列的序号和field.
            Map<String, Integer> cellMap = new HashMap<String, Integer>();

            // 获取表头值
            for (int i = startColNum; i < endColNum; i++)
            {
                Cell cell = startRow.getCell(i);
                if (StringUtils.isNotNull(cell))
                {//获取cell方式存在问题，出现多解析抑或少解析，此方法在子类重写，cell直接等价复制之后拷贝过来即可，不用处理额外逻辑
                    String value = this.getTitleCellValue(startRow, i).toString();
                    cellMap.put(value, i);
                }
                else
                {
                    cellMap.put(null, i);
                }
            }
//            System.out.println("cellMap" + cellMap);
            // 有数据时才处理 得到类的所有field.
            List<Object[]> fields = super.getFields();
            Map<Integer, Object[]> fieldsMap = new HashMap<Integer, Object[]>();
            for (Object[] objects : fields) {
                Excel attr = (Excel) objects[1];
                String typeName = attr.name();
                Integer column = cellMap.get(typeName);
                if (column != null) {
                    fieldsMap.put(column, objects);
                    continue;
                }
                if (cleanMap == null){
                    cleanMap = new HashMap<String,String>();
                }
                //找临时存储的数据集
                if (cleanMap.get(typeName) != null){
                    Integer cleanColumn = cellMap.get(cleanMap.get(typeName));
                    if (cleanColumn != null)
                    {
                        fieldsMap.put(cleanColumn, objects);
                        continue;
                    }
                }
                //简单的数据清洗
                simpleDateClean(cellMap, fieldsMap, objects, typeName);
            }

            if (cellMap != null) {
                totalCol = cellMap.size();
                if (cellMap.containsKey("")){
                    totalCol--;
                }
                if (cellMap.containsKey(null)){
                    totalCol--;
                }
            }

            for (int i = startRowNum + 1; i <= endRowNum; i++)
            {
                // 从第2行开始取数据,默认第一行是表头.
                Row row = sheet.getRow(i);
                T entity = null;
                for (Map.Entry<Integer, Object[]> entry : fieldsMap.entrySet())
                {
                    Object val = this.getCellValue(row, entry.getKey());
                    // 如果不存在实例则新建.
                    entity = (entity == null ? clazz.newInstance() : entity);
                    // 从map中得到对应列的field.
                    Field field = (Field) entry.getValue()[0];
                    String propertyName = field.getName();
                    ReflectUtils.invokeSetter(entity, propertyName, val);
                }
                list.add(entity);
            }
        }

        List<ImportPartComponentDTO> importPartComponentDTOS = new ArrayList<>();
        if (list != null && list.size() > 1){
            ImportPartComponentDTO firstComponentDTO = (ImportPartComponentDTO) list.get(0);
            if (firstComponentDTO != null){
                firstComponentDTO.setRowNum(startRowNum + 1);
                importPartComponentDTOS.add(firstComponentDTO);
            }

            for (int i = 1;i < list.size();i++){
                ImportPartComponentDTO nowGoldenInfo = (ImportPartComponentDTO) list.get(i);
                if (nowGoldenInfo == null){
                    continue;
                }
                ImportPartComponentDTO lastGoldenInfo = importPartComponentDTOS.get(i - 1);
                if (nowGoldenInfo != null && StringUtils.isEmpty(nowGoldenInfo.getCOMPONENTS())){
                    nowGoldenInfo.setCOMPONENTS(lastGoldenInfo.getCOMPONENTS());
                }
                    nowGoldenInfo.setRowNum(startRowNum + 1);
//                    System.out.println("nowGoldenInfo" + nowGoldenInfo);
                    importPartComponentDTOS.add(nowGoldenInfo);
            }
        }
        if (totalCol != 0) {
            //startRowNum偏移
            System.out.println("加前：startRowNum:" + startRowNum + ",startColNum:" + startColNum + ",totalCol:" + totalCol);
            startRowNum = 0;
            //startCol偏移
            startColNum = startColNum + totalCol;
            System.out.println("加后：startRowNum:" + startRowNum + ",startColNum:" + startColNum + ",totalCol:" + totalCol);
        }else {
            startColNum++;
        }
        return importPartComponentDTOS;

    }

     Map<String,String> cleanMap;
    /**
     * 简单的数据清洗
     * @param cellMap 列名，列值键值对
     * @param fieldsMap 真实属性键值对
     * @param objects fieldsMap.put(column, objects);
     * @param dtoTypeName ExcelDTO中的名字.
     */
    private void simpleDateClean(Map<String, Integer> cellMap, Map<Integer, Object[]> fieldsMap, Object[] objects, String dtoTypeName) {
        char[] dtoTypeNameChars = dtoTypeName.toCharArray();
        StringBuffer dtoTypeNameCharsBuffer=new StringBuffer();
        for(int i = 0; i < dtoTypeNameChars.length; i ++) {
            if((dtoTypeNameChars[i] >= 19968 && dtoTypeNameChars[i] <= 40869) || (dtoTypeNameChars[i] >= 97 && dtoTypeNameChars[i] <= 122) || (dtoTypeNameChars[i] >= 65 && dtoTypeNameChars[i] <= 90)) {
                dtoTypeNameCharsBuffer.append(dtoTypeNameChars[i]);//去除特殊格式
            }
        }
        String typeName = new String(dtoTypeNameCharsBuffer.toString().toUpperCase());
        for (String trueCellName: cellMap.keySet()){
            char[] chars = trueCellName.toCharArray();
            StringBuffer buffer=new StringBuffer();
            for(int i = 0; i < chars.length; i ++) {
                if((chars[i] >= 19968 && chars[i] <= 40869) || (chars[i] >= 97 && chars[i] <= 122) || (chars[i] >= 65 && chars[i] <= 90)) {
                    buffer.append(chars[i]);//去除特殊格式
                }
            }
            String cleanTypeName = new String(buffer.toString().toUpperCase());
//                    System.out.println("cleanTypeName:" + cleanTypeName + ",trueCellName:" + trueCellName);
            if (cleanTypeName.contains(typeName) || typeName.contains(cleanTypeName) || typeName.toUpperCase().equals(cleanTypeName)){
                Integer tempColumn = cellMap.get(trueCellName);
                if (tempColumn != null) {
                    fieldsMap.put(tempColumn, objects);
                    cleanMap.put(dtoTypeName,trueCellName);
                    break;
                }
            }
            /*TCleanMapping tCleanMapping = tCleanMappingMapper.selectOne(new QueryWrapper<TCleanMapping>().like("confuse_data", cleanTypeName)
                    .eq("use_scene","1"));
            if (tCleanMapping != null && StringUtils.isNotEmpty(tCleanMapping.getGuessValue())){
                String confuseData = tCleanMapping.getConfuseData();
                Integer tempColumn = cellMap.get(confuseData);
                if (tempColumn != null) {
                    fieldsMap.put(tempColumn, objects);
                    break;
                }
            }*/
        }
    }

    private Boolean isCellNotEmpty(Row row, int col) {
        Boolean Cell0;
        if (this.getCellValue(row, col + 0) == null){
            return false;
        }else {
            Cell0 = StringUtils.isNotEmpty(this.getCellValue(row, col + 0).toString());
        }
        Boolean Cell1;
        if (this.getCellValue(row, col + 1) == null){
            return false;
        }else {
            Cell1 = StringUtils.isNotEmpty(this.getCellValue(row, col + 1).toString());
        }
        Boolean Cell2;
        if (this.getCellValue(row, col + 2) == null){
            return false;
        }else {
            Cell2 = StringUtils.isNotEmpty(this.getCellValue(row, col + 2).toString());
        }
        Boolean Cell3;
        if (this.getCellValue(row, col + 3) == null){
            return false;
        }else {
            Cell3 = StringUtils.isNotEmpty(this.getCellValue(row, col + 3).toString());
        }
        Boolean Cell4;
        if (this.getCellValue(row, col + 4) == null){
            return false;
        }else {
            Cell4 = StringUtils.isNotEmpty(this.getCellValue(row, col + 4).toString());
        }

        if (Cell0 && Cell1 && Cell2 && Cell3 && Cell4){
            return true;
        }
        return false;
    }


    /**
     * 对excel表单指定表格索引名转换成list
     *
     * @param is 输入流
     * @return 转换后集合
     */
    public List<T> importDeviceExcel(InputStream is,Map<String,List<ImportDeviceDTO>> importDeviceMap) throws Exception
    {
        super.type = Type.IMPORT;
        super.wb = WorkbookFactory.create(is);
        int numberOfSheets = wb.getNumberOfSheets();
//        System.out.println("numberOfSheets:" + numberOfSheets);

        for (int j = 0;j < numberOfSheets;j++) {
            Sheet sheet = wb.getSheetAt(j);
            String sheetName = sheet.getSheetName();
            if (StringUtils.isEmpty(sheetName)){
                continue;
            }
            char[] chars = sheetName.toCharArray();
            StringBuffer buffer=new StringBuffer();
            for(int i = 0; i < chars.length; i ++) {
                if((chars[i] >= 19968 && chars[i] <= 40869) || (chars[i] >= 97 && chars[i] <= 122) || (chars[i] >= 65 && chars[i] <= 90)) {
                    buffer.append(chars[i]);//去除特殊格式
                }
            }
            String cleanTypeName = new String(buffer.toString().toUpperCase());
            if (cleanTypeName.contains("DEVICE") || cleanTypeName.contains("BENCH") ||
                    cleanTypeName.contains("DEVICE") || cleanTypeName.equals("BENCH")){
                importDeviceSheet(importDeviceMap, sheet);
            }
        }
            return null;
        }

    private void importDeviceSheet(Map<String, List<ImportDeviceDTO>> importDeviceMap, Sheet sheet) throws InstantiationException, IllegalAccessException, IOException {
        String sheetName = new String(sheet.getSheetName());
        Integer titleNum = 0;//标题占用行数
        List<T> list = new ArrayList<T>();

        boolean isXSSFWorkbook = !(wb instanceof HSSFWorkbook);
        Map<String, PictureData> pictures;
        if (isXSSFWorkbook)
        {
            pictures = getSheetPictures07((XSSFSheet) sheet, (XSSFWorkbook) wb);
        }
        else
        {
            pictures = getSheetPictures03((HSSFSheet) sheet, (HSSFWorkbook) wb);
        }
        // 获取最后一个非空行的行下标，比如总行数为n，则返回的为n-1
        int rows = sheet.getLastRowNum();
        if (rows > 0)
        {
            // 定义一个map用于存放excel列的序号和field.
            Map<String, Integer> cellMap = new HashMap<String, Integer>();
            // 获取表头
            Row heard = sheet.getRow(titleNum);
            for (int i = 0; i < heard.getPhysicalNumberOfCells(); i++)
            {
                Cell cell = heard.getCell(i);
                if (StringUtils.isNotNull(cell))
                {
                    String value = this.getCellValue(heard, i).toString();
                    cellMap.put(value, i);
                }
                else
                {
                    cellMap.put(null, i);
                }
            }
            // 有数据时才处理 得到类的所有field.
            List<Object[]> fields = super.getFields();
            Map<Integer, Object[]> fieldsMap = new HashMap<Integer, Object[]>();
            for (Object[] objects : fields)
            {
                Excel attr = (Excel) objects[1];
                String typeName = attr.name();
                Integer column = cellMap.get(typeName);
                if (column != null)
                {
                    fieldsMap.put(column, objects);
                    continue;
                }
                if (cleanMap == null){
                    cleanMap = new HashMap<>();
                }
                //找存储的数据集
                if (cleanMap.get(typeName) != null){
                    Integer cleanColumn = cellMap.get(cleanMap.get(typeName));
                    if (cleanColumn != null)
                    {
                        fieldsMap.put(cleanColumn, objects);
                        continue;
                    }
                }
                //简单的数据清洗
                simpleDateClean(cellMap, fieldsMap, objects, typeName);
            }
            for (int i = titleNum + 1; i <= rows; i++)
            {
                // 从第2行开始取数据,默认第一行是表头.
                Row row = sheet.getRow(i);
                // 判断当前行是否是空行
                if (isRowEmpty(row))
                {
                    continue;
                }
                T entity = null;
                for (Map.Entry<Integer, Object[]> entry : fieldsMap.entrySet())
                {
                    Object val = this.getCellValue(row, entry.getKey());

                    // 如果不存在实例则新建.
                    entity = (entity == null ? clazz.newInstance() : entity);
                    // 从map中得到对应列的field.
                    Field field = (Field) entry.getValue()[0];
                    Excel attr = (Excel) entry.getValue()[1];
                    // 取得类型,并根据对象类型设置值.
                    Class<?> fieldType = field.getType();
                    if (String.class == fieldType)
                    {
                        String s = Convert.toStr(val);
                        if (StringUtils.endsWith(s, ".0"))
                        {
                            val = StringUtils.substringBefore(s, ".0");
                        }
                        else
                        {
                            String dateFormat = field.getAnnotation(Excel.class).dateFormat();
                            if (StringUtils.isNotEmpty(dateFormat))
                            {
                                val = parseDateToStr(dateFormat, val);
                            }
                            else
                            {
                                val = Convert.toStr(val);
                            }
                        }
                    }
                    else if ((Integer.TYPE == fieldType || Integer.class == fieldType) && StringUtils.isNumeric(Convert.toStr(val)))
                    {
                        val = Convert.toInt(val);
                    }
                    else if ((Long.TYPE == fieldType || Long.class == fieldType) && StringUtils.isNumeric(Convert.toStr(val)))
                    {
                        val = Convert.toLong(val);
                    }
                    else if (Double.TYPE == fieldType || Double.class == fieldType)
                    {
                        val = Convert.toDouble(val);
                    }
                    else if (Float.TYPE == fieldType || Float.class == fieldType)
                    {
                        val = Convert.toFloat(val);
                    }
                    else if (BigDecimal.class == fieldType)
                    {
                        val = Convert.toBigDecimal(val);
                    }
                    else if (Date.class == fieldType)
                    {
                        if (val instanceof String)
                        {
                            val = DateUtils.parseDate(val);
                        }
                        else if (val instanceof Double)
                        {
                            val = DateUtil.getJavaDate((Double) val);
                        }
                    }
                    else if (Boolean.TYPE == fieldType || Boolean.class == fieldType)
                    {
                        val = Convert.toBool(val, false);
                    }
                    if (StringUtils.isNotNull(fieldType))
                    {
                        String propertyName = field.getName();
                        if (StringUtils.isNotEmpty(attr.targetAttr()))
                        {
                            propertyName = field.getName() + "." + attr.targetAttr();
                        }
                        else if (StringUtils.isNotEmpty(attr.readConverterExp()))
                        {
                            val = reverseByExp(Convert.toStr(val), attr.readConverterExp(), attr.separator());
                        }
                        else if (StringUtils.isNotEmpty(attr.dictType()))
                        {
                            val = reverseDictByExp(Convert.toStr(val), attr.dictType(), attr.separator());
                        }
                        else if (!attr.handler().equals(ExcelHandlerAdapter.class))
                        {
                            val = dataFormatHandlerAdapter(val, attr);
                        }
                        else if (ColumnType.IMAGE == attr.cellType() && StringUtils.isNotEmpty(pictures))
                        {
                            PictureData image = pictures.get(row.getRowNum() + "_" + entry.getKey());
                            if (image == null)
                            {
                                val = "";
                            }
                            else
                            {
                                byte[] data = image.getData();
                                val = FileUtils.writeImportBytes(data);
                            }
                        }
                        ReflectUtils.invokeSetter(entity, propertyName, val);
                    }
                }
                list.add(entity);
            }
        }
        if (list != null && list.size() > 0){
            List<ImportDeviceDTO> importDeviceDTOS = new ArrayList<>();
            for (int i = 0;i < list.size();i++){
                ImportDeviceDTO nowGoldenInfo = (ImportDeviceDTO) list.get(i);
                importDeviceDTOS.add(nowGoldenInfo);
            }
            importDeviceMap.put(sheetName,importDeviceDTOS);
        }
    }

}
