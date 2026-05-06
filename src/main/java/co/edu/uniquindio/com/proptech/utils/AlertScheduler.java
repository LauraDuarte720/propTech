package co.edu.uniquindio.com.proptech.utils;

import co.edu.uniquindio.com.proptech.domain.model.BasicAlert;
import co.edu.uniquindio.com.proptech.services.BasicAlertService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AlertScheduler {

    private final BasicAlertService alertService;

    public AlertScheduler(BasicAlertService alertService) {
        this.alertService = alertService;
    }

    @Scheduled(cron = "0 0 8 * * *") // Todos los días a las 8am
    public void generateDailyAlerts() {
        alertService.generateAllAlerts();
    }
}