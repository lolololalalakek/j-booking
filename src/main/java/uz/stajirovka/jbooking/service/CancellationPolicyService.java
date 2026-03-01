package uz.stajirovka.jbooking.service;

import uz.stajirovka.jbooking.dto.response.CancellationPolicyInfoResponse;

public interface CancellationPolicyService {

    CancellationPolicyInfoResponse getCancellationPolicyInfo(long bookingId);
}