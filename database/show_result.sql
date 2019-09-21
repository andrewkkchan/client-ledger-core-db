-- show result of the ledger transaction processing
use ledger;
show tables;

-- show mapping of each of the request event to its respective result, calculating the time elapsed in ms, and success/fail
select result.requestId,
       event.createTime                                               as eventTime,
       event.kafkaOffset                                              as kafkaOffset,
       event.kafkaPartition                                           as kafkaPartition,
       result.createTime                                              as resultTime,
       format((result.createTime - event.createTime) * 1000, '#,###') as msElapsed,
       result.success,
       event.type
from transaction_result result
       join transaction_event event on result.requestId like event.id
order by result.createTime;

-- show final account balances after snapshot
select *
from account;

