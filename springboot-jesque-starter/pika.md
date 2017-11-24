## bug
```
2017-11-24 15:39:42.229 - WARN [Worker-149 Jesque-2.1.0: RUNNING] c.p.j.component.RedisPooledWorkerImpl    516 : Terminating in response to exception
-
redis.clients.jedis.exceptions.JedisException: Could not return the resource to the pool
	at redis.clients.jedis.JedisPool.returnResource(JedisPool.java:256) ~[jedis-2.9.0.jar:na]
	at redis.clients.jedis.JedisPool.returnResource(JedisPool.java:16) ~[jedis-2.9.0.jar:na]
	at net.greghaines.jesque.utils.PoolUtils.doWorkInPool(PoolUtils.java:54) ~[jesque-2.1.0.jar:na]
	at net.greghaines.jesque.utils.PoolUtils.doWorkInPoolNicely(PoolUtils.java:72) ~[jesque-2.1.0.jar:na]
	at cn.patterncat.jesque.component.RedisPooledWorkerImpl.pop(RedisPooledWorkerImpl.java:462) [classes/:na]
	at cn.patterncat.jesque.component.RedisPooledWorkerImpl.poll(RedisPooledWorkerImpl.java:417) [classes/:na]
	at cn.patterncat.jesque.component.RedisPooledWorkerImpl.run(RedisPooledWorkerImpl.java:171) [classes/:na]
	at java.lang.Thread.run(Thread.java:745) [na:1.8.0_71]
Caused by: redis.clients.jedis.exceptions.JedisDataException: Err unknown or unsupported command 'unwatch'
	at redis.clients.jedis.Protocol.processError(Protocol.java:127) ~[jedis-2.9.0.jar:na]
	at redis.clients.jedis.Protocol.process(Protocol.java:161) ~[jedis-2.9.0.jar:na]
	at redis.clients.jedis.Protocol.read(Protocol.java:215) ~[jedis-2.9.0.jar:na]
	at redis.clients.jedis.Connection.readProtocolWithCheckingBroken(Connection.java:340) ~[jedis-2.9.0.jar:na]
	at redis.clients.jedis.Connection.getStatusCodeReply(Connection.java:239) ~[jedis-2.9.0.jar:na]
	at redis.clients.jedis.BinaryJedis.unwatch(BinaryJedis.java:1802) ~[jedis-2.9.0.jar:na]
	at redis.clients.jedis.BinaryJedis.resetState(BinaryJedis.java:1785) ~[jedis-2.9.0.jar:na]
	at redis.clients.jedis.JedisPool.returnResource(JedisPool.java:252) ~[jedis-2.9.0.jar:na]
	... 7 common frames omitted
```