package com.ghostchu.btn.btnserver.recentactivities;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;

@Controller()
@RequestMapping("/recentActivities")
public class RecentActivitiesController {
    private final RecentActivitiesService recentActivitiesService;

    public RecentActivitiesController(RecentActivitiesService recentActivitiesService) {
        this.recentActivitiesService = recentActivitiesService;
    }

    @GetMapping("")
    public String viewRecentActivities(Model model) throws SQLException {
        model.addAttribute("banRecords", recentActivitiesService.getBanActivities());
        return "recentActivities/index";
    }
}
