package com.zb.techmission.controller;

import com.zb.techmission.entity.Reservation;
import com.zb.techmission.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @DeleteMapping("/{reservationId}")
    public String cancelReservation(@PathVariable Long reservationId) {
        return reservationService.cancelReservation(reservationId);
    }
}
