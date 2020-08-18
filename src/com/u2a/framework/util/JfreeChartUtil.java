package com.u2a.framework.util;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.TextAnchor;


/**      
 * 类名称：JfreeChartUtil   
 * 类描述：JfreeChart报表工具类   
 * 创建人：庞海超 
 * 创建时间：2013年3月19日 09:33:29 
 */
  
public class JfreeChartUtil {
	
//	private static final String CHART_PATH = "d:/";

	public static void main(String[] args) throws IOException {
		JfreeChartUtil.createBar("肉类销量统计图", "肉类", "销量", getDataSet(), "d:/bar.png","");
		// test.createBarChart(getDataSet(), "肉类", "销量", "肉类销量统计图",
		// "bbarbar.png");

	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void createBar(String chartTitle, String xName, String yName,
			CategoryDataset dataset, String chartPath ,String chartName) throws IOException {

		JFreeChart chart = ChartFactory.createBarChart3D(chartTitle, // 图表标题
				xName, // 目录轴的显示标
				yName, // 数值轴的显示标签
				dataset, // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直
				true, // 是否显示图例(对于简单的柱状图必须是false)
				false, // 是否生成工具
				false // 是否生成URL链接
				);

		chart.setTitle(new TextTitle(chartTitle,
				new Font("黑体", Font.ITALIC, 22)));
		chart.setBackgroundPaint(ChartColor.white); 
		LegendTitle legend = chart.getLegend(0);
		legend.setItemFont(new Font("宋体", Font.BOLD, 14));
		// 设置不显示图例
		chart.getLegend().setVisible(false);

		//获得图表区域对象
		CategoryPlot plot = chart.getCategoryPlot();
		
		// 如果没有数据则显示
		plot.setNoDataMessage("没有可供使用的数据！");
		plot.setNoDataMessageFont(new Font("隶属", Font.BOLD, 18));
		plot.setNoDataMessagePaint(Color.blue);
		
		//设置图表的纵轴和横轴CategoryAxis
		CategoryAxis categoryAxis = plot.getDomainAxis();
		categoryAxis.setLabelFont(new Font("隶属", Font.BOLD, 18));
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		categoryAxis.setTickLabelFont(new Font("隶属", Font.BOLD, 18));
		categoryAxis.setAxisLineVisible(false);
//		categoryAxis.setCategoryMargin(0.1);

		// 设置从坐标为整数
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberAxis.setLabelFont(new Font("隶属", Font.BOLD, 18));

		
		BarRenderer3D renderer = new BarRenderer3D();
		
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());//显示每个柱的数值 
		renderer.setBaseItemLabelsVisible(true); 
		//注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题 
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition( 
		ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER)); 
		renderer.setItemLabelAnchorOffset(10D);// 设置柱形图上的文字偏离值 
		renderer.setItemLabelFont(new Font("黑体", Font.BOLD, 12));// 12号黑体加粗
		renderer.setItemLabelPaint(Color.black);// 字体为黑色
		renderer.setItemLabelsVisible(true); 
		
		
//		renderer.setItemLabelsVisible(true);// 显示每个柱的数值
//		// 显示每个柱的数值，并修改该数值的字体属性
//		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
//		renderer.setItemLabelFont(new Font("黑体", Font.BOLD, 12));// 12号黑体加粗
//		renderer.setItemLabelPaint(Color.black);// 字体为黑色
//		renderer.setIncludeBaseInRange(true);
		
		plot.setRenderer(renderer);// 使用我们设计的效果

		
		FileOutputStream fos = new FileOutputStream(chartPath + chartName);
		ChartUtilities.writeChartAsJPEG(fos, 1, chart, 1350, 600, null);
		fos.close();
	}

	private static CategoryDataset getDataSet() {
		double[][] data = new double[][] { { 1310,123,123,125,154}};
		String[] rowKeys = { "广州深圳东莞西安"  };
		String[] columnKeys = { "广州", "深圳", "东莞", "佛山", "西安" };

		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
				rowKeys, columnKeys, data);
		return dataset;
	}

	/**
	 * 判断文件夹是否存在，如果不存在则新建
	 * 
	 * @param chartPath
	 */
	private static void isChartPathExist(String chartPath) {
		DefaultCategoryDataset ds = new DefaultCategoryDataset();
		File file = new File(chartPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	/**
     * 饼状图
     * 
     * @param dataset 数据集
     * @param chartTitle 图标题
     * @param charName 生成图的名字
     * @param pieKeys 分饼的名字集
     * @return
     */
    public static void createValidityComparePimChar(String chartTitle,PieDataset dataset,
             String chartPath,String charName, String[] pieKeys)throws IOException  {
        JFreeChart chart = ChartFactory.createPieChart3D(chartTitle, // chart
                // title
                dataset,// data
                true,// include legend
                true, false);

        // 使下说明标签字体清晰,去锯齿类似于
        // chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);的效果
        chart.setTextAntiAlias(false);
        // 图片背景色
        chart.setBackgroundPaint(Color.white);
        // 设置图标题的字体重新设置title
        Font font = new Font("宋体", Font.BOLD, 25);
        TextTitle title = new TextTitle(chartTitle);
        chart.getLegend().setItemFont(new Font("宋体", 1, 13));
        title.setFont(font);
        chart.setTitle(title);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        // 图片中显示百分比:默认方式

        // 指定饼图轮廓线的颜色
        // plot.setBaseSectionOutlinePaint(Color.BLACK);
        // plot.setBaseSectionPaint(Color.BLACK);

        // 设置无数据时的信息
        plot.setNoDataMessage("无对应的数据，请重新查询。");

        // 设置无数据时的信息显示颜色
        plot.setNoDataMessagePaint(Color.red);

        // 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}={1}({2})", NumberFormat.getNumberInstance(),
                new DecimalFormat("0.00%")));
        // 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}={1}({2})"));

        plot.setLabelFont(new Font("宋体", Font.TRUETYPE_FONT, 12));
        // 指定图片的透明度(0.0-1.0)
        plot.setForegroundAlpha(0.65f);
        // 指定显示的饼图上圆形(false)还椭圆形(true)
        plot.setCircular(false, true);

        // 设置第一个 饼块section 的开始位置，默认是12点钟方向
        plot.setStartAngle(90);

        // // 设置分饼颜色
        //plot.setSectionPaint(pieKeys[0], new Color(244, 194, 144));
        //plot.setSectionPaint(pieKeys[1], new Color(144, 233, 144));

        FileOutputStream fos = new FileOutputStream(chartPath + charName);
            // 高宽的设置影响椭圆饼图的形状
        ChartUtilities.writeChartAsJPEG(fos, chart, 1250, 600);


    }
}