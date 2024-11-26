package com.mss;

import com.mss.entity.ChargeConfig;
import com.mss.entity.Keyword;
import com.mss.repository.ChargeConfigRepository;
import com.mss.repository.KeywordRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@SpringBootApplication
public class MssContentServiceApplication implements CommandLineRunner {

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private ChargeConfigRepository chargeConfigRepository;

    public static void main(String[] args) {
        SpringApplication.run(MssContentServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) throws Exception {
        saveKeyword();
        saveChargeConfig();
    }

    private void saveKeyword() {
        Keyword apple = new Keyword("APPLE", "2024-11-20 00:01:00.000", "2024-11-20 00:01:00.000");
        Keyword google = new Keyword("GOOGLE", "2024-11-20 00:01:00.000", "2024-11-20 00:01:00.000");
        Keyword microsoft = new Keyword("MICROSOFT", "2024-11-20 00:01:00.000", "2024-11-20 00:01:00.000");
        Keyword facebook = new Keyword("FACEBOOK", "2024-11-20 00:01:00.000", "2024-11-20 00:01:00.000");
        Keyword netflix = new Keyword("NETFLIX", "2024-11-20 00:01:00.000", "2024-11-20 00:01:00.000");
        Keyword amazon = new Keyword("AMAZON", "2024-11-20 00:01:00.000", "2024-11-20 00:01:00.000");
        Keyword nvidia = new Keyword("NVIDIA", "2024-11-20 00:01:00.000", "2024-11-20 00:01:00.000");
        Keyword intel = new Keyword("INTEL", "2024-11-20 00:01:00.000", "2024-11-20 00:01:00.000");
        Keyword amd = new Keyword("AMD", "2024-11-20 00:01:00.000", "2024-11-20 00:01:00.000");
        Keyword ibm = new Keyword("IBM", "2024-11-20 00:01:00.000", "2024-11-20 00:01:00.000");

        for (Keyword keyword : Arrays.asList(apple, google, microsoft, facebook, netflix, amazon, nvidia, intel, amd, ibm)) {
            try {
                keywordRepository.save(keyword);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void saveChargeConfig() {
        ChargeConfig banglalink = new ChargeConfig("BANGLALINK", "BL914679", "2024-11-20 00:01:00.000", "2024-11-20 00:01:00.000");
        ChargeConfig grameenphone = new ChargeConfig("GRAMEENPHONE", "GP714363", "2024-11-20 00:01:00.000", "2024-11-20 00:01:00.000");
        ChargeConfig robi = new ChargeConfig("ROBI", "RO874556", "2024-11-20 00:01:00.000", "2024-11-20 00:01:00.000");
        ChargeConfig teletalk = new ChargeConfig("TELETALK", "TL512343", "2024-11-20 00:01:00.000", "2024-11-20 00:01:00.000");
        ChargeConfig airtel = new ChargeConfig("AIRTEL", "AL621124", "2024-11-20 00:01:00.000", "2024-11-20 00:01:00.000");

        for (ChargeConfig chargeConfig : Arrays.asList(banglalink, grameenphone, robi, teletalk, airtel)) {
            try {
                chargeConfigRepository.save(chargeConfig);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }


}
