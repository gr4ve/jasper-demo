package jasper.demo;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvMetadataExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsMetadataExporter;
import net.sf.jasperreports.engine.export.JsonMetadataExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsMetadataReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.export.type.PdfaConformanceEnum;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gr4ve
 */
public class JasperApp extends AbstractSampleApp {

    /**
     *
     */
    public static void main(String[] args) {
        main(new JasperApp(), args);
    }

    /**
     *
     */
    public void test() throws JRException {
        compile();
        fill();
        pdf();
        xmlEmbed();
        xml();
        html();
        rtf();
        xls();
        csv();
        odt();
        ods();
        docx();
        xlsx();
        pptx();
    }

    public void compile() throws JRException {
        try {
            JasperCompileManager.compileReportToStream(new FileInputStream(new File("FirstJasper.jrxml")),
                    new FileOutputStream(new File("FirstJasper.jasper")));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void fill() throws JRException
    {
        long start = System.currentTimeMillis();
        //Preparing parameters
        Image image =
                Toolkit.getDefaultToolkit().createImage(
                        JRLoader.loadBytesFromResource("dukesign.jpg")
                );
        MediaTracker traker = new MediaTracker(new Panel());
        traker.addImage(image, 0);
        try
        {
            traker.waitForID(0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ReportTitle", "The First Jasper Report Ever");
        parameters.put("MaxOrderID", new Integer(10500));
        parameters.put("SummaryImage", image);

        JasperFillManager.fillReportToFile("FirstJasper.jasper", parameters);
       
    }


    /**
     *
     */
    public void print() throws JRException
    {
        long start = System.currentTimeMillis();
        JasperPrintManager.printReport("FirstJasper.jrprint", true);
       
    }


    /**
     *
     */
    public void pdf() throws JRException
    {
        long start = System.currentTimeMillis();
        JasperExportManager.exportReportToPdfFile("FirstJasper.jrprint");
       
    }


    /**
     *
     */
    public void pdfa1() throws JRException
    {
        long start = System.currentTimeMillis();

        try{
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));

            JasperPrint jp = (JasperPrint)JRLoader.loadObject(new File("FirstJasper.jrprint"));

            // Exclude transparent images when exporting to PDF; elements marked with the key 'TransparentImage'
            // will be excluded from the exported PDF
            jp.setProperty("net.sf.jasperreports.export.pdf.exclude.key.TransparentImage", null);

            exporter.setExporterInput(new SimpleExporterInput(jp));

            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

            // Include structure tags for PDF/A-1a compliance; unnecessary for PDF/A-1b
            configuration.setTagged(true);

            configuration.setPdfaConformance(PdfaConformanceEnum.PDFA_1A);

            // Uncomment the following line and specify a valid path for the ICC profile
            //			configuration.setIccProfilePath("path/to/ICC/profile");

            exporter.setConfiguration(configuration);
            exporter.exportReport();

            FileOutputStream fos = new FileOutputStream("FirstJasper_pdfa.pdf");
            os.writeTo(fos);
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }

       
    }


    /**
     *
     */
    public void xml() throws JRException
    {
        long start = System.currentTimeMillis();
        JasperExportManager.exportReportToXmlFile("FirstJasper.jrprint", false);
       
    }


    /**
     *
     */
    public void xmlEmbed() throws JRException
    {
        long start = System.currentTimeMillis();
        JasperExportManager.exportReportToXmlFile("FirstJasper.jrprint", true);
       
    }


    /**
     *
     */
    public void html() throws JRException
    {
        long start = System.currentTimeMillis();
        JasperExportManager.exportReportToHtmlFile("FirstJasper.jrprint");
       
    }


    /**
     *
     */
    public void rtf() throws JRException
    {
        long start = System.currentTimeMillis();
        File sourceFile = new File("FirstJasper.jrprint");

        JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

        File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".rtf");

        JRRtfExporter exporter = new JRRtfExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(destFile));

        exporter.exportReport();

       
    }


    /**
     *
     */
    public void xls() throws JRException
    {
        long start = System.currentTimeMillis();
        File sourceFile = new File("FirstJasper.jrprint");

        JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

        File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");

        Map<String, String> dateFormats = new HashMap<String, String>();
        dateFormats.put("EEE, MMM d, yyyy", "ddd, mmm d, yyyy");

        JRXlsExporter exporter = new JRXlsExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setOnePagePerSheet(true);
        configuration.setDetectCellType(true);
        configuration.setFormatPatternsMap(dateFormats);
        exporter.setConfiguration(configuration);

        exporter.exportReport();

       
    }


    /**
     *
     */
    public void xlsMetadata() throws JRException
    {
        long start = System.currentTimeMillis();
        File sourceFile = new File("FirstJasper.jrprint");

        JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

        File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls.metadata.xls");

        Map<String, String> dateFormats = new HashMap<String, String>();
        dateFormats.put("EEE, MMM d, yyyy", "ddd, mmm d, yyyy");

        JRXlsMetadataExporter exporter = new JRXlsMetadataExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
        SimpleXlsMetadataReportConfiguration configuration = new SimpleXlsMetadataReportConfiguration();
        configuration.setOnePagePerSheet(true);
        configuration.setDetectCellType(true);
        configuration.setFormatPatternsMap(dateFormats);
        exporter.setConfiguration(configuration);

        exporter.exportReport();

       
    }


    /**
     *
     */
    public void jsonMetadata() throws JRException
    {
        long start = System.currentTimeMillis();
        File sourceFile = new File("FirstJasper.jrprint");

        JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

        File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".json");

        JsonMetadataExporter exporter = new JsonMetadataExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(destFile));

        exporter.exportReport();

       
    }


    /**
     *
     */
    public void csv() throws JRException
    {
        long start = System.currentTimeMillis();
        File sourceFile = new File("FirstJasper.jrprint");

        JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

        File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".csv");

        JRCsvExporter exporter = new JRCsvExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(destFile));

        exporter.exportReport();

       
    }


    /**
     *
     */
    public void csvMetadata() throws JRException
    {
        long start = System.currentTimeMillis();
        File sourceFile = new File("FirstJasper.jrprint");

        JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

        File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".metadata.csv");

        JRCsvMetadataExporter exporter = new JRCsvMetadataExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(destFile));

        exporter.exportReport();

       
    }


    /**
     *
     */
    public void odt() throws JRException
    {
        long start = System.currentTimeMillis();
        File sourceFile = new File("FirstJasper.jrprint");

        JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

        File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".odt");

        JROdtExporter exporter = new JROdtExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));

        exporter.exportReport();

       
    }


    /**
     *
     */
    public void ods() throws JRException
    {
        long start = System.currentTimeMillis();
        File sourceFile = new File("FirstJasper.jrprint");

        JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

        File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".ods");

        JROdsExporter exporter = new JROdsExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));

        exporter.exportReport();

       
    }


    /**
     *
     */
    public void docx() throws JRException
    {
        long start = System.currentTimeMillis();
        File sourceFile = new File("FirstJasper.jrprint");

        JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

        File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".docx");

        JRDocxExporter exporter = new JRDocxExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));

        exporter.exportReport();

       
    }


    /**
     *
     */
    public void xlsx() throws JRException
    {
        long start = System.currentTimeMillis();
        File sourceFile = new File("FirstJasper.jrprint");

        JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

        File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xlsx");

        Map<String, String> dateFormats = new HashMap<String, String>();
        dateFormats.put("EEE, MMM d, yyyy", "ddd, mmm d, yyyy");

        JRXlsxExporter exporter = new JRXlsxExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setDetectCellType(true);
        configuration.setFormatPatternsMap(dateFormats);
        exporter.setConfiguration(configuration);

        exporter.exportReport();

       
    }


    /**
     *
     */
    public void pptx() throws JRException
    {
        long start = System.currentTimeMillis();
        File sourceFile = new File("FirstJasper.jrprint");

        JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

        File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".pptx");

        JRPptxExporter exporter = new JRPptxExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));

        exporter.exportReport();

       
    }


    /**
     *
     */
    public void run() throws JRException
    {
        long start = System.currentTimeMillis();
        //Preparing parameters
        Image image = Toolkit.getDefaultToolkit().createImage("dukesign.jpg");
        MediaTracker traker = new MediaTracker(new Panel());
        traker.addImage(image, 0);
        try
        {
            traker.waitForID(0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ReportTitle", "The First Jasper Report Ever");
        parameters.put("MaxOrderID", new Integer(10500));
        parameters.put("SummaryImage", image);

        JasperRunManager.runReportToPdfFile("FirstJasper.jasper", parameters, getDemoHsqldbConnection());
       
    }

}