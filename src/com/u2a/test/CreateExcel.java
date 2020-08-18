package com.u2a.test;

// 生成Excel的类
import  java.io.File;

import  jxl.Workbook;
import  jxl.write.Label;
import  jxl.write.WritableSheet;
import  jxl.write.WritableWorkbook;

public   class  CreateExcel  {
     public   static   void  main(String args[])  {
         try   {
             //  打开文件
            WritableWorkbook book  =  Workbook.createWorkbook( new  File( "c://test.xls" ));
             //  生成名为“第一页”的工作表，参数0表示这是第一页
            WritableSheet sheet  =  book.createSheet( "第一页" ,  0 );
             //  在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
             //  以及单元格内容为test
            sheet.mergeCells(0, 6, 3, 8);
            Label label = new Label(0, 6, "合并了12个单元格");
            sheet.addCell(label);
            
            //Label label1  =   new  Label( 11,2 ,"test" );
            
             //  将定义好的单元格添加到工作表中
            //sheet.addCell(label);

             /*
             * 生成一个保存数字的单元格 必须使用Number的完整包路径，否则有语法歧义 单元格位置是第二列，第一行，值为789.123
              */
            jxl.write.Number number  =   new  jxl.write.Number( 1 ,  0 ,  555.12541 );
            sheet.addCell(number);

             //  写入数据并关闭文件
            book.write();
            book.close();

        }   catch  (Exception e)  {
            System.out.println(e);
        }
    }
}
