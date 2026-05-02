package com.diamondbarbershop.apibarbershop.services;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JasperService {

    private final DataSource dataSource;

    public byte[] exportarHorario(Date fechaInicio, Date fechaFin) throws Exception {

        InputStream reportStream = new ClassPathResource("reports/HorarioBarberoReporte.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        //parámetros
        Map<String,Object> params = new HashMap<>();
        params.put("FechaInicio",fechaInicio);
        params.put("FechaFin",fechaFin);
        //Conexión a la base de datos
        Connection conn = DataSourceUtils.getConnection(dataSource);
        //Llenar el reporte
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,params,conn);
        //Exportar a pdf
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

}
