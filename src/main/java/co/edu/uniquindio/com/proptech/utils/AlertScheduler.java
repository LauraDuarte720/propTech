package co.edu.uniquindio.com.proptech.utils;

import co.edu.uniquindio.com.proptech.domain.model.BasicAlert;
import co.edu.uniquindio.com.proptech.services.AbnormalAlertService;
import co.edu.uniquindio.com.proptech.services.BasicAlertService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AlertScheduler {

    private final BasicAlertService alertService;
    private final AbnormalAlertService abnormalAlertService;

    public AlertScheduler(BasicAlertService alertService, AbnormalAlertService abnormalAlertService) {
        this.alertService = alertService;
        this.abnormalAlertService = abnormalAlertService;
    }

    @Scheduled(cron = "0 0 8 * * *") // Todos los días a las 8am
    public void generateDailyAlerts() {
        alertService.generateAllAlerts();
        abnormalAlertService.runAllDetectors();
    }
}