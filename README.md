# client-ledger-core-db: MariaDB-based consumer to demonstrate competing consumer in event sourcing
## What does it do?
client-ledger-core-db is Part 2 of 3 Part demo of event sourcing over common web/mobile architecture with commoditized software.  This code base relies on MariaDB, which must be installed before running this demo.

## Quick Start
### Step 1
Log in MariaDB to show the database command line tool.
```
mysql -u username -p password
```

### Step 2
Go to the database script folder.
```
cd database
ls
```
Now you can see the two scripts:
* schema_and_data.sql: This is for setting up the tables and sourcing empty states.
* show_result.sql: This is for showing the state during event sourcing, and printing out the log as well.

### Step 3
In the database command line tool, input
```
source {Path}/schema_and_data.sql
```
Then you can start the spring boot app.

### Step 4
During event sourcing, in the database command line tool, input repeatedly
``` 
source {Path}/show_result.sql
``` 
to show the result of states up to the high water mark.

