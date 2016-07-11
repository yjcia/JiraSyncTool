package com.kewill.jira.util;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * <p>Title: ExcelUtil</p>
 * <p>Description: ExcelUtil</p>
 * <p>Copyright: Copyright (c) 2012</p>
 * <p>Company: KEWILL-IPACS Pte Ltd</p>
 *
 * @author <hongsen.lu>
 * @date 2012-08-01
 */
public class ExcelUtil {

    public static void fillCellWithValue(Sheet sheet, int row_index, int column_index, Object object) {
        Cell cell = sheet.getRow(row_index).getCell(column_index);
        if (cell != null && object != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    cell.setCellValue(object.toString());
//                    System.out.println("Blank type:("+row_index+","+column_index+")=>"+object.toString());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    cell.setCellValue(Boolean.parseBoolean(object.toString()));
                    break;
                case Cell.CELL_TYPE_ERROR:
                    cell.setCellErrorValue(Byte.parseByte(object.toString()));
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    cell.setCellFormula(object.toString());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    cell.setCellValue(Double.parseDouble(object.toString()));
//                    System.out.println("Got numeric:("+row_index+","+column_index+")=>"+object.toString());
                    break;
                case Cell.CELL_TYPE_STRING:
                    cell.setCellValue(new HSSFRichTextString(object.toString()));
//                    System.out.println("Got string type:("+row_index+","+column_index+")=>"+object.toString());
                    break;
            }
        }

//        String value = "";
//        if (object != null) {
//            value = object.toString();
//        }
//        sheet.getRow(row_index).getCell(column_index).setCellValue(value);
    }

    public static void copyRow(Sheet sheet, int sourceRowIndex, int destinationRowIndex) {
        Row newRow = sheet.getRow(destinationRowIndex);
        Row sourceRow = sheet.getRow(sourceRowIndex);
        if (newRow != null) {
            sheet.shiftRows(destinationRowIndex, sheet.getLastRowNum(), 1);
        } else {
            newRow = sheet.createRow(destinationRowIndex);
        }


        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            Cell oldCell = sourceRow.getCell(i);
            Cell newCell = newRow.createCell(i);

            if (oldCell == null) {
                newCell = null;
                continue;
            }
//            CellStyle newCellStyle = sheet.getWorkbook().createCellStyle();
            CellStyle newCellStyle = oldCell.getCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            newCell.setCellStyle(newCellStyle);

            if (oldCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            newCell.setCellType(oldCell.getCellType());
            switch (oldCell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }


        }
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangAddress = new CellRangeAddress(
                    newRow.getRowNum(),
                    (newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
                    cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
                sheet.addMergedRegion(newCellRangAddress);
            }
        }
    }


}
