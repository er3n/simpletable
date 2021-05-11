package tr.com.report.maker;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DJValueFormatter;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import tr.com.report.maker.mock.TestRepositoryProducts;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Map;

public class PlaningReportBuilder {


    public DynamicReport buildReport() throws ClassNotFoundException, JRException {

        FastReportBuilder drb = new FastReportBuilder();
        drb.addColumn("State", "state", String.class.getName(), 30)
                .addColumn("Branch", "branch", String.class.getName(), 30)
                .addColumn("Product Line", "productLine", String.class.getName(), 50)
                .addColumn("Item", "item", String.class.getName(), 50)
                .addColumn("Item Code", "id", Long.class.getName(), 30, true)
                .addColumn("Quantity", "quantity", Long.class.getName(), 60, true)
                .addColumn("Amount", "amount", Float.class.getName(), 70, true)
                .addGroups(2)
                .setTitle("November 2006 sales report")
                .setSubtitle("This report was generated at " + new Date())
                .setPrintBackgroundOnOddRows(true)
                .setUseFullPageWidth(true);

        drb.addGlobalFooterVariable(drb.getColumn(4), DJCalculation.COUNT, null, new DJValueFormatter() {

            public String getClassName() {
                return String.class.getName();
            }


            public Object evaluate(Object value, Map fields, Map variables,   Map parameters) {
                return (value == null ? "0" : value.toString()) + " Clients";
            }
        });
        DynamicReport dr = drb.build();
        return dr;
    }

    public static void main(String[] args) throws JRException, ClassNotFoundException, FileNotFoundException {
        DynamicReport dr = new PlaningReportBuilder().buildReport();
        JRDataSource ds = new JRBeanCollectionDataSource(TestRepositoryProducts.getDummyCollection());
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);
        ReportExporter.exportReportToPdf(jp, System.getProperty("user.dir")+ "/target/reports/" + PlaningReportBuilder.class.getName() + ".pdf");
    }

}
