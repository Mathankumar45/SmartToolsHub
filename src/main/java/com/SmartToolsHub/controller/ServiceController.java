package com.SmartToolsHub.controller;

import com.SmartToolsHub.dto.PlanDto;
import com.SmartToolsHub.dto.ProviderDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    // returns category list
    @GetMapping("/categories")
    public List<String> categories() {
        return List.of("mobile","dth","electricity","water","gas","broadband","fastag","postpaid","insurance","subscriptions");
    }

    // provider list for manual mode (static)
    @GetMapping("/providers")
    public List<ProviderDto> providers(@RequestParam String service) {
        service = service == null ? "" : service.toLowerCase();
        if ("mobile".equals(service)) {
            return List.of(
                new ProviderDto("AIRTEL", "AIRTEL"),
                new ProviderDto("JIO", "JIO"),
                new ProviderDto("VI", "VI"),
                new ProviderDto("BSNL", "BSNL")
            );
        } else if ("dth".equals(service)) {
            return List.of(
                new ProviderDto("TATAPLAY", "Tata Play"),
                new ProviderDto("AIRTELDTH", "Airtel DTH"),
                new ProviderDto("DISHTV", "DishTV")
            );
        } else if ("electricity".equals(service)) {
            return List.of(
                new ProviderDto("GENERIC_ELECTRIC", "Electricity Board (Manual)")
            );
        }
        // default fallback
        return List.of(new ProviderDto("GENERIC", "Generic Provider (Manual)"));
    }

    // simple static plans for providers (manual mode)
    @GetMapping("/plans")
    public List<PlanDto> plans(@RequestParam String provider, @RequestParam(required=false) String circle) {
        provider = provider == null ? "" : provider.toUpperCase();

        if ("AIRTEL".equals(provider)) {
            return List.of(new PlanDto("Airtel ₹199 - 28 Days",199), new PlanDto("Airtel ₹399 - 56 Days",399));
        }
        if ("JIO".equals(provider)) {
            return List.of(new PlanDto("Jio ₹149 - 28 Days",149), new PlanDto("Jio ₹399 - 56 Days",399));
        }
        if ("VI".equals(provider)) {
            return List.of(new PlanDto("Vi ₹179 - 28 Days",179), new PlanDto("Vi ₹349 - 56 Days",349));
        }
        if ("BSNL".equals(provider)) {
            return List.of(new PlanDto("BSNL ₹149 - 28 Days",149), new PlanDto("BSNL ₹299 - 56 Days",299));
        }
        // fallback empty
        return List.of();
    }
}
