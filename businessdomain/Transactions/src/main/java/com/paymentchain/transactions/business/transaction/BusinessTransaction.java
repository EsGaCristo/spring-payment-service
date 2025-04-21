package com.paymentchain.transactions.business.transaction;

import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymentchain.transactions.entities.Transaction;
import com.paymentchain.transactions.exception.BusinessRuleException;
import com.paymentchain.transactions.respository.TransactionRepository;

import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import reactor.netty.http.client.HttpClient;

@Service
public class BusinessTransaction {

    @Autowired
    private WebClient.Builder webClienBuilder;

    @Autowired
    TransactionRepository transactionRepository;

    HttpClient client = HttpClient.create()
            // Connection Timeout: is a period within which a connection between a client
            // and a server must be established
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            // Response Timeout: The maximun time we wait to receive a response after
            // sending a request
            .responseTimeout(Duration.ofSeconds(1))
            // Read and Write Timeout: A read timeout occurs when no data was read within a
            // certain
            // period of time, while the write timeout when a write operation cannot finish
            // at a specific time
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

    // Las transacciones con valor positivo (mayor que 0) significan abonos en la
    // cuenta y las
    // transacciones negativas (menor que 0) significan retiros de la cuenta, por
    // tanto el
    // saldo de la cuenta seria la diferencia de sumar las transacciones con valor
    // positivo y
    // restarles la suma de las tranacciones con valor negativo.

    public Transaction post(@RequestBody Transaction input) throws BusinessRuleException, UnknownHostException {
        Double balance = getBalanceAccount(input.getIbanAccount());
        double amount = input.getAmount();
        double fee = amount < 0 ? (amount*(0.0098)) : 0 ;//Comision por si es retiro

        

        if(balance == null){
            BusinessRuleException businessRuleException = new BusinessRuleException(
                "1012",
                "Numero de cuenta no existe",
                HttpStatus.NOT_FOUND);
                throw businessRuleException;
        }

        if(amount<0 && (balance < (Math.abs(amount)+fee))){
            BusinessRuleException businessRuleException = new BusinessRuleException(
                "1013",
                "Error de validacion, el monto capturado es mayor al saldo disponible en la cuenta iban:"+input.getIbanAccount(),
                HttpStatus.PRECONDITION_FAILED);
                throw businessRuleException;
        }
        if(amount<0){
            input.setAmount(amount+fee);
            input.setFee(fee);
            input.setStatus("RETIRO");
        }else{
            input.setStatus("DEPOSITO");
        }

        Transaction save = transactionRepository.save(input);
        return save;
    }

    private Double getBalanceAccount(String iban) throws UnknownHostException{
        double balance=0;
        try {
        WebClient build =  webClienBuilder.clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://localhost:8081/business/v2/customer")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE )
                    .build();
        JsonNode block = build.method(HttpMethod.GET)
                        .uri(uriBuilder -> uriBuilder
                        .path("/iban")
                        .queryParam("iban", iban)
                        .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        balance = block.get("balance").asDouble();   

        
        } catch (WebClientResponseException ex) {
            if(ex.getStatusCode() == HttpStatus.NOT_FOUND){
                return null;
            }else{
                throw new UnknownHostException(ex.getMessage());
            }
        }
        return balance;
    }
}
