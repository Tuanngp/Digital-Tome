package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.dto.paymentResponse.PaymentDTOResponse;
import com.fpt.swp391.group6.DigitalTome.dto.paymentResponse.PaymentPageDTOResponse;
import com.fpt.swp391.group6.DigitalTome.entity.PaymentEntity;
import com.fpt.swp391.group6.DigitalTome.repository.PaymentRepository;
import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
import com.fpt.swp391.group6.DigitalTome.utils.PaymentMapper;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaypalService {

    private final APIContext apiContext;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public Payment createPayment(
            Double total,               // Tổng tiền
            String currency,            // Loại tiền
            String method,              // Phương thức thanh toán: Paypal
            String intent,              // Mục đích thanh toán
            String description,         // Mô tả thanh toán, nội dung ck
            String cancelUrl,           // Url hủy giao dịch
            String successUrl           // Url thanh toán thành công
    ) throws PayPalRESTException {
        // Amount là đối tượng trong thư viện Paypal, đại diện cho số tiền cần thanh toán và loại tiền tệ
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", total)); // 9.99$ - 9,99€


        // Transaction đại diện cho giao dịch thanh toán
        Transaction transaction = new Transaction();
        transaction.setDescription(description); // mô tả hay nội dung chuyển khoản
        transaction.setAmount(amount);           // Số tiền giao dịch là bao nhiêu ?


        // Tạo danh sách Transaction, để lưu những transaction đã tạo
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);


        // Payer: xác định phương thức thanh toán
        Payer payer = new Payer();
        payer.setPaymentMethod(method);


        // Cấu hình Thanh toán
        Payment payment = new Payment();
        payment.setIntent(intent);  // mục đích thanh toán
        payment.setPayer(payer);    // Thông tin về người thanh toán
        payment.setTransactions(transactions); // set giao dịch nào để thanh toán


        // RedirectUrls: đối tượng dùng để chuyển hướng khi thực hiện giao dịch
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);  // thất bại
        redirectUrls.setReturnUrl(successUrl); // thành công

        payment.setRedirectUrls(redirectUrls);

        // gửi yêu cầu tạo thanh toán tới PayPal API, sử dụng đối tượng apiContext đã được cấu hình trước đó.
        //  Kết quả trả về từ phương thức create là một Payment object mới được tạo thành công và đã được cấu hình sẵn
        // để thực hiện giao dịch thanh toán
        return payment.create(apiContext);
    }


    // executePayment:  sử dụng để thực hiện một giao dịch thanh toán đã được tạo trước đó trong hệ thống PayPal.

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        // Tạo một đối tượng Payment mới
        Payment payment = new Payment();
        // Đặt ID của Payment bằng ID của thanh toán đã được tạo trước đó
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        // Đặt ID của người thanh toán cho PaymentExecution. ID này xác định người dùng PayPal mà bạn muốn thực hiện giao dịch thanh toán.
        paymentExecution.setPayerId(payerId);
        // Gửi yêu cầu thực hiện thanh toán tới PayPal API
        // Sử dụng đối tượng APIContext và đối tượng PaymentExecution đã được tạo trước đó
        // Kết quả trả về từ phương thức execute là một Payment object đại diện cho kết quả của giao dịch
        return payment.execute(apiContext, paymentExecution);
    }

    public PaymentPageDTOResponse searchPaymentsByAccountIdAndDateRange(Long accountId, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);

        Page<PaymentEntity> paymentsPage = paymentRepository.findPaymentsByAccountIdAndDateRange(accountId, startDateTime, endDateTime, pageable);

        List<PaymentDTOResponse> payments = PaymentMapper.toPaymentDTOList(paymentsPage.getContent());

        return PaymentPageDTOResponse.builder()
                .payments(payments)
                .totalPages(paymentsPage.getTotalPages())
                .currentPage(page)
                .build();
    }
}
