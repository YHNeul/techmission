package com.zb.techmission.controller;

import com.zb.techmission.entity.Reservation;
import com.zb.techmission.entity.Role;
import com.zb.techmission.entity.Store;
import com.zb.techmission.service.ReservationService;
import com.zb.techmission.service.StoreService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final StoreService storeService;

    public ReservationController(ReservationService reservationService, StoreService storeService) {
        this.reservationService = reservationService;
        this.storeService = storeService;
    }

    // 예약 폼 페이지
    @GetMapping("/create")
    public String showReservationForm(@RequestParam Long storeId,
                                      @RequestParam String username,
                                      @RequestParam String role,
                                      Model model) {
        // OWNER는 예약할 수 없음
        if (role.equals("OWNER")) {
            return "redirect:/stores/" + storeId + "?username=" + username + "&role=" + role;
        }

        Store store = storeService.getStoreById(storeId);
        if (store == null) {
            return "redirect:/stores?username=" + username + "&role=" + role;
        }

        model.addAttribute("store", store);
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("today", LocalDate.now());

        return "reservation_form";
    }

    // 예약 처리
    @PostMapping("/create")
    public String makeReservation(@RequestParam Long storeId,
                                  @RequestParam String username,
                                  @RequestParam String role,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                                  RedirectAttributes redirectAttributes) {
        try {
            // 예약 생성
            reservationService.makeReservation(storeId, username, date, time);
            redirectAttributes.addFlashAttribute("message", "예약이 성공적으로 완료되었습니다.");
            return "redirect:/reservations/my?username=" + username + "&role=" + role;
        } catch (Exception e) {
            // 예약 실패 시 에러 메시지
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reservations/create?storeId=" + storeId + "&username=" + username + "&role=" + role;
        }
    }

    // 내 예약 목록
    @GetMapping("/my")
    public String myReservations(@RequestParam String username,
                                 @RequestParam String role,
                                 Model model) {
        List<Reservation> reservations = reservationService.getUserReservations(username);

        model.addAttribute("reservations", reservations);
        model.addAttribute("username", username);
        model.addAttribute("role", role);

        return "my_reservations";
    }

    // 예약 취소
    @PostMapping("/cancel/{id}")
    public String cancelReservation(@PathVariable Long id,
                                    @RequestParam String username,
                                    @RequestParam String role) {
        try {
            reservationService.cancelReservation(id, username);
            return "redirect:/reservations/my?username=" + username + "&role=" + role;
        } catch (Exception e) {
            return "redirect:/reservations/my?username=" + username + "&role=" + role + "&error=" + e.getMessage();
        }
    }

    // 매장 예약 목록 (OWNER용)
    @GetMapping("/store/{storeId}")
    public String storeReservations(@PathVariable Long storeId,
                                    @RequestParam String username,
                                    @RequestParam String role,
                                    Model model) {
        // USER는 매장 예약 목록을 볼 수 없음
        if (!role.equals("OWNER")) {
            return "redirect:/stores?username=" + username + "&role=" + role;
        }

        Store store = storeService.getStoreById(storeId);
        if (store == null) {
            return "redirect:/stores?username=" + username + "&role=" + role;
        }

        // 해당 매장의 점주인지 확인
        if (!store.getOwner().getUsername().equals(username)) {
            return "redirect:/stores?username=" + username + "&role=" + role;
        }

        List<Reservation> reservations = reservationService.getStoreReservations(storeId);

        model.addAttribute("store", store);
        model.addAttribute("reservations", reservations);
        model.addAttribute("username", username);
        model.addAttribute("role", role);

        return "store_reservations";
    }
}