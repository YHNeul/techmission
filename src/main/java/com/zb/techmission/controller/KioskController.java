package com.zb.techmission.controller;

import com.zb.techmission.entity.Reservation;
import com.zb.techmission.entity.Store;
import com.zb.techmission.service.KioskService;
import com.zb.techmission.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/kiosk")
public class KioskController {
    private final KioskService kioskService;
    private final StoreService storeService;

    public KioskController(KioskService kioskService, StoreService storeService) {
        this.kioskService = kioskService;
        this.storeService = storeService;
    }

    // 키오스크 메인 화면 (매장 선택)
    @GetMapping
    public String kioskMain(Model model) {
        model.addAttribute("stores", storeService.getAllStores());
        return "kiosk/main";
    }

    // 특정 매장의 키오스크 화면
    @GetMapping("/{storeId}")
    public String kioskStore(@PathVariable Long storeId, Model model) {
        Store store = storeService.getStoreById(storeId);
        if (store == null) {
            return "redirect:/kiosk";
        }

        model.addAttribute("store", store);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("currentTime", LocalTime.now());
        return "kiosk/store";
    }

    // 예약 검색 폼 처리
    @PostMapping("/{storeId}/check-reservation")
    public String checkReservation(@PathVariable Long storeId,
                                   @RequestParam String username,
                                   RedirectAttributes redirectAttributes) {
        try {
            Reservation reservation = kioskService.findActiveReservation(storeId, username);
            return "redirect:/kiosk/" + storeId + "/confirm/" + reservation.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/kiosk/" + storeId;
        }
    }

    // 예약 확인 및 체크인 화면
    @GetMapping("/{storeId}/confirm/{reservationId}")
    public String confirmReservation(@PathVariable Long storeId,
                                     @PathVariable Long reservationId,
                                     Model model) {
        Store store = storeService.getStoreById(storeId);
        Reservation reservation = kioskService.getReservationById(reservationId);

        if (store == null || reservation == null || !reservation.getStore().getId().equals(storeId)) {
            return "redirect:/kiosk/" + storeId;
        }

        boolean canCheckIn = kioskService.canCheckIn(reservation);
        model.addAttribute("store", store);
        model.addAttribute("reservation", reservation);
        model.addAttribute("canCheckIn", canCheckIn);
        model.addAttribute("currentTime", LocalTime.now());
        return "kiosk/confirm";
    }

    // 체크인 처리
    @PostMapping("/{storeId}/checkin/{reservationId}")
    public String processCheckin(@PathVariable Long storeId,
                                 @PathVariable Long reservationId,
                                 RedirectAttributes redirectAttributes) {
        try {
            kioskService.checkIn(reservationId);
            redirectAttributes.addFlashAttribute("message", "방문 확인이 완료되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/kiosk/" + storeId + "/success";
    }

    // 체크인 성공 화면
    @GetMapping("/{storeId}/success")
    public String checkInSuccess(@PathVariable Long storeId, Model model) {
        Store store = storeService.getStoreById(storeId);
        if (store == null) {
            return "redirect:/kiosk";
        }

        model.addAttribute("store", store);
        return "kiosk/success";
    }
}