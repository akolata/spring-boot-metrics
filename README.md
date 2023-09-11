# Metrics

This is a simple Spring Boot application created to demonstrate the usage of 
Micrometer, Prometheus and Grafana.

Key dependencies:
* `micrometer-registry-prometheus` which gives us API for metrics
* `spring-boot-starter-actuator` which exposes `/actuator/prometheus` endpoint 

https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#actuator.metrics

## Docker
Go to `./docker/prometheus` directory and edit `prometheus.yml` file.  
Update `scrape_configs[].static_configs[].targets` list under `books_app` job  
with your IP address.  
On macOS you can use `ifconfig | grep "inet " | grep -v 127.0.0.1` command.

After that in `./docker` directory run `docker-compose start` to start Prometheus and Grafana running in Docker.

## Prometheus
* Prometheus http://localhost:9090
* Configured job's targets http://localhost:9090/targets  

In Prometheus you can see the configured targets, browse metrics and run some queries.

## Grafana
http://localhost:3000

Login credentials are admin/admin. After log in you can either set a new password or skip such step.  

### Dashboards
You can browse available dashboards under this address: https://grafana.com/grafana/dashboards  
JVM (Micrometer) dashboards: https://grafana.com/grafana/dashboards/4701-jvm-micrometer/


## Service traffic simulation using Hey tool
You can use `hey` program to send requests to the service.  
https://github.com/rakyll/hey

```shell
hey -c 10 -n 50 "http://localhost:8080/api/test"
hey -c 10 -n 20 "http://localhost:8080/api/books"
hey -c 2 -n 15 "http://localhost:8080/api/books?title=Clean%20code"
hey -c 10 -n 50 "http://localhost:8080/api/books?title=Fundamental%20Algorithms"
hey -c 10 -n 50 "http://localhost:8080/api/books?title=Domain%20Driven%20Design"
```

## Example metrics/queries to monitor

Custom metrics:
* `books_service_books_in_store_count` - gauge - a current number of books in store
* `books_service_books_search_by_title` - timer showing how long does it take to search for books
* `books_service_api_books_get_count` - counter - a number of requests to `GET /api/books` endpoint

### HTTP graph
* avg response time for statuses other than 5..: `sum(rate(http_server_requests_seconds_sum{status!~"5.."}[60s])) / sum(rate(http_server_requests_seconds_count{status!~"5.."}[60s]))`, label `avg`
* the longest response time for statues other than 5..: `max(http_server_requests_seconds_max{status!~"5.."})`, label `max`
* 0.95 quantile `histogram_quantile(0.95, sum by(le) (rate(http_server_requests_seconds_bucket[1m])))`, label `p95`

### HTTP requests latency by title graph
* `rate(books_service_books_search_by_title_seconds_sum[1m]) / rate(books_service_books_search_by_title_seconds_count[1m])`
* label `{{title}}`

### HTTP requests per second by endpoint graph
* `rate(http_server_requests_seconds_count[60s])`
* label `{{uri}}`

### Number of books in the system gauge
* `books_service_books_in_store_count`

### Number of books requests by title graph
* `books_service_api_books_get_count_total`, label `{{title}}`

### Not found books graph
* `books_service_api_books_get_count_total{matching_books="0"}`, label `{{title}}`
