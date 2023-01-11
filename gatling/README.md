# Gatling Load Test

To execute the load test, start the app you want to test and then simply execute

10 parallel User each 10000 Requests

```shell
./gradlew gatlingRun
```

## Results

| -                       | Requests per Second | start time milliseconds |
|:------------------------|:-------------------:|:-----------------------:| 
| Spring Boot             |         476         |          13200          |
| Quarkus                 |         917         |          4700           |
| Micronaut               |        1388         |          4800           |
| Spring Boot V3          |         495         |          12800          |
| Spring Boot V3 Reactive |         625         |          11900          |
| Minimal                 |        1678         |          1236           |
| Vert.X                  |        1960         |          1396           |


Disclaimer: All results are executed against minimal basic apps without special configuration.
You can get better results by fine tuning it, for example: increase http thread pool size, active db connections and so on 