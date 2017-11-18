# changelog
## v0.0.3
- add worker enabled config

## v0.0.2
- add jobid for job event
- add event store : jdbc\mongo\elasticsearch

## v0.0.1
- add woker activator
- add redis pooled worker impl
- add robust worker pool
- support disable client config
- adapt event to spring event
- support config poll interval
- wrapper client and add submit and remove job event

## todo
- provide event store
- add jesque web management
- add jesque error monitor and alert

## jesque version
2.1.0版本兼容pika,因为pika不支持script
