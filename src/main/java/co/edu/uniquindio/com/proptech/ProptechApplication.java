package co.edu.uniquindio.com.proptech;


import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
@Log
public class ProptechApplication {


    public ProptechApplication() {
    }


    public static void main(String[] args) {
        SpringApplication.run(ProptechApplication.class, args);
        Runnable a;
    }

}