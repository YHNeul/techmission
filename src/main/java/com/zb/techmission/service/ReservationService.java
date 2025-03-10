package com.zb.techmission.service;

import com.zb.techmission.entity.Reservation;
import com.zb.techmission.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation makeReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getUserReservations(String userId) {
        return reservationRepository.findByUserId(userId);
    }

    public String cancelReservation(Long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isPresent()) {
            reservationRepository.deleteById(reservationId);
            return "Reservation Canceled";
        } else {
            return "Reservation Not Found";
        }
    }
}
