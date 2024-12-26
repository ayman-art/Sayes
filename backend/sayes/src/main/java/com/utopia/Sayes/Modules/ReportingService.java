package com.utopia.Sayes.Modules;

import com.utopia.Sayes.Models.Log;
import com.utopia.Sayes.Repo.LogDAO;
import com.utopia.Sayes.Repo.LotDAO;
import com.utopia.Sayes.Repo.SpotDAO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
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
    public ByteArrayOutputStream generateTopUsersReport() throws Exception {
        List<Map<String, Object>> topUsersData = logDAO.getTopUsers();
        String reportPath = "src/main/resources/reports/TopUsersReport.jrxml";
        JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(topUsersData);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        return outputStream;
    }
    public ByteArrayOutputStream generateTopLotsReport() throws Exception {
        List<Map<String, Object>> topLotsData = lotDAO.getTopLots();
        String reportPath = "src/main/resources/reports/TopLotsReport.jrxml";
        JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(topLotsData);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        return outputStream;
    }
    public List<Map<String , Object>> getTopUsers() throws Exception {
        try {
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
