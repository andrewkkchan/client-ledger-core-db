package com.industrieit.ledger.clientledger.core.db.controller;

import com.industrieit.ledger.clientledger.core.db.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.db.entity.TransactionResult;
import com.industrieit.ledger.clientledger.core.db.repository.TransactionEventRepository;
import com.industrieit.ledger.clientledger.core.db.repository.TransactionResultRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST Controller which is exclusively allowed to POST on the Ledger through creating and enqueuing {@link TransactionEvent}
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionEventRepository transactionEventRepository;
    private final TransactionResultRepository transactionResultRepository;

    public TransactionController(TransactionEventRepository transactionEventRepository, TransactionResultRepository transactionResultRepository) {
        this.transactionEventRepository = transactionEventRepository;
        this.transactionResultRepository = transactionResultRepository;
    }

    /**
     * GET one {@link TransactionResult} based on request ID
     *
     * @param requestId ID which uniquely identifies the {@link TransactionResult}
     * @return {@link TransactionResult}
     */
    @GetMapping(value = "/result/event/{requestId}",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public TransactionResult getResult(@PathVariable String requestId) {
        Optional<TransactionResult> byId = transactionResultRepository.findByRequestId(requestId);
        return byId.orElse(null);
    }

    @GetMapping(value = "/result/current",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public TransactionResult getLastResult() {
        Optional<TransactionResult> byId = transactionResultRepository.findTop1ByOrderByKafkaOffsetDesc();
        return byId.orElse(null);
    }

    /**
     * GET all {@link TransactionResult} produced by the Ledger
     *
     * @return all {@link TransactionResult}
     */
    @GetMapping(value = "/result",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public Iterable<TransactionResult> getAllResult() {
        return transactionResultRepository.findAll();
    }

    /**
     * GET one {@link TransactionEvent} based on request ID
     *
     * @param id ID which uniquely identifies the {@link TransactionEvent}
     * @return {@link TransactionEvent}
     */
    @GetMapping(value = "/event/{id}",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public TransactionEvent getEvent(@PathVariable String id) {
        Optional<TransactionEvent> byId = transactionEventRepository.findById(id);
        return byId.orElse(null);
    }

    @GetMapping(value = "/event/current",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public TransactionEvent getLastEvent() {
        Optional<TransactionEvent> byId = transactionEventRepository.findTop1ByOrderByKafkaOffsetDesc();
        return byId.orElse(null);
    }

    /**
     * GET all {@link TransactionEvent} enqueued for the Ledger
     *
     * @return all {@link TransactionEvent}
     */
    @GetMapping(value = "/event",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public Iterable<TransactionEvent> getAllEvent() {
        return transactionEventRepository.findAll();
    }
}
