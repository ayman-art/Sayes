package com.utopia.Sayes.Modules;

import com.utopia.Sayes.Models.Log;
import com.utopia.Sayes.Repo.LogDAO;
import com.utopia.Sayes.Repo.LotDAO;
import com.utopia.Sayes.Repo.SpotDAO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportingService {
    @Autowired
    LogDAO logDAO;

    @Autowired
    SpotDAO spotDAO;

    @Autowired
    LotDAO lotDAO;

    public List<Log> getLogs() throws Exception {
        try {
            return logDAO.getlogs();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public double getOccupancyRate(long lotId) throws Exception {
        try {
            return spotDAO.getOccupancyRate(lotId);
        }
        catch (Exception e){
          throw new Exception(e.getMessage());
        }
    }
    public void generateTopUsersReport() throws Exception {
        // Fetch data from the service method
        List<Map<String, Object>> topUsersData = logDAO.getTopUsers();

        // Load the .jrxml file (you can place it in your resources folder or load it dynamically)
        String reportPath = "D:\\Sayes\\backend\\sayes\\src\\main\\resources\\reports\\TopUsersReport.jrxml";
        JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

        // Create a data source from the fetched data
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(topUsersData);

        // Fill the report with the data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

        // Export the report to a desired format (e.g., PDF, HTML, etc.)
        JasperExportManager.exportReportToPdfFile(jasperPrint, "TopUsersReport.pdf");

        // Or you can export to other formats like HTML, CSV, etc.
        // JasperExportManager.exportReportToHtmlFile(jasperPrint, "TopUsersReport.html");
    }
    public List<Map<String , Object>> getTopUsers() throws Exception {
        try {
            //generateTopUsersReport();
            return logDAO.getTopUsers();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Map<String , Object>> getTopLots() throws Exception {
        try {
            return lotDAO.getTopLots();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
