package com.idealbank.accounts.service.impl;

import com.idealbank.accounts.dto.AccountsDto;
import com.idealbank.accounts.dto.CardsDto;
import com.idealbank.accounts.dto.CustomerDetailsDto;
import com.idealbank.accounts.dto.LoansDto;
import com.idealbank.accounts.entity.Accounts;
import com.idealbank.accounts.entity.Customer;
import com.idealbank.accounts.exception.ResourceNotFoundException;
import com.idealbank.accounts.mapper.AccountsMapper;
import com.idealbank.accounts.mapper.CustomerMapper;
import com.idealbank.accounts.repository.AccountsRepository;
import com.idealbank.accounts.repository.CustomerRepository;
import com.idealbank.accounts.service.ICustomersService;
import com.idealbank.accounts.service.client.CardsFeignClient;
import com.idealbank.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());

        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);

        customerDetailsDto.setLoansDto( loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);

        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;
    }
}
