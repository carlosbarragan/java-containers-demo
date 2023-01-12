# Gatling Load Test

To execute the load test, start the app you want to test and then simply execute

1000 parallel User each 100 Requests

```shell
./gradlew gatlingRun -DparallelUsers=1000 -DnumberOfRequests=100
```

## Results

all gatlin load tests are executed 3 times, then I took the highest mean request value.

| -                       | Requests per Second | start time milliseconds |
|:------------------------|:-------------------:|:-----------------------:| 
| Spring Boot             |        3125         |          8143           |
| Quarkus                 |        5555         |          2598           |
| Micronaut               |        7692         |          2888           |
| Spring Boot V3          |        2564         |          7354           |
| Spring Boot V3 Reactive |        3571         |          6715           |
| Minimal                 |        6666         |           996           |
| Vert.X                  |        6250         |           777           |


Disclaimer: All results are executed against minimal basic apps without special configuration.
You can get better results by fine tuning it, for example: increase http thread pool size, active db connections and so on